package ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import dsa.BellmanFordAlgorithm;
import dsa.BellmanFordAlgorithm.Graph;
import dsa.BellmanFordAlgorithm.Graph.Edge;
import java.util.*;

public class GraphVisualizerController {
    @FXML
    private Pane graphPane;

    public void visualizeGraph(Graph graph) {
        graphPane.getChildren().clear();

        Map<String, Circle> vertexNodes = new HashMap<>();
        double centerX = graphPane.getPrefWidth() / 2;
        double centerY = graphPane.getPrefHeight() / 2;
        double radius = Math.min(centerX, centerY) - 50;

        // Create vertex circles
        List<String> vertices = graph.getVertices();
        int vertexCount = vertices.size();

        for (int i = 0; i < vertexCount; i++) {
            String vertex = vertices.get(i);
            double angle = 2 * Math.PI * i / vertexCount;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            Circle circle = new Circle(x, y, 20, Color.LIGHTBLUE);
            circle.setStroke(Color.BLUE);
            Text text = new Text(x - 10, y + 5, vertex);
            text.setFill(Color.WHITE);

            graphPane.getChildren().addAll(circle, text);
            vertexNodes.put(vertex, circle);
        }

        // Draw edges
        for (Edge edge : graph.getEdges()) {
            Circle sourceCircle = vertexNodes.get(edge.source);
            Circle targetCircle = vertexNodes.get(edge.destination);

            if (sourceCircle != null && targetCircle != null) {
                Line line = new Line(
                        sourceCircle.getCenterX(),
                        sourceCircle.getCenterY(),
                        targetCircle.getCenterX(),
                        targetCircle.getCenterY());
                line.setStroke(Color.GRAY);

                // Add weight label
                Text weightText = new Text(
                        (sourceCircle.getCenterX() + targetCircle.getCenterX()) / 2,
                        (sourceCircle.getCenterY() + targetCircle.getCenterY()) / 2,
                        String.format("%.2f", edge.weight));
                weightText.setFill(Color.WHITE);

                graphPane.getChildren().addAll(line, weightText);
            }
        }
    }
}
