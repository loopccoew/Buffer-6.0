


import java.util.*;
import java.io.*;
import java.time.LocalDate;

class Transaction {
    String category;
    double amount;
    LocalDate dueDate;

    Transaction(String category, double amount) {
        this(category, amount, null);
    }

    Transaction(String category, double amount, LocalDate dueDate) {
        this.category = category;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public String toString() {
        return category + ": ₹" + amount + (dueDate != null ? " (Due: " + dueDate + ")" : "");
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static HashMap<String, Object> userData = new HashMap<>();
    static LinkedList<Transaction> transactions = new LinkedList<>();
    static Stack<Transaction> undoStack = new Stack<>();
    static Queue<Transaction> upcomingPayments = new LinkedList<>();
    static HashMap<String, Double> budget = new HashMap<>();
    static double walletBalance = 0;

    public static void setupUser() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your monthly income: ₹");
        double income = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Select your profession:");
        System.out.println("1. Student\n2. Engineer\n3. Doctor\n4. Teacher\n5. Businessman");
        System.out.print("Enter your choice: ");
        int professionChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Do you live in (Urban/Rural)? ");
        String location = scanner.nextLine().toLowerCase();

        String profession = getProfession(professionChoice);
        userData.put("name", name);
        userData.put("income", income);
        userData.put("profession", profession);
        userData.put("location", location);
        walletBalance = income;

        suggestBudget(profession, income, location);
    }

    public static String getProfession(int choice) {
        return switch (choice) {
            case 1 -> "Student";
            case 2 -> "Engineer";
            case 3 -> "Doctor";
            case 4 -> "Teacher";
            case 5 -> "Businessman";
            default -> "Other";
        };
    }

    public static void suggestBudget(String profession, double income, String location) {
        double needs = income * 0.50;
        double wants = income * 0.30;
        double savings = income * 0.20;

        if (profession.equals("Student")) {
            needs = income * 0.40;
            wants = income * 0.40;
            savings = income * 0.20;
        } else if (profession.equals("Doctor") || profession.equals("Engineer")) {
            needs = income * 0.45;
            wants = income * 0.35;
            savings = income * 0.20;
        } else if (profession.equals("Businessman")) {
            needs = income * 0.35;
            wants = income * 0.40;
            savings = income * 0.25;
        }

        if (location.equals("urban")) {
            needs += income * 0.05;
            savings -= income * 0.05;
        }

        budget.put("Needs", needs);
        budget.put("Wants", wants);
        budget.put("Savings", savings);

        System.out.println("\nSuggested Budget for " + profession + " (" + location + " area)");
        System.out.println("✅ Needs: ₹" + needs);
        System.out.println("✅ Wants: ₹" + wants);
        System.out.println("✅ Savings: ₹" + savings + "\n");
    }

    public static void addTransaction() {
        System.out.print("Enter category (Food, Rent, Shopping, etc.): ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ₹");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount > walletBalance) {
            System.out.println("❌Transaction declined! Insufficient balance.");
            return;
        }

        Transaction transaction = new Transaction(category, amount);
        transactions.addFirst(transaction);
        undoStack.push(transaction);
        walletBalance -= amount;

        System.out.println("✅ Transaction added: " + category + " - ₹" + amount);
        System.out.println("💰 Remaining Balance: ₹" + walletBalance + "\n");

        suggestInvestments();
    }

    public static void suggestInvestments() {
        double savings = budget.get("Savings");

        if (walletBalance > savings) {
            System.out.println("📈 Investment Suggestion:");
            System.out.println("💼 Consider investing ₹" + (walletBalance * 0.10) + " in Mutual Funds.");
            System.out.println("🏡 Consider saving ₹" + (walletBalance * 0.15) + " for real estate.\n");
        }
    }

    public static void undoTransaction() {
        if (!undoStack.isEmpty()) {
            Transaction lastTransaction = undoStack.pop();
            transactions.removeFirst();
            walletBalance += lastTransaction.amount;
            System.out.println("🔄 Undoing last transaction: " + lastTransaction);
            System.out.println("💰 Updated Balance: ₹" + walletBalance + "\n");
        } else {
            System.out.println("❌ No transactions to undo.\n");
        }
    }

    public static void addUpcomingPayment() {
        System.out.print("Enter bill name: ");
        String billName = scanner.nextLine();

        System.out.print("Enter amount: ₹");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter due date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate dueDate = LocalDate.parse(dateStr);

        if (!dueDate.isAfter(LocalDate.now())) {
            System.out.println("❌ Invalid due date! Please enter a future date.\n");
            return;
        }

        Transaction payment = new Transaction(billName, amount, dueDate);
        upcomingPayments.add(payment);

        System.out.println("📅 Upcoming payment added: " + billName + " - ₹" + amount + " (Due: " + dueDate + ")\n");
    }

