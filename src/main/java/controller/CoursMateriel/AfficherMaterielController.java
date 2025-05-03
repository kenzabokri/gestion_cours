package controller.CoursMateriel;

import entity.Materiel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import service.CoursMaterielService;
import service.MaterielService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class AfficherMaterielController {

    @FXML
    private VBox materielContainer;

    @FXML
    private Button btnTelechargerTous;

    private final CoursMaterielService coursMaterielService = new CoursMaterielService();
    private final MaterielService materielService = new MaterielService();

    public void setCoursId(int coursId) {
        materielContainer.getChildren().clear();

        List<Materiel> materiels = coursMaterielService
                .readAll()
                .stream()
                .filter(cm -> cm.getCoursId() == coursId)
                .map(cm -> materielService.findById(cm.getMaterielId()))
                .filter(m -> m != null)
                .toList();

        if (materiels.isEmpty()) {
            Label noMatLabel = new Label("Aucun matériel pour ce cours.");
            materielContainer.getChildren().add(noMatLabel);
            btnTelechargerTous.setDisable(true);
        } else {
            btnTelechargerTous.setDisable(false);

            for (Materiel mat : materiels) {
                VBox materielBox = new VBox(10); // Contient la carte + WebView si cliquée
                HBox card = new HBox(20);
                card.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #eee;");

                VBox infoBox = new VBox(8);
                Label nom = new Label("Nom : " + mat.getNom_materiel());
                Label desc = new Label("Description : " + mat.getDescription_materiel());
                Label prix = new Label("Prix : " + mat.getPrix_materiel() + "€");
                Label lieu = new Label("Lieu : " + mat.getLieu_achat_materiel());

                Hyperlink mapLink = new Hyperlink("Voir la carte");
                mapLink.setOnAction(e -> {
                    try {
                        String location = mat.getLieu_achat_materiel().replace(" ", "+");
                        String url = "https://www.openstreetmap.org/search?query=" + location;

                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(new URI(url));
                        }

                        WebView dynamicMapView = new WebView();
                        dynamicMapView.setPrefHeight(300);
                        dynamicMapView.setPrefWidth(600);

                        String iframe = """
                            <html><body style='margin:0'>
                                <iframe width='100%%' height='100%%' frameborder='0'
                                src='https://www.openstreetmap.org/export/embed.html?bbox=0&layer=mapnik&marker=0&query=%s'></iframe>
                            </body></html>
                            """.formatted(location);

                        dynamicMapView.getEngine().loadContent(iframe);

                        if (!materielBox.getChildren().contains(dynamicMapView)) {
                            materielBox.getChildren().add(dynamicMapView);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                infoBox.getChildren().addAll(nom, desc, prix, lieu, mapLink);

                ImageView img = new ImageView();
                if (mat.getImage_materiel() != null) {
                    File file = new File(mat.getImage_materiel());
                    if (file.exists()) {
                        img.setImage(new Image(file.toURI().toString()));
                        img.setFitWidth(150);
                        img.setPreserveRatio(true);
                        infoBox.getChildren().add(img);
                    }
                }

                card.getChildren().add(infoBox);
                materielBox.getChildren().add(card);
                materielContainer.getChildren().add(materielBox);
            }

            btnTelechargerTous.setOnAction(event -> {
                try {
                    int width = 800;
                    int yOffset = 180;
                    int height = 50 + (materiels.size() * yOffset);

                    BufferedImage imageFinale = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = imageFinale.createGraphics();

                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, width, height);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 14));

                    int y = 20;
                    for (Materiel mat : materiels) {
                        g.drawString("Nom : " + mat.getNom_materiel(), 20, y);
                        g.drawString("Description : " + mat.getDescription_materiel(), 20, y + 20);
                        g.drawString("Prix : " + mat.getPrix_materiel() + "€", 20, y + 40);
                        g.drawString("Lieu d'achat : " + mat.getLieu_achat_materiel(), 20, y + 60);

                        if (mat.getImage_materiel() != null) {
                            File file = new File(mat.getImage_materiel());
                            if (file.exists()) {
                                BufferedImage matImage = ImageIO.read(file);
                                g.drawImage(matImage, 500, y - 10, 150, 100, null);
                            }
                        }

                        y += yOffset;
                    }

                    g.dispose();

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Enregistrer tous les matériels");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image PNG", "*.png"));
                    fileChooser.setInitialFileName("materiels_cours_" + coursId + ".png");

                    File saveFile = fileChooser.showSaveDialog(null);
                    if (saveFile != null) {
                        ImageIO.write(imageFinale, "png", saveFile);
                        System.out.println("Tous les matériels ont été téléchargés avec succès !");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    public void initialize() {
        btnTelechargerTous.setDisable(true);

    }
}
