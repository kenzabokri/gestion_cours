package controller.User;

import entity.User;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import service.UserService;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UpdateProfileViewController {

    @FXML private TextField prenomField;
    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView profileImageView;
    @FXML private Label errorLabel;

    private User currentUser;
    private UserService userService;
    private String newImagePath;
    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        userService = new UserService();
    }

    public void setUser(User user) {
        this.currentUser = user;

        // Populate fields
        prenomField.setText(user.getPrenom());
        nomField.setText(user.getNom());
        emailField.setText(user.getEmail());

        // Load current image
        try {
            String imagePath = user.getImage();
            Image userImg;
            if (imagePath != null && !imagePath.isEmpty()) {
                // First try to load from resources
                try {
                    userImg = new Image(getClass().getResourceAsStream("/uploads/" + imagePath));
                } catch (Exception e) {
                    // If that fails, try loading from file system
                    File file = new File("src/main/resources/uploads/" + imagePath);
                    if (file.exists()) {
                        userImg = new Image(file.toURI().toString());
                    } else {
                        // If both fail, load default image
                        userImg = new Image(getClass().getResourceAsStream("/uploads/default-avatar.jpg"));
                    }
                }
            } else {
                // Load default image if no image path
                userImg = new Image(getClass().getResourceAsStream("/uploads/default-avatar.jpg"));
            }

            profileImageView.setImage(userImg);
        } catch (Exception e) {
            e.printStackTrace();
            createPlaceholderImage();
        }
    }

    @FXML
    private void handleImageChange() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            try {
                // Create a unique filename
                String fileName = "user_" + currentUser.getId() + "_" +
                        System.currentTimeMillis() + "." +
                        file.getName().substring(file.getName().lastIndexOf(".") + 1);

                // Define the destination path
                Path destPath = Paths.get("src/main/resources/uploads/" + fileName);
                Files.createDirectories(destPath.getParent());

                // Copy the file
                Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                // Update the image view
                profileImageView.setImage(new Image(destPath.toUri().toString()));

                // Store the new image path (including uploads/ prefix)
                newImagePath = "uploads/" + fileName;
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error uploading image");
            }
        }
    }

    private void createPlaceholderImage() {
        Circle circle = new Circle(20);
        circle.setFill(Color.LIGHTGRAY);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        WritableImage placeholderImage = new WritableImage(40, 40);
        circle.snapshot(params, placeholderImage);

        profileImageView.setImage(placeholderImage);
    }

//    @FXML
//    private void handleImageChange() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
//        );
//
//        File file = fileChooser.showOpenDialog(dialogStage);
//        if (file != null) {
//            try {
//                // Create a unique filename
//                String fileName = "user_" + currentUser.getId() + "_" +
//                        System.currentTimeMillis() + "." +
//                        file.getName().substring(file.getName().lastIndexOf(".") + 1);
//
//                // Define the destination path
//                Path destPath = Paths.get("src/main/resources/uploads" + fileName);
//                Files.createDirectories(destPath.getParent());
//
//                // Copy the file
//                Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
//
//                // Update the image view
//                profileImageView.setImage(new Image(file.toURI().toString()));
//
//                // Store the new image path
//                newImagePath = "uploads/" + fileName;
//            } catch (Exception e) {
//                e.printStackTrace();
//                showError("Error uploading image");
//            }
//        }
//    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            // Update user object
            currentUser.setPrenom(prenomField.getText());
            currentUser.setNom(nomField.getText());
            currentUser.setEmail(emailField.getText());

            // Update password if provided
            if (!passwordField.getText().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());
                currentUser.setPassword(hashedPassword);
            }

            // Update image if changed
            if (newImagePath != null) {
                currentUser.setImage(newImagePath);
            }

            // Save to database
            userService.update(currentUser);

            okClicked = true;
            dialogStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error updating profile");
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (prenomField.getText() == null || prenomField.getText().isEmpty()) {
            errorMessage += "First name cannot be empty\n";
        }
        if (nomField.getText() == null || nomField.getText().isEmpty()) {
            errorMessage += "Last name cannot be empty\n";
        }
        if (emailField.getText() == null || emailField.getText().isEmpty()) {
            errorMessage += "Email cannot be empty\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showError(errorMessage);
            return false;
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}