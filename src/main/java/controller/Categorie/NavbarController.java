package controller.Categorie;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class NavbarController {
    @FXML private Button btnDashboard;
    @FXML private Button btnOeuvres;
    @FXML private Button btnCategories;

    @FXML
    public void initialize() {
        setupButtonActions();
    }

    private void setupButtonActions() {
        btnDashboard.setOnAction(e -> redirectToDashboard());
        btnOeuvres.setOnAction(e -> redirectToOeuvreManagement());
        btnCategories.setOnAction(e -> redirectToCategoryManagement());
    }

    private void redirectToDashboard() {
        loadFXML("/Dashboard/main-dashboard.fxml");
    }

    private void redirectToOeuvreManagement() {
        loadFXML("/Oeuvre/OeuvreMarket.fxml");
    }

    private void redirectToCategoryManagement() {
        loadFXML("/Categorie/AfficherCategorie.fxml");
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnDashboard.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Navigation Error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}