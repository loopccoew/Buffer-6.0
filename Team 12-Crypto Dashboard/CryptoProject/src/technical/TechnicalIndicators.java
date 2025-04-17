package technical;

import java.util.List;
import java.util.ArrayList;

public class TechnicalIndicators {
    public static double calculateRSI(List<Double> prices, int period) {
        if (prices.size() < period + 1)
            return 50.0;

        List<Double> gains = new ArrayList<>();
        List<Double> losses = new ArrayList<>();

        for (int i = 1; i < prices.size(); i++) {
            double change = prices.get(i) - prices.get(i - 1);
            gains.add(Math.max(0, change));
            losses.add(Math.max(0, -change));
        }

        double avgGain = gains.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double avgLoss = losses.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        if (avgLoss == 0)
            return 100.0;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    public static List<Double> calculateMA(List<Double> prices, int period) {
        List<Double> ma = new ArrayList<>();
        for (int i = period - 1; i < prices.size(); i++) {
            double sum = 0;
            for (int j = 0; j < period; j++) {
                sum += prices.get(i - j);
            }
            ma.add(sum / period);
        }
        return ma;
    }

    public static List<Double> calculateBollingerBands(List<Double> prices, int period) {
        List<Double> bands = new ArrayList<>();
        List<Double> sma = calculateMA(prices, period);

        // Calculate standard deviation
        double stdDev = calculateStandardDeviation(prices, period);

        // Upper band = MA + (2 × σ)
        bands.add(sma.get(sma.size() - 1) + (2 * stdDev));
        // Middle band = MA
        bands.add(sma.get(sma.size() - 1));
        // Lower band = MA - (2 × σ)
        bands.add(sma.get(sma.size() - 1) - (2 * stdDev));

        return bands;
    }

    private static double calculateStandardDeviation(List<Double> prices, int period) {
        double mean = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = prices.stream()
                .mapToDouble(price -> Math.pow(price - mean, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }
}
