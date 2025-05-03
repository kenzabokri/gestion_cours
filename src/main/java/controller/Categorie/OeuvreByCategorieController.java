package controller.Categorie;

import controller.Oeuvre.ModifierOeuvreController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import entity.CategorieOeuvre;
import entity.Oeuvre;
import service.CategorieOeuvreService;
import service.OeuvreService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OeuvreByCategorieController implements Initializable {

    @FXML
    private ComboBox<CategorieOeuvre> categoryComboBox;

    @FXML
    private FlowPane oeuvresContainer;

    private OeuvreService oeuvreDAO = new OeuvreService(); // Supposons que OeuvreDAO contient la méthode findByCategorie



    @FXML
    private TextField searchField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCategoryComboBox();
        loadCategories();
        setupSearchListener();


    }
    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            CategorieOeuvre selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                loadOeuvresForCategory(selectedCategory, newValue.trim());
            }
        });
    }

    private void setupCategoryComboBox() {
        categoryComboBox.setConverter(new StringConverter<CategorieOeuvre>() {
            @Override
            public String toString(CategorieOeuvre categorie) {
                return categorie != null ? categorie.getNomCategorie() : "";
            }

            @Override
            public CategorieOeuvre fromString(String string) {
                return null;
            }
        });

        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadOeuvresForCategory(newVal);
            }
        });

        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadOeuvresForCategory(newVal, searchField.getText().trim());
            }
        });
    }

    private void loadCategories() {
        CategorieOeuvreService service = new CategorieOeuvreService();
        List<CategorieOeuvre> categories = service.readAll();
        categoryComboBox.getItems().setAll(categories);
    }
    private void loadOeuvresForCategory(CategorieOeuvre categorie) {
        oeuvresContainer.getChildren().clear();
        List<Oeuvre> oeuvres = oeuvreDAO.findByCategorie(categorie);

        for (Oeuvre oeuvre : oeuvres) {
            VBox card = createOeuvreCard(oeuvre);
            oeuvresContainer.getChildren().add(card);
        }
    }

    private void loadOeuvresForCategory(CategorieOeuvre categorie, String searchText) {
        oeuvresContainer.getChildren().clear();
        List<Oeuvre> oeuvres = oeuvreDAO.findByCategorie(categorie);

        if (searchText != null && !searchText.isEmpty()) {
            String finalSearchText = searchText.toLowerCase();
            oeuvres = oeuvres.stream()
                    .filter(oeuvre -> oeuvre.getNomOeuvre().toLowerCase().contains(finalSearchText))
                    .collect(Collectors.toList());
        }

        for (Oeuvre oeuvre : oeuvres) {
            VBox card = createOeuvreCard(oeuvre);
            oeuvresContainer.getChildren().add(card);
        }
    }
    private VBox createOeuvreCard(Oeuvre oeuvre) {
        VBox card = new VBox(10);
        card.getStyleClass().add("oeuvre-card");
        card.setPrefWidth(250);
        card.setMaxWidth(250);

        // Partie Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        try {
            String imagePath = oeuvreDAO.getUploadDir() + oeuvre.getFichierMultimedia();
            File file = new File(imagePath);
            if (file.exists()) {
                // Load the actual image file
                Image image = new Image(file.toURI().toString(), 200, 150, true, true);
                imageView.setImage(image);
            } else {
                // Try to load placeholder from resources
                URL placeholderUrl = getClass().getResource("/images/placeholder.png");
                if (placeholderUrl != null) {
                    imageView.setImage(new Image(placeholderUrl.toString(), 200, 150, true, true));
                } else {
                    // Create a default colored rectangle as fallback
                    imageView.setStyle("-fx-background-color: #f0f0f0;");
                }
            }
        } catch (Exception e) {
            // Try to load error image from resources
            try {
                URL errorUrl = getClass().getResource("/images/error.png");
                if (errorUrl != null) {
                    imageView.setImage(new Image(errorUrl.toString(), 200, 150, true, true));
                } else {
                    // Create a default colored rectangle as fallback
                    imageView.setStyle("-fx-background-color: #ffebee;"); // Light red for error
                }
            } catch (Exception ex) {
                // Final fallback - colored rectangle
                imageView.setStyle("-fx-background-color: #ffebee;");
            }
        }

        // Partie Texte
        Label titleLabel = new Label(oeuvre.getNomOeuvre());
        titleLabel.getStyleClass().add("oeuvre-title");
        titleLabel.setWrapText(true);

        Label likesLabel = new Label("❤ " + oeuvre.getLikes());
        likesLabel.getStyleClass().add("likes-label");

        VBox infoBox = new VBox(5, titleLabel, likesLabel);
        infoBox.setPadding(new Insets(5));

        // Boutons d'action
        Button editButton = new Button("Modifier");
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(e -> handleEditOeuvre(oeuvre));

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> handleDeleteOeuvre(oeuvre));

        HBox buttonBox = new HBox(10, editButton, deleteButton);
        buttonBox.getStyleClass().add("button-box");
        buttonBox.setPadding(new Insets(5, 0, 0, 0));

        // Assemblage des éléments
        card.getChildren().addAll(
                imageView,
                infoBox,
                buttonBox
        );

        return card;
    }
    private void handleEditOeuvre(Oeuvre oeuvre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/ModdifierOeuvre.fxml"));
            Parent root = loader.load();
            ModifierOeuvreController controller = loader.getController();

            controller.setOeuvre(oeuvre);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'Œuvre");
            stage.setOnHiding(event -> {
                // Refresh the list after the edit window is closed
                CategorieOeuvre selected = categoryComboBox.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    loadOeuvresForCategory(selected);
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteOeuvre(Oeuvre oeuvre) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'œuvre");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette œuvre ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            oeuvreDAO.delete(oeuvre); // Ensure OeuvreService has a delete method
            // Refresh the current category list
            CategorieOeuvre selected = categoryComboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                loadOeuvresForCategory(selected);
            }
        }
    }
    public void setSelectedCategory(CategorieOeuvre categorie) {
        // Initialiser les catégories avant la sélection
        loadCategories();
        categoryComboBox.getSelectionModel().select(categorie);

    }


    @FXML
    private void redirectToAddOeuvre(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/ajouterOeuvre.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Œuvre");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }
    @FXML
    private void redirectToAddCategory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/AjouterCategorie.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Catégorie");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
