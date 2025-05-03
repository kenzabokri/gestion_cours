package controller.CoursMateriel;

import entity.Materiel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import service.MaterielService;

import java.util.List;

public class ListeMaterielController {

    @FXML private TableView<Materiel> tableMateriel;
    @FXML private TableColumn<Materiel, String> colNom;
    @FXML private TableColumn<Materiel, String> colDescription_materiel;
    @FXML private TableColumn<Materiel, Float> colPrix;
    @FXML private TableColumn<Materiel, String> colLieu;
    @FXML private TableColumn<Materiel, String> colImage_materiel;
    @FXML private TableColumn<Materiel, Void> colSupprimer;
    @FXML private TableColumn<Materiel, Void> colModifier;
    @FXML private TextField searchField;


    private ObservableList<Materiel> materielList;
    private FilteredList<Materiel> filteredList;
    private final MaterielService materielService = new MaterielService();

    @FXML
    public void initialize() {
        tableMateriel.setEditable(true);

        // Liens entre colonnes et propriétés
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom_materiel"));
        colDescription_materiel.setCellValueFactory(new PropertyValueFactory<>("description_materiel"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix_materiel"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu_achat_materiel"));
        colImage_materiel.setCellValueFactory(new PropertyValueFactory<>("image_materiel"));

        // Cellules éditables
        colNom.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescription_materiel.setCellFactory(TextFieldTableCell.forTableColumn());
        colPrix.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colLieu.setCellFactory(TextFieldTableCell.forTableColumn());

        // Image
        colImage_materiel.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            private final Button editButton = new Button("Modifier");
            private final StackPane stackPane = new StackPane();

            {
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                editButton.setVisible(false);
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                StackPane.setAlignment(editButton, Pos.TOP_RIGHT);
                StackPane.setMargin(editButton, new Insets(5));
                stackPane.getChildren().addAll(imageView, editButton);

                imageView.setOnMouseClicked(event -> {
                    if (!isEmpty()) {
                        editButton.setVisible(true);
                    }
                });

                editButton.setOnAction(e -> {
                    javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                    fileChooser.setTitle("Choisir une nouvelle image");
                    fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                    java.io.File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
                    if (selectedFile != null) {
                        String newPath = selectedFile.getAbsolutePath();
                        Materiel mat = getTableView().getItems().get(getIndex());
                        mat.setImage_materiel(newPath);
                        materielService.update(mat);
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
                    try {
                        imageView.setImage(new Image("file:" + imagePath));
                        editButton.setVisible(false);
                        setGraphic(stackPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                        setGraphic(null);
                    }
                }
            }
        });

        // Édition des cellules
        colNom.setOnEditCommit(event -> {
            Materiel m = event.getRowValue();
            m.setNom_materiel(event.getNewValue());
            materielService.update(m);
        });

        colDescription_materiel.setOnEditCommit(event -> {
            Materiel m = event.getRowValue();
            m.setDescription_materiel(event.getNewValue());
            materielService.update(m);
        });

        colPrix.setOnEditCommit(event -> {
            Materiel m = event.getRowValue();
            m.setPrix_materiel(event.getNewValue());
            materielService.update(m);
        });

        colLieu.setOnEditCommit(event -> {
            Materiel m = event.getRowValue();
            m.setLieu_achat_materiel(event.getNewValue());
            materielService.update(m);
        });

        // Charger les données
        loadMaterielData();

        // Recherche
        setupSearchFilter();

        // Boutons
        ajouterBoutonSupprimer();
        ajouterBoutonModifier();
    }

    private void loadMaterielData() {
        List<Materiel> materiels = materielService.readAll();
        materielList = FXCollections.observableArrayList(materiels);
        filteredList = new FilteredList<>(materielList, p -> true);

        // 🔁 Trie ajouté ici
        javafx.collections.transformation.SortedList<Materiel> sortedList = new javafx.collections.transformation.SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableMateriel.comparatorProperty());

        tableMateriel.setItems(sortedList);
    }


    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lower = newValue == null ? "" : newValue.toLowerCase();

            filteredList.setPredicate(materiel -> {
                if (materiel == null) return false;

                boolean matchNom = materiel.getNom_materiel() != null && materiel.getNom_materiel().toLowerCase().contains(lower);
                boolean matchDesc = materiel.getDescription_materiel() != null && materiel.getDescription_materiel().toLowerCase().contains(lower);
                boolean matchPrix = String.valueOf(materiel.getPrix_materiel()).toLowerCase().contains(lower);
                boolean matchLieu = materiel.getLieu_achat_materiel() != null && materiel.getLieu_achat_materiel().toLowerCase().contains(lower);

                return matchNom || matchDesc || matchPrix || matchLieu;
            });
        });
    }

    private void ajouterBoutonSupprimer() {
        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btn.setOnAction(event -> {
                    Materiel materiel = getTableView().getItems().get(getIndex());
                    materielService.delete(materiel.getId());
                    getTableView().getItems().remove(materiel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void ajouterBoutonModifier() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                btn.setOnAction(event -> {
                    Materiel materiel = getTableView().getItems().get(getIndex());
                    materielService.update(materiel);
                    System.out.println("✅ Materiel mis à jour: ID " + materiel.getId());
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void retourajout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CoursMateriel/AjoutMateriel.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("ajout");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage : " + e.getMessage());
        }
    }
    @FXML
    private void affichercours() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CoursMateriel/ListeCours.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("la liste des cours");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage des cours : " + e.getMessage());
        }
    }
}
