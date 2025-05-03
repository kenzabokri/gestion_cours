package controller.Categorie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import entity.CategorieOeuvre;
import service.CategorieOeuvreService;

import java.io.IOException;
import java.util.Optional;

public class AfficherCategorieController {

    @FXML private TableView<CategorieOeuvre> tableView;
    @FXML private TableColumn<CategorieOeuvre, Integer> idCol;
    @FXML private TableColumn<CategorieOeuvre, String> nomCol;
    @FXML private TableColumn<CategorieOeuvre, String> styleCol;
    @FXML private TableColumn<CategorieOeuvre, String> techniqueCol;
    @FXML private TableColumn<CategorieOeuvre, Void> actionCol;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));
        styleCol.setCellValueFactory(new PropertyValueFactory<>("styleCategorie"));
        techniqueCol.setCellValueFactory(new PropertyValueFactory<>("techniqueCategorie"));

        // Configuration de la colonne d'actions
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox box = new HBox(10, btnModifier, btnSupprimer);

            {
                // Style des boutons
                btnModifier.getStyleClass().add("btn-modifier");
                btnSupprimer.getStyleClass().add("btn-supprimer");

                // Gestion des événements
                btnModifier.setOnAction(event -> {
                    CategorieOeuvre selected = getTableView().getItems().get(getIndex());
                    modifierCategorie(selected);
                });

                btnSupprimer.setOnAction(event -> {
                    CategorieOeuvre selected = getTableView().getItems().get(getIndex());
                    supprimerCategorie(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        chargerDonnees();
    }

    private void chargerDonnees() {
        try {
            CategorieOeuvreService service = new CategorieOeuvreService();
            ObservableList<CategorieOeuvre> categories = FXCollections.observableArrayList(service.readAll());
            tableView.setItems(categories);
        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible de charger les données: " + e.getMessage());
        }
    }

    private void modifierCategorie(CategorieOeuvre categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/ModifierCategorie.fxml"));
            Parent root = loader.load();

            ModifierCategorieController controller = loader.getController();
            controller.setCategorie(categorie);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Catégorie");
            stage.showAndWait();

            // Rafraîchir les données après modification
            chargerDonnees();
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
        }
    }

    private void supprimerCategorie(CategorieOeuvre categorie) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer la catégorie '" + categorie.getNomCategorie() + "' ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                CategorieOeuvreService service = new CategorieOeuvreService();


                service.delete(categorie);
                chargerDonnees();
                afficherSucces("Succès", "Catégorie supprimée avec succès");
            } catch (Exception e) {
                afficherErreur("Erreur", "Échec de la suppression: " + e.getMessage());
            }
        }
    }


    @FXML
    private void redirectToAddCategory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/AjouterCategorie.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Catégorie");
            stage.show();
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'ouvrir la page d'ajout: " + e.getMessage());
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void ouvrirDrawer(ActionEvent event) {
        // Exemple simple : affichage d'un message ou future ouverture d'un panneau latéral
        System.out.println("Drawer ouvert !");
        // Tu peux ensuite charger une VBox latérale ou une nouvelle scène avec AnchorPane.setLeft(...)
    }


    @FXML
    private void redirectToAddOeuvre(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/AjouterOeuvre.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Œuvre");
            stage.show();
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible d'ouvrir la page d'ajout d'œuvre: " + e.getMessage());
        }
    }



}