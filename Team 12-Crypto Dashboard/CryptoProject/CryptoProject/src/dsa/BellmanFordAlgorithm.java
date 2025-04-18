package dsa;

import java.util.*;

/**
 * Implementation of the Bellman-Ford Algorithm and Graph structure.
 */
public class BellmanFordAlgorithm {

    /**
     * Graph class representing vertices and edges.
     */
    public static class Graph {
        private final List<String> vertices;
        private final List<Edge> edges;

        public Graph() {
            vertices = new ArrayList<>();
            edges = new ArrayList<>();
        }

        public void addVertex(String vertex) {
            vertices.add(vertex);
        }

        public void addEdge(String source, String destination, double weight) {
            edges.add(new Edge(source, destination, weight));
        }

        public List<String> getVertices() {
            return vertices;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        /**
         * Public Bellman-Ford method to calculate shortest paths.
         */
        public Map<String, Double> bellmanFord(String startVertex) throws IllegalArgumentException {
            Map<String, Double> distances = new HashMap<>();

            // Initialize all distances to infinity
            for (String vertex : vertices) {
                distances.put(vertex, Double.POSITIVE_INFINITY);
            }
            distances.put(startVertex, 0.0);

            // Relax edges |V| - 1 times
            for (int i = 0; i < vertices.size() - 1; i++) {
                for (Edge edge : edges) {
                    // Debugging: Print edge details
                    System.out.println("Checking edge: " + edge.source + " -> " + edge.destination);

                    // Null check for adjacency list and weights
                    if (distances.containsKey(edge.source) && distances.get(edge.source) != Double.POSITIVE_INFINITY) {
                        Double weight = Optional.ofNullable(edge.weight).orElse(Double.POSITIVE_INFINITY);
                        if (weight == Double.POSITIVE_INFINITY) {
                            System.out.println("Missing edge weight for: " + edge.source + " -> " + edge.destination);
                            continue;
                        }

                        double newDistance = distances.get(edge.source) + weight;
                        if (newDistance < distances.get(edge.destination)) {
                            distances.put(edge.destination, newDistance);
                        }
                    }
                }
            }

            // Check for negative-weight cycles
            for (Edge edge : edges) {
                if (distances.containsKey(edge.source) && distances.get(edge.source) != Double.POSITIVE_INFINITY) {
                    Double weight = Optional.ofNullable(edge.weight).orElse(Double.POSITIVE_INFINITY);
                    if (distances.get(edge.source) + weight < distances.get(edge.destination)) {
                        throw new IllegalArgumentException("Graph contains a negative-weight cycle.");
                    }
                }
            }

            return distances;
        }

        /**
         * Public Bellman-Ford method to calculate shortest paths and return the best
         * path.
         */
        public Map<String, String> bellmanFordWithPaths(String startVertex) throws IllegalArgumentException {
            Map<String, Double> distances = new HashMap<>();
            Map<String, String> predecessors = new HashMap<>();

            // Initialize distances and predecessors
            for (String vertex : vertices) {
                distances.put(vertex, Double.POSITIVE_INFINITY);
                predecessors.put(vertex, null);
            }
            distances.put(startVertex, 0.0);

            // Relax edges |V| - 1 times
            for (int i = 0; i < vertices.size() - 1; i++) {
                for (Edge edge : edges) {
                    Double sourceDistance = distances.get(edge.source);
                    Double weight = edge.weight != null ? edge.weight : Double.POSITIVE_INFINITY;

                    if (sourceDistance != null && sourceDistance != Double.POSITIVE_INFINITY) {
                        double newDistance = sourceDistance + weight;
                        if (newDistance < distances.getOrDefault(edge.destination, Double.POSITIVE_INFINITY)) {
                            distances.put(edge.destination, newDistance);
                            predecessors.put(edge.destination, edge.source);
                        }
                    }
                }
            }

            // Check for negative-weight cycles
            for (Edge edge : edges) {
                Double sourceDistance = distances.get(edge.source);
                Double weight = edge.weight != null ? edge.weight : Double.POSITIVE_INFINITY;

                if (sourceDistance != null && sourceDistance != Double.POSITIVE_INFINITY) {
                    if (sourceDistance + weight < distances.getOrDefault(edge.destination, Double.POSITIVE_INFINITY)) {
                        throw new IllegalArgumentException("Graph contains a negative-weight cycle.");
                    }
                }
            }

            return predecessors; // Return the path map
        }

        /**
         * Edge class representing a graph edge.
         */
        public static class Edge {
            public final String source; // Changed to public
            public final String destination; // Changed to public
            public final Double weight; // Changed to public

            public Edge(String source, String destination, Double weight) {
                this.source = source;
                this.destination = destination;
                this.weight = weight;
            }
        }
    }

    /**
     * Visualizer class for Bellman-Ford Algorithm.
     */
    public static class BellmanFordVisualizer {

        /**
         * Visualizes Bellman-Ford algorithm execution.
         */
        public void visualize(Graph graph, String startVertex) {
            try {
                Map<String, Double> distances = graph.bellmanFord(startVertex); // Correct method call

                System.out.println("Shortest distances from vertex '" + startVertex + "':");
                for (Map.Entry<String, Double> entry : distances.entrySet()) {
                    System.out.printf("Vertex: %s, Distance: %.2f%n", entry.getKey(), entry.getValue());
                }

                System.out.println("Bellman-Ford Algorithm executed successfully.");
                // Extend this section with JavaFX for visualization if needed.

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
