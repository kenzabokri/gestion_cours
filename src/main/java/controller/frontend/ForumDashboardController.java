package controller.frontend;


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
import service.forumservice;

import java.io.IOException;
import java.util.List;

public class ForumDashboardController {

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

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomForum"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("themeForum"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionForum"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageForum"));
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreationForum"));

        addViewRubriquesButton();
        loadForums();
    }

    private void loadForums() {
        List<forum> forumList = forumService.getAll();
        ObservableList<forum> observableList = FXCollections.observableArrayList(forumList);
        forumTable.setItems(observableList);
    }

    private void addViewRubriquesButton() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button rubriqueButton = new Button("Voir Rubriques");

            {
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
                    HBox buttons = new HBox(10, rubriqueButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void showRubriques(forum selectedForum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/RubriqueViewfront.fxml"));
            AnchorPane pane = loader.load();

            RubriqueFrontController rubriqueFrontController = (RubriqueFrontController) loader.getController();

            rubriqueFrontController.setForum(selectedForum);

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
}