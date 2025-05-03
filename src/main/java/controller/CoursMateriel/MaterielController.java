package controller.CoursMateriel;

import entity.Materiel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.MaterielService;

import java.io.File;
import java.io.IOException;

public class MaterielController {
    @FXML private TextField nomField;
    @FXML private TextField description_materielField;
    @FXML private TextField prixField;
    @FXML private TextField lieuField;
    @FXML private ImageView imageView;
    @FXML private Button btnChoisirImage;
    @FXML private Button ajouterButton;

    private String imagePath;

    private final MaterielService materielService = new MaterielService();

    @FXML
    public void initialize() {
        btnChoisirImage.setOnAction(event -> choisirImage());
        ajouterButton.setOnAction(this::ajouterMateriel);
    }

    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(btnChoisirImage.getScene().getWindow());
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            imageView.setImage(new Image("file:" + imagePath));
        }
    }

    @FXML
    private void ajouterMateriel(ActionEvent event) {
        // Nettoyage des styles précédents
        nomField.setStyle("");
        description_materielField.setStyle("");
        prixField.setStyle("");
        lieuField.setStyle("");

        String nom = nomField.getText().trim();
        String description = description_materielField.getText().trim();
        String prixText = prixField.getText().trim();
        String lieu = lieuField.getText().trim();

        boolean isValid = true;

        // Vérification du champ nom
        if (nom.isEmpty() || nom.length() < 5) {
            isValid = false;
        }

        // Vérification du champ description
        if (description.isEmpty() || description.length() < 5) {
            isValid = false;
        }

        float prix = 0;
        try {
            prix = Float.parseFloat(prixText);
            if (prix < 0) {
                isValid = false;
            }
        } catch (NumberFormatException e) {
            isValid = false;
        }

        // Vérification du champ lieu
        if (lieu.isEmpty() || lieu.length() < 5) {
            isValid = false;
        }

        if (!isValid) {
            System.out.println("❌ Veuillez corriger les erreurs de saisie.");
            return;
        }else
       if (nom.length() < 5 || description.length() < 5 || lieu.length() < 5 ) {
            System.out.println("❌ Les champs doivent contenir au moins 5 caractères.");
            return;
        }

        try {
            Materiel materiel = new Materiel(nom, description, prix, lieu, imagePath);
            materielService.create(materiel);
            System.out.println("✅ Matériel ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'ajout du matériel : " + e.getMessage());
            return;
        }
        try {
            prix = Float.parseFloat(prixText);
        } catch (NumberFormatException e) {
            System.out.println("Le prix doit être un nombre");
            return;
        }

        // Redirection vers la liste des matériels
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursMateriel/ListeMateriel.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void afficherMateriel() { 
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CoursMateriel/Listemateriel.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des matériels");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage des matériels : " + e.getMessage());
        }
    }

}



