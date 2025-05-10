package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.CryptoData;
import api.ApiIntegration;
import java.util.List;

public class MarketOverviewController {
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
    private TableColumn<CryptoData, Node> graphCol;
    @FXML
    private Label statusBar;

    private ObservableList<CryptoData> cryptoDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize columns
        nameCol.setCellValueFactory(cellData -> cellData.getValue().readableNameProperty());
        symbolCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        changeCol.setCellValueFactory(cellData -> cellData.getValue().priceChangePercentProperty().asObject());
        highCol.setCellValueFactory(cellData -> cellData.getValue().highProperty().asObject());
        lowCol.setCellValueFactory(cellData -> cellData.getValue().lowProperty().asObject());
        marketCapCol.setCellValueFactory(cellData -> cellData.getValue().marketCapProperty().asObject());
        graphCol.setCellValueFactory(cellData -> cellData.getValue().priceGraphProperty());

        // Format price column
        priceCol.setCellFactory(column -> new TableCell<CryptoData, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", value));
                }
            }
        });

        // Format market cap column
        marketCapCol.setCellFactory(column -> new TableCell<CryptoData, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    if (value >= 1e9) {
                        setText(String.format("$%.2fB", value / 1e9));
                    } else if (value >= 1e6) {
                        setText(String.format("$%.2fM", value / 1e6));
                    } else {
                        setText(String.format("$%.2f", value));
                    }
                }
            }
        });

        cryptoTable.setItems(cryptoDataList);
        refreshData();
    }

    @FXML
    private void search() {
        String query = searchBar.getText().toLowerCase();
        ObservableList<CryptoData> filteredList = cryptoDataList.filtered(
                data -> data.getName().toLowerCase().contains(query) ||
                        data.getReadableName().toLowerCase().contains(query));
        cryptoTable.setItems(filteredList);
    }

    @FXML
    private void refreshData() {
        try {
            statusBar.setText("Fetching latest cryptocurrency data...");
            ApiIntegration api = new ApiIntegration();
            List<CryptoData> cryptoData = api.fetchDetailedCryptoData();
            cryptoDataList.setAll(cryptoData);
            statusBar.setText("Data refreshed successfully - " + cryptoData.size() + " coins loaded");
        } catch (Exception e) {
            statusBar.setText("Error refreshing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
