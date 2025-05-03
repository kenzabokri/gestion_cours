package controller.publicationsandcomments;

import service.PublicationForumService;
import entity.PublicationForum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditPublicationController {

    @FXML
    private TextField nomField;

    @FXML
    private TextArea contenuArea;

    @FXML
    private TextField imageField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button updateButton;

    private final PublicationForumService publicationService = new PublicationForumService();

    private PublicationForum publication;

    private PublicationController publicationController;

    public void setPublication(PublicationForum publication) {
        this.publication = publication;
        nomField.setText(publication.getNomPublicationForum());
        contenuArea.setText(publication.getContenuPublicationForum());
        imageField.setText(publication.getImagePublicationForum());
        datePicker.setValue(publication.getDateCreationPubForum());
    }

    public void setPublicationController(PublicationController controller) {
        this.publicationController = controller;
    }

    @FXML
    private void handleUpdate() {
        String nom = nomField.getText();
        String contenu = contenuArea.getText();
        String image = imageField.getText();
        LocalDate date = datePicker.getValue();

        if (nom.isEmpty() || contenu.isEmpty() || date == null) {
            showAlert("Tous les champs doivent être remplis !");
            return;
        }

        publication.setNomPublicationForum(nom);
        publication.setContenuPublicationForum(contenu);
        publication.setImagePublicationForum(image);
        publication.setDateCreationPubForum(date);

        publicationService.update(publication);

        // Refresh the list in the parent controller
        if (publicationController != null) {
            publicationController.refreshPublications();
        }

        // Close the window
        ((Stage) updateButton.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setContentText(message);
        alert.showAndWait();
    }
}