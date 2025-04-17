import java.util.PriorityQueue;

public class Location {
    private String locationId;
    private String name;
    private String address;
    private int totalSlots;
    private int availableSlots;
    private double pricePerHour;
    private PriorityQueue<Feedback> feedbacks;
    private boolean hasEVCharging;

    public Location(String locationId, String name, String address, int totalSlots, double pricePerHour, boolean hasEVCharging) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots;
        this.pricePerHour = pricePerHour;
        this.hasEVCharging = hasEVCharging;
        this.feedbacks = new PriorityQueue<>((a, b) -> Double.compare(b.getRating(), a.getRating()));
    }

    // Getters and Setters
    public String getLocationId() { return locationId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getTotalSlots() { return totalSlots; }
    public int getAvailableSlots() { return availableSlots; }
    public double getPricePerHour() { return pricePerHour; }
    public boolean hasEVCharging() { return hasEVCharging; }
    public PriorityQueue<Feedback> getFeedbacks() { return feedbacks; }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void addFeedback(Feedback feedback) {
        feedbacks.offer(feedback);
    }

    public double getAverageRating() {
        if (feedbacks.isEmpty()) return 0.0;
        return feedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    public String getRatingStars() {
        double rating = getAverageRating();
        return String.format("%.1f/5.0", rating);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("+============================================================================+\n");
        sb.append(String.format("| Location: %-65s \n", name));
        sb.append(String.format("| Address: %-66s \n", address));
        sb.append(String.format("| Available Slots: %d/%d%55s \n", 
            availableSlots, totalSlots, ""));
        sb.append(String.format("| Price: Rs%.2f/hour%60s \n", pricePerHour, ""));
        sb.append(String.format("| Rating: %s%65s \n", getRatingStars(), ""));
        if (hasEVCharging) {
            sb.append(String.format("| EV Charging Available%65s \n", ""));
        }
        sb.append("+============================================================================+");
        return sb.toString();
    }

    public String toTableRow() {
        return String.format("| %-20s | %-30s | %3d/%3d | Rs%6.2f | %6s | %-11s |",
            name, address, availableSlots, totalSlots, pricePerHour, 
            getRatingStars(), hasEVCharging ? "Yes" : "No");
    }
} 