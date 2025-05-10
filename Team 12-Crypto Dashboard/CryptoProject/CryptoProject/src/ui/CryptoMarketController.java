package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;
import logic.CryptoData;
import api.ApiIntegration;
import java.util.List;

public class CryptoMarketController {
    @FXML
    private TextField searchBar;

    @FXML
    private TableView<CryptoData> cryptoTable;

    @FXML
    private TableColumn<CryptoData, Integer> rankCol;

    @FXML
    private TableColumn<CryptoData, String> symbolCol;

    @FXML
    private TableColumn<CryptoData, String> nameCol;

    @FXML
    private TableColumn<CryptoData, Double> priceCol;

    @FXML
    private TableColumn<CryptoData, Double> changeCol;

    @FXML
    private TableColumn<CryptoData, Double> volumeCol;

    @FXML
    private TableColumn<CryptoData, Double> marketCapCol;

    private ObservableList<CryptoData> cryptoDataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup cell value factories
        rankCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                cryptoDataList.indexOf(cellData.getValue()) + 1).asObject());
        symbolCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().readableNameProperty());
        priceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        changeCol.setCellValueFactory(cellData -> cellData.getValue().priceChangePercentProperty().asObject());
        marketCapCol.setCellValueFactory(cellData -> cellData.getValue().marketCapProperty().asObject());

        // Format market cap numbers
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

        // Style price changes
        changeCol.setCellFactory(column -> new TableCell<CryptoData, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f%%", value));
                    setStyle(value >= 0 ? "-fx-text-fill: #00ff00;" : "-fx-text-fill: #ff0000;");
                }
            }
        });

        cryptoTable.setItems(cryptoDataList);
        refreshData();
    }

    @FXML
    private void refreshData() {
        try {
            ApiIntegration api = new ApiIntegration();
            List<CryptoData> cryptoData = api.fetchDetailedCryptoData();
            cryptoDataList.setAll(cryptoData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void search() {
        String query = searchBar.getText().toLowerCase();
        ObservableList<CryptoData> filteredList = cryptoDataList.filtered(
                data -> data.getName().toLowerCase().contains(query) ||
                        data.getReadableName().toLowerCase().contains(query));
        cryptoTable.setItems(filteredList);
    }
}
