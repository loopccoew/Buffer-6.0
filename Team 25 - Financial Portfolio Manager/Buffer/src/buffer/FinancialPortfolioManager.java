package buffer;
import java.util.*;

import java.util.List;



import javax.swing.*;

import java.awt.*;




// -------------------- Stock Class --------------------

class Stock {

 String symbol;

 String name;

 double currentPrice;

 double priceChangePercent;



 public Stock(String symbol, String name, double currentPrice, double priceChangePercent) {

 this.symbol = symbol;

 this.name = name;

 this.currentPrice = currentPrice;

 this.priceChangePercent = priceChangePercent;

 }



 @Override

 public String toString() {

 return symbol + " - " + name + ": ₹" + currentPrice + " (" + priceChangePercent + "%)";

 }

}



// -------------------- Stock Manager --------------------

class StockManager {

 private final List<Stock> stocks = new ArrayList<>();



 // Mock method to simulate fetching real-time stock data

 public void fetchRealTimeStockData() {

 // Simulating real-time stock data fetching

 stocks.add(new Stock("AAPL", "Apple Inc.", 145.09, 1.2));

 stocks.add(new Stock("GOOGL", "Alphabet Inc.", 2730.30, 0.8));

 stocks.add(new Stock("AMZN", "Amazon.com Inc.", 3500.50, 1.5));

 stocks.add(new Stock("MSFT", "Microsoft Corp.", 299.60, 2.1));

 stocks.add(new Stock("TSLA", "Tesla Inc.", 750.70, -0.5));

 }



 public List<Stock> getBestStocks() {

 stocks.sort(Comparator.comparingDouble(stock -> -stock.priceChangePercent)); // Sort by highest price change

 return stocks.subList(0, Math.min(5, stocks.size())); // Return top 5 best performing stocks

 }



 public List<Stock> getAllStocks() {

 return stocks;

 }



 public Stock getStock(String symbol) {

 return stocks.stream()

 .filter(stock -> stock.symbol.equalsIgnoreCase(symbol))

 .findFirst()

 .orElse(null);

 }

}



// -------------------- Investment CRUD Manager --------------------

class Investment {

 String category;

 String name;

 int amount;



 public Investment(String category, String name, int amount) {

 this.category = category;

 this.name = name;

 this.amount = amount;

 }



 public String toString() {

 return category + " - " + name + ": ₹" + amount;

 }

}



class InvestmentManager {

 private final List<Investment> investments = new ArrayList<>();



 public void addInvestment(Investment inv) {

 investments.add(inv);

 }



 public List<Investment> getInvestments() {

 return new ArrayList<>(investments);

 }



 public boolean deleteInvestment(String name) {

 return investments.removeIf(inv -> inv.name.equalsIgnoreCase(name));

 }



 public Investment searchInvestment(String name) {

 List<Investment> sorted = new ArrayList<>(investments);

 sorted.sort(Comparator.comparing(inv -> inv.name));

 int low = 0, high = sorted.size() - 1;

 while (low <= high) {

 int mid = (low + high) / 2;

 int cmp = sorted.get(mid).name.compareToIgnoreCase(name);

 if (cmp == 0) return sorted.get(mid);

 else if (cmp < 0) low = mid + 1;

 else high = mid - 1;

 }

 return null;

 }

}



// -------------------- Tax Deduction Trie --------------------

class TaxTrie {

 static class TrieNode {

 TrieNode[] children = new TrieNode[26];

 int deductionPercentage = 0;

 }



 private final TrieNode root;



 public TaxTrie() {

 root = new TrieNode();

 }



 public void insert(String category, int deductionPercentage) {

 TrieNode node = root;

 for (char ch : category.toUpperCase().toCharArray()) {

 if (ch < 'A' || ch > 'Z') continue;

 int index = ch - 'A';

 if (node.children[index] == null) {

 node.children[index] = new TrieNode();

 }

 node = node.children[index];

 }

 node.deductionPercentage = deductionPercentage;

 }



 public int search(String category) {

 TrieNode node = root;

 for (char ch : category.toUpperCase().toCharArray()) {

 if (ch < 'A' || ch > 'Z') continue;

 int index = ch - 'A';

 if (node.children[index] == null) {

 return -1;

 }

 node = node.children[index];

 }

 return node.deductionPercentage;

 }

}



// -------------------- Tax Optimizer (Dynamic Programming) --------------------

class TaxOptimizer {

 public int calculateMaxDeduction(int income, int[] deductions, int[] limits) {

 int n = deductions.length;

 int[][] dp = new int[n + 1][income + 1];



 for (int i = 1; i <= n; i++) {

 for (int j = 0; j <= income; j++) {

 if (deductions[i - 1] <= j) {

 dp[i][j] = Math.max(dp[i - 1][j],

 dp[i - 1][j - deductions[i - 1]] + Math.min(deductions[i - 1], limits[i - 1]));

 } else {

 dp[i][j] = dp[i - 1][j];

 }

 }

 }

 return dp[n][income];

 }

}



// -------------------- Budget Manager --------------------

class BudgetManager {

 private final Map<String, Integer> budgetMap = new HashMap<>();

 private final Map<String, Integer> spendingMap = new HashMap<>();



 public void addBudget(String category, int amount) {

 budgetMap.put(category.toLowerCase(), amount);

 }



 public String checkSpending(String category, int spent) {

 String key = category.toLowerCase();

 if (!budgetMap.containsKey(key)) {

 return "No budget found for category: " + category;

 }



 spendingMap.put(key, spendingMap.getOrDefault(key, 0) + spent);

 int totalSpent = spendingMap.get(key);

 int budget = budgetMap.get(key);



 StringBuilder result = new StringBuilder();

 result.append("Category: ").append(category).append("\n")

 .append("Total Spent: ₹").append(totalSpent).append("\n")

 .append("Budget: ₹").append(budget).append("\n");



 if (totalSpent > budget) {

 result.append("Status: You have exceeded your budget!");

 } else {

 result.append("Status: Spending is within budget.");

 }



 return result.toString();

 }

}



// -------------------- Spending Tracker --------------------

class TopSpendingTracker {

 private final PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

 private final List<String> topSpendingCategories = new ArrayList<>();



 public void addSpending(String category, int amount) {

 maxHeap.offer(amount);

 topSpendingCategories.add(category + ": ₹" + amount);

 }



 public List<String> getTopSpending() {

 return new ArrayList<>(topSpendingCategories);

 }

}



// -------------------- Financial Portfolio Manager --------------------

public class FinancialPortfolioManager {

    public static String currentUserName = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinancialPortfolioManager::showLoginFrame);
    }

    private static void showLoginFrame() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 200);
        loginFrame.setLayout(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.getContentPane().setBackground(new Color(255, 192, 203));

        JLabel label = new JLabel("Enter your name:");
        label.setBounds(50, 30, 150, 25);
        loginFrame.add(label);

        JTextField nameField = new JTextField();
        nameField.setBounds(170, 30, 150, 25);
        loginFrame.add(nameField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(140, 80, 100, 30);
        loginButton.setBackground(new Color(255, 192, 203));
        loginButton.setForeground(new Color(255, 192, 203));
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentUserName = name;
            loginFrame.dispose();
            Login.showDashboard(name);
        });

        loginFrame.setVisible(true);
    }
}