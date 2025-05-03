package controller.Oeuvre;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import entity.Oeuvre;
import service.OeuvreService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class OeuvreAfficherController {

    @FXML private Label nomLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    @FXML private Label categorieLabel;
    @FXML private Label signatureLabel;
    @FXML private Label likesLabel;
    @FXML private ImageView imageView;

    private Oeuvre currentOeuvre;
    private final OeuvreService oeuvreService = new OeuvreService();

    public void displayOeuvre(int oeuvreId) {
        currentOeuvre = oeuvreService.readById(oeuvreId);
        if (currentOeuvre != null) {
            nomLabel.setText(currentOeuvre.getNomOeuvre());
            descriptionLabel.setText(currentOeuvre.getDescription());
            dateLabel.setText(currentOeuvre.getDateDeCreation().toString());
            categorieLabel.setText(currentOeuvre.getCategorie().getNomCategorie());
            signatureLabel.setText(currentOeuvre.isSignature() ? "Oui" : "Non");
            likesLabel.setText(String.valueOf(currentOeuvre.getLikes()));

            // Load image if exists
            if (currentOeuvre.getFichierMultimedia() != null) {
                String imagePath = oeuvreService.getUploadDir() + currentOeuvre.getFichierMultimedia();
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    imageView.setImage(image);
                    imageView.setPreserveRatio(true);
                }
            }
        }
    }

    @FXML
    private void handleModifier() {
        try {
            // Load the modification view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/uploadOeuvre.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the current oeuvre
            ModifierOeuvreController controller = loader.getController();
            controller.setOeuvre(currentOeuvre);

            // Create new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'œuvre");
            stage.show();

            // Close current window
            handleClose();

        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la vue de modification");
        }
    }

    @FXML
    private void handleSupprimer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer cette œuvre ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer définitivement cette œuvre ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                oeuvreService.delete(currentOeuvre);
                showAlert("Succès", "Œuvre supprimée avec succès");
                handleClose();
            } catch (Exception e) {
                showAlert("Erreur", "Échec de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleClose() {
        ((Stage) imageView.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}