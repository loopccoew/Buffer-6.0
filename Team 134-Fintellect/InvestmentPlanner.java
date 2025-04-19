import java.util.*;

public class InvestmentPlanner {

    static class Investment {
        String name;
        int cost, expectedReturn, risk;

        Investment(String name, int cost, int expectedReturn, int risk) {
            this.name = name;
            this.cost = cost;
            this.expectedReturn = expectedReturn;
            this.risk = risk;
        }
    }

    public static void optimizePortfolioFractional() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your total budget (₹): ");
        int budget = sc.nextInt();
        if (budget <= 0) {
            System.out.println("Budget must be greater than 0. Exiting optimization.");
            return;
        }

        System.out.println("Enter your maximum acceptable risk level (e.g., 1 to 10): ");
        int maxRisk = sc.nextInt();

        System.out.println("How many investment options do you want to enter?");
        int n = sc.nextInt();
        if (n <= 0) {
            System.out.println("No investment options provided. Exiting optimization.");
            return;
        }

        Investment[] options = new Investment[n];
        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for Investment " + (i + 1));
            System.out.print("Name: ");
            String name = sc.next();
            System.out.print("Cost (₹): ");
            int cost = sc.nextInt();
            System.out.print("Expected Return (₹): ");
            int ret = sc.nextInt();
            System.out.print("Risk (1-10): ");
            int risk = sc.nextInt();

            if (cost <= 0 || ret <= 0 || risk < 1 || risk > 10) {
                System.out.println("Invalid investment details. Skipping this investment.");
                continue;
            }

            options[i] = new Investment(name, cost, ret, risk);
        }

        Arrays.sort(options, (a, b) -> Double.compare((double) b.expectedReturn / b.cost, (double) a.expectedReturn / a.cost));

        double totalReturn = 0;
        for (Investment inv : options) {
            if (budget >= inv.cost) {
                budget -= inv.cost;
                totalReturn += inv.expectedReturn;
                System.out.println("Selected: " + inv.name + " (Full)");
            } else {
                double fraction = (double) budget / inv.cost;
                totalReturn += inv.expectedReturn * fraction;
                System.out.println("Selected: " + inv.name + " (" + (fraction * 100) + "%)");
                break;
            }
        }

        System.out.println("Maximized Return: ₹" + totalReturn);
    }
}