package controller.Categorie;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import entity.CategorieOeuvre;
import service.CategorieOeuvreService;

import java.io.IOException;

public class AjouterCategorieController {

    @FXML
    private TextField txtNomCategorie;
    @FXML
    private TextField txtStyleCategorie;
    @FXML
    private TextField txtTechniqueCategorie;

    @FXML
    public void initialize() {
        setupTextFilters();
        setupTooltips();
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

    private void setupTooltips() {
        Tooltip tooltip = new Tooltip("Caractères autorisés : lettres, accents, espaces, _-&\nLongueur : 4 à 100 caractères");
        txtNomCategorie.setTooltip(tooltip);
        txtStyleCategorie.setTooltip(tooltip);
        txtTechniqueCategorie.setTooltip(tooltip);
    }

    @FXML
    private void Ajouter(ActionEvent event) {
        try {
            if (!validerFormulaire()) {
                return; // Si la validation échoue, on sort de la méthode
            }

            CategorieOeuvreService service = new CategorieOeuvreService();
            CategorieOeuvre categorie = new CategorieOeuvre(
                    txtNomCategorie.getText().trim(),
                    txtStyleCategorie.getText().trim(),
                    txtTechniqueCategorie.getText().trim());

            service.createPst(categorie); // Enregistrement en base

            // Affiche l'alerte dans le thread JavaFX
            Platform.runLater(() -> {
                afficherAlerteSucces();
                viderChamps(); // Réinitialise le formulaire
            });

        } catch (Exception e) {
            Platform.runLater(() -> {
                afficherErreur("Erreur", "Échec de l'ajout", e.getMessage());
            });
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

    private void viderChamps() {
        txtNomCategorie.clear();
        txtStyleCategorie.clear();
        txtTechniqueCategorie.clear();
    }

    private void afficherErreur(String titre, String message) {
        afficherAlerte(Alert.AlertType.ERROR, titre, message);
    }

    private void afficherErreur(String titre, String entete, String message) {
        afficherAlerte(Alert.AlertType.ERROR, titre, entete, message);
    }

    private void afficherSucces(String titre, String message) {
        afficherAlerte(Alert.AlertType.INFORMATION, titre, message);
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String entete, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/AfficherCategorie.fxml"));
            Parent root = loader.load();
            txtNomCategorie.getScene().setRoot(root);
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'afficher la liste", e.getMessage());
        }
    }

    @FXML
    void AfficherDash(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/Categorie.fxml"));
            Parent root = loader.load();
            txtNomCategorie.getScene().setRoot(root);
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'afficher la liste", e.getMessage());
        }
    }

    private Runnable categoryAddedCallback;

    public void setCategoryAddedCallback(Runnable callback) {
        this.categoryAddedCallback = callback;
    }

    // Call this when category is successfully added
    private void onCategoryAdded() {
        if (categoryAddedCallback != null) {
            categoryAddedCallback.run();
        }
    }
    private void afficherAlerteSucces() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(txtNomCategorie.getScene().getWindow()); // Important pour le centrage
        alert.setTitle("Confirmation");
        alert.setHeaderText("Opération réussie");
        alert.setContentText("La catégorie a été ajoutée avec succès.");

        // Style personnalisé
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("chic-alert");

        // Charge le CSS si nécessaire
        try {
            dialogPane.getStylesheets().add(
                    getClass().getResource("/styles/chic-theme.css").toExternalForm()
            );
        } catch (NullPointerException e) {
            System.err.println("Fichier CSS non trouvé");
        }

        alert.showAndWait(); // Bloque jusqu'à fermeture
    }
}