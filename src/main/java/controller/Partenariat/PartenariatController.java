package controller.Partenariat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import entity.Partenariat;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.PartenariatService;
import java.io.File;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;


public class PartenariatController {

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfType;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfAdresse;

    @FXML
    private TextField tfLogo;

    @FXML
    private TextField tfPhone;

    @FXML
    private DatePicker tfDebut;

    @FXML
    private DatePicker tfFin;

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnimage;

    @FXML
    private ImageView imageViewPreview;

    private File selectedFile;

    @FXML private Label nomError;
    @FXML private Label typeError;
    @FXML private Label descError;
    @FXML private Label adresseError;
    @FXML private Label imgError;
    @FXML private Label telError;
    @FXML private Label dbError;
    @FXML private Label dfError;


    @FXML
    private Button afficherButton;


    private PartenariatService partenariatService = new PartenariatService();

    @FXML
    public void initialize() {
        btnAjout.setOnAction(this::ajouterPartenariat);
    }

    @FXML
    private void choisirImg(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif")
        );
        File file = fileChooser.showOpenDialog(btnimage.getScene().getWindow());
        if (file != null) {
            try {
                // Définir un dossier pour copier l'image (ex: images/)
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();

                // Créer un fichier de destination (avec le même nom que l’original)
                File destFile = new File(destDir, file.getName());

                // Copier l'image dans le dossier
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Stocker le chemin relatif dans le champ texte
                tfLogo.setText(destFile.getPath());

                // Afficher l’image dans un ImageView
                Image image = new Image(destFile.toURI().toString());
                imageViewPreview.setImage(image);

                selectedFile = destFile; // garder le fichier sélectionné si besoin plus tard

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkFormValidity() {
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;

        // Validation du nom
        if (tfNom.getText().isEmpty()) {
            errors.append("Le nom est requis.\n");
            nomError.setText("Le nom est requis");
            nomError.setVisible(true);
            isValid = false;
        } else {
            nomError.setVisible(false);
        }

        // Validation du type
        if (tfType.getText().isEmpty()) {
            errors.append("Le type est requis.\n");
            typeError.setText("Le type est requis");
            typeError.setVisible(true);
            isValid = false;
        } else {
            typeError.setVisible(false);
        }

        // Validation de la description
        if (tfDescription.getText().isEmpty()) {
            errors.append("La description est requise.\n");
            descError.setText("La description est requise");
            descError.setVisible(true);
            isValid = false;
        } else {
            descError.setVisible(false);
        }

        // Validation de l'adresse
        if (tfAdresse.getText().isEmpty()) {
            errors.append("L'adresse est requise.\n");
            adresseError.setText("L'adresse est requise");
            adresseError.setVisible(true);
            isValid = false;
        } else {
            adresseError.setVisible(false);
        }

        // Validation du logo
        if (tfLogo.getText().isEmpty()) {
            errors.append("Le logo est requis.\n");
            imgError.setText("Le logo est requis");
            imgError.setVisible(true);
            isValid = false;
        } else {
            imgError.setVisible(false);
        }

        // Validation du téléphone
        if (tfPhone.getText().isEmpty()) {
            errors.append("Le numéro de téléphone est requis.\n");
            telError.setText("Le numéro de téléphone est requis");
            telError.setVisible(true);
            isValid = false;
        } else {
            try {
                Integer.parseInt(tfPhone.getText());
                telError.setVisible(false);
            } catch (NumberFormatException e) {
                errors.append("Le numéro de téléphone doit être un entier.\n");
                telError.setText("Le numéro de téléphone doit être un entier");
                telError.setVisible(true);
                isValid = false;
            }
        }

        // Validation des dates
        if (tfDebut.getValue() == null) {
            errors.append("La date de début est requise.\n");
            dbError.setText("La date de début est requise");
            dbError.setVisible(true);
            isValid = false;
        } else {
            dbError.setVisible(false);
        }

        if (tfFin.getValue() == null) {
            errors.append("La date de fin est requise.\n");
            dfError.setText("La date de fin est requise");
            dfError.setVisible(true);
            isValid = false;
        } else {
            dfError.setVisible(false);
        }

        if (tfDebut.getValue() != null && tfFin.getValue() != null && tfFin.getValue().isBefore(tfDebut.getValue())) {
            errors.append("La date de fin ne peut pas être avant la date de début.\n");
            dfError.setText("La date de fin ne peut pas être avant la date de début");
            dfError.setVisible(true);
            isValid = false;
        }

        // Si des erreurs existent, afficher un message global
        if (!errors.toString().isEmpty()) {
            return false;
        }
        return isValid;
    }




    @FXML
    private void ajouterPartenariat(ActionEvent event) {
        if (!checkFormValidity()) return;

        Partenariat p = new Partenariat(
                0,
                tfNom.getText(),
                tfType.getText(),
                tfDescription.getText(),
                tfAdresse.getText(),
                tfLogo.getText(),
                Integer.parseInt(tfPhone.getText()),
                tfDebut.getValue(),
                tfFin.getValue()
        );

        partenariatService.create(p);
        System.out.println("✅ Partenariat ajouté avec succès !");

        // Redirection vers la vue suivante
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/partenariat/affichagePartenariat.fxml"));
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
    private void afficherPartenariat() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/partenariat/affichagePartenariat.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des partenariats");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors de l'affichage des cours : " + e.getMessage());
        }
    }





}