package buffer;

import java.util.*;
import java.io.*;

class Volunteer {
    String name;
    String phone;
    String address;
    String city;
    String aidType;
    double amount;

    public Volunteer(String name, String phone, String address, String city, String aidType, double amount) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city.toLowerCase();
        this.aidType = aidType.toLowerCase();
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                ", Phone: " + phone +
                ", Address: " + address +
                ", Aid Type: " + aidType +
                (aidType.equals("money") ? ", Amount: ₹" + amount : "");
    }

    public String toFileString() {
        return name + ";" + phone + ";" + address + ";" + city + ";" + aidType + ";" + amount;
    }

    public static Volunteer fromFileString(String data) {
        String[] parts = data.split(";");
        return new Volunteer(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                Double.parseDouble(parts[5])
        );
    }
}

class Graph {
    private Map<String, Map<String, Integer>> adjList = new HashMap<>();

    public void addEdge(String source, String destination, int distance) {
        adjList.putIfAbsent(source, new HashMap<>());
        adjList.putIfAbsent(destination, new HashMap<>());
        adjList.get(source).put(destination, distance);
        adjList.get(destination).put(source, distance);  // Assuming undirected graph
    }

    public List<String> shortestPath(String source, String destination) {
        // Implementing a simple Dijkstra’s Algorithm or BFS for shortest path
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        PriorityQueue<String> nodes = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        adjList.keySet().forEach(node -> {
            distances.put(node, Integer.MAX_VALUE);
            previousNodes.put(node, null);
            nodes.add(node);
        });

        distances.put(source, 0);

        while (!nodes.isEmpty()) {
            String currentNode = nodes.poll();
            if (currentNode.equals(destination)) {
                break;
            }

            adjList.getOrDefault(currentNode, Collections.emptyMap()).forEach((neighbor, distance) -> {
                int newDist = distances.get(currentNode) + distance;
                if (newDist < distances.get(neighbor)) {
                    nodes.remove(neighbor);
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, currentNode);
                    nodes.add(neighbor);
                }
            });
        }

        List<String> path = new ArrayList<>();
        for (String node = destination; node != null; node = previousNodes.get(node)) {
            path.add(node);
        }
        Collections.reverse(path);
        return path.size() == 1 ? Collections.emptyList() : path;  // return empty if no path
    }
}

public class VolunteerCityMap {
    static Map<String, List<Volunteer>> cityVolunteers = new HashMap<>();
    static List<Volunteer> allVolunteers = new ArrayList<>();
    static double totalFunds = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Load existing data from files
        loadDataFromFiles();

        // GRAPH SYSTEM
        Graph graph = new Graph();
        
        // Adding edges for various cities as per the initial data
        //pune,mumbai,nashik,nagpur,chandrapur,thane,raigad,wayanad,ratnagiri,sangli,palghar,satara
      //pune
        graph.addEdge("Karve Nagar", "Pimpri Chinchwad", 21);
        graph.addEdge("Karve Nagar", "Shivaji Nagar", 7);
        graph.addEdge("Karve Nagar", "Hadapsar", 15);
        graph.addEdge("Karve Nagar", "Camp", 9);
        graph.addEdge("Karve Nagar", "Swargate", 7);
        graph.addEdge("Karve Nagar", "Rasta Peth", 10);
        graph.addEdge("Pimpri Chinchwad", "Shivaji Nagar",16 );
        graph.addEdge("Pimpri Chinchwad", "Hadapsar", 22);
        graph.addEdge("Pimpri Chinchwad", "Camp", 17);
        graph.addEdge("Pimpri Chinchwad", "Swargate", 20);
        graph.addEdge("Pimpri Chinchwad", "Rasta Peth", 17);
        graph.addEdge("Shivaji Nagar", "Hadapsar", 13);
        graph.addEdge("Shivaji Nagar", "Camp", 6);
        graph.addEdge("Shivaji Nagar", "Swargate", 5);
        graph.addEdge("Shivaji Nagar", "Rasta Peth", 5);
        graph.addEdge("Hadapsar", "Camp", 9);
        graph.addEdge("Hadapsar", "Swargate", 9);
        graph.addEdge("Hadapsar", "Rasta Peth",10 );
        graph.addEdge("Camp", "Swargate", 4);
        graph.addEdge("Camp", "Rasta Peth", 2);
        graph.addEdge("Swargate", "Rasta Peth", 3);
        
