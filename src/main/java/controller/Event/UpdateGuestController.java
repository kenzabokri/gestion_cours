package controller.Event;

import entity.Event;
import entity.GuestEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.EventService;
import service.GuestEventService;

import java.io.File;
import java.io.IOException;

public class UpdateGuestController {
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField imageField;
    @FXML private ComboBox<Event> eventComboBox;
    @FXML private Button updateButton;
    @FXML private Button backButton;
    @FXML private Button selectImageButton;

    private final GuestEventService guestEventService = new GuestEventService();
    private final EventService eventService = new EventService();
    private GuestEvent guestToUpdate;

    public void setGuestToUpdate(GuestEvent guest) {
        this.guestToUpdate = guest;
        // Populate the form with guest data
        nomField.setText(guest.getNomGuest());
        prenomField.setText(guest.getPrenomGuest());
        emailField.setText(guest.getEmailGuest());
        phoneField.setText(String.valueOf(guest.getPhoneGuest()));
        imageField.setText(guest.getImageGuest());
        
        // Set the event in the combo box
        Event event = eventService.getById(guest.getEventId());
        if (event != null) {
            eventComboBox.getSelectionModel().select(event);
        }
    }

    @FXML
    public void initialize() {
        // Load events into the combo box
        ObservableList<Event> events = FXCollections.observableArrayList(eventService.getAll());
        eventComboBox.setItems(events);

        // Set cell factory to display event names
        eventComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNomEvent());
            }
        });

        eventComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNomEvent());
            }
        });
    }

    @FXML
    private void updateGuest() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String phoneText = phoneField.getText();
        String imagePath = imageField.getText();
        Event selectedEvent = eventComboBox.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || phoneText.isEmpty() || selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int phone = Integer.parseInt(phoneText);
            
            // Update the guest object
            guestToUpdate.setNomGuest(nom);
            guestToUpdate.setPrenomGuest(prenom);
            guestToUpdate.setEmailGuest(email);
            guestToUpdate.setPhoneGuest(phone);
            guestToUpdate.setImageGuest(imagePath);
            guestToUpdate.setEventId(selectedEvent.getId());

            // Update in database
            guestEventService.update(guestToUpdate);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Invité mis à jour avec succès !");
            goBack();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit être un nombre valide.");
        }
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/showguest.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 