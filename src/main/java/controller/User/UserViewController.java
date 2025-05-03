package controller.User;

import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserViewController {

    @FXML private Label userNameLabel;
    @FXML private ImageView userImage;
    @FXML private ImageView profileImage;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private StackPane contentArea;
    @FXML private Label clockLabel; // Add this
    @FXML private Label userLoginLabel; // Add this

    @FXML private Button profileButton;
    @FXML private Button eventsButton;
    @FXML private Button galleryButton;

    private User currentUser;

    @FXML
    public void initialize() {
        setupNavigationButtons();
        setupClock();
        profileButton.setDisable(true);
    }

    private void setupClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            clockLabel.setText("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): "
                    + LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void setupNavigationButtons() {
        profileButton.setOnAction(e -> showProfile());
        eventsButton.setOnAction(e -> showEvents());
        galleryButton.setOnAction(e -> showGallery());
    }

    public void setUser(User user) {
        this.currentUser = user;

        // Set user name in navbar
        userNameLabel.setText(user.getPrenom() + " " + user.getNom());

        // Set user login label
        userLoginLabel.setText("Current User's Login: " + user.getPrenom());

        // Load user image for both avatar and profile
        try {
            String imagePath = user.getImage();
            Image userImg;

            if (imagePath != null && !imagePath.isEmpty()) {
                // Try loading from file system first
                File file = new File("src/main/resources/" + imagePath);
                if (file.exists()) {
                    userImg = new Image(file.toURI().toString());
                } else {
                    // Try loading from resources
                    userImg = new Image(getClass().getResourceAsStream("/" + imagePath));
                }
            } else {
                // Load default image if no image path
                userImg = new Image(getClass().getResourceAsStream("/uploads/default-avatar.jpg"));
            }

            if (userImg != null && !userImg.isError()) {
                userImage.setImage(userImg);
                profileImage.setImage(userImg);
            } else {
                throw new Exception("Failed to load image");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Load default image as fallback
            try {
                Image defaultImg = new Image(getClass().getResourceAsStream("/uploads/default-avatar.jpg"));
                userImage.setImage(defaultImg);
                profileImage.setImage(defaultImg);
            } catch (Exception ex) {
                ex.printStackTrace();
                createPlaceholderImage();
            }
        }

        // Update profile information
        nameLabel.setText("Name: " + user.getPrenom() + " " + user.getNom());
        emailLabel.setText("Email: " + user.getEmail());
        roleLabel.setText("Role: " + (user.isArtist() ? "Artist" : "Member"));
    }

    private void createPlaceholderImage() {
        Circle circle = new Circle(20);
        circle.setFill(Color.LIGHTGRAY);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        WritableImage placeholderImage = new WritableImage(40, 40);
        circle.snapshot(params, placeholderImage);

        userImage.setImage(placeholderImage);
        profileImage.setImage(placeholderImage);
    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/UpdateProfileView.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Profile");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(userNameLabel.getScene().getWindow());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Fix: Removed 'ViewController' from class name and fixed variable name
            UpdateProfileViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUser(currentUser);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                // Refresh the display with updated user info
                setUser(currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open profile editor");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not logout");
        }
    }

    private void showProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/UserView.fxml"));
            Parent root = loader.load();
            UserViewController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = (Stage) profileButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not navigate to profile");
        }
    }

    private void showEvents() {
        System.out.println("Showing events view");
    }

    private void showGallery() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/GalleryView.fxml"));
            Parent root = loader.load();
            GalleryViewController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = (Stage) galleryButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open gallery");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}