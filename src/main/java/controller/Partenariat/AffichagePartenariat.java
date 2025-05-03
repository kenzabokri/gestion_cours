package controller.Partenariat;

import entity.Partenariat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import service.PartenariatService;

import java.io.File;

public class AffichagePartenariat {

    @FXML
    private TableView<Partenariat> tablePartenariat;

    @FXML
    private TableColumn<Partenariat, Integer> colId;

    @FXML
    private TableColumn<Partenariat, String> colNom;

    @FXML
    private TableColumn<Partenariat, String> colType;

    @FXML
    private TableColumn<Partenariat, String> colDesc;

    @FXML
    private TableColumn<Partenariat, String> colAdresse;

    @FXML
    private TableColumn<Partenariat, String> colLogo;

    @FXML
    private TableColumn<Partenariat, Integer> colPhone;

    @FXML
    private TableColumn<Partenariat, java.time.LocalDate> colDebut;

    @FXML
    private TableColumn<Partenariat, java.time.LocalDate> colFin;

    @FXML
    private TableColumn<Partenariat, Void> colActions;

    private final PartenariatService service = new PartenariatService();

    private int editingRow = -1;

    private void addButtonToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {

            private final Button btnEdit = new Button("✏️ ");
            private final Button btnDelete = new Button("🗑️ ");
            private final HBox hbox = new HBox(10, btnEdit, btnDelete);

            {
                hbox.setStyle("-fx-alignment: CENTER;");
                btnEdit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                btnEdit.setOnAction(event -> {
                    Partenariat p = getTableView().getItems().get(getIndex());
                    editingRow = getIndex();
                    tablePartenariat.setEditable(true);

                    colNom.setCellFactory(TextFieldTableCell.forTableColumn());
                    colNom.setOnEditCommit(evt -> {
                        Partenariat part = evt.getRowValue();
                        part.setNom_p(evt.getNewValue());
                        service.update(part);
                    });

                    colType.setCellFactory(TextFieldTableCell.forTableColumn());
                    colType.setOnEditCommit(evt -> {
                        Partenariat part = evt.getRowValue();
                        part.setType_p(evt.getNewValue());
                        service.update(part);
                    });

                    colDesc.setCellFactory(TextFieldTableCell.forTableColumn());
                    colDesc.setOnEditCommit(evt -> {
                        Partenariat part = evt.getRowValue();
                        part.setDesc_p(evt.getNewValue());
                        service.update(part);
                    });

                    colAdresse.setCellFactory(TextFieldTableCell.forTableColumn());
                    colAdresse.setOnEditCommit(evt -> {
                        Partenariat part = evt.getRowValue();
                        part.setAdresse_p(evt.getNewValue());
                        service.update(part);
                    });

                    colPhone.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                    colPhone.setOnEditCommit(evt -> {
                        Partenariat part = evt.getRowValue();
                        part.setPhone_p(evt.getNewValue());
                        service.update(part);
                    });

                    colDebut.setCellFactory(column -> new TableCell<>() {
                        private final DatePicker datePicker = new DatePicker();
                        {
                            datePicker.setOnAction(e -> {
                                Partenariat part = getTableView().getItems().get(getIndex());
                                part.setDuree_deb_p(datePicker.getValue());
                                service.update(part);
                                tablePartenariat.refresh();
                            });
                        }
                        @Override
                        protected void updateItem(java.time.LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else if (getIndex() == editingRow) {
                                datePicker.setValue(item);
                                setGraphic(datePicker);
                            } else {
                                setText(item != null ? item.toString() : "");
                                setGraphic(null);
                            }
                        }
                    });

                    colFin.setCellFactory(column -> new TableCell<>() {
                        private final DatePicker datePicker = new DatePicker();
                        {
                            datePicker.setOnAction(e -> {
                                Partenariat part = getTableView().getItems().get(getIndex());
                                part.setDuree_fin_p(datePicker.getValue());
                                service.update(part);
                                tablePartenariat.refresh();
                            });
                        }
                        @Override
                        protected void updateItem(java.time.LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else if (getIndex() == editingRow) {
                                datePicker.setValue(item);
                                setGraphic(datePicker);
                            } else {
                                setText(item != null ? item.toString() : "");
                                setGraphic(null);
                            }
                        }
                    });

                    colLogo.setCellFactory(column -> new TableCell<>() {
                        private final Button btnChoose = new Button();
                        private final ImageView imageView = new ImageView();

                        {
                            Image uploadIcon = new Image("/uploads/upload.png", 20, 20, true, true);
                            btnChoose.setGraphic(new ImageView(uploadIcon));
                            btnChoose.setStyle("-fx-background-color: transparent;");

                            btnChoose.setOnAction(e -> {
                                FileChooser fileChooser = new FileChooser();
                                fileChooser.setTitle("Choisir une image");
                                File file = fileChooser.showOpenDialog(getScene().getWindow());
                                if (file != null) {
                                    String path = file.getAbsolutePath();
                                    Partenariat part = getTableView().getItems().get(getIndex());
                                    part.setLogo_p(path);
                                    service.update(part);
                                    tablePartenariat.refresh();
                                }
                            });

                            imageView.setFitHeight(80);
                            imageView.setFitWidth(80);
                        }

                        @Override
                        protected void updateItem(String imagePath, boolean empty) {
                            super.updateItem(imagePath, empty);
                            if (empty || imagePath == null) {
                                setGraphic(null);
                                return;
                            }

                            if (getIndex() == editingRow) {
                                setGraphic(btnChoose);
                            } else {
                                try {
                                    imageView.setImage(new Image("file:" + imagePath));
                                    setGraphic(imageView);
                                } catch (Exception e) {
                                    setGraphic(null);
                                }
                            }
                        }
                    });

                    tablePartenariat.refresh();
                    tablePartenariat.edit(editingRow, colNom);
                });

                btnDelete.setOnAction(event -> {
                    Partenariat p = getTableView().getItems().get(getIndex());
                    service.delete(p.getId());
                    getTableView().getItems().remove(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    @FXML
    public void initialize() {
        tablePartenariat.setEditable(false);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom_p"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type_p"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc_p"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse_p"));
        colLogo.setCellValueFactory(new PropertyValueFactory<>("logo_p"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone_p"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("duree_deb_p"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("duree_fin_p"));

        colLogo.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);
            }
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || getIndex() == editingRow) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new Image("file:" + imagePath));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        ObservableList<Partenariat> data = FXCollections.observableArrayList(service.readAll());
        tablePartenariat.setItems(data);

        addButtonToTable();
    }
}