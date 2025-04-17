package buffer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Login 
{
	 static void showDashboard(String currentUserName) {
	        JFrame dashboard = new JFrame("Welcome, " + currentUserName);
	        dashboard.setSize(400, 250);
	        dashboard.setLayout(null);
	        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        dashboard.getContentPane().setBackground(new Color(255, 192, 203));

	        JLabel welcome = new JLabel("Hi " + currentUserName + ", what would you like to manage?");
	        welcome.setBounds(40, 30, 320, 30);
	        welcome.setFont(new Font("Arial", Font.BOLD, 14));
	        dashboard.add(welcome);

	        JButton stockBtn = new JButton("Stocks");
	        stockBtn.setBounds(50, 100, 120, 40);
	        stockBtn.setBackground(new Color(100, 149, 237));
	        stockBtn.setForeground(new Color(255, 192, 203));
	        dashboard.add(stockBtn);

	        JButton investBtn = new JButton("Investments");
	        investBtn.setBounds(200, 100, 120, 40);
	        investBtn.setBackground(new Color(60, 179, 113));
	        investBtn.setForeground(new Color(255, 192, 203));
	        dashboard.add(investBtn);

	        stockBtn.addActionListener(e -> {
	            dashboard.dispose();
	            StockInvestmentUI.launchUI(true, currentUserName);  // for Stocks

	        });

	        investBtn.addActionListener(e -> {
	            dashboard.dispose();
	            StockInvestmentUI.launchUI(false, currentUserName);  // for Investments

	        });

	        dashboard.setVisible(true);
	    }
}
