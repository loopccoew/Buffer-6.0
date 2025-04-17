import java.time.LocalDateTime;
import java.util.*;

public class ParkingSystem {
    private LocationGraph locationGraph;
    private Map<String, User> users;
    private Map<String, SegmentTree> locationSlots;
    private Map<String, List<Booking>> bookings;
    private Map<String, Double> revenue;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public ParkingSystem() {
        this.locationGraph = new LocationGraph();
        this.users = new HashMap<>();
        this.locationSlots = new HashMap<>();
        this.bookings = new HashMap<>();
        this.revenue = new HashMap<>();
    }

    // Admin Operations
    public boolean adminLogin(String username, String password) {
        return username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
    }

    public void addLocation(Location location) {
        locationGraph.addLocation(location);
        locationSlots.put(location.getLocationId(), new SegmentTree(location.getTotalSlots()));
        bookings.put(location.getLocationId(), new ArrayList<>());
        revenue.put(location.getLocationId(), 0.0);
    }

    public void addLocationConnection(String locationId1, String locationId2, double distance) {
        locationGraph.addConnection(locationId1, locationId2, distance);
    }

    public List<Location> getAllLocations() {
        return locationGraph.getAllLocations();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void blockUser(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.setBlocked(true);
        }
    }

    public void unblockUser(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.setBlocked(false);
        }
    }

    public void deleteUser(String userId) {
        users.remove(userId);
    }

    public void messageUser(String userId, String message) {
        User user = users.get(userId);
        if (user != null) {
            user.addMessage(message);
        }
    }

    public List<Feedback> getLocationFeedbacks(String locationId) {
        Location location = locationGraph.getLocation(locationId);
        if (location != null) {
            List<Feedback> feedbackList = new ArrayList<>();
            feedbackList.addAll((Collection<Feedback>) location.getFeedbacks());
            return feedbackList;
        }
        return new ArrayList<>();
    }

    public double getTotalRevenue() {
        return revenue.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public Map<String, Double> getLocationRevenue() {
        return new HashMap<>(revenue);
    }

    // User Operations
    public boolean registerUser(String userId, String username, String password) {
        if (users.containsKey(userId)) {
            return false;
        }
        users.put(userId, new User(userId, username, password));
        return true;
    }

    public User loginUser(String userId, String password) {
        User user = users.get(userId);
        if (user != null && user.verifyPassword(password) && !user.isBlocked()) {
            return user;
        }
        return null;
    }

    public Location findLocationByName(String name) {
        for (Location location : locationGraph.getAllLocations()) {
            if (location.getName().equalsIgnoreCase(name)) {
                return location;
            }
        }
        return null;
    }

    public List<Location> searchLocations(String sourceName, int count) {
        Location sourceLocation = findLocationByName(sourceName);
        if (sourceLocation == null) {
            throw new IllegalArgumentException("Source location does not exist");
        }
        return locationGraph.findNearestLocations(sourceLocation.getLocationId(), count);
    }

    public List<Location> findEVChargingStations(String sourceLocationId) {
        return locationGraph.findEVChargingStations(sourceLocationId);
    }

    public Booking bookSlot(String userId, String locationId, int slotNumber, LocalDateTime startTime, LocalDateTime endTime) {
        User user = users.get(userId);
        Location location = locationGraph.getLocation(locationId);
        SegmentTree slots = locationSlots.get(locationId);

        if (user == null || location == null || slots == null || user.isBlocked()) {
            return null;
        }

        if (!slots.isSlotAvailable(slotNumber)) {
            return null;
        }

        double hours = java.time.Duration.between(startTime, endTime).toHours();
        double totalCost = hours * location.getPricePerHour();

        // Generate a shorter booking ID: BK + locationId + slotNumber + last 4 digits of timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String shortBookingId = "BK" + locationId + slotNumber + timestamp.substring(timestamp.length() - 4);

        Booking booking = new Booking(shortBookingId, userId, locationId, 
                                    slotNumber, startTime, endTime, totalCost);
        
        slots.bookSlot(slotNumber);
        location.setAvailableSlots(location.getAvailableSlots() - 1);  // Update available slots
        bookings.get(locationId).add(booking);
        user.addBooking(booking);
        revenue.put(locationId, revenue.get(locationId) + totalCost);

        return booking;
    }

    public boolean cancelBooking(String bookingId) {
        for (List<Booking> locationBookings : bookings.values()) {
            for (Booking booking : locationBookings) {
                if (booking.getBookingId().equals(bookingId) && booking.isActive()) {
                    booking.setActive(false);
                    locationSlots.get(booking.getLocationId()).releaseSlot(booking.getSlotNumber());
                    return true;
                }
            }
        }
        return false;
    }

    public void addFeedback(String userId, String locationId, double rating, String comment) {
        Location location = locationGraph.getLocation(locationId);
        if (location != null) {
            Feedback feedback = new Feedback(userId, locationId, rating, comment);
            location.addFeedback(feedback);
        }
    }

    public List<Booking> getUserBookings(String userId) {
        User user = users.get(userId);
        if (user != null) {
            List<Booking> bookingList = new ArrayList<>();
            bookingList.addAll((Collection<Booking>) user.getBookings());
            return bookingList;
        }
        return new ArrayList<>();
    }

    public Map<String, Integer> getAnalytics() {
        Map<String, Integer> analytics = new HashMap<>();
        
        // Total parking areas
        analytics.put("totalAreas", locationGraph.getAllLocations().size());
        
        // Total slots
        int totalSlots = locationSlots.values().stream()
            .mapToInt(slots -> slots.getAvailableSlots())
            .sum();
        analytics.put("totalSlots", totalSlots);
        
        // Total registered users
        analytics.put("totalUsers", users.size());
        
        return analytics;
    }

    public LocationGraph getLocationGraph() {
        return locationGraph;
    }

    public List<Booking> getLocationBookings(String locationId) {
        return bookings.getOrDefault(locationId, new ArrayList<>());
    }

    public void addMessage(String userId, String message) {
        User user = users.get(userId);
        if (user != null) {
            user.addMessage("[User] " + message);
        }
    }

    public void addAdminReply(String userId, String reply) {
        User user = users.get(userId);
        if (user != null) {
            user.addMessage("[Admin] " + reply);
        }
    }

    public List<String> getUserMessages(String userId) {
        User user = users.get(userId);
        if (user != null) {
            return new ArrayList<>(user.getMessages());
        }
        return new ArrayList<>();
    }

    public List<String> getAllUserMessages() {
        List<String> allMessages = new ArrayList<>();
        for (User user : users.values()) {
            allMessages.add("=== Messages for user: " + user.getUsername() + " ===");
            allMessages.addAll(user.getMessages());
            allMessages.add("=========================================");
        }
        return allMessages;
    }
} 