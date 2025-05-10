package ui;

import logic.CryptoData;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import arbitrage.ArbitrageFinder;
import arbitrage.ArbitrageOpportunity;
import api.ApiIntegration;
import java.util.*;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.nio.charset.StandardCharsets;

public class CryptoDashboardController {
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<CryptoData> cryptoTable;
    @FXML
    private TableColumn<CryptoData, String> nameCol;
    @FXML
    private TableColumn<CryptoData, String> symbolCol;
    @FXML
    private TableColumn<CryptoData, Double> priceCol;
    @FXML
    private TableColumn<CryptoData, Double> changeCol;
    @FXML
    private TableColumn<CryptoData, Double> highCol;
    @FXML
    private TableColumn<CryptoData, Double> lowCol;
    @FXML
    private TableColumn<CryptoData, Double> marketCapCol;
    @FXML
    private TableColumn<CryptoData, ImageView> iconCol;
    @FXML
    private Label statusBar;
    @FXML
    private ChoiceBox<String> displayLanguage;

    private ObservableList<CryptoData> cryptoDataList = FXCollections.observableArrayList();
    private Timeline autoRefreshTimeline;
    private String currentLanguage = "en";

    @FXML
    public void initialize() {
        try {
            // Setup icon column
            iconCol.setCellValueFactory(data -> data.getValue().iconViewProperty());
            iconCol.setCellFactory(col -> new TableCell<CryptoData, ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(item);
                    }
                }
            });

            // Initialize language selector
            displayLanguage.setItems(FXCollections.observableArrayList(
                    "English", "Spanish", "French", "German", "Chinese", "Japanese"));
            displayLanguage.setValue("English");
            displayLanguage.setOnAction(e -> translateTableContent());

            // Set up columns
            nameCol.setCellValueFactory(data -> data.getValue().readableNameProperty());
            symbolCol.setCellValueFactory(data -> data.getValue().nameProperty());
            priceCol.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
            changeCol.setCellValueFactory(data -> data.getValue().priceChangePercentProperty().asObject());
            highCol.setCellValueFactory(data -> data.getValue().highProperty().asObject());
            lowCol.setCellValueFactory(data -> data.getValue().lowProperty().asObject());
            marketCapCol.setCellValueFactory(data -> data.getValue().marketCapProperty().asObject());

            // Format price cells with translation
            priceCol.setCellFactory(col -> new TableCell<CryptoData, Double>() {
                @Override
                protected void updateItem(Double value, boolean empty) {
                    super.updateItem(value, empty);
                    if (empty || value == null) {
                        setText(null);
                    } else {
                        String formattedPrice = String.format("$%.2f", value);
                        translateText(formattedPrice, "en", currentLanguage).thenAccept(this::setText);
                    }
                }
            });

            // Set items
            cryptoTable.setItems(cryptoDataList);

            // Setup auto-refresh
            autoRefreshTimeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> refreshData()));
            autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
            autoRefreshTimeline.play();

            // Initial data load with loading indicator
            statusBar.setText("Loading cryptocurrency data...");
            refreshData();

            // Add row factory for yellow highlighting
            cryptoTable.setRowFactory(tv -> new TableRow<CryptoData>() {
                @Override
                protected void updateItem(CryptoData item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else {
                        setStyle("-fx-background-color: #f0b90b;");
                        // Set text color to black for better visibility on yellow
                        for (Node node : getChildren()) {
                            if (node instanceof TableCell) {
                                ((TableCell) node).setTextFill(javafx.scene.paint.Color.BLACK);
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            statusBar.setText("Error initializing: " + e.getMessage());
        }
    }

    @FXML
    public void search() {
        try {
            String query = searchBar.getText().toLowerCase().trim();
            ObservableList<CryptoData> filteredList = FXCollections.observableArrayList();

            for (CryptoData data : cryptoDataList) {
                if (data.getName().toLowerCase().contains(query)) {
                    filteredList.add(data);
                }
            }

            cryptoTable.setItems(filteredList);
        } catch (Exception e) {
            statusBar.setText("Search error: " + e.getMessage());
        }
    }

    @FXML
    public void refreshData() {
        statusBar.setText("Fetching latest cryptocurrency data...");
        new Thread(() -> {
            try {
                ApiIntegration api = new ApiIntegration();
                List<CryptoData> cryptoData = api.fetchDetailedCryptoData();

                Platform.runLater(() -> {
                    cryptoDataList.setAll(cryptoData);
                    cryptoTable.refresh();
                    statusBar.setText("Data updated: " +
                            java.time.LocalDateTime.now().format(
                                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                });
            } catch (Exception e) {
                Platform.runLater(() -> statusBar.setText("Error updating data: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void refreshArbitrage() {
        try {
            statusBar.setText("Finding arbitrage opportunities...");
            Map<String, Map<String, Double>> priceMap = CryptoData.toPriceMap(cryptoDataList);
            List<ArbitrageOpportunity> opportunities = ArbitrageFinder.findArbitrageOpportunities(priceMap);

            if (!opportunities.isEmpty()) {
                StringBuilder alert = new StringBuilder("Found opportunities:\n");
                for (ArbitrageOpportunity opp : opportunities) {
                    alert.append(String.format("%s: Buy at %s ($%.2f), Sell at %s ($%.2f), Profit: %.2f%%\n",
                            opp.getCoin(), opp.getBuyExchange(), opp.getBuyPrice(),
                            opp.getSellExchange(), opp.getSellPrice(), opp.getProfitPercentage()));
                }
                statusBar.setText(alert.toString());
            } else {
                statusBar.setText("No arbitrage opportunities found");
            }
        } catch (Exception e) {
            statusBar.setText("Error finding arbitrage: " + e.getMessage());
        }
    }

    @FXML
    public void visualizeCurrentGraph() {
        // Implementation for graph visualization
        statusBar.setText("Graph visualization not implemented yet");
    }

    @FXML
    public void showShortestPath() {
        // Implementation for shortest path
        statusBar.setText("Shortest path visualization not implemented yet");
    }

    @FXML
    public void displayArbitrageDetails() {
        // Implementation for arbitrage details
        statusBar.setText("Arbitrage details not implemented yet");
    }

    @FXML
    public void convertCurrency() {
        // Implementation for currency conversion
        statusBar.setText("Currency conversion not implemented yet");
    }

    @FXML
    public void findBestConversion() {
        // Implementation for finding best conversion path
        statusBar.setText("Best conversion path not implemented yet");
    }

    private void translateTableContent() {
        String targetLang = getLanguageCode(displayLanguage.getValue());
        if (!targetLang.equals(currentLanguage)) {
            currentLanguage = targetLang;
            cryptoTable.refresh();
        }
    }

    private CompletableFuture<String> translateText(String text, String sourceLang, String targetLang) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (sourceLang.equals(targetLang))
                    return text;

                String urlStr = String.format(
                        "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s",
                        sourceLang,
                        targetLang,
                        URLEncoder.encode(text, StandardCharsets.UTF_8.toString()));

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    String result = response.toString();
                    int start = result.indexOf("\"") + 1;
                    int end = result.indexOf("\"", start);
                    return result.substring(start, end);
                }
            } catch (Exception e) {
                System.err.println("Translation error: " + e.getMessage());
                return text; // Return original text on error
            }
        });
    }

    private String getLanguageCode(String language) {
        return switch (language) {
            case "Spanish" -> "es";
            case "French" -> "fr";
            case "German" -> "de";
            case "Chinese" -> "zh";
            case "Japanese" -> "ja";
            default -> "en";
        };
    }
}