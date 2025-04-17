package in.bloodapp.utility;

import java.util.*;

public class GraphSearchUtil {

    // Graph map: pincode -> list of neighboring pincodes
    private static final Map<String, List<String>> graph = new HashMap<>();

    static {
        // Define neighboring areas (you can customize this!)
        graph.put("560001", Arrays.asList("560002", "560003"));
        graph.put("560002", Arrays.asList("560001", "560004"));
        graph.put("560003", Arrays.asList("560001", "560005"));
        graph.put("560004", Arrays.asList("560002", "560005"));
        graph.put("560005", Arrays.asList("560003", "560004"));
    }

    // BFS to find all reachable pincodes up to a depth
    public static List<String> getNearbyPincodes(String startPincode, int depthLimit) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.offer(startPincode);
        visited.add(startPincode);
        int level = 0;

        while (!queue.isEmpty() && level <= depthLimit) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                result.add(current);
                List<String> neighbors = graph.getOrDefault(current, new ArrayList<>());
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }
            level++;
        }
        return result;
    }
}
