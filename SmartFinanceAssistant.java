import java.util.ArrayList;
import java.util.Scanner;

public class SmartFinanceAssistant {
    static class Goal {
        String name;
        double amount;
        int duration;
        double savedAmount;

        Goal(String name, double amount, int duration) {
            this.name = name;
            this.amount = amount;
            this.duration = duration;
            this.savedAmount = 0;
        }

        void addSavedAmount(double amount) {
            this.savedAmount += amount;
        }

        boolean isAchieved() {
            return savedAmount >= amount;
        }

        @Override
        public String toString() {
            return name + " - $" + amount + " over " + duration + " months. Saved: $" + savedAmount +
                    (isAchieved() ? " (Achieved)" : " (Not Achieved) - To Save: $" + (amount - savedAmount));
        }
    }

    static class Loan {
        double amount;
        double interestRate;
        int duration;
        double paidAmount;

        Loan(double amount, double interestRate, int duration) {
            this.amount = amount;
            this.interestRate = interestRate;
            this.duration = duration;
            this.paidAmount = 0;
        }

        double calculateMonthlyPayment() {
            return (amount * (1 + interestRate / 100)) / duration;
        }

        boolean isPaidOff() {
            return paidAmount >= amount;
        }

        double amountRemaining() {
            return amount - paidAmount;
        }

        void predictRepaymentSchedule() {
            double monthlyPayment = calculateMonthlyPayment();
            double remaining = amountRemaining();
            int monthsLeft = (int) Math.ceil(remaining / monthlyPayment);

            System.out.println("Predicted Monthly Payment: $" + String.format("%.2f", monthlyPayment));
            System.out.println("Remaining Amount: $" + String.format("%.2f", remaining));
            System.out.println("Estimated Months Left: " + monthsLeft + " month(s)");
        }

        @Override
        public String toString() {
            return "Loan of $" + amount + " at " + interestRate + "% for " + duration + " months. Paid: $" + paidAmount +
                    (isPaidOff() ? " (Paid off)" : " (Remaining: $" + amountRemaining() + ")");
        }
    }

    static class Transaction {
        double amount;
        String category;

        Transaction(double amount, String category) {
            this.amount = amount;
            this.category = category;
        }

        @Override
        public String toString() {
            return "$" + amount + " - " + category;
        }
    }

    static class BankAccount {
        String accountNumber;
        String ifscCode;
        double balance;

        BankAccount(String accountNumber, String ifscCode, double balance) {
            this.accountNumber = accountNumber;
            this.ifscCode = ifscCode;
            this.balance = balance;
        }

        boolean verifyAccount(String accountNumber, String ifscCode) {
            return this.accountNumber.equals(accountNumber) && this.ifscCode.equals(ifscCode);
        }

        void deductAmount(double amount) {
            if (balance >= amount) {
                balance -= amount;
            } else {
                System.out.println("Insufficient funds!");
            }
        }

        @Override
        public String toString() {
            return "Account Balance: $" + balance;
        }
    }

    static double accountBalance = 0;
    static BankAccount bankAccount = null;
    static ArrayList<Goal> goals = new ArrayList<>();
    static ArrayList<Loan> loans = new ArrayList<>();
    static ArrayList<Transaction> transactions = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addGoal();
                    break;
                case 2:
                    addTransaction();
                    break;
                case 3:
                    addLoan();
                    break;
                case 4:
                    payLoan();
                    break;
                case 5:
                    viewSummary();
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    return;
                case 7:
                    predictLoanRepayment();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void showMenu() {
        System.out.println("\nSmart Finance Assistant");
        System.out.println("1. Add Goal");
        System.out.println("2. Add Transaction");
        System.out.println("3. Add Loan");
        System.out.println("4. Pay Loan");
        System.out.println("5. View Summary");
        System.out.println("6. Exit");
        System.out.println("7. Predict Loan Repayment");
        System.out.print("Enter your choice: ");
    }

