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

public class AddUserAdminController {

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

    private String imagePath;
    private UserService userService;
    private static final String UPLOAD_DIR = "src/main/resources/uploads/";

    @FXML
    public void initialize() {
        userService = new UserService();
        hideError();

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (Exception e) {
            System.err.println("Could not create uploads directory: " + e.getMessage());
        }

        // Set member as default role
        memberCheck.setSelected(true);

        // Add listeners for role checkboxes
        memberCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !adminCheck.isSelected() && !artistCheck.isSelected()) {
                memberCheck.setSelected(true);
            }
        });

        setDefaultImage();
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
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = Paths.get(UPLOAD_DIR, fileName);

                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(new FileInputStream(targetPath.toFile()));
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);

                imagePath = "uploads/" + fileName;
                imageLabel.setText("New image selected");

            } catch (Exception e) {
                e.printStackTrace();
                showError("Error uploading image: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            User user = new User();
            user.setNom(txtnom.getText().trim());
            user.setPrenom(txtprenom.getText().trim());
            user.setEmail(txtmail.getText().trim());

            // Hash the password
            String hashedPassword = BCrypt.hashpw(txtpass.getText(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            // Set roles
            user.setAdmin(adminCheck.isSelected());
            user.setMember(memberCheck.isSelected());
            user.setArtist(artistCheck.isSelected());

            // Set image if uploaded
            if (imagePath != null) {
                user.setImage(imagePath);
            }

            // Create user
            userService.create(user);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("User created successfully!");
            alert.showAndWait();

            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error creating user: " + e.getMessage());
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

        if (txtpass.getText().trim().isEmpty()) {
            showError("Password is required");
            return false;
        }

        if (txtpass.getText().length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }

        // Ensure at least one role is selected
        if (!adminCheck.isSelected() && !memberCheck.isSelected() && !artistCheck.isSelected()) {
            showError("At least one role must be selected");
            return false;
        }

        // Check if email already exists
        if (userService.findByEmail(txtmail.getText().trim()) != null) {
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