    public static void displayTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("❌ No transactions found.\n");
            return;
        }

        System.out.println("\n📝 Transaction History:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.println();
    }

    public static void displayUpcomingPayments() {
        if (upcomingPayments.isEmpty()) {
            System.out.println("❌ No upcoming payments.\n");
            return;
        }

        System.out.println("\n📅 Upcoming Payments:");
        for (Transaction t : upcomingPayments) {
            System.out.println(t);
        }
        System.out.println();
    }

    public static void displayCategoryReport() {
        if (transactions.isEmpty()) {
            System.out.println("❌ No transactions found.\n");
            return;
        }

        HashMap<String, Double> report = new HashMap<>();
        for (Transaction t : transactions) {
            report.put(t.category, report.getOrDefault(t.category, 0.0) + t.amount);
        }

        System.out.println("\n📊 Category-wise Report:");
        for (String cat : report.keySet()) {
            System.out.println("🔹 " + cat + ": ₹" + report.get(cat));
        }
        System.out.println();
    }

    public static void manageTransaction() {
        if (transactions.isEmpty()) {
            System.out.println("❌ No transactions to edit or delete.\n");
            return;
        }

        System.out.println("\n📝 Transactions:");
        int index = 1;
        for (Transaction t : transactions) {
            System.out.println(index + ". " + t);
            index++;
        }

        System.out.print("Enter transaction number to edit/delete: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > transactions.size()) {
            System.out.println("❌ Invalid choice.\n");
            return;
        }

        Transaction selected = transactions.get(choice - 1);
        System.out.println("Selected: " + selected);
        System.out.print("1. Edit\n2. Delete\nChoose option: ");
        int action = scanner.nextInt();
        scanner.nextLine();

        if (action == 1) {
            System.out.print("New category: ");
            String newCat = scanner.nextLine();
            System.out.print("New amount: ₹");
            double newAmt = scanner.nextDouble();
            scanner.nextLine();

            walletBalance += selected.amount;
            selected.category = newCat;
            selected.amount = newAmt;
            walletBalance -= newAmt;

            System.out.println("✅ Transaction updated.\n");
        } else if (action == 2) {
            transactions.remove(choice - 1);
            walletBalance += selected.amount;
            System.out.println("🗑️ Transaction deleted.\n");
        } else {
            System.out.println("❌ Invalid action.\n");
        }
    }

    public static void addMoneyToWallet() {
        System.out.print("Enter amount to add: ₹");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        walletBalance += amount;
        System.out.println("✅ ₹" + amount + " added to wallet. 💰 New balance: ₹" + walletBalance + "\n");
    }

    public static void saveDataToFile() {
        try (FileWriter writer = new FileWriter("userdata.txt")) {
            writer.write("User Data:\n");
            for (Map.Entry<String, Object> entry : userData.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }

            writer.write("\nTransactions:\n");
            for (Transaction t : transactions) {
                writer.write(t.category + "," + t.amount + "\n");
            }

            writer.write("\nUpcoming Payments:\n");
            for (Transaction t : upcomingPayments) {
                writer.write(t.category + "," + t.amount + ", Due: " + t.dueDate + "\n");
            }

            writer.write("\nWallet Balance: ₹" + walletBalance + "\n");
            System.out.println("📂 Data saved successfully!\n");
        } catch (Exception e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    public static void displayMenu() {
        System.out.println("\n📌 Personal Finance Advisor - Menu");
        System.out.println("1. Setup User");
        System.out.println("2. Add Transaction");
        System.out.println("3. View Transaction History");
        System.out.println("4. Undo Last Transaction");
        System.out.println("5. Add Upcoming Payment");
        System.out.println("6. View Upcoming Payments");
        System.out.println("7. Exit");
        System.out.println("8. View Expense by Category");
        System.out.println("9. Edit/Delete a Transaction");
        System.out.println("10. Add Money to Wallet");
    }

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> setupUser();
                case 2 -> addTransaction();
                case 3 -> displayTransactions();
                case 4 -> undoTransaction();
                case 5 -> addUpcomingPayment();
                case 6 -> displayUpcomingPayments();
                case 7 -> {
                    saveDataToFile();
                    System.out.println("🚀 Exiting... Stay financially smart! 💰");
                    scanner.close();
                    return;
                }
                case 8 -> displayCategoryReport();
                case 9 -> manageTransaction();
                case 10 -> addMoneyToWallet();
                default -> System.out.println("❌ Invalid choice, please try again.\n");
            }
        }
    }
}
/*OUTPUT
? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 1
Enter your name: Tanuja
Enter your monthly income: ?60000
Select your profession:
1. Student
2. Engineer
3. Doctor
4. Teacher
5. Businessman
Enter your choice: 1
Do you live in (Urban/Rural)? Ruural

Suggested Budget for Student (ruural area)
? Needs: ?24000.0
? Wants: ?24000.0
? Savings: ?12000.0


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 2
Enter category (Food, Rent, Shopping, etc.): Food
Enter amount: ?200
? Transaction added: Food - ?200.0
? Remaining Balance: ?59800.0

? Investment Suggestion:
? Consider investing ?5980.0 in Mutual Funds.
? Consider saving ?8970.0 for real estate.


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 3

? Transaction History:
Food: ?200.0


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 4
? Undoing last transaction: Food: ?200.0
? Updated Balance: ?60000.0


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 5
Enter bill name: Light
Enter amount: ?500
Enter due date (YYYY-MM-DD): 2025-05-03
? Upcoming payment added: Light - ?500.0 (Due: 2025-05-03)


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 6

? Upcoming Payments:
Light: ?500.0 (Due: 2025-05-03)


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 8
? No transactions found.


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 2
Enter category (Food, Rent, Shopping, etc.): rent
Enter amount: ?600
? Transaction added: rent - ?600.0
? Remaining Balance: ?59400.0

? Investment Suggestion:
? Consider investing ?5940.0 in Mutual Funds.
? Consider saving ?8910.0 for real estate.


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 9

? Transactions:
1. rent: ?600.0
Enter transaction number to edit/delete: 1
Selected: rent: ?600.0
1. Edit
2. Delete
Choose option: 1
New category: travel
New amount: ?800
? Transaction updated.


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: 10
Enter amount to add: ?700
? ?700.0 added to wallet. ? New balance: ?59900.0


? Personal Finance Advisor - Menu
1. Setup User
2. Add Transaction
3. View Transaction History
4. Undo Last Transaction
5. Add Upcoming Payment
6. View Upcoming Payments
7. Exit
8. View Expense by Category
9. Edit/Delete a Transaction
10. Add Money to Wallet

Enter your choice: */