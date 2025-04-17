package buffer;
import java.util.*;

public class Buffer {
	
	    // Define a custom class to hold all the lists
	    static class PlaceInfo {
	        List<String> nearbyAreas = new ArrayList<>();
	        List<String> farbyAreas = new ArrayList<>();
	        List<String> shoppingCenters = new ArrayList<>();
	        List<String> shelters = new ArrayList<>();

	        @Override
	        public String toString() {
	            return "Nearby Areas: " + nearbyAreas +
	                   ", Shopping Centers: " + shoppingCenters +
	                   ", Shelters: " + shelters;
	        }
	    }

	    // Class to represent an edge between two areas
	    static class Edge {
	        String destination;
	        int weight;

	        Edge(String destination, int weight) {
	            this.destination = destination;
	            this.weight = weight;
	        }
	    }

	    // Graph class using HashMap
	    static class Graph {
	        Map<String, List<Edge>> adjList = new HashMap<>();

	        void addEdge(String src, String dest, int weight) {
	            adjList.computeIfAbsent(src, k -> new ArrayList<>()).add(new Edge(dest, weight));
	            adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(new Edge(src, weight)); // undirected graph
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
	    
	    public static void main(String[] args) {

	        Scanner scanner = new Scanner(System.in);
	        VanArrivalTime vat=new VanArrivalTime();
	        Map<String, PlaceInfo> placeMap = new HashMap<>();
	        Graph graph = new Graph();
	        
	        
		    graph.addEdge("pune", "mumbai", 148);
		    graph.addEdge("pune", "nashik", 214);
	        graph.addEdge("pune", "nagpur", 1000);
	        graph.addEdge("pune", "chandrapur", 748);
	        graph.addEdge("pune", "thane", 154);
	        graph.addEdge("pune", "raigad", 135);
	        graph.addEdge("pune", "wayanad", 1035);
	        graph.addEdge("pune", "ratnagiri", 285);
	        graph.addEdge("pune", "sangli", 235);
	        graph.addEdge("pune", "palghar", 232);
	        graph.addEdge("pune", "satara", 113);
	
	        graph.addEdge("nashik", "mumbai", 166);
	        graph.addEdge("nashik", "nagpur", 624);
	        graph.addEdge("nashik", "chandrapur", 682);
	        graph.addEdge("nashik", "thane", 147);
	        graph.addEdge("nashik", "raigad", 298);
	        graph.addEdge("nashik", "wayanad", 1168);
	        graph.addEdge("nashik", "ratnagiri", 469);
	        graph.addEdge("nashik", "sangli", 426);
	        graph.addEdge("nashik", "palghar", 148);
	        graph.addEdge("nashik", "satara", 332);
	
	        graph.addEdge("mumbai", "nagpur", 843);
	        graph.addEdge("mumbai", "chandrapur", 838);
	        graph.addEdge("mumbai", "thane", 23);
	        graph.addEdge("mumbai", "raigad", 171);
	        graph.addEdge("mumbai", "wayanad", 1101);
	        graph.addEdge("mumbai", "ratnagiri", 342);
	        graph.addEdge("mumbai", "sangli", 381);
	        graph.addEdge("mumbai", "palghar", 94);
	        graph.addEdge("mumbai", "satara", 258);
	
	        graph.addEdge("thane", "nagpur", 753);
	        graph.addEdge("thane", "chandrapur", 820);
	        graph.addEdge("thane", "raigad", 182);
	        graph.addEdge("thane", "wayanad", 1108);
	        graph.addEdge("thane", "ratnagiri", 350);
	        graph.addEdge("thane", "sangli", 387);
	        graph.addEdge("thane", "palghar", 81);
	        graph.addEdge("thane", "satara", 265);
	
	
	        graph.addEdge("nagpur", "chandrapur", 153);
	        graph.addEdge("nagpur", "raigad", 824);
	        graph.addEdge("nagpur", "wayanad", 1492);
	        graph.addEdge("nagpur", "ratnagiri", 961);
	        graph.addEdge("nagpur", "sangli", 786);
	        graph.addEdge("nagpur", "palghar", 762);
	        graph.addEdge("nagpur", "satara", 760);       
	
	       
	        graph.addEdge("chandrapur", "raigad", 951);
	        graph.addEdge("chandrapur", "wayanad", 1259);
	        graph.addEdge("chandrapur", "ratnagiri", 972);
	        graph.addEdge("chandrapur", "sangli", 735);
	        graph.addEdge("chandrapur", "palghar", 829);
	        graph.addEdge("chandrapur", "satara", 822);
	
	
	        graph.addEdge("raigad", "wayanad", 1010);
	        graph.addEdge("raigad", "ratnagiri", 197);
	        graph.addEdge("raigad", "sangli", 264);
	        graph.addEdge("raigad", "palghar", 251);
	        graph.addEdge("raigad", "satara", 142);
	
	       
	        graph.addEdge("wayanad", "ratnagiri", 835);
	        graph.addEdge("wayanad", "sangli", 752);
	        graph.addEdge("wayanad", "palghar", 1178);
	        graph.addEdge("wayanad", "satara", 844);
	
	  
	        graph.addEdge("ratnagir", "sangli", 171);
	        graph.addEdge("ratnagir", "palghar", 422);
	        graph.addEdge("ratnagir", "satara",190 );
	
	        graph.addEdge("sangli", "palghar", 458);
	        graph.addEdge("sangli", "satara", 124);
	
	        graph.addEdge("palghar", "satara", 355);
	         
	        
	        
	        // Disaster input
	        System.out.println("Enter The Area Name where Disaster has occurred  ::     ");
	        String affectedArea = scanner.next().toLowerCase();
	        System.out.println("What type of disaster has occurred??\n1.Landslide\n2.EarthQuake\n3.Flood\n4.Fire");
	        int tod = scanner.nextInt();

	        PlaceInfo pune = new PlaceInfo();
			        pune.nearbyAreas.add("Shikrapur");
			        pune.nearbyAreas.add("Chakan");
			        pune.nearbyAreas.add("Lonavla");
			        pune.nearbyAreas.add("Mahabaleshwar");
			        pune.farbyAreas.add("Kolhapur");
			        pune.farbyAreas.add("Nagpur");
			        pune.farbyAreas.add("Nashik");
			        pune.farbyAreas.add("Mumbai");
			        pune.shoppingCenters.add("Amanora");
			        pune.shoppingCenters.add("Kumar Pacific");
			        pune.shelters.add("Sanjivsni NGO\nAdress  :  SR NO 59/1A, SULAI COMPLEX, FLAT NO 17, near DESAI HOSPITAL, Mohammed Wadi, pune, Maharashtra 411060");
			        pune.shelters.add("Spherule Foundation\nAddress  :  E02, Gokul Gardens, Anand Vidya Niketan High School Rd, Konark Nagar, Mhada Colony, Viman Nagar, pune, Maharashtra 411014");
			        pune.shelters.add("Marpu Foundation\nAddress  :  Sr.no.442, Sant Gadge Maharaj Vasahant, S Main Rd, Koregaon Park, pune, Maharashtra 411001");
			        pune.shelters.add("Wings For Dreams\\nAddress  :  Ashoka Mall, G4, Bund Garden Rd, Sangamvadi, pune, Maharashtra 411001");
			        placeMap.put("pune", pune);

			        // Adding data for "mumbai"
			        PlaceInfo mumbai = new PlaceInfo();
			        mumbai.nearbyAreas.add("Navi Mumbai");
			        mumbai.nearbyAreas.add("Thane");
			        mumbai.nearbyAreas.add("Kalyan");
			        mumbai.nearbyAreas.add("Karjat");
			        mumbai.farbyAreas.add("Pune");
			        mumbai.farbyAreas.add("Nashik");
			        mumbai.farbyAreas.add("Kolhapur");
			        mumbai.farbyAreas.add("Nagpur");
			        mumbai.shoppingCenters.add("Parkside Market");
			        mumbai.shoppingCenters.add("Parkside Market");
			        mumbai.shelters.add("Central Shelter A");
			        mumbai.shelters.add("Central Shelter A");
			        mumbai.shelters.add("Central Shelter A");
			        mumbai.shelters.add("Central Shelter A");
			        placeMap.put("mumbai", mumbai);
			        
			        //Adding data for nashik
			        PlaceInfo nashik = new PlaceInfo();
			        nashik.nearbyAreas.add("Sinnar");
			        nashik.nearbyAreas.add("Pimpalgaon");
			        nashik.nearbyAreas.add("Sangamner");
			        nashik.nearbyAreas.add("Saputara");
			        nashik.farbyAreas.add("Kolhapur");
			        nashik.farbyAreas.add("Nagpur");
			        nashik.farbyAreas.add("Pune");
			        nashik.farbyAreas.add("Mumbai");
			        nashik.shoppingCenters.add("City Centre Mall");
			        nashik.shoppingCenters.add("Westside");
			        nashik.shelters.add("some random address");
			        placeMap.put("nashik", nashik);
			        
			      //Adding data for nagpur
			        PlaceInfo nagpur = new PlaceInfo();
			        nagpur.nearbyAreas.add("Koradi");
			        nagpur.nearbyAreas.add("Surabardi");
			        nagpur.nearbyAreas.add("Hingna");
			        nagpur.nearbyAreas.add("Jamtha");
			        nagpur.farbyAreas.add("Kolhapur");
			        nagpur.farbyAreas.add("Wardha");
			        nagpur.farbyAreas.add("Chhindwara");
			        nagpur.farbyAreas.add("Mumbai");
			        nagpur.shoppingCenters.add("City Centre Mall");
			        nagpur.shoppingCenters.add("Westside");
			        nagpur.shelters.add("some random address");
			        placeMap.put("nagpur", nagpur);
			        
			      //Adding data for chandrapur
			        PlaceInfo chandrapur = new PlaceInfo();
			        chandrapur.nearbyAreas.add("Lohara");
			        chandrapur.nearbyAreas.add("Kondumal");
			        chandrapur.nearbyAreas.add("Kosara");
			        chandrapur.nearbyAreas.add("Dewada");
			        chandrapur.farbyAreas.add("Ballarpur");
			        chandrapur.farbyAreas.add("Junona");
			        chandrapur.farbyAreas.add("Padmapur");
			        chandrapur.farbyAreas.add("Tadali");
			        chandrapur.shoppingCenters.add("City Centre Mall");
			        chandrapur.shoppingCenters.add("Westside");
			        chandrapur.shelters.add("some random address");
			        placeMap.put("chandrapur", chandrapur);
			        
			        
			       //Adding data for thane
			        PlaceInfo thane = new PlaceInfo();
			        thane.nearbyAreas.add("Mumbai");
			        thane.nearbyAreas.add("Bhiwandi");
			        thane.nearbyAreas.add("Kalyan");
			        thane.nearbyAreas.add("Navi Mumbai");
			        thane.farbyAreas.add("Matheran");
			        thane.farbyAreas.add("Karjat");
			        thane.farbyAreas.add("Padgha");
			        thane.farbyAreas.add("Virar");
			        thane.shoppingCenters.add("City Centre Mall");
			        thane.shoppingCenters.add("Westside");
			        thane.shelters.add("some random address");
			        placeMap.put("thane", thane);
			       
			        
			       //Adding data for raigad
			        PlaceInfo raigad = new PlaceInfo();
			        raigad.nearbyAreas.add("HirkaniWadi");
			        raigad.nearbyAreas.add("Konzar");
			        raigad.nearbyAreas.add("Walsure");
			        raigad.nearbyAreas.add("Chhatri Nijampur");
			        raigad.farbyAreas.add("Sandoshi");
			        raigad.farbyAreas.add("Dapoli");
			        raigad.farbyAreas.add("Nandgaon");
			        raigad.farbyAreas.add("Koturde");
			        raigad.shoppingCenters.add("City Centre Mall");
			        raigad.shoppingCenters.add("Westside");
			        raigad.shelters.add("some random address");
			        placeMap.put("raigad", raigad);
			        
			      
			       //Adding data for wayanad
			        PlaceInfo wayanad = new PlaceInfo();
			        wayanad.nearbyAreas.add("Varadoor");
			        wayanad.nearbyAreas.add("Kaniyambetta");
			        wayanad.nearbyAreas.add("Kambalakkad");
			        wayanad.nearbyAreas.add("Cheriyamkolly");
			        wayanad.farbyAreas.add("Kalpetta");
			        wayanad.farbyAreas.add("Kenichira");
			        wayanad.farbyAreas.add("Panamaram");
			        wayanad.farbyAreas.add("Vellamunda");
			        wayanad.shoppingCenters.add("City Centre Mall");
			        wayanad.shoppingCenters.add("Westside");
			        wayanad.shelters.add("some random address");
			        placeMap.put("wayanad", wayanad);
			   
			      
			      // Adding data for "ratnagiri"
			        PlaceInfo ratnagiri= new PlaceInfo();
			        ratnagiri.nearbyAreas.add("Bhoke");
			        ratnagiri.nearbyAreas.add("Khedashi");
			        ratnagiri.nearbyAreas.add("Hathkhamba");
			        ratnagiri.nearbyAreas.add("Niwali");
			        ratnagiri.farbyAreas.add("Pali");
			        ratnagiri.farbyAreas.add("Karabude");
			        ratnagiri.farbyAreas.add("Ranpat");
			        ratnagiri.farbyAreas.add("Narsigne");
			        ratnagiri.shoppingCenters.add("DMart");
			        ratnagiri.shelters.add("Maher Aashram NGO");
			        placeMap.put("ratnagiri", ratnagiri);

			     // Adding data for "sangli"
			        PlaceInfo sangli= new PlaceInfo();
			        sangli.nearbyAreas.add("Miraj");
			        sangli.nearbyAreas.add("Budhgaon");
			        sangli.nearbyAreas.add("Haripur");
			        sangli.nearbyAreas.add("Bolwad");
			        sangli.farbyAreas.add("Malgaon");
			        sangli.farbyAreas.add("Tasgaon");
			        sangli.farbyAreas.add("Manejuri");
			        sangli.farbyAreas.add("Mhaisal");
			        sangli.shoppingCenters.add("DYP City mall");
			        sangli.shelters.add("Aayush Sevabhavi Sanshta ");
			        placeMap.put("sangli", sangli);
			        

					//palghar
					PlaceInfo palghar = new PlaceInfo();
			        palghar.nearbyAreas.add("Kamare");
			        palghar.nearbyAreas.add("Haranwali");
			        palghar.nearbyAreas.add("Shelwadi");
			        palghar.nearbyAreas.add("Umroli");
			        palghar.farbyAreas.add("Manor");
			        palghar.farbyAreas.add("Boisar");
			        palghar.farbyAreas.add("Virar");
			        palghar.farbyAreas.add("thane");
			        palghar.shoppingCenters.add("Tania Horizon Mall");
			        palghar.shelters.add(" Mhada's Disaster Management Cell");
					palghar.shelters.add(" Shashwat Utkranti Pratishthan: Located in Kanhor Gaon, Kanhor");
					palghar.shelters.add(" Nirdhar Pratishthan: Located in Tokare. ");
					palghar.shelters.add(" World Connect Development Foundation ");
			        placeMap.put("palghar",palghar);

					//satara
					PlaceInfo satara = new PlaceInfo();
			        satara.nearbyAreas.add("Shendre");
			        satara.nearbyAreas.add("Degaon");
			        satara.nearbyAreas.add("Shahupuri");
			        satara.nearbyAreas.add("Wadhe");
			        satara.farbyAreas.add("Nagthane");
			        satara.farbyAreas.add("Kashil");
			        satara.farbyAreas.add("Rajmachi");
			        satara.farbyAreas.add("Jejuri");
			        satara.shoppingCenters.add(" SGS Mall");
			        satara.shelters.add("Shree Brahmachaitanya Gondwalekar Maharaj Institute");
					satara.shelters.add("Mann Deshi Foundation");
					satara.shelters.add("Shri Samarth Seva Mandal");
					satara.shelters.add("Ehsaas Matimand Mulanche Balgruh");
			        placeMap.put("satara",satara);
			        

	        if (tod == 1 || tod == 4) {
	            for (Map.Entry<String, PlaceInfo> entry : placeMap.entrySet()) {
	                String placeName = entry.getKey();
	                if (!affectedArea.contains(placeName)) continue;

	                PlaceInfo info = entry.getValue();
	                System.out.println("\nPlace: " + entry.getKey());
	                System.out.println("\n  Nearby Areas: " + info.nearbyAreas + "\n");
	                System.out.println("  Shopping Centers: " + info.shoppingCenters + "\n");
	                System.out.println("  Shelters: " + info.shelters + "\n");
	            }
	        } else {
	            for (Map.Entry<String, PlaceInfo> entry : placeMap.entrySet()) {
	                String placeName = entry.getKey();
	                if (!affectedArea.contains(placeName)) continue;

	                PlaceInfo info = entry.getValue();
	                System.out.println("\nPlace: " + entry.getKey());
	                System.out.println("\n  Farby Areas: " + info.farbyAreas + "\n");
	                System.out.println("  Shelters: " + info.shelters + "\n");
	            }
	        }

	        // Shortest Path Logic
	        System.out.println("\nDo you want to find the safest/shortest path to deliver aid?");
	        System.out.println("Enter source location:");
	        String source = scanner.next().toLowerCase();
	        System.out.println("Enter destination location:");
	        String dest = scanner.next().toLowerCase();

	        List<String> path = graph.shortestPath(source, dest);
	        if (!path.isEmpty()) {
	            System.out.println("Safest/Shortest Path: " + path);
	        } else {
	            System.out.println("No path available between " + source + " and " + dest);
	        }
	        
	        System.out.println("Do you need our RESCUE VAN for your HELP??\n1.Yes\n2.No");
	        int choice=scanner.nextInt();
	        if(choice==1)
	        {
	        	vat.main(args);
	        }
	        else
	        {
	        	System.out.println("Thank you for your Support!!\nBE SAFE!!");
	        }
	        
	        System.out.println("\nSome Important Helpline numbers for your convenience:\n\n");
	        System.out.println("\t\tDisaster Management :  \t\t 1077\n\t\tNDRF Distress Helpline :  \t+919711077372");
	        System.out.println("\t\tNDRF Headquarters :  \t\t 011-24363260\n\t\tNDMA Helpline :   \t\t 011-1078");
	        
	        scanner.close();
	    }

}
