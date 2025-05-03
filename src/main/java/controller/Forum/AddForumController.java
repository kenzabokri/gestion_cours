package controller.Forum;

import service.forumservice;
import entity.forum;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddForumController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField themeField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField imageField;

    private final forumservice forumService = new forumservice();

    // Référence vers le contrôleur principal pour le rafraîchissement
    private DashboardController forumController;

    public void setForumController(DashboardController forumController) {
        this.forumController = forumController;
    }

    @FXML
    private void handleAddForum() {
        String nom = nomField.getText();
        String theme = themeField.getText();
        String description = descriptionArea.getText();
        String image = imageField.getText();

        // ✅ Contrôle de saisie : tous les champs doivent être remplis
        if (nom.isEmpty() || theme.isEmpty() || description.isEmpty() || image.isEmpty()) {
            showAlert("Champs requis", "Veuillez remplir tous les champs.");
            return;
        }

        forum newForum = new forum();
        newForum.setNomForum(nom);
        newForum.setThemeForum(theme);
        newForum.setDescriptionForum(description);
        newForum.setImageForum(image);
        newForum.setDateCreationForum(LocalDate.now());

        forumService.create(newForum);

        // Rafraîchir la liste des forums
        if (forumController != null) {
            forumController.refreshTable(); // <- recharge les forums
        }

        // Fermer la fenêtre après ajout
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    // ⚠️ Affichage d'une alerte simple
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}