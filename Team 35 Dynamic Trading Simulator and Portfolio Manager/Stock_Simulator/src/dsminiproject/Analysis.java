package dsminiproject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultHighLowDataset;
import java.text.SimpleDateFormat;

public class Analysis {
    private HashMap<Stock, Integer> holdings;

    public Analysis(HashMap<Stock, Integer> holdings) {
        this.holdings = holdings;
//        System.out.println("Holdings size: " + holdings.size());
       

        showMenu();
    }

    public void showMenu() {
        String[] options = {
                "1. Show Candlestick Pattern",
                "2. Show Sector-wise Distribution (Pie Chart)",
                "3. Show Profit/Loss Summary",
                "4. Exit"
        };

        while (true) {
            String input = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Analysis Option:",
                    "Portfolio Analysis Menu",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (input == null || input.startsWith("4")) break;

            switch (input.charAt(0)) {
                case '1': showCandlestickChart(); break;
                case '2': showPieChart(); break;
                case '3': showProfitLoss(); break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option selected.");
            }
        }
    }

    private void showCandlestickChart() {
        int days = 10;
        Date[] date = new Date[days];
        double[] high = new double[days];
        double[] low = new double[days];
        double[] open = new double[days];
        double[] close = new double[days];
        double[] volume = new double[days];

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        for (int i = 0; i < days; i++) {
            cal.add(Calendar.DATE, 1);
            date[i] = cal.getTime();
            open[i] = 100 + Math.random() * 50;
            high[i] = open[i] + Math.random() * 10;
            low[i] = open[i] - Math.random() * 10;
            close[i] = low[i] + Math.random() * (high[i] - low[i]);
            volume[i] = 1000 + Math.random() * 500;
        }

        DefaultHighLowDataset dataset = new DefaultHighLowDataset(
                "Demo Candlestick Data",
                date,
                high,
                low,
                open,
                close,
                volume
        );

        JFreeChart chart = ChartFactory.createCandlestickChart(
                "Candlestick Chart",
                "Date",
                "Price",
                dataset,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame("Candlestick Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void showPieChart() {
        Map<String, Integer> sectorMap = new HashMap<>();
        for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
            String sector = entry.getKey().getSector();
            sectorMap.put(sector, sectorMap.getOrDefault(sector, 0) + entry.getValue());
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : sectorMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Sector-wise Distribution",
                dataset,
                true, true, false
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));

        ChartPanel panel = new ChartPanel(pieChart);
        JFrame frame = new JFrame("Sector Pie Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void showProfitLoss() {
        double totalInvestment = 0.0;
        double currentValue = 0.0;
        StringBuilder details = new StringBuilder();

        Random rand = new Random();

        for (Map.Entry<Stock, Integer> entry : holdings.entrySet()) {
            Stock stock = entry.getKey();
            int quantity = entry.getValue();

            double currentPrice = stock.getPrice();
            double volatility = stock.getVolatility();

            // Simulate buy price based on volatility
            double fluctuation = (rand.nextDouble() * 2 * volatility) - volatility;  // [-vol, +vol]
            double boughtAt = currentPrice * (1 - fluctuation);
            boughtAt = Math.round(boughtAt * 100.0) / 100.0;

            double investment = boughtAt * quantity;
            double current = currentPrice * quantity;

            totalInvestment += investment;
            currentValue += current;

            double profit = current - investment;
            String result = profit >= 0 ? "Profit" : "Loss";
            details.append(String.format(
                    "%s (%d shares): %s of ₹%.2f (Buy: ₹%.2f, Current: ₹%.2f)\n",
                    stock.getName(), quantity, result, Math.abs(profit), boughtAt, currentPrice
            ));
        }

        double profitLoss = currentValue - totalInvestment;
        String summary = String.format(
                "Total Investment: ₹%.2f\nCurrent Value: ₹%.2f\nOverall %s: ₹%.2f\n\nBreakdown:\n%s",
                totalInvestment, currentValue,
                profitLoss >= 0 ? "Profit" : "Loss",
                Math.abs(profitLoss),
                details.toString()
        );

        JOptionPane.showMessageDialog(null, summary, "Profit & Loss Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}
