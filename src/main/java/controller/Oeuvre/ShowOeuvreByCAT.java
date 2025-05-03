package controller.Oeuvre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import entity.CategorieOeuvre;
import entity.Oeuvre;
import service.OeuvreService;

import java.io.File;
import java.io.IOException;

public class ShowOeuvreByCAT {

    @FXML private Label categoryLabel;
    @FXML private TableView<Oeuvre> oeuvreTable;
    @FXML private TableColumn<Oeuvre, String> nomColumn;
    @FXML private TableColumn<Oeuvre, String> descriptionColumn;
    @FXML private TableColumn<Oeuvre, String> dateColumn;
    @FXML private TableColumn<Oeuvre, Integer> likesColumn;
    @FXML private ImageView imageView;

    private CategorieOeuvre currentCategory;
    private final OeuvreService oeuvreService = new OeuvreService();
    private final ObservableList<Oeuvre> oeuvreList = FXCollections.observableArrayList();

    public void setCategory(CategorieOeuvre category) {
        this.currentCategory = category;
        initializeTable();
        loadOeuvres();
        updateUI();
    }


    private void initializeTable() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomOeuvre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDeCreation"));
        likesColumn.setCellValueFactory(new PropertyValueFactory<>("likes"));

        oeuvreTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showOeuvreDetails(newValue));
    }

    private void loadOeuvres() {
        oeuvreList.clear();
        oeuvreList.addAll(oeuvreService.findByCategorie(currentCategory));
    }

    private void updateUI() {
        categoryLabel.setText("Œuvres dans la catégorie: " + currentCategory.getNomCategorie());
        oeuvreTable.setItems(oeuvreList);
    }

    private void showOeuvreDetails(Oeuvre oeuvre) {
        if (oeuvre != null && oeuvre.getFichierMultimedia() != null) {
            String imagePath = oeuvreService.getUploadDir() + oeuvre.getFichierMultimedia();
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
                imageView.setPreserveRatio(true);
            } else {
                imageView.setImage(null);
            }
        } else {
            imageView.setImage(null);
        }
    }

    @FXML
    private void handleShowDetails() {
        Oeuvre selected = oeuvreTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/oeuvreAfficher.fxml"));
                Parent root = loader.load();

                OeuvreAfficherController controller = loader.getController();
                controller.displayOeuvre(selected.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de l'œuvre");
                stage.show();

            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir les détails de l'œuvre");
            }
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une œuvre");
        }
    }

    @FXML
    private void handleClose() {
        ((Stage) categoryLabel.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}