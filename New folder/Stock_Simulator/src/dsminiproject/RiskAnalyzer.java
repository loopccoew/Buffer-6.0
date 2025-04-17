package dsminiproject;

import java.util.*;
import java.text.DecimalFormat;

public class RiskAnalyzer {

    // Hardcoded 7-day price data
    private static final Map<String, double[]> stockPriceHistory = new HashMap<>();

    static {
        stockPriceHistory.put("Maruti Suzuki", new double[]{9000, 9050, 8900, 9100, 8950, 8800, 9200});
        stockPriceHistory.put("Bajaj finance", new double[]{7800, 7900, 7750, 7850, 8000, 7700, 7950});
        stockPriceHistory.put("Titan company", new double[]{2800, 2820, 2780, 2750, 2770, 2790, 2810});
        stockPriceHistory.put("Ultratech Cement", new double[]{7200, 7300, 7100, 7000, 7050, 7150, 7250});
        stockPriceHistory.put("SBI", new double[]{620, 610, 615, 630, 625, 618, 622});
        stockPriceHistory.put("Asian Paints", new double[]{3000, 3010, 2990, 3020, 3030, 2980, 2970});
        stockPriceHistory.put("TATA motors", new double[]{520, 516.7, 522.44, 523.6, 529.72, 534.54, 532.07});
        stockPriceHistory.put("Nestle India", new double[]{2300, 2277.88, 2265.25, 2260.62, 2279.96, 2282.17, 2287.61});
        stockPriceHistory.put("Bhartiya Airtel", new double[]{700, 698.01, 691.66, 690.31, 689.45, 682.63, 678.02});
        stockPriceHistory.put("Hero MotoCorp", new double[]{3200, 3169.38, 3153.29, 3116.45, 3098.71, 3131.63, 3142.78});
        stockPriceHistory.put("Wipro", new double[]{415, 418.48, 414.49, 415.62, 414.0, 413.38, 411.87});
        stockPriceHistory.put("ITC", new double[]{500, 502.22, 497.12, 497.52, 501.34, 499.7, 504.58});
        stockPriceHistory.put("Adani green energy", new double[]{950, 939.22, 950.56, 948.96, 948.59, 957.7, 957.53});
        stockPriceHistory.put("HUL", new double[]{2500, 2481.28, 2491.91, 2495.5, 2503.73, 2525.35, 2541.34});
        stockPriceHistory.put("Power grid", new double[]{230, 230.41, 232.35, 233.54, 231.28, 229.41, 228.44});
        stockPriceHistory.put("TCS", new double[]{3500, 3481.95, 3452.38, 3435.53, 3465.21, 3456.95, 3488.49});
        stockPriceHistory.put("Coal India", new double[]{240, 238.12, 235.73, 236.88, 239.66, 242.17, 240.45});
        stockPriceHistory.put("NTPC", new double[]{210, 209.83, 208.12, 207.83, 210.05, 210.09, 208.64});
        stockPriceHistory.put("Infosys", new double[]{1500, 1492.85, 1484.73, 1498.8, 1487.07, 1478.26, 1475.9});
        stockPriceHistory.put("Reliance", new double[]{2400.5, 2403.19, 2403.8, 2380.11, 2379.66, 2383.3, 2394.11});
        stockPriceHistory.put("Alok Industries", new double[]{18, 17.98, 17.77, 17.68, 17.78, 17.69, 17.82});
        stockPriceHistory.put("Aarti Drugs", new double[]{500, 502.15, 500.22, 501.59, 507.04, 510.34, 504.6});
        stockPriceHistory.put("Trident Ltd", new double[]{32, 31.87, 32.23, 32.13, 32.37, 32.02, 32.18});
        stockPriceHistory.put("Goa Carbon", new double[]{300, 295.7, 291.98, 292.29, 294.02, 295.81, 295.95});
        stockPriceHistory.put("TV Network", new double[]{110, 108.78, 107.82, 108.55, 109.92, 111.2, 110.59});
        stockPriceHistory.put("Hatsun curd", new double[]{750, 750.52, 750.12, 741.6, 751.59, 754.45, 755.22});
        stockPriceHistory.put("Tata Elxsi", new double[]{80, 79.64, 80.11, 79.52, 79.54, 79.74, 80.22});
        stockPriceHistory.put("India Cements", new double[]{50, 49.91, 50.14, 49.8, 49.37, 49.1, 48.89});
        stockPriceHistory.put("Sequent Scientific", new double[]{75, 74.36, 73.85, 74.31, 73.84, 74.83, 74.75});
        stockPriceHistory.put("JBM Auto", new double[]{135, 133.64, 135.25, 133.58, 135.5, 134.94, 133.88});
        stockPriceHistory.put("Eimco Elecon", new double[]{250, 251.57, 250.83, 249.92, 247.33, 249.41, 252.72});
        stockPriceHistory.put("Shalimae Paints", new double[]{85, 85.76, 85.17, 84.79, 84.58, 84.95, 85.49});
        stockPriceHistory.put("TCL Express", new double[]{380, 376.53, 377.17, 381.88, 385.89, 387.22, 389.95});
        stockPriceHistory.put("Minda Corporation", new double[]{95, 96.06, 96.08, 94.93, 95.99, 96.9, 98.01});
        stockPriceHistory.put("Sequent Scientific 2", new double[]{5, 4.93, 4.9, 4.86, 4.86, 4.83, 4.87});
        stockPriceHistory.put("Kokuyo Camlin", new double[]{50, 50.11, 49.99, 50.0, 50.26, 50.68, 50.2});
        stockPriceHistory.put("Nitin Spinners", new double[]{46, 45.46, 45.12, 44.61, 44.9, 44.75, 44.31});
        stockPriceHistory.put("Zen Technologies", new double[]{180, 179.69, 177.49, 178.68, 178.62, 178.41, 180.5});
        stockPriceHistory.put("Trigyn Technologies", new double[]{55, 55.44, 55.76, 56.05, 56.06, 55.7, 56.12});
        stockPriceHistory.put("Hester Biosciences", new double[]{15, 14.84, 14.86, 15.03, 14.99, 14.82, 14.94});
        stockPriceHistory.put("Dwarkish Sugar", new double[]{45, 44.96, 45.33, 44.77, 44.82, 44.27, 44.49});
    }

