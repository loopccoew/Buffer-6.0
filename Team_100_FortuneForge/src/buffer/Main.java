package buffer;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Buffer buffer = new Buffer();
        VolunteerCityMap volunteerMap = new VolunteerCityMap();

        System.out.println("Who are you?\n1. Victim\n2. Volunteer");
        int role = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (role) {
            case 1:
                Buffer.main(args); 
                break;
            case 2:
            	volunteerMap.main(args); 
                break;
            default:
                System.out.println("Invalid choice.");
        }
        scanner.close();
    }
}
