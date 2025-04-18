package ui;

import logic.CryptoData;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.application.Platform;
import api.ApiIntegration;
import java.util.List;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleObjectProperty;

public class MarketViewController {
    @FXML
    private TableView<CryptoData> cryptoTable;
    @FXML
    private TableColumn<CryptoData, ImageView> iconCol;
    @FXML
    private TableColumn<CryptoData, CryptoData> coinCol;
    @FXML
    private TableColumn<CryptoData, Double> priceCol;
    @FXML
    private TableColumn<CryptoData, Double> changeCol;
    @FXML
    private TableColumn<CryptoData, Double> volumeCol;
    @FXML
    private TableColumn<CryptoData, Double> marketCapCol;
    @FXML
    private TextField searchBar;
    @FXML
    private Label statusBar;

    private final ObservableList<CryptoData> cryptoDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Set up icon column
            iconCol.setCellValueFactory(data -> data.getValue().iconViewProperty());

            // Setup coin column with icon and name
            coinCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
            coinCol.setCellFactory(col -> new TableCell<CryptoData, CryptoData>() {
                @Override
                protected void updateItem(CryptoData item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        HBox container = new HBox(10);
                        container.setAlignment(Pos.CENTER_LEFT);

                        // Create and configure icon
                        ImageView icon = item.iconViewProperty().get();
                        if (icon != null) {
                            icon.setFitHeight(24);
                            icon.setFitWidth(24);
                            icon.setPreserveRatio(true);
                            container.getChildren().add(icon);
                        }

                        // Create name and symbol labels
                        VBox nameBox = new VBox(2);
                        Label nameLabel = new Label(item.getReadableName());
                        Label symbolLabel = new Label(item.getName().toUpperCase());
                        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
                        symbolLabel.setStyle("-fx-text-fill: gray;");
                        nameBox.getChildren().addAll(nameLabel, symbolLabel);

                        container.getChildren().add(nameBox);
                        setGraphic(container);
                    }
                }
            });

            // Set up other columns
            priceCol.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
            changeCol.setCellValueFactory(data -> data.getValue().priceChangePercentProperty().asObject());
            volumeCol.setCellValueFactory(data -> data.getValue().volumeProperty().asObject());
            marketCapCol.setCellValueFactory(data -> data.getValue().marketCapProperty().asObject());

            // Set items
            cryptoTable.setItems(cryptoDataList);

            // Load initial data
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            statusBar.setText("Error initializing: " + e.getMessage());
        }
    }

    @FXML
    public void refreshData() {
        statusBar.setText("Fetching latest data...");
        new Thread(() -> {
            try {
                ApiIntegration api = new ApiIntegration();
                List<CryptoData> data = api.fetchDetailedCryptoData();

                Platform.runLater(() -> {
                    cryptoDataList.setAll(data);
                    statusBar.setText("Data updated: " +
                            java.time.LocalDateTime.now().format(
                                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                });
            } catch (Exception e) {
                Platform.runLater(() -> statusBar.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void onSearch() {
        try {
            String query = searchBar.getText().toLowerCase().trim();
            cryptoTable.setItems(cryptoDataList.filtered(data -> data.getName().toLowerCase().contains(query) ||
                    data.getReadableName().toLowerCase().contains(query)));
        } catch (Exception e) {
            statusBar.setText("Search error: " + e.getMessage());
        }
    }

    @FXML
    public void onRefresh() {
        refreshData();
    }
}