    public static void calculateRiskScore(String stockName) {
        double[] prices = stockPriceHistory.get(stockName);
        if (prices == null) {
            System.out.println("No historical data available for stock: " + stockName);
            return;
        }

        // Calculate mean
        double sum = 0;
        for (double price : prices) {
            sum += price;
        }
        double mean = sum / prices.length;

        // Calculate standard deviation
        double variance = 0;
        for (double price : prices) {
            variance += Math.pow(price - mean, 2);
        }
        variance /= prices.length;
        double stdDev = Math.sqrt(variance);

        // Map standard deviation to a 1–10 risk score
        int riskScore;
        if (stdDev <= 50) riskScore = 1;
        else if (stdDev <= 100) riskScore = 2;
        else if (stdDev <= 150) riskScore = 3;
        else if (stdDev <= 200) riskScore = 4;
        else if (stdDev <= 250) riskScore = 5;
        else if (stdDev <= 300) riskScore = 6;
        else if (stdDev <= 350) riskScore = 7;
        else if (stdDev <= 400) riskScore = 8;
        else if (stdDev <= 450) riskScore = 9;
        else riskScore = 10;

        // Risk category
        String riskCategory;
        if (riskScore <= 3) riskCategory = "Low Risk";
        else if (riskScore <= 7) riskCategory = "Medium Risk";
        else riskCategory = "High Risk";

        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("--------------------------------------------------");
        System.out.println("Stock: " + stockName);
        System.out.println("Standard Deviation: " + df.format(stdDev));
        System.out.println("Risk Score: " + riskScore + " (" + riskCategory + ")");
        System.out.println("--------------------------------------------------");
    }
}
