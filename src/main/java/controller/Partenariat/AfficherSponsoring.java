package controller.Partenariat;

import entity.sponsoring;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import service.SponsoringService;
import entity.Partenariat;
import java.time.LocalDate;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class AfficherSponsoring {


    @FXML
    private TableView<sponsoring> tableSponsoring;

    @FXML
    private TableColumn<sponsoring, Integer> colId;

    @FXML
    private TableColumn<sponsoring, String> colType;

    @FXML
    private TableColumn<sponsoring, LocalDate> colDateDebut;

    @FXML
    private TableColumn<sponsoring, LocalDate> colDateFin;

    @FXML
    private TableColumn<sponsoring, String> colPartenariat;

    @FXML
    private TableColumn<sponsoring, Void> col_action;


    private final SponsoringService sponsoringService = new SponsoringService();

    private void addButtonToTable() {
        col_action.setCellFactory(param -> new TableCell<>() {

            private final javafx.scene.control.Button btnEdit = new javafx.scene.control.Button("✏️ ");
            private final javafx.scene.control.Button btnDelete = new javafx.scene.control.Button("🗑️ ");
            private final javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(10, btnEdit, btnDelete);

            {
                // Style optionnel
                hbox.setStyle("-fx-alignment: CENTER;");
                btnEdit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                btnDelete.setOnAction(event -> {
                    sponsoring s = getTableView().getItems().get(getIndex());
                    // Supprimer l’élément avec le service
                    sponsoringService.delete(s.getId());
                    getTableView().getItems().remove(s);
                    System.out.println("Supprimé: " );
                });

                btnEdit.setOnAction(event -> {
                    sponsoring s = getTableView().getItems().get(getIndex());

                    // Convertir les colonnes en champs éditables
                    colType.setCellFactory(TextFieldTableCell.forTableColumn());

                    // Ne rendre éditable que la ligne sélectionnée pour les dates
                    colDateDebut.setCellFactory(param -> new TableCell<sponsoring, LocalDate>() {
                        private final DatePicker datePicker = new DatePicker();

                        @Override
                        protected void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else if (getIndex() == getTableRow().getIndex()) {
                                datePicker.setValue(item);
                                setGraphic(datePicker);
                                datePicker.setOnAction(event2 -> {
                                    s.setDate_debut_s(datePicker.getValue());
                                    sponsoringService.update(s);
                                });
                            } else {
                                setGraphic(null);
                            }
                        }
                    });

                    colDateFin.setCellFactory(param -> new TableCell<sponsoring, LocalDate>() {
                        private final DatePicker datePicker = new DatePicker();

                        @Override
                        protected void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else if (getIndex() == getTableRow().getIndex()) {
                                datePicker.setValue(item);
                                setGraphic(datePicker);
                                datePicker.setOnAction(event2 -> {
                                    s.setDate_fin_s(datePicker.getValue());
                                    sponsoringService.update(s);
                                });
                            } else {
                                setGraphic(null);
                            }
                        }
                    });

                    // Rendre les cellules éditables
                    tableSponsoring.setEditable(true);
                    tableSponsoring.edit(getIndex(), colType); // On commence par éditer la colonne Type

                    // Ajouter un listener pour mettre à jour l'entité après modification
                    colType.setOnEditCommit(event2 -> {
                        s.setType_s(event2.getNewValue());
                        sponsoringService.update(s);
                    });
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });
    }



    @FXML
    public void initialize() {


        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type_s"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("date_debut_s"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("date_fin_s"));
        colPartenariat.setCellValueFactory(new PropertyValueFactory<>("id_p"));


        // Charger les données
        ObservableList<sponsoring> data = FXCollections.observableArrayList(sponsoringService.readAll());
        tableSponsoring.setItems(data);

        addButtonToTable();


    }


}