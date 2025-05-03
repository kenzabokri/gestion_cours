package controller.CoursMateriel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import entity.Cours;
import entity.Note;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.CoursService;
import service.NoteService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;

public class AffichageAtelierController {

    @FXML
    private VBox coursContainer;

    private final CoursService coursService = new CoursService();
    private final NoteService noteService = new NoteService();
    private final int utilisateurId = 1; // 👈 À remplacer par l’utilisateur connecté

    @FXML
    public void initialize() {
        List<Cours> coursList = coursService.readAll();
        for (Cours cours : coursList) {
            HBox card = new HBox(20);
            card.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-color: #f9f9f9;");
            VBox contentBox = new VBox(10);

            Label labelType = new Label("Type : " + cours.getType_atelier());
            Label labelDescription = new Label("Description : " + cours.getDescription_cours());
            Label labelDuree = new Label("Durée : " + cours.getDuree_cours());
            Label labelDate = new Label("Date : " + cours.getDate_cours());

            HBox hboxEtoiles = new HBox(2);
            afficherEtoiles(hboxEtoiles, cours);

            ImageView imageView = new ImageView();
            if (cours.getImage_cours() != null) {
                File imageFile = new File(cours.getImage_cours());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    imageView.setImage(image);
                    imageView.setFitWidth(150);
                    imageView.setPreserveRatio(true);
                }
            }

            Button btnVideo = new Button("Voir la vidéo");
            btnVideo.setOnAction(e -> {
                if (cours.getLien_video_cours() != null && !cours.getLien_video_cours().isEmpty()) {
                    try {
                        Desktop.getDesktop().browse(java.net.URI.create(cours.getLien_video_cours()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            Button btnMateriel = new Button("Voir le matériel");
            btnMateriel.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursMateriel/AfficherMateriel.fxml"));
                    Parent root = loader.load();
                    AfficherMaterielController controller = loader.getController();
                    controller.setCoursId(cours.getId());
                    Stage stage = new Stage();
                    stage.setTitle("Matériel pour le cours : " + cours.getType_atelier());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Button btnTelechargerCours = new Button("Télécharger");
            btnTelechargerCours.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Enregistrer le cours en PDF");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
                fileChooser.setInitialFileName("cours_" + cours.getId() + ".pdf");

                File saveFile = fileChooser.showSaveDialog(null);
                if (saveFile != null) {
                    try {
                        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                        com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(saveFile));
                        document.open();

                        // Titre
                        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
                        document.add(new com.itextpdf.text.Paragraph("Détails du cours", titleFont));
                        document.add(new com.itextpdf.text.Paragraph(" "));

                        // Infos du cours
                        document.add(new com.itextpdf.text.Paragraph("Type : " + cours.getType_atelier()));
                        document.add(new com.itextpdf.text.Paragraph("Description : " + cours.getDescription_cours()));
                        document.add(new com.itextpdf.text.Paragraph("Durée : " + cours.getDuree_cours()));
                        document.add(new com.itextpdf.text.Paragraph("Date : " + cours.getDate_cours()));
                        document.add(new com.itextpdf.text.Paragraph(" "));

                        // Image du cours
                        if (cours.getImage_cours() != null) {
                            File imageFile = new File(cours.getImage_cours());
                            if (imageFile.exists()) {
                                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imageFile.getAbsolutePath());
                                image.scaleToFit(300, 200);
                                document.add(image);
                                document.add(new com.itextpdf.text.Paragraph(" "));
                            }
                        }

                        // QR Code
                        if (cours.getLien_video_cours() != null && !cours.getLien_video_cours().isEmpty()) {
                            BufferedImage qrImage = generateQRCodeBufferedImage(cours.getLien_video_cours(), 150, 150);
                            if (qrImage != null) {
                                File tempQR = File.createTempFile("qrcode_temp", ".png");
                                ImageIO.write(qrImage, "png", tempQR);
                                com.itextpdf.text.Image qrPdfImg = com.itextpdf.text.Image.getInstance(tempQR.getAbsolutePath());
                                qrPdfImg.scaleToFit(150, 150);
                                document.add(qrPdfImg);
                                document.add(new com.itextpdf.text.Paragraph("Lien vidéo : " + cours.getLien_video_cours()));
                            }
                        }

                        document.close();
                        System.out.println("Cours exporté en PDF avec succès !");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            if (cours.getLien_video_cours() != null && !cours.getLien_video_cours().isEmpty()) {
                BufferedImage qrBufferedImage = generateQRCodeBufferedImage(cours.getLien_video_cours(), 100, 100);
                if (qrBufferedImage != null) {
                    Image fxImage = SwingFXUtils.toFXImage(qrBufferedImage, null);
                    ImageView qrImageView = new ImageView(fxImage);
                    qrImageView.setFitWidth(100);
                    qrImageView.setFitHeight(100);
                    contentBox.getChildren().add(qrImageView);
                }
            }

            HBox boutonBox = new HBox(10);
            boutonBox.getChildren().addAll(btnMateriel, btnTelechargerCours);

            contentBox.getChildren().addAll(labelType, labelDescription, labelDuree, labelDate, hboxEtoiles, btnVideo, boutonBox);
            card.getChildren().addAll(imageView, contentBox);
            coursContainer.getChildren().add(card);
        }
    }

    private void afficherEtoiles(HBox hbox, Cours cours) {
        hbox.getChildren().clear();  // On réinitialise les étoiles à chaque fois

        int noteExistante = noteService.getNoteParCoursUtilisateur(utilisateurId, cours.getId());
        final int[] note = {noteExistante > 0 ? noteExistante : 0}; // On récupère la note existante

        for (int i = 1; i <= 5; i++) {
            ImageView etoile = new ImageView();
            updateEtoileImage(etoile, i <= note[0]);  // On met l'état de l'étoile en fonction de la note
            etoile.setFitWidth(20);
            etoile.setFitHeight(20);

            final int starIndex = i;
            etoile.setOnMouseClicked(event -> {
                note[0] = starIndex;  // On met à jour la note

                // Mettre à jour l'affichage des étoiles immédiatement
                for (int j = 0; j < hbox.getChildren().size(); j++) {
                    ImageView updatedStar = (ImageView) hbox.getChildren().get(j);
                    updateEtoileImage(updatedStar, j + 1 <= note[0]);  // Mettre à jour l'état des étoiles
                }

                // Enregistrer la nouvelle note dans la base de données
                Note nouvelleNote = new Note(note[0], utilisateurId, cours.getId());
                noteService.create(nouvelleNote);  // On enregistre la nouvelle note
                System.out.println("Note enregistrée : " + note[0]);
            });

            hbox.getChildren().add(etoile);
        }
    }


    // Méthode qui met à jour l'image de l'étoile en fonction de si elle est remplie ou vide
    private void updateEtoileImage(ImageView etoile, boolean remplie) {
        InputStream imageStream;
        if (remplie) {
            imageStream = getClass().getResourceAsStream("/images/star_filled.jpg");
        } else {
            imageStream = getClass().getResourceAsStream("/images/star_empty.jpg");
        }

        if (imageStream != null) {
            etoile.setImage(new Image(imageStream));
        } else {
            System.out.println("Image étoile introuvable !");
        }
    }



    public BufferedImage generateQRCodeBufferedImage(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int grayValue = (bitMatrix.get(x, y) ? 0 : 255);
                    bufferedImage.setRGB(x, y, (grayValue == 0 ? 0xFF000000 : 0xFFFFFFFF));
                }
            }
            return bufferedImage;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
