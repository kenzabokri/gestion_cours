package controller.Forum;

import entity.forum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.forumservice;

public class EditForumController {

    @FXML
    private TextField nomForumField;
    @FXML
    private TextField themeForumField;
    @FXML
    private TextArea descriptionForum;
    @FXML
    private DatePicker dateCreationForumPicker;
    @FXML
    private TextField imageForumField;

    private forum forumToEdit;
    private forumservice forumService = new forumservice();
    private DashboardController dashboardController;

    public void setForum(forum forum) {
        this.forumToEdit = forum;
        nomForumField.setText(forum.getNomForum());
        themeForumField.setText(forum.getThemeForum());
        descriptionForum.setText(forum.getDescriptionForum());
        dateCreationForumPicker.setValue(forum.getDateCreationForum());
        imageForumField.setText(forum.getImageForum());
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    private void saveChanges() {
        forumToEdit.setNomForum(nomForumField.getText());
        forumToEdit.setThemeForum(themeForumField.getText());
        forumToEdit.setDescriptionForum(descriptionForum.getText());
        forumToEdit.setDateCreationForum(dateCreationForumPicker.getValue());
        forumToEdit.setImageForum(imageForumField.getText());

        forumService.update(forumToEdit);
        dashboardController.refreshTable();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Forum modifié !");
        alert.showAndWait();

        nomForumField.getScene().getWindow().hide();
    }
}