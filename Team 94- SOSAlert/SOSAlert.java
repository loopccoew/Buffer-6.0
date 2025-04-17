import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class SOSAlert {

    static class Location {
        double latitude, longitude;

        public Location(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }

        public double distanceTo(Location other) {
            final int R = 6371;
            double latDist = Math.toRadians(other.latitude - this.latitude);
            double lonDist = Math.toRadians(other.longitude - this.longitude);
            double a = Math.sin(latDist / 2) * Math.sin(latDist / 2)
                    + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude))
                            * Math.sin(lonDist / 2) * Math.sin(lonDist / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c;
        }
    }

    static class EmergencyContact {
        String name, phone;

        public EmergencyContact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public void notify(String message, JTextArea log) {
            log.append("\n📱 Notifying " + name + " at " + phone + ": " + message + "\n");
        }
    }

    static class PoliceStation {
        String name;
        Location location;

        public PoliceStation(String name, Location loc) {
            this.name = name;
            this.location = loc;
        }

        public double getDistanceFrom(Location userLoc) {
            return location.distanceTo(userLoc);
        }

        public void alert(String message, JTextArea log) {
            log.append("\n🚨 Alert sent to " + name + ": " + message + "\n");
        }
    }

    static class User {
        String name;
        Location location;
        List<EmergencyContact> contacts;

        public User(String name, Location loc, List<EmergencyContact> contacts) {
            this.name = name;
            this.location = loc;
            this.contacts = contacts;
        }
    }

    static class SosService {
        List<PoliceStation> stations;
        JTextArea log;

        public SosService(List<PoliceStation> stations, JTextArea log) {
            this.stations = stations;
            this.log = log;
        }

        public void sendSos(User user) {
            String message = "SOS! " + user.name + " needs help at (Lat: " + user.location.latitude +
                    ", Lon: " + user.location.longitude + ") - " + new Date();

            for (EmergencyContact contact : user.contacts) {
                contact.notify(message, log);
            }

            stations.stream()
                    .sorted(Comparator.comparingDouble(s -> s.getDistanceFrom(user.location)))
                    .limit(3)
                    .forEach(s -> s.alert(message, log));
        }
    }

    static class SosButtonHandler {
        private static final int PRESS_LIMIT = 3;
        private static final long TIME_WINDOW_MS = 5000;

        private int pressCount = 0;
        private long firstPressTime = 0;

        private final SosService sosService;
        private final User user;

        public SosButtonHandler(SosService sosService, User user) {
            this.sosService = sosService;
            this.user = user;
        }

        public void pressButton() {
            long now = System.currentTimeMillis();

            if (pressCount == 0) {
                firstPressTime = now;
            }

            if (now - firstPressTime <= TIME_WINDOW_MS) {
                pressCount++;
                sosService.log.append("🔘 Button pressed " + pressCount + " time(s)\n");

                if (pressCount == PRESS_LIMIT) {
                    sosService.sendSos(user);
                    reset();
                }
            } else {
                sosService.log.append("\n⏱ Timeout. Resetting counter.\n");
                reset();
                pressButton(); // count this press again
            }
        }

        private void reset() {
            pressCount = 0;
            firstPressTime = 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SOS Alert App");
            frame.setSize(600, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.getContentPane().setBackground(new Color(245, 245, 245));

            // Header
            JLabel title = new JLabel("🚨  SOSNet SOS Service", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setBorder(new EmptyBorder(15, 10, 15, 10));
            frame.add(title, BorderLayout.NORTH);

            // Log Area
            JTextArea logArea = new JTextArea();
            logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            logArea.setBackground(new Color(30, 30, 30));
            logArea.setForeground(Color.WHITE);
            logArea.setCaretColor(Color.WHITE);
            logArea.setEditable(false);
            logArea.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY),
                    "Notification Log",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Segoe UI", Font.BOLD, 14),
                    Color.GRAY));

            JScrollPane logScroll = new JScrollPane(logArea);
            logScroll.setPreferredSize(new Dimension(600, 250));
            logScroll.setBorder(new EmptyBorder(10, 20, 10, 20));
            frame.add(logScroll, BorderLayout.CENTER);

            // Button
            JButton sosButton = new JButton("🔴 PRESS SOS");
            sosButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
            sosButton.setFocusPainted(false);
            sosButton.setBackground(new Color(220, 20, 60));
            sosButton.setForeground(Color.WHITE);
            sosButton.setPreferredSize(new Dimension(300, 60));
            sosButton.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            sosButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(245, 245, 245));
            buttonPanel.add(sosButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Setup data
            Location userLoc = new Location(12.9716, 77.5946);
            EmergencyContact mom = new EmergencyContact("Mom", "+91 1234567890");
            EmergencyContact friend = new EmergencyContact("Bestie", "+91 9876543210");
            User user = new User("Anjali", userLoc, Arrays.asList(mom, friend));

            List<PoliceStation> stations = Arrays.asList(
                    new PoliceStation("Borivali Station", new Location(12.9720, 77.5930)),
                    new PoliceStation("Karvenagar Station", new Location(12.9600, 77.5900)),
                    new PoliceStation("Kothrud Station", new Location(12.9800, 77.6100)));

            SosService sosService = new SosService(stations, logArea);
            SosButtonHandler buttonHandler = new SosButtonHandler(sosService, user);

            sosButton.addActionListener(e -> buttonHandler.pressButton());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            logArea.append("✅ Welcome! Click the SOS button 3 times within 5 seconds to send alerts.\n");
        });
    }
}