package controller.Categorie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import entity.CategorieOeuvre;

import java.io.IOException;

public class AfficheCategorieAjouteeController {

    @FXML
    private Label lblConfirmation;

    private CategorieOeuvre categorieAjoutee;

    public void setCategorieAjoutee(CategorieOeuvre categorie) {
        this.categorieAjoutee = categorie;
        afficherConfirmation();
    }

    private void afficherConfirmation() {
        if (categorieAjoutee != null) {
            lblConfirmation.setText("Catégorie '" + categorieAjoutee.getNomCategorie()
                    + "' ajoutée avec succès (ID: " + categorieAjoutee.getId() + ")");
        }
    }

    @FXML
    void retourAjout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/AjouterCategorie.fxml"));
            Parent root = loader.load();
            lblConfirmation.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void voirListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/AfficherCategorie.fxml"));
            Parent root = loader.load();
            lblConfirmation.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}