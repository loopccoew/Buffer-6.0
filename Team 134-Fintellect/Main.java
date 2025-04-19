import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            // Display the main menu
            System.out.println("\nWelcome to Fintech Investment Planner!");
            System.out.println("======================================");
            System.out.println("1. Optimize Investment Portfolio");
            System.out.println("2. View Investment Goal Dependencies");
            System.out.println("3. Run Risk Profile Simulator");
            System.out.println("4. Get Personalized Fund Recommendations");
            System.out.println("5. Exit");

            // User input for menu choice
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            // Handle user selection using switch
            switch (choice) {
                case 1:
                    System.out.println("Running Portfolio Optimization...");
                    InvestmentPlanner.optimizePortfolioFractional(); // Updated to call fractional optimization
                    break;

                case 2:
                    System.out.println("Opening Goal Dependency Tracker...");
                    runGoalDependencyModule(); // Calls method for goal dependency management
                    break;

                case 3:
                    System.out.println("Launching Risk Profiling Simulator...");
                    runRiskProfileSimulation(sc); // Calls method to simulate risk profile
                    break;

                case 4:
                    System.out.println("Generating Personalized Fund Recommendations...");
                    PortfolioAllocator.suggestFundsWeighted(); // Updated to call weighted fund recommendation
                    break;

                case 5:
                    System.out.println("Exiting... Thank you for using the planner.");
                    return; // Ends the program

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Handles the investment goal and dependency logic
    private static void runGoalDependencyModule() {
        try (Scanner sc = new Scanner(System.in)) {
            InvestmentGraph graph = new InvestmentGraph(); // Custom class for managing goals as a graph

            // Collect number of goals from user
            System.out.print("Enter number of goals: ");
            int n = sc.nextInt();
            sc.nextLine(); // Clear input buffer

            // Add goals with their priorities
            for (int i = 0; i < n; i++) {
                System.out.print("Enter goal name: ");
                String goal = sc.nextLine();

                System.out.print("Enter priority (integer) for this goal: ");
                int priority = sc.nextInt();
                sc.nextLine();

                graph.addGoal(goal, priority);
            }

            // Collect dependencies between goals
            System.out.print("Enter number of dependencies: ");
            int d = sc.nextInt();
            sc.nextLine();

            for (int i = 0; i < d; i++) {
                System.out.print("Enter dependency (format: dependentGoal prerequisiteGoal): ");
                String[] dep = sc.nextLine().split(" ");
                if (dep.length == 2) {
                    graph.addDependency(dep[0], dep[1]);
                } else {
                    System.out.println("Invalid format. Skipping this dependency.");
                }
            }

            // Display the ordered list of goals based on dependencies
            graph.displayGoalOrder();
        }
    }

    // Simulates user risk profiling based on basic inputs
    private static void runRiskProfileSimulation(Scanner sc) {
        System.out.print("Enter your age: ");
        int age = sc.nextInt();

        System.out.print("Enter total savings (in ₹): ");
        int savings = sc.nextInt();

        System.out.print("Do you have active loans? (true/false): ");
        boolean hasLoans = sc.nextBoolean();

        // Create a UserProfile object
        RiskProfileSimulator.UserProfile profile = new RiskProfileSimulator.UserProfile(
            age, savings, 0, hasLoans, 0, 0
        );

        // Determine risk type using the updated method
        String riskType = RiskProfileSimulator.determineRiskType(profile);
        System.out.println("Risk Profile: " + riskType);
    }
}