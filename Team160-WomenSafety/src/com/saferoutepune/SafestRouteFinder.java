package com.saferoutepune;

import java.util.*;
import java.io.*;
import java.time.LocalTime;
import java.nio.file.*;

public final class SafestRouteFinder {
    public Map<String, List<SmartEdge>> graph = new HashMap<>();
    private Random random = new Random();

    public class SmartEdge {
        String destination;
        int baseSafety;
        int nightPenalty;
        int rainPenalty;
        int trafficDelay;

        
    public SmartEdge(String destination, int baseSafety) {
        this.destination = destination;
        this.baseSafety = baseSafety;
        this.nightPenalty = 1; // Reduced night penalty
        this.rainPenalty = random.nextInt(2); // Reduced rain penalty to a max of 1
        this.trafficDelay = random.nextInt(3); // Reduced traffic delay to a max of 2
    }

    public int getCurrentSafety(boolean isNight, boolean isRaining) {
        int safety = baseSafety;
        if (isNight) safety -= nightPenalty;
        if (isRaining) safety -= rainPenalty;
        safety -= trafficDelay;
        return Math.max(safety, 0); // Ensuring the safety score is never negative
    }
    }

    public class SmartNode implements Comparable<SmartNode> {
        String area;
        int cumulativeSafety;
        int estimatedTime;
        List<String> path;

        public SmartNode(String area, int cumulativeSafety, int estimatedTime, List<String> path) {
            this.area = area;
            this.cumulativeSafety = cumulativeSafety;
            this.estimatedTime = estimatedTime;
            this.path = new ArrayList<>(path);
            this.path.add(area);
        }

        @Override
        public int compareTo(SmartNode other) {
            return Integer.compare(this.cumulativeSafety, other.cumulativeSafety);
        }
    }

    public class RouteOption {
        public List<String> path;
        public int safetyScore;
        public int timeMinutes;
        public String conditions;

        public RouteOption(List<String> path, int safetyScore, int timeMinutes, String conditions) {
            this.path = path;
            this.safetyScore = safetyScore;
            this.timeMinutes = timeMinutes;
            this.conditions = conditions;
        }
    }

    public SafestRouteFinder() throws IOException {
        loadGraph();
    }

    public void loadGraph() throws IOException {
        Path path = Paths.get("Team160-WomenSafety\\src\\com\\saferoutepune\\data\\safety_data_updated.csv");
        try (Scanner scanner = new Scanner(path)) {
            scanner.nextLine(); // Skip header
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length < 3) continue;
                String area1 = parts[0].trim();
                String area2 = parts[1].trim();
                try {
                    int baseSafety = Integer.parseInt(parts[2].trim());
                    addEdge(area1, area2, baseSafety);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid entry: " + Arrays.toString(parts));
                }
            }
        }
    }

    private void addEdge(String area1, String area2, int baseSafety) {
        graph.computeIfAbsent(area1, _ -> new ArrayList<>()).add(new SmartEdge(area2, baseSafety));
        graph.computeIfAbsent(area2, _ -> new ArrayList<>()).add(new SmartEdge(area1, baseSafety));
    }

    public boolean isNightTime() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(19, 0)) || now.isBefore(LocalTime.of(6, 0));
    }

    private int calculateSegmentTime(boolean isRaining) {
        int baseTime = 5;
        if (isRaining) baseTime *= 1.3;
        return baseTime + random.nextInt(5);
    }

    private String getRouteFactors(boolean isNight, boolean isRaining) {
        List<String> factors = new ArrayList<>();
        if (isNight) factors.add("Night");
        if (isRaining) factors.add("Rain");
        return factors.isEmpty() ? "Ideal conditions" : String.join(" + ", factors);
    }

    public List<RouteOption> findRoutes(String start, String end, boolean isRaining, boolean isNight) {
        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            return Collections.emptyList();
        }

        PriorityQueue<SmartNode> pq = new PriorityQueue<>();
        Map<String, Integer> bestSafety = new HashMap<>();
        graph.keySet().forEach(area -> bestSafety.put(area, Integer.MIN_VALUE));
        
        pq.add(new SmartNode(start, 0, 0, new ArrayList<>()));
        bestSafety.put(start, 0);

        List<RouteOption> options = new ArrayList<>();
        
        while (!pq.isEmpty() && options.size() < 3) {
            SmartNode current = pq.poll();
            
            if (current.area.equals(end)) {
                options.add(new RouteOption(
                    current.path,
                    current.cumulativeSafety,
                    current.estimatedTime,
                    getRouteFactors(isNight, isRaining)
                ));
                continue;
            }

            for (SmartEdge edge : graph.getOrDefault(current.area, Collections.emptyList())) {
                int newSafety = current.cumulativeSafety + edge.getCurrentSafety(isNight, isRaining);
                int newTime = current.estimatedTime + calculateSegmentTime(isRaining);
                
                if (newSafety > bestSafety.get(edge.destination)) {
                    bestSafety.put(edge.destination, newSafety);
                    pq.add(new SmartNode(
                        edge.destination,
                        newSafety,
                        newTime,
                        current.path
                    ));
                }
            }
        }
        return options;
    }
}
