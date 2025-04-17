package utils;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.List;

public class GraphUtil {
    public static Node generatePriceGraph(List<Double> priceHistory) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Price");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Price Trend");
        lineChart.setCreateSymbols(false);
        lineChart.setPrefSize(150, 50);
        lineChart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < priceHistory.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, priceHistory.get(i)));
        }

        lineChart.getData().add(series);
        return lineChart;
    }

    public static Node createMiniGraph(double basePrice) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelsVisible(false);
        yAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        yAxis.setTickMarkVisible(false);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        lineChart.setPrefSize(100, 40);
        lineChart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        double variation = basePrice * 0.02; // 2% variation
        for (int i = 0; i < 10; i++) {
            double randomPrice = basePrice + (Math.random() - 0.5) * variation;
            series.getData().add(new XYChart.Data<>(i, randomPrice));
        }

        lineChart.getData().add(series);
        return lineChart;
    }

    public static LineChart<Number, Number> createPriceChart(List<Double> prices) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < prices.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, prices.get(i)));
        }

        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);

        return lineChart;
    }
}
