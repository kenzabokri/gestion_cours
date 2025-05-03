package controller.User;

import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.mindrot.jbcrypt.BCrypt;

public class AddUser {

    @FXML
    private Button Valider;

    @FXML
    private TextField txtmail;

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txtpass;

    @FXML
    private TextField txtprenom;

    @FXML
    private ImageView imageView;

    @FXML
    private Label imageLabel;

    @FXML
    private Button uploadButton;

    @FXML
    private Label errorLabel; // Add this to your FXML

    private String imagePath = null;

    @FXML
    public void initialize() {
        // Set default image
        try {
            imageView.setImage(new Image(getClass().getResource("/uploads/default-avatar.jpg").toString()));
        } catch (Exception e) {
            System.err.println("Could not load default avatar");
        }

        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Create images directory if it doesn't exist
                Path imagesDir = Paths.get("src/main/resources/uploads");
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }

                // Generate unique filename using timestamp and user information
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = imagesDir.resolve(fileName);

                // Copy the file to our images directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Update ImageView
                Image image = new Image(targetPath.toUri().toString());
                imageView.setImage(image);

                // Store only the filename in imagePath
                imagePath = fileName;  // Changed this line
                imageLabel.setText("Image selected: " + fileName);

            } catch (IOException e) {
                e.printStackTrace();
                imageLabel.setText("Error uploading image");
            }
        }
    }

    @FXML
    void AjouterUser(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            UserService userService = new UserService();

            // Check if email already exists
            if (userService.findByEmail(txtmail.getText()) != null) {
                showError("Email already exists!");
                return;
            }

            // Hash the password
            String hashedPassword = BCrypt.hashpw(txtpass.getText(), BCrypt.gensalt());

            // Create user with hashed password
            User user = new User(txtnom.getText(), txtprenom.getText(), txtmail.getText(), hashedPassword);

            if (imagePath != null) {
                user.setImage("uploads/" + imagePath);  // Changed this line to include 'uploads/'
            }

            userService.create(user);

            // Navigate to login page
            navigateToLogin();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error creating account: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        // Reset error message
        hideError();

        // Check for empty fields
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

        // Validate email format
        if (!txtmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Invalid email format");
            return false;
        }

        if (txtpass.getText().trim().isEmpty()) {
            showError("Password is required");
            return false;
        }

        // Validate password strength (at least 6 characters)
        if (txtpass.getText().length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void hideError() {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/Login.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) Valider.getScene().getWindow();

            // Create new scene
            Scene scene = new Scene(root);

            // Set the scene
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Error navigating to login page");
        }
    }

    private void clearFields() {
        txtnom.clear();
        txtprenom.clear();
        txtmail.clear();
        txtpass.clear();
        try {
            imageView.setImage(new Image(getClass().getResource("/images/default-avatar.jpg").toString()));
        } catch (Exception e) {
            System.err.println("Could not load default avatar");
        }
        imageLabel.setText("No image selected");
        imagePath = null;
    }
}