      //Mumbai
        graph.addEdge("SantaCruz", "Kurla", 8);
        graph.addEdge("SantaCruz", "SakiNaka", 9);
        graph.addEdge("SantaCruz", "Chembur", 16);
        graph.addEdge("SantaCruz", "Vakola", 11);
        graph.addEdge("SantaCruz", "Ghatkopar", 12);
        graph.addEdge("Kurla", "SakiNaka", 4);
        graph.addEdge("Kurla", "Chembur", 6);
        graph.addEdge("Kurla", "Vakola", 6);
        graph.addEdge("Kurla", "Ghatkopar", 4);
        graph.addEdge("SakiNaka", "Chembur", 8);
        graph.addEdge("SakiNaka", "Vakola", 7);
        graph.addEdge("SakiNaka", "Ghatkopar", 5);
        graph.addEdge("Chembur", "Vakola", 9);
        graph.addEdge("Chembur", "Ghatkopar", 6);
        graph.addEdge("Vakola", "Ghatkopar", 9);
        
      //nashik
        graph.addEdge("Gangapur Road", "Makhmalabad", 18);
        graph.addEdge("Makhmalabad", "Indira Nagar", 9);
        graph.addEdge("Makhmalabad", "Pathardi Phata", 9);
        graph.addEdge("Makhmalabad", "Jail Road", 9);
        graph.addEdge("Makhmalabad", "Serene Meadows", 9);
        graph.addEdge("Indira Nagar", "Pathardi Phata", 13);
        graph.addEdge("Indira Nagar", "Jail Road", 13);
        graph.addEdge("Indira Nagar", "Serene Meadows", 13);
        graph.addEdge("Indira Nagar", "Gangapur Road", 13);
        graph.addEdge("Pathardi Phata", "Jail Road", 13);
        graph.addEdge("Pathardi Phata", "Serene Meadow", 13);
        graph.addEdge("Pathardi Phata", "Gangapur Road", 13);
        graph.addEdge("Jail Road", "Serene Meadow", 13);
        graph.addEdge("Jail Road", "Gangapur Road", 13);
        graph.addEdge("Serene Meadows", "Gangapur Road", 13);
        
        
        //Nagpur
        graph.addEdge("DharamPeth", "Nadanvan", 7);
        graph.addEdge("DharamPeth", "Itwari", 7);
        graph.addEdge("DharamPeth", "Dhantoli", 3);
        graph.addEdge("DharamPeth", "Ghogali", 28);
        graph.addEdge("DharamPeth", "Hingna", 10);
        graph.addEdge("Nadanvan", "Itwari",4);
        graph.addEdge("Nadanvan", "Dhantoli", 6);
        graph.addEdge("Nadanvan", "Ghogali", 32);
        graph.addEdge("Nadanvan", "Hingna", 17);
        graph.addEdge("Itwari", "Dhantoli", 5);
        graph.addEdge("Itwari", "Ghogali", 30);
        graph.addEdge("Itwari", "Hingna", 16);
        graph.addEdge("Dhantoli", "Ghogali", 11);
        graph.addEdge("Dhantoli", "Hingna", 12);
        graph.addEdge("Ghogali", "Hingna", 17);

         //Chandrapur
        graph.addEdge("Tukum", "Ramnagar", 3);
        graph.addEdge("Tukum", "Morwa", 12);
        graph.addEdge("Tukum", "BabuPeth", 6);
        graph.addEdge("Tukum", "Durgapur", 5);
        graph.addEdge("Tukum", "Kosara", 7);
        graph.addEdge("Ramnagar", "Morwa", 12);
        graph.addEdge("Ramnagar", "BabuPeth", 5);
        graph.addEdge("Ramnagar", "Durgapur", 8);
        graph.addEdge("Ramnagar", "Kosara", 5);
        graph.addEdge("Morwa", "BabuPeth", 17);
        graph.addEdge("Morwa", "Durgapur", 12);
        graph.addEdge("Morwa", "Kosara", 9);
        graph.addEdge("BabuPeth", "Durgapur", 11);
        graph.addEdge("BabuPeth", "Kosara", 10);
        graph.addEdge("Durgapur", "Kosara", 8);


        //thane
        graph.addEdge("Diva", "Kharegaon", 17);
        graph.addEdge("Diva", "ThaneWest", 24);
        graph.addEdge("Diva", "Dhokali", 23);
        graph.addEdge("Diva", "Kolbad", 21);
        graph.addEdge("Diva", "Yeoor", 26);
        graph.addEdge("Kharegaon", "ThaneWest", 8);
        graph.addEdge("Kharegaon", "Dhokali", 6);
        graph.addEdge("Kharegaon", "Kolbad",4 );
        graph.addEdge("Kharegaon", "Yeoor", 9);
        graph.addEdge("ThaneWest", "Dhokali", 3);
        graph.addEdge("ThaneWest", "Kolbad", 5);
        graph.addEdge("ThaneWest", "Yeoor", 8);
        graph.addEdge("Dhokali", "Kolbad", 3);
        graph.addEdge("Dhokali", "Yeoor", 8);
        graph.addEdge("Kolbad", "Yeoor", 7);        
        
