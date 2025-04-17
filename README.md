# Buffer-6.0

Women Safety
Description:
an integrated Women Safety System to address real-time emergencies, secure incident reporting, and predictive analytics.

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

// Event system for real-time alerts
class EventBus {
    private static Map<String, List<Runnable>> listeners = new HashMap<>();

    public static void subscribe(String event, Runnable callback) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(callback);
    }

    public static void publish(String event) {
        List<Runnable> callbacks = listeners.getOrDefault(event, Collections.emptyList());
        for (Runnable callback : callbacks) {
            callback.run();
        }
    }
}

class EmergencyResponseSystem {
    private Connection conn;

    public EmergencyResponseSystem() throws SQLException {
        // Initialize SQLite
        conn = DriverManager.getConnection("jdbc:sqlite:emergency.db");
        initDatabase();
    }

    private void initDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS panic_events (id INTEGER PRIMARY KEY AUTOINCREMENT, userId TEXT, latitude REAL, longitude REAL, timestamp INTEGER)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void triggerPanicButton(String userId, double latitude, double longitude) {
        System.out.println("Panic button triggered for user: " + userId);
        System.out.println("Location: (" + latitude + ", " + longitude + ")");

        // Store in SQLite
        try {
            String sql = "INSERT INTO panic_events (userId, latitude, longitude, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            pstmt.setLong(4, System.currentTimeMillis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Notify via event bus
        EventBus.publish("panic");

        // Find nearest help point
        BinarySearchTree bst = new BinarySearchTree();
        bst.insert(new HelpPoint("Police Station", 10.0, 20.0));
        HelpPoint nearest = bst.findNearest(latitude, longitude);
        System.out.println("Nearest help point: " + nearest.name + " at (" + nearest.latitude + ", " + nearest.longitude + ")");

        // Display simple console map
        displayConsoleMap(latitude, longitude, nearest);
    }

    private void displayConsoleMap(double userLat, double userLon, HelpPoint nearest) {
        System.out.println("=== Console Map ===");
        System.out.println("U: User (" + userLat + ", " + userLon + ")");
        System.out.println("H: Help Point (" + nearest.latitude + ", " + nearest.longitude + ")");
        double dist = Math.sqrt(Math.pow(userLat - nearest.latitude, 2) + Math.pow(userLon - nearest.longitude, 2));
        System.out.println("Distance: " + String.format("%.2f", dist) + " units");
        System.out.println("=================");
    }
}

class IncidentReportingSystem {
    private MaxHeap incidentHeap;
    private Connection conn;

    public IncidentReportingSystem() throws SQLException {
        incidentHeap = new MaxHeap(100);
        conn = DriverManager.getConnection("jdbc:sqlite:emergency.db");
        initDatabase();
    }

