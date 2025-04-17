package dsminiproject;

import java.util.*;

public class PortfolioRiskAnalyzer {

    // 1. Diversification Score
    public double calculateDiversificationScore(List<Stock> holdings) {
        Map<String, Integer> sectorCount = new HashMap<>();
        for (Stock stock : holdings) {
            sectorCount.put(stock.getSector(), sectorCount.getOrDefault(stock.getSector(), 0) + 1);
        }

        int total = holdings.size();
        int max = Collections.max(sectorCount.values());
        return 1.0 - (double) max / total;  // closer to 1 = better diversification
    }
    
//    // 2. Risk Alerts
//    public void riskAlert(List<Stock> holdings) {
//        Map<String, Integer> sectorCount = new HashMap<>();
//        for (Stock s : holdings)
//            sectorCount.put(s.getSector(), sectorCount.getOrDefault(s.getSector(), 0) + 1);
//
//        int total = holdings.size();
//        for (Map.Entry<String, Integer> entry : sectorCount.entrySet()) {
//            double ratio = (double) entry.getValue() / total;
//            if (ratio > 0.6)
//                System.out.println("⚠️ High exposure in sector: " + entry.getKey());
//        }
//
//        Map<String, double[]> priceData = simulateHistoricalPrices(holdings); // Stub/mock
//        for (int i = 0; i < holdings.size(); i++) {
//            for (int j = i + 1; j < holdings.size(); j++) {
//                double corr = correlation(
//                        priceData.get(holdings.get(i).getName()),
//                        priceData.get(holdings.get(j).getName()));
//                if (corr > 0.85) {
//                    System.out.printf("⚠️ High correlation (%.2f) between %s and %s\n", corr,
//                            holdings.get(i).getName(), holdings.get(j).getName());
//                }
//            }
//        }
//    }

    public void riskAlert(List<Stock> holdings) {
        if (holdings.isEmpty()) {
            System.out.println("ℹ️ Your portfolio is empty. No risk analysis available.");
            return;
        }

        Map<String, Integer> sectorCount = new HashMap<>();
        for (Stock s : holdings)
            sectorCount.put(s.getSector(), sectorCount.getOrDefault(s.getSector(), 0) + 1);

        int total = holdings.size();
        boolean highSectorExposure = false;
        System.out.println("📊 Sector Exposure Analysis:");
        for (Map.Entry<String, Integer> entry : sectorCount.entrySet()) {
            double ratio = (double) entry.getValue() / total;
            System.out.printf("- %s: %.2f%%\n", entry.getKey(), ratio * 100);
            if (ratio > 0.6) {
                System.out.println("\u001B[33m⚠️ High exposure in sector: " + entry.getKey() + "\u001B[0m");
                highSectorExposure = true;
            }
        }

        Map<String, double[]> priceData = simulateHistoricalPrices(holdings); // Stub/mock
        boolean highCorrelationFound = false;
        System.out.println("\n🔗 Correlation Check:");
        for (int i = 0; i < holdings.size(); i++) {
            for (int j = i + 1; j < holdings.size(); j++) {
                double corr = correlation(
                        priceData.get(holdings.get(i).getName()),
                        priceData.get(holdings.get(j).getName()));
                if (corr > 0.85) {
                    System.out.printf("\u001B[33m⚠️ High correlation (%.2f) between %s and %s\u001B[0m\n", corr,
                            holdings.get(i).getName(), holdings.get(j).getName());
                    highCorrelationFound = true;
                }
            }
        }

        // Optional: Add Volatility-based risk
        double avgVolatility = holdings.stream().mapToDouble(Stock::getVolatility).average().orElse(0.0);
        if (avgVolatility > 1.5) {
            System.out.printf("\n\u001B[31m⚠️ ALERT: High average volatility (%.2f)! Consider adding stable stocks.\u001B[0m\n", avgVolatility);
        }

        // Overall summary
        System.out.println("\n📌 Risk Summary:");
        if (highSectorExposure || highCorrelationFound || avgVolatility > 1.5) {
            System.out.println("\u001B[31m🚨 Portfolio Risk Detected! Please diversify your holdings.\u001B[0m");
        } else {
            System.out.println("\u001B[32m✅ Portfolio appears well-diversified and stable.\u001B[0m");
        }
    }

    // 3. Diversification Suggestions
    public void suggestDiversification(List<Stock> holdings, List<Stock> stockUniverse) {
        Set<String> heldSectors = new HashSet<>();
        Map<String, Integer> sectorCount = new HashMap<>();

        for (Stock s : holdings) {
            heldSectors.add(s.getSector());
            sectorCount.put(s.getSector(), sectorCount.getOrDefault(s.getSector(), 0) + 1);
        }

        String weakestSector = null;
        int min = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : sectorCount.entrySet()) {
            if (entry.getValue() < min) {
                min = entry.getValue();
                weakestSector = entry.getKey();
            }
        }

        System.out.println("📌 Suggested Stocks from other sectors:");
        for (Stock s : stockUniverse) {
            if (!heldSectors.contains(s.getSector())) {
                System.out.printf("👉 %s (%s)\n", s.getName(), s.getSector());
            }
        }

        System.out.println("📌 Add more to diversify: " + weakestSector);
    }

    // Final integration method
    public void riskAndDiversificationAnalysis(List<Stock> holdings, List<Stock> stockUniverse) {
        System.out.println("=== Portfolio Risk & Diversification Analysis ===");

        double score = calculateDiversificationScore(holdings);
        System.out.printf("Diversification Score: %.2f\n", score);

        riskAlert(holdings);
        suggestDiversification(holdings, stockUniverse);
    }

    // Mock for correlation calculation
    private double correlation(double[] prices1, double[] prices2) {
        // Simple Pearson correlation mock
        int n = prices1.length;
        double mean1 = 0, mean2 = 0;
        for (int i = 0; i < n; i++) {
            mean1 += prices1[i];
            mean2 += prices2[i];
        }
        mean1 /= n;
        mean2 /= n;

        double num = 0, denom1 = 0, denom2 = 0;
        for (int i = 0; i < n; i++) {
            double diff1 = prices1[i] - mean1;
            double diff2 = prices2[i] - mean2;
            num += diff1 * diff2;
            denom1 += diff1 * diff1;
            denom2 += diff2 * diff2;
        }
        return num / Math.sqrt(denom1 * denom2);
    }

    // Mock for historical price simulation
    private Map<String, double[]> simulateHistoricalPrices(List<Stock> holdings) {
        Map<String, double[]> data = new HashMap<>();
        Random rand = new Random();

        for (Stock s : holdings) {
            double[] prices = new double[30];
            prices[0] = 100 + rand.nextDouble() * 50;
            for (int i = 1; i < 30; i++) {
                prices[i] = prices[i - 1] + rand.nextGaussian(); // simulate small daily changes
            }
            data.put(s.getName(), prices);
        }

        return data;
    }
}