        //raigad
        graph.addEdge("Bavale", "Walsure", 17);
        graph.addEdge("Bavale", "Pachad", 10);
        graph.addEdge("Bavale", "HirkaniWadi", 9);
        graph.addEdge("Bavale", "Vagheri", 64);
        graph.addEdge("Bavale", "Raigadwadi", 6);
        graph.addEdge("Walsure", "Pachad", 7);
        graph.addEdge("Walsure", "HirkaniWadi", 10);
        graph.addEdge("Walsure", "Vagheri", 50);
        graph.addEdge("Walsure", "Raigadwadi", 11);
        graph.addEdge("Pachad", "HirkaniWadi", 3);
        graph.addEdge("Pachad", "Vagheri", 54);
        graph.addEdge("Pachad", "Raigadwadi", 4);
        graph.addEdge("HirkaniWadi", "Vagheri", 55);
        graph.addEdge("HirkaniWadi", "Raigadwadi", 4);
        graph.addEdge("Vagheri", "Raigadwadi", 58);
        
      //wayanad
        graph.addEdge("Meenangadi", "Pachilakaadu", 13);
        graph.addEdge("Meenangadi", "Varadoor", 11);
        graph.addEdge("Meenangadi", "Millumukku", 15);
        graph.addEdge("Meenangadi", "Koodothummal", 11);
        graph.addEdge("Meenangadi", "Kaniyambetta", 11);
        graph.addEdge("Pachilakaadu", "Kaniyambetta", 4);
        graph.addEdge("Pachilakaadu", "Varadoor", 4);
        graph.addEdge("Pachilakaadu", "Millumukku", 3);
        graph.addEdge("Pachilakaadu", "Koodothummal", 2);
        graph.addEdge("Varadoor", "Koodothummal", 3);
        graph.addEdge("Varadoor", "Millumukku", 6);
        graph.addEdge("Varadoor", "Kaniyambetta", 3);
        graph.addEdge("Millumukku", "Koodothummal", 3);
        graph.addEdge("Millumukku", "Kaniyambetta", 6);
        graph.addEdge("Kaniyambetta", "Koodothummal", 3);
        
        //ratnagiri
        graph.addEdge("Madhaliwadi", "Nachane", 5);
        graph.addEdge("Madhaliwadi", "Adishkatinagar", 6);
        graph.addEdge("Madhaliwadi", "Mirjole", 3);
        graph.addEdge("Madhaliwadi", "Zadgaon", 8);
        graph.addEdge("Madhaliwadi", "Rajiwada",7 );
        graph.addEdge("Nachane", "Adishkatinagar", 5);
        graph.addEdge("Nachane", "Mirjole", 7);
        graph.addEdge("Nachane", "Zadgaon", 7);
        graph.addEdge("Nachane", "Rajiwada", 6);
        graph.addEdge("Adishkatinagar", "Mirjole", 8);
        graph.addEdge("Adishkatinagar", "Zadgaon", 10);
        graph.addEdge("Adishkatinagar", "Rajiwada", 10);
        graph.addEdge("Mirjole", "Zadgaon", 9);
        graph.addEdge("Mirjole", "Rajiwadi", 8);
        graph.addEdge("Zadgaon", "Rajiwada", 2);


        //sangli
        graph.addEdge("Vijayanagar", "Bhadakewadi", 3);
        graph.addEdge("Vijayanagar", "Dhondewadi", 11);
        graph.addEdge("Vijayanagar", "Datta Nagar", 50);
        graph.addEdge("Vijayanagar", "Kala Nagar", 49);
        graph.addEdge("Vijayanagar", "Madhav Nagar", 46);
        graph.addEdge("Bhadakewadi", "Dhondewadi", 6);
        graph.addEdge("Bhadakewadi", "Datta Nagar", 60);
        graph.addEdge("Bhadakewadi", "Kala Nagar", 51);
        graph.addEdge("Bhadakewadi", "Madhav Nagar", 49);
        graph.addEdge("Dhondewadi", "Datta Nagar", 62);
        graph.addEdge("Dhondewadi", "Kala Nagar", 57);
        graph.addEdge("Dhondewadi", "Madhav Nagar", 54);
        graph.addEdge("Datta Nagar", "Kala Nagar", 6);
        graph.addEdge("Datta Nagar", "Madhav Nagar", 7);
        graph.addEdge("Kala Nagar", "Madhav Nagar", 3);

