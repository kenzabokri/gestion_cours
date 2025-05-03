package controller.Event;

import entity.Event;
import entity.GuestEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import service.EventService;
import service.GuestEventService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ShowGuestEventController {

    @FXML private TableView<GuestEvent> tableGuest;
    @FXML private TableColumn<GuestEvent, Integer> colId;
    @FXML private TableColumn<GuestEvent, String> colNom;
    @FXML private TableColumn<GuestEvent, String> colPrenom;
    @FXML private TableColumn<GuestEvent, String> colEmail;
    @FXML private TableColumn<GuestEvent, Integer> colPhone;
    @FXML private TableColumn<GuestEvent, String> colImage;
    @FXML private TableColumn<GuestEvent, String> colEventId;
    @FXML private TableColumn<GuestEvent, Void> colActions;

    @FXML private TextField searchGuestField;
    @FXML private Button btnAdd;
    @FXML private Button btnBack;
    @FXML private Button btnSurface;
    @FXML private Button btnRefresh;

    private final GuestEventService guestService = new GuestEventService();
    private final EventService eventService = new EventService();
    private final ObservableList<GuestEvent> guestList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            setupTableColumns();
            setupSearchField();
            loadGuests();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        try {
            // Setup basic columns
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nomGuest"));
            colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomGuest"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("emailGuest"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneGuest"));
            colImage.setCellValueFactory(new PropertyValueFactory<>("imageGuest"));
            
            // Setup event column to show event name instead of ID
            colEventId.setCellValueFactory(cellData -> {
                try {
                    Integer eventId = cellData.getValue().getEventId();
                    if (eventId != null) {
                        Event event = eventService.getById(eventId);
                        return new SimpleStringProperty(event != null ? event.getNomEvent() : "N/A");
                    }
                    return new SimpleStringProperty("N/A");
                } catch (Exception e) {
                    return new SimpleStringProperty("Erreur");
                }
            });

            setupActionColumn();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la configuration des colonnes: " + e.getMessage());
        }
    }

    @FXML
    private void onSearchGuest(KeyEvent event) {
        String keyword = searchGuestField.getText();
        if (keyword == null || keyword.isEmpty()) {
            tableGuest.setItems(guestList);
        } else {
            filterGuests(keyword);
        }
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("✏️");
            private final Button btnDelete = new Button("🗑️");
            private final HBox hbox = new HBox(10, btnEdit, btnDelete);

            {
                // Style buttons
                hbox.setStyle("-fx-alignment: CENTER;");
                btnEdit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");

                // Add tooltips
                btnEdit.setTooltip(new Tooltip("Modifier l'invité"));
                btnDelete.setTooltip(new Tooltip("Supprimer l'invité"));

                btnEdit.setOnAction(event -> {
                    GuestEvent guest = getTableView().getItems().get(getIndex());
                    if (guest != null) {
                        updateGuest(guest);
                    }
                });

                btnDelete.setOnAction(event -> {
                    GuestEvent guest = getTableView().getItems().get(getIndex());
                    if (guest != null) {
                        deleteGuest(guest);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    private void setupSearchField() {
        // Keep both the listener and the onKeyReleased method for robustness
        searchGuestField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tableGuest.setItems(guestList);
            } else {
                filterGuests(newValue);
            }
        });
    }

    private void filterGuests(String keyword) {
        try {
            keyword = keyword.toLowerCase().trim();
            ObservableList<GuestEvent> filtered = FXCollections.observableArrayList();
            for (GuestEvent guest : guestList) {
                if (guest.getNomGuest().toLowerCase().contains(keyword) ||
                    guest.getPrenomGuest().toLowerCase().contains(keyword) ||
                    guest.getEmailGuest().toLowerCase().contains(keyword)) {
                    filtered.add(guest);
                }
            }
            tableGuest.setItems(filtered);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du filtrage: " + e.getMessage());
        }
    }

    @FXML
    private void refreshTable() {
        try {
            loadGuests();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "La liste des invités a été actualisée.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'actualisation: " + e.getMessage());
        }
    }

    private void loadGuests() {
        try {
            List<GuestEvent> guests = guestService.getAll();
            guestList.setAll(guests);
            tableGuest.setItems(guestList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la liste des invités: " + e.getMessage());
        }
    }

    private void updateGuest(GuestEvent selectedGuest) {
        if (selectedGuest == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un invité à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/updateguest.fxml"));
            Parent root = loader.load();
            
            UpdateGuestController controller = loader.getController();
            controller.setGuestToUpdate(selectedGuest);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Invité");
            stage.show();
            
            stage.setOnHidden(e -> refreshTable());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la page de modification: " + e.getMessage());
        }
    }

    private void deleteGuest(GuestEvent selectedGuest) {
        if (selectedGuest == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un invité à supprimer.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Supprimer l'invité");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer " + 
                                   selectedGuest.getPrenomGuest() + " " + 
                                   selectedGuest.getNomGuest() + " ?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                guestService.delete(selectedGuest);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'invité a été supprimé avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void goToAddGuest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/addguest.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un invité");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la page d'ajout d'invité: " + e.getMessage());
        }
    }

    @FXML
    private void goToSurface(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/interface.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSurface.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des événements");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'interface principale: " + e.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/addguest.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un invité");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du retour à la page précédente: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
