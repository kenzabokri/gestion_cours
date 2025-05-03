package controller.User;

import entity.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.UserService;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainViewController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> imageColumn;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> rolesColumn;
    @FXML private TableColumn<User, Boolean> isAdminColumn;
    @FXML private TableColumn<User, Boolean> isMemberColumn;
    @FXML private TableColumn<User, Boolean> isArtistColumn;
    @FXML private TableColumn<User, Void> actionsColumn;
    @FXML private Label clockLabel;
    @FXML private Label userLabel;
    @FXML private TextField searchField;

    private User currentUser;
    private UserService userService;
    private ObservableList<User> allUsers;
    private FilteredList<User> filteredUsers;

    @FXML
    public void initialize() {
        userService = new UserService();
        setupClock();
        setupColumns();
        setupSearchField();
        loadUsers();
    }

    private void setupClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            clockLabel.setText("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): "
                    + LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterUsers(newValue);
        });
    }

    private void setupColumns() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));
        isMemberColumn.setCellValueFactory(new PropertyValueFactory<>("member"));
        isArtistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        // Setup boolean columns with checkboxes
        setupBooleanColumn(isAdminColumn);
        setupBooleanColumn(isMemberColumn);
        setupBooleanColumn(isArtistColumn);

        // Setup image column
        setupImageColumn();

        // Setup actions column
        setupActionsColumn();
    }

    private void setupBooleanColumn(TableColumn<User, Boolean> column) {
        column.setCellFactory(col -> new TableCell<User, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            {
                checkBox.setDisable(true);
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item != null && item);
                    setGraphic(checkBox);
                }
            }
        });
    }

    private void setupImageColumn() {
        imageColumn.setCellFactory(col -> new TableCell<User, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);
                        imageView.setPreserveRatio(true);

                        // Try to load from absolute path first
                        File imageFile = new File("src/main/resources/" + imagePath);
                        if (imageFile.exists()) {
                            Image image = new Image(imageFile.toURI().toString());
                            imageView.setImage(image);
                            setGraphic(imageView);
                        } else {
                            // Try loading from resources as fallback
                            try {
                                Image image = new Image(getClass().getResource("/" + imagePath).toExternalForm());
                                imageView.setImage(image);
                                setGraphic(imageView);
                            } catch (Exception e) {
                                // If both attempts fail, load default image
                                Image defaultImage = new Image(getClass().getResource("/images/default-avatar.jpg").toExternalForm());
                                imageView.setImage(defaultImage);
                                setGraphic(imageView);
                            }
                        }
                    } catch (Exception e) {
                        try {
                            Image defaultImage = new Image(getClass().getResource("/images/default-avatar.jpg").toExternalForm());
                            imageView.setImage(defaultImage);
                            setGraphic(imageView);
                        } catch (Exception ex) {
                            setGraphic(null);
                        }
                    }
                }
            }
        });
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<User, Void>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(5, updateButton, deleteButton);

            {
                updateButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleUpdate(user);
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDelete(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private void filterUsers(String searchText) {
        if (filteredUsers != null) {
            filteredUsers.setPredicate(user -> {
                if (searchText == null || searchText.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = searchText.toLowerCase();

                return (user.getNom() != null && user.getNom().toLowerCase().contains(lowerCaseFilter)) ||
                        (user.getPrenom() != null && user.getPrenom().toLowerCase().contains(lowerCaseFilter)) ||
                        (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerCaseFilter));
            });
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getPrenom() + " " + user.getNom() + "!");
        userLabel.setText("Current User's Login: " + user.getPrenom());
        loadUsers();
    }

    private void loadUsers() {
        if (allUsers == null) {
            allUsers = FXCollections.observableArrayList();
            filteredUsers = new FilteredList<>(allUsers, p -> true);
            userTable.setItems(filteredUsers);
        }
        allUsers.setAll(userService.getAll());
    }

    @FXML
    private void handleSearch() {
        filterUsers(searchField.getText());
    }

    private void handleUpdate(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/UpdateUser.fxml"));
            Parent root = loader.load();

            UpdateUserController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle("Update User");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadUsers());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open update window");
        }
    }

    private void handleDelete(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete user: " + user.getNom() + " " + user.getPrenom() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                userService.delete(user);
                loadUsers();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Could not delete user");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not logout");
        }
    }

    @FXML
    void handleAddUserAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AddUserAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New User");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadUsers());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open add user window");
        }
    }
}