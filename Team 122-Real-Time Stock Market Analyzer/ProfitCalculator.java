package buffer_files;

import java.util.List;
public class ProfitCalculator {
    public double getMaxProfit(List<Double> prices) {
        if (prices == null || prices.size() < 2) return 0;
        double minPrice = prices.get(0);
        double maxProfit = 0;

        for (int i = 1; i < prices.size(); i++) {
            double profit = prices.get(i) - minPrice;
            maxProfit = Math.max(maxProfit, profit);
            minPrice = Math.min(minPrice, prices.get(i));
        }
        return maxProfit;
    }
}

