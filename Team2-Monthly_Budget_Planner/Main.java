import java.util.*;
import java.io.*;

class BudgetPlanner {
    private final Map<String, Double> categoryBudgetMap;
    private final Map<String, Double> categoryRemainingMap;
    private final Map<String, Double> categorySpentMap;
    private final Map<String, Double> miscBreakdown;

    private double totalBudget;
    private double totalSpent = 0;

    public BudgetPlanner() {
        categoryBudgetMap = new HashMap<>();
        categoryRemainingMap = new HashMap<>();
        categorySpentMap = new HashMap<>();
        miscBreakdown = new HashMap<>();
    }

    public void setTotalBudget(double budget) {
        this.totalBudget = budget;
    }

    public void addCategory(String category, double amount) {
    category = cleanCategory(category);

    double currentAllocated = 0;
    for (double val : categoryBudgetMap.values()) {
        currentAllocated += val;
    }

    if (currentAllocated + amount > totalBudget) {
        System.out.printf("  Budget exceeded! Total allocation (₹%.2f) would exceed your total budget of ₹%.2f%n",
                currentAllocated + amount, totalBudget);
        return;
    }

    categoryBudgetMap.put(category, amount);
    categoryRemainingMap.put(category, amount);
    categorySpentMap.put(category, 0.0);
}


    public void addDailySpending(String category, double amount) {
        category = cleanCategory(category);
        if (amount < 0) {
            System.out.println("Amount cannot be negative.");
            return;
        }

        if (!categoryRemainingMap.containsKey(category)) {
            System.out.println("Category not found. Adding to 'Miscellaneous'.");
            miscBreakdown.put(category, miscBreakdown.getOrDefault(category, 0.0) + amount);

            double remainingMisc = categoryRemainingMap.get("miscellaneous");
            double newRemaining = remainingMisc - amount;

            if (newRemaining < 0) {
                System.out.printf("Overspent in 'Miscellaneous' by ₹%.2f%n", -newRemaining);
                amount = remainingMisc;
                categoryRemainingMap.put("miscellaneous", 0.0);
            } else {
                categoryRemainingMap.put("miscellaneous", newRemaining);
            }

            categorySpentMap.put("miscellaneous", categorySpentMap.get("miscellaneous") + amount);
        } else {
            double remaining = categoryRemainingMap.get(category);
            double newRemaining = remaining - amount;

            if (newRemaining < 0) {
                System.out.printf("Overspent in '%s' by ₹%.2f%n", category, -newRemaining);
                amount = remaining;
                categoryRemainingMap.put(category, 0.0);
            } else {
                categoryRemainingMap.put(category, newRemaining);
            }

            categorySpentMap.put(category, categorySpentMap.get(category) + amount);
        }

        totalSpent += amount;
    }

    public void deleteCategory(String category) {
        category = cleanCategory(category);
        if (category.equals("miscellaneous")) {
            System.out.println("Cannot delete Miscellaneous category.");
            return;
        }

        if (categoryBudgetMap.containsKey(category)) {
            totalBudget -= categoryBudgetMap.get(category);
            totalSpent -= categorySpentMap.getOrDefault(category, 0.0);
            categoryBudgetMap.remove(category);
            categoryRemainingMap.remove(category);
            categorySpentMap.remove(category);
            System.out.println("Category '" + category + "' deleted.");
        } else {
            System.out.println("Category not found.");
        }
    }

    public void showMonthlyPlan() {
        System.out.println("\nMonthly Plan (Category Allocations):");
        printMap(categoryBudgetMap, false);
        System.out.printf("Total Budget Set: ₹%.2f%n", totalBudget);
    }

    public void showSpentByCategory() {
        System.out.println("\nAmount Spent per Category:");
        printMap(categorySpentMap, true);

        if (!miscBreakdown.isEmpty()) {
            System.out.println(" → Breakdown of Miscellaneous:");
            printMap(miscBreakdown, true);
        }
    }

    public void showRemainingByCategory() {
        System.out.println("\nRemaining Budget per Category:");
        printMap(categoryRemainingMap, false);
    }

    public void showSummary() {
        System.out.println("\nMonthly Budget Summary:");
        System.out.printf("Total Monthly Budget : ₹%.2f%n", totalBudget);
        System.out.printf("Total Spent So Far   : ₹%.2f%n", totalSpent);
        double remaining = totalBudget - totalSpent;

        if (remaining < 0) {
            System.out.printf("You've overspent the total budget by ₹%.2f%n", -remaining);
        } else {
            System.out.printf("Remaining Budget     : ₹%.2f%n", remaining);
        }
    }

