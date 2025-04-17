package buffer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockInvestmentUI {

    private static final StockManager stockManager = new StockManager();
    private static final InvestmentManager investmentManager = new InvestmentManager();

    public static void launchUI(boolean showStocks, String currentUserName)
 {
        JFrame frame = new JFrame(showStocks ? "Stock Manager" : "Investment Manager");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        JTextArea outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setBounds(320, 380, 440, 150);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.add(outputArea);

        // BACK TO DASHBOARD BUTTON
        JButton backBtn = new JButton("← Back");
        backBtn.setBounds(20, 500, 100, 30);
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(new Color(255, 192, 203));
        backBtn.setFocusPainted(false);
        frame.add(backBtn);

        backBtn.addActionListener(e -> {
            frame.dispose();
            Login.showDashboard(currentUserName); // Go back to dashboard with username
        });

        if (showStocks) {
            JButton fetchStockData = new JButton("Fetch Real-Time Stock Data");
            JButton showBestStocks = new JButton("Show Best Stocks");
            JButton showAllStocks = new JButton("Show All Stocks");

            JButton[] buttons = {fetchStockData, showBestStocks, showAllStocks};

            int y = 60;
            for (JButton btn : buttons) {
                btn.setBounds(50, y, 250, 35);
                btn.setBackground(new Color(70, 130, 180));
                btn.setForeground(new Color(255, 192, 203));
                btn.setFocusPainted(false);
                btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
                frame.add(btn);
                y += 45;
            }

            String[] columns = {"Symbol", "Stock Name", "Current Price", "Price Change (%)"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
            JTable stockTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(stockTable);
            scrollPane.setBounds(320, 60, 440, 300);
            frame.add(scrollPane);

            fetchStockData.addActionListener(e -> {
                stockManager.fetchRealTimeStockData();
                outputArea.setText("Real-time stock data fetched successfully.");
            });

            showBestStocks.addActionListener(e -> {
                List<Stock> bestStocks = stockManager.getBestStocks();
                tableModel.setRowCount(0);
                for (Stock stock : bestStocks) {
                    tableModel.addRow(new Object[]{stock.symbol, stock.name, "₹" + stock.currentPrice, stock.priceChangePercent + "%"});
                }
                outputArea.setText("Best performing stocks displayed.");
            });

            showAllStocks.addActionListener(e -> {
                List<Stock> allStocks = stockManager.getAllStocks();
                tableModel.setRowCount(0);
                for (Stock stock : allStocks) {
                    tableModel.addRow(new Object[]{stock.symbol, stock.name, "₹" + stock.currentPrice, stock.priceChangePercent + "%"});
                }
                outputArea.setText("All stocks displayed.");
            });

        } else {
            JButton addInvestment = new JButton("Add Investment");
            JButton searchInvestment = new JButton("Search Investment");
            JButton showInvestments = new JButton("Show All Investments");
            JButton deleteInvestment = new JButton("Delete Investment");

            JButton[] buttons = {addInvestment, searchInvestment, showInvestments, deleteInvestment};

            int y = 60;
            for (JButton btn : buttons) {
                btn.setBounds(50, y, 250, 35);
                btn.setBackground(new Color(34, 139, 34));
                btn.setForeground(new Color(255, 192, 203));
                btn.setFocusPainted(false);
                btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
                frame.add(btn);
                y += 45;
            }

            addInvestment.addActionListener(e -> {
                String category = JOptionPane.showInputDialog("Enter investment category:");
                String name = JOptionPane.showInputDialog("Enter investment name:");
                String amtStr = JOptionPane.showInputDialog("Enter investment amount:");
                try {
                    int amt = Integer.parseInt(amtStr);
                    if (amt <= 0) throw new NumberFormatException();
                    investmentManager.addInvestment(new Investment(category, name, amt));
                    outputArea.setText("Investment added: " + name);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            searchInvestment.addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Enter investment name to search:");
                Investment found = investmentManager.searchInvestment(name);
                outputArea.setText(found == null ? "Investment not found." : found.toString());
            });

            showInvestments.addActionListener(e -> {
                List<Investment> all = investmentManager.getInvestments();
                StringBuilder sb = new StringBuilder("Investments:\n");
                for (Investment inv : all) sb.append(inv).append("\n");
                outputArea.setText(sb.toString());
            });

            deleteInvestment.addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Enter investment name to delete:");
                boolean removed = investmentManager.deleteInvestment(name);
                outputArea.setText(removed ? "Deleted successfully." : "Investment not found.");
            });
        }

        frame.setVisible(true);
    }
}
