import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private String userId;
    private String locationId;
    private double rating;
    private String comment;
    private LocalDateTime timestamp;

    public Feedback(String userId, String locationId, double rating, String comment) {
        this.userId = userId;
        this.locationId = locationId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getUserId() { return userId; }
    public String getLocationId() { return locationId; }
    public double getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Rating: %.1f\nComment: %s\nTime: %s", 
            rating, comment, timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
} 