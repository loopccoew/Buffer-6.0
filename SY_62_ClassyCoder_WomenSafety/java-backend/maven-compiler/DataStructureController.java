package com.womenssafety.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DataStructureController {
    // Stack for complaint history
    private final Stack<String> stack = new Stack<>();
    // Priority Queue for case prioritization
    private final PriorityQueue<String> priorityQueue = new PriorityQueue<>();
    // HashMap for legal query classification
    private final HashMap<String, String> hashMap = new HashMap<>();
    // LinkedList for complaint history
    private final LinkedList<String> complaintHistory = new LinkedList<>();
    // Trie for legal query classification
    private final Trie trie = new Trie();
    // Graph for lawyer matching and emergency routing
    private final Graph lawyerGraph = new Graph();
    private final WeightedGraph emergencyGraph = new WeightedGraph();

    // ==================== STACK ENDPOINTS ====================

    private final Stack<String> stack = new Stack<>();
    private final PriorityQueue<String> priorityQueue = new PriorityQueue<>();
    private final HashMap<String, String> hashMap = new HashMap<>();

    // Stack endpoints
    @PostMapping("/stack/push")
    public String pushStack(@RequestBody String value) {
        stack.push(value);
        complaintHistory.add(value);
        return "Pushed to stack: " + value;
    }

    @GetMapping("/stack/pop")
    public String popStack() {
        if (stack.isEmpty()) return "Stack is empty.";
        return "Popped from stack: " + stack.pop();
    }

    @GetMapping("/stack/all")
    public List<String> getStack() {
        return new ArrayList<>(stack);
    }

    // LinkedList endpoints (Complaint History)
    @GetMapping("/complaints/history")
    public List<String> getComplaintHistory() {
        return new ArrayList<>(complaintHistory);
    }

    // ==================== PRIORITY QUEUE ENDPOINTS ====================


    // PriorityQueue endpoints
    @PostMapping("/queue/add")
    public String addQueue(@RequestBody String value) {
        priorityQueue.add(value);
        return "Added to queue: " + value;
    }

    @GetMapping("/queue/poll")
    public String pollQueue() {
        if (priorityQueue.isEmpty()) return "Queue is empty.";
        return "Polled from queue: " + priorityQueue.poll();
    }

    @GetMapping("/queue/all")
    public List<String> getPriorityQueue() {
        List<String> sorted = new ArrayList<>(priorityQueue);
        Collections.sort(sorted);
        return sorted;
    }

    // ==================== HASHMAP/TRIE ENDPOINTS ====================


    // HashMap endpoints
    @PostMapping("/map/put")
    public String putMap(@RequestParam String key, @RequestBody String value) {
        hashMap.put(key, value);
        trie.insert(key);
        return "Put in map: (" + key + ", " + value + ")";
    }

    @GetMapping("/map/get")
    public String getMap(@RequestParam String key) {
        if (!hashMap.containsKey(key)) return "Key not found.";
        return "Value for key '" + key + "': " + hashMap.get(key);
    }

    // Trie endpoints
    @PostMapping("/trie/insert")
    public String insertTrie(@RequestBody String word) {
        trie.insert(word);
        return "Inserted in trie: " + word;
    }

    @GetMapping("/trie/search")
    public boolean searchTrie(@RequestParam String word) {
        return trie.search(word);
    }

    @GetMapping("/trie/prefix")
    public boolean startsWithTrie(@RequestParam String prefix) {
        return trie.startsWith(prefix);
    }

    // ==================== LAWYER MATCHING (GRAPH BFS/DFS) ====================
    @PostMapping("/lawyer/add")
    public String addLawyer(@RequestParam String lawyer) {
        lawyerGraph.addNode(lawyer);
        return "Lawyer added: " + lawyer;
    }

    @PostMapping("/lawyer/connect")
    public String connectLawyers(@RequestParam String from, @RequestParam String to) {
        lawyerGraph.addEdge(from, to);
        return "Connected: " + from + " <-> " + to;
    }

    @GetMapping("/lawyer/bfs")
    public List<String> bfsLawyer(@RequestParam String start) {
        return lawyerGraph.bfs(start);
    }

    @GetMapping("/lawyer/dfs")
    public List<String> dfsLawyer(@RequestParam String start) {
        return lawyerGraph.dfs(start);
    }

    @GetMapping("/lawyer/connections")
    public Map<String, List<String>> getLawyerGraph() {
        return lawyerGraph.getAdjacencyList();
    }

    // ==================== EMERGENCY ROUTING (DIJKSTRA) ====================
    @PostMapping("/emergency/node")
    public String addEmergencyNode(@RequestParam String node) {
        emergencyGraph.addNode(node);
        return "Node added: " + node;
    }

    @PostMapping("/emergency/edge")
    public String addEmergencyEdge(@RequestParam String from, @RequestParam String to, @RequestParam int weight) {
        emergencyGraph.addEdge(from, to, weight);
        return "Edge added: " + from + " <-> " + to + " (" + weight + ")";
    }

    @GetMapping("/emergency/shortest")
    public List<String> getShortestPath(@RequestParam String start, @RequestParam String end) {
        return emergencyGraph.dijkstra(start, end);
    }

    @GetMapping("/emergency/graph")
    public Map<String, Map<String, Integer>> getEmergencyGraph() {
        return emergencyGraph.getAdjacencyMap();
    }

    // ==================== INNER CLASSES ====================
    // Trie implementation
    static class Trie {
        private final TrieNode root = new TrieNode();
        static class TrieNode {
            Map<Character, TrieNode> children = new HashMap<>();
            boolean isEnd = false;
        }
        public void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.isEnd = true;
        }
        public boolean search(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.get(c);
                if (node == null) return false;
            }
            return node.isEnd;
        }
        public boolean startsWith(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                node = node.children.get(c);
                if (node == null) return false;
            }
            return true;
        }
    }

    // Unweighted Graph for lawyer matching
    static class Graph {
        private final Map<String, List<String>> adj = new HashMap<>();
        public void addNode(String node) {
            adj.putIfAbsent(node, new ArrayList<>());
        }
        public void addEdge(String from, String to) {
            adj.putIfAbsent(from, new ArrayList<>());
            adj.putIfAbsent(to, new ArrayList<>());
            adj.get(from).add(to);
            adj.get(to).add(from);
        }
        public Map<String, List<String>> getAdjacencyList() {
            return adj;
        }
        public List<String> bfs(String start) {
            List<String> visited = new ArrayList<>();
            Queue<String> queue = new LinkedList<>();
            Set<String> seen = new HashSet<>();
            queue.add(start);
            seen.add(start);
            while (!queue.isEmpty()) {
                String node = queue.poll();
                visited.add(node);
                for (String neighbor : adj.getOrDefault(node, Collections.emptyList())) {
                    if (!seen.contains(neighbor)) {
                        seen.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            return visited;
        }
        public List<String> dfs(String start) {
            List<String> visited = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            dfsHelper(start, visited, seen);
            return visited;
        }
        private void dfsHelper(String node, List<String> visited, Set<String> seen) {
            if (!adj.containsKey(node) || seen.contains(node)) return;
            seen.add(node);
            visited.add(node);
            for (String neighbor : adj.get(node)) {
                dfsHelper(neighbor, visited, seen);
            }
        }
    }

    // Weighted Graph for Dijkstra
    static class WeightedGraph {
        private final Map<String, Map<String, Integer>> adj = new HashMap<>();
        public void addNode(String node) {
            adj.putIfAbsent(node, new HashMap<>());
        }
        public void addEdge(String from, String to, int weight) {
            adj.putIfAbsent(from, new HashMap<>());
            adj.putIfAbsent(to, new HashMap<>());
            adj.get(from).put(to, weight);
            adj.get(to).put(from, weight);
        }
        public Map<String, Map<String, Integer>> getAdjacencyMap() {
            return adj;
        }
        public List<String> dijkstra(String start, String end) {
            Map<String, Integer> dist = new HashMap<>();
            Map<String, String> prev = new HashMap<>();
            PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));
            for (String node : adj.keySet()) {
                dist.put(node, Integer.MAX_VALUE);
            }
            dist.put(start, 0);
            pq.add(new Node(start, 0));
            while (!pq.isEmpty()) {
                Node curr = pq.poll();
                if (curr.name.equals(end)) break;
                for (Map.Entry<String, Integer> neighbor : adj.getOrDefault(curr.name, Collections.emptyMap()).entrySet()) {
                    int alt = dist.get(curr.name) + neighbor.getValue();
                    if (alt < dist.get(neighbor.getKey())) {
                        dist.put(neighbor.getKey(), alt);
                        prev.put(neighbor.getKey(), curr.name);
                        pq.add(new Node(neighbor.getKey(), alt));
                    }
                }
            }
            List<String> path = new LinkedList<>();
            String at = end;
            if (!prev.containsKey(at) && !start.equals(end)) return path;
            while (at != null) {
                path.add(0, at);
                at = prev.get(at);
                if (at != null && at.equals(start)) {
                    path.add(0, at);
                    break;
                }
            }
            return path;
        }
        static class Node {
            String name;
            int dist;
            Node(String name, int dist) {
                this.name = name;
                this.dist = dist;
            }
        }
    }
}

