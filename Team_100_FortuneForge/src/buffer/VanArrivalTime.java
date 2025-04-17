package buffer;

import java.time.LocalTime;
import java.time.Duration;
import java.util.Scanner;

public class VanArrivalTime {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press Enter when you want to request the van:");
        scanner.nextLine();

        // Save the moment the van was requested
        LocalTime vanRequestedTime = LocalTime.now();
        int estimatedArrivalMinutes = 20;

        System.out.println("Van requested at: " + vanRequestedTime);
        System.out.println("Estimated arrival time: " + vanRequestedTime.plusMinutes(estimatedArrivalMinutes));

        while (true) {
            System.out.println("\nPress Enter to refresh or type 'exit' to quit:");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting tracker. Be SAFE!!!");
                break;
            }

            LocalTime currentTime = LocalTime.now();
            long minutesPassed = Duration.between(vanRequestedTime, currentTime).toMinutes();
            long minutesRemaining = estimatedArrivalMinutes - minutesPassed;

            if (minutesRemaining <= 0) {
                System.out.println("\n\nVan has arrived! Please look out for van nearby.....");
            } else {
                System.out.println("Van will arrive in " + minutesRemaining + " minute(s).");
            }
        }

        scanner.close();
    }
}



