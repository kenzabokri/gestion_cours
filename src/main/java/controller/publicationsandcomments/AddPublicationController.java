package controller.publicationsandcomments;

import entity.PublicationForum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.PublicationForumService;

import java.time.LocalDate;

public class AddPublicationController {

    @FXML
    private TextField nomField;

    @FXML
    private TextArea contenuArea;

    @FXML
    private TextField imageField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addButton;

    private final PublicationForumService publicationService = new PublicationForumService();

    private long rubriqueId;  // Assurer que cette valeur est bien définie avant l'ajout

    private PublicationController publicationController;

    public void setRubriqueId(long id) {
        this.rubriqueId = id;
    }

    public void setPublicationController(PublicationController controller) {
        this.publicationController = controller;
    }

    @FXML
    private void handleAdd() {
        String nom = nomField.getText();
        String contenu = contenuArea.getText();
        String image = imageField.getText();
        LocalDate date = datePicker.getValue();

        if (nom.isEmpty() || contenu.isEmpty() || date == null) {
            showAlert("Tous les champs doivent être remplis !");
            return;
        }

        if (rubriqueId == 0) {
            showAlert("ID de rubrique non défini !");
            return;
        }

        PublicationForum pub = new PublicationForum(0, nom, contenu, image, date, rubriqueId);
        publicationService.create(pub);

        // 🔄 Rafraîchir la liste dans le contrôleur parent
        if (publicationController != null) {
            publicationController.refreshPublications();
        }

        // ❌ Fermer la fenêtre
        ((Stage) addButton.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setContentText(message);
        alert.showAndWait();
    }
}