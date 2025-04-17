package buffer_files;

import java.util.List;

public class MinMaxTracker {

    public double getMin(List<Double> prices, int start, int end) {
        double min = Double.MAX_VALUE;
        for (int i = start; i <= end; i++) {
            min = Math.min(min, prices.get(i));
        }
        return min;
    }

    public double getMax(List<Double> prices, int start, int end) {
        double max = Double.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            max = Math.max(max, prices.get(i));
        }
        return max;
    }

    public String getAction(double currentPrice, double movingAverage, double min, double max) {
        if (currentPrice == min) {
            return "BUY";
        } else if (currentPrice == max) {
            return "SELL";
        } else if ((currentPrice >= movingAverage - 0.5 && currentPrice <= movingAverage + 0.5)) {
            return "WAIT";
        } else if (currentPrice < movingAverage - 0.5) {
            return "BUY";
        } else if (currentPrice > movingAverage + 0.5) {
            return "SELL";
        } else {
            return "WAIT"; // default catch
        }
    }

}
