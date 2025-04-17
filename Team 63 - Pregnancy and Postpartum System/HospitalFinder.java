import java.io.*;
import java.util.*;

class Hospital {
    String name;
    double latitude;
    double longitude;

    Hospital(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    double distanceTo(double lat, double lon) {
        double dLat = this.latitude - lat;
        double dLon = this.longitude - lon;
        return Math.sqrt(dLat * dLat + dLon * dLon) * 111; // Convert degrees to km
    }
}

public class HospitalFinder {
    public static Map<String, double[]> cityToLatLon = new HashMap<>();

    static {
        cityToLatLon.put("pune", new double[]{18.5204, 73.8567});
        cityToLatLon.put("mumbai", new double[]{19.0760, 72.8777}); 
        cityToLatLon.put("delhi", new double[]{28.7041, 77.1025});
        cityToLatLon.put("bangalore", new double[]{12.9716, 77.5946});
        cityToLatLon.put("hyderabad", new double[]{17.3850, 78.4867});
        cityToLatLon.put("chennai", new double[]{13.0827, 80.2707});
        cityToLatLon.put("kolkata", new double[]{22.5726, 88.3639});
    }

    public static List<Hospital> loadHospitals(String csvFilePath) {
        List<Hospital> hospitals = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (data.length >= 3) {
                    String name = data[0].replaceAll("\"", "").trim();
                    double lat = Double.parseDouble(data[1].replaceAll("\"", "").trim());
                    double lon = Double.parseDouble(data[2].replaceAll("\"", "").trim());
                    if (!seen.contains(name)) {
                        hospitals.add(new Hospital(name, lat, lon));
                        seen.add(name);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hospitals;
    }

    public static List<Hospital> getNearestHospitals(List<Hospital> hospitals, double userLat, double userLon, int count) {
        PriorityQueue<Hospital> pq = new PriorityQueue<>(Comparator.comparingDouble(h -> h.distanceTo(userLat, userLon)));
        pq.addAll(hospitals);

        List<Hospital> nearest = new ArrayList<>();
        for (int i = 0; i < count && !pq.isEmpty(); i++) {
            nearest.add(pq.poll());
        }
        return nearest;
    }
}

// import java.io.*;
// import java.util.*;

// public class HospitalFinder {

//     // Define a class for storing hospital data
//     static class Hospital {
//         String name;
//         double lat;
//         double lon;
//         double safetyScore;

//         public Hospital(String name, double lat, double lon, int safetyScore) {
//             this.name = name;
//             this.lat = lat;
//             this.lon = lon;
//             this.safetyScore = safetyScore;
//         }
//     }

//     // Function to calculate the distance between two points using the Haversine formula
//     public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
//         final int R = 6371; // Radius of the earth in km
//         double latDistance = Math.toRadians(lat2 - lat1);
//         double lonDistance = Math.toRadians(lon2 - lon1);
//         double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
//                 Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                         Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//         double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//         return R * c; // Distance in km
//     }

//     // Function to calculate the weighted score based on distance and safety score
//     public static double calculateWeightedScore(double distance, int safetyScore, double distanceWeight, double safetyWeight) {
//         // Normalize safety score (if needed) to fit within a similar scale as distance (e.g., 0-10 range)
//         double normalizedSafetyScore = safetyScore / 10.0;
//         return (distance * distanceWeight) + (normalizedSafetyScore * safetyWeight);
//     }

//     // Function to read the hospital dataset from a CSV file
//     public static List<Hospital> readHospitalData(String filePath) {
//         List<Hospital> hospitals = new ArrayList<>();
//         try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//             String line;
//             br.readLine(); // Skip header
//             while ((line = br.readLine()) != null) {
//                 String[] values = line.split(",");
//                 try {
//                     String name = values[0]; // Hospital Name
//                     double lat = Double.parseDouble(values[1].trim()); // Latitude
//                     double lon = Double.parseDouble(values[2].trim()); // Longitude
//                     // String city = values[3]; // Optional, not used in logic
//                     int safetyScore = Integer.parseInt(values[4].trim()); // Safety Score
    
//                     hospitals.add(new Hospital(name, lat, lon, safetyScore));
//                 } catch (NumberFormatException e) {
//                     System.out.println("Skipping invalid row: " + line);
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         return hospitals;
//     }
    

//     // Function to find the nearest hospital considering both safety and distance
//     public static Hospital findNearestHospital(double startLat, double startLon, List<Hospital> hospitals, double distanceWeight, double safetyWeight) {
//         PriorityQueue<Hospital> pq = new PriorityQueue<>(Comparator.comparingDouble(h -> calculateWeightedScore(
//                 calculateDistance(startLat, startLon, h.lat, h.lon), h.safetyScore, distanceWeight, safetyWeight)));

//         for (Hospital hospital : hospitals) {
//             pq.add(hospital);
//         }

//         return pq.poll(); // The best hospital will be at the top of the priority queue
//     }

//     public static void main(String[] args) {
//         // Specify the path to your CSV dataset
//         String filePath = "large_hospital_dataset.csv";

//         // Read hospital data from CSV
//         List<Hospital> hospitals = readHospitalData(filePath);

//         // Create a Scanner object to take user input for current location
//         Scanner scanner = new Scanner(System.in);

//         // Input the user's location
//         System.out.print("Enter your current latitude: ");
//         double startLat = scanner.nextDouble();
//         System.out.print("Enter your current longitude: ");
//         double startLon = scanner.nextDouble();

//         // Weights for distance and safety
//         double distanceWeight = 0.7;
//         double safetyWeight = 0.3;

//         // Find the nearest and safest hospital
//         Hospital bestHospital = findNearestHospital(startLat, startLon, hospitals, distanceWeight, safetyWeight);

//         // Output the result
//         if (bestHospital != null) {
//             System.out.println("Best Hospital: " + bestHospital.name);
//             System.out.println("Distance: " + calculateDistance(startLat, startLon, bestHospital.lat, bestHospital.lon) + " km");
//             System.out.println("Safety Score: " + bestHospital.safetyScore);
//         } else {
//             System.out.println("No hospitals found.");
//         }

//         // Close the scanner
//         scanner.close();
//     }
// }
