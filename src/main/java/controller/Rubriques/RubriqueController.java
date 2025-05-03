package controller.Rubriques;

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

public class RubriqueController {

    @FXML
    private TableView<rubrique> rubriqueTable;

    @FXML
    private TableColumn<rubrique, String> nomRubriqueColumn;

    @FXML
    private TableColumn<rubrique, String> descriptionColumn;

    @FXML
    private TableColumn<rubrique, String> dateColumn;

    @FXML
    private TableColumn<rubrique, Void> actionsColumn;

    @FXML
    private Button addRubriqueButton;

    private final rubriqueservice rubriqueService = new rubriqueservice();

    private forum forum;

    public void setForum(forum forum) {
        this.forum = forum;
        loadRubriques();
    }

    @FXML
    public void initialize() {
        // Set the cell value factory for each column
        nomRubriqueColumn.setCellValueFactory(new PropertyValueFactory<>("nomRubrique"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionRubrique"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreationRubrique"));

        // Set the actions column with buttons
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button editButton = new Button("Modifier");
            private final Button viewPublicationsButton = new Button("Voir publications");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton, viewPublicationsButton);

            {
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                viewPublicationsButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

                // Action: Supprimer
                deleteButton.setOnAction(e -> {
                    rubrique selectedRubrique = getTableView().getItems().get(getIndex());
                    if (selectedRubrique != null) {
                        rubriqueService.delete(selectedRubrique);  // Delete rubrique
                        loadRubriques();  // Reload the rubrique table
                    }
                });

                // Action: Modifier
                editButton.setOnAction(e -> {
                    rubrique selectedRubrique = getTableView().getItems().get(getIndex());
                    if (selectedRubrique != null) {
                        openEditRubriqueForm(selectedRubrique);  // Open the edit form
                    }
                });

                // Action: Voir publications
                viewPublicationsButton.setOnAction(e -> {
                    rubrique selectedRubrique = getTableView().getItems().get(getIndex());
                    if (selectedRubrique != null) {
                        System.out.println("📂 Ouverture des publications de la rubrique : " + selectedRubrique.getNomRubrique());
                        openPublicationsWindow(selectedRubrique);  // Open publications window
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
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

    @FXML
    private void handleAddRubrique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/AddRubrique.fxml"));
            AnchorPane pane = loader.load();

            AddRubriqueController controller = loader.getController();
            controller.setForum(forum);
            controller.setRubriqueController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter Rubrique");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditRubriqueForm(rubrique rubriqueToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/AddRubrique.fxml"));
            AnchorPane pane = loader.load();

            AddRubriqueController controller = loader.getController();
            controller.setForum(forum);
            controller.setRubriqueController(this);
            controller.setRubriqueToEdit(rubriqueToEdit);

            Stage stage = new Stage();
            stage.setTitle("Modifier Rubrique");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openPublicationsWindow(rubrique selectedRubrique) {
        try {
            // Assure-toi que le chemin est correct
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forum/PublicationView.fxml"));
            AnchorPane pane = loader.load();

            PublicationController controller = loader.getController();

            // Vérifier si le contrôleur est bien instancié
            if (controller != null) {
                System.out.println("📂 Rubrique sélectionnée : " + selectedRubrique.getNomRubrique());
                controller.setRubrique(selectedRubrique);  // On passe la rubrique au contrôleur
                controller.loadPublications();  // Charger les publications pour cette rubrique
            } else {
                System.err.println("❌ PublicationController est nul.");
            }

            Stage stage = new Stage();
            stage.setTitle("Publications de : " + selectedRubrique.getNomRubrique());
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadRubriques();
    }
}