package dsa;

import java.util.*;

public class Graph {
    public static class Edge {
        private int source, destination, weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public int getSource() {
            return source;
        }

        public int getDestination() {
            return destination;
        }

        public int getWeight() {
            return weight;
        }
    }

    private final List<Integer> vertices;
    private final List<Edge> edges;
    private final Map<Integer, List<Edge>> adjacencyList;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(int v) {
        if (!vertices.contains(v)) {
            vertices.add(v);
            adjacencyList.put(v, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        edges.add(new Edge(source, destination, weight));
        addVertex(source);
        addVertex(destination);
        adjacencyList.get(source).add(new Edge(source, destination, weight));
    }

    public List<Integer> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Map<Integer, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    // Breadth-First Search (BFS)
    public List<Integer> bfs(int start) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);

            for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(edge.getDestination())) {
                    queue.add(edge.getDestination());
                    visited.add(edge.getDestination());
                }
            }
        }
        return result;
    }

    // Depth-First Search (DFS)
    public List<Integer> dfs(int start) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsHelper(start, visited, result);
        return result;
    }

    private void dfsHelper(int current, Set<Integer> visited, List<Integer> result) {
        visited.add(current);
        result.add(current);

        for (Edge edge : adjacencyList.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(edge.getDestination())) {
                dfsHelper(edge.getDestination(), visited, result);
            }
        }
    }

    // Dijkstra's Algorithm
    public Map<Integer, Integer> dijkstra(int start) {
        Map<Integer, Integer> distances = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        for (int vertex : vertices) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(new int[] { start, 0 });

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentVertex = current[0];
            int currentDistance = current[1];

            if (currentDistance > distances.get(currentVertex))
                continue;

            for (Edge edge : adjacencyList.getOrDefault(currentVertex, new ArrayList<>())) {
                int newDistance = currentDistance + edge.getWeight();
                if (newDistance < distances.get(edge.getDestination())) {
                    distances.put(edge.getDestination(), newDistance);
                    pq.add(new int[] { edge.getDestination(), newDistance });
                }
            }
        }
        return distances;
    }
}
