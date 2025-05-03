package controller.CoursMateriel;

import entity.Cours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import service.CoursService;

import java.sql.Date;
import java.util.List;

public class ListeCoursController {
    @FXML
    private TableView<Cours> coursTableView;
    @FXML
    private TableColumn<Cours, String> colTypeAtelier;
    @FXML
    private TableColumn<Cours, String> colDescription_cours;
    @FXML
    private TableColumn<Cours, String> colDuree;
    @FXML
    private TableColumn<Cours, Date> colDate_cours;
    @FXML
    private TableColumn<Cours, String> colImage_cours;
    @FXML
    private TableColumn<Cours, String> colLien;
    @FXML
    private TableColumn<Cours, Void> colSupprimer;
    @FXML
    private TableColumn<Cours, Void> colModifier;
    @FXML
    private TextField searchField;
    @FXML
    private Button retourButton;

    private final CoursService coursService = new CoursService();
    private ObservableList<Cours> coursList;
    private FilteredList<Cours> filteredList;

    @FXML
    private void initialize() {
        coursTableView.setEditable(true);

        // Bind des colonnes
        colTypeAtelier.setCellValueFactory(new PropertyValueFactory<>("type_atelier"));
        colDescription_cours.setCellValueFactory(new PropertyValueFactory<>("description_cours"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree_cours"));
        colDate_cours.setCellValueFactory(new PropertyValueFactory<>("date_cours"));
        colImage_cours.setCellValueFactory(new PropertyValueFactory<>("image_cours"));
        colLien.setCellValueFactory(new PropertyValueFactory<>("lien_video_cours"));

        // Cellules éditables (texte)
        colTypeAtelier.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescription_cours.setCellFactory(TextFieldTableCell.forTableColumn());
        colDuree.setCellFactory(TextFieldTableCell.forTableColumn());

        // Cellule Date
        colDate_cours.setCellFactory(column -> new TableCell<Cours, Date>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setOnAction(e -> commitEdit(Date.valueOf(datePicker.getValue())));
            }

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else if (isEditing()) {
                    datePicker.setValue(item.toLocalDate());
                    setGraphic(datePicker);
                } else {
                    setText(item.toString());
                    setGraphic(null);
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                if (getItem() != null) {
                    datePicker.setValue(getItem().toLocalDate());
                    setGraphic(datePicker);
                    setText(null);
                }
            }

            @Override
            public void commitEdit(Date newValue) {
                super.commitEdit(newValue);
                Cours cours = getTableView().getItems().get(getIndex());
                cours.setDate_cours(newValue);
                coursService.update(cours);
            }
        });

        // Cellule Image
        colImage_cours.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private final Button editButton = new Button("Modifier");
            private final StackPane stackPane = new StackPane();

            {
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                editButton.setVisible(false);
                StackPane.setAlignment(editButton, Pos.TOP_RIGHT);
                StackPane.setMargin(editButton, new Insets(5));
                stackPane.getChildren().addAll(imageView, editButton);

                imageView.setOnMouseClicked(e -> editButton.setVisible(true));

                editButton.setOnAction(e -> {
                    javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                    fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                    java.io.File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());

                    if (selectedFile != null) {
                        String newPath = selectedFile.getAbsolutePath();
                        Cours cours = getTableView().getItems().get(getIndex());
                        cours.setImage_cours(newPath);
                        coursService.update(cours);
                        imageView.setImage(new Image("file:" + newPath));
                        editButton.setVisible(false);
                    }
                });
            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image("file:" + imagePath, true));
                    editButton.setVisible(false);
                    setGraphic(stackPane);
                }
            }
        });

        // Cellule Lien (hyperlink éditable)
        colLien.setCellFactory(column -> new TableCell<>() {
            private final Hyperlink hyperlink = new Hyperlink();
            private final TextField textField = new TextField();

            {
                hyperlink.setOnAction(e -> {
                    try {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(hyperlink.getText()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                textField.setOnAction(e -> commitEdit(textField.getText()));
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) commitEdit(textField.getText());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else if (isEditing()) {
                    textField.setText(item);
                    setGraphic(textField);
                } else {
                    hyperlink.setText(item);
                    setGraphic(hyperlink);
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                textField.setText(getItem());
                setGraphic(textField);
                textField.requestFocus();
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                Cours cours = getTableView().getItems().get(getIndex());
                cours.setLien_video_cours(newValue);
                coursService.update(cours);
            }
        });

        // Événements de modification (texte uniquement)
        colTypeAtelier.setOnEditCommit(e -> {
            e.getRowValue().setType_atelier(e.getNewValue());
            coursService.update(e.getRowValue());
        });

        colDescription_cours.setOnEditCommit(e -> {
            e.getRowValue().setDescription_cours(e.getNewValue());
            coursService.update(e.getRowValue());
        });

        colDuree.setOnEditCommit(e -> {
            e.getRowValue().setDuree_cours(e.getNewValue());
            coursService.update(e.getRowValue());
        });

        colLien.setOnEditCommit(e -> {
            e.getRowValue().setLien_video_cours(e.getNewValue());
            coursService.update(e.getRowValue());
        });

        // Charger les données
        loadCoursData();

        // Ajout des boutons
        addButtonToTable();
        addModifierButtonToTable();

        // Recherche dynamique
        setupSearchFilter();
    }


    private void loadCoursData() {
        List<Cours> cours = coursService.readAll();
        coursList = FXCollections.observableArrayList(cours);
        filteredList = new FilteredList<>(coursList, p -> true);

        // 🔁 Trie ajouté ici
        javafx.collections.transformation.SortedList<Cours> sortedList = new javafx.collections.transformation.SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(coursTableView.comparatorProperty());

        coursTableView.setItems(sortedList);
    }


    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal == null ? "" : newVal.toLowerCase();

            filteredList.setPredicate(cours -> {
                if (cours == null) return false;

                // Filtrer uniquement si le champ correspond
                boolean match = false;

                if (cours.getType_atelier() != null && cours.getType_atelier().toLowerCase().contains(lower)) match = true;
                if (cours.getDescription_cours() != null && cours.getDescription_cours().toLowerCase().contains(lower)) match = true;
                if (cours.getDuree_cours() != null && cours.getDuree_cours().toLowerCase().contains(lower)) match = true;
                if (cours.getLien_video_cours() != null && cours.getLien_video_cours().toLowerCase().contains(lower)) match = true;
                if (cours.getDate_cours() != null && cours.getDate_cours().toString().contains(lower)) match = true;

                return match;
            });

            // Important : forcer le rafraîchissement
            coursTableView.refresh();
        });
    }


    private void addButtonToTable() {
        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btn.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    coursService.delete(cours.getId());
                    loadCoursData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void addModifierButtonToTable() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                btn.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    coursService.update(cours);
                    loadCoursData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    @FXML
    private void naviguerVersCoursMateriel() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/CoursMateriel/CoursMaterielView.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Affectation Cours - Matériel");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
            Stage currentStage = (Stage) retourButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void afficherMateriel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CoursMateriel/Listemateriel.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des matériels");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) retourButton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage des matériels : " + e.getMessage());
        }
    }
    @FXML
    private void retourajout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CoursMateriel/interface.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("ajout");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) retourButton.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage : " + e.getMessage());
        }
    }
}
