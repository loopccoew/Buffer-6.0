import java.util.*;
import java.sql.*;

class DBConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_system?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "2468@Sneha";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("[DB Error] MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    public static void savePatientToDB(Patient p, String hospitalId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO patients (name, age, severity, latitude, longitude, treatment_type, assigned_hospital, admission_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.name);
            stmt.setInt(2, p.age);
            stmt.setString(3, p.severity);
            stmt.setDouble(4, p.latitude);
            stmt.setDouble(5, p.longitude);
            stmt.setString(6, p.treatmentType);
            stmt.setString(7, hospitalId);
            stmt.setTimestamp(8, new Timestamp(p.timestamp));
            stmt.executeUpdate();
            decrementHospitalBeds(hospitalId);
            System.out.println("✅ Patient inserted into database successfully.");
        } catch (SQLException e) {
            System.out.println("[DB Error] Failed to insert patient: " + e.getMessage());
        }
    }

    public static void updatePatientDischarge(String patientName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String getHospitalSQL = "SELECT assigned_hospital FROM patients WHERE name = ? AND discharge_time IS NULL";
            PreparedStatement getHospitalStmt = conn.prepareStatement(getHospitalSQL);
            getHospitalStmt.setString(1, patientName);
            ResultSet rs = getHospitalStmt.executeQuery();

            if (rs.next()) {
                String hospitalId = rs.getString("assigned_hospital");

                String sql = "UPDATE patients SET discharge_time = ? WHERE name = ? AND discharge_time IS NULL";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                stmt.setString(2, patientName);
                stmt.executeUpdate();

                incrementHospitalBeds(hospitalId);
                System.out.println("✅ Patient " + patientName + " has been discharged and updated in the DB.");
            } else {
                System.out.println("❌ Patient not found in the database or already discharged.");
            }
        } catch (SQLException e) {
            System.out.println("[DB Error] Failed to update discharge: " + e.getMessage());
        }
    }

    public static void decrementHospitalBeds(String hospitalId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE hospitals SET available_beds = available_beds - 1 WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, hospitalId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[DB Error] Failed to decrement beds: " + e.getMessage());
        }
    }

    public static void incrementHospitalBeds(String hospitalId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "UPDATE hospitals SET available_beds = available_beds + 1 WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, hospitalId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[DB Error] Failed to increment beds: " + e.getMessage());
        }
    }

    public static void loadHospitalsFromDB(Graph graph) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Map<String, Set<String>> hospitalSpecialties = new HashMap<>();
            Map<String, Integer> activePatientCount = new HashMap<>();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM hospitals");

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                int availableBeds = rs.getInt("available_beds");
                hospitalSpecialties.put(id, new HashSet<>());

                Hospital hospital = new Hospital(id, name, lat, lon, availableBeds, new HashSet<>());
                graph.addHospital(hospital);
                hospitalSpecialties.put(id, hospital.specialties);
            }

            // Load specialties
            rs = stmt.executeQuery("SELECT * FROM specialties");
            while (rs.next()) {
                String hospitalId = rs.getString("hospital_id");
                String specialty = rs.getString("specialty_name");
                if (hospitalSpecialties.containsKey(hospitalId)) {
                    hospitalSpecialties.get(hospitalId).add(specialty);
                }
            }

            // Count active patients (not discharged)
            rs = stmt.executeQuery("SELECT assigned_hospital, COUNT(*) AS active_count FROM patients WHERE discharge_time IS NULL GROUP BY assigned_hospital");
            while (rs.next()) {
                activePatientCount.put(rs.getString("assigned_hospital"), rs.getInt("active_count"));
            }

            // Update hospitals in graph based on real-time occupied beds
            for (Map.Entry<String, Hospital> entry : graph.hospitals.entrySet()) {
                String hospitalId = entry.getKey();
                Hospital h = entry.getValue();
                int activePatients = activePatientCount.getOrDefault(hospitalId, 0);
                h.availableBeds -= activePatients;
            }

        } catch (SQLException e) {
            System.out.println("[DB Error] Failed to load hospitals: " + e.getMessage());
        }
    }
}

class Patient {
    String name;
    int age;
    String severity;
    double latitude, longitude;
    long timestamp;
    String assignedHospital;
    String treatmentType;

    public Patient(String name, int age, String severity, double latitude, double longitude, String treatmentType) {
        this.name = name;
        this.age = age;
        this.severity = severity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.treatmentType = treatmentType;
        this.timestamp = System.currentTimeMillis();
    }
}

class Hospital {
    String id;
    String name;
    double latitude, longitude;
    int availableBeds;
    Set<String> specialties;
    List<Patient> admittedPatients = new ArrayList<>();

    public Hospital(String id, String name, double lat, double lon, int beds, Set<String> specialties) {
        this.id = id;
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.availableBeds = beds;
        this.specialties = specialties;
    }
}

class Graph {
    Map<String, Hospital> hospitals = new HashMap<>();

