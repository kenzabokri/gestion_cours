package controller.frontend;

import controller.publicationsandcomments.PublicationController;
import entity.rubrique;
import entity.forum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import service.rubriqueservice;

import java.io.IOException;
import java.util.List;

public class RubriqueFrontController {

    @FXML
    private TableView<rubrique> rubriqueTable;

    @FXML
    private TableColumn<rubrique, String> nomRubriqueColumn;

    @FXML
    private TableColumn<rubrique, String> descriptionColumn;

    @FXML
    private TableColumn<rubrique, String> dateColumn;

    @FXML
    private TableColumn<rubrique, Void> actionColumn;

    private final rubriqueservice rubriqueService = new rubriqueservice();
    private forum forum;

    public void setForum(forum forum) {
        this.forum = forum;
        loadRubriques();
    }

    @FXML
    public void initialize() {
        if (nomRubriqueColumn == null || descriptionColumn == null || dateColumn == null || actionColumn == null) {
            System.err.println("One or more columns are not properly initialized.");
            return;
        }

        // Initialisation des colonnes
        nomRubriqueColumn.setCellValueFactory(new PropertyValueFactory<>("nomRubrique"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionRubrique"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreationRubrique"));

        // Colonne d'action : Voir publications
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("Voir publications");

            {
                viewBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                viewBtn.setOnAction(event -> {
                    rubrique selectedRubrique = getTableView().getItems().get(getIndex());
                    if (selectedRubrique != null) {
                        openPublicationsWindow(selectedRubrique);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(viewBtn));
                }
            }
        });
    }

    private void loadRubriques() {
        if (forum != null) {
            List<rubrique> rubriqueList = rubriqueService.getAllByForumId(forum.getId());
            ObservableList<rubrique> observableList = FXCollections.observableArrayList(rubriqueList);
            rubriqueTable.setItems(observableList);
        }
    }

    private void openPublicationsWindow(rubrique selectedRubrique) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/PublicationViewfront.fxml"));
            AnchorPane pane = loader.load();

            PublicationController controller = loader.getController();
            if (controller != null) {
                controller.setRubrique(selectedRubrique);
                controller.loadPublications();
            }

            Stage stage = new Stage();
            stage.setTitle("Publications de : " + selectedRubrique.getNomRubrique());
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}