import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TransactionManager manager = new TransactionManager();

        System.out.println("\ud83d\udce5 Enter transaction data (type 'exit' as Sender ID to stop):");

        while (true) {
            System.out.print("\nEnter Sender ID (or 'exit'): ");
            String input = sc.next();
            if (input.equalsIgnoreCase("exit")) break;
            int senderId = Integer.parseInt(input);

            System.out.print("Enter Receiver ID: ");
            int receiverId = sc.nextInt();

            System.out.print("Enter Amount: ");
            int amount = sc.nextInt();

            System.out.print("Enter Timestamp (yyyy-MM-dd HH:mm): ");
            sc.nextLine(); // flush
            String timestamp = sc.nextLine();

            Transaction t = new Transaction(senderId, receiverId, amount, timestamp);
            manager.addTransaction(t);
        }

        manager.printAllTransactions();
        sc.close();
    }
}
