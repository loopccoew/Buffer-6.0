package utils;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.List;

public class GraphUtil {
    public static LineChart<Number, Number> createMiniGraph(double baseValue) {
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
            series.getData().add(new XYChart.Data<>(i, baseValue + Math.random() * 100 - 50));
        }

        lineChart.getData().add(series);
        return lineChart;
    }

    public static Node generateGraphNode(List<Double> prices) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setVisible(false);
        yAxis.setVisible(false);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        chart.setPrefSize(180, 40);
        chart.setMaxHeight(40);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < prices.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, prices.get(i)));
        }

        chart.getData().add(series);
        return chart;
    }
}
