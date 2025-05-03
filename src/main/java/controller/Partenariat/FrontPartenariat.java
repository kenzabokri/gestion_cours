package controller.Partenariat;


import entity.Partenariat;
import entity.sponsoring;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.PartenariatService;
import service.SponsoringService;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class FrontPartenariat implements Initializable {

    @FXML
    private VBox partenariatContainer;

    private final PartenariatService partenariatService = new PartenariatService();
    private final SponsoringService sponsoringService = new SponsoringService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPartenariats();
    }

    private void loadPartenariats() {
        List<Partenariat> partenariats = partenariatService.readAll();

        for (Partenariat p : partenariats) {
            HBox card = createPartenariatHBox(p);
            partenariatContainer.getChildren().add(card);
        }
    }

    private HBox createPartenariatHBox(Partenariat p) {
        HBox hbox = new HBox(30);
        hbox.setPadding(new Insets(20));
        hbox.setStyle("""
        -fx-background-color: #f9f9f9;
        -fx-border-color: #cccccc;
        -fx-border-width: 1;
        -fx-border-radius: 15;
        -fx-background-radius: 15;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);
        """);
        hbox.setPrefHeight(160);
        hbox.setMaxWidth(1100);

        // Image
        ImageView imageView = new ImageView();
        try {
            Image image = new Image("file:" + p.getLogo_p(), true);
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Image not found: " + p.getLogo_p());
        }
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0, 2, 2);");

        // Infos
        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(5));

        Label nameLabel = new Label("Nom : " + p.getNom_p());
        Label typeLabel = new Label("Type : " + p.getType_p());
        Label descLabel = new Label("Description : " + p.getDesc_p());
        Label datesLabel = new Label("Période : " +
                p.getDuree_deb_p().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) +
                " ➜ " +
                p.getDuree_fin_p().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        for (Label label : List.of(nameLabel, typeLabel, descLabel, datesLabel)) {
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        }
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Button viewSponsoringButton = new Button("Voir les sponsorings");

        viewSponsoringButton.setOnAction(e -> {
            try {
                List<sponsoring> sponsorings = sponsoringService.getByPartenariatId(p.getId());
                if (sponsorings.isEmpty()) {
                    Stage stage = new Stage();
                    VBox vbox = new VBox(new Label("Aucun sponsoring pour ce partenariat."));
                    vbox.setPadding(new Insets(20));
                    stage.setScene(new Scene(vbox, 300, 100));
                    stage.setTitle("Sponsorings");
                    stage.show();
                } else {
                    FrontSponsoring.showSponsorings(sponsorings);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        infoBox.getChildren().addAll(nameLabel, typeLabel, descLabel, datesLabel, viewSponsoringButton);

        hbox.getChildren().addAll(imageView, infoBox);
        return hbox;
    }
}