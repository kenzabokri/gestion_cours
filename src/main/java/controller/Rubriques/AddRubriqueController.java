package controller.Rubriques;

import entity.rubrique;
import entity.forum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.rubriqueservice;

import java.time.LocalDate;

public class AddRubriqueController {

    @FXML
    private TextField nomField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button saveButton;

    private forum forum;
    private RubriqueController rubriqueController;

    private final rubriqueservice rubriqueService = new rubriqueservice();

    private rubrique rubriqueToEdit = null;

    public void setForum(forum forum) {
        this.forum = forum;
    }

    public void setRubriqueController(RubriqueController controller) {
        this.rubriqueController = controller;
    }

    public void setRubriqueToEdit(rubrique r) {
        this.rubriqueToEdit = r;

        // Remplir les champs avec les données existantes
        nomField.setText(r.getNomRubrique());
        descriptionField.setText(r.getDescriptionRubrique());

        // Modifier le texte du bouton
        saveButton.setText("Modifier");
    }

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        String nom = nomField.getText().trim();
        String description = descriptionField.getText().trim();

        if (nom.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        if (rubriqueToEdit == null) {
            // Ajout
            rubrique newRubrique = new rubrique(0, nom, description, LocalDate.now(), forum.getId());
            rubriqueService.create(newRubrique);
        } else {
            // Modification
            rubriqueToEdit.setNomRubrique(nom);
            rubriqueToEdit.setDescriptionRubrique(description);
            rubriqueService.update(rubriqueToEdit);
        }

        if (rubriqueController != null) {
            rubriqueController.refreshTable();
        }

        // Fermer la fenêtre
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}