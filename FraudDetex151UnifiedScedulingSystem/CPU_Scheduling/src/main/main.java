

package main;

import java.util.Scanner;
import Scheduling.*;


public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nUnified Scheduling System");
            System.out.println("1. Cloud Job Scheduler");
            System.out.println("2. Ride Sharing System");
            System.out.println("3. Sports Match Scheduler");
            System.out.println("4. Call Center Queue System");
            System.out.println("5. Hospital Queue System");
            System.out.println("6. Restaurant Order System");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    CloudJobScheduler cloudScheduler = new CloudJobScheduler();
                    cloudScheduler.run(scanner);
                    break;
                case 2:
                    RideSharingSystem rideSystem = new RideSharingSystem();
                    rideSystem.run(scanner);
                    break;
                case 3:
                    SportsMatchScheduler matchScheduler = new SportsMatchScheduler();
                    matchScheduler.run(scanner);
                    break;
                case 4:
                    CallCenterSystem callSystem = new CallCenterSystem();
                    callSystem.run(scanner);
                    break;
                case 5:
                    HospitalQueueSystem hospitalSystem = new HospitalQueueSystem();
                    hospitalSystem.run(scanner);
                    break;
                case 6:
                    RestaurantOrderSystem restaurantSystem = new RestaurantOrderSystem();
                    restaurantSystem.run(scanner);
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting Unified Scheduling System.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    
}