    public void addHospital(Hospital h) {
        hospitals.put(h.id, h);
    }

    public Hospital findNearestHospital(String specialty, double patientLat, double patientLon) {
        return hospitals.values().stream()
                .filter(h -> h.availableBeds > 0 && h.specialties.contains(specialty))
                .min(Comparator.comparingDouble(h -> distance(h.latitude, h.longitude, patientLat, patientLon)))
                .orElse(null);
    }

    public Hospital findAnyNearestHospital(double patientLat, double patientLon) {
        return hospitals.values().stream()
                .min(Comparator.comparingDouble(h -> distance(h.latitude, h.longitude, patientLat, patientLon)))
                .orElse(null);
    }

    public void displayAllHospitalStatus() {
        for (Hospital h : hospitals.values()) {
            System.out.println("\nHospital: " + h.name);
            System.out.println("Available Beds: " + h.availableBeds);
            System.out.println("Total Patients Being Treated: " + h.admittedPatients.size());
            for (Patient p : h.admittedPatients) {
                System.out.println("  - " + p.name + " | Age: " + p.age + " | Severity: " + p.severity
                        + " | Treatment: " + p.treatmentType);
            }
        }
    }

    public void dischargePatient(String hospitalName, String patientName) {
        for (Hospital h : hospitals.values()) {
            if (h.name.equalsIgnoreCase(hospitalName)) {
                Iterator<Patient> iterator = h.admittedPatients.iterator();
                while (iterator.hasNext()) {
                    Patient p = iterator.next();
                    if (p.name.equalsIgnoreCase(patientName)) {
                        iterator.remove();
                        h.availableBeds++;
                        System.out.println("\n✅ " + p.name + " has been discharged from " + h.name);
                        DBConnector.updatePatientDischarge(p.name);
                        return;
                    }
                }
            }
        }
        System.out.println("\nPatient not found in the given hospital.");
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }
}

class BedAllocator {
    Graph hospitalGraph;

    public BedAllocator(Graph g) {
        this.hospitalGraph = g;
    }

    public void assignBed(Patient p) {
        Hospital nearest = hospitalGraph.findNearestHospital(p.treatmentType, p.latitude, p.longitude);
        if (nearest != null) {
            nearest.availableBeds--;
            nearest.admittedPatients.add(p);
            p.assignedHospital = nearest.name;
            System.out.println("\nBed Assigned at: " + nearest.name);
            System.out.println("Remaining beds at " + nearest.name + ": " + nearest.availableBeds);
            DBConnector.savePatientToDB(p, nearest.id);
        } else {
            System.out.println("\nNo hospital with available bed found for specialty: " + p.treatmentType);
        }
    }

    public void handleEmergency(double latitude, double longitude) {
        Hospital nearest = hospitalGraph.findAnyNearestHospital(latitude, longitude);
        if (nearest != null) {
            System.out.println("\nEmergency patient directed to: " + nearest.name);
        } else {
            System.out.println("\nNo hospitals found nearby.");
        }
    }
}

public class HospitalBedAllocation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph();
        DBConnector.loadHospitalsFromDB(graph);
        BedAllocator allocator = new BedAllocator(graph);

        while (true) {
            System.out.println("\nSelect User Type:\n1. Patient\n2. Hospital Admin\n3. Emergency Case\n4. Exit");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.println("\nEnter Patient Details:");
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Age: ");
                int age = Integer.parseInt(scanner.nextLine());
                System.out.print("Severity (Critical/Serious/Mild): ");
                String severity = scanner.nextLine();
                System.out.print("Latitude: ");
                double lat = Double.parseDouble(scanner.nextLine());
                System.out.print("Longitude: ");
                double lon = Double.parseDouble(scanner.nextLine());
                System.out.print("Required Specialty: ");
                String specialty = scanner.nextLine();

                Patient p = new Patient(name, age, severity, lat, lon, specialty);
                allocator.assignBed(p);

            } else if (choice == 2) {
                graph.displayAllHospitalStatus();
                System.out.print("\nDo you want to discharge a patient? (yes/no): ");
                String ans = scanner.nextLine();
                if (ans.equalsIgnoreCase("yes")) {
                    System.out.print("Enter hospital name: ");
                    String hospitalName = scanner.nextLine();
                    System.out.print("Enter patient name to discharge: ");
                    String patientName = scanner.nextLine();
                    graph.dischargePatient(hospitalName, patientName);
                }

            } else if (choice == 3) {
                System.out.print("\nEnter emergency location - Latitude: ");
                double lat = Double.parseDouble(scanner.nextLine());
                System.out.print("Longitude: ");
                double lon = Double.parseDouble(scanner.nextLine());
                allocator.handleEmergency(lat, lon);

            } else if (choice == 4) {
                System.out.println("\nThank you for using the Hospital Bed Allocation System.");
                break;

            } else {
                System.out.println("\nInvalid selection. Please try again.");
            }
        }

        scanner.close();
    }
}