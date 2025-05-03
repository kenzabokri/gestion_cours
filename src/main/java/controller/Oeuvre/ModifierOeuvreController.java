package controller.Oeuvre;

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

public class ModifierOeuvreController {

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
    @FXML
    private Button uploadButton;
    @FXML
    private Button updateButton;

    private File selectedImageFile;
    private Oeuvre currentOeuvre;
    private final OeuvreService oeuvreService = new OeuvreService();
    private final CategorieOeuvreService categorieService = new CategorieOeuvreService();

    public void setOeuvre(Oeuvre oeuvre) {
        this.currentOeuvre = oeuvre;
        populateFields();
    }

    @FXML
    public void initialize() {
        loadCategories();
        dateCreationPicker.setValue(LocalDate.now());

        imagePreview.setFitWidth(200);
        imagePreview.setFitHeight(200);
        imagePreview.setPreserveRatio(true);
    }

    private void populateFields() {
        if (currentOeuvre != null) {
            nomField.setText(currentOeuvre.getNomOeuvre());
            descriptionField.setText(currentOeuvre.getDescription());
            dateCreationPicker.setValue(currentOeuvre.getDateDeCreation());
            signatureCheckbox.setSelected(currentOeuvre.isSignature());

            // Select the correct category in combo box
            for (CategorieOeuvre categorie : categorieComboBox.getItems()) {
                if (categorie.getId() == currentOeuvre.getCategorie().getId()) {
                    categorieComboBox.getSelectionModel().select(categorie);
                    break;
                }
            }

            // Load image if exists
            if (currentOeuvre.getFichierMultimedia() != null && !currentOeuvre.getFichierMultimedia().isEmpty()) {
                try {
                    File imageFile = new File("uploads/" + currentOeuvre.getFichierMultimedia());
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toString());
                        imagePreview.setImage(image);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }
            }
        }
    }

    private void loadCategories() {
        List<CategorieOeuvre> categories = categorieService.readAll();
        categorieComboBox.getItems().addAll(categories);
    }

    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            try {
                Image image = new Image(selectedImageFile.toURI().toString());
                imagePreview.setImage(image);
            } catch (Exception e) {
                showAlert("Error", "Could not load image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }

            // Update the current Oeuvre object
            currentOeuvre.setNomOeuvre(nomField.getText());
            currentOeuvre.setDescription(descriptionField.getText());
            currentOeuvre.setDateDeCreation(dateCreationPicker.getValue());
            currentOeuvre.setSignature(signatureCheckbox.isSelected());
            currentOeuvre.setCategorie(categorieComboBox.getValue());

            // Handle image update if selected
            if (selectedImageFile != null) {
                try (InputStream imageStream = new FileInputStream(selectedImageFile)) {
                    String fileName = selectedImageFile.getName();
                    oeuvreService.updateWithImage(currentOeuvre, imageStream, fileName);
                }
            } else {
                oeuvreService.update(currentOeuvre);
            }

            statusLabel.setText("Oeuvre mise à jour avec succès!");
            statusLabel.setStyle("-fx-text-fill: green;");


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update oeuvre: " + e.getMessage());
            statusLabel.setText("Erreur lors de la mise à jour de l'oeuvre");
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



    private Consumer<Oeuvre> onSave;

    public void setOnSave(Consumer<Oeuvre> onSave) {
        this.onSave = onSave;
    }



    @FXML
    private void handleUpdateDash(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }

            // Update the current Oeuvre object
            currentOeuvre.setNomOeuvre(nomField.getText());
            currentOeuvre.setDescription(descriptionField.getText());
            currentOeuvre.setDateDeCreation(dateCreationPicker.getValue());
            currentOeuvre.setSignature(signatureCheckbox.isSelected());
            currentOeuvre.setCategorie(categorieComboBox.getValue());

            // Handle image update if selected
            if (selectedImageFile != null) {
                try (InputStream imageStream = new FileInputStream(selectedImageFile)) {
                    String fileName = selectedImageFile.getName();
                    oeuvreService.updateWithImage(currentOeuvre, imageStream, fileName);
                }
            } else {
                oeuvreService.update(currentOeuvre);
            }

            statusLabel.setText("Oeuvre mise à jour avec succès!");
            statusLabel.setStyle("-fx-text-fill: green;");

            // Redirect to the Dashboard page after a short delay
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                redirectToDashboard();
                            });
                        }
                    },
                    1500
            );

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update oeuvre: " + e.getMessage());
            statusLabel.setText("Erreur lors de la mise à jour de l'oeuvre");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void redirectToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/Categorie.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard - Oeuvre Affichage");
            stage.show();

            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page du tableau de bord");
        }
    }




    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}