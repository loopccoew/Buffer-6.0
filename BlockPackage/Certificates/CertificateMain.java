package Certificates;

//import blockchain.Block;
import blockchain.Blockchain;
import java.util.*;

public class CertificateMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Blockchain<Certificate> certificateChain = new Blockchain<>();

        while (true) {
            System.out.println("\n Certificate Blockchain Menu:");
            System.out.println("1. Add Certificate");
            System.out.println("2. View Certificate Blockchain");
            System.out.println("3. Search by Student Name");
            System.out.println("4. Reset Blockchain");
            System.out.println("5. Exit");
            System.out.println("6. Check Blockchain Integrity");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add only one certificate at a time
                    System.out.print("Student Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Course: ");
                    String course = scanner.nextLine();
                    System.out.print("Grade: ");
                    String grade = scanner.nextLine();

                    Certificate cert = new Certificate(name, course, grade);
                    List<Certificate> singleCertList = new ArrayList<>();
                    singleCertList.add(cert);

                    certificateChain.addBlock(singleCertList);
                    System.out.println("Certificate added to blockchain.");
                    break;

                case 2:
                    certificateChain.printChain();
                    break;

                case 3:
                    System.out.print("Enter student name to search: ");
                    String searchName = scanner.nextLine();
                    certificateChain.searchByItem(searchName);
                    break;

                case 4:
                    certificateChain.clearChain();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                case 6:
                certificateChain.getBlock(1).getData().get(0).setStudentName("Hacker");

                    if (certificateChain.isChainValid()) {
                        System.out.println("Blockchain is valid. No tampering detected.");
                    } else {
                        System.out.println("Blockchain integrity compromised! Tampering detected.");
                    }
                    break;
                

                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
}
