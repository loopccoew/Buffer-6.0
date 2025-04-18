import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static class Item {
        String name;
        double price;
        int quantityOrdered;
        int stock;

        Item(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.quantityOrdered = 0;
            this.stock = stock;
        }

        boolean order(int quantity) {
            if (quantity <= stock) {
                quantityOrdered += quantity;
                stock -= quantity;
                return true;
            } else {
                System.out.println("Only " + stock + " units available for " + name + ".");
                return false;
            }
        }

        void restock(int quantity) {
            stock += quantity;
        }

        boolean isAvailable() {
            return stock > 0;
        }
    }

    static class Order {
        String username;
        String role;
        Map<Item, Integer> items = new HashMap<>();
        double totalAmount;
        int priorityLevel;
        String status = "Pending";

        Order(String username, String role) {
            this.username = username;
            this.role = role;
            this.priorityLevel = assignPriority(role);
        }

        static int assignPriority(String role) {
            role = role.toLowerCase();
            switch (role) {
                case "placement officer": return 1;
                case "staff": return 2;
                case "student": return 3;
                default: return 4;
            }
        }

        void addItem(Item item, int quantity) {
            items.put(item, quantity);
            item.order(quantity);
            totalAmount += item.price * quantity;
        }
    }

    static class CustomQueue {
        PriorityQueue<Order> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o.priorityLevel));

        void enqueue(Order order) {
            queue.offer(order);
        }

        Order pollOrder() {
            return queue.poll();
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }

        List<Order> getAllOrders() {
            return new ArrayList<>(queue);
        }
    }

    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin123";

    static Map<String, String> users = new HashMap<>();
    static Map<String, String> userRoles = new HashMap<>();
    static Map<String, List<Order>> userOrders = new HashMap<>();
    static ArrayList<Item> menuItems = new ArrayList<>();
    static CustomQueue orderQueue = new CustomQueue();
    static double totalRevenue = 0;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Canteen Management System!");
        //adminLogin();
        adminPanel();
        

        // Predefined menu
        menuItems.add(new Item("Samosa", 10, 50));
        menuItems.add(new Item("Poha", 15, 30));
        menuItems.add(new Item("Coffee", 20, 40));
        menuItems.add(new Item("Vada Pav", 12, 60));
        menuItems.add(new Item("Tea", 8, 100));

        while (true) {
            userLoginOrRegister();
        }
    }

    static void adminLogin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("Admin login successful!\n");
            
        } else {
            System.out.println("Invalid admin credentials!");
            System.exit(0);
        }
    }

    static void userLoginOrRegister() {
        System.out.print("\nEnter user name: ");
        String username = scanner.nextLine();

        System.out.print("Are you a new user? (yes/no): ");
        String isNew = scanner.nextLine();

        if (isNew.equalsIgnoreCase("yes")) {
            System.out.print("Create password: ");
            String password = scanner.nextLine();
            users.put(username, password);

            System.out.print("Enter your role (Student/Staff/Placement Officer): ");
            String role = scanner.nextLine();
            userRoles.put(username, role);
            System.out.println("Registration successful!");
        } else {
            if (!users.containsKey(username)) {
                System.out.println("User not found! Please register first.");
                return;
            }
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (!users.get(username).equals(password)) {
                System.out.println("Incorrect password.");
                return;
            }
        }

        showUserMenu(username);
    }

    static void showUserMenu(String username) {
        while (true) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. Show Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View Top 3 Items");
            System.out.println("4. View My Order History");
            System.out.println("5. Exit");
            System.out.println("6. Admin Panel");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> showMenu();
                case 2 -> placeOrder(username);
                case 3 -> showTop3Items();
                case 4 -> showUserOrderHistory(username);
                case 5 -> {
                    System.out.print("Login as another user? (yes/no): ");
                    String ans = scanner.nextLine();
                    if (ans.equalsIgnoreCase("yes")) return;
                    else {
                        System.out.println("Thank you! Visit again.");
                        System.exit(0);
                    }
                }
                case 6 -> adminPanel();
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void showMenu() {
        System.out.println("Menu Items:");
        for (int i = 0; i < menuItems.size(); i++) {
            Item item = menuItems.get(i);
            if (item.isAvailable()) {
                System.out.println((i + 1) + ". " + item.name + " - ₹" + item.price + " (Stock: " + item.stock + ")");
            }
        }
    }

    static void placeOrder(String username) {
        showMenu();
        Map<Integer, Integer> orderData = new HashMap<>();

        while (true) {
            System.out.print("Enter item number and quantity (e.g. 1 2): ");
            int itemNum = scanner.nextInt();
            int qty = scanner.nextInt();
            scanner.nextLine();
            orderData.put(itemNum, qty);

            System.out.print("Add more items? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("n")) break;
        }

        String role = userRoles.get(username);
        Order order = new Order(username, role);

        for (var entry : orderData.entrySet()) {
            int index = entry.getKey() - 1;
            if (index >= 0 && index < menuItems.size()) {
                Item item = menuItems.get(index);
                int qty = entry.getValue();
                if (qty <= item.stock) {
                    order.addItem(item, qty);
                } else {
                    System.out.println("Skipped " + item.name + " due to low stock.");
                }
            }
        }

        if (order.totalAmount > 0) {
            orderQueue.enqueue(order);
            userOrders.putIfAbsent(username, new ArrayList<>());
            userOrders.get(username).add(order);
            totalRevenue += order.totalAmount;
            System.out.println("Order placed! Total: ₹" + order.totalAmount);
        } else {
            System.out.println("No valid items in order.");
        }
    }

    static void showTop3Items() {
        System.out.println("Top 3 Most Ordered Items:");
        menuItems.stream()
                .sorted((a, b) -> b.quantityOrdered - a.quantityOrdered)
                .limit(3)
                .forEach(i -> System.out.println(i.name + " - Ordered: " + i.quantityOrdered));
    }

    static void showUserOrderHistory(String username) {
        List<Order> orders = userOrders.get(username);
        if (orders == null || orders.isEmpty()) {
            System.out.println("No order history.");
        } else {
            for (Order o : orders) {
                System.out.println("Order: ₹" + o.totalAmount + " | Status: " + o.status);
                for (var entry : o.items.entrySet()) {
                    System.out.println("  " + entry.getKey().name + " x" + entry.getValue());
                }
            }
        }
    }

    static void adminPanel() {
    	System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("Admin login successful!\n");
        } else {
            System.out.println("Invalid admin credentials!");
            System.exit(0);
        }
    	
        while (true) {
            System.out.println("\n===== ADMIN PANEL =====");
            System.out.println("1. Add New Menu Item");
            System.out.println("2. Restock Items");
            System.out.println("3. View Users and Their Orders");
            System.out.println("4. Serve Orders by Priority");
            System.out.println("5. Login as User");
            System.out.print("Select Option: ");
            int opt = Integer.parseInt(scanner.nextLine());

            switch (opt) {
                case 1 -> addNewMenuItem();
                case 2 -> restockItems();
                case 3 -> viewUserDetails();
                case 4 -> serveOrders();
                case 5 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void addNewMenuItem() {
        System.out.print("Item name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ₹");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        menuItems.add(new Item(name, price, stock));
        System.out.println("Item added!");
    }

    static void restockItems() {
        
        showMenu();
        System.out.print("Item number: ");
        int index = Integer.parseInt(scanner.nextLine());
        System.out.print("Quantity to add: ");
        int qty = Integer.parseInt(scanner.nextLine());

        if (index >= 1 && index <= menuItems.size()) {
            menuItems.get(index - 1).restock(qty);
            System.out.println("Stock updated!");
        } else {
            System.out.println("Invalid item.");
        }
    }

    static void viewUserDetails() {
        System.out.println("Users and Orders:");
        userOrders.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> Order.assignPriority(userRoles.get(e.getKey()))))
                .forEach(e -> {
                    String user = e.getKey();
                    System.out.println("User: " + user + " (Role: " + userRoles.get(user) + ")");
                    for (Order order : e.getValue()) {
                        System.out.println("  Total: ₹" + order.totalAmount + " | Status: " + order.status);
                        for (Map.Entry<Item, Integer> entry : order.items.entrySet()) {
                            System.out.println("    " + entry.getKey().name + " x" + entry.getValue());
                        }
                    }
                });
    }

    static void serveOrders() {
        List<Order> pendingOrders = orderQueue.getAllOrders().stream()
                .filter(o -> !o.status.equalsIgnoreCase("Served"))
                .sorted(Comparator.comparingInt(o -> o.priorityLevel))
                .collect(Collectors.toList());

        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders.");
            return;
        }

        for (int i = 0; i < pendingOrders.size(); i++) {
            Order o = pendingOrders.get(i);
            System.out.println((i + 1) + ". " + o.username + " (" + o.role + ") - ₹" + o.totalAmount);
            for (Map.Entry<Item, Integer> entry : o.items.entrySet()) {
                System.out.println("    " + entry.getKey().name + " x" + entry.getValue());
            }
            System.out.println("    Status: " + o.status);
        }

        System.out.print("Mark which order as served (number): ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice >= 1 && choice <= pendingOrders.size()) {
            Order selected = pendingOrders.get(choice - 1);
            selected.status = "Served";
            orderQueue.queue.remove(selected); // Remove the order from the priority queue
            System.out.println("Order marked as served and removed from queue!");
        } else {
            System.out.println("Invalid selection.");
        }
    }
}