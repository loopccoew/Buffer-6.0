package buffer_files;
import java.util.List;

public class MovingAverageCalculator {
    public double getMovingAverage(List<Double> prices, int windowSize, int endIndex) {
        if (endIndex < windowSize - 1) return 0;
        int sum = 0;
        for (int i = endIndex - windowSize + 1; i <= endIndex; i++) {
            sum += prices.get(i);
        }
        return (double) sum / windowSize;
    }
}

