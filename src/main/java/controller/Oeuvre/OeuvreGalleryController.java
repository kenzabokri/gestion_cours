package controller.Oeuvre;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import entity.Oeuvre;
import service.OeuvreService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

    public class OeuvreGalleryController implements Initializable {

        @FXML
        private VBox mainContainer;
        @FXML
        private FlowPane galleryContainer;
        @FXML
        private ComboBox<String> categoryFilter;
        @FXML
        private Button sortButton;
        @FXML
        private Button searchButton;
        @FXML
        private Button categoriesButton;
        @FXML
        private Button generateButton;
        @FXML
        private HBox notificationIcon;
        @FXML
        private Label notificationCount;
        @FXML
        private VBox notificationPanel;

        private final OeuvreService oeuvreService = new OeuvreService();

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            setupUI();
            loadGallery();
            setupNotifications();
        }

        private void setupUI() {
            // Setup category filter
            List<Oeuvre> oeuvres = oeuvreService.readAll();
            categoryFilter.getItems().add("Toutes les catégories");
            oeuvres.stream()
                    .map(oeuvre -> oeuvre.getCategorie().getNomCategorie())
                    .distinct()
                    .forEach(categoryFilter.getItems()::add);
            categoryFilter.getSelectionModel().selectFirst();

            // Style buttons
            String buttonStyle = "-fx-background-color: #c8ad7f; -fx-text-fill: white; -fx-background-radius: 30;";
            sortButton.setStyle(buttonStyle);
            searchButton.setStyle(buttonStyle);
            categoriesButton.setStyle(buttonStyle);
            generateButton.setStyle(buttonStyle);
        }

        private void loadGallery() {
            galleryContainer.getChildren().clear();
            List<Oeuvre> oeuvres = oeuvreService.readAll();

            for (Oeuvre oeuvre : oeuvres) {
                VBox artCard = createArtCard(oeuvre);
                galleryContainer.getChildren().add(artCard);
            }
        }

        private VBox createArtCard(Oeuvre oeuvre) {
            VBox card = new VBox(10);
            card.getStyleClass().add("art-card");
            card.setPadding(new Insets(15));
            card.setMaxWidth(250);

            // Artwork image
            ImageView imageView = new ImageView();
            try {
                Image image = new Image("file:" + oeuvre.getFichierMultimedia());
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(180);
                imageView.setPreserveRatio(true);
            } catch (Exception e) {
                // Placeholder if image fails to load
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/placeholder.png")));
            }

            // Artwork details
            Label titleLabel = new Label(oeuvre.getNomOeuvre());
            titleLabel.getStyleClass().add("art-title");
            titleLabel.setStyle("-fx-font-weight: bold;");

            Text description = new Text(oeuvre.getDescription());
            description.setWrappingWidth(200);
            description.getStyleClass().add("art-description");

            Button viewButton = new Button("Voir");
            viewButton.getStyleClass().add("view-button");
            viewButton.setStyle("-fx-background-color: #c8ad7f; -fx-text-fill: white; -fx-background-radius: 30;");

            card.getChildren().addAll(imageView, titleLabel, description, viewButton);

            // Add hover effect
            card.setOnMouseEntered(e -> {
                card.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0, 4, 5, 0);");
            });
            card.setOnMouseExited(e -> {
                card.setStyle("-fx-effect: null;");
            });

            return card;
        }

        private void setupNotifications() {
            notificationPanel.setVisible(false);
            notificationIcon.setOnMouseClicked(e -> {
                notificationPanel.setVisible(!notificationPanel.isVisible());
            });

            // Here you would integrate with your notification system
            // For example, using a service that pushes notifications
        }

        @FXML
        private void handleSort() {
            // Implement sorting logic
        }

        @FXML
        private void handleSearch() {
            // Implement search logic
        }

        @FXML
        private void handleCategoryFilter() {
            // Implement category filter logic
        }

        @FXML
        private void handleCategories() {
            // Show categories view
        }

        @FXML
        private void handleGenerate() {
            // Show generation view
        }
    }