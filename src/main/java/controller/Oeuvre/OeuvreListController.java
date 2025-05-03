package controller.Oeuvre;

import controller.Oeuvre.OeuvreAfficherController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import entity.CategorieOeuvre;
import entity.Oeuvre;
import service.OeuvreService;

import java.io.IOException;
import java.util.Optional;

public class OeuvreListController {

    @FXML
    private TableView<Oeuvre> tableView;
    @FXML
    private TableColumn<Oeuvre, Integer> idCol;
    @FXML
    private TableColumn<Oeuvre, String> nomCol;
    @FXML
    private TableColumn<Oeuvre, String> descriptionCol;
    @FXML
    private TableColumn<Oeuvre, String> dateCol;
    @FXML
    private TableColumn<Oeuvre, String> categorieCol;
    @FXML
    private TableColumn<Oeuvre, Integer> likesCol;
    @FXML
    private TableColumn<Oeuvre, Void> actionCol;

    private final OeuvreService oeuvreService = new OeuvreService();

    @FXML
    public void initialize() {
        // Configure table columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nomOeuvre"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateDeCreation"));
        likesCol.setCellValueFactory(new PropertyValueFactory<>("likes"));

        // Configure action column
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button btnView = new Button("Voir");
            private final Button btnEdit = new Button("Modifier");
            private final Button btnDelete = new Button("Supprimer");
            private final HBox box = new HBox(10, btnView, btnEdit, btnDelete);

            {
                // Style buttons
                btnView.getStyleClass().add("btn-view");
                btnEdit.getStyleClass().add("btn-edit");
                btnDelete.getStyleClass().add("btn-delete");

                // Set button actions
                btnView.setOnAction(event -> {
                    Oeuvre oeuvre = getTableView().getItems().get(getIndex());
                    viewOeuvre(oeuvre);
                });

                btnEdit.setOnAction(event -> {
                    Oeuvre oeuvre = getTableView().getItems().get(getIndex());
                    editOeuvre(oeuvre);
                });

                btnDelete.setOnAction(event -> {
                    Oeuvre oeuvre = getTableView().getItems().get(getIndex());
                    deleteOeuvre(oeuvre);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        loadData();
    }

    private void loadData() {
        ObservableList<Oeuvre> oeuvres = FXCollections.observableArrayList(oeuvreService.readAll());
        tableView.setItems(oeuvres);
    }

    private void viewOeuvre(Oeuvre oeuvre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/OeuvreAfficher.fxml"));
            Parent root = loader.load();

            OeuvreAfficherController controller = loader.getController();
            controller.displayOeuvre(oeuvre.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'œuvre");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir les détails de l'œuvre");
        }
    }

    private void editOeuvre(Oeuvre oeuvre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/uploadOeuvre.fxml"));
            Parent root = loader.load();

            ModifierOeuvreController controller = loader.getController();
            controller.setOeuvre(oeuvre);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'œuvre");
            stage.showAndWait();

            // Refresh data after editing
            loadData();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'éditeur d'œuvre");
        }
    }

    private void deleteOeuvre(Oeuvre oeuvre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer cette œuvre ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer '" + oeuvre.getNomOeuvre() + "' ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            oeuvreService.delete(oeuvre);
            loadData();
            showAlert("Succès", "Œuvre supprimée avec succès");
        }
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }
    // Dans OeuvreListController.java
    public void loadOeuvres(CategorieOeuvre categorie) {
        ObservableList<Oeuvre> oeuvres = FXCollections.observableArrayList(
                oeuvreService.findByCategorie(categorie)
        );
        tableView.setItems(oeuvres);
    }

    @FXML
    private void handleAddOeuvre() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/AjouterOeuvre.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une œuvre");
            stage.showAndWait();

            // Refresh data after adding
            loadData();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'ajout d'œuvre");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}