        //palghar
        graph.addEdge("Vevoor", "Navali", 1);
        graph.addEdge("Vevoor", "Boisar", 3);
        graph.addEdge("Vevoor", "Vajulsar", 5);
        graph.addEdge("Vevoor", "Haranwali",6);
        graph.addEdge("Vevoor", "Udhyog Nagar", 3);
        graph.addEdge("Navali", "Boisar", 4);
        graph.addEdge("Navali", "Vajulsar", 6);
        graph.addEdge("Navali", "Haranwali", 7);
        graph.addEdge("Navali", "Udhyog Nagar", 3);
        graph.addEdge("Boisar", "Vajulsar", 5);
        graph.addEdge("Boisar", "Haranwali", 5);
        graph.addEdge("Boisar", "Udhyog Nagar", 4);
        graph.addEdge("Vajulsar", "Haranwali", 2);
        graph.addEdge("Vajulsar", "Udhyog Nagar", 5);
        graph.addEdge("Haranwali", "Udhyog Nagar", 5);

        //satara
        graph.addEdge("Khed", "Kshetra Mahul", 7);
        graph.addEdge("Khed", "Mhasave", 5);
        graph.addEdge("Khed", "Shahupuri", 7);
        graph.addEdge("Khed", "Karandwadi", 8);
        graph.addEdge("Khed", "Saidapur", 7);
        graph.addEdge("Kshetra Mahul", "Mhasave", 10);
        graph.addEdge("Kshetra Mahul", "Shahupuri", 10);
        graph.addEdge("Kshetra Mahul", "Karandwadi", 9);
        graph.addEdge("Kshetra Mahul", "Saidapur", 10);
        graph.addEdge("Mhasave", "Shahupuri", 10);
        graph.addEdge("Mhasave", "Karandwadi", 11);
        graph.addEdge("Mhasave", "Saidapur", 9);
        graph.addEdge("Shahupuri", "Karandwadi", 11);
        graph.addEdge("Shahupuri", "Saidapur", 2);
        graph.addEdge("Karandwadi", "Saidapur", 11);



        
        
        // (Continue to add all other edges as in your original graph setup)
        
        // User Volunteer Registration
        System.out.println("=== Disaster Relief Volunteer Registration ===");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter your address (area name): ");
        String address = scanner.nextLine();

        System.out.print("Enter your city: ");
        String city = scanner.nextLine();

        System.out.print("Enter aid type (money/food/medical): ");
        String aidType = scanner.nextLine();

