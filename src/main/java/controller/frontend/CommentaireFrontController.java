package controller.frontend;

import service.CommentaireService;
import entity.Commentaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CommentaireFrontController {

    @FXML
    private TableView<Commentaire> commentaireTable;

    @FXML
    private TableColumn<Commentaire, String> contenuCol;

    @FXML
    private TableColumn<Commentaire, String> dateCol;

    private final CommentaireService commentaireService = new CommentaireService();

    @FXML
    public void initialize() {
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCommentaire"));
        loadCommentaires();
    }

    private void loadCommentaires() {
        List<Commentaire> commentaires = commentaireService.getAll();
        ObservableList<Commentaire> observableList = FXCollections.observableArrayList(commentaires);
        commentaireTable.setItems(observableList);
    }
}