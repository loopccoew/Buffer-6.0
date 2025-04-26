
package BlockPackage.Cryptocurrency;

import java.util.*;

public class CryptoMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CryptoBlockchain blockchain = new CryptoBlockchain(3);

        List<Account> accounts = new ArrayList<>();

        System.out.println("Welcome to the Cryptocurrency Application!");

        // Step 1: Create Accounts
        System.out.print("Enter number of accounts: ");
        int numAccounts = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numAccounts; i++) {
            System.out.print("Enter account name: ");
            String name = scanner.nextLine();
            System.out.print("Enter initial balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            accounts.add(new Account(name, balance));
        }

        // Step 2: Process Transactions
        System.out.print("Enter number of transactions: ");
        int numTransactions = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numTransactions; i++) {
            System.out.print("Enter sender name: ");
            String sender = scanner.nextLine();
            System.out.print("Enter receiver name: ");
            String receiver = scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            Account senderAcc = null, receiverAcc = null;
            for (Account acc : accounts) {
                if (acc.getName().equalsIgnoreCase(sender)) senderAcc = acc;
                if (acc.getName().equalsIgnoreCase(receiver)) receiverAcc = acc;
            }

            if (senderAcc == null || receiverAcc == null) {
                System.out.println(" Account not found.");
                continue;
            }

            if (senderAcc.getBalance() < amount) {
                System.out.println(" Insufficient balance.");
                continue;
            }

            senderAcc.debit(amount);
            receiverAcc.credit(amount);
            String txId = UUID.randomUUID().toString();
            blockchain.addTransaction(new Transaction(sender, receiver, amount, txId));
            blockchain.addBlock();
            System.out.println(" Transaction ID: " + txId);
        }

        // Final Blockchain and Account Status
        blockchain.printChain();
        System.out.println("\n Final Account Balances:");
        for (Account acc : accounts) {
            System.out.println(acc.getName() + ": ₹" + acc.getBalance());
        }

        scanner.close();
    }
}

// Account class
class Account {
    private final String name;
    private double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void debit(double amount) {
        balance -= amount;
    }

    public void credit(double amount) {
        balance += amount;
    }
}
