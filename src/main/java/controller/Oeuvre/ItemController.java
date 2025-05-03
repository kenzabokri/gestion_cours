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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import entity.Oeuvre;
import service.OeuvreService;

import java.io.IOException;
import java.util.Optional;

public class ItemController {
    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label likesLabel;
    @FXML private ImageView img;

    private OeuvreService oeuvreService = new OeuvreService();
    private Oeuvre oeuvre;
    private ArtworkClickListener listener;
    private LikeUpdateListener likeUpdateListener;
    private ArtworkUpdateListener artworkUpdateListener;

    public interface ArtworkClickListener {
        void onClick(Oeuvre oeuvre);
    }

    public interface LikeUpdateListener {
        void onLikeUpdate(Oeuvre oeuvre);
    }

    public interface ArtworkUpdateListener {
        void onArtworkUpdated(Oeuvre updatedOeuvre);
        void onArtworkDeleted(Oeuvre deletedOeuvre);
    }

    public void setLikeUpdateListener(LikeUpdateListener listener) {
        this.likeUpdateListener = listener;
    }

    public void setArtworkUpdateListener(ArtworkUpdateListener listener) {
        this.artworkUpdateListener = listener;
    }

    @FXML
    private void click(MouseEvent event) {
        if (listener != null) {
            listener.onClick(oeuvre);
        }
    }

    public void setData(Oeuvre oeuvre, ArtworkClickListener listener) {
        this.oeuvre = oeuvre;
        this.listener = listener;

        updateDisplay();
    }

    private void updateDisplay() {
        nameLabel.setText(oeuvre.getNomOeuvre());
        descriptionLabel.setText(oeuvre.getDescription());
        likesLabel.setText("❤ " + oeuvre.getLikes());

        try {
            if (oeuvre.getFichierMultimedia() != null) {
                Image image = new Image("file:" + oeuvreService.getUploadDir() + oeuvre.getFichierMultimedia());
                img.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            controller.setOeuvre(oeuvre);

            // Create new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'œuvre");
            stage.show();



        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la vue de modification");
        }
    }
    @FXML
    private void handleSupprimer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette œuvre ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                oeuvreService.delete(oeuvre);
                if (artworkUpdateListener != null) {
                    artworkUpdateListener.onArtworkDeleted(oeuvre);
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to delete the artwork");
                e.printStackTrace();
            }
        }
    }

    public void updateLikes(int newLikes) {
        oeuvre.setLikes(newLikes);
        likesLabel.setText("❤ " + newLikes);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}