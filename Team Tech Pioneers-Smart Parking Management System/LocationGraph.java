import java.util.*;

public class LocationGraph {
    private Map<String, Location> locations;
    private Map<String, Map<String, Double>> distances;

    public LocationGraph() {
        this.locations = new HashMap<>();
        this.distances = new HashMap<>();
    }

    public void addLocation(Location location) {
        locations.put(location.getLocationId(), location);
        distances.put(location.getLocationId(), new HashMap<>());
    }

    public void addConnection(String locationId1, String locationId2, double distance) {
        distances.get(locationId1).put(locationId2, distance);
        distances.get(locationId2).put(locationId1, distance);
    }

    public Location getLocation(String locationId) {
        return locations.get(locationId);
    }

    public List<Location> getAllLocations() {
        return new ArrayList<>(locations.values());
    }

    public double getDistance(String locationName1, String locationName2) {
        Location loc1 = findLocationByName(locationName1);
        Location loc2 = findLocationByName(locationName2);
        if (loc1 == null || loc2 == null) {
            return Double.MAX_VALUE;
        }

        // Use Dijkstra's algorithm to find shortest path
        Map<String, Double> shortestDistances = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
            (a, b) -> Double.compare(shortestDistances.get(a), shortestDistances.get(b))
        );

        // Initialize distances
        for (String locationId : locations.keySet()) {
            shortestDistances.put(locationId, Double.MAX_VALUE);
        }
        shortestDistances.put(loc1.getLocationId(), 0.0);
        queue.offer(loc1.getLocationId());

        // Run Dijkstra's algorithm
        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            Map<String, Double> neighbors = distances.get(currentId);
            
            for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
                String neighborId = entry.getKey();
                double distance = entry.getValue();
                double newDistance = shortestDistances.get(currentId) + distance;

                if (newDistance < shortestDistances.get(neighborId)) {
                    shortestDistances.put(neighborId, newDistance);
                    queue.offer(neighborId);
                }
            }
        }

        return shortestDistances.get(loc2.getLocationId());
    }

    private Location findLocationByName(String name) {
        return locations.values().stream()
                .filter(loc -> loc.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Location> findNearestLocations(String sourceLocationId, int count) {
        Location sourceLocation = locations.get(sourceLocationId);
        if (sourceLocation == null) {
            return new ArrayList<>();
        }

        // Use Dijkstra's algorithm to find shortest paths
        Map<String, Double> shortestDistances = new HashMap<>();
        Map<String, String> previousLocations = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
            (a, b) -> Double.compare(shortestDistances.get(a), shortestDistances.get(b))
        );

        // Initialize distances
        for (String locationId : locations.keySet()) {
            shortestDistances.put(locationId, Double.MAX_VALUE);
            previousLocations.put(locationId, null);
        }
        shortestDistances.put(sourceLocationId, 0.0);
        queue.offer(sourceLocationId);

        // Run Dijkstra's algorithm
        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            Map<String, Double> neighbors = distances.get(currentId);
            
            for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
                String neighborId = entry.getKey();
                double distance = entry.getValue();
                double newDistance = shortestDistances.get(currentId) + distance;

                if (newDistance < shortestDistances.get(neighborId)) {
                    shortestDistances.put(neighborId, newDistance);
                    previousLocations.put(neighborId, currentId);
                    queue.offer(neighborId);
                }
            }
        }

        // Sort locations by distance
        List<Map.Entry<String, Double>> sortedLocations = new ArrayList<>(shortestDistances.entrySet());
        sortedLocations.sort((a, b) -> Double.compare(a.getValue(), b.getValue()));

        // Return top 'count' nearest locations (excluding source)
        List<Location> result = new ArrayList<>();
        for (int i = 1; i < Math.min(count + 1, sortedLocations.size()); i++) {
            String locationId = sortedLocations.get(i).getKey();
            result.add(locations.get(locationId));
        }

        return result;
    }

    public List<Location> findEVChargingStations(String sourceLocationId) {
        Location sourceLocation = locations.get(sourceLocationId);
        if (sourceLocation == null) {
            return new ArrayList<>();
        }

        // Use Dijkstra's algorithm to find shortest paths to EV charging stations
        Map<String, Double> shortestDistances = new HashMap<>();
        Map<String, String> previousLocations = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
            (a, b) -> Double.compare(shortestDistances.get(a), shortestDistances.get(b))
        );

        // Initialize distances
        for (String locationId : locations.keySet()) {
            shortestDistances.put(locationId, Double.MAX_VALUE);
            previousLocations.put(locationId, null);
        }
        shortestDistances.put(sourceLocationId, 0.0);
        queue.offer(sourceLocationId);

        // Run Dijkstra's algorithm
        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            Map<String, Double> neighbors = distances.get(currentId);
            
            for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
                String neighborId = entry.getKey();
                double distance = entry.getValue();
                double newDistance = shortestDistances.get(currentId) + distance;

                if (newDistance < shortestDistances.get(neighborId)) {
                    shortestDistances.put(neighborId, newDistance);
                    previousLocations.put(neighborId, currentId);
                    queue.offer(neighborId);
                }
            }
        }

        // Find all EV charging stations and sort by distance
        List<Map.Entry<String, Double>> evStations = new ArrayList<>();
        for (Map.Entry<String, Location> entry : locations.entrySet()) {
            if (entry.getValue().hasEVCharging()) {
                evStations.add(new AbstractMap.SimpleEntry<>(
                    entry.getKey(), shortestDistances.get(entry.getKey())
                ));
            }
        }
        evStations.sort((a, b) -> Double.compare(a.getValue(), b.getValue()));

        // Return all EV charging stations with their distances
        List<Location> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : evStations) {
            result.add(locations.get(entry.getKey()));
        }

        return result;
    }
} 