import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private String password;
    private LocalDateTime registrationTime;
    private boolean isBlocked;
    private List<Booking> bookings;
    private List<String> messages;

    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.registrationTime = LocalDateTime.now();
        this.isBlocked = false;
        this.bookings = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public LocalDateTime getRegistrationTime() { return registrationTime; }
    public boolean isBlocked() { return isBlocked; }
    public List<Booking> getBookings() { return bookings; }
    public List<String> getMessages() { return messages; }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return String.format("User: %s\nRegistration Time: %s\nStatus: %s\nTotal Bookings: %d",
            username, 
            registrationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            isBlocked ? "Blocked" : "Active",
            bookings.size());
    }
} 