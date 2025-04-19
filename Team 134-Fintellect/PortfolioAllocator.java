import java.util.*;

public class PortfolioAllocator {

    static class Fund {
        String name;
        int expectedReturn, risk, tenure, cost;

        Fund(String name, int expectedReturn, int risk, int tenure, int cost) {
            this.name = name;
            this.expectedReturn = expectedReturn;
            this.risk = risk;
            this.tenure = tenure;
            this.cost = cost;
        }

        public int weightedScore(int userRisk, int userTenure, int weightReturn, int weightRisk, int weightTenure) {
            int riskScore = Math.max(0, 10 - Math.abs(userRisk - this.risk) * 2);
            int tenureScore = Math.max(0, 10 - Math.abs(userTenure - this.tenure));
            return (expectedReturn * weightReturn) + (riskScore * weightRisk) + (tenureScore * weightTenure);
        }
    }

    // HashMap to store funds for quick lookup
    private static Map<String, Fund> fundMap = new HashMap<>();

    // Graph to represent fund dependencies
    private static Map<String, List<String>> fundDependencyGraph = new HashMap<>();

    public static void suggestFundsWeighted() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your risk tolerance (1 - Conservative, 2 - Balanced, 3 - Aggressive): ");
        int userRisk = sc.nextInt();
        if (userRisk < 1 || userRisk > 3) {
            System.out.println("Invalid risk tolerance. Please enter a value between 1 and 3.");
            return;
        }

        System.out.println("Enter your preferred investment tenure (in years): ");
        int userTenure = sc.nextInt();
        if (userTenure <= 0) {
            System.out.println("Investment tenure must be greater than 0. Please enter a valid tenure.");
            return;
        }

        System.out.println("Assign weights to factors (total should be 100):");
        System.out.print("Weight for Return: ");
        int weightReturn = sc.nextInt();
        System.out.print("Weight for Risk: ");
        int weightRisk = sc.nextInt();
        System.out.print("Weight for Tenure: ");
        int weightTenure = sc.nextInt();

        if (weightReturn + weightRisk + weightTenure != 100) {
            System.out.println("Weights must add up to 100. Please re-enter the weights.");
            return;
        }

        // Initialize funds and dependencies
        initializeFundsAndDependencies();

        // Sort funds based on weighted score
        List<Fund> fundList = new ArrayList<>(fundMap.values());
        fundList.sort((a, b) -> b.weightedScore(userRisk, userTenure, weightReturn, weightRisk, weightTenure)
                - a.weightedScore(userRisk, userTenure, weightReturn, weightRisk, weightTenure));

        System.out.println("\nPersonalized Fund Recommendations:");
        for (Fund f : fundList) {
            System.out.println(f.name + " | Score: " + f.weightedScore(userRisk, userTenure, weightReturn, weightRisk, weightTenure));
        }

        // Display fund dependencies
        System.out.println("\nFund Dependency Graph:");
        for (String fund : fundDependencyGraph.keySet()) {
            System.out.println(fund + " -> " + fundDependencyGraph.get(fund));
        }

        // Portfolio optimization using DP
        System.out.println("\nEnter your budget for investment: ");
        int budget = sc.nextInt();
        int maxReturn = optimizePortfolioDP(fundList, budget);
        System.out.println("Maximum return achievable within budget: " + maxReturn);
    }

    private static void initializeFundsAndDependencies() {
        // Add funds to the HashMap
        fundMap.put("Tech ETF", new Fund("Tech ETF", 18, 3, 5, 100));
        fundMap.put("Public Provident Fund", new Fund("Public Provident Fund", 8, 1, 15, 50));
        fundMap.put("SIP Balanced", new Fund("SIP Balanced", 12, 2, 7, 70));
        fundMap.put("Government Bonds", new Fund("Government Bonds", 7, 1, 10, 40));
        fundMap.put("Bluechip Equity", new Fund("Bluechip Equity", 14, 2, 6, 90));
        fundMap.put("Aggressive Mutual", new Fund("Aggressive Mutual", 20, 3, 4, 120));

        // Add dependencies to the graph
        fundDependencyGraph.put("Tech ETF", Arrays.asList("Bluechip Equity"));
        fundDependencyGraph.put("Public Provident Fund", Arrays.asList());
        fundDependencyGraph.put("SIP Balanced", Arrays.asList("Government Bonds"));
        fundDependencyGraph.put("Government Bonds", Arrays.asList());
        fundDependencyGraph.put("Bluechip Equity", Arrays.asList());
        fundDependencyGraph.put("Aggressive Mutual", Arrays.asList("Tech ETF", "SIP Balanced"));
    }

    // Dynamic Programming for portfolio optimization
    private static int optimizePortfolioDP(List<Fund> funds, int budget) {
        int n = funds.size();
        int[][] dp = new int[n + 1][budget + 1];

        for (int i = 1; i <= n; i++) {
            Fund fund = funds.get(i - 1);
            for (int w = 1; w <= budget; w++) {
                if (fund.cost <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - fund.cost] + fund.expectedReturn);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return dp[n][budget];
    }

    public static void main(String[] args) {
        suggestFundsWeighted();
    }
}