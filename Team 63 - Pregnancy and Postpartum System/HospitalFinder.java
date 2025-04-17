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
        return Math.sqrt(dLat * dLat + dLon * dLon) * 111; 
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
            br.readLine(); 
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

