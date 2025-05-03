package controller.Categorie;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {
    @FXML
    private Button btnOeuvre;

    @FXML
    public void initialize() {
        btnOeuvre.setOnAction(e -> redirectToOeuvreManagement());
    }

    private void redirectToOeuvreManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/oeuvre-by-categorie.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnOeuvre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            afficherErreur("Erreur de navigation", e.getMessage());
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
