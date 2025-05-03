package controller.Event;

import entity.Event;
import entity.GuestEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.List;

public class AddGuestController {

    @FXML
    private TextField nomGuestField;

    @FXML
    private TextField prenomGuestField;

    @FXML
    private TextField emailGuestField;

    @FXML
    private TextField phoneGuestField;

    @FXML
    private TextField imageGuestField;
    @FXML
    private Button btnViewGuests;

    @FXML
    private ComboBox<Event> eventComboBox;

    private final GuestEventService guestEventService = new GuestEventService();
    private final EventService eventService = new EventService();

    @FXML
    public void initialize() {
        // Load all events into the ComboBox
        List<Event> events = eventService.getAll();
        ObservableList<Event> eventList = FXCollections.observableArrayList(events);
        eventComboBox.setItems(eventList);
        
        // Set the cell factory to display event names
        eventComboBox.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomEvent());
                }
            }
        });
        
        // Set the button cell to display event names
        eventComboBox.setButtonCell(new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomEvent());
                }
            }
        });
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageGuestField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void addGuest() {
        String nom = nomGuestField.getText();
        String prenom = prenomGuestField.getText();
        String email = emailGuestField.getText();
        String phoneText = phoneGuestField.getText();
        String imagePath = imageGuestField.getText();
        Event selectedEvent = eventComboBox.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || phoneText.isEmpty() || selectedEvent == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Validate that the phone number is a valid integer
            Integer.parseInt(phoneText);
            // Pass eventId instead of the entire Event object
            int eventId = selectedEvent.getId();
            GuestEvent guest = new GuestEvent(nom, prenom, email, phoneText, imagePath, eventId);
            guestEventService.create(guest);

            showAlert(Alert.AlertType.INFORMATION, "Invité ajouté avec succès !");

            // Optionnel : vider les champs après ajout
            nomGuestField.clear();
            prenomGuestField.clear();
            emailGuestField.clear();
            phoneGuestField.clear();
            imageGuestField.clear();
            eventComboBox.getSelectionModel().clearSelection();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Le numéro de téléphone doit être un nombre valide.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToShowGuests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/showguest.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnViewGuests.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement de la page des invités.");
            alert.showAndWait();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/showevent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du retour à la liste des événements.");
            alert.showAndWait();
        }
    }
}
