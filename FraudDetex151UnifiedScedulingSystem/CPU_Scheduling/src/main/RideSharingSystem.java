package main;

import Scheduling.Driver;

import java.util.*;

public class RideSharingSystem {
    private Map<String, Queue<Driver>> areaDrivers;
    private Set<String> activeRides;

    public RideSharingSystem() {
        areaDrivers = new HashMap<>();
        activeRides = new HashSet<>();
    }

    public void addDriver(String name, String location) {
        location = location.trim();
        if (name.isEmpty() || location.isEmpty()) {
            System.out.println("[ERROR] Driver name and location must not be empty.");
            return;
        }

        areaDrivers.putIfAbsent(location, new LinkedList<>());
        areaDrivers.get(location).add(new Driver(name, location, true));
        System.out.println("[INFO] Added Driver " + name + " to area " + location);
    }

    public void requestRide(String pickup, String drop) {
        pickup = pickup.trim();
        drop = drop.trim();

        if (pickup.equalsIgnoreCase(drop)) {
            System.out.println("[ERROR] Pickup and drop location cannot be the same.");
            return;
        }

        if (activeRides.contains(pickup)) {
            System.out.println("[INFO] Ride already in progress for this pickup area: " + pickup);
            return;
        }

        Queue<Driver> drivers = areaDrivers.getOrDefault(pickup, new LinkedList<>());
        while (!drivers.isEmpty()) {
            Driver driver = drivers.poll();
            if (driver.isAvailable()) {
                driver.setAvailable(false);
                activeRides.add(pickup);
                System.out.println("[INFO] Ride assigned to driver: " + driver.getName() +
                        " (Pickup: " + pickup + ", Drop: " + drop + ")");
                return;
            }
        }

        System.out.println("[INFO] No drivers available in area: " + pickup);
    }

    public void cancelRide(String pickup) {
        pickup = pickup.trim();
        if (activeRides.contains(pickup)) {
            activeRides.remove(pickup);
            System.out.println("[INFO] Ride cancelled for pickup area: " + pickup);
        } else {
            System.out.println("[INFO] No active ride found for pickup area: " + pickup);
        }
    }

    public void run(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Ride Sharing System ---");
            System.out.println("1. Add Driver");
            System.out.println("2. Request Ride");
            System.out.println("3. Cancel Ride");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int option;
            try {
                option = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] Invalid input.");
                scanner.nextLine();
                continue;
            }

            switch (option) {
                case 1:
                    System.out.print("Enter driver name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter area: ");
                    String area = scanner.nextLine().trim();
                    addDriver(name, area);
                    break;

                case 2:
                    System.out.print("Enter pickup area: ");
                    String pickup = scanner.nextLine();
                    System.out.print("Enter drop location: ");
                    String drop = scanner.nextLine();
                    requestRide(pickup, drop);
                    break;

                case 3:
                    System.out.print("Enter pickup area to cancel ride: ");
                    String cancelArea = scanner.nextLine();
                    cancelRide(cancelArea);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("[ERROR] Invalid option.");
            }
        }
    }
}
