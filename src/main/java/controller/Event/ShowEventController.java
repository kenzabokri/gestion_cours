package controller.Event;

import entity.Event;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import service.EventService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ShowEventController {

    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> nomColumn;
    @FXML private TableColumn<Event, LocalDate> dateColumn;
    @FXML private TableColumn<Event, String> localisationColumn;
    @FXML private TableColumn<Event, String> statutColumn;
    @FXML private TableColumn<Event, String> descriptionColumn;
    @FXML private TableColumn<Event, Void> colActions;
    @FXML private Button backButton;
    @FXML private TextField searchField;

    private final EventService eventService = new EventService();
    private final ObservableList<Event> eventList = FXCollections.observableArrayList(); // ✅ Manquait cette ligne

    @FXML
    private void initialize() {
        eventTable.setEditable(true);

        // Configuration des colonnes
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEvent()));
        nomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nomColumn.setOnEditCommit(event -> {
            Event e = event.getRowValue();
            e.setNomEvent(event.getNewValue());
            eventService.update(e);
        });

        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateEvent()));
        dateColumn.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setOnAction(e -> {
                    Event rowData = getTableView().getItems().get(getIndex());
                    rowData.setDateEvent(datePicker.getValue());
                    eventService.update(rowData);
                });
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(item);
                    setGraphic(datePicker);
                }
            }
        });

        localisationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalisationEvent()));
        localisationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        localisationColumn.setOnEditCommit(event -> {
            Event e = event.getRowValue();
            e.setLocalisationEvent(event.getNewValue());
            eventService.update(e);
        });

        statutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatuEvent()));
        statutColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statutColumn.setOnEditCommit(event -> {
            Event e = event.getRowValue();
            e.setStatuEvent(event.getNewValue());
            eventService.update(e);
        });

        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescriptionEvent()));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit(event -> {
            Event e = event.getRowValue();
            e.setDescriptionEvent(event.getNewValue());
            eventService.update(e);
        });

        addButtonToTable();
        loadEvents();
    }

    private void addButtonToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("✏️");
            private final Button btnDelete = new Button("🗑️");
            private final HBox hbox = new HBox(10, btnEdit, btnDelete);

            {
                hbox.setStyle("-fx-alignment: CENTER;");
                btnEdit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                btnEdit.setOnAction(event -> {
                    Event selectedEvent = getTableView().getItems().get(getIndex());
                    System.out.println("Modifier: " + selectedEvent.getNomEvent());
                    // Implémentation future d'une fenêtre de modification ?
                });

                btnDelete.setOnAction(event -> {
                    Event selectedEvent = getTableView().getItems().get(getIndex());
                    eventService.delete(selectedEvent);
                    loadEvents(); // recharge après suppression
                    System.out.println("Supprimé: " + selectedEvent.getNomEvent());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    private void loadEvents() {
        List<Event> events = eventService.getAll();
        eventList.setAll(events);
        eventTable.setItems(eventList);
    }

    @FXML
    private void onSearch(KeyEvent event) {
        String keyword = searchField.getText().toLowerCase().trim();

        if (keyword.isEmpty()) {
            eventTable.setItems(eventList);
            return;
        }

        ObservableList<Event> filteredList = FXCollections.observableArrayList();
        for (Event e : eventList) {
            if (e.getNomEvent().toLowerCase().contains(keyword)
                    || e.getLocalisationEvent().toLowerCase().contains(keyword)) {
                filteredList.add(e);
            }
        }

        eventTable.setItems(filteredList);
    }

    @FXML
    private void goBackToAddEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/interface.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du retour à l'interface principale.");
            alert.showAndWait();
        }
    }
    @FXML
    private void showStat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventGuest/statistique.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Statistiques des événements");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
