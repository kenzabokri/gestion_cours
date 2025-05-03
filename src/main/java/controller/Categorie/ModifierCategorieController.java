package controller.Categorie;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import entity.CategorieOeuvre;
import service.CategorieOeuvreService;

public class ModifierCategorieController {

    @FXML
    private TextField txtNomCategorie;
    @FXML
    private TextField txtStyleCategorie;
    @FXML
    private TextField txtTechniqueCategorie;

    private CategorieOeuvre categorie;
    private CategorieOeuvreService service = new CategorieOeuvreService();

    public void setCategorie(CategorieOeuvre categorie) {
        this.categorie = categorie;
        chargerDonnees();
        setupTextFilters();
    }

    private void chargerDonnees() {
        txtNomCategorie.setText(categorie.getNomCategorie());
        txtStyleCategorie.setText(categorie.getStyleCategorie());
        txtTechniqueCategorie.setText(categorie.getTechniqueCategorie());
    }

    private void setupTextFilters() {
        // Expression régulière pour lettres, accents, espaces, _-&
        String regex = "[a-zA-ZéàèçÉÀÈÇ\\s_\\-&]*";

        txtNomCategorie.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(regex)) {
                txtNomCategorie.setText(oldValue);
            }
        });

        txtStyleCategorie.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(regex)) {
                txtStyleCategorie.setText(oldValue);
            }
        });

        txtTechniqueCategorie.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(regex)) {
                txtTechniqueCategorie.setText(oldValue);
            }
        });
    }

    @FXML
    private void modifier() {
        if (!validerFormulaire()) {
            return;
        }

        try {
            categorie.setNomCategorie(txtNomCategorie.getText().trim());
            categorie.setStyleCategorie(txtStyleCategorie.getText().trim());
            categorie.setTechniqueCategorie(txtTechniqueCategorie.getText().trim());

            service.update(categorie);

            // Afficher l'alerte de succès avant de fermer
            afficherAlerteSucces();

            ((Stage) txtNomCategorie.getScene().getWindow()).close();

        } catch (Exception e) {
            afficherErreur("Erreur de modification", e.getMessage());
        }
    }

    private boolean validerFormulaire() {
        return validerChamp(txtNomCategorie, "nom")
                && validerChamp(txtStyleCategorie, "style")
                && validerChamp(txtTechniqueCategorie, "technique");
    }

    private boolean validerChamp(TextField champ, String nomChamp) {
        String valeur = champ.getText().trim();

        if (valeur.isEmpty()) {
            afficherErreur("Champ requis", "Le " + nomChamp + " ne peut pas être vide");
            champ.requestFocus();
            return false;
        }

        if (valeur.length() < 4) {
            afficherErreur("Trop court", "Le " + nomChamp + " doit contenir au moins 4 caractères");
            champ.requestFocus();
            return false;
        }

        if (valeur.length() > 100) {
            afficherErreur("Trop long", "Le " + nomChamp + " ne peut pas dépasser 100 caractères");
            champ.requestFocus();
            return false;
        }

        if (!valeur.matches("[a-zA-ZéàèçÉÀÈÇ\\s_\\-&]+")) {
            afficherErreur("Caractères invalides", "Le " + nomChamp + " contient des caractères non autorisés");
            champ.requestFocus();
            return false;
        }

        return true;
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void annuler() {
        ((Stage) txtNomCategorie.getScene().getWindow()).close();
    }
    private void afficherAlerteSucces() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);

        // Contenu personnalisé
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        Text icon = new Text("✓");
        icon.setStyle("-fx-font-size: 24px; -fx-fill: #4CAF50;");

        Label message = new Label("Modification enregistrée !");
        message.setStyle("-fx-font-size: 14px; -fx-text-fill: #7A5C58;");

        content.getChildren().addAll(icon, message);
        alert.getDialogPane().setContent(content);

        // Style
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("chic-alert");
        try {
            dialogPane.getStylesheets().add(
                    getClass().getResource("/styles/chic-theme.css").toExternalForm()
            );
        } catch (Exception e) {
            System.err.println("Erreur CSS: " + e.getMessage());
        }

        alert.showAndWait();
    }


}