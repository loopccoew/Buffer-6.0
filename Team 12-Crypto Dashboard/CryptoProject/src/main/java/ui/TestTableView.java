package ui;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestTableView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ObservableList to hold table data
        ObservableList<CryptoData> tableData = FXCollections.observableArrayList();

        // Create a TableView
        TableView<CryptoData> tableView = new TableView<>(tableData);

        // Name Column
        TableColumn<CryptoData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        // Price Column
        TableColumn<CryptoData, Double> priceColumn = new TableColumn<>("Price (USD)");
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty().asObject());

        // Price Graph Column
        TableColumn<CryptoData, Node> graphColumn = new TableColumn<>("Price Graph");
        graphColumn.setCellValueFactory(data -> data.getValue().priceGraphProperty());

        // Custom cell to show Node
        graphColumn.setCellFactory(col -> new TableCell<CryptoData, Node>() {
            @Override
            protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        });

        // Add columns to table
        tableView.getColumns().addAll(nameColumn, priceColumn, graphColumn);

        // Add sample data with mini line charts
        tableData.add(new CryptoData("Bitcoin", 50000.0));
        tableData.add(new CryptoData("Ethereum", 4000.0));

        // Layout
        VBox layout = new VBox(tableView);
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Crypto Table with Graph");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Inner class for table data
    public static class CryptoData {
        private final SimpleStringProperty name;
        private final SimpleDoubleProperty price;
        private final ObjectProperty<Node> priceGraph;

        public CryptoData(String name, double price) {
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleDoubleProperty(price);
            this.priceGraph = new SimpleObjectProperty<>(createMiniGraph(price));
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public double getPrice() {
            return price.get();
        }

        public SimpleDoubleProperty priceProperty() {
            return price;
        }

        public Node getPriceGraph() {
            return priceGraph.get();
        }

        public ObjectProperty<Node> priceGraphProperty() {
            return priceGraph;
        }

        // Generates a mini chart (just demo, you can modify)
        private Node createMiniGraph(double baseValue) {
            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setVisible(false);
            yAxis.setVisible(false);

            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setLegendVisible(false);
            lineChart.setAnimated(false);
            lineChart.setPrefSize(120, 60);

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i < 10; i++) {
                series.getData().add(new XYChart.Data<>(i, baseValue + Math.random() * 1000 - 500));
            }

            lineChart.getData().add(series);
            return lineChart;
        }
    }
}
