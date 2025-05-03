package controller.CoursMateriel;

import entity.Cours;
import entity.CoursMateriel;
import entity.Materiel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import service.CoursMaterielService;
import service.CoursService;
import service.MaterielService;

public class CoursMaterielController {

    @FXML private ComboBox<Cours> comboBoxCours;
    @FXML private ComboBox<Materiel> comboBoxMateriel;
    @FXML private Button buttonAjouter;
    @FXML private Button buttonModifier;

    @FXML private TableView<CoursMateriel> tableViewAssociations;
    @FXML private TableColumn<CoursMateriel, String> colNomCours;
    @FXML private TableColumn<CoursMateriel, String> colNomMateriel;
    @FXML private TableColumn<CoursMateriel, Void> colModifier;
    @FXML private TableColumn<CoursMateriel, Void> colSupprimer;

    private ObservableList<CoursMateriel> associations = FXCollections.observableArrayList();
    private CoursMaterielService coursMaterielService = new CoursMaterielService();
    private CoursService coursService = new CoursService();
    private MaterielService materielService = new MaterielService();

    private CoursMateriel coursMaterielAModifier = null;

    @FXML
    public void initialize() {
        colNomCours.setCellValueFactory(new PropertyValueFactory<>("nomCours"));
        colNomMateriel.setCellValueFactory(new PropertyValueFactory<>("nomMateriel"));

        comboBoxCours.setItems(FXCollections.observableArrayList(coursService.readAll()));
        comboBoxMateriel.setItems(FXCollections.observableArrayList(materielService.readAll()));

        comboBoxCours.setConverter(new StringConverter<>() {
            public String toString(Cours cours) { return cours != null ? cours.getType_atelier() : ""; }
            public Cours fromString(String s) { return null; }
        });

        comboBoxMateriel.setConverter(new StringConverter<>() {
            public String toString(Materiel materiel) { return materiel != null ? materiel.getNom_materiel() : ""; }
            public Materiel fromString(String s) { return null; }
        });

        associations.setAll(coursMaterielService.readAll());
        tableViewAssociations.setItems(associations);

        ajouterBoutonsAction();
        tableViewAssociations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                coursMaterielAModifier = newSelection;

                Cours coursSelectionne = comboBoxCours.getItems()
                        .stream()
                        .filter(c -> c.getId() == newSelection.getCoursId())
                        .findFirst()
                        .orElse(null);

                Materiel materielSelectionne = comboBoxMateriel.getItems()
                        .stream()
                        .filter(m -> m.getId() == newSelection.getMaterielId())
                        .findFirst()
                        .orElse(null);

                comboBoxCours.setValue(coursSelectionne);
                comboBoxMateriel.setValue(materielSelectionne);

                // Changer le texte du bouton pour indiquer qu'on est en mode modification
                buttonAjouter.setText("Modifier");
            }
        });

    }

    @FXML
    private void ajouterAssociation() {
        Cours cours = comboBoxCours.getValue();
        Materiel materiel = comboBoxMateriel.getValue();

        if (cours != null && materiel != null) {
            if (coursMaterielAModifier == null) {
                // MODE AJOUT
                CoursMateriel cm = new CoursMateriel(
                        cours.getId(),
                        materiel.getId(),
                        cours.getType_atelier(),
                        materiel.getNom_materiel());

                coursMaterielService.create(cm);
                associations.add(cm);
            } else {
                // MODE MODIFICATION
                coursMaterielService.delete(coursMaterielAModifier.getCoursId(), coursMaterielAModifier.getMaterielId());
                associations.remove(coursMaterielAModifier);

                CoursMateriel updated = new CoursMateriel(
                        cours.getId(),
                        materiel.getId(),
                        cours.getType_atelier(),
                        materiel.getNom_materiel());

                coursMaterielService.create(updated);
                associations.add(updated);

                coursMaterielAModifier = null;
            }

            // Réinitialiser l’état après ajout/modification
            comboBoxCours.getSelectionModel().clearSelection();
            comboBoxMateriel.getSelectionModel().clearSelection();
            tableViewAssociations.getSelectionModel().clearSelection();
            buttonAjouter.setText("Ajouter");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Sélectionne un cours et un matériel.", ButtonType.OK);
            alert.showAndWait();
        }
    }


    @FXML
    private void validerModification() {
        if (coursMaterielAModifier != null) {
            Cours nouveauCours = comboBoxCours.getValue();
            Materiel nouveauMateriel = comboBoxMateriel.getValue();

            if (nouveauCours != null && nouveauMateriel != null) {
                // Supprimer l'ancienne association
                coursMaterielService.delete(coursMaterielAModifier.getCoursId(), coursMaterielAModifier.getMaterielId());
                associations.remove(coursMaterielAModifier);

                // Créer la nouvelle association mise à jour
                CoursMateriel updated = new CoursMateriel(
                        nouveauCours.getId(),
                        nouveauMateriel.getId(),
                        nouveauCours.getType_atelier(),
                        nouveauMateriel.getNom_materiel());

                coursMaterielService.create(updated);
                associations.add(updated);

                // Réinitialisation
                comboBoxCours.getSelectionModel().clearSelection();
                comboBoxMateriel.getSelectionModel().clearSelection();
                coursMaterielAModifier = null;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Sélectionne un cours et un matériel.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    private void ajouterBoutonsAction() {
        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    CoursMateriel cm = getTableView().getItems().get(getIndex());
                    coursMaterielService.delete(cm.getCoursId(), cm.getMaterielId());
                    associations.remove(cm);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    CoursMateriel cm = getTableView().getItems().get(getIndex());

                    Cours coursSelectionne = comboBoxCours.getItems()
                            .stream()
                            .filter(c -> c.getId() == cm.getCoursId())
                            .findFirst()
                            .orElse(null);

                    Materiel materielSelectionne = comboBoxMateriel.getItems()
                            .stream()
                            .filter(m -> m.getId() == cm.getMaterielId())
                            .findFirst()
                            .orElse(null);

                    comboBoxCours.setValue(coursSelectionne);
                    comboBoxMateriel.setValue(materielSelectionne);
                    coursMaterielAModifier = cm;
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
}
