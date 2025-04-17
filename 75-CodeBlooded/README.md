import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.google.gson.*;

public class CurrencyExchangeOptimizer {

    static class Edge {
        String to;
        double rate;

        Edge(String to, double rate) {
            this.to = to;
            this.rate = rate;
        }
    }

    static class Node implements Comparable<Node> {
        String currency;
        double cost;
        java.util.List<String> path;

        Node(String currency, double cost, java.util.List<String> path) {
            this.currency = currency;
            this.cost = cost;
            this.path = path;
        }

        public int compareTo(Node other) {
            return Double.compare(this.cost, other.cost);
        }
    }

    static class Result {
        double rate;
        java.util.List<String> path;

        Result(double rate, java.util.List<String> path) {
            this.rate = rate;
            this.path = path;
        }
    }

    public static Map<String, Double> fetchRatesFromAPI() {
        Map<String, Double> ratesMap = new HashMap<>();
        String apiUrl = "https://open.er-api.com/v6/latest/USD";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
            if (!jsonObject.has("rates")) {
                throw new RuntimeException("API response does not contain 'rates'");
            }

            JsonObject rates = jsonObject.getAsJsonObject("rates");
            for (Map.Entry<String, JsonElement> entry : rates.entrySet()) {
                ratesMap.put(entry.getKey(), entry.getValue().getAsDouble());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch exchange rates.\n" + e.getMessage());
        }

        return ratesMap;
    }

    static Map<String, java.util.List<Edge>> buildGraph(Map<String, Double> ratesMap) {
        Map<String, java.util.List<Edge>> graph = new HashMap<>();
        for (String from : ratesMap.keySet()) {
            for (String to : ratesMap.keySet()) {
                if (!from.equals(to)) {
                    double rate = ratesMap.get(to) / ratesMap.get(from);
                    graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, -Math.log(rate)));
                }
            }
        }
        return graph;
    }

    static Result maxConversionPath(Map<String, java.util.List<Edge>> graph, String src, String target) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(src, 0, new ArrayList<>(java.util.List.of(src))));
        Set<String> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (visited.contains(current.currency)) continue;
            visited.add(current.currency);

            if (current.currency.equals(target)) {
                return new Result(Math.exp(-current.cost), current.path);
            }

            if (graph.containsKey(current.currency)) {
                for (Edge e : graph.get(current.currency)) {
                    if (!visited.contains(e.to)) {
                        java.util.List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(e.to);
                        pq.add(new Node(e.to, current.cost + e.rate, newPath));
                    }
                }
            }
        }

        return new Result(0, new ArrayList<>());
    }

    static Map<Integer, Integer> denominationBreakdown(int amount, int[] denominations) {
        Map<Integer, Integer> result = new LinkedHashMap<>();
        for (int denom : denominations) {
            int count = amount / denom;
            if (count > 0) {
                result.put(denom, count);
                amount %= denom;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Map<String, Double> ratesMap = fetchRatesFromAPI();
            if (ratesMap.isEmpty()) return;

            JFrame frame = new JFrame("Currency Exchange Optimizer 💱");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);

            JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

            // Most popular currencies to display first
            java.util.List<String> popular = Arrays.asList("USD", "INR", "EUR", "GBP", "JPY", "CAD", "AUD", "CNY");
            java.util.List<String> currencyList = new ArrayList<>(ratesMap.keySet());

            // Prioritize popular currencies at the top
            java.util.List<String> ordered = new ArrayList<>();
            for (String p : popular) {
                if (currencyList.contains(p)) ordered.add(p);
            }
            Collections.sort(currencyList);
            for (String c : currencyList) {
                if (!popular.contains(c)) ordered.add(c);
            }

            // Currency dropdowns
            JComboBox<String> sourceBox = new JComboBox<>(ordered.toArray(new String[0]));
            JComboBox<String> targetBox = new JComboBox<>(ordered.toArray(new String[0]));

            JTextField amountField = new JTextField();
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            JButton convertButton = new JButton("Convert");

            panel.add(new JLabel("Source Currency:"));
            panel.add(sourceBox);
            panel.add(new JLabel("Target Currency:"));
            panel.add(targetBox);
            panel.add(new JLabel("Amount:"));
            panel.add(amountField);
            panel.add(convertButton);
            panel.add(new JLabel());

            frame.add(panel, BorderLayout.NORTH);
            frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

            convertButton.addActionListener(e -> {
                String source = (String) sourceBox.getSelectedItem();
                String target = (String) targetBox.getSelectedItem();
                double amount;

                try {
                    amount = Double.parseDouble(amountField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid amount.");
                    return;
                }

                if (!ratesMap.containsKey(source) || !ratesMap.containsKey(target)) {
                    JOptionPane.showMessageDialog(frame, "Invalid currency code entered.");
                    return;
                }

                Map<String, java.util.List<Edge>> graph = buildGraph(ratesMap);
                Result result = maxConversionPath(graph, source, target);
                if (result.rate == 0) {
                    outputArea.setText("❌ No conversion path found.");
                    return;
                }

                double converted = amount * result.rate;
                StringBuilder sb = new StringBuilder();
                sb.append("✅ Best Conversion Path: ").append(String.join(" → ", result.path)).append("\n");
                sb.append(String.format("💰 Converted Amount: %.2f %s\n\n", converted, target));

                int[] denominations = {2000, 500, 200, 100, 50, 20, 10, 5, 1};
                Map<Integer, Integer> breakdown = denominationBreakdown((int) converted, denominations);
                sb.append("💵 Denomination Breakdown:\n");
                for (Map.Entry<Integer, Integer> entry : breakdown.entrySet()) {
                    sb.append("₹").append(entry.getKey()).append(": ").append(entry.getValue()).append(" note(s)\n");
                }

                outputArea.setText(sb.toString());
            });

            frame.setVisible(true);
        });
    }
}

