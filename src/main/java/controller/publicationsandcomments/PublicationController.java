package controller.publicationsandcomments;

import service.PublicationForumService;
import entity.PublicationForum;
import entity.rubrique;
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

import java.io.IOException;
import java.util.List;

public class PublicationController {

    @FXML
    private TableView<PublicationForum> publicationTable;

    @FXML
    private TableColumn<PublicationForum, String> nomColumn;

    @FXML
    private TableColumn<PublicationForum, String> contenuColumn;

    @FXML
    private TableColumn<PublicationForum, String> imageColumn;

    @FXML
    private TableColumn<PublicationForum, String> dateColumn;

    @FXML
    private TableColumn<PublicationForum, Void> actionsCol;

    private rubrique rubrique;

    private final PublicationForumService publicationService = new PublicationForumService();

    public void setRubrique(rubrique rubrique) {
        this.rubrique = rubrique;
        loadPublications();  // Assurer que les publications sont chargées après la rubrique définie
    }

    public void loadPublications() {
        if (rubrique != null) {
            List<PublicationForum> publications = publicationService.getAllByRubriqueId(rubrique.getId());
            ObservableList<PublicationForum> observableList = FXCollections.observableArrayList(publications);
            publicationTable.setItems(observableList);
        } else {
            showAlert("Rubrique non définie", "Aucune rubrique sélectionnée pour charger les publications.");
        }
    }

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomPublicationForum"));
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("contenuPublicationForum"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePublicationForum"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreationPubForum"));

        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");
            private final Button voirCommentairesBtn = new Button("Voir commentaires");

            {
                // Styles
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                voirCommentairesBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    PublicationForum pub = getTableView().getItems().get(getIndex());
                    openEditPublicationWindow(pub);
                });

                deleteBtn.setOnAction(e -> {
                    PublicationForum pub = getTableView().getItems().get(getIndex());
                    deletePublication(pub);
                });

                voirCommentairesBtn.setOnAction(e -> {
                    PublicationForum pub = getTableView().getItems().get(getIndex());
                    openCommentairesWindow(pub);
                });
            }

            private final HBox pane = new HBox(10, editBtn, deleteBtn, voirCommentairesBtn);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    @FXML
    private void handleAddPublication() {
        if (rubrique != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/AddPublication.fxml"));
                Parent root = loader.load();

                AddPublicationController controller = loader.getController();
                controller.setRubriqueId(rubrique.getId());  // Passer l'ID de la rubrique au contrôleur d'ajout
                controller.setPublicationController(this);

                Stage stage = new Stage();
                stage.setTitle("Ajouter une Publication");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une rubrique avant d'ajouter une publication.");
        }
    }

    private void openEditPublicationWindow(PublicationForum publication) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/EditPublication.fxml"));
            Parent root = loader.load();

            EditPublicationController controller = loader.getController();
            controller.setPublication(publication);
            controller.setPublicationController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier une Publication");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCommentairesWindow(PublicationForum publicationForum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/Commentaires.fxml"));
            Parent root = loader.load();

            CommentaireController controller = loader.getController();
            controller.setPublication(publicationForum);

            Stage stage = new Stage();
            stage.setTitle("Commentaires de : " + publicationForum.getNomPublicationForum());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePublication(PublicationForum pub) {
        if (pub != null) {
            publicationService.delete(pub);
            loadPublications();
        } else {
            showAlert("Erreur", "La publication sélectionnée est invalide.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshPublications() {
        loadPublications();
    }
}