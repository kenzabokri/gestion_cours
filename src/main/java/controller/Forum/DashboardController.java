package controller.Forum;

import service.rubriqueservice;
import controller.Forum.EditForumController;
import controller.Rubriques.RubriqueController;
import entity.forum;
import entity.rubrique;
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
import service.forumservice;
import service.rubriqueservice;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML
    private TableView<forum> forumTable;

    @FXML
    private TableColumn<forum, String> nomColumn;

    @FXML
    private TableColumn<forum, String> themeColumn;

    @FXML
    private TableColumn<forum, String> descriptionColumn;

    @FXML
    private TableColumn<forum, String> imageColumn;

    @FXML
    private TableColumn<forum, String> dateCreationColumn;

    @FXML
    private TableColumn<forum, Void> actionsColumn;

    private final forumservice forumService = new forumservice();
    private final rubriqueservice rubriqueService = new rubriqueservice();

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomForum"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("themeForum"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionForum"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageForum"));
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreationForum"));

        addActionsToTable();
        loadForums();
    }

    private void loadForums() {
        List<forum> forumList = forumService.getAll();
        ObservableList<forum> observableList = FXCollections.observableArrayList(forumList);
        forumTable.setItems(observableList);
    }

    private void addActionsToTable() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final Button rubriqueButton = new Button("Voir Rubriques");

            {
                editButton.setOnAction(e -> {
                    forum selectedForum = getTableView().getItems().get(getIndex());
                    openEditForumWindow(selectedForum);
                });

                deleteButton.setOnAction(e -> {
                    forum selectedForum = getTableView().getItems().get(getIndex());
                    if (selectedForum != null) {
                        forumService.delete(selectedForum);
                        loadForums();
                    }
                });

                rubriqueButton.setOnAction(e -> {
                    forum selectedForum = getTableView().getItems().get(getIndex());
                    showRubriques(selectedForum);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, editButton, deleteButton, rubriqueButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    @FXML
    private void goToAddForum() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/AddForum.fxml"));
            AnchorPane pane = loader.load();

            // Récupérer le contrôleur et lui passer le DashboardController
            AddForumController controller = loader.getController();
            controller.setForumController(this); // Important pour refresh !

            Stage stage = new Stage();
            stage.setTitle("Ajouter un forum");
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openEditForumWindow(forum forumToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/EditForum.fxml"));
            AnchorPane pane = loader.load();

            // Récupérer le contrôleur du fichier FXML et lui passer le forum à éditer et le DashboardController
            EditForumController controller = loader.getController();
            controller.setForum(forumToEdit); // Passer le forum à éditer
            controller.setDashboardController(this); // Passer le DashboardController

            Stage stage = new Stage();
            stage.setTitle("Modifier Forum");
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm()); // Ajout CSS
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showRubriques(forum selectedForum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/Rubrique.fxml"));
            AnchorPane pane = loader.load();

            RubriqueController controller = loader.getController();
            controller.setForum(selectedForum);

            Stage stage = new Stage();
            stage.setTitle("Rubriques du Forum");
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable(){
        loadForums();
    }
}