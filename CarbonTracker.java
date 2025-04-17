import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CarbonTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}

class UserData {
    static int totalPoints = 0;       // Total points for vehicle and path combined
    static int vehiclePoints = 0;     // Points for selected vehicle
    static int pathPoints = 0;        // Points for selected path
    static ArrayList<String> journeyHistory = new ArrayList<>();  // History of journeys
}

// Login Page
class LoginPage extends JFrame {
    LoginPage() {
        this.setTitle("track.c");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(420, 420);
        this.setLayout(null);

        JPanel outerPanel = new JPanel();
        outerPanel.setBackground(new Color(240, 240, 240));
        outerPanel.setBounds(0, 0, 420, 420);
        outerPanel.setLayout(null);

        Border outerBorder = BorderFactory.createLineBorder(Color.BLACK, 5);
        outerPanel.setBorder(outerBorder);

        JPanel yellowPanel = new JPanel();
        yellowPanel.setBackground(Color.YELLOW);
        yellowPanel.setBounds(80, 100, 260, 160);
        yellowPanel.setLayout(null);

        Border panelBorder = BorderFactory.createLineBorder(Color.RED, 4);
        yellowPanel.setBorder(panelBorder);

        JLabel userLabel = new JLabel("USERNAME:- ");
        userLabel.setBounds(10, 10, 100, 25);
        yellowPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(120, 10, 120, 25);
        yellowPanel.add(userField);

        JLabel passLabel = new JLabel("PASSWORD:- ");
        passLabel.setBounds(10, 50, 100, 25);
        yellowPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(120, 50, 120, 25);
        yellowPanel.add(passField);

        JButton enterButton = new JButton("Enter");
        enterButton.setBounds(80, 100, 100, 30);
        yellowPanel.add(enterButton);

        enterButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals("user1") && password.equals("pass1")) {
                dispose();
                new MainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password");
            }
        });

        outerPanel.add(yellowPanel);

        ImageIcon image = new ImageIcon("C:\\Users\\wankh\\OneDrive\\Pictures\\Documents\\Desktop\\TrackC\\trackc_graph.png");
        this.setIconImage(image.getImage());

        this.setContentPane(outerPanel);
        this.setVisible(true);
    }
}

// Main Menu
class MainMenu extends JFrame {
    JRadioButton car, bus, bicycle, bike;

