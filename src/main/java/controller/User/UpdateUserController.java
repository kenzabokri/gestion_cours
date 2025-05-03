package controller.User;

import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpdateUserController {

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txtprenom;

    @FXML
    private TextField txtmail;

    @FXML
    private PasswordField txtpass;

    @FXML
    private CheckBox adminCheck;

    @FXML
    private CheckBox memberCheck;

    @FXML
    private CheckBox artistCheck;

    @FXML
    private ImageView imageView;

    @FXML
    private Label imageLabel;

    @FXML
    private Label errorLabel;

    private User user;
    private String imagePath;
    private UserService userService;
    private static final String UPLOAD_DIR = "src/main/resources/uploads/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        userService = new UserService();
        hideError();

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (Exception e) {
            System.err.println("Could not create uploads directory: " + e.getMessage());
        }

        memberCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !adminCheck.isSelected() && !artistCheck.isSelected()) {
                memberCheck.setSelected(true);
            }
        });
    }

    public void setUser(User user) {
        this.user = user;

        // Debug output
        System.out.println("Setting user with ID: " + user.getId());

        txtnom.setText(user.getNom());
        txtprenom.setText(user.getPrenom());
        txtmail.setText(user.getEmail());
        txtpass.clear(); // Leave password field empty

        // Set roles
        adminCheck.setSelected(user.isAdmin());
        memberCheck.setSelected(user.isMember());
        artistCheck.setSelected(user.isArtist());

        loadUserImage();
    }
    private void loadUserImage() {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            try {
                File imageFile = new File("src/main/resources/" + user.getImage());
                if (imageFile.exists()) {
                    Image image = new Image(new FileInputStream(imageFile));
                    imageView.setImage(image);
                    this.imagePath = user.getImage();
                } else {
                    setDefaultImage();
                }

                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);

            } catch (Exception e) {
                System.err.println("Could not load user image: " + e.getMessage());
                setDefaultImage();
            }
        } else {
            setDefaultImage();
        }
    }

    private void setDefaultImage() {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default-avatar.jpg"));
            imageView.setImage(defaultImage);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            imageLabel.setText("Default image");
        } catch (Exception e) {
            System.err.println("Could not load default avatar: " + e.getMessage());
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Generate unique filename with timestamp
                String timestamp = LocalDateTime.now().format(formatter).replace(" ", "_").replace(":", "-");
                String fileName = timestamp + "_" + selectedFile.getName();
                Path targetPath = Paths.get(UPLOAD_DIR, fileName);

                // Copy the file
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Update ImageView
                Image image = new Image(targetPath.toUri().toString());
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);

                // Store the relative path
                imagePath = "uploads/" + fileName;
                imageLabel.setText("New image selected");

            } catch (Exception e) {
                e.printStackTrace();
                showError("Error uploading image: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            // Store the current password before updates
            String currentPassword = user.getPassword();

            // Update basic info
            user.setNom(txtnom.getText().trim());
            user.setPrenom(txtprenom.getText().trim());
            user.setEmail(txtmail.getText().trim());

            // Update roles
            user.setAdmin(adminCheck.isSelected());
            user.setMember(memberCheck.isSelected());
            user.setArtist(artistCheck.isSelected());

            // Handle password update
            String newPassword = txtpass.getText().trim();
            if (!newPassword.isEmpty()) {
                // Only update password if a new one is provided
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashedPassword);
            } else {
                // Keep the existing password
                user.setPassword(currentPassword);
            }

            // Update image if changed
            if (imagePath != null && !imagePath.equals(user.getImage())) {
                user.setImage(imagePath);
            }

            // Debug output
            System.out.println("Updating user: " + user.getId());
            System.out.println("Name: " + user.getNom());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Image: " + user.getImage());

            // Save changes
            userService.update(user);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("User updated successfully!");
            alert.showAndWait();

            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error updating user: " + e.getMessage());
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateFields() {
        hideError();

        if (txtnom.getText().trim().isEmpty()) {
            showError("Name is required");
            return false;
        }

        if (txtprenom.getText().trim().isEmpty()) {
            showError("First name is required");
            return false;
        }

        if (txtmail.getText().trim().isEmpty()) {
            showError("Email is required");
            return false;
        }

        if (!txtmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Invalid email format");
            return false;
        }

        // Ensure at least one role is selected
        if (!adminCheck.isSelected() && !memberCheck.isSelected() && !artistCheck.isSelected()) {
            showError("At least one role must be selected");
            return false;
        }

        // Check if email is already used by another user
        User existingUser = userService.findByEmail(txtmail.getText().trim());
        if (existingUser != null && existingUser.getId() != user.getId()) {
            showError("Email already exists");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }

    private void closeWindow() {
        Stage stage = (Stage) txtnom.getScene().getWindow();
        stage.close();
    }
}