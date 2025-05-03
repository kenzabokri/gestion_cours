package controller.publicationsandcomments;

import service.CommentaireService;
import entity.Commentaire;
import entity.PublicationForum;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddCommentaireController {

    @FXML
    private TextArea contenuArea;

    private PublicationForum publication;
    private final CommentaireService commentaireService = new CommentaireService();
    private CommentaireController commentaireController;

    public void setPublication(PublicationForum publication) {
        this.publication = publication;
    }

    public void setCommentaireController(CommentaireController controller) {
        this.commentaireController = controller;
    }

    @FXML
    private void handleAdd() {
        String contenu = contenuArea.getText().trim();

        // Contrôle de saisie
        if (contenu.isEmpty()) {
            showAlert("Champ vide", "Le contenu du commentaire ne peut pas être vide.");
            return;
        }

        if (contenu.length() < 3) {
            showAlert("Contenu trop court", "Le commentaire doit contenir au moins 3 caractères.");
            return;
        }

        // Création du commentaire
        Commentaire c = new Commentaire(0, contenu, LocalDate.now(), 0, 0, publication.getId());
        commentaireService.create(c);

        // Rafraîchir la liste des commentaires
        if (commentaireController != null) {
            commentaireController.refreshTable();
        }

        // Message de succès
        showInfo("Succès", "Commentaire ajouté avec succès.");

        // Fermer la fenêtre
        ((Stage) contenuArea.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}