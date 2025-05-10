package dsa;

import java.util.*;

public class BellmanFordAlgorithm {
    public static class Graph {
        private final List<String> vertices = new ArrayList<>();
        private final List<Edge> edges = new ArrayList<>();

        public void addVertex(String vertex) {
            vertices.add(vertex);
        }

        public void addEdge(String source, String destination, double weight) {
            edges.add(new Edge(source, destination, weight));
        }

        public Map<String, String> bellmanFordWithPaths(String startVertex) {
            Map<String, Double> distances = new HashMap<>();
            Map<String, String> predecessors = new HashMap<>();

            // Initialize
            for (String vertex : vertices) {
                distances.put(vertex, Double.POSITIVE_INFINITY);
                predecessors.put(vertex, null);
            }
            distances.put(startVertex, 0.0);

            // Relax edges
            for (int i = 0; i < vertices.size() - 1; i++) {
                for (Edge edge : edges) {
                    if (distances.get(edge.source) + edge.weight < distances.getOrDefault(edge.destination,
                            Double.POSITIVE_INFINITY)) {
                        distances.put(edge.destination, distances.get(edge.source) + edge.weight);
                        predecessors.put(edge.destination, edge.source);
                    }
                }
            }

            return predecessors;
        }
    }

    public static class Edge {
        final String source;
        final String destination;
        final double weight;

        public Edge(String source, String destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }
}
