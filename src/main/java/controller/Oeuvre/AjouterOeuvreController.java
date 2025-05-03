package controller.Oeuvre;

import controller.Categorie.AfficheCatDashboard;
import controller.Categorie.AjouterCategorieController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entity.CategorieOeuvre;
import entity.Oeuvre;
import service.CategorieOeuvreService;
import service.OeuvreService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class AjouterOeuvreController {

    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dateCreationPicker;
    @FXML
    private CheckBox signatureCheckbox;
    @FXML
    private ComboBox<CategorieOeuvre> categorieComboBox;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Label statusLabel;

    // Ajouter cette propriété
    @FXML
    private Label imageNameLabel;

    private File selectedImageFile;
    private final OeuvreService oeuvreService = new OeuvreService();
    private final CategorieOeuvreService categorieService = new CategorieOeuvreService();

    @FXML
    public void initialize() {
        // Load categories into combo box
        loadCategories();

        // Set default date to today
        dateCreationPicker.setValue(LocalDate.now());

        // Configure image preview
        imagePreview.setFitWidth(200);
        imagePreview.setFitHeight(200);
        imagePreview.setPreserveRatio(true);
    }

    private void loadCategories() {
        List<CategorieOeuvre> categories = categorieService.readAll();
        categorieComboBox.getItems().addAll(categories);

        if (!categories.isEmpty()) {
            categorieComboBox.getSelectionModel().selectFirst();
        }
    }

    // Modifier handleChooseImage
    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp")
        );

        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            try {
                // Validation du format
                String fileName = selectedImageFile.getName().toLowerCase();
                if (!fileName.matches(".*\\.(png|jpg|jpeg|webp)$")) {
                    showAlert("Format invalide", "Formats acceptés : PNG, JPG, JPEG, WebP");
                    resetImageSelection();
                    return;
                }

                // Validation de la taille (5MB max)
                long fileSize = selectedImageFile.length();
                if (fileSize > 5 * 1024 * 1024) {
                    showAlert("Fichier trop volumineux", "La taille maximale autorisée est 5MB");
                    resetImageSelection();
                    return;
                }

                // Affichage de l'aperçu
                Image image = new Image(selectedImageFile.toURI().toString());
                imagePreview.setImage(image);
                imageNameLabel.setText(selectedImageFile.getName());
                imageNameLabel.setStyle("-fx-text-fill: #28a745;");

            } catch (Exception e) {
                showAlert("Erreur", "Impossible de charger l'image : " + e.getMessage());
                resetImageSelection();
            }
        }
    }

    private void resetImageSelection() {
        selectedImageFile = null;
        imagePreview.setImage(null);
        imageNameLabel.setText("Aucun fichier sélectionné");
        imageNameLabel.setStyle("-fx-text-fill: #6c757d;");
    }


    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            // Validate inputs
            if (!validateInputs()) {
                return;
            }

            // Create new Oeuvre object
            Oeuvre newOeuvre = new Oeuvre();
            newOeuvre.setNomOeuvre(nomField.getText());
            newOeuvre.setDescription(descriptionField.getText());
            newOeuvre.setDateDeCreation(dateCreationPicker.getValue());
            newOeuvre.setSignature(signatureCheckbox.isSelected());
            newOeuvre.setLikes(0); // Default likes to 0
            newOeuvre.setCategorie(categorieComboBox.getValue());

            // Handle image upload if selected
            if (selectedImageFile != null) {
                try (InputStream imageStream = new FileInputStream(selectedImageFile)) {
                    String fileName = selectedImageFile.getName();
                    oeuvreService.createWithImage(newOeuvre, imageStream, fileName);
                }
            } else {
                oeuvreService.create(newOeuvre);
            }

            // Show success message
            statusLabel.setText("Oeuvre ajoutée avec succès!");
            statusLabel.setStyle("-fx-text-fill: green;");



        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add oeuvre: " + e.getMessage());
            statusLabel.setText("Erreur lors de l'ajout de l'oeuvre");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        // Validation du nom
        String nom = nomField.getText();
        if (nom == null || nom.trim().isEmpty()) {
            errors.append("Le nom de l'oeuvre est requis\n");
        } else {
            if (!nom.matches("^[a-zA-ZéèçàêëïöüâäôûùÉÈÇÀÊËÏÖÜÂÄÔÛÙ\\s-]{4,10}$")) {
                errors.append("Le nom doit contenir entre 4 et 10 lettres (caractères accentués acceptés)\n");
            }
        }
        if (selectedImageFile == null) {
            errors.append("L'image est obligatoire\n");
        } else {
            // Validation du format
            String fileName = selectedImageFile.getName().toLowerCase();
            if (!fileName.matches(".*\\.(png|jpg|jpeg|webp)$")) {
                errors.append("Format d'image non supporté (utilisez JPG/PNG/WebP)\n");
            }

            // Validation de la taille
            long fileSize = selectedImageFile.length();
            if (fileSize > 5 * 1024 * 1024) {
                errors.append("La taille de l'image dépasse 5MB\n");
            }
        }

        // Validation de la description
        String description = descriptionField.getText();
        if (description == null || description.trim().isEmpty()) {
            errors.append("La description est requise\n");
        } else {
            if (description.length() < 10 || description.length() > 50) {
                errors.append("La description doit contenir entre 10 et 50 caractères\n");
            }
        }

        // Validation de la date
        if (dateCreationPicker.getValue() == null) {
            errors.append("La date de création est requise\n");
        } else if (dateCreationPicker.getValue().isAfter(LocalDate.now())) {
            errors.append("La date de création ne peut pas être dans le futur\n");
        }

        // Validation de la catégorie
        if (categorieComboBox.getValue() == null) {
            errors.append("La catégorie est requise\n");
        }

        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString());
            return false;
        }

        return true;
    }


    private void clearForm() {
        nomField.clear();
        descriptionField.clear();
        dateCreationPicker.setValue(LocalDate.now());
        signatureCheckbox.setSelected(false);
        imagePreview.setImage(null);
        selectedImageFile = null;
        statusLabel.setText("");
        resetImageSelection();

    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void redirectToAddCategory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/AjouterCategorie.fxml"));
            Parent root = loader.load();

            // Get controller to set callback
            AjouterCategorieController controller = loader.getController();
            controller.setCategoryAddedCallback(this::loadCategories);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Catégorie");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la page d'ajout de catégorie");
        }
    }

    // Add this field at the top of your AjouterOeuvreController class
    private Consumer<Integer> oeuvreAddedCallback;

    // Add this method to your AjouterOeuvreController class
    public void setOeuvreAddedCallback(Consumer<Integer> callback) {
        this.oeuvreAddedCallback = callback;
    }

    // Modify your redirectToOeuvreAfficher method to include the callback:
    private void redirectToOeuvreAfficher(int oeuvreId) {
        try {
            // Notify the callback that an artwork was added
            if (oeuvreAddedCallback != null) {
                oeuvreAddedCallback.accept(oeuvreId);
            }

            // Rest of your existing code...
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/OeuvreMarkets.fxml"));
            Parent root = loader.load();

            OeuvreAfficherController controller = loader.getController();
            controller.displayOeuvre(oeuvreId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'œuvre");
            stage.show();

            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la vue des détails");
        }
    }


    @FXML
    private void redirectToAddCategoryDash(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashBoard/AjouterCategorie.fxml"));

            Parent root = loader.load();

            // Get controller to set callback
            AjouterCategorieController controller = loader.getController();
            controller.setCategoryAddedCallback(this::loadCategories);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Catégorie");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la page d'ajout de catégorie");
        }
    }


    @FXML
    private void handleAjouterDash(ActionEvent event) {
        try {
            // Validate inputs
            if (!validateInputs()) {
                return;
            }

            // Create new Oeuvre object
            Oeuvre newOeuvre = new Oeuvre();
            newOeuvre.setNomOeuvre(nomField.getText());
            newOeuvre.setDescription(descriptionField.getText());
            newOeuvre.setDateDeCreation(dateCreationPicker.getValue());
            newOeuvre.setSignature(signatureCheckbox.isSelected());
            newOeuvre.setLikes(0); // Default likes to 0
            newOeuvre.setCategorie(categorieComboBox.getValue());

            // Handle image upload if selected
            if (selectedImageFile != null) {
                try (InputStream imageStream = new FileInputStream(selectedImageFile)) {
                    String fileName = selectedImageFile.getName();
                    oeuvreService.createWithImage(newOeuvre, imageStream, fileName);
                }
            } else {
                oeuvreService.create(newOeuvre);
            }

            // Show success message
            statusLabel.setText("Oeuvre ajoutée avec succès!");
            statusLabel.setStyle("-fx-text-fill: green;");

            // Clear form after short delay
          /*  new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                clearForm();
                                closeWindow();
                            });
                        }
                    },
                    1500
            );*/
            // Redirect after short delay
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                redirectToOeuvreAfficherDash(newOeuvre.getId());
                            });
                        }
                    },
                    1500  // 1.5 second delay
            );


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add oeuvre: " + e.getMessage());
            statusLabel.setText("Erreur lors de l'ajout de l'oeuvre");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }


    // Modify your redirectToOeuvreAfficher method to include the callback:
    private void redirectToOeuvreAfficherDash(int oeuvreId) {
        try {
            // Notify the callback that an artwork was added
            if (oeuvreAddedCallback != null) {
                oeuvreAddedCallback.accept(oeuvreId);
            }

            // Rest of your existing code...
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/Categorie.fxml")); // Remplace par le bon fichier
            Parent root = loader.load();

            AfficheCatDashboard controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'œuvre");
            stage.show();

            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la vue des détails");
        }
    }

}