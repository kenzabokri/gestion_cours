package controller.Partenariat;

import entity.Partenariat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import entity.sponsoring;

import javafx.stage.Stage;
import service.PartenariatService;
import service.SponsoringService;


import java.time.LocalDate;
import java.util.List;

public class AjouterSponsoring {

    @FXML private TextArea tfType;
    @FXML private ComboBox<Partenariat> comboPartenariat;
    @FXML private DatePicker dateDebutS;
    @FXML private DatePicker dateFinS;
    @FXML private Button ajouter;

    @FXML private Label typeSError;
    @FXML private Label datedebutError;
    @FXML private Label datefinError;

    private SponsoringService sponsoringService = new SponsoringService();
    private final PartenariatService partenariatService = new PartenariatService();

    private boolean checkFormValidity() {
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;
        // Validation du type
        if (tfType.getText().isEmpty()) {
            errors.append("Le type est requis.\n");
            typeSError.setText("Le type est requis");
            typeSError.setVisible(true);
            isValid = false;
        } else {
            typeSError.setVisible(false);
        }
        // Validation des dates
        if (dateDebutS.getValue() == null) {
            errors.append("La date de début est requise.\n");
            datedebutError.setText("La date de début est requise");
            datedebutError.setVisible(true);
            isValid = false;
        } else {
            datedebutError.setVisible(false);
        }

        if (dateFinS.getValue() == null) {
            errors.append("La date de fin est requise.\n");
            datefinError.setText("La date de fin est requise");
            datefinError.setVisible(true);
            isValid = false;
        } else {
            datefinError.setVisible(false);
        }

        if (dateDebutS.getValue() != null && dateFinS.getValue() != null && dateFinS.getValue().isBefore(dateDebutS.getValue())) {
            errors.append("La date de fin ne peut pas être avant la date de début.\n");
            datefinError.setText("La date de fin ne peut pas être avant la date de début");
            datefinError.setVisible(true);
            isValid = false;
        }

        // Si des erreurs existent, afficher un message global
        if (!errors.toString().isEmpty()) {
            return false;
        }
        return isValid;
    }
    @FXML
    public void initialize() {
        chargerPartenariats();
        ajouter.setOnAction(event -> ajouterSponsoring());
    };

    private void chargerPartenariats() {
        List<Partenariat> partenariats = partenariatService.readAll();
        ObservableList<Partenariat> observableList = FXCollections.observableArrayList(partenariats);
        comboPartenariat.setItems(observableList);
    }

    public void ajouterSponsoring() {
        if (!checkFormValidity()) return;

        try {
            Partenariat selectedPartenariat = comboPartenariat.getValue();
            String type = tfType.getText();
            LocalDate dateDebut = dateDebutS.getValue();
            LocalDate dateFin = dateFinS.getValue();

            if (selectedPartenariat == null || type.isEmpty() || dateDebut == null || dateFin == null) {
                System.out.println("Tous les champs doivent être remplis.");
                return;
            }

            sponsoring sponsoring = new sponsoring();
            sponsoring.setId_p(selectedPartenariat.getId());
            sponsoring.setType_s(type);
            sponsoring.setDate_debut_s(dateDebut);
            sponsoring.setDate_fin_s(dateFin);

            sponsoringService.create(sponsoring);
            System.out.println("Sponsoring ajouté avec succès.");

            // Réinitialiser les champs
            tfType.clear();
            comboPartenariat.getSelectionModel().clearSelection();
            dateDebutS.setValue(null);
            dateFinS.setValue(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherSponsoring() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Sponsoring/afficherSponsoring.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des sponsorings");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage des sponsorings : " + e.getMessage());
        }
    }
}