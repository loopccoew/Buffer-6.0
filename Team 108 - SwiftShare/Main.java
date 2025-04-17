import java.util.*;
// Class representing a contact
class Contact {
    String name;
    String contactNumber;
    String ID;

    // Constructor for initializing contact details
    public Contact(String name, String contactNumber) {
        this.ID = IDGenerator.generateMemberID(); // Generate unique ID for the contact
        this.name = name;
        this.contactNumber = contactNumber;
    }
}

// Class representing a member
class Member {
    String name;
    String contactNumber;
    String ID;
    double amountDue;
    boolean isPaid;

    // Constructor for initializing member details
    public Member(String ID, String name, String contactNumber) {
        this.ID = ID; // Unique ID for the member
        this.name = name;
        this.contactNumber = contactNumber;
        this.amountDue = 0.0; // Default amount due is 0
        this.isPaid = false; // Default payment status is unpaid
    }
}

// Utility class for generating unique IDs
class IDGenerator {
    private static int id_generator = 1000; // Starting ID value
    // Method to generate a unique member ID
    public static String generateMemberID() {
        return "M" + (id_generator++);
    }
}

// Class representing the payment system
class PaymentSystem {
    private int groupNumber; // Group number for the payment system
    private List<Member> members = new ArrayList<>(); // List to store members
    private double totalAmount; // Total amount to be allocated among members

    // List of predefined contacts
    private List<Contact> contactList = Arrays.asList(
        new Contact("Riya", "9283746572"),
        new Contact("Rhea", "9184736234"),
        new Contact("Chaitrali", "9183712342"),
        new Contact("Diya", "9278475621"),
        new Contact("Neha", "9567231234"),
        new Contact("Harshada", "9038473621"),
        new Contact("Laxmi", "9087897654"),
        new Contact("Bhoomi", "9603847361"),
        new Contact("Trupti", "9177342342"),
        new Contact("Vedika", "9234541234")
    );

    public String id; // Temporary variable to store ID during member creation
    // Method to get the list of members
    public List<Member> getMembers() {
        return members;
    }

    // Method to collect member information
    public void collectMemberInfo(int numMembers) {
        Set<String> selectedContactIDs = new HashSet<>(); // To track already selected contacts
        Scanner scanner = new Scanner(System.in);

        // Loop for collecting details of each member
        for (int i = 0; i < numMembers; i++) {
            System.out.println("\nEnter details for member " + (i + 1) + ".");
            System.out.print("\nDo you want to add from contact list? (yes/no): ");
            String fromList = scanner.nextLine();
            String name, contactNumber;

            if (fromList.equalsIgnoreCase("yes")) {
                // Displaying the contact list
                System.out.println("\n                  MY CONTACTS:                ");
                System.out.println("-----------------------------------------------------------------");
                System.out.printf("%-18s %-20s %-18s\n", "ID", "Name", "Contact Number");
                System.out.println("-----------------------------------------------------------------");
                for (Contact c : contactList) {
                    System.out.printf("%-18s %-20s %-18s\n", c.ID, c.name, c.contactNumber);
                }
                Contact selectedContact = null;

                // Loop to select a contact from the list
                while (true) {
                    System.out.print("\nSelect member ID for member " + (i + 1) + ": ");
                    String choice = scanner.nextLine().trim();

                    // Find the contact with the given ID
                    for (Contact contact : contactList) {
                        if (contact.ID.equalsIgnoreCase(choice)) {
                            selectedContact = contact;
                            break;
                        }
                    }

                    if (selectedContact == null) {
                        System.out.println("Invalid ID. Please try again.");
                        continue;
                    }

                    // Check if the contact is already added
                    if (selectedContactIDs.contains(selectedContact.ID)) {
                        System.out.println("This member is already added to the group.");
                        selectedContact = null;
                        continue;
                    }

                    selectedContactIDs.add(selectedContact.ID); // Mark contact as selected
                    break;
                }

                id = selectedContact.ID;
                name = selectedContact.name;
                contactNumber = selectedContact.contactNumber;

                // Validate contact number
                if (!isValidContactNumber(contactNumber)) {
                    System.out.println("Invalid contact number.");
                    contactNumber = getValidContactNumber(scanner);
                }
            } else {
                // Collect details manually
                id = IDGenerator.generateMemberID(); // Generate a new ID
                System.out.print("Name: ");
                name = scanner.nextLine();
                contactNumber = getValidContactNumber(scanner); // Validate contact number
            }

            // Add member to the list
            members.add(new Member(id, name, contactNumber));
        }
    }

    // Method to get a valid contact number
    private String getValidContactNumber(Scanner scanner) {
        while (true) {
            System.out.print("Contact Number: 91+ ");
            String contactNumber = scanner.nextLine().trim();
            if (isValidContactNumber(contactNumber)) {
                return contactNumber; // Return valid contact number
            } else {
                System.out.println("Invalid contact number. Try again!");
            }
        }
    }

    // Method to validate a contact number
    private boolean isValidContactNumber(String number) {
        return number.matches("\\d{10}"); // Check if the number has exactly 10 digits
    }

    // Method to display member data
    public void displayMemberData() {
        System.out.println("\n               Members' Data:                ");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s\n", "ID", "Name", "Contact Number");
        System.out.println("------------------------------------------------------------------");
        for (Member member : members) {
            System.out.printf("%-20s %-20s %-20s\n", member.ID, member.name, member.contactNumber);
        }
        System.out.println("-------------------------------------------------------------------");
    }

    // Method to allocate the total amount among members
    public void allocateAmount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the total amount to be paid: ");
        totalAmount = scanner.nextDouble(); // Input total amount
        scanner.nextLine();
        System.out.print("\nIs the amount same for all members (yes/no)? ");
        String sameAmount = scanner.nextLine();

        if (sameAmount.equalsIgnoreCase("yes")) {
            // Allocate equal amount to all members
            double equalAmount = totalAmount / members.size();
            for (Member member : members) {
                member.amountDue = equalAmount;
            }
        } else {
            // Allocate custom amounts to members
            double sumAmount = 0.0;
            while (sumAmount != totalAmount) {
                sumAmount = 0.0; // Reset sum
                for (Member member : members) {
                    System.out.print(member.name + ", Enter the amount: ");
                    double amount = scanner.nextDouble();
                    member.amountDue = amount;
                    sumAmount += amount; // Add to total
                }
                if (sumAmount != totalAmount) {
                    System.out.println("\nTotal amount does not match :( :( \nTry again.");
                }
            }
        }
        displayAmountTable(); // Display the payment table
    }

    // Method to display the payment table
    public void displayAmountTable() {
        System.out.println("\n                     Payment Distribution:            ");
        System.out.println("---------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-10s %-10s\n", "ID", "Name", "Amount", "Status");
        System.out.println("---------------------------------------------------------------------");
        for (Member member : members) {
            System.out.printf("%-20s %-20s %-10.2f %-10s\n", member.ID, member.name, member.amountDue, "Unpaid");
        }
        System.out.println("---------------------------------------------------------------------");
    }

    public void collectPayments() {
        Scanner scanner = new Scanner(System.in);
        boolean allPaid = false;

        while (!allPaid) {
            allPaid = true; // Assume all members have paid
            for (Member member : members) {
                if (!member.isPaid) { // Check if the member has not paid
                    System.out.print(member.name + ", do you want to pay? (yes/no): ");
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("yes")) {
                        member.isPaid = true; // Mark as paid
                        System.out.println(member.name + " has paid.");
                    } else {
                        System.out.println(member.name + " has not paid.");
                        allPaid = false; // Not all members have paid
                    }
                }
            }

            if (!allPaid) {
                handleUnpaidMembers(scanner); // Handle unpaid members
            }
        }

        displayPaymentStatus(); // Display the payment status
    }

    private void handleUnpaidMembers(Scanner scanner) {
        for (Member member : members) {
            if (!member.isPaid) { // Check if the member has not paid
                System.out.print("Anyone wants to pay for " + member.name + "? (yes/no): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter name/contact to search: ");
                    String searchTerm = scanner.nextLine();
                    Member selected = searchMember(searchTerm); // Search for the member
                    if (selected != null) {
                        System.out.print("Does " + selected.name + " agree? (yes/no): ");
                        if (scanner.nextLine().equalsIgnoreCase("yes")) {
                            selected.isPaid = true; // Mark as paid
                            member.isPaid = true; // Mark the unpaid member as paid
                            System.out.println(selected.name + " has paid for " + member.name);
                        } else {
                            System.out.println("Declined.");
                        }
                    } else {
                        System.out.println("No match found.");
                    }
                }
            }
        }
    }

    private Member searchMember(String searchTerm) {
        for (Member member : members) {
            // Search by name or contact number
            if (member.name.equalsIgnoreCase(searchTerm) || member.contactNumber.equals(searchTerm)) {
                return member;
            }
        }
        return null; // Return null if no match is found
    }

    public void displayPaymentStatus() {
        System.out.println("\n                   Payment Status:                  ");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-10s %-10s\n", "ID", "Name", "Amount", "Status");
        System.out.println("-------------------------------------------------------------");
        for (Member member : members) {
            String status = member.isPaid ? "Paid" : "Not Paid"; // Determine payment status
            System.out.printf("%-20s %-20s %-10.2f %-10s\n", member.ID, member.name, member.amountDue, status);
        }
        System.out.println("-------------------------------------------------------------");
    }

    public void transferFunds(String groupName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nTotal Amount is successfully collected!!!");
        System.out.print("Do you want to transfer the full amount? (yes/no): "); //conformation message
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            System.out.print("Enter the name of the receiver: ");
            String receiverName = scanner.nextLine();
            String receiverContact;
            while (true) {
                System.out.print("Enter the receiver's contact number: 91+ ");
                receiverContact = scanner.nextLine();
                if (isValidContactNumber(receiverContact)) {
                    break; // Exit loop if contact number is valid
                } else {
                    System.out.println("Invalid contact number. Please try again.");
                }
            }

            System.out.println("\nTotal amount has been transferred to " + receiverName + " successfully!");
            System.out.println("With the successful transaction, "+ groupName +" Group has been dissolved!");
            System.out.println();
            System.out.println("Thank you for using the SwiftShare Payment System !");
        } else {
            System.out.println("Transaction cancelled.");
        }
    }
}

// Class to manage groups
class Groups {
    private HashMap<String, List<Member>> pastGroups = new HashMap<>(); // Store past groups

    // Menu to interact with the user
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display menu options with a box
            System.out.println("╔══════════════════════════════════════════════════╗");
            System.out.println("║                      MENU                        ║");
            System.out.println("╠══════════════════════════════════════════════════╣");
            System.out.println("║  1. Create New Group                             ║");
            System.out.println("║  2. View Previous Groups                         ║");
            System.out.println("║  0. Exit                                         ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Create a new group
                    System.out.print("Enter group name: ");
                    String groupName = scanner.nextLine();

                    System.out.print("Enter number of members: ");
                    int numMembers = scanner.nextInt();
                    scanner.nextLine();

                    PaymentSystem ps = new PaymentSystem(); // Create a new payment system
                    ps.collectMemberInfo(numMembers); // Collect member information
                    ps.displayMemberData(); // Display member data
                    ps.allocateAmount(); // Allocate the total amount
                    ps.collectPayments(); // Collect payments
                    ps.transferFunds(groupName); // Transfer funds

                    pastGroups.put(groupName, ps.getMembers()); // Save the group
                    break;

                case 2:
                    // View previous groups
                    if (pastGroups.isEmpty()) {
                        System.out.println("\nNo previous groups found.");
                        break;
                    }

                    for (Map.Entry<String, List<Member>> entry : pastGroups.entrySet()) {
                        System.out.println("\nGroup: " + entry.getKey());
                        List<Member> list = entry.getValue();
                        System.out.println("Total Members: " + list.size());

                        System.out.println("----------------------------------------------------------------------------");
                        System.out.printf("%-18s %-20s %-20s %-10s\n", "ID", "Name", "Contact", "Amount Paid");
                        System.out.println("----------------------------------------------------------------------------");

                        for (Member m : list) {
                            System.out.printf("%-18s %-20s %-20s %-10.2f\n", m.ID, m.name, m.contactNumber, m.amountDue);
                        }

                        System.out.println("----------------------------------------------------------------------------");
                    }
                    break;

                case 0:
                    // Exit the system
                    System.out.println("\nEXITING SYSTEM.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }
}

// Main class to run the program
public class Main {
    public static void main(String[] args) {
        System.out.println();
        System.out.println();
        System.out.println("~~~~~~~~~~~~  SWIFT-SHARE PAYMENT SYSTEM!!!  ~~~~~~~~~~~");
        System.out.println("         -Settle up in seconds with SwiftShare-        \n ");
        Groups groupManager = new Groups(); // Create a group manager
        groupManager.menu(); // Start the menu
    }
}
