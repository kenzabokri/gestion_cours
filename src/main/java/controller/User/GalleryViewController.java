package controller.User;

import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.SnapshotParameters;
import javafx.stage.Stage;
import java.io.File;
import java.io.InputStream;

public class GalleryViewController {

    @FXML private Label userNameLabel;
    @FXML private ImageView userImage;
    @FXML private Button profileButton;
    @FXML private Button eventsButton;
    @FXML private Button galleryButton;
    @FXML
    private Button coursbutton;

    private User currentUser;

    @FXML
    public void initialize() {
        setupNavigationButtons();
    }

    private void setupNavigationButtons() {
        profileButton.setOnAction(e -> navigateToProfile());
        eventsButton.setOnAction(e -> navigateToEvents());
        // Gallery button is current page
        galleryButton.setDisable(true);
        coursbutton.setOnAction(e->afficherMateriel());
    }

    public void setUser(User user) {
        this.currentUser = user;

        // Set user name in navbar
        userNameLabel.setText(user.getPrenom() + " " + user.getNom());

        // Load user image
        try {
            String imagePath = user.getImage();
            Image userImg;

            if (imagePath != null && !imagePath.isEmpty()) {
                // Try loading from file system first
                File file = new File("src/main/resources/" + imagePath);
                if (file.exists()) {
                    userImg = new Image(file.toURI().toString());
                } else {
                    // If file doesn't exist in file system, try loading from classpath
                    InputStream resourceStream = getClass().getResourceAsStream("/" + imagePath);
                    if (resourceStream != null) {
                        userImg = new Image(resourceStream);
                    } else {
                        // If both fail, load default
                        userImg = new Image(getClass().getResourceAsStream("/uploads/default-avatar.jpg"));
                    }
                }
            } else {
                // If no image path, load default
                userImg = new Image(getClass().getResourceAsStream("/uploads/default-avatar.jpg"));
            }

            if (userImg != null && !userImg.isError()) {
                userImage.setImage(userImg);
            } else {
                throw new Exception("Failed to load image");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Load default image as fallback
            try {
                Image defaultImg = new Image(getClass().getResourceAsStream("/uploads/default-avatar.jpg"));
                userImage.setImage(defaultImg);
            } catch (Exception ex) {
                ex.printStackTrace();
                createPlaceholderImage();
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

        userImage.setImage(placeholderImage);
    }

    private void navigateToProfile() {
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

    private void navigateToEvents() {
        // Implement events navigation when ready
        System.out.println("Navigating to events...");
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleCollectionClick(MouseEvent event) {
        // Handle collection card clicks
    }

    @FXML
    private void handleArtistClick(MouseEvent event) {
        // Handle artist card clicks
    }

    @FXML
    private void handleExhibitionClick(MouseEvent event) {
        // Handle exhibition card clicks
    }
    @FXML
    private void afficherMateriel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CoursMateriel/CoursFront.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("les cours");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) coursbutton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage des matériels : " + e.getMessage());
        }
    }
}