    private void initDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS incidents (id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT, severity INTEGER, latitude REAL, longitude REAL, timestamp INTEGER)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void reportIncident(String description, int severity, double latitude, double longitude) {
        Incident incident = new Incident(description, severity, latitude, longitude);
        incidentHeap.insert(incident);

        // Store in SQLite
        try {
            String sql = "INSERT INTO incidents (description, severity, latitude, longitude, timestamp) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, description);
            pstmt.setInt(2, severity);
            pstmt.setDouble(3, latitude);
            pstmt.setDouble(4, longitude);
            pstmt.setLong(5, System.currentTimeMillis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Incident reported anonymously: " + description + " (Severity: " + severity + ")");
        EventBus.publish("incident");
    }

    public Incident getHighestPriorityIncident() {
        return incidentHeap.extractMax();
    }
}

class GuardianAngelSystem {
    private Trie voiceTrie;
    private SegmentTree locationTree;
    private Connection conn;
    private LiveSpeechRecognizer recognizer;

    public GuardianAngelSystem() throws SQLException {
        voiceTrie = new Trie();
        locationTree = new SegmentTree(0, 24 * 60);
        conn = DriverManager.getConnection("jdbc:sqlite:emergency.db");
        initDatabase();
        initVoiceRecognition();
    }

    private void initDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS locations (id INTEGER PRIMARY KEY AUTOINCREMENT, time INTEGER, latitude REAL, longitude REAL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void initVoiceRecognition() {
        try {
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
            recognizer = new LiveSpeechRecognizer(configuration);
            
            // Start voice recognition in a separate thread
            new Thread(() -> {
                recognizer.startRecognition(true);
                while (true) {
                    String result = recognizer.getResult().getHypothesis();
                    if (voiceTrie.search(result.toLowerCase())) {
                        System.out.println("Voice trigger detected: " + result);
                        EventBus.publish("voice_trigger");
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addVoiceTrigger(String phrase) {
        voiceTrie.insert(phrase.toLowerCase());
        System.out.println("Voice trigger added: " + phrase);
    }

    public boolean detectVoiceTrigger(String input) {
        return voiceTrie.search(input.toLowerCase());
    }

    public void updateLocation(int time, double latitude, double longitude) {
        locationTree.update(time, latitude, longitude);

        // Store in SQLite
        try {
            String sql = "INSERT INTO locations (time, latitude, longitude) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, time);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Location updated at time " + time + ": (" + latitude + ", " + longitude + ")");
    }

    public Location getLocationAtTime(int time) {
        return locationTree.query(time);
    }
}

class FakeCallSystem {
    public void initiateFakeCall(String userId) {
        System.out.println("Initiating fake call for user: " + userId);
        // Simulate audio (requires audio file in resources)
        try {
            javax.sound.sampled.AudioInputStream audioInputStream = 
                javax.sound.sampled.AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("/ringtone.wav"));
            javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(3000); // Play for 3 seconds
            clip.close();
        } catch (Exception e) {
            System.out.println("Ring... Ring... Hello, this is your friend calling!");
        }
    }
}

// Existing data structures (unchanged)
class HelpPoint {
    String name;
    double latitude, longitude;

    public HelpPoint(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

class BinarySearchTree {
    class Node {
        HelpPoint point;
        Node left, right;

        Node(HelpPoint point) {
            this.point = point;
        }
    }

    private Node root;

    public void insert(HelpPoint point) {
        root = insertRec(root, point);
    }

    private Node insertRec(Node root, HelpPoint point) {
        if (root == null) return new Node(point);
        if (point.latitude < root.point.latitude)
            root.left = insertRec(root.left, point);
        else
            root.right = insertRec(root.right, point);
        return root;
    }

    public HelpPoint findNearest(double latitude, double longitude) {
        return findNearestRec(root, latitude, longitude);
    }

    private HelpPoint findNearestRec(Node root, double latitude, double longitude) {
        if (root == null) return null;
        HelpPoint current = root.point;
        double dist = Math.sqrt(Math.pow(current.latitude - latitude, 2) + Math.pow(current.longitude - longitude, 2));
        HelpPoint left = findNearestRec(root.left, latitude, longitude);
        HelpPoint right = findNearestRec(root.right, latitude, longitude);
        HelpPoint nearest = current;
        double minDist = dist;
        if (left != null) {
            double leftDist = Math.sqrt(Math.pow(left.latitude - latitude, 2) + Math.pow(left.longitude - longitude, 2));
            if (leftDist < minDist) {
                nearest = left;
                minDist = leftDist;
            }
        }
        if (right != null) {
            double rightDist = Math.sqrt(Math.pow(right.latitude - latitude, 2) + Math.pow(right.longitude - longitude, 2));
            if (rightDist < minDist) {
                nearest = right;
            }
        }
        return nearest;
    }
}

class Dijkstra {
    private static final int V = 9;

    public int[] shortestPath(int[][] graph, int src) {
        int[] dist = new int[V];
        boolean[] sptSet = new boolean[V];
        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }
        dist[src] = 0;
        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, sptSet);
            sptSet[u] = true;
            for (int v = 0; v < V; v++)
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v])
                    dist[v] = dist[u] + graph[u][v];
        }
        return dist;
    }

    private int minDistance(int[] dist, boolean[] sptSet) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < V; v++)
            if (!sptSet[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        return minIndex;
    }
}

class Incident {
    String description;
    int severity;
    double latitude, longitude;

    public Incident(String description, int severity, double latitude, double longitude) {
        this.description = description;
        this.severity = severity;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

class MaxHeap {
    private Incident[] heap;
    private int size;
    private int capacity;

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Incident[capacity];
    }

    public void insert(Incident incident) {
        if (size >= capacity) return;
        heap[size] = incident;
        int current = size++;
        while (current > 0 && heap[current].severity > heap[parent(current)].severity) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public Incident extractMax() {
        if (size == 0) return null;
        Incident max = heap[0];
        heap[0] = heap[--size];
        heapify(0);
        return max;
    }

    private void heapify(int i) {
        int largest = i;
        int left = leftChild(i);
        int right = rightChild(i);
        if (left < size && heap[left].severity > heap[largest].severity)
            largest = left;
        if (right < size && heap[right].severity > heap[largest].severity)
            largest = right;
        if (largest != i) {
            swap(i, largest);
            heapify(largest);
        }
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int leftChild(int i) { return 2 * i + 1; }
    private int rightChild(int i) { return 2 * i + 2; }
    private void swap(int i, int j) {
        Incident temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}

class Trie {
    class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEndOfWord;
    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null)
                current.children[index] = new TrieNode();
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null)
                return false;
            current = current.children[index];
        }
        return current.isEndOfWord;
    }
}

class Location {
    double latitude, longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

class SegmentTree {
    class Node {
        int start, end;
        Location location;
        Node left, right;

        Node(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private Node root;

    public SegmentTree(int start, int end) {
        root = new Node(start, end);
    }

    public void update(int time, double latitude, double longitude) {
        updateRec(root, time, new Location(latitude, longitude));
    }

    private void updateRec(Node node, int time, Location location) {
        if (node.start == time && node.end == time) {
            node.location = location;
            return;
        }
        int mid = (node.start + node.end) / 2;
        if (time <= mid) {
            if (node.left == null) node.left = new Node(node.start, mid);
            updateRec(node.left, time, location);
        } else {
            if (node.right == null) node.right = new Node(mid + 1, node.end);
            updateRec(node.right, time, location);
        }
    }

    public Location query(int time) {
        return queryRec(root, time);
    }

    private Location queryRec(Node node, int time) {
        if (node == null) return null;
        if (node.start == time && node.end == time)
            return node.location;
        int mid = (node.start + node.end) / 2;
        if (time <= mid)
            return queryRec(node.left, time);
        return queryRec(node.right, time);
    }
}


public class Main {
    public static void main(String[] args) {
        try {
            // Initialize Firebase
            FileInputStream serviceAccount = new FileInputStream("C:/Users/Arya/Downloads/womenssafety-7b2dc-firebase-adminsdk-fbsvc-b861a7e920.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://console.firebase.google.com/u/0/project/womenssafety-7b2dc/database/womenssafety-7b2dc-default-rtdb/data/~2F")
                    .build();

            FirebaseApp.initializeApp(options);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Initialize systems (pass database where needed)
            EmergencyResponseSystem ers = new EmergencyResponseSystem();
            IncidentReportingSystem irs = new IncidentReportingSystem();
            GuardianAngelSystem gas = new GuardianAngelSystem();
            FakeCallSystem fcs = new FakeCallSystem();

            // Setup real-time alerts
            EventBus.subscribe("panic", () -> System.out.println("ALERT: Panic button triggered! Dispatching help..."));
            EventBus.subscribe("incident", () -> System.out.println("ALERT: New incident reported!"));
            EventBus.subscribe("voice_trigger", () -> {
                System.out.println("ALERT: Voice emergency detected!");
                ers.triggerPanicButton("voice_user", 12.34, 56.78);
            });

            // Test EmergencyResponseSystem
            ers.triggerPanicButton("user123", 12.34, 56.78);

            // Test IncidentReportingSystem
            irs.reportIncident("Suspicious activity", 8, 11.0, 22.0);
            Incident highestPriority = irs.getHighestPriorityIncident();
            if (highestPriority != null) {
                System.out.println("Highest priority incident: " + highestPriority.description);
            }

            // Test GuardianAngelSystem
            gas.addVoiceTrigger("help");
            boolean detected = gas.detectVoiceTrigger("help");
            if (detected) {
                System.out.println("Voice trigger detected: help");
            }
            gas.updateLocation(600, 13.0, 57.0);
            Location loc = gas.getLocationAtTime(600);
            if (loc != null) {
                System.out.println("Location at time 600: (" + loc.latitude + ", " + loc.longitude + ")");
            }

            // Test FakeCallSystem
            fcs.initiateFakeCall("user123");

            // Test Dijkstra (local calculation remains unchanged)
            Dijkstra dijkstra = new Dijkstra();
            int[][] graph = {
                    {0, 4, 0, 0, 0, 0, 0, 8, 0},
                    {4, 0, 8, 0, 0, 0, 0, 11, 0},
                    {0, 8, 0, 7, 0, 4, 0, 0, 2},
                    {0, 0, 7, 0, 9, 14, 0, 0, 0},
                    {0, 0, 0, 9, 0, 10, 0, 0, 0},
                    {0, 0, 4, 14, 10, 0, 2, 0, 0},
                    {0, 0, 0, 0, 0, 2, 0, 1, 6},
                    {8, 11, 0, 0, 0, 0, 1, 0, 7},
                    {0, 0, 2, 0, 0, 0, 6, 7, 0}
            };
            int[] distances = dijkstra.shortestPath(graph, 0);
            System.out.println("Shortest distances from source:");
            for (int i = 0; i < distances.length; i++) {
                System.out.println("To node " + i + ": " + distances[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

