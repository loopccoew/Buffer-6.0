import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ParkingSystem parkingSystem = new ParkingSystem();
        Scanner scanner = new Scanner(System.in);

        // Add 25 locations in Pune
        Location[] locations = {
            new Location("L1", "Phoenix Market City", "Viman Nagar, Pune", 200, 50.0, true),
            new Location("L2", "Amanora Mall", "Hadapsar, Pune", 150, 45.0, true),
            new Location("L3", "Koregaon Park Plaza", "Koregaon Park, Pune", 100, 60.0, true),
            new Location("L4", "Seasons Mall", "Magarpatta, Pune", 180, 55.0, true),
            new Location("L5", "Central Mall", "Kharadi, Pune", 120, 40.0, false),
            new Location("L6", "Inorbit Mall", "Wakad, Pune", 160, 45.0, true),
            new Location("L7", "Westend Mall", "Aundh, Pune", 90, 50.0, false),
            new Location("L8", "City Pride", "Kothrud, Pune", 110, 35.0, false),
            new Location("L9", "SGS Mall", "Swargate, Pune", 80, 30.0, false),
            new Location("L10", "Pune Central", "Kalyani Nagar, Pune", 130, 55.0, true),
            new Location("L11", "Pune Station", "Pune Railway Station, Pune", 250, 40.0, true),
            new Location("L12", "Airport Parking", "Pune International Airport", 300, 60.0, true),
            new Location("L13", "Shivaji Nagar", "Shivaji Nagar, Pune", 140, 35.0, false),
            new Location("L14", "Deccan Gymkhana", "Deccan, Pune", 100, 45.0, false),
            new Location("L15", "FC Road", "Fergusson College Road, Pune", 120, 40.0, false),
            new Location("L16", "JM Road", "Jangli Maharaj Road, Pune", 150, 45.0, false),
            new Location("L17", "Baner", "Baner, Pune", 180, 50.0, true),
            new Location("L18", "Hinjewadi", "Hinjewadi Phase 1, Pune", 200, 55.0, true),
            new Location("L19", "Wakad", "Wakad, Pune", 160, 45.0, true),
            new Location("L20", "Aundh", "Aundh, Pune", 140, 50.0, true),
            new Location("L21", "Kalyani Nagar", "Kalyani Nagar, Pune", 120, 55.0, false),
            new Location("L22", "Kharadi", "Kharadi, Pune", 170, 45.0, true),
            new Location("L23", "Viman Nagar", "Viman Nagar, Pune", 190, 50.0, true),
            new Location("L24", "Magarpatta", "Magarpatta, Pune", 180, 55.0, true),
            new Location("L25", "Hadapsar", "Hadapsar, Pune", 160, 40.0, false)
        };

        // Add all locations to the system
        for (Location location : locations) {
            parkingSystem.addLocation(location);
        }

        // Add connections between nearby locations with realistic distances (in kilometers)
        addLocationConnections(parkingSystem);

        while (true) {
            try {
                System.out.println("\nSmart Parking Management System");
                System.out.println("1. Login as Admin");
                System.out.println("2. Login as User");
                System.out.println("3. Register as User");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                String input = scanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
                    continue;
                }

                switch (choice) {
                    case 1:
                        adminLogin(parkingSystem, scanner);
                        break;
                    case 2:
                        userLogin(parkingSystem, scanner);
                        break;
                    case 3:
                        registerUser(parkingSystem, scanner);
                        break;
                    case 4:
                        System.out.println("Thank you for using Smart Parking Management System!");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
    }

    private static void adminLogin(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (parkingSystem.adminLogin(username, password)) {
            adminMenu(parkingSystem, scanner);
        } else {
            System.out.println("Invalid admin credentials!");
        }
    }

    private static void adminMenu(ParkingSystem parkingSystem, Scanner scanner) {
        while (true) {
            System.out.println("\n+============================================================================+");
            System.out.println("|                           ADMIN MENU                                        |");
            System.out.println("+============================================================================+");
            System.out.println("| 1. View All Locations                                                      |");
            System.out.println("| 2. Add New Location                                                        |");
            System.out.println("| 3. View All Users                                                          |");
            System.out.println("| 4. View Analytics                                                          |");
            System.out.println("| 5. View All Messages                                                       |");
            System.out.println("| 6. Reply to User Message                                                   |");
            System.out.println("| 7. View Feedbacks & Ratings                                               |");
            System.out.println("| 8. Revenue Management                                                      |");
            System.out.println("| 9. Logout                                                                  |");
            System.out.println("+============================================================================+");
            
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    viewAllLocations(parkingSystem);
                    break;
                case 2:
                    addNewLocation(parkingSystem, scanner);
                    break;
                case 3:
                    viewAllUsers(parkingSystem, scanner);
                    break;
                case 4:
                    viewAnalytics(parkingSystem);
                    break;
                case 5:
                    viewAllMessages(parkingSystem);
                    break;
                case 6:
                    replyToUserMessage(parkingSystem, scanner);
                    break;
                case 7:
                    viewFeedbacks(parkingSystem);
                    break;
                case 8:
                    revenueManagement(parkingSystem, scanner);
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewAllLocations(ParkingSystem parkingSystem) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("║                                   All Locations                                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Location Name        │ Address                    │ Slots  │ Price/Hour │ Rating │ EV Charging ║");
        System.out.println("╠══════════════════════╪════════════════════════════╪════════╪════════════╪════════╪═════════════╣");
        
        for (Location location : parkingSystem.getAllLocations()) {
            System.out.println(location.toTableRow());
        }
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════╝");
    }

    private static void addNewLocation(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.println("\n+============================================================================+");
        System.out.println("|                           ADD NEW LOCATION                                    |");
        System.out.println("+============================================================================+");
        
        System.out.print("Enter location ID: ");
        String locationId = scanner.nextLine();
        System.out.print("Enter location name: ");
        String name = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter total slots: ");
        int totalSlots = scanner.nextInt();
        System.out.print("Enter price per hour: ");
        double pricePerHour = scanner.nextDouble();
        System.out.print("Has EV charging? (true/false): ");
        boolean hasEVCharging = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline
        
        Location location = new Location(locationId, name, address, totalSlots, pricePerHour, hasEVCharging);
        parkingSystem.addLocation(location);
        System.out.println("Location added successfully!");
    }

    private static void viewAllUsers(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.println("\nAll Users:");
        for (User user : parkingSystem.getAllUsers()) {
            System.out.println(user);
            System.out.println("------------------------");
        }

        System.out.println("\nUser Management:");
        System.out.println("1. Block User");
        System.out.println("2. Unblock User");
        System.out.println("3. Delete User");
        System.out.println("4. Message User");
        System.out.println("5. Back");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice >= 1 && choice <= 4) {
            System.out.print("Enter user ID: ");
            String userId = scanner.nextLine();
            
            switch (choice) {
                case 1:
                    parkingSystem.blockUser(userId);
                    System.out.println("User blocked successfully!");
                    break;
                case 2:
                    parkingSystem.unblockUser(userId);
                    System.out.println("User unblocked successfully!");
                    break;
                case 3:
                    parkingSystem.deleteUser(userId);
                    System.out.println("User deleted successfully!");
                    break;
                case 4:
                    System.out.print("Enter message: ");
                    String message = scanner.nextLine();
                    parkingSystem.messageUser(userId, message);
                    System.out.println("Message sent successfully!");
                    break;
            }
        }
    }

    private static void viewFeedbacks(ParkingSystem parkingSystem) {
        System.out.println("\n+============================================================================+");
        System.out.println("|                           LOCATION FEEDBACKS                                  |");
        System.out.println("+============================================================================+");
        
        for (Location location : parkingSystem.getAllLocations()) {
            System.out.println("\n+------------------------------------------------------------------------+");
            System.out.printf("| Location: %-65s |\n", location.getName());
            System.out.printf("| Average Rating: %-63s |\n", location.getRatingStars());
            System.out.println("+------------------------------------------------------------------------+");
            
            List<Feedback> feedbacks = parkingSystem.getLocationFeedbacks(location.getLocationId());
            if (feedbacks.isEmpty()) {
                System.out.println("| No feedbacks available for this location.                              |");
            } else {
                System.out.println("| User ID | Rating | Comment                                           |");
                System.out.println("|---------|--------|---------------------------------------------------|");
                for (Feedback feedback : feedbacks) {
                    System.out.printf("| %-7s | %-6.1f | %-49s |\n",
                        feedback.getUserId(),
                        feedback.getRating(),
                        feedback.getComment());
                }
            }
            System.out.println("+------------------------------------------------------------------------+");
        }
        System.out.println("+============================================================================+");
    }

    private static void revenueManagement(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.println("\n+============================================================================+");
        System.out.println("|                           REVENUE MANAGEMENT                                  |");
        System.out.println("+============================================================================+");
        
        double totalRevenue = parkingSystem.getTotalRevenue();
        System.out.printf("| Total Revenue: Rs%.2f%55s |\n", totalRevenue, "");
        
        Map<String, Double> locationRevenue = parkingSystem.getLocationRevenue();
        List<Map.Entry<String, Double>> sortedRevenue = new ArrayList<>(locationRevenue.entrySet());
        sortedRevenue.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        System.out.println("\n+------------------------------------------------------------------------+");
        System.out.println("|                           HIGH REVENUE LOCATIONS                           |");
        System.out.println("+------------------------------------------------------------------------+");
        System.out.println("| Location Name        | Revenue (Rs)                                      |");
        System.out.println("+----------------------+---------------------------------------------------+");
        
        for (int i = 0; i < Math.min(3, sortedRevenue.size()); i++) {
            Location location = parkingSystem.getLocationGraph().getLocation(sortedRevenue.get(i).getKey());
            System.out.printf("| %-20s | %-47.2f |\n", 
                location.getName(), 
                sortedRevenue.get(i).getValue());
        }
        
        System.out.println("\n+------------------------------------------------------------------------+");
        System.out.println("|                           LOW REVENUE LOCATIONS                            |");
        System.out.println("+------------------------------------------------------------------------+");
        System.out.println("| Location Name        | Revenue (Rs)                                      |");
        System.out.println("+----------------------+---------------------------------------------------+");
        
        for (int i = sortedRevenue.size() - 1; i >= Math.max(0, sortedRevenue.size() - 3); i--) {
            Location location = parkingSystem.getLocationGraph().getLocation(sortedRevenue.get(i).getKey());
            System.out.printf("| %-20s | %-47.2f |\n", 
                location.getName(), 
                sortedRevenue.get(i).getValue());
        }
        
        System.out.println("\n+------------------------------------------------------------------------+");
        System.out.print("| Enter location name to search revenue (or 'back' to return): ");
        String locationName = scanner.nextLine();
        
        if (!locationName.equalsIgnoreCase("back")) {
            Location location = parkingSystem.findLocationByName(locationName);
            if (location != null) {
                double revenue = locationRevenue.getOrDefault(location.getLocationId(), 0.0);
                System.out.printf("| Revenue for %s: Rs%.2f%40s |\n", 
                    location.getName(), revenue, "");
            } else {
                System.out.println("| Location not found!                                                    |");
            }
        }
        System.out.println("+============================================================================+");
    }

    private static void viewAnalytics(ParkingSystem parkingSystem) {
        System.out.println("\n+============================================================================+");
        System.out.println("|                                Analytics                                    |");
        System.out.println("+============================================================================+");
        
        Map<String, Integer> analytics = parkingSystem.getAnalytics();
        System.out.println("| Total Parking Areas: " + analytics.get("totalAreas") + "                                                  |");
        System.out.println("| Total Slots: " + analytics.get("totalSlots") + "                                                          |");
        System.out.println("| Total Registered Users: " + analytics.get("totalUsers") + "                                              |");
        
        System.out.println("\n+============================================================================+");
        System.out.println("| Location Name         | Occupied | Available | Total |");
        System.out.println("+----------------------+----------+-----------+-------+");
        
        for (Location location : parkingSystem.getAllLocations()) {
            int occupied = location.getTotalSlots() - location.getAvailableSlots();
            System.out.printf("| %-20s | %-8d | %-9d | %-5d |\n",
                location.getName(), occupied, location.getAvailableSlots(), location.getTotalSlots());
        }
        System.out.println("+============================================================================+");
        
        // Show today's bookings
        int todayBookings = 0;
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        for (Location location : parkingSystem.getAllLocations()) {
            for (Booking booking : parkingSystem.getLocationBookings(location.getLocationId())) {
                if (booking.getStartTime().isAfter(today)) {
                    todayBookings++;
                }
            }
        }
        System.out.println("| Total Bookings Today: " + todayBookings + "                                                      |");
        System.out.println("+============================================================================+");
    }

    private static void userLogin(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = parkingSystem.loginUser(userId, password);
        if (user != null) {
            userMenu(parkingSystem, user, scanner);
        } else {
            System.out.println("Invalid credentials or user is blocked!");
        }
    }

    private static void userMenu(ParkingSystem parkingSystem, User user, Scanner scanner) {
        while (true) {
            System.out.println("\n+============================================================================+");
            System.out.println("|                           USER MENU                                         |");
            System.out.println("+============================================================================+");
            System.out.println("| 1. View Locations                                                           |");
            System.out.println("| 2. Search Location                                                          |");
            System.out.println("| 3. Book Parking Slot                                                        |");
            System.out.println("| 4. Cancel Booking                                                           |");
            System.out.println("| 5. Rate Location                                                            |");
            System.out.println("| 6. Find EV Charging Station                                                 |");
            System.out.println("| 7. View Messages                                                            |");
            System.out.println("| 8. Booking Summary                                                          |");
            System.out.println("| 9. Logout                                                                   |");
            System.out.println("+=============================================================================+");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        viewLocations(parkingSystem);
                        break;
                    case 2:
                        searchLocation(parkingSystem, scanner);
                        break;
                    case 3:
                        bookSlot(parkingSystem, user, scanner);
                        break;
                    case 4:
                        cancelBooking(parkingSystem, user, scanner);
                        break;
                    case 5:
                        rateLocation(parkingSystem, user, scanner);
                        break;
                    case 6:
                        findEVChargingStation(parkingSystem, scanner);
                        break;
                    case 7:
                        viewMessages(user, scanner);
                        break;
                    case 8:
                        viewBookingSummary(parkingSystem, user);
                        break;
                    case 9:
                        System.out.println("Logging out... Thank you for using our service! 👋");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void viewLocations(ParkingSystem parkingSystem) {
        System.out.println("\n+==================================================================================================+");
        System.out.println("|                                       AVAILABLE LOCATIONS                                          |");
        System.out.println("+====================================================================================================+");
        System.out.println("| Location Name        | Address                    | Slots  | Price/Hour | Rating  | EV Charging |");
        System.out.println("+----------------------+----------------------------+--------+------------+---------+-------------+");
        
        for (Location location : parkingSystem.getAllLocations()) {
            System.out.println(location.toTableRow());
        }
        System.out.println("+====================================================================================================+");
    }

    private static void searchLocation(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                              Search Locations                                   ");
        System.out.println("═════════════════════════════════════════════════════════════════════════════════");
        
        // First show available locations
        System.out.println("\nAvailable Locations:");
        for (Location location : parkingSystem.getAllLocations()) {
            System.out.println("- " + location.getName());
        }
        
        System.out.print("\nEnter source location name: ");
        String sourceName = scanner.nextLine();
        System.out.print("Enter number of nearest locations to find: ");
        int count = scanner.nextInt();

        try {
            List<Location> nearestLocations = parkingSystem.searchLocations(sourceName, count);
            System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
            System.out.println("                        Nearest Locations                                        ");
            System.out.println("═════════════════════════════════════════════════════════════════════════════════");
            System.out.println("| Location Name        | Distance  | Price/Hour | Available Slots | Rating  |");
            System.out.println("|----------------------|-----------|------------|-----------------|---------|");
            
            for (Location location : nearestLocations) {
                double distance = parkingSystem.getLocationGraph().getDistance(sourceName, location.getName());
                System.out.printf("| %-20s | %6.1f km | Rs%8.2f | %d/%d           | %s |\n",
                    location.getName(), 
                    distance,
                    location.getPricePerHour(),
                    location.getAvailableSlots(), 
                    location.getTotalSlots(),
                    location.getRatingStars());
            }
            System.out.println("═════════════════════════════════════════════════════════════════════════════════");
        } catch (IllegalArgumentException e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private static void bookSlot(ParkingSystem parkingSystem, User user, Scanner scanner) {
        try {
            System.out.println("\n+======================================================================+");
            System.out.println("|                         BOOK PARKING SLOT                            |");
            System.out.println("+======================================================================+");
            
            // First show main locations
            System.out.println("\nAvailable Locations:");
            for (Location location : parkingSystem.getAllLocations()) {
                System.out.println("- " + location.getName() + " (ID: " + location.getLocationId() + ")");
            }
            
            System.out.print("\nEnter location ID: ");
            String locationId = scanner.nextLine();
            
            Location location = parkingSystem.getLocationGraph().getLocation(locationId);
            if (location == null) {
                System.out.println("Invalid location ID!");
                return;
            }

            System.out.println("\nAvailable slots: " + location.getAvailableSlots() + "/" + location.getTotalSlots());
            System.out.print("Enter slot number: ");
            int slotNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.println("\nEnter parking start time (Format: yyyy-MM-dd HH:mm)");
            System.out.println("Example: 2024-04-16 14:30");
            System.out.print("Start time: ");
            String startTimeStr = scanner.nextLine();
            
            System.out.println("\nEnter parking end time (Format: yyyy-MM-dd HH:mm)");
            System.out.println("Example: 2024-04-16 16:30");
            System.out.print("End time: ");
            String endTimeStr = scanner.nextLine();

            try {
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                if (startTime.isAfter(endTime)) {
                    System.out.println("Error: Start time must be before end time!");
                    return;
                }

                if (startTime.isBefore(LocalDateTime.now())) {
                    System.out.println("Error: Start time cannot be in the past!");
                    return;
                }

                // Calculate total cost
                double hours = java.time.Duration.between(startTime, endTime).toHours();
                double totalCost = hours * location.getPricePerHour();

                System.out.println("\n+======================================================================+");
                System.out.println("|                         PAYMENT DETAILS                                |");
                System.out.println("+========================================================================+");
                System.out.println("Duration: " + hours + " hours");
                System.out.println("Rate: Rs" + location.getPricePerHour() + "/hour");
                System.out.println("Total Cost: Rs" + totalCost);
                System.out.println("+========================================================================+");

                System.out.print("\nProceed with payment? (yes/no): ");
                String proceed = scanner.nextLine().toLowerCase();
                
                if (!proceed.equals("yes")) {
                    System.out.println("Booking cancelled!");
                    return;
                }

                // Simulate payment process
                System.out.println("\nProcessing payment...");
                Thread.sleep(2000); // Simulate payment processing
                System.out.println("Payment successful!");

                Booking booking = parkingSystem.bookSlot(user.getUserId(), locationId, slotNumber, startTime, endTime);
                if (booking != null) {
                    System.out.println("\n+======================================================================+");
                    System.out.println("|                         BOOKING SUCCESSFUL                           |");
                    System.out.println("+======================================================================+");
                    System.out.println("Booking ID: " + booking.getBookingId());
                    System.out.println("Location: " + location.getName());
                    System.out.println("Slot Number: " + slotNumber);
                    System.out.println("Start Time: " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    System.out.println("End Time: " + endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    System.out.println("Total Cost: Rs" + booking.getTotalCost());
                    System.out.println("+======================================================================+");
                } else {
                    System.out.println("Booking failed! Please check slot availability.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date/time format. Please use the format: yyyy-MM-dd HH:mm");
            } catch (InterruptedException e) {
                System.out.println("Error during payment processing!");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void cancelBooking(ParkingSystem parkingSystem, User user, Scanner scanner) {
        System.out.println("\nYour Active Bookings:");
        List<Booking> userBookings = parkingSystem.getUserBookings(user.getUserId());
        for (Booking booking : userBookings) {
            if (booking.isActive()) {
                System.out.println(booking);
                System.out.println("------------------------");
            }
        }

        System.out.print("Enter booking ID to cancel: ");
        String bookingId = scanner.nextLine();

        if (parkingSystem.cancelBooking(bookingId)) {
            System.out.println("Booking cancelled successfully!");
        } else {
            System.out.println("Booking not found or already cancelled!");
        }
    }

    private static void rateLocation(ParkingSystem parkingSystem, User user, Scanner scanner) {
        try {
            System.out.print("Enter location ID: ");
            String locationId = scanner.nextLine();
            System.out.print("Enter rating (1-5): ");
            String ratingInput = scanner.nextLine();
            double rating;
            try {
                rating = Double.parseDouble(ratingInput);
                if (rating < 1 || rating > 5) {
                    System.out.println(" Rating must be between 1 and 5!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid rating number!");
                return;
            }
            System.out.print("Enter feedback: ");
            String comment = scanner.nextLine();

            parkingSystem.addFeedback(user.getUserId(), locationId, rating, comment);
            System.out.println("Feedback submitted successfully!");
        } catch (Exception e) {
            System.out.println(" An error occurred: " + e.getMessage());
        }
    }

    private static void findEVChargingStation(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.print("Enter your current location ID: ");
        String sourceId = scanner.nextLine();

        List<Location> evStations = parkingSystem.findEVChargingStations(sourceId);
        System.out.println("\nNearest EV Charging Stations:");
        for (Location station : evStations) {
            System.out.println(station);
            System.out.println("------------------------");
        }
    }

    private static void viewMessages(User user, Scanner scanner) {
        try {
            System.out.println("\n════════════════════════════════════════════════════════════════");
            System.out.println("                      Your Messages                              ");
            System.out.println("════════════════════════════════════════════════════════════════");
            
            List<String> messages = user.getMessages();
            if (messages.isEmpty()) {
                System.out.println("No messages found.");
            } else {
                for (int i = 0; i < messages.size(); i++) {
                    System.out.println("\nMessage " + (i + 1) + ":");
                    System.out.println(messages.get(i));
                    System.out.println("------------------------");
                }
            }

            System.out.print("\nWould you like to reply to any message? (yes/no): ");
            String reply = scanner.nextLine().toLowerCase();
            
            if (reply.equals("yes")) {
                System.out.print("Enter message number to reply to: ");
                String messageNumInput = scanner.nextLine();
                int messageNum;
                try {
                    messageNum = Integer.parseInt(messageNumInput);
                } catch (NumberFormatException e) {
                    System.out.println(" Please enter a valid message number!");
                    return;
                }
                
                if (messageNum > 0 && messageNum <= messages.size()) {
                    System.out.print("Enter your reply: ");
                    String replyText = scanner.nextLine();
                    user.addMessage("Your reply to message " + messageNum + ": " + replyText);
                    System.out.println(" Reply sent successfully!");
                } else {
                    System.out.println(" Invalid message number!");
                }
            }
        } catch (Exception e) {
            System.out.println(" An error occurred: " + e.getMessage());
        }
    }

    private static void viewBookingSummary(ParkingSystem parkingSystem, User user) {
        System.out.println("\n+===============================================================================+");
        System.out.println("|                           BOOKING SUMMARY                                       |");
        System.out.println("+=================================================================================+");
        
        List<Booking> userBookings = parkingSystem.getUserBookings(user.getUserId());
        if (userBookings.isEmpty()) {
            System.out.println("|                           No bookings found.                               |");
        } else {
            System.out.println("| Booking ID | Location | Slot | Start Time | End Time | Status | Cost  |");
            System.out.println("+------------+----------+------+------------+----------+--------+-------+");
            
            for (Booking booking : userBookings) {
                Location location = parkingSystem.getLocationGraph().getLocation(booking.getLocationId());
                System.out.printf("| %-10s | %-8s | %-4d | %-10s | %-8s | %-6s | Rs%-4.2f |\n",
                    booking.getBookingId(),
                    location.getName(),
                    booking.getSlotNumber(),
                    booking.getStartTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")),
                    booking.getEndTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")),
                    booking.isActive() ? "Active" : "Cancelled",
                    booking.getTotalCost());
            }
        }
        System.out.println("+===============================================================================+");
    }

    private static void registerUser(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (parkingSystem.registerUser(userId, username, password)) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("User ID already exists! Please try again.");
        }
    }

    private static void addLocationConnections(ParkingSystem parkingSystem) {
        // Central Pune connections
        parkingSystem.addLocationConnection("L13", "L14", 2.0); // Shivaji Nagar to Deccan
        parkingSystem.addLocationConnection("L14", "L15", 1.5); // Deccan to FC Road
        parkingSystem.addLocationConnection("L15", "L16", 1.0); // FC Road to JM Road
        parkingSystem.addLocationConnection("L16", "L3", 2.5);  // JM Road to Koregaon Park
        parkingSystem.addLocationConnection("L3", "L10", 3.0);  // Koregaon Park to Pune Central

        // East Pune connections
        parkingSystem.addLocationConnection("L1", "L23", 1.0);  // Phoenix to Viman Nagar
        parkingSystem.addLocationConnection("L23", "L22", 3.0); // Viman Nagar to Kharadi
        parkingSystem.addLocationConnection("L22", "L5", 2.0);  // Kharadi to Central Mall
        parkingSystem.addLocationConnection("L5", "L24", 2.5);  // Central Mall to Magarpatta
        parkingSystem.addLocationConnection("L24", "L4", 1.0);  // Magarpatta to Seasons Mall

        // West Pune connections
        parkingSystem.addLocationConnection("L17", "L18", 4.0); // Baner to Hinjewadi
        parkingSystem.addLocationConnection("L18", "L19", 2.0); // Hinjewadi to Wakad
        parkingSystem.addLocationConnection("L19", "L6", 1.5);  // Wakad to Inorbit Mall
        parkingSystem.addLocationConnection("L6", "L20", 3.0);  // Inorbit Mall to Aundh
        parkingSystem.addLocationConnection("L20", "L7", 2.0);  // Aundh to Westend Mall

        // North-South connections
        parkingSystem.addLocationConnection("L11", "L13", 3.0); // Station to Shivaji Nagar
        parkingSystem.addLocationConnection("L12", "L1", 5.0);  // Airport to Phoenix
        parkingSystem.addLocationConnection("L8", "L9", 2.5);   // City Pride to SGS Mall
        parkingSystem.addLocationConnection("L9", "L11", 2.0);  // SGS Mall to Station
        parkingSystem.addLocationConnection("L21", "L10", 1.5); // Kalyani Nagar to Pune Central

        // Cross connections
        parkingSystem.addLocationConnection("L2", "L25", 1.0);  // Amanora to Hadapsar
        parkingSystem.addLocationConnection("L25", "L24", 3.0); // Hadapsar to Magarpatta
        parkingSystem.addLocationConnection("L4", "L2", 2.5);   // Seasons Mall to Amanora
        parkingSystem.addLocationConnection("L17", "L20", 4.0); // Baner to Aundh
        parkingSystem.addLocationConnection("L21", "L23", 2.0); // Kalyani Nagar to Viman Nagar
    }

    private static void viewAllMessages(ParkingSystem parkingSystem) {
        System.out.println("\n+============================================================================+");
        System.out.println("|                           ALL USER MESSAGES                                   |");
        System.out.println("+============================================================================+");
        
        List<String> messages = parkingSystem.getAllUserMessages();
        if (messages.isEmpty()) {
            System.out.println("| No messages found.                                                         |");
        } else {
            for (String message : messages) {
                System.out.println("| " + message + " |");
            }
        }
        System.out.println("+============================================================================+");
    }

    private static void replyToUserMessage(ParkingSystem parkingSystem, Scanner scanner) {
        System.out.println("\n+============================================================================+");
        System.out.println("|                           REPLY TO USER MESSAGE                               |");
        System.out.println("+============================================================================+");
        
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        List<String> userMessages = parkingSystem.getUserMessages(userId);
        if (userMessages.isEmpty()) {
            System.out.println("No messages found for this user.");
            return;
        }
        
        System.out.println("\nUser's messages:");
        for (String message : userMessages) {
            System.out.println(message);
        }
        
        System.out.print("\nEnter your reply: ");
        String reply = scanner.nextLine();
        
        parkingSystem.addAdminReply(userId, reply);
        System.out.println("Reply sent successfully!");
    }
} 