    public void exportToCSV(String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write("Category,Budget,Spent,Remaining\n");
        for (String category : categoryBudgetMap.keySet()) {
            bw.write(String.format("%s,%.2f,%.2f,%.2f%n", category,
                    categoryBudgetMap.get(category),
                    categorySpentMap.getOrDefault(category, 0.0),
                    categoryRemainingMap.getOrDefault(category, 0.0)));
        }
        bw.write("TOTAL,");
        bw.write(String.format("%.2f,%.2f,%.2f%n", totalBudget, totalSpent, totalBudget - totalSpent));

        if (!miscBreakdown.isEmpty()) {
            bw.write("\nMiscellaneous Breakdown:\nCategory,Spent\n");
            for (String misc : miscBreakdown.keySet()) {
                bw.write(String.format("%s,%.2f%n", misc, miscBreakdown.get(misc)));
            }
        }

        bw.close();
        System.out.println("Data exported to " + filename);
    }

    public void forecastSpendingAndAlerts() {
        System.out.println("\nSpending Forecast and Alerts:");
        double remainingBudget = totalBudget - totalSpent;

        if (remainingBudget < 0) {
            System.out.printf("You’ve already overspent by ₹%.2f%n", -remainingBudget);
        } else if (remainingBudget < totalBudget * 0.1) {
            System.out.printf("Warning: Only ₹%.2f (10%%) of your budget remains!%n", remainingBudget);
        } else {
            System.out.printf("You’re on track! ₹%.2f remaining.%n", remainingBudget);
        }
    }

    private String cleanCategory(String cat) {
        return cat.trim().toLowerCase();
    }

    private void printMap(Map<String, Double> map, boolean skipZero) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (!skipZero || entry.getValue() > 0) {
                System.out.printf("%-15s: ₹%.2f%n", entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<String, Double> getCategoryBudgetMap() {
        return new HashMap<>(categoryBudgetMap);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BudgetPlanner planner = new BudgetPlanner();

        try {
            System.out.print("Enter your total monthly budget: ₹");
            double totalBudget = sc.nextDouble();
            if (totalBudget < 0) throw new IllegalArgumentException("Budget can't be negative.");
            planner.setTotalBudget(totalBudget);
            sc.nextLine();

            System.out.print("Enter number of categories (excluding 'Miscellaneous'): ");
            int n = sc.nextInt();
            sc.nextLine();

            for (int i = 0; i < n; i++) {
                System.out.print("Enter category name: ");
                String category = sc.nextLine();

                System.out.print("Enter allocated amount for " + category + ": ₹");
                double amount = sc.nextDouble();
                if (amount < 0) throw new IllegalArgumentException("Amount can't be negative.");
                sc.nextLine();

                planner.addCategory(category, amount);
            }

            System.out.print("Enter allocated amount for 'Miscellaneous': ₹");
            double misc = sc.nextDouble();
            if (misc < 0) throw new IllegalArgumentException("Amount can't be negative.");
            sc.nextLine();
            planner.addCategory("Miscellaneous", misc);

            boolean exit = false;
            while (!exit) {
                System.out.println("\nMenu:");
                System.out.println("1. Add Today's Spending");
                System.out.println("2. View Monthly Plan");
                System.out.println("3. View Spending Summary");
                System.out.println("4. Forecast Alerts");
                System.out.println("5. Export to CSV");
                System.out.println("6. Delete a Category");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter category: ");
                        String cat = sc.nextLine();
                        System.out.print("Enter amount spent today: ₹");
                        double amt = sc.nextDouble();
                        sc.nextLine();
                        planner.addDailySpending(cat, amt);
                        break;

                    case 2:
                        planner.showMonthlyPlan();
                        break;

                    case 3:
                        planner.showSpentByCategory();
                        planner.showRemainingByCategory();
                        planner.showSummary();
                        break;

                    case 4:
                        planner.forecastSpendingAndAlerts();
                        break;

                    case 5:
                        try {
                            System.out.print("Enter filename to export (e.g., budget.csv): ");
                            String filename = sc.nextLine();
                            planner.exportToCSV(filename);
                        } catch (IOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;

                    case 6:
                        System.out.print("Enter category to delete: ");
                        String delCat = sc.nextLine();
                        planner.deleteCategory(delCat);
                        break;

                    case 7:
                        exit = true;
                        planner.showSummary();
                        break;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers where expected.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
        }
    }
}