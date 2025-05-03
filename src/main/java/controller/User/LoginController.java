package controller.User;

import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import service.UserService;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;

    private UserService userService;

    @FXML
    public void initialize() {
        userService = new UserService();
        errorLabel.setVisible(false);
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        User user = userService.findByEmail(email);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            try {
                FXMLLoader loader;

                // Check user role and load appropriate view
                if (user.isAdmin()) {
                    // Load admin dashboard
                    loader = new FXMLLoader(getClass().getResource("/User/MainView.fxml"));
                    Parent root = loader.load();
                    MainViewController controller = loader.getController();
                    controller.setUser(user);
                } else {
                    // Load user view for members and artists
                    loader = new FXMLLoader(getClass().getResource("/User/UserView.fxml"));
                    Parent root = loader.load();
                    UserViewController controller = loader.getController();
                    controller.setUser(user);
                }

                // Get current stage and set new scene
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(loader.getRoot());
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading application");
            }
        } else {
            showError("Invalid email or password");
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AddUser.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading registration form");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}