    MainMenu() {
        setTitle("track.c - Journey");
        setSize(600, 500);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();

        JMenu profile = new JMenu("Profile");
        profile.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Total Points: " + UserData.totalPoints);
            }
        });

        JMenu history = new JMenu("History");
        history.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (UserData.journeyHistory.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No journeys yet.");
                } else {
                    JOptionPane.showMessageDialog(null, String.join("\n", UserData.journeyHistory));
                }
            }
        });

        JMenu logout = new JMenu("Logout");
        logout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginPage();
            }
        });

        menuBar.add(profile);
        menuBar.add(history);
        menuBar.add(logout);
        setJMenuBar(menuBar);

        add(new JLabel("Choose Vehicle:"));
        car = new JRadioButton("Car");
        bus = new JRadioButton("Bus");
        bicycle = new JRadioButton("Bicycle");
        bike = new JRadioButton("Bike");

        ButtonGroup group = new ButtonGroup();
        group.add(car);
        group.add(bus);
        group.add(bicycle);
        group.add(bike);

        add(car);
        add(bus);
        add(bicycle);
        add(bike);
        add(Box.createRigidArea(new Dimension(1000, 20)));

        JButton findPathBtn = new JButton("Find Path");
        findPathBtn.addActionListener(e -> {
            String from = JOptionPane.showInputDialog("Enter source (A-J):").toUpperCase();
            String to = JOptionPane.showInputDialog("Enter destination (A-J):").toUpperCase();
            String mode = getVehicle();

            if (from.length() != 1 || to.length() != 1 || mode == null) {
                JOptionPane.showMessageDialog(this, "Please enter valid nodes and select a vehicle.");
                return;
            }

            Dijkstra dijkstra = new Dijkstra();
            Dijkstra.PathResult result = dijkstra.findPath(from.charAt(0), to.charAt(0));

            if (result.path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No path found.");
            } else {
                int emissions = calculateEmission(mode, result.distance);
                String message = "Shortest Path: " + result.path +
                        "\nDistance: " + result.distance + " km" +
                        "\nCO₂ Emissions (" + mode + "): " + emissions + " g";
                JOptionPane.showMessageDialog(this, message);
                UserData.journeyHistory.add(from + " → " + to + ": " + result.path + " | " + result.distance + " km | " + emissions + "g CO₂");

                int confirm = JOptionPane.showConfirmDialog(this, "Do you want to explore more paths?", "Explore More", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    List<Dijkstra.PathResult> allPaths = dijkstra.findAllPaths(from.charAt(0), to.charAt(0));
                    allPaths.removeIf(p -> p.path.equals(result.path));
                    allPaths.sort(Comparator.comparingInt(p -> calculateEmission(mode, p.distance)));

                    StringBuilder altMessage = new StringBuilder("Top 3 Low-Emission Paths:\n");
                    for (int i = 0; i < Math.min(3, allPaths.size()); i++) {
                        Dijkstra.PathResult alt = allPaths.get(i);
                        int co2 = calculateEmission(mode, alt.distance);
                        altMessage.append((i + 1) + ". " + alt.path + " | " + alt.distance + " km | " + co2 + "g CO₂\n");
                    }

                    JOptionPane.showMessageDialog(this, altMessage.toString());

                    String[] options = {"1", "2", "3"};
                    int choice = JOptionPane.showOptionDialog(this,
                            "Which path would you like to choose?",
                            "Select Path",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    int pathReward = switch (choice) {
                        case 0 -> 100;
                        case 1 -> 50;
                        case 2 -> 20;
                        default -> 0;
                    };

                    if (choice >= 0 && choice <= 2) {
                        UserData.totalPoints += pathReward;
                        UserData.pathPoints = pathReward;
                        String rank = switch (choice) {
                            case 0 -> "least";
                            case 1 -> "second least";
                            case 2 -> "third least";
                            default -> "";
                        };
                        JOptionPane.showMessageDialog(this,
                                "You chose the " + rank + " CO₂ emission path! \n+" + pathReward + " points!");
                    }
                }
            }
        });

        add(findPathBtn);
        add(Box.createRigidArea(new Dimension(0, 50)));

        JButton rewardBtn = new JButton("Show Reward");
        rewardBtn.addActionListener(e -> {
            String vehicle = getVehicle();
            if (vehicle == null) {
                JOptionPane.showMessageDialog(this, "Choose a vehicle first!");
                return;
            }
            int vehicleReward = switch (vehicle) {
                case "Bicycle" -> 50;
                case "Bus" -> 30;
                case "Bike" -> 20;
                case "Car" -> 10;
                default -> 0;
            };
            UserData.totalPoints += vehicleReward;
            UserData.vehiclePoints = vehicleReward;
            JOptionPane.showMessageDialog(this, vehicle + " selected.\n+" + vehicleReward + " points!");
        });

        add(rewardBtn);
        add(Box.createRigidArea(new Dimension(1000, 20)));

        // Add button for total reward points
        JButton totalRewardBtn = new JButton("Total Reward Points");
        totalRewardBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Total Reward Points: " + UserData.totalPoints);
        });
        add(totalRewardBtn);

        add(Box.createRigidArea(new Dimension(1000, 20)));

        JButton showGraphBtn = new JButton("Show Graph");
        showGraphBtn.addActionListener(e -> new GraphWindow());
        add(showGraphBtn);

        setVisible(true);
    }

    String getVehicle() {
        if (bicycle.isSelected()) return "Bicycle";
        if (bus.isSelected()) return "Bus";
        if (bike.isSelected()) return "Bike";
        if (car.isSelected()) return "Car";
        return null;
    }

    static int calculateEmission(String mode, int distance) {
        return switch (mode) {
            case "Car" -> distance * 192;
            case "Bus" -> distance * 27;
            case "Metro" -> distance * 14;
            case "Bicycle" -> 0;
            default -> 0;
        };
    }
}

