import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private String bookingId;
    private String userId;
    private String locationId;
    private int slotNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private boolean isActive;

    public Booking(String bookingId, String userId, String locationId, int slotNumber, 
                  LocalDateTime startTime, LocalDateTime endTime, double totalCost) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.locationId = locationId;
        this.slotNumber = slotNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.isActive = true;
    }

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getLocationId() { return locationId; }
    public int getSlotNumber() { return slotNumber; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getTotalCost() { return totalCost; }
    public boolean isActive() { return isActive; }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return String.format("Booking ID: %s\nLocation ID: %s\nSlot: %d\nStart: %s\nEnd: %s\nCost: ₹%.2f\nStatus: %s",
            bookingId,
            locationId,
            slotNumber,
            startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            totalCost,
            isActive ? "Active" : "Cancelled");
    }
} 