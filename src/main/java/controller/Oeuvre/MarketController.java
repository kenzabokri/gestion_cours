package controller.Oeuvre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import entity.CategorieOeuvre;
import entity.Oeuvre;
import service.CategorieOeuvreService;
import service.OeuvreService;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MarketController implements Initializable, ItemController.LikeUpdateListener {
    @FXML private VBox chosenArtworkCard;
    @FXML private Label artworkNameLabel;
    @FXML private Label artworkDescriptionLabel;
    @FXML private Label likesLabel;
    @FXML private ImageView artworkImg;
    @FXML private GridPane grid;
    @FXML private ComboBox<CategorieOeuvre> categoryFilter;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button likeButton;
    @FXML private Button addOeuvreButton;
    // Champs existants...
    @FXML private HBox viewCategoriesCard; // Nouveau champ ajouté


    private List<Oeuvre> allOeuvres;
    private ObservableList<Oeuvre> displayedOeuvres;
    private OeuvreService oeuvreService = new OeuvreService();
    private CategorieOeuvreService categorieService = new CategorieOeuvreService();
    private Oeuvre currentSelectedOeuvre;
    private Map<Integer, ItemController> itemControllers = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allOeuvres = oeuvreService.readAll();
        displayedOeuvres = FXCollections.observableArrayList(allOeuvres);

        initializeCategoryFilter();
        setupSearch();
        setupLikeButton();
        setupAddOeuvreButton();

        if (!allOeuvres.isEmpty()) {
            setChosenArtwork(allOeuvres.get(0));
            loadArtworkItems(displayedOeuvres);
        }

        setOnArtworkDeleted(deletedOeuvre -> {
            refreshArtworks();
            if (currentSelectedOeuvre != null && currentSelectedOeuvre.getId() == deletedOeuvre.getId()) {
                clearChosenArtwork();
            }
        });
        // Ajouter le gestionnaire de clic pour la carte des catégories
        setupCategoryNavigation();
    }

    private void setupCategoryNavigation() {
        viewCategoriesCard.setOnMouseClicked(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Categorie/AfficherCategorie.fxml"));
                Stage stage = (Stage) viewCategoriesCard.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Navigation Error", "Could not load categories view");
            }
        });


        // Ajouter un effet visuel au survol
        viewCategoriesCard.setOnMouseEntered(e ->
                viewCategoriesCard.setStyle("-fx-background-color: #f5f5f5;"));
        viewCategoriesCard.setOnMouseExited(e ->
                viewCategoriesCard.setStyle("-fx-background-color: white;"));
    }
    @Override
    public void onLikeUpdate(Oeuvre oeuvre) {
        // Update the main display if this is the currently selected artwork
        if (currentSelectedOeuvre != null && currentSelectedOeuvre.getId() == oeuvre.getId()) {
            likesLabel.setText("❤ " + oeuvre.getLikes());
        }

        // Update the item in the grid if it exists
        ItemController itemController = itemControllers.get(oeuvre.getId());
        if (itemController != null) {
            itemController.updateLikes(oeuvre.getLikes());
        }
    }

    private void setupAddOeuvreButton() {
        addOeuvreButton.setOnAction(this::handleAddOeuvre);
    }

    @FXML
    private void handleAddOeuvre(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/AjouterOeuvre.fxml"));
            Parent root = loader.load();

            AjouterOeuvreController controller = loader.getController();
            controller.setOeuvreAddedCallback(this::refreshArtworks);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Artwork");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open the add artwork window");
        }
    }

    private void refreshArtworks(int newOeuvreId) {
        allOeuvres = oeuvreService.readAll();
        displayedOeuvres.setAll(allOeuvres);
        loadArtworkItems(displayedOeuvres);

        if (newOeuvreId > 0) {
            allOeuvres.stream()
                    .filter(o -> o.getId() == newOeuvreId)
                    .findFirst()
                    .ifPresent(this::setChosenArtwork);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void initializeCategoryFilter() {
        List<CategorieOeuvre> categories = categorieService.readAll();
        categoryFilter.setItems(FXCollections.observableArrayList(categories));

        CategorieOeuvre allCategories = new CategorieOeuvre();
        allCategories.setNomCategorie("All Categories");
        categoryFilter.getItems().add(0, allCategories);
        categoryFilter.getSelectionModel().selectFirst();

        categoryFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filterArtworks();
        });
    }

    private void setupSearch() {
        searchButton.setOnAction(event -> filterArtworks());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterArtworks();
        });
    }

    private void setupLikeButton() {
        likeButton.setOnAction(event -> {
            if (currentSelectedOeuvre != null) {
                oeuvreService.incrementLikes(currentSelectedOeuvre);
                currentSelectedOeuvre.incrementLikes();

                // Update all views
                onLikeUpdate(currentSelectedOeuvre);
            }
        });
    }

    private void filterArtworks() {
        String searchText = searchField.getText().toLowerCase();
        CategorieOeuvre selectedCategory = categoryFilter.getValue();

        List<Oeuvre> filtered = allOeuvres.stream()
                .filter(oeuvre -> {
                    boolean matchesSearch = searchText.isEmpty() ||
                            oeuvre.getNomOeuvre().toLowerCase().contains(searchText) ||
                            oeuvre.getDescription().toLowerCase().contains(searchText);

                    boolean matchesCategory = selectedCategory == null ||
                            selectedCategory.getNomCategorie().equals("All Categories") ||
                            (oeuvre.getCategorie() != null &&
                                    oeuvre.getCategorie().getId() == selectedCategory.getId());

                    return matchesSearch && matchesCategory;
                })
                .collect(Collectors.toList());

        displayedOeuvres.setAll(filtered);
        loadArtworkItems(displayedOeuvres);

        if (!filtered.isEmpty()) {
            setChosenArtwork(filtered.get(0));
        } else {
            clearChosenArtwork();
        }
    }

    private void loadArtworkItems(List<Oeuvre> oeuvres) {
        grid.getChildren().clear();
        itemControllers.clear();

        int column = 0;
        int row = 1;

        try {
            for (Oeuvre oeuvre : oeuvres) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Oeuvre/artwork_item.fxml"));
                AnchorPane pane = loader.load();

                ItemController controller = loader.getController();
                controller.setData(oeuvre, this::setChosenArtwork);
                controller.setLikeUpdateListener(this);
                itemControllers.put(oeuvre.getId(), controller);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setChosenArtwork(Oeuvre oeuvre) {
        currentSelectedOeuvre = oeuvre;
        artworkNameLabel.setText(oeuvre.getNomOeuvre());
        artworkDescriptionLabel.setText(oeuvre.getDescription());
        likesLabel.setText("❤ " + oeuvre.getLikes());

        try {
            if (oeuvre.getFichierMultimedia() != null) {
                Image image = new Image("file:" + oeuvreService.getUploadDir() + oeuvre.getFichierMultimedia());
                artworkImg.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        chosenArtworkCard.setStyle("-fx-background-color: rgba(236,182,126,0.82); -fx-background-radius: 30;");
    }

    private void clearChosenArtwork() {
        currentSelectedOeuvre = null;
        artworkNameLabel.setText("No artwork selected");
        artworkDescriptionLabel.setText("");
        likesLabel.setText("❤ 0");
        artworkImg.setImage(null);
        chosenArtworkCard.setStyle("-fx-background-color: transparent;");
    }

    private Consumer<Oeuvre> onArtworkDeleted;

    public void setOnArtworkDeleted(Consumer<Oeuvre> callback) {
        this.onArtworkDeleted = callback;
    }

    void refreshArtworks() {
        allOeuvres = oeuvreService.readAll();
        displayedOeuvres.setAll(allOeuvres);
        loadArtworkItems(displayedOeuvres);

        if (!allOeuvres.isEmpty()) {
            setChosenArtwork(allOeuvres.get(0));
        } else {
            clearChosenArtwork();
        }
    }

}