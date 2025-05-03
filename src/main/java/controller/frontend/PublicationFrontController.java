package controller.frontend;

import service.PublicationForumService;
import entity.PublicationForum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PublicationFrontController {

    @FXML
    private TableView<PublicationForum> publicationTable;

    @FXML
    private TableColumn<PublicationForum, String> nomCol;

    @FXML
    private TableColumn<PublicationForum, String> contenuCol;

    @FXML
    private TableColumn<PublicationForum, String> dateCol;

    private final PublicationForumService publicationService = new PublicationForumService();

    @FXML
    public void initialize() {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nomPublicationForum"));
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenuPublicationForum"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreationPubForum"));
        loadPublications();
    }

    private void loadPublications() {
        List<PublicationForum> publications = publicationService.getAll();
        ObservableList<PublicationForum> observableList = FXCollections.observableArrayList(publications);
        publicationTable.setItems(observableList);
    }
}