    public static void addGoal() {
        verifyBankDetails();

        System.out.print("Enter Goal Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Goal Amount ($): ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Goal Duration (Months): ");
        int duration = scanner.nextInt();
        scanner.nextLine();

        Goal goal = new Goal(name, amount, duration);
        goals.add(goal);
        System.out.print("Enter amount to save towards this goal: ");
        double saveAmount = scanner.nextDouble();
        scanner.nextLine();

        if (saveAmount <= accountBalance) {
            goal.addSavedAmount(saveAmount);
            accountBalance -= saveAmount;
            System.out.println("Saved $" + saveAmount + " towards the goal. New balance: $" + accountBalance);
        } else {
            System.out.println("Insufficient funds to save towards this goal.");
        }

        System.out.println("Goal added successfully!");
    }

    public static void addTransaction() {
        verifyBankDetails();

        System.out.print("Enter Transaction Amount ($): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();

        if (accountBalance >= amount) {
            accountBalance -= amount;
            transactions.add(new Transaction(amount, category));
            System.out.println("Transaction processed successfully!");
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public static void addLoan() {
        verifyBankDetails();

        System.out.print("Enter Loan Amount ($): ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Interest Rate (%): ");
        double interestRate = scanner.nextDouble();
        System.out.print("Enter Loan Duration (Months): ");
        int duration = scanner.nextInt();
        scanner.nextLine();

        Loan loan = new Loan(amount, interestRate, duration);
        loans.add(loan);
        System.out.println("Loan added successfully!");
    }

    public static void payLoan() {
        verifyBankDetails();

        System.out.print("Enter Loan Index to Pay: ");
        int loanIndex = scanner.nextInt();
        scanner.nextLine();

        if (loanIndex >= 0 && loanIndex < loans.size()) {
            Loan loan = loans.get(loanIndex);
            double monthlyPayment = loan.calculateMonthlyPayment();
            if (accountBalance >= monthlyPayment) {
                loan.paidAmount += monthlyPayment;
                accountBalance -= monthlyPayment;
                System.out.println("Paid $" + monthlyPayment + " towards the loan.");
            } else {
                System.out.println("Insufficient funds to pay the loan.");
            }
        } else {
            System.out.println("Invalid loan index.");
        }
    }

    public static void predictLoanRepayment() {
        if (loans.isEmpty()) {
            System.out.println("No loans found.");
            return;
        }

        System.out.println("Select Loan to Predict:");
        for (int i = 0; i < loans.size(); i++) {
            System.out.println(i + ": " + loans.get(i));
        }

        int index = scanner.nextInt();
        scanner.nextLine();

        if (index >= 0 && index < loans.size()) {
            loans.get(index).predictRepaymentSchedule();
        } else {
            System.out.println("Invalid loan index.");
        }
    }

    public static void viewSummary() {
        System.out.println("\n--- Summary ---");
        if (bankAccount != null) {
            System.out.println(bankAccount);
        }

        System.out.println("\nGoals:");
        for (Goal goal : goals) {
            System.out.println(goal);
        }

        System.out.println("\nLoans:");
        for (Loan loan : loans) {
            System.out.println(loan);
        }

        System.out.println("\nTransactions:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println("Current Account Balance: $" + accountBalance);
    }

    public static void linkBankAccount() {
        System.out.print("Enter Bank Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter IFSC Code: ");
        String ifscCode = scanner.nextLine();

        double balance = 1000.00;
        bankAccount = new BankAccount(accountNumber, ifscCode, balance);
        accountBalance = balance;
        System.out.println("Bank account linked successfully with balance: $" + accountBalance);
    }

    private static void verifyBankDetails() {
        if (bankAccount == null) {
            System.out.println("Please link your bank account first.");
            linkBankAccount();
        }

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter IFSC Code: ");
        String ifscCode = scanner.nextLine();

        if (!bankAccount.verifyAccount(accountNumber, ifscCode)) {
            System.out.println("Invalid bank details. Please check and try again.");
            System.exit(0);
        }
    }
}
