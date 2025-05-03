package controller.CoursMateriel;

import entity.Cours;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.CoursService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;

public class CoursController {

    @FXML
    private TextField typeAtelierTF, description_coursTF, dureeTF, lienTF;
    @FXML
    private DatePicker date_coursTF;
    @FXML
    private Button browseImageButton, addButton;
    @FXML
    private TextField imagePathTF;
    @FXML
    private ImageView imageViewPreview;

    private String imagePath = "";
    private final CoursService coursService = new CoursService();

    @FXML
    private void initialize() {
        addButton.setOnAction(this::ajouterCours);
        browseImageButton.setOnAction(event -> handleBrowseImage());
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(browseImageButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();

                File destFile = new File(destDir, selectedFile.getName());

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                imagePath = "images" + File.separator + selectedFile.getName();

                if (imagePathTF != null) {
                    imagePathTF.setText(imagePath);
                }

                Image image = new Image(destFile.toURI().toString());
                imageViewPreview.setImage(image);

                System.out.println("✅ Image copiée et affichée : " + imagePath);

            } catch (IOException e) {
                System.out.println("❌ Erreur lors de la copie de l'image : " + e.getMessage());
            }
        } else {
            System.out.println("❌ Aucune image sélectionnée.");
        }
    }

    @FXML
    private void ajouterCours(ActionEvent event) {
        // Vérification des champs
        String typeAtelier = typeAtelierTF.getText().trim();
        String description = description_coursTF.getText().trim();
        String duree = dureeTF.getText().trim();
        String lien = lienTF.getText().trim();
        java.time.LocalDate date = date_coursTF.getValue();

        // Validation de base
        if (typeAtelier.isEmpty() || description.isEmpty() || duree.isEmpty() || lien.isEmpty() || date == null) {
            System.out.println("❌ Tous les champs doivent être remplis.");
            return;
        }

        // Vérifier que les chaînes ont au moins 5 caractères
        if (typeAtelier.length() < 5 || description.length() < 5 || duree.length() < 5 || lien.length() < 5) {
            System.out.println("❌ Les champs doivent contenir au moins 5 caractères.");
            return;
        }

        try {
            Cours cours = new Cours(
                    typeAtelier,
                    description,
                    duree,
                    Date.valueOf(date),
                    imagePath,
                    lien
            );

            coursService.create(cours);
            System.out.println("✅ Cours ajouté !");

            // Affichage après ajout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursMateriel/ListeCours.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void afficherCours() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CoursMateriel/ListeCours.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des cours");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage des cours : " + e.getMessage());
        }
    }
}
