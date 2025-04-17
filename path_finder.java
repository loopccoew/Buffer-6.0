package buffer;

import java.util.*;
import java.io.*;

class Location {
    String name;
    int crimeRate;
    int infraScore;

    public Location(String name, int crimeRate, int infraScore) {
        this.name = name;
        this.crimeRate = crimeRate;
        this.infraScore = infraScore;
    }

    public int getSafetyScore() {
        return infraScore - crimeRate;
    }
}

class Edge {
    int target, distance;
    public Edge(int target, int distance) {
        this.target = target;
        this.distance = distance;
    }
}

public class path_finder {
    static Map<Integer, Location> locationMap = new HashMap<>();
    static Map<Integer, List<Edge>> graph = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1) Load CSV with exception handling
        try {
            loadZoneDataFromCSV("C:\\Users\\evasa\\Downloads\\pune_zones.csv");
        } catch (IOException e) {
            System.err.println("Error loading zone data: " + e.getMessage());
            System.exit(1);
        }

        // 2) Build graph connections (extend as needed)
        try {
            addEdge(0, 1, 2);
            addEdge(0, 2, 3);
            addEdge(1, 3, 4);
            addEdge(1, 4, 5);
            addEdge(2, 3, 2);
            addEdge(3, 5, 4);
            addEdge(4, 5, 3);
            addEdge(5, 6, 5);
            addEdge(6, 7, 5);
            addEdge(7, 8, 4);
            addEdge(8, 9, 3);
            addEdge(9,10,2);
            addEdge(10,11,2);
            addEdge(11,12,2);
            addEdge(12,13,4);
            addEdge(13,14,3);
        } catch (Exception e) {
            System.err.println("Error adding edges: " + e.getMessage());
            System.exit(1);
        }

        // 3) Display zones
        System.out.println("Available Zones:");
        for (int i = 0; i < locationMap.size(); i++) {
            System.out.printf("%d: %s%n", i, locationMap.get(i).name);
        }

        // 4) User input with exception handling
        int src = getValidIndex(sc, "Enter source index: ");
        int dest = getValidIndex(sc, "Enter destination index: ");

        // 5) Compute and print path
        List<Integer> path = null;
        try {
            path = findSafestPath(src, dest);
        } catch (Exception e) {
            System.err.println("Error finding safest path: " + e.getMessage());
            System.exit(1);
        }

        System.out.printf("\nSafest Path from %s to %s:%n",
            locationMap.get(src).name,
            locationMap.get(dest).name
        );
        if (path.isEmpty()) {
            System.out.println("No safe path");
        } else {
            for (int i = 0; i < path.size(); i++) {
                System.out.print(locationMap.get(path.get(i)).name);
                if (i < path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }

    static void loadZoneDataFromCSV(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // skip header
            String line;
            int idx = 0;
            while ((line = br.readLine()) != null) {
                try {
                    String[] p = line.split(",");
                    String zone = p[0].trim();
                    int crime = Integer.parseInt(p[1].trim());
                    int infra = Integer.parseInt(p[2].trim());
                    locationMap.put(idx++, new Location(zone, crime, infra));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid data in CSV file: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File not found: " + fileName, e);
        }
    }

    static void addEdge(int u, int v, int d) throws Exception {
        if (u < 0 || v < 0 || d <= 0) {
            throw new IllegalArgumentException("Invalid edge data: u=" + u + ", v=" + v + ", d=" + d);
        }
        graph.computeIfAbsent(u, x -> new ArrayList<>()).add(new Edge(v, d));
        graph.computeIfAbsent(v, x -> new ArrayList<>()).add(new Edge(u, d));
    }

    static List<Integer> findSafestPath(int src, int dest) throws Exception {
        int n = locationMap.size();
        int[] risk = new int[n], parent = new int[n];
        boolean[] seen = new boolean[n];
        Arrays.fill(risk, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        risk[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            if (u == dest) break;
            if (seen[u]) continue;
            seen[u] = true;

            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                int v = e.target;
                if (seen[v]) continue;
                int r = -locationMap.get(v).getSafetyScore();
                int nr = risk[u] + e.distance + r;
                if (nr < risk[v]) {
                    risk[v] = nr;
                    parent[v] = u;
                    pq.add(new int[]{v, nr});
                }
            }
        }

        if (risk[dest] == Integer.MAX_VALUE) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        for (int at = dest; at != -1; at = parent[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // Method to get valid index input (non-negative and within bounds)
    static int getValidIndex(Scanner sc, String prompt) {
        int index = -1;
        while (true) {
            try {
                System.out.print(prompt);
                index = sc.nextInt();
                sc.nextLine();  // consume newline
                if (index < 0 || index >= locationMap.size()) {
                    System.out.println("Invalid index. Please enter a valid index between 0 and " + (locationMap.size() - 1));
                } else {
                    break;  // valid input, break out of the loop
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.nextLine();  // clear the buffer
            }
        }
        return index;
    }
}

/*
 -------------------Output---------------------------------------
Available Zones:
0: Shivajinagar
1: Deccan
2: FC Road
3: JM Road
4: Kothrud
5: Swargate
6: Bibvewadi
7: Hadapsar
8: Kharadi
9: Viman Nagar
10: Koregaon Park
11: Camp
12: Magarpatta
13: Baner
14: Wakad
Enter source index: 4
Enter destination index: 11

Safest Path from Kothrud to Camp:
Kothrud -> Deccan -> JM Road -> Swargate -> Bibvewadi -> Hadapsar -> Kharadi -> Viman Nagar -> Koregaon Park -> Camp

Available Zones:
0: Shivajinagar
1: Deccan
2: FC Road
3: JM Road
4: Kothrud
5: Swargate
6: Bibvewadi
7: Hadapsar
8: Kharadi
9: Viman Nagar
10: Koregaon Park
11: Camp
12: Magarpatta
13: Baner
14: Wakad
Enter source index: 65348
Invalid index. Please enter a valid index between 0 and 14
Enter source index: 2
Enter destination index: 9

Safest Path from FC Road to Viman Nagar:
FC Road -> JM Road -> Deccan -> Kothrud -> Swargate -> Bibvewadi -> Hadapsar -> Kharadi -> Viman Nagar


Available Zones:
0: Shivajinagar
1: Deccan
2: FC Road
3: JM Road
4: Kothrud
5: Swargate
6: Bibvewadi
7: Hadapsar
8: Kharadi
9: Viman Nagar
10: Koregaon Park
11: Camp
12: Magarpatta
13: Baner
14: Wakad
Enter source index: -8
Invalid index. Please enter a valid index between 0 and 14
Enter source index: 9
Enter destination index: 1

Safest Path from Viman Nagar to Deccan:
Viman Nagar -> Kharadi -> Hadapsar -> Bibvewadi -> Swargate -> JM Road -> FC Road -> Shivajinagar -> Deccan

Available Zones:
0: Shivajinagar
1: Deccan
2: FC Road
3: JM Road
4: Kothrud
5: Swargate
6: Bibvewadi
7: Hadapsar
8: Kharadi
9: Viman Nagar
10: Koregaon Park
11: Camp
12: Magarpatta
13: Baner
14: Wakad
Enter source index: wakad
Invalid input. Please enter a valid integer.
Enter source index: 9
Enter destination index: 1

Safest Path from Viman Nagar to Deccan:
Viman Nagar -> Kharadi -> Hadapsar -> Bibvewadi -> Swargate -> JM Road -> FC Road -> Shivajinagar -> Deccan

*/