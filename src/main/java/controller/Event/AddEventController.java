package controller.Event;

import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import service.EventService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class AddEventController {

    @FXML private TextField nomEventField;
    @FXML private DatePicker dateEventPicker;
    @FXML private TextField localisationField;
    @FXML private TextField statutField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField imageField;
    @FXML private Button ajouterButton;
    @FXML private Button parcourirButton;
    @FXML private Button showEventButton;
    @FXML private RadioButton termineRadio;
    @FXML private RadioButton bientotCommenceeRadio;
    @FXML private RadioButton encoursRadio;
    @FXML private ToggleGroup statusToggleGroup;

    private final EventService eventService = new EventService();

    @FXML
    private void ajouterEvent() {
        String nom = nomEventField.getText();
        LocalDate date = dateEventPicker.getValue();
        String localisation = localisationField.getText();

        // Get the selected status from the radio buttons
        String statut = "";
        if (termineRadio.isSelected()) {
            statut = "Terminé";
        } else if (bientotCommenceeRadio.isSelected()) {
            statut = "Bientôt commencé";
        } else if (encoursRadio.isSelected()) {
            statut = "En cours";
        }

        String description = descriptionArea.getText();
        String imagePath = imageField.getText(); // chemin absolu choisi

        // Vérification que tous les champs nécessaires sont remplis
        if (nom == null || nom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom de l'événement est requis !");
            return;
        }
        if (date == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de l'événement est requise !");
            return;
        }
        if (localisation == null || localisation.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La localisation de l'événement est requise !");
            return;
        }
        if (statut == null || statut.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut de l'événement est requis !");
            return;
        }
        if (description == null || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La description de l'événement est requise !");
            return;
        }

        // Gestion de l'image
        if (imagePath != null && !imagePath.isEmpty()) {
            File img = new File(imagePath);
            // Vérifie si le fichier existe
            if (!img.exists()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'image sélectionnée n'existe pas !");
                return;
            }

            // Copie de l'image dans le dossier "resources/images"
            String nomImage = img.getName();
            Path destination = Path.of("resources/images", nomImage);

            try {
                // Crée le dossier s'il n'existe pas
                Files.createDirectories(destination.getParent());

                // Copie de l'image
                Files.copy(Path.of(imagePath), destination, StandardCopyOption.REPLACE_EXISTING);
                imagePath = "resources/images/" + nomImage; // chemin relatif stocké
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de copier l'image.");
                return;
            }
        }

        // Create the event object and set the fields
        Event event = new Event();
        event.setNomEvent(nom);
        event.setDateEvent(date);
        event.setLocalisationEvent(localisation);
        event.setStatuEvent(statut); // Set the selected status
        event.setDescriptionEvent(description);
        event.setImageEvent(imagePath);
        event.setQrCode(null);  // Assuming you handle QR code elsewhere

        // Assuming eventService.create handles saving the event to a database
        eventService.create(event);
        System.out.println("Événement ajouté !");
    }

    // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        nomEventField.clear();
        dateEventPicker.setValue(null);
        localisationField.clear();
        statutField.clear();
        descriptionArea.clear();
        imageField.clear();
    }



    @FXML
    private void parcourirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Window window = nomEventField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void goToShowEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/showevent.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = (Stage) showEventButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Optionnel : méthode d'initialisation si besoin
    @FXML
    private void initialize() {
        // Create a ToggleGroup and assign it to the RadioButtons
        ToggleGroup statusToggleGroup = new ToggleGroup();
        termineRadio.setToggleGroup(statusToggleGroup);
        bientotCommenceeRadio.setToggleGroup(statusToggleGroup);
        encoursRadio.setToggleGroup(statusToggleGroup);
    }
}
