package buffer_files;
public class Main {
    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();
        StockDataManager dataManager = new StockDataManager();
        MinMaxTracker tracker = new MinMaxTracker();
        MovingAverageCalculator maCalculator = new MovingAverageCalculator();
        ProfitCalculator profitCalculator = new ProfitCalculator();

        inputHandler.captureInput(dataManager);
        dataManager.displayMenu(maCalculator, tracker, profitCalculator);
    }
}
