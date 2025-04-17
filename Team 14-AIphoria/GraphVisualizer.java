import java.util.*;

public class GraphVisualizer {
    private Map<String, List<Edge>> graph;
    private Scanner sc;

    public GraphVisualizer() {
        graph = new HashMap<>();
        sc = new Scanner(System.in);
    }

    public void graphMenu() {
        System.out.println("\n🎓 Do you want to enter Learning Mode first?");
        System.out.println("1. Yes, teach me about Graphs 🌐");
        System.out.println("2. No, take me to operations 🚀");
        System.out.print("Enter your choice: ");
        int learnChoice = sc.nextInt();
        if (learnChoice == 1) {
            graphLearningMode();
        }

        System.out.println("\nDo you want to start with basic graph operations or add cities?");
        System.out.println("1. Start with an empty graph (No cities yet) 🌍");
        System.out.println("2. Start defining your own cities and routes 🏙️");
        System.out.print("Enter your choice: ");
        int graphChoice = sc.nextInt();

        if (graphChoice == 1) {
            // Empty graph, no cities yet
            System.out.println("🌍 Starting with an empty graph.");
        } else {
            // Allow user to define cities and routes
            defineCitiesAndRoutes();
            System.out.println("🏙️ Custom cities and routes defined.");
        }

        while (true) {
            System.out.println("\n===== 🌐 Advanced Graph Operations Menu =====");
            System.out.println("1. Add a City (Vertex)");
            System.out.println("2. Add a Route (Edge)");
            System.out.println("3. Display Map");
            System.out.println("4. Find a Route (Destination Finder) 🚗");
            System.out.println("5. DFS Traversal");
            System.out.println("6. BFS Traversal");
            System.out.println("7. Find Shortest Path (Dijkstra's Algorithm)");
            System.out.println("8. Fun Fact about Graphs 🎉");
            System.out.println("9. Graph Quiz 🤔");
            System.out.println("0. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addCity();
                case 2 -> addRoute();
                case 3 -> displayMap();
                case 4 -> findRoute();
                case 5 -> dfsTraversal();
                case 6 -> bfsTraversal();
                case 7 -> findShortestPath();
                case 8 -> funFact();
                case 9 -> graphQuiz();
                case 0 -> {
                    System.out.println("🔙 Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }

    private void graphLearningMode() {
        System.out.println("\n📚 Welcome to Graph Learning Mode!");
        System.out.println("\n🌐 What is a Graph?");
        System.out.println("- A graph is a collection of nodes (vertices) and edges that connect pairs of nodes.");
        System.out.println("- Graphs are used in various applications such as social networks, web page linking, and computer networks.");

        System.out.println("\n✨ Types of Graphs:");
        System.out.println("- Directed Graph: Edges have direction (one-way connections).");
        System.out.println("- Undirected Graph: Edges have no direction (two-way connections).");

        System.out.println("\n🌱 Real-life Example: City Map");
        System.out.println("In a city map, each city is a vertex, and each road connecting the cities is an edge. If you want to travel from City A to City B, the map represents all possible routes.");

        System.out.println("\n✨ Algorithms in Graphs:");
        System.out.println("- BFS (Breadth-First Search): Explores neighbors first, before going to the next level neighbors. Uses a queue.");
        System.out.println("- DFS (Depth-First Search): Explores as deep as possible along one branch before backtracking. Uses a stack.");
        System.out.println("- Dijkstra's Algorithm: Finds the shortest path between two cities by minimizing the total distance (cost).");

        System.out.println("\n🎉 You've completed Graph Learning Mode! Let's start operations!");
    }

    private void defineCitiesAndRoutes() {
        System.out.println("\n📝 Define your cities and routes:");

        while (true) {
            System.out.print("Enter a city name (or type 'done' to finish): ");
            String city = sc.next();
            if (city.equalsIgnoreCase("done")) {
                break;
            }
            graph.putIfAbsent(city, new ArrayList<>());
            System.out.println("City " + city + " added to the graph.");
        }

        System.out.println("\nNow, define routes between cities:");

        while (true) {
            System.out.print("Enter the source city for the route (or type 'done' to finish): ");
            String source = sc.next();
            if (source.equalsIgnoreCase("done")) {
                break;
            }
            if (!graph.containsKey(source)) {
                System.out.println("City " + source + " doesn't exist! Please add it first.");
                continue;
            }

            System.out.print("Enter the destination city: ");
            String destination = sc.next();
            if (!graph.containsKey(destination)) {
                System.out.println("City " + destination + " doesn't exist! Please add it first.");
                continue;
            }

            System.out.print("Enter distance between " + source + " and " + destination + " (in km): ");
            int distance = sc.nextInt();

            graph.get(source).add(new Edge(destination, distance));
            graph.get(destination).add(new Edge(source, distance)); // Bidirectional route
            System.out.println("Route added between " + source + " and " + destination + " with distance: " + distance + " km");
        }
    }

    private void addCity() {
        System.out.print("Enter city name to add: ");
        String city = sc.next();
        graph.putIfAbsent(city, new ArrayList<>());
        System.out.println("✅ City " + city + " added.");
    }

    private void addRoute() {
        System.out.print("Enter source city: ");
        String source = sc.next();
        System.out.print("Enter destination city: ");
        String destination = sc.next();
        System.out.print("Enter distance between cities (in km): ");
        int distance = sc.nextInt();

        graph.putIfAbsent(source, new ArrayList<>());
        graph.putIfAbsent(destination, new ArrayList<>());
        graph.get(source).add(new Edge(destination, distance));
        graph.get(destination).add(new Edge(source, distance)); // For bidirectional route
        System.out.println("✅ Route added from " + source + " to " + destination + " with distance: " + distance + " km");
    }

    private void displayMap() {
        System.out.println("\n📜 City Map Representation:");
        System.out.println("===== VISUAL CITY MAP =====");
        for (Map.Entry<String, List<Edge>> entry : graph.entrySet()) {
            System.out.print(entry.getKey() + " → ");
            for (Edge edge : entry.getValue()) {
                System.out.print(edge.destination + " (" + edge.distance + " km) → ");
            }
            System.out.println();
        }
        System.out.println("====================================");
    }

    private void findRoute() {
        System.out.print("Enter start city: ");
        String start = sc.next();
        System.out.print("Enter destination city: ");
        String destination = sc.next();

        System.out.println("\nFinding the route from " + start + " to " + destination + "...");
        List<String> route = bfsRouteFinder(start, destination);

        if (route.isEmpty()) {
            System.out.println("❌ No route found from " + start + " to " + destination);
        } else {
            System.out.println("✅ Route found: " + String.join(" → ", route));
        }
    }

    private List<String> bfsRouteFinder(String start, String destination) {
        Set<String> visited = new HashSet<>();
        Queue<List<String>> queue = new LinkedList<>();
        List<String> startList = new ArrayList<>();
        startList.add(start);
        queue.add(startList);
        visited.add(start);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String currentCity = path.get(path.size() - 1);

            if (currentCity.equals(destination)) {
                return path;
            }

            for (Edge edge : graph.getOrDefault(currentCity, new ArrayList<>())) {
                if (!visited.contains(edge.destination)) {
                    visited.add(edge.destination);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(edge.destination);
                    queue.add(newPath);
                }
            }
        }

        return new ArrayList<>();  // Return empty list if no path found
    }

    private void findShortestPath() {
        System.out.print("Enter start city: ");
        String start = sc.next();
        System.out.print("Enter destination city: ");
        String destination = sc.next();

        System.out.println("\nFinding the shortest path from " + start + " to " + destination + " using Dijkstra's Algorithm...");
        Map<String, Integer> distances = dijkstra(start);
        if (!distances.containsKey(destination)) {
            System.out.println("❌ No path found from " + start + " to " + destination);
        } else {
            System.out.println("✅ The shortest distance from " + start + " to " + destination + " is: " + distances.get(destination) + " km");
        }
    }

    private Map<String, Integer> dijkstra(String start) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.distance));
        pq.add(new Edge(start, 0)); // Starting point with distance 0
        distances.put(start, 0);

        while (!pq.isEmpty()) {
            Edge currentEdge = pq.poll();
            String currentCity = currentEdge.destination;

            for (Edge edge : graph.getOrDefault(currentCity, new ArrayList<>())) {
                int newDist = distances.get(currentCity) + edge.distance;
                if (newDist < distances.getOrDefault(edge.destination, Integer.MAX_VALUE)) {
                    distances.put(edge.destination, newDist);
                    pq.add(new Edge(edge.destination, newDist));
                }
            }
        }

        return distances;
    }

    private void dfsTraversal() {
        System.out.print("Enter start city for DFS: ");
        String start = sc.next();
        Set<String> visited = new HashSet<>();
        System.out.print("DFS Traversal (step-by-step): ");
        dfsHelper(start, visited);
        System.out.println();
    }

    private void dfsHelper(String city, Set<String> visited) {
        visited.add(city);
        System.out.print(city + " ");
        for (Edge edge : graph.getOrDefault(city, new ArrayList<>())) {
            if (!visited.contains(edge.destination)) {
                dfsHelper(edge.destination, visited);
            }
        }
    }

    private void bfsTraversal() {
        System.out.print("Enter start city for BFS: ");
        String start = sc.next();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        System.out.print("BFS Traversal: ");
        while (!queue.isEmpty()) {
            String city = queue.poll();
            System.out.print(city + " ");
            for (Edge edge : graph.getOrDefault(city, new ArrayList<>())) {
                if (!visited.contains(edge.destination)) {
                    visited.add(edge.destination);
                    queue.add(edge.destination);
                }
            }
        }
        System.out.println();
    }

    private void funFact() {
        System.out.println("\n🎉 FUN FACT:");
        System.out.println("Graphs are fundamental in the design of search engines, social networks, and routing algorithms!");
    }

    private void graphQuiz() {
        System.out.println("\n🤔 GRAPH QUIZ:");
        System.out.println("Q. What is the difference between BFS and DFS?");
        System.out.println("1. BFS uses a queue; DFS uses a stack.");
        System.out.println("2. BFS uses a stack; DFS uses a queue.");
        System.out.print("Your answer (1/2): ");
        int answer = sc.nextInt();
        if (answer == 1) {
            System.out.println("✅ Correct! BFS uses a queue, and DFS uses a stack.");
        } else {
            System.out.println("❌ Incorrect. The correct answer is 1.");
        }
    }

    public static void main(String[] args) {
        GraphVisualizer visualizer = new GraphVisualizer();
        visualizer.graphMenu();
    }
}

class Edge {
    String destination;
    int distance;

    public Edge(String destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }
}