        double amount = 0;
        if (aidType.equalsIgnoreCase("money")) {
            System.out.print("Enter amount to donate (₹): ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                totalFunds += amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount! Defaulting to ₹0.");
            }
        }

        Volunteer volunteer = new Volunteer(name, phone, address, city, aidType, amount);
        allVolunteers.add(volunteer);

        cityVolunteers.putIfAbsent(city.toLowerCase(), new ArrayList<>());
        cityVolunteers.get(city.toLowerCase()).add(volunteer);

        saveDataToFiles();

        System.out.println("\n✅ Volunteer Registered Successfully!");

        printAllVolunteers();
        System.out.println("\n💰 Total Funds Collected: ₹" + totalFunds);

        // Shortest path demo
        System.out.print("\nEnter source location for shortest path: ");
        String source = scanner.nextLine();

        System.out.print("Enter destination location: ");
        String destination = scanner.nextLine();

        List<String> path = graph.shortestPath(source, destination);
        if (path.isEmpty()) {
            System.out.println("No path found between " + source + " and " + destination);
        } else {
            System.out.println("Shortest path from " + source + " to " + destination + ": " + path);
        }

        System.out.println("\n\n\nSome Important Helpline numbers for your convenience:\n\n");
        System.out.println("\t\tDisaster Management :  \t\t 1077\n\t\tNDRF Distress Helpline :  \t+919711077372");
        System.out.println("\t\tNDRF Headquarters :  \t\t 011-24363260\n\t\tNDMA Helpline :   \t\t 011-1078");

        scanner.close();
    }

    static void loadDataFromFiles() {
        // Load total funds
        try {
            File f = new File("funds.txt");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                totalFunds = Double.parseDouble(br.readLine().trim());
                br.close();
            }
        } catch (Exception e) {
            totalFunds = 0;
        }

        // Load volunteers
        try {
            File f = new File("volunteers.txt");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                while ((line = br.readLine()) != null) {
                    Volunteer v = Volunteer.fromFileString(line);
                    allVolunteers.add(v);
                    cityVolunteers.putIfAbsent(v.city, new ArrayList<>());
                    cityVolunteers.get(v.city).add(v);
                }
                br.close();
            }
        } catch (Exception e) {
            System.out.println("Error reading volunteer data.");
        }
    }

    static void saveDataToFiles() {
        // Save funds
        try (PrintWriter pw = new PrintWriter(new FileWriter("funds.txt"))) {
            pw.println(totalFunds);
        } catch (IOException e) {
            System.out.println("Error saving funds.");
        }

        // Save volunteers
        try (PrintWriter pw = new PrintWriter(new FileWriter("volunteers.txt"))) {
            for (Volunteer v : allVolunteers) {
                pw.println(v.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving volunteer data.");
        }
    }

    static void printAllVolunteers() {
        System.out.println("\n--- Volunteers by City ---");
        for (String city : cityVolunteers.keySet()) {
            System.out.println("City: " + city);
            for (Volunteer v : cityVolunteers.get(city)) {
                System.out.println("  - " + v);
            }
        }
    }
}




/**

package buffer;

import java.util.*;

class Volunteer {
    String name;
    String phone;
    String address;
    String helpType; // "money", "food", "medical"
    double contributionAmount; // If money, else 0

    public Volunteer(String name, String phone, String address, String helpType, double contributionAmount) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.helpType = helpType;
        this.contributionAmount = contributionAmount;
    }

    public String toString() {
        String base = "Name: " + name + ", Phone: " + phone + ", Address: " + address + ", Help Type: " + helpType;
        if (helpType.equalsIgnoreCase("money")) {
            base += ", Amount: ₹" + contributionAmount;
        }
        return base;
    }
}

class Edge {
    String destination;
    int weight;

    public Edge(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}

class Graph {
    Map<String, List<Edge>> adjList = new HashMap<>();

    void addEdge(String src, String dest, int weight) {
        adjList.computeIfAbsent(src, k -> new ArrayList<>()).add(new Edge(dest, weight));
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(new Edge(src, weight));
    }

    public List<String> shortestPath(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String area : adjList.keySet()) {
            distances.put(area, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(end)) break;

            for (Edge edge : adjList.getOrDefault(current, new ArrayList<>())) {
                int newDist = distances.get(current) + edge.weight;
                if (newDist < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDist);
                    previous.put(edge.destination, current);
                    pq.add(edge.destination);
                }
            }
        }

        List<String> path = new LinkedList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(0, at);
        }

        if (path.size() == 1 && !path.get(0).equals(start)) return Collections.emptyList();
        return path;
    }
}

public class VolunteerCityMap {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, List<Volunteer>> cityVolunteerMap = new HashMap<>();
        double totalVictimAid = 0.0; // Accumulator for financial help

        // Predefined volunteers
        cityVolunteerMap.put("pune", new ArrayList<>(Arrays.asList(
                new Volunteer("Amit", "9876543210", "Karve Nagar", "food", 0),
                new Volunteer("Sneha", "9123456780", "Pimpri Chinchwad", "medical", 0),
                new Volunteer("Shreeya", "9881228844", "Peth", "money", 2000)
        )));

        // (add other cities and volunteers here as before...)

        // USER REGISTRATION
        System.out.println("You have chosen to Become a Volunteer!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter your address (area name): ");
        String address = scanner.nextLine();

        System.out.print("Enter your city: ");
        String city = scanner.nextLine().toLowerCase();

        System.out.print("How would you like to help? (money/food/medical): ");
        String helpType = scanner.nextLine().toLowerCase();

        double amount = 0;
        if (helpType.equals("money")) {
            System.out.print("Enter the amount you want to donate (₹): ");
            while (true) {
                try {
                    amount = Double.parseDouble(scanner.nextLine());
                    if (amount <= 0) throw new Exception();
                    break;
                } catch (Exception e) {
                    System.out.print("Please enter a valid positive amount: ");
                }
            }
            totalVictimAid += amount;
        }

        Volunteer newVolunteer = new Volunteer(name, phone, address, helpType, amount);

        cityVolunteerMap.putIfAbsent(city, new ArrayList<>());
        cityVolunteerMap.get(city).add(newVolunteer);

        // Show updated list
        System.out.println("\n--- Updated Volunteer List ---");
        for (String c : cityVolunteerMap.keySet()) {
            System.out.println("\nCity: " + c);
            for (Volunteer v : cityVolunteerMap.get(c)) {
                System.out.println("  " + v);
            }
        }

        // Display total monetary aid
        System.out.println("\nTotal monetary aid received so far: ₹" + totalVictimAid);

        // GRAPH setup (add your existing graph edges here...)
        Graph graph = new Graph();
        // For brevity, I'm skipping re-pasting the entire graph.addEdge block here
        // Just add your existing graph.addEdge(...) lines below this comment

        // Shortest path demo
        System.out.print("\nEnter source location for shortest path: ");
        String source = scanner.nextLine();

        System.out.print("Enter destination location: ");
        String destination = scanner.nextLine();

        List<String> path = graph.shortestPath(source, destination);
        if (path.isEmpty()) {
            System.out.println("No path found between " + source + " and " + destination);
        } else {
            System.out.println("Shortest path from " + source + " to " + destination + ": " + path);
        }

        System.out.println("\n\nSome Important Helpline numbers for your convenience:\n");
        System.out.println("\t\tDisaster Management :  \t\t 1077\n\t\tNDRF Distress Helpline :  \t+919711077372");
        System.out.println("\t\tNDRF Headquarters :  \t\t 011-24363260\n\t\tNDMA Helpline :   \t\t 011-1078");

        scanner.close();
    }
}








package buffer;

import java.util.*;

class Volunteer {
    String name;
    String phone;
    String address;

    public Volunteer(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String toString() {
        return "Name: " + name +
               ", Phone: " + phone +
               ", Address: " + address;
    }
}

class Edge {
    String destination;
    int weight;

    public Edge(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}

class Graph {
    Map<String, List<Edge>> adjList = new HashMap<>();

    void addEdge(String src, String dest, int weight) {
        adjList.computeIfAbsent(src, k -> new ArrayList<>()).add(new Edge(dest, weight));
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(new Edge(src, weight));
    }

    public List<String> shortestPath(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String area : adjList.keySet()) {
            distances.put(area, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(end)) break;

            for (Edge edge : adjList.getOrDefault(current, new ArrayList<>())) {
                int newDist = distances.get(current) + edge.weight;
                if (newDist < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDist);
                    previous.put(edge.destination, current);
                    pq.add(edge.destination);
                }
            }
        }

        List<String> path = new LinkedList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(0, at);
        }

        if (path.size() == 1 && !path.get(0).equals(start)) return Collections.emptyList();
        return path;
    }
}

public class VolunteerCityMap {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Volunteer Map
        Map<String, List<Volunteer>> cityVolunteerMap = new HashMap<>();

        // Predefined volunteers
        cityVolunteerMap.put("pune", new ArrayList<>(Arrays.asList(
            new Volunteer("Amit", "9876543210", "Karve Nagar"),
            new Volunteer("Sneha", "9123456780", "Pimpri Chinchwad"),
            new Volunteer("Shreeya", "9881228844", "Peth")
        )));
        
        //mumbai
        cityVolunteerMap.put("mumbai", new ArrayList<>(Arrays.asList(
            new Volunteer("Raj", "9898989898", "Dombivali"),
            new Volunteer("Nina", "8529637412", "Kandivali"),
            new Volunteer("Aditya", "9021919492", "Malad")

        )));
        
        //nashik
        cityVolunteerMap.put("nashik", new ArrayList<>(Arrays.asList(
                new Volunteer("Niraj", "7894561230", "Gangapur"),
                new Volunteer("Sagar", "9511858978", "Makhmalabad"),
                new Volunteer("Sandhya", "9823399551", "Indira Nagar")
            )));
        
        //nagpur
        cityVolunteerMap.put("nagpur", new ArrayList<>(Arrays.asList(
                new Volunteer("Anushree", "9898989898", "DharamPeth"),
                new Volunteer("Sanjeev", "8529637412", "Nadanvan"),
                new Volunteer("Anushka", "9021919492", "Manish Nagar")
            )));
        
        //chandrapur
        cityVolunteerMap.put("chandrapur", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Tukum"),
                new Volunteer("Nina", "8529637412", "Ramnagar"),
                new Volunteer("Sagar", "9511858978", "Dikshit")
            )));
        
        //thane
        cityVolunteerMap.put("thane", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Diva"),
                new Volunteer("Nina", "8529637412", "Kharegaon"),
                new Volunteer("Sagar", "9511858978", "Thanewest")
            )));
        
        //raigad
        cityVolunteerMap.put("raigad", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Khopoli"),
                new Volunteer("Nina", "8529637412", "Alibaug"),
                new Volunteer("Sagar", "9511858978", "Mangaon")
            )));
        
        
        //wayanad
        cityVolunteerMap.put("wayanad", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Meenangadi"),
                new Volunteer("Nina", "8529637412", "Pachilakaadu"),
                new Volunteer("Sagar", "9511858978", "Kaniyambetta")
            )));
        
        //ratnagiri
        cityVolunteerMap.put("ratnagiri", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Madhaliwadi"),
                new Volunteer("Nina", "8529637412", "Nachane"),
                new Volunteer("Sagar", "9511858978", "Adishkatinagar")
            )));
                
      //sangli
        cityVolunteerMap.put("sangli", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Vijayanagar"),
                new Volunteer("Nina", "8529637412", "Chinatamani"),
                new Volunteer("Sagar", "9511858978", "Abhay")
            )));
        
      //palghar
        cityVolunteerMap.put("palghar", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Vevoor"),
                new Volunteer("Nina", "8529637412", "Navali"),
                new Volunteer("Sagar", "9511858978", "Boisar")
            )));
        
      //satara
        cityVolunteerMap.put("satara", new ArrayList<>(Arrays.asList(
                new Volunteer("Raj", "9898989898", "Khed"),
                new Volunteer("Nina", "8529637412", "Kshetra Mahuli"),
                new Volunteer("Sagar", "9511858978", "Mhasave")
            )));

        // Add a new volunteer using user input
        System.out.println("You have chosen to Become a Volunteer!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter your address (area name): ");
        String address = scanner.nextLine();

        System.out.print("Enter your city: ");
        String city = scanner.nextLine().toLowerCase();

        Volunteer newVolunteer = new Volunteer(name, phone, address);

        cityVolunteerMap.putIfAbsent(city, new ArrayList<>());
        cityVolunteerMap.get(city).add(newVolunteer);

        // Display updated city-wise volunteers
        System.out.println("\n--- Updated Volunteer List ---");
        for (String c : cityVolunteerMap.keySet()) {
            System.out.println("\nCity: " + c);
            for (Volunteer v : cityVolunteerMap.get(c)) {
                System.out.println("  " + v);
            }
        }

        // GRAPH SYSTEM
        Graph graph = new Graph();
        
        //Pune
        graph.addEdge("Karve Nagar", "Pimpri Chinchwad", 21);
        graph.addEdge("Karve Nagar", "Peth", 9);
        graph.addEdge("Pimpri Chinchwad", "Peth", 16);
        
       //Mumbai
        graph.addEdge("Dombivali", "Kandivali", 21);
        graph.addEdge("Kandivali", "Malad", 9);
        graph.addEdge("Malad", "Dombivali", 16);
        
      //nashik
        graph.addEdge("Gangapur Road", "Makhmalabad", 11);
        graph.addEdge("Makhmalabad", "Indira Nagar", 10);
        graph.addEdge("Makhmalabad", "Pathardi Phata", 13);
        graph.addEdge("Makhmalabad", "Jail Road", 13);
        graph.addEdge("Makhmalabad", "Serene Meadows", 10);
        graph.addEdge("Indira Nagar", "Pathardi Phata", 3);
        graph.addEdge("Indira Nagar", "Jail Road", 8);
        graph.addEdge("Indira Nagar", "Serene Meadows", 9);
        graph.addEdge("Indira Nagar", "Gangapur Road", 6);
        graph.addEdge("Pathardi Phata", "Jail Road", 12);
        graph.addEdge("Pathardi Phata", "Serene Meadow", 13);
        graph.addEdge("Pathardi Phata", "Gangapur Road", 10);
        graph.addEdge("Jail Road", "Serene Meadow", 15);
        graph.addEdge("Jail Road", "Gangapur Road", 11);
        graph.addEdge("Serene Meadows", "Gangapur Road", 5);

        
        //Nagpur
        graph.addEdge("DharamPeth", "Nadanvan", 7);
        graph.addEdge("Nadanvan", "Manish Nagar", 10);
        graph.addEdge("Manish Nagar", "DharamPeth", 8);
        
        //Chandrapur
        graph.addEdge("Tukum", "Ramnagar", 3);
        graph.addEdge("Ramnagar", "Dikshit", 2);
        graph.addEdge("Dikshit", "Tukum", 4);

        //thane
        graph.addEdge("Diva", "Kharegaon", 17);
        graph.addEdge("Kharegaon", "Thanewest", 7);
        graph.addEdge("Thanewest", "Diva", 24);
        
        //raigad
        graph.addEdge("Khopoli", "Alibaug", 63);
        graph.addEdge("Alibaug", "Mangaon",81 );
        graph.addEdge("Mangaon", "Khopoli", 82);
        
      //wayanad
        graph.addEdge("Meenangadi", "Pachilakaadu", 13);
        graph.addEdge("Meenangadi", "Varadoor", 11);
        graph.addEdge("Meenangadi", "Millumukku", 15);
        graph.addEdge("Meenangadi", "Koodothummal", 11);
        graph.addEdge("Meenangadi", "Kaniyambetta", 11);
        graph.addEdge("Pachilakaadu", "Kaniyambetta", 4);
        graph.addEdge("Pachilakaadu", "Varadoor", 4);
        graph.addEdge("Pachilakaadu", "Millumukku", 3);
        graph.addEdge("Pachilakaadu", "Koodothummal", 2);
        graph.addEdge("Varadoor", "Koodothummal", 3);
        graph.addEdge("Varadoor", "Millumukku", 6);
        graph.addEdge("Varadoor", "Kaniyambetta", 3);
        graph.addEdge("Millumukku", "Koodothummal", 3);
        graph.addEdge("Millumukku", "Kaniyambetta", 6);
        graph.addEdge("Kaniyambetta", "Koodothummal", 3);
        
        //ratnagiri
        graph.addEdge("Madhaliwadi", "Nachane", 5);
        graph.addEdge("Madhaliwadi", "Adishkatinagar", 6);
        graph.addEdge("Madhaliwadi", "Mirjole", 3);
        graph.addEdge("Madhaliwadi", "Zadgaon", 8);
        graph.addEdge("Madhaliwadi", "Rajiwada",7 );
        graph.addEdge("Nachane", "Adishkatinagar", 5);
        graph.addEdge("Nachane", "Mirjole", 7);
        graph.addEdge("Nachane", "Zadgaon", 7);
        graph.addEdge("Nachane", "Rajiwada", 6);
        graph.addEdge("Adishkatinagar", "Mirjole", 8);
        graph.addEdge("Adishkatinagar", "Zadgaon", 10);
        graph.addEdge("Adishkatinagar", "Rajiwada", 10);
        graph.addEdge("Mirjole", "Zadgaon", 9);
        graph.addEdge("Mirjole", "Rajiwadi", 8);
        graph.addEdge("Zadgaon", "Rajiwada", 2);


        //sangli
        graph.addEdge("Vijayanagar", "Bhadakewadi", 3);
        graph.addEdge("Vijayanagar", "Dhondewadi", 11);
        graph.addEdge("Vijayanagar", "Datta Nagar", 50);
        graph.addEdge("Vijayanagar", "Kala Nagar", 49);
        graph.addEdge("Vijayanagar", "Madhav Nagar", 46);
        graph.addEdge("Bhadakewadi", "Dhondewadi", 6);
        graph.addEdge("Bhadakewadi", "Datta Nagar", 60);
        graph.addEdge("Bhadakewadi", "Kala Nagar", 51);
        graph.addEdge("Bhadakewadi", "Madhav Nagar", 49);
        graph.addEdge("Dhondewadi", "Datta Nagar", 62);
        graph.addEdge("Dhondewadi", "Kala Nagar", 57);
        graph.addEdge("Dhondewadi", "Madhav Nagar", 54);
        graph.addEdge("Datta Nagar", "Kala Nagar", 6);
        graph.addEdge("Datta Nagar", "Madhav Nagar", 7);
        graph.addEdge("Kala Nagar", "Madhav Nagar", 3);




        //palghar
        graph.addEdge("Vevoor", "Navali", 1);
        graph.addEdge("Vevoor", "Boisar", 3);
        graph.addEdge("Vevoor", "Vajulsar", 5);
        graph.addEdge("Vevoor", "Haranwali",6);
        graph.addEdge("Vevoor", "Udhyog Nagar", 3);
        graph.addEdge("Navali", "Boisar", 4);
        graph.addEdge("Navali", "Vajulsar", 6);
        graph.addEdge("Navali", "Haranwali", 7);
        graph.addEdge("Navali", "Udhyog Nagar", 3);
        graph.addEdge("Boisar", "Vajulsar", 5);
        graph.addEdge("Boisar", "Haranwali", 5);
        graph.addEdge("Boisar", "Udhyog Nagar", 4);
        graph.addEdge("Vajulsar", "Haranwali", 2);
        graph.addEdge("Vajulsar", "Udhyog Nagar", 5);
        graph.addEdge("Haranwali", "Udhyog Nagar", 5);

        //satara
        graph.addEdge("Khed", "Kshetra Mahul", 7);
        graph.addEdge("Khed", "Mhasave", 5);
        graph.addEdge("Khed", "Shahupuri", 7);
        graph.addEdge("Khed", "Karandwadi", 8);
        graph.addEdge("Khed", "Saidapur", 7);
        graph.addEdge("Kshetra Mahul", "Mhasave", 10);
        graph.addEdge("Kshetra Mahul", "Shahupuri", 10);
        graph.addEdge("Kshetra Mahul", "Karandwadi", 9);
        graph.addEdge("Kshetra Mahul", "Saidapur", 10);
        graph.addEdge("Mhasave", "Shahupuri", 10);
        graph.addEdge("Mhasave", "Karandwadi", 11);
        graph.addEdge("Mhasave", "Saidapur", 9);
        graph.addEdge("Shahupuri", "Karandwadi", 11);
        graph.addEdge("Shahupuri", "Saidapur", 2);
        graph.addEdge("Karandwadi", "Saidapur", 11);

        
        // Shortest path demo
        System.out.print("\nEnter source location for shortest path: ");
        String source = scanner.nextLine();

        System.out.print("Enter destination location: ");
        String destination = scanner.nextLine();

        List<String> path = graph.shortestPath(source, destination);
        if (path.isEmpty()) {
            System.out.println("No path found between " + source + " and " + destination);
        } else {
            System.out.println("Shortest path from " + source + " to " + destination + ": " + path);
        }
        
        System.out.println("\n\n\nSome Important Helpline numbers for your convenience:\n\n");
        System.out.println("\t\tDisaster Management :  \t\t 1077\n\t\tNDRF Distress Helpline :  \t+919711077372");
        System.out.println("\t\tNDRF Headquarters :  \t\t 011-24363260\n\t\tNDMA Helpline :   \t\t 011-1078");

        scanner.close();
    }
}
*/