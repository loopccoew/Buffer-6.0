package com.saferoutepune;

import java.util.*;
import java.io.IOException;
import java.time.LocalTime;

class LawInfo {
    String name;
    long contactnum;
    String description;
    String specialization;

    LawInfo(String name, long contactnum, String description, String specialization) {
        this.name = name;
        this.contactnum = contactnum;
        this.description = description;
        this.specialization = specialization;
    }
}

class Info {
    String name;
    long phoneNum;
    long[] emergencyContacts;
    String email;
    List<LawInfo> relatedLawyers;

    Info(String name, long phoneNum, int contactCount, Scanner sc, String email) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.relatedLawyers = new ArrayList<>();
        this.emergencyContacts = new long[contactCount];

        System.out.println("Enter " + contactCount + " emergency contact numbers:");
        for (int i = 0; i < contactCount; i++) {
            System.out.print("Contact " + (i + 1) + ": ");
            this.emergencyContacts[i] = sc.nextLong();
        }
        sc.nextLine();
    }

    public void display() {
        System.out.println("\n--- User Info ---");
        System.out.println("Name: " + name);
        System.out.println("Phone Number: " + phoneNum);
        System.out.println("Email: " + email);
        System.out.println("Emergency Contacts:");
        for (int i = 0; i < emergencyContacts.length; i++) {
            System.out.println("  " + (i + 1) + ": " + emergencyContacts[i]);
        }

        System.out.println("Contacted Lawyers:");
        for (LawInfo lawyer : relatedLawyers) {
            System.out.println("  - " + lawyer.name + " (" + lawyer.contactnum + ")");
        }
    }

    public void editProfile(Scanner sc) {
        while (true) {
            System.out.println("\n--- Edit Profile ---");
            System.out.println("1. Edit Name\n2. Edit Phone Number\n3. Edit Email\n4. Go Back");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    this.name = sc.nextLine();
                    break;
                case 2:
                    System.out.print("Enter new phone number: ");
                    this.phoneNum = sc.nextLong();
                    sc.nextLine();
                    break;
                case 3:
                    System.out.print("Enter new email: ");
                    this.email = sc.nextLine();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void manageEmergencyContacts(Scanner sc) {
        System.out.println("\nSending Emergency Alert!!!!");
    
        Stack<Long> stack = new Stack<>();
    
        // Push all existing emergency contacts into the stack
        for (long contact : emergencyContacts) {
            stack.push(contact);
        }
    
        // Use another stack to reverse the order so it's sent in original order
        Stack<Long> orderedStack = new Stack<>();
        while (!stack.isEmpty()) {
            orderedStack.push(stack.pop());
        }
    
        // Now send simulated messages
        while (!orderedStack.isEmpty()) {
            long contact = orderedStack.pop();
            System.out.println("Sending SMS to " + contact + ": \"Help me! This is an emergency.\"");
        }
    
        System.out.println("All emergency messages sent.");
    }
    
    

    public void saferRoute(Scanner sc) {
        try {
            SafestRouteFinder routeFinder = new SafestRouteFinder();

            System.out.println("\n=== Pune Safe Route Finder ===");

            boolean isNight = LocalTime.now().isAfter(LocalTime.of(19, 0)) ||
                              LocalTime.now().isBefore(LocalTime.of(6, 0));
            System.out.println("\nCurrent Time: " + LocalTime.now());
            System.out.println("Automatically detected: " + (isNight ? "Night Travel" : "Day Travel"));

            System.out.print("\nOverride time settings? (yes/no): ");
            if (sc.nextLine().equalsIgnoreCase("yes")) {
                System.out.print("Set to Day Travel? (yes/no): ");
                isNight = !sc.nextLine().equalsIgnoreCase("yes");
            }

            System.out.print("\nIs rain expected? (yes/no): ");
            boolean isRaining = sc.nextLine().equalsIgnoreCase("yes");

            System.out.println("\nAvailable Areas: " + String.join(", ", routeFinder.graph.keySet()));

            System.out.print("\nEnter starting location: ");
            String start = sc.nextLine().trim();
            System.out.print("Enter destination: ");
            String end = sc.nextLine().trim();

            List<SafestRouteFinder.RouteOption> routes = routeFinder.findRoutes(start, end, isRaining, isNight);

            System.out.println("\n=== Recommended Routes ===");
            if (routes.isEmpty()) {
                System.out.println("No routes found between these locations.");
            } else {
                for (int i = 0; i < routes.size(); i++) {
                    SafestRouteFinder.RouteOption route = routes.get(i);
                    System.out.printf("\nOption %d:%n  Path: %s%n  Safety: %d%n  Time: %d mins%n  Conditions: %s%n",
                            i + 1,
                            String.join("->", route.path),
                            route.safetyScore,
                            route.timeMinutes,
                            route.conditions);
                }
            }

            System.out.println("\n=== Emergency Contacts ===");
            for (int i = 0; i < this.emergencyContacts.length; i++) {
                System.out.printf("  %d: %d%n", i + 1, this.emergencyContacts[i]);
            }

            if (!this.relatedLawyers.isEmpty()) {
                System.out.println("\n=== Your Legal Contacts ===");
                for (LawInfo lawyer : this.relatedLawyers) {
                    System.out.printf("  %s (%d)%n", lawyer.name, lawyer.contactnum);
                }
            }

        } catch (IOException e) {
            System.out.println("\nError: Could not load route data.");
            System.out.println("Please ensure data/safety_data_updated.csv exists and is properly formatted.");
        }
    }
}

public class SafestRoute {
    static List<LawInfo> lawyerList = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        addPredefinedLawyers();
        System.out.println("Welcome to the Legal Assistance Portal!");

        while (true) {
            System.out.println("\nLogin as:\n1. User\n2. Lawyer\n3. Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                handleUser();
            } else if (choice == 2) {
                handleLawyer();
            } else {
                System.out.println("Thank you for using the portal.");
                break;
            }
        }

        sc.close();
    }

    static void handleUser() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter phone number: ");
        long phoneNum = sc.nextLong();
        sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("How many emergency contacts? ");
        int contactCount = sc.nextInt();

        Info user = new Info(name, phoneNum, contactCount, sc, email);

        while (true) {
            System.out.println("\n1. View All Lawyers\n2. Search Lawyer by Specialization(eg :rape, molestation, criminal law,harrasment,women,498a,divorce)\n3. Add Intrested Lawyers\n4. View Your Details\n5. Edit Profile\n6. Alert Emergency Contacts\n7. Find Safest Route\n8. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                displayAllLawyers();
            } else if (choice == 2) {
                searchLawyers();
            } else if (choice == 3) {
                contactLawyer(user);
            } else if (choice == 4) {
                user.display();
            } else if (choice == 5) {
                user.editProfile(sc);
            } else if (choice == 6) {
                user.manageEmergencyContacts(sc);
            } else if (choice == 7) {
                user.saferRoute(sc);
            } else {
                break;
            }
        }
    }

    static void handleLawyer() {
        System.out.println("Enter lawyer's name:");
        String name = sc.nextLine();

        System.out.println("Enter contact number:");
        long contactnum = sc.nextLong();
        sc.nextLine();

        System.out.println("Enter description:");
        String description = sc.nextLine();

        System.out.println("Enter specialization keywords (comma separated):");
        String specialization = sc.nextLine();

        lawyerList.add(new LawInfo(name, contactnum, description, specialization));
        System.out.println("Lawyer registered successfully!");
    }

    static void addPredefinedLawyers() {
        lawyerList.add(new LawInfo("Advocate Santosh Lonkar", 9876543210L, "Specializes in Family Law, Domestic Violence, Rape, and Women's Rights cases.", "rape, domestic violence, women, family"));
        lawyerList.add(new LawInfo("GAG Lawyers", 9821122334L, "Handles 498A IPC, POCSO, workplace sexual harassment and women protection cases.", "498a, posco, molestation, women"));
        lawyerList.add(new LawInfo("Advocate Manasi Joshi", 9812345678L, "Focused on sexual abuse, harassment, divorce and women's legal aid.", "abuse, harassment, divorce, women"));
        lawyerList.add(new LawInfo("Advocate Sneha Deshmukh", 9765432101L, "Handled high-profile rape and molestation cases. Works with NGOs.", "rape, abuse, molestation, women"));
        lawyerList.add(new LawInfo("Advocate Rahul Sharma", 9765123478L, "15+ years in criminal cases like rape, confinement, molestation.", "rape, molestation, criminal law"));
    }

    static void displayAllLawyers() {
        System.out.println("\n================= All Lawyers =================");
        System.out.printf("%-30s %-20s %-40s %-50s%n", "Name", "Contact", "Specialization", "Description");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (LawInfo i : lawyerList) {
            String shortDesc = i.description.length() > 30 ? i.description.substring(0, 30) + "..." : i.description;
            System.out.printf("%-30s %-20s %-40s %-50s%n", i.name, i.contactnum, i.specialization, shortDesc);
        }
        System.out.println("=============================================================================================");
    }

    static void searchLawyers() {
        System.out.print("Enter specialization keyword to search: ");
        String keyword = sc.nextLine().toLowerCase();

        boolean found = false;
        for (LawInfo lawyer : lawyerList) {
            if (lawyer.specialization.toLowerCase().contains(keyword)) {
                found = true;
                System.out.println("Name: " + lawyer.name);
                System.out.println("Contact: " + lawyer.contactnum);
                System.out.println("Description: " + lawyer.description);
                System.out.println("-----------------------------");
            }
        }

        if (!found) {
            System.out.println("No lawyer found with that specialization.");
        }
    }

    static void contactLawyer(Info user) {
        displayAllLawyers();
        System.out.print("Enter name of lawyer to contact: ");
        String lawyerName = sc.nextLine().toLowerCase();

        for (LawInfo lawyer : lawyerList) {
            if (lawyer.name.toLowerCase().contains(lawyerName)) {
                user.relatedLawyers.add(lawyer);
                System.out.println("Lawyer added to your contact list!");
                return;
            }
        }

        System.out.println("Lawyer not found.");
    }
}