// Dijkstra algorithm classes
class Dijkstra {
    static class Edge {
        char dest;
        int weight;
        Edge(char d, int w) {
            dest = d;
            weight = w;
        }
    }

    static class PathResult {
        List<Character> path;
        int distance;
        PathResult(List<Character> path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }

    static Map<Character, List<Edge>> graph = new HashMap<>();

    Dijkstra() {
        add('A', 'B', 5); add('B', 'A', 5);
        add('A', 'E', 2); add('E', 'A', 2);
        add('B', 'C', 3); add('C', 'B', 3);
        add('B', 'D', 1); add('D', 'B', 1);
        add('C', 'G', 6); add('G', 'C', 6);
        add('D', 'C', 1); add('C', 'D', 1);
        add('D', 'F', 3); add('F', 'D', 3);
        add('E', 'D', 4); add('D', 'E', 4);
        add('E', 'I', 9); add('I', 'E', 9);
        add('F', 'I', 8); add('I', 'F', 8);
        add('F', 'J', 8); add('J', 'F', 8);
        add('G', 'J', 8); add('J', 'G', 8);
        add('I', 'J', 6); add('J', 'I', 6);
        add('I', 'H', 7); add('H', 'I', 7);
        add('H', 'J', 10); add('J', 'H', 10);
    }

    void add(char src, char dst, int weight) {
        graph.computeIfAbsent(src, e -> new ArrayList<>()).add(new Edge(dst, weight));
    }

    PathResult findPath(char start, char end) {
        Map<Character, Integer> dist = new HashMap<>();
        Map<Character, Character> prev = new HashMap<>();
        Set<Character> visited = new HashSet<>();
        PriorityQueue<Character> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (char node : graph.keySet()) {
            dist.put(node, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            char u = pq.poll();
            if (visited.contains(u)) continue;
            visited.add(u);

            for (Edge edge : graph.get(u)) {
                char v = edge.dest;
                int alt = dist.get(u) + edge.weight;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }

        List<Character> path = new ArrayList<>();
        for (char at = end; at != 0; at = prev.getOrDefault(at, (char) 0)) {
            path.add(at);
        }
        Collections.reverse(path);
        return new PathResult(path, dist.get(end));
    }

    List<PathResult> findAllPaths(char start, char end) {
        List<PathResult> allPaths = new ArrayList<>();
        LinkedList<Character> currentPath = new LinkedList<>();
        Set<Character> visited = new HashSet<>();
    
        dfs(start, end, currentPath, 0, visited, allPaths);
    
        // 🔍 Filter out duplicate path patterns
        Set<String> seen = new HashSet<>();
        List<PathResult> uniquePaths = new ArrayList<>();
        for (PathResult path : allPaths) {
            String key = path.path.toString();
            if (seen.add(key)) {
                uniquePaths.add(path);
            }
        }
    
        return uniquePaths;
    }
    
    
    private void dfs(char current, char end, LinkedList<Character> path, int dist, Set<Character> visited, List<PathResult> results) {
        visited.add(current);
        path.add(current);
    
        if (current == end) {
            results.add(new PathResult(new ArrayList<>(path), dist));
        } else {
            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(edge.dest)) {
                    dfs(edge.dest, end, path, dist + edge.weight, visited, results);
                }
            }
        }
    
        path.removeLast();
        visited.remove(current);
    }
    
}

// GraphWindow class to display graph

class GraphWindow extends JFrame {
    GraphWindow() {
        setTitle("Graph Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            // Path to your image
            String imagePath ="C:\\Users\\Bhagyashri\\Desktop\\TrackCApp\\trackc_graph.png";
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

            // Zoom out by scaling to 50% size
            int scaledWidth = originalImage.getWidth() / 2;
            int scaledHeight = originalImage.getHeight() / 2;

            Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(imageIcon);
            JScrollPane scrollPane = new JScrollPane(imageLabel);

            add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load image: " + e.getMessage());
        }

        setVisible(true);
    }
}