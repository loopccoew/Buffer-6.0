import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

class User {
    int id;
    String username;
    String password;
    String role;

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
class Property {
    static int idCounter = 1;
    int propertyId;
    String name;
    String location;
    double totalPrice;
    int totalTokens;
    int availableTokens;
    String kycStatus;

    public Property(String name, String location, double totalPrice, int totalTokens) {
        this.propertyId = idCounter++;
        this.name = name;
        this.location = location;
        this.totalPrice = totalPrice;
        this.totalTokens = totalTokens;
        this.availableTokens = totalTokens;
        this.kycStatus = "Pending";
    }

    public double getTokenPrice() {
        return totalPrice / totalTokens;
    }
}

class SupportQuery {
    String investorUsername;
    String subject;
    String message;

    public SupportQuery(String investorUsername, String subject, String message) {
        this.investorUsername = investorUsername;
        this.subject = subject;
        this.message = message;
    }
}


class Token {
    static int tokenIdCounter = 1;
    int tokenId;
    int ownerId;
    int propertyId;
    LocalDate purchaseDate;
    double tokenValue;

    public Token(int ownerId, int propertyId, double tokenValue) {
        this.tokenId = tokenIdCounter++;
        this.ownerId = ownerId;
        this.propertyId = propertyId;
        this.purchaseDate = LocalDate.now();
        this.tokenValue = tokenValue;
    }
}

public class TokenVestAppSwing {
    static ArrayList<User> users = new ArrayList<>();
    static HashMap<Integer, Property> propertyMap = new HashMap<>();
    static ArrayList<Token> tokens = new ArrayList<>();
    static HashMap<Integer, List<Token>> investorTokens = new HashMap<>();
    static int userIdCounter = 1;
    static ArrayList<SupportQuery> supportQueries = new ArrayList<>();
    static List<TokenResale> resaleListings = new ArrayList<>();

    static class TokenResale {
        Token token;
        int sellerId;
        double resalePrice;

        public TokenResale(Token token, int sellerId, double resalePrice) {
            this.token = token;
            this.sellerId = sellerId;
            this.resalePrice = resalePrice;
        }
    }
    public static String getUsernameById(int userId) {
        for (User u : users) {
            if (u.id == userId) return u.username;
        }
        return "Unknown";
    }

    public static void main(String[] args) {
        seedUsers();
        seedProperties(); 
        SwingUtilities.invokeLater(TokenVestAppSwing::showRoleSelection);
    }

    public static void seedUsers() {
        users.add(new User(userIdCounter++, "admin", "admin123", "admin"));
        users.add(new User(userIdCounter++, "investor1", "pass123", "investor"));
        users.add(new User(userIdCounter++, "investor2", "invest2", "investor"));
        users.add(new User(userIdCounter++, "investor3", "invest3", "investor"));
        users.add(new User(userIdCounter++, "investor4", "invest4", "investor"));
    }

    public static void seedProperties() {
        Property p1 = new Property("Palm Residency", "Mumbai", 5000000, 100);
        Property p2 = new Property("Skyline Towers", "Pune", 7500000, 150);
        Property p3 = new Property("Ocean View", "Goa", 10000000, 200);
        Property p4 = new Property("Marriot", "Banglore", 50000000, 200);
    
        propertyMap.put(p1.propertyId, p1);
        propertyMap.put(p2.propertyId, p2);
        propertyMap.put(p3.propertyId, p3);
        propertyMap.put(p4.propertyId, p4);
    }
    
    public static void showRoleSelection() {
        JFrame frame = new JFrame("Welcome to TokenVest");
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(3, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton adminBtn = new JButton("Login as Admin");
        JButton investorBtn = new JButton("Login as Investor");

        adminBtn.addActionListener(e -> {
            frame.dispose();
            showLoginForm("admin");
        });

        investorBtn.addActionListener(e -> {
            frame.dispose();
            showInvestorChoice();
        });

        frame.add(new JLabel("Select Role", SwingConstants.CENTER));
        frame.add(adminBtn);
        frame.add(investorBtn);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showInvestorChoice() {
        JFrame frame = new JFrame("Investor Options");
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(3, 1));

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");

        loginBtn.addActionListener(e -> {
            frame.dispose();
            showLoginForm("investor");
        });

        signupBtn.addActionListener(e -> {
            frame.dispose();
            showSignupForm();
        });

        frame.add(new JLabel("Investor - Choose an option", SwingConstants.CENTER));
        frame.add(loginBtn);
        frame.add(signupBtn);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showLoginForm(String role) {
        JFrame frame = new JFrame("Login - " + role.toUpperCase());
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(new JLabel());
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String uname = usernameField.getText();
            String pwd = new String(passwordField.getPassword());
            User loggedIn = login(uname, pwd, role);
            if (loggedIn != null) {
                frame.dispose();
                if (role.equals("admin")) adminDashboard(loggedIn);
                else investorDashboard(loggedIn);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials for " + role);
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showSignupForm() {
        JFrame frame = new JFrame("Investor Sign Up");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton signupBtn = new JButton("Sign Up");

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(new JLabel());
        frame.add(signupBtn);

        signupBtn.addActionListener(e -> {
            String uname = usernameField.getText();
            String pwd = new String(passwordField.getPassword());

            if (uname.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter valid details.");
                return;
            }

            for (User u : users) {
                if (u.username.equals(uname)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists.");
                    return;
                }
            }

            User newUser = new User(userIdCounter++, uname, pwd, "investor");
            users.add(newUser);
            JOptionPane.showMessageDialog(frame, "Signup successful! Please login.");
            frame.dispose();
            showLoginForm("investor");
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static User login(String uname, String pwd, String role) {
        for (User u : users) {
            if (u.username.equals(uname) && u.password.equals(pwd) && u.role.equals(role)) return u;
        }
        return null;
    }

    public static void adminDashboard(User admin) {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(0, 1));

        JButton addProp = new JButton("Add Property");
        JButton viewProps = new JButton("View Properties");
        JButton updateKYC = new JButton("Update KYC Status");
        JButton deleteProp = new JButton("Delete Property");
        JButton analysisReport = new JButton("Generate Analysis Report");
        JButton viewInvestors = new JButton("View Investors");  // New button to view investors
        JButton logout = new JButton("Logout");

        addProp.addActionListener(e -> addPropertyUI());
        viewProps.addActionListener(e -> viewPropertiesUI());
        updateKYC.addActionListener(e -> updateKYCUI());
        deleteProp.addActionListener(e -> deletePropertyUI());
        analysisReport.addActionListener(e -> generateAnalysisReport());
        JButton viewQueries = new JButton("View Support Queries");
        viewQueries.addActionListener(e -> viewSupportQueriesUI());
        
        viewInvestors.addActionListener(e -> viewInvestorsUI());  // Action to view investors
        JButton updateProp = new JButton("Update Property Details");
        updateProp.addActionListener(e -> updatePropertyUI());
        

        JButton updatePwd = new JButton("Update Password");
        updatePwd.addActionListener(e -> updatePasswordUI(admin));
        
        logout.addActionListener(e -> {
            frame.dispose();
            showRoleSelection();
        });
       
        frame.add(addProp);
        frame.add(viewProps);
        frame.add(updateProp);
        frame.add(deleteProp);
        frame.add(updateKYC);
        frame.add(viewInvestors); 
        frame.add(analysisReport);
        frame.add(viewQueries);
        frame.add(updatePwd);
        frame.add(logout);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void viewSupportQueriesUI() {
        if (supportQueries.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No support queries yet.");
            return;
        }
    
        StringBuilder sb = new StringBuilder();
        for (SupportQuery q : supportQueries) {
            sb.append("From: ").append(q.investorUsername).append("\n")
              .append("Subject: ").append(q.subject).append("\n")
              .append("Message: ").append(q.message).append("\n")
              .append("-----------------------------\n");
        }
    
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Support Queries", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void viewInvestorsUI() {
        StringBuilder sb = new StringBuilder();
        for (User u : users) {
            if (u.role.equals("investor")) {
                sb.append("ID: ").append(u.id)
                  .append(", Username: ").append(u.username)
                  .append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.length() == 0 ? "No investors found" : sb.toString());
    }

    public static void addPropertyUI() {
        JTextField name = new JTextField();
        JTextField location = new JTextField();
        JTextField price = new JTextField();
        JTextField tokens = new JTextField();
        Object[] fields = {
            "Name:", name,
            "Location:", location,
            "Total Price:", price,
            "Total Tokens:", tokens
        };

        int opt = JOptionPane.showConfirmDialog(null, fields, "Add Property", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                Property p = new Property(
                    name.getText(),
                    location.getText(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(tokens.getText())
                );
                propertyMap.put(p.propertyId, p);
                JOptionPane.showMessageDialog(null, "Property added with ID: " + p.propertyId);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input");
            }
        }
    }

//    public static void viewPropertiesUI() {
//        if (propertyMap.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "No properties found.");
//            return;
//        }
//    
//        // Define column names
//        String[] columnNames = {"ID", "Name", "Location", "₹ Total Price", "Available Tokens", "KYC", "Token Price"};
//    
//        // Create data array
//        Object[][] data = new Object[propertyMap.size()][columnNames.length];
//        int i = 0;
//        for (Property p : propertyMap.values()) {
//            data[i][0] = p.propertyId;
//            data[i][1] = p.name;
//            data[i][2] = p.location;
//            data[i][3] = p.totalPrice;
//            data[i][4] = p.availableTokens;
//            data[i][5] = p.kycStatus;
//            data[i][6] = p.getTokenPrice();
//            i++;
//        }
//    
//        // Create table
//        JTable table = new JTable(data, columnNames);
//        JScrollPane scrollPane = new JScrollPane(table);
//        table.setFillsViewportHeight(true);
//    
//        // Show in JOptionPane
//        JOptionPane.showMessageDialog(null, scrollPane, "Property List", JOptionPane.INFORMATION_MESSAGE);
//    }
    public static void viewPropertiesUI() {
        JFrame frame = new JFrame("View Properties");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Dropdown options
        String[] sortOptions = {
            "Sort by: Default",
            "Price: Low to High",
            "Price: High to Low",
            "Location: A to Z",
            "Availability: Most to Least",
            "KYC: Pending First",
            "KYC: Approved First"
        };

        JComboBox<String> sortDropdown = new JComboBox<>(sortOptions);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton refreshBtn = new JButton("Apply Sort");

        // Sorting and displaying properties
        ActionListener showSortedProps = e -> {
            List<Property> sortedProps = new ArrayList<>(propertyMap.values());
            String selected = (String) sortDropdown.getSelectedItem();

            if (selected.equals("Price: Low to High")) {
                sortedProps.sort(Comparator.comparingDouble(p -> p.totalPrice));
            } else if (selected.equals("Price: High to Low")) {
                sortedProps.sort((p1, p2) -> Double.compare(p2.totalPrice, p1.totalPrice));
            } else if (selected.equals("Location: A to Z")) {
                sortedProps.sort(Comparator.comparing(p -> p.location.toLowerCase()));
            } else if (selected.equals("Availability: Most to Least")) {
                sortedProps.sort((p1, p2) -> Integer.compare(p2.availableTokens, p1.availableTokens));
            } else if (selected.equals("KYC: Pending First")) {
                sortedProps.sort(Comparator.comparing(p -> !p.kycStatus.equals("Pending")));
            } else if (selected.equals("KYC: Approved First")) {
                sortedProps.sort(Comparator.comparing(p -> !p.kycStatus.equals("Approved")));
            }

            StringBuilder sb = new StringBuilder();
            for (Property p : sortedProps) {
                sb.append("ID: ").append(p.propertyId)
                  .append(", Name: ").append(p.name)
                  .append(", Location: ").append(p.location)
                  .append(", ₹").append(p.totalPrice)
                  .append(", Available: ").append(p.availableTokens)
                  .append(", KYC: ").append(p.kycStatus)
                  .append(", Token Price: ").append(p.getTokenPrice())
                  .append("\n");
            }

            textArea.setText(sb.length() == 0 ? "No properties found." : sb.toString());
        };

        refreshBtn.addActionListener(showSortedProps);
        showSortedProps.actionPerformed(null); // Initial call to show default listing

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(sortDropdown, BorderLayout.CENTER);
        topPanel.add(refreshBtn, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void updateKYCUI() {
        String[] options = { "Approve", "Reject" };
        String idStr = JOptionPane.showInputDialog("Enter Property ID to update KYC");
        try {
            int id = Integer.parseInt(idStr);
            Property p = propertyMap.get(id);
            if (p == null) {
                JOptionPane.showMessageDialog(null, "Property not found");
                return;
            }

            int choice = JOptionPane.showOptionDialog(null, "Update KYC Status", "KYC Update",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                p.kycStatus = "Approved";
            } else if (choice == 1) {
                p.kycStatus = "Rejected";
            }

            JOptionPane.showMessageDialog(null, "KYC Status updated to: " + p.kycStatus);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input");
        }
    }

    public static void deletePropertyUI() {
        String idStr = JOptionPane.showInputDialog("Enter Property ID to delete");
        try {
            int id = Integer.parseInt(idStr);
            Property p = propertyMap.remove(id);
            if (p != null) {
                JOptionPane.showMessageDialog(null, "Property deleted");
            } else {
                JOptionPane.showMessageDialog(null, "Property not found");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input");
        }
    }

    public static void generateAnalysisReport() {
        // Create a new JFrame for the analysis options
        JFrame frame = new JFrame("Select Analysis Type");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 1, 10, 10));
    
        // Create buttons for each analysis type
        JButton mostTokensSoldBtn = new JButton("Properties with Most Tokens Sold");
        JButton highestRevenueBtn = new JButton("Properties Generating Highest Revenue");
        JButton soldOutBtn = new JButton("Properties that are Sold Out");
        JButton pendingKYCBtn = new JButton("Properties Pending KYC");
    
        // Add ActionListeners for each button
        mostTokensSoldBtn.addActionListener(e -> showMostTokensSold());
        highestRevenueBtn.addActionListener(e -> showHighestRevenueProperties());
        soldOutBtn.addActionListener(e -> showSoldOutProperties());
        pendingKYCBtn.addActionListener(e -> showPendingKYCProperties());
    
        // Add buttons to the JFrame
        frame.add(mostTokensSoldBtn);
        frame.add(highestRevenueBtn);
        frame.add(soldOutBtn);
        frame.add(pendingKYCBtn);
    
        frame.setLocationRelativeTo(null);  // Center the window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);  // Show the window
    }

    private static void showMostTokensSold() {
        StringBuilder sb = new StringBuilder("Top Properties with Most Tokens Sold:\n");
        propertyMap.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.totalTokens - p2.availableTokens, p1.totalTokens - p1.availableTokens))
                .limit(5)
                .forEach(p -> sb.append(p.name).append(": ").append(p.totalTokens - p.availableTokens).append(" tokens\n"));
    
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void showHighestRevenueProperties() {
        StringBuilder sb = new StringBuilder("Top Properties Generating Highest Revenue:\n");
        propertyMap.values().stream()
                .sorted((p1, p2) -> Double.compare(
                        p2.totalPrice - (p2.availableTokens * p2.getTokenPrice()),
                        p1.totalPrice - (p1.availableTokens * p1.getTokenPrice())))
                .limit(5)
                .forEach(p -> sb.append(p.name).append(": ₹")
                        .append(p.totalPrice - (p.availableTokens * p.getTokenPrice())).append("\n"));
    
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void showSoldOutProperties() {
        StringBuilder sb = new StringBuilder("Properties that are Sold Out:\n");
        propertyMap.values().stream()
                .filter(p -> p.availableTokens == 0)
                .forEach(p -> sb.append(p.name).append("\n"));
    
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No sold-out properties yet.");
    }

    private static void showPendingKYCProperties() {
        StringBuilder sb = new StringBuilder("Properties Pending KYC:\n");
        propertyMap.values().stream()
                .filter(p -> p.kycStatus.equals("Pending"))
                .forEach(p -> sb.append(p.name).append("\n"));
    
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No properties pending KYC.");
    }
    
    
    public static void updatePropertyUI() {
        JFrame frame = new JFrame("Update Property Details");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));
    
        JTextField propIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField tokensField = new JTextField();
        JButton updateBtn = new JButton("Update");
    
        frame.add(new JLabel("Property ID:"));
        frame.add(propIdField);
        frame.add(new JLabel("New Name:"));
        frame.add(nameField);
        frame.add(new JLabel("New Location:"));
        frame.add(locationField);
        frame.add(new JLabel("New Total Price:"));
        frame.add(priceField);
        frame.add(new JLabel("New Total Tokens:"));
        frame.add(tokensField);
        frame.add(new JLabel(""));
        frame.add(updateBtn);
    
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(propIdField.getText());
                Property prop = propertyMap.get(id);
                if (prop == null) {
                    JOptionPane.showMessageDialog(frame, "Property not found.");
                    return;
                }
    
                String newName = nameField.getText();
                String newLoc = locationField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                int newTokens = Integer.parseInt(tokensField.getText());
    
                // Optional check: prevent reduction of total tokens below sold tokens
                int soldTokens = prop.totalTokens - prop.availableTokens;
                if (newTokens < soldTokens) {
                    JOptionPane.showMessageDialog(frame, "Total tokens can't be less than tokens already sold.");
                    return;
                }
    
                // Adjust availableTokens accordingly
                prop.name = newName;
                prop.location = newLoc;
                prop.totalPrice = newPrice;
                prop.availableTokens = newTokens - soldTokens;
                prop.totalTokens = newTokens;
    
                JOptionPane.showMessageDialog(frame, "Property updated successfully!");
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input: " + ex.getMessage());
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    

    public static void investorDashboard(User investor) {
        JFrame frame = new JFrame("Investor Dashboard");
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(0, 1));
    
        JButton viewProps = new JButton("View Properties");
        JButton buyTokens = new JButton("Buy Tokens");
        JButton viewMyTokens = new JButton("View My Tokens");
        JButton contactSupport = new JButton("Contact Support"); // New Button
        JButton logout = new JButton("Logout");
        JButton updatePwd = new JButton("Update Password");
        JButton resellToken = new JButton("Resell Token");
        resellToken.addActionListener(e -> listTokenForResaleUI(investor));

        JButton buyFromResale = new JButton("Buy From Resale");
        buyFromResale.addActionListener(e -> buyTokenFromResaleUI(investor));

        viewProps.addActionListener(e -> viewPropertiesUI());
        buyTokens.addActionListener(e -> buyTokensUI(investor));
        viewMyTokens.addActionListener(e -> viewMyTokensUI(investor));
        contactSupport.addActionListener(e -> contactSupportUI(investor)); // Action
        updatePwd.addActionListener(e -> updatePasswordUI(investor));
        logout.addActionListener(e -> {
            frame.dispose();
            showRoleSelection();
        });
    
        frame.add(viewProps);
        frame.add(buyTokens);
        frame.add(viewMyTokens);
        frame.add(resellToken);
        frame.add(buyFromResale);
        frame.add(updatePwd);  
        frame.add(contactSupport);
        frame.add(logout);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void listTokenForResaleUI(User investor) {
        List<Token> myTokens = investorTokens.getOrDefault(investor.id, new ArrayList<>());
        if (myTokens.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have no tokens to resell.");
            return;
        }

        String[] tokenOptions = myTokens.stream()
                .map(t -> "Token ID: " + t.tokenId + " (Property ID: " + t.propertyId + ")")
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(null, "Select Token to Resell:",
                "Resell Token", JOptionPane.PLAIN_MESSAGE, null, tokenOptions, tokenOptions[0]);

        if (selected == null) return;

        int selectedIndex = Arrays.asList(tokenOptions).indexOf(selected);
        Token selectedToken = myTokens.get(selectedIndex);

        String priceStr = JOptionPane.showInputDialog("Enter Resale Price:");
        if (priceStr == null || priceStr.isEmpty()) return;

        try {
            double price = Double.parseDouble(priceStr);
            resaleListings.add(new TokenResale(selectedToken, investor.id, price));
            myTokens.remove(selectedToken);
            JOptionPane.showMessageDialog(null, "Token listed for resale.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid price.");
        }
    }
    public static void buyTokenFromResaleUI(User buyer) {
        if (resaleListings.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tokens available for resale.");
            return;
        }

        String[] resaleOptions = resaleListings.stream()
                .map(r -> "Token ID: " + r.token.tokenId +
                          ", Property: " + propertyMap.get(r.token.propertyId).name +
                          ", Price: ₹" + r.resalePrice +
                          ", Seller: " + getUsernameById(r.sellerId))
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(null, "Select token to buy:",
                "Buy From Resale", JOptionPane.PLAIN_MESSAGE, null, resaleOptions, resaleOptions[0]);

        if (selected == null) return;

        int selectedIndex = Arrays.asList(resaleOptions).indexOf(selected);
        TokenResale chosenSale = resaleListings.get(selectedIndex);

        if (chosenSale.sellerId == buyer.id) {
            JOptionPane.showMessageDialog(null, "You cannot buy your own token.");
            return;
        }

        resaleListings.remove(chosenSale);
        investorTokens.computeIfAbsent(buyer.id, k -> new ArrayList<>()).add(chosenSale.token);
        chosenSale.token.ownerId = buyer.id;

        JOptionPane.showMessageDialog(null, "Token purchased successfully!");
    }

    public static void contactSupportUI(User investor) {
        JFrame frame = new JFrame("Contact Support / Submit Query");
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
    
        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JTextField subjectField = new JTextField();
        JTextArea messageArea = new JTextArea(5, 20);
        JButton submitBtn = new JButton("Submit");
    
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Message:"));
        inputPanel.add(new JScrollPane(messageArea));
    
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(submitBtn, BorderLayout.SOUTH);
    
        submitBtn.addActionListener(e -> {
            String subject = subjectField.getText().trim();
            String message = messageArea.getText().trim();
    
            if (subject.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }
    
        

            supportQueries.add(new SupportQuery(investor.username, subject, message));

    
            JOptionPane.showMessageDialog(frame, "Thank you for reaching out! We'll get back to you shortly.");
            frame.dispose();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    


    public static void updatePasswordUI(User currentUser) {
        JFrame frame = new JFrame("Update Password");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));
    
        JPasswordField oldPassField = new JPasswordField();
        JPasswordField newPassField = new JPasswordField();
    
        JButton updateBtn = new JButton("Update");
    
        frame.add(new JLabel("Old Password:"));
        frame.add(oldPassField);
        frame.add(new JLabel("New Password:"));
        frame.add(newPassField);
        frame.add(new JLabel());
        frame.add(updateBtn);
    
        updateBtn.addActionListener(e -> {
            String oldPwd = new String(oldPassField.getPassword());
            String newPwd = new String(newPassField.getPassword());
    
            if (!currentUser.password.equals(oldPwd)) {
                JOptionPane.showMessageDialog(frame, "Old password is incorrect.");
            } else if (newPwd.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "New password cannot be empty.");
            } else {
                currentUser.password = newPwd;
                JOptionPane.showMessageDialog(frame, "Password updated successfully.");
                frame.dispose();
            }
        });
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
    
    // Add this inside the TokenVestAppSwing class
    public static void buyTokensUI(User investor) {
        String idStr = JOptionPane.showInputDialog("Enter Property ID to buy tokens for:");
        if (idStr == null || idStr.isEmpty()) return;
    
        try {
            int propertyId = Integer.parseInt(idStr);
            Property property = propertyMap.get(propertyId);
    
            if (property == null) {
                JOptionPane.showMessageDialog(null, "Property not found.");
                return;
            }
    
            if (!"Approved".equalsIgnoreCase(property.kycStatus)) {
                JOptionPane.showMessageDialog(null, "KYC not approved for this property.");
                return;
            }
    
            String tokenStr = JOptionPane.showInputDialog("Enter number of tokens to buy:");
            if (tokenStr == null || tokenStr.isEmpty()) return;
    
            int tokensToBuy = Integer.parseInt(tokenStr);
            if (tokensToBuy <= 0 || tokensToBuy > property.availableTokens) {
                JOptionPane.showMessageDialog(null, "Invalid number of tokens.");
                return;
            }
    
            double tokenPrice = property.getTokenPrice();
            double totalCost = tokenPrice * tokensToBuy;
    
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Confirm purchase of " + tokensToBuy + " tokens for ₹" + totalCost + "?",
                    "Confirm Purchase", JOptionPane.YES_NO_OPTION);
    
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = 0; i < tokensToBuy; i++) {
                    Token token = new Token(investor.id, propertyId, tokenPrice);
                    tokens.add(token);
    
                    investorTokens.computeIfAbsent(investor.id, k -> new ArrayList<>()).add(token);
                }
                property.availableTokens -= tokensToBuy;
    
                // Generate receipt
                String receipt = "------------------- RECEIPT -------------------\n"
                                + "Investor: " + investor.username + "\n"
                                + "Property ID: " + propertyId + "\n"
                                + "Property Name: " + property.name + "\n"
                                + "Tokens Purchased: " + tokensToBuy + "\n"
                                + "Price per Token: ₹" + tokenPrice + "\n"
                                + "Total Cost: ₹" + totalCost + "\n"
                                + "----------------------------------------------\n"
                                + "Thank you for your purchase!";
    
                // Show receipt in a message dialog
                JOptionPane.showMessageDialog(null, receipt, "Purchase Receipt", JOptionPane.INFORMATION_MESSAGE);
            }
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input.");
        }
    }
    


    public static void viewMyTokensUI(User investor) {
        List<Token> myTokens = investorTokens.getOrDefault(investor.id, new ArrayList<>());
    
        if (myTokens.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have not purchased any tokens.");
            return;
        }
    
        Map<Integer, List<Token>> tokensByProperty = new HashMap<>();
        for (Token t : myTokens) {
            tokensByProperty.computeIfAbsent(t.propertyId, k -> new ArrayList<>()).add(t);
        }
    
        StringBuilder sb = new StringBuilder("------- Your Token Holdings -------\n\n");
    
        for (Map.Entry<Integer, List<Token>> entry : tokensByProperty.entrySet()) {
            int propertyId = entry.getKey();
            List<Token> tokens = entry.getValue();
            Property property = propertyMap.get(propertyId);
    
            if (property != null && !tokens.isEmpty()) {
                double pricePerToken = tokens.get(0).tokenValue;
                int quantity = tokens.size();
                double total = pricePerToken * quantity;
    
                sb.append("Property: ").append(property.name).append("\n")
                  .append("Tokens Owned: ").append(quantity).append("\n")
                  .append("Price per Token: ₹").append(pricePerToken).append("\n")
                  .append("Total Paid: ₹").append(total).append("\n\n");
            }
        }
    
        JOptionPane.showMessageDialog(null, sb.toString(), "My Tokens", JOptionPane.INFORMATION_MESSAGE);
    }
}