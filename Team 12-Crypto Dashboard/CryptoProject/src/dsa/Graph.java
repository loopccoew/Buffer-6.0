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

    // Add Kruskal's MST Algorithm
    public List<Edge> findMinimumSpanningTree() {
        List<Edge> mst = new ArrayList<>();
        DisjointSet disjointSet = new DisjointSet(vertices.size());

        // Sort edges by weight
        List<Edge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort((a, b) -> Integer.compare(a.getWeight(), b.getWeight()));

        for (Edge edge : sortedEdges) {
            if (disjointSet.find(edge.getSource()) != disjointSet.find(edge.getDestination())) {
                mst.add(edge);
                disjointSet.union(edge.getSource(), edge.getDestination());
            }
        }
        return mst;
    }

    // Add A* Search Algorithm
    public List<Integer> aStarSearch(int start, int goal, Map<Integer, Integer> heuristic) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Integer> closedSet = new HashSet<>();
        Map<Integer, Integer> cameFrom = new HashMap<>();
        Map<Integer, Integer> gScore = new HashMap<>();

        openSet.add(new Node(start, 0 + heuristic.get(start)));
        gScore.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.vertex == goal) {
                return reconstructPath(cameFrom, goal);
            }

            closedSet.add(current.vertex);
            for (Edge edge : adjacencyList.get(current.vertex)) {
                if (closedSet.contains(edge.getDestination()))
                    continue;

                int tentativeGScore = gScore.get(current.vertex) + edge.getWeight();
                if (tentativeGScore < gScore.getOrDefault(edge.getDestination(), Integer.MAX_VALUE)) {
                    cameFrom.put(edge.getDestination(), current.vertex);
                    gScore.put(edge.getDestination(), tentativeGScore);
                    int fScore = tentativeGScore + heuristic.get(edge.getDestination());
                    openSet.add(new Node(edge.getDestination(), fScore));
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Integer> reconstructPath(Map<Integer, Integer> cameFrom, int goal) {
        List<Integer> path = new ArrayList<>();
        for (Integer at = goal; at != null; at = cameFrom.get(at)) {
            path.add(0, at);
        }
        return path;
    }

    // Add Red-Black Tree implementation for balanced search
    private class RedBlackTree {
        private Node root;
        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private class Node {
            int key;
            Node left, right;
            boolean color;

            Node(int key) {
                this.key = key;
                this.color = RED;
            }
        }

        private boolean isRed(Node node) {
            return node != null && node.color == RED;
        }

        public void insert(int key) {
            root = insert(root, key);
            root.color = BLACK;
        }

        private Node insert(Node node, int key) {
            if (node == null)
                return new Node(key);

            if (key < node.key)
                node.left = insert(node.left, key);
            else if (key > node.key)
                node.right = insert(node.right, key);

            if (isRed(node.right) && !isRed(node.left))
                node = rotateLeft(node);
            if (isRed(node.left) && isRed(node.left.left))
                node = rotateRight(node);
            if (isRed(node.left) && isRed(node.right))
                flipColors(node);

            return node;
        }

        private Node rotateLeft(Node h) {
            Node x = h.right;
            h.right = x.left;
            x.left = h;
            x.color = h.color;
            h.color = RED;
            return x;
        }

        private Node rotateRight(Node h) {
            Node x = h.left;
            h.left = x.right;
            x.right = h;
            x.color = h.color;
            h.color = RED;
            return x;
        }

        private void flipColors(Node h) {
            h.color = RED;
            h.left.color = BLACK;
            h.right.color = BLACK;
        }
    }

    // Add Quick Sort for efficient sorting
    public void quickSort(List<Edge> edges, int low, int high) {
        if (low < high) {
            int pi = partition(edges, low, high);
            quickSort(edges, low, pi - 1);
            quickSort(edges, pi + 1, high);
        }
    }

    private int partition(List<Edge> edges, int low, int high) {
        Edge pivot = edges.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (edges.get(j).getWeight() < pivot.getWeight()) {
                i++;
                Edge temp = edges.get(i);
                edges.set(i, edges.get(j));
                edges.set(j, temp);
            }
        }
        Edge temp = edges.get(i + 1);
        edges.set(i + 1, edges.get(high));
        edges.set(high, temp);
        return i + 1;
    }

    // Helper class for A* search
    private class Node implements Comparable<Node> {
        int vertex;
        int fScore;

        Node(int vertex, int fScore) {
            this.vertex = vertex;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fScore, other.fScore);
        }
    }

    // Disjoint Set for MST
    private class DisjointSet {
        private int[] parent;
        private int[] rank;

        DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int px = find(x), py = find(y);
            if (rank[px] < rank[py])
                parent[px] = py;
            else if (rank[px] > rank[py])
                parent[py] = px;
            else {
                parent[py] = px;
                rank[px]++;
            }
        }
    }
}
