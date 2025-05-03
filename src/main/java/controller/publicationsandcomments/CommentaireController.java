package controller.publicationsandcomments;
import service.CommentaireService;
import entity.Commentaire;
import entity.PublicationForum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CommentaireController {

    @FXML
    private TableView<Commentaire> commentaireTable;

    @FXML
    private TableColumn<Commentaire, String> contenuColumn;

    @FXML
    private TableColumn<Commentaire, String> dateColumn;

    @FXML
    private TableColumn<Commentaire, Integer> likesColumn;

    @FXML
    private TableColumn<Commentaire, Integer> dislikesColumn;

    @FXML
    private TableColumn<Commentaire, Void> actionsColumn;

    private CommentaireService commentaireService = new CommentaireService();
    private PublicationForum currentPublication;

    public void setPublication(PublicationForum publicationForum) {
        this.currentPublication = publicationForum;
        loadCommentaires();
    }

    public void initialize() {
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("contenuCommentaire"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreationCommentaire"));
        likesColumn.setCellValueFactory(new PropertyValueFactory<>("likes"));
        dislikesColumn.setCellValueFactory(new PropertyValueFactory<>("dislikes"));
        addActionsToTable();
    }

    private void loadCommentaires() {
        List<Commentaire> all = commentaireService.getAll();
        ObservableList<Commentaire> publicationComments = FXCollections.observableArrayList();

        for (Commentaire c : all) {
            if (c.getPublicationForumId() == currentPublication.getId()) {
                publicationComments.add(c);
            }
        }

        commentaireTable.setItems(publicationComments);
    }

    private void addActionsToTable() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(e -> {
                    Commentaire selected = getTableView().getItems().get(getIndex());
                    commentaireService.delete(selected);
                    loadCommentaires();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    @FXML
    private void handleAddCommentaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/AddCommentaire.fxml"));
            AnchorPane pane = loader.load();

            AddCommentaireController controller = loader.getController();
            controller.setPublication(currentPublication);
            controller.setCommentaireController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un commentaire");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadCommentaires();
    }
}