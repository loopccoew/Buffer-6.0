package ui;

import arbitrage.ArbitrageOpportunity;
import arbitrage.ArbitrageFinder;
import logic.CryptoData;
import api.ApiIntegration;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.application.Platform;
import java.util.List;
import java.util.Map;

public class ArbitrageViewController {
    @FXML
    private TableView<ArbitrageOpportunity> arbitrageTable;
    @FXML
    private TableColumn<ArbitrageOpportunity, String> marketCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, String> pairCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, Double> buyPriceCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, Double> sellPriceCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, Double> profitCol;
    @FXML
    private Label statusBar;
    @FXML
    private CheckBox autoRefresh;
    @FXML
    private ChoiceBox<String> languageChoice;

    private ObservableList<ArbitrageOpportunity> opportunities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup columns
        marketCol.setCellValueFactory(data -> data.getValue().marketProperty());
        pairCol.setCellValueFactory(data -> data.getValue().pairProperty());
        buyPriceCol.setCellValueFactory(data -> data.getValue().buyPriceProperty().asObject());
        sellPriceCol.setCellValueFactory(data -> data.getValue().sellPriceProperty().asObject());
        profitCol.setCellValueFactory(data -> data.getValue().profitProperty().asObject());

        // Setup language selector
        languageChoice.setItems(FXCollections.observableArrayList(
                "English", "Spanish", "French", "German", "Chinese", "Japanese"));
        languageChoice.setValue("English");

        arbitrageTable.setItems(opportunities);
        findArbitrage();
    }

    @FXML
    public void findArbitrage() {
        try {
            statusBar.setText("Finding arbitrage opportunities...");
            ApiIntegration api = new ApiIntegration();
            List<CryptoData> cryptoData = api.fetchDetailedCryptoData();
            Map<String, Map<String, Double>> priceMap = CryptoData.toPriceMap(cryptoData);
            List<ArbitrageOpportunity> newOpportunities = ArbitrageFinder.findArbitrageOpportunities(priceMap);

            Platform.runLater(() -> {
                opportunities.setAll(newOpportunities);
                statusBar.setText("Found " + newOpportunities.size() + " opportunities");
            });
        } catch (Exception e) {
            Platform.runLater(() -> statusBar.setText("Error: " + e.getMessage()));
        }
    }
}
