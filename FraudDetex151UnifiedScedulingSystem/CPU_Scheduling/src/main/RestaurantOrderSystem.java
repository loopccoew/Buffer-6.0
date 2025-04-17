package main;

import Scheduling.Order;
import java.util.*;

public class RestaurantOrderSystem {
    private PriorityQueue<Order> orderQueue;
    private Stack<Order> platingStack;

    public RestaurantOrderSystem() {
        // Orders with shortest preparation time first
        orderQueue = new PriorityQueue<>();
        platingStack = new Stack<>();
    }

    public void placeOrder(Order order) {
        orderQueue.add(order);
        System.out.println("[INFO] Placed Order: " + order);
    }

    public void cancelOrder(String orderName) {
        boolean removed = orderQueue.removeIf(order -> order.getName().equalsIgnoreCase(orderName));
        if (removed) {
            System.out.println("[INFO] Cancelled Order: " + orderName);
        } else {
            System.out.println("[INFO] Order not found: " + orderName);
        }
    }

    public void prepareNextOrder() {
        if (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            platingStack.push(order);
            System.out.println("[INFO] Prepared Order: " + order);
        } else {
            System.out.println("[INFO] No orders to prepare.");
        }
    }

    public void printPlatingStack() {
        System.out.println("[STATE] Current Plating Stack:");
        for (Order order : platingStack) {
            System.out.println(order);
        }
    }

    public void run(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Restaurant Order System ---");
            System.out.println("1. Place Order");
            System.out.println("2. Cancel Order");
            System.out.println("3. Prepare Next Order");
            System.out.println("4. Print Plating Stack");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    System.out.print("Enter order item: ");
                    String item = scanner.nextLine();

                    System.out.print("Enter table number (positive integer): ");
                    int table = -1;
                    while (table < 0) {
                        try {
                            table = Integer.parseInt(scanner.nextLine());
                            if (table < 0) {
                                System.out.println("Table number must be a positive integer. Try again.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input! Please enter a valid table number.");
                        }
                    }

                    System.out.print("Enter preparation time in minutes (positive integer): ");
                    int prepTime = -1;
                    while (prepTime <= 0) {
                        try {
                            prepTime = Integer.parseInt(scanner.nextLine());
                            if (prepTime <= 0) {
                                System.out.println("Preparation time must be a positive integer.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input! Please enter a valid number.");
                        }
                    }

                    if (!item.matches("[a-zA-Z0-9 ]+")) {
                        System.out.println("Invalid item name! Special characters are not allowed.");
                    } else {
                        placeOrder(new Order(item, table, prepTime));
                    }
                    break;

                case 2:
                    System.out.print("Enter order name to cancel: ");
                    String orderName = scanner.nextLine();
                    cancelOrder(orderName);
                    break;

                case 3:
                    prepareNextOrder();
                    break;

                case 4:
                    printPlatingStack();
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}