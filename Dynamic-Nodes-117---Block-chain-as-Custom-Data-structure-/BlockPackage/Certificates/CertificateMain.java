package Certificates;

import blockchain.Block;
import blockchain.Blockchain;
import java.util.*;

public class CertificateMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Blockchain<Certificate> certificateChain = new Blockchain<>();

        while (true) {
            System.out.println("\n🎓 Certificate Blockchain Menu:");
            System.out.println("1. Add Certificate(s)");
            System.out.println("2. View Blockchain");
            System.out.println("3. Search by Student Name");
            System.out.println("4. Reset Blockchain");
            System.out.println("5. Exit");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter number of certificates to add: ");
                    int numCerts = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    List<Certificate> certs = new ArrayList<>();
                    for (int i = 0; i < numCerts; i++) {
                        System.out.print("Student Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Course: ");
                        String course = scanner.nextLine();
                        System.out.print("Grade: ");
                        String grade = scanner.nextLine();

                        certs.add(new Certificate(name, course, grade));
                    }

                    certificateChain.addBlock(certs);
                    System.out.println("Certificates added to blockchain.");
                    break;

                case 2:
                    certificateChain.printChain();
                    break;

                case 3:
                    System.out.print("Enter student name to search: ");
                    String name = scanner.nextLine();
                    boolean found = false;

                    for (Block<Certificate> block : certificateChain.getChain()) {
                        for (Certificate c : block.data) {
                            if (c.getStudentName().equalsIgnoreCase(name)) {
                                System.out.println(c);
                                found = true;
                            }
                        }
                    }

                    if (!found) {
                        System.out.println("No certificates found for student: " + name);
                    }
                    break;

                case 4:
                    certificateChain = new Blockchain<>();
                    System.out.println("Blockchain reset.");
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
}
