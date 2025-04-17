
import java.util.*;

public class LoginTracker {

    // Mock user credentials (username -> password)
    static Map<String, String> userCredentials = new HashMap<>();
    static {
        userCredentials.put("unnati", "1234");
        userCredentials.put("shruti", "abcd");
        userCredentials.put("ishwari", "pass");
    }

    // Track login sources: username -> Set of location-device combinations
    private static Map<String, Set<String>> userLoginSources = new HashMap<>();

    // Track failed login attempts: username -> queue of timestamps
    private static Map<String, Queue<Long>> failedLoginAttempts = new HashMap<>();

    // Constants for brute-force detection
    private static final long TIME_WINDOW = 60 * 1000; // 1 minute
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🔐 Welcome to Identity Theft Login Tracker");

        while (true) {
            System.out.print("
Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            System.out.print("Enter location: ");
            String location = sc.nextLine();

            System.out.print("Enter device: ");
            String device = sc.nextLine();

            boolean isSuccess = validateLogin(username, password);

            login(username, location, device, isSuccess);

            System.out.print("
Do you want to simulate another login? (yes/no): ");
            String again = sc.nextLine().trim().toLowerCase();
            if (!again.equals("yes")) {
                System.out.println("👋 Exiting tracker. Goodbye!");
                break;
            }
        }
    }

    // Validate username and password
    private static boolean validateLogin(String username, String password) {
        if (!userCredentials.containsKey(username)) {
            System.out.println("❌ Username not found.");
            return false;
        }
        if (!userCredentials.get(username).equals(password)) {
            System.out.println("❌ Incorrect password.");
            return false;
        }
        return true;
    }

    // Handle login process
    private static void login(String username, String location, String device, boolean isSuccess) {
        String source = location + "-" + device;

        if (isSuccess) {
            boolean isSuspicious = isSuspiciousLogin(username, source);
            if (isSuspicious) {
                System.out.println("⚠️  Suspicious login detected for " + username + " from new device/location: " + source);
            } else {
                System.out.println("✅ Login successful for " + username + " from known source.");
            }

            userLoginSources.putIfAbsent(username, new HashSet<>());
            userLoginSources.get(username).add(source);

            failedLoginAttempts.remove(username); // clear previous failed attempts
        } else {
            System.out.println("❌ Failed login attempt for " + username + " from " + source);
            detectBruteForce(username);
        }
    }

    // Check if login source is new
    private static boolean isSuspiciousLogin(String username, String source) {
        Set<String> sources = userLoginSources.getOrDefault(username, new HashSet<>());
        return !sources.contains(source);
    }

    // Detect brute-force by checking if there are 5 failed attempts in 1 minute
    private static void detectBruteForce(String username) {
        long currentTime = System.currentTimeMillis();

        failedLoginAttempts.putIfAbsent(username, new LinkedList<>());
        Queue<Long> attempts = failedLoginAttempts.get(username);

        // Remove old entries beyond the time window
        while (!attempts.isEmpty() && (currentTime - attempts.peek() > TIME_WINDOW)) {
            attempts.poll();
        }

        attempts.add(currentTime);

        if (attempts.size() >= MAX_FAILED_ATTEMPTS) {
            System.out.println("🚨 Brute-force attack suspected on user: " + username);
        }
    }
}
