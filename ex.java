import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

public class ex {
    public static void main(String[] args) {
        String csvPath = "C:\\Users\\karti\\kartika\\Karti\\Loop_CodeZen\\UPI_pay2.csv";

        Map<String, String> tagToCategory = new HashMap<>();

        // Food
        String[] foodKeywords = {"patis", "vada pav", "coffee", "sandwich", "idli", "momos", "juice", "icecream", "chocolate", "cake", "fry", "biryani", "egg", "maggie", "shev", "samosa", "vadapav"};
        for (String word : foodKeywords) tagToCategory.put(word.toLowerCase(), "food");

        // Shopping
        String[] shoppingKeywords = {"myntra", "flipkart", "gift", "toys", "shopping"};
        for (String word : shoppingKeywords) tagToCategory.put(word.toLowerCase(), "shopping");

        // Groceries
        String[] groceriesKeywords = {"grocery", "supermarket", "vegetables", "fruits", "rice", "flour", "groceries", "spices"};
        for (String word : groceriesKeywords) tagToCategory.put(word.toLowerCase(), "groceries");

        // Bill payments
        String[] billsKeywords = {"electricity", "water", "recharge", "internet", "bill payments", "rent", "payment for"};
        for (String word : billsKeywords) tagToCategory.put(word.toLowerCase(), "bill payments");

        // Travel
        String[] travelKeywords = {"flight", "msrtc", "irctc", "travel", "taxi", "uber", "cab", "trip"};
        for (String word : travelKeywords) tagToCategory.put(word.toLowerCase(), "travel");

        // Transfers
        String[] transferKeywords = {"Transfers", "sent", "money", "bank", "from", "received from", "to", "account", "credited"};
        for (String word : transferKeywords) tagToCategory.put(word.toLowerCase(), "transfers");

        // Budget Limits
        Map<String, Double> budgetLimits = new HashMap<>();
        budgetLimits.put("food", 500.0);
        budgetLimits.put("shopping", 500.0);
        budgetLimits.put("groceries", 350.0);
        budgetLimits.put("bill payments", 1000.0);
        budgetLimits.put("travel", 500.0);
        budgetLimits.put("transfers", 1000.0);

        Map<String, Double> spending = new HashMap<>();

        class Transaction {
            Date date;
            double amount;
            String category;

            Transaction(Date date, double amount, String category) {
                this.date = date;
                this.amount = amount;
                this.category = category;
            }
        }

        List<Transaction> transactionList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line = br.readLine(); // skip header
            System.out.printf("%12s %45s %10s %15s\n", "Date", "Details", "Amount", "Category");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 7) continue;

                String date = parts[0].trim();
                String time = parts[1].trim();
                String details = parts[2].trim();
                String amountStr = parts[3].trim().replace("+", "").replace("\"", "").replace("₹", "").replace(",", "");
                String upi = parts[4].trim();
                String remarks = parts[5].trim().toLowerCase();
                String tags = parts[6].trim().toLowerCase();

                String fullText = (remarks + " " + tags).toLowerCase();
                String category = "uncategorized";

                for (String keyword : tagToCategory.keySet()) {
                    Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE);
                    if (pattern.matcher(fullText).find()) {
                        category = tagToCategory.get(keyword);
                        break;
                    }
                }

                System.out.printf("%12s %45s %10s %15s\n", date, details, amountStr, category);

                try {
                    double amount = Math.abs(Double.parseDouble(amountStr)); // Always positive
                    spending.put(category, spending.getOrDefault(category, 0.0) + amount);

                    Date parsedDate = sdf.parse(date);
                    transactionList.add(new Transaction(parsedDate, amount, category));

                } catch (Exception e) {
                    // Ignore bad data
                }
            }

            // Budget Check
            System.out.println("\n Budget Status:");
            PriorityQueue<Map.Entry<String, Double>> overspentPQ = new PriorityQueue<>(
                    (a, b) -> Double.compare(
                            (b.getValue() - budgetLimits.getOrDefault(b.getKey(), 0.0)),
                            (a.getValue() - budgetLimits.getOrDefault(a.getKey(), 0.0)))
            );

            for (Map.Entry<String, Double> entry : spending.entrySet()) {
                String cat = entry.getKey();
                double spent = entry.getValue();
                double limit = budgetLimits.getOrDefault(cat, 0.0);

                if (spent > limit) {
                    System.out.printf("\tOverspent in %s: Rs.%.2f \n", capitalize(cat), spent);
                    overspentPQ.offer(entry);
                } else {
                    System.out.printf("\t%s: Rs.%.2f \n", capitalize(cat), spent);
                }
            }

            // Top 3 Overspent Categories
            System.out.println("\n Top Overspent Categories:");
            int rank = 1;
            while (!overspentPQ.isEmpty() && rank <= 3) {
                Map.Entry<String, Double> entry = overspentPQ.poll();
                System.out.printf("\t%d. %s : Rs.%.2f\n", rank++, capitalize(entry.getKey()), entry.getValue());
            }

            // Weekly Analysis
            System.out.println("\n Weekly Spending Analysis:");
            transactionList.sort(Comparator.comparing(t -> t.date));
            if (transactionList.size() > 0) {
                Date startDate = transactionList.get(0).date;
                List<Double> weeklySpending = new ArrayList<>();
                Map<Integer, Map<String, Double>> weeklyCategorySpending = new HashMap<>(); // For category-wise spending per week
                double currentWeeklySum = 0.0;
                int currentWeek = 1;

                for (Transaction t : transactionList) {
                    long diff = t.date.getTime() - startDate.getTime();
                    int weekNumber = (int) (diff / (1000 * 60 * 60 * 24 * 7)) + 1;

                    // Track category spending per week
                    weeklyCategorySpending.putIfAbsent(weekNumber, new HashMap<>());
                    weeklyCategorySpending.get(weekNumber).put(t.category,
                            weeklyCategorySpending.get(weekNumber).getOrDefault(t.category, 0.0) + t.amount);

                    if (weekNumber > currentWeek) {
                        weeklySpending.add(currentWeeklySum);
                        currentWeeklySum = 0.0;
                        currentWeek = weekNumber;
                    }

                    currentWeeklySum += t.amount;
                }
                weeklySpending.add(currentWeeklySum);

                // Find the week with the maximum spending
                double maxSpending = Collections.max(weeklySpending);
                int maxSpendingWeekIndex = weeklySpending.indexOf(maxSpending) + 1;

                System.out.println("\nWeek with maximum spending: Week " + maxSpendingWeekIndex);
                System.out.printf("\tTotal Spending: Rs. %.2f\n", maxSpending);

                // List the categories contributing to the maximum spending week
                System.out.println("\nCategories contributing to this week:");
                Map<String, Double> maxWeekCategories = weeklyCategorySpending.get(maxSpendingWeekIndex);
                for (Map.Entry<String, Double> entry : maxWeekCategories.entrySet()) {
                    System.out.printf("\t%s: Rs. %.2f\n", entry.getKey(), entry.getValue());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
