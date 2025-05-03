package controller.Partenariat;

import entity.sponsoring;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FrontSponsoring {

    @FXML
    private VBox sponsoringContainer; // changé de HBox à VBox

    public void initialize(List<sponsoring> sponsorings) {
        System.out.println("Sponsorings reçus : " + sponsorings.size());

        sponsoringContainer.getChildren().clear();

        for (sponsoring s : sponsorings) {
            VBox card = new VBox();
            card.setSpacing(5);
            VBox.setMargin(card, new Insets(5, 0, 5, 0)); // Espace vertical entre les cartes
            card.setStyle("-fx-padding: 10; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #f9f9f9;");


            Label typeLabel = new Label("Type: " + s.getType_s());
            typeLabel.setStyle("-fx-font-weight: bold;");
            Label debutLabel = new Label("Début: " + s.getDate_debut_s());
            Label finLabel = new Label("Fin: " + s.getDate_fin_s());

            card.getChildren().addAll( typeLabel, debutLabel, finLabel);
            sponsoringContainer.getChildren().add(card);
        }
    }

    public static void showSponsorings(List<sponsoring> sponsorings) throws IOException {
        FXMLLoader loader = new FXMLLoader(FrontSponsoring.class.getResource("/sponsoring/frontSponsoring.fxml"));
        Parent root = loader.load();

        FrontSponsoring controller = loader.getController();
        controller.initialize(sponsorings);

        Stage stage = new Stage();
        stage.setTitle("Sponsorings associés");
        stage.setScene(new Scene(root));
        stage.show();
    }
}