package dsminiproject;

import java.util.*;

public class GraphCorrelation {
	Scanner sc=new Scanner(System.in);
	String[] IT_stocks = { "WIT", "TCS.NS", "INFY.NS", "TATAELXSI.NS", "TRIGYN.NS" };
	double[][] IT_correlation_graph= {
			{1, 0.94, 0.9, 0.83, 0.07},
			{0.94, 1, 0.96, 0.91, 0.1},
			{0.9, 0.96, 1, 0.93, 0.21},
			{0.83, 0.91, 0.93, 1, 0.37},
			{0.07, 0.1, 0.21, 0.37, 1}
	};
	
	String[] cons_goods_stocks= { "ASIANPAINT.BO", "NESTLEIND.NS", "HINDUNILVR.NS", "HATSUN.NS", "KOKUYOCMLN.NS"};
	double[][] cons_goods_correlation_graph= {
			{1, 0.94, 0.93, 0.23, 0.18},
			{0.94, 1, 0.97, 0.32, 0.29},
			{0.93, 0.97, 1, 0.34, 0.24},
			{0.23, 0.32, 0.34, 1, 0.33},
			{0.18, 0.29, 0.24, 0.33, 1}
	};
	
	String[] automotive_stocks = { "MARUTI.NS", "TATAMOTORS.NS", "HEROMOTOCO.NS", "JBMA.NS", "MINDACORP.BO" };
	double[][] auto_correlation_graph= {
			{1, 0.64, 0.34, 0.36, 0.55},
			{0.64, 1, 0.0016, -0.13, 0.91},
			{0.34, 0.0016, 1, 0.65, 0.24},
			{0.36, -0.13, 0.65, 1, -0.065},
			{0.55, 0.91, 0.24, -0.065, 1}
	};
	
	String[] textile_stocks = { "ALOKINDS.NS", "NITINSPIN.NS", "TTFL.BO" };
	double[][] textile_correlation_graph= {
			{1, 0.46, 0.24},
			{0.46, 1, -0.34},
			{0.24, -0.34, 1}
	};
	
	String[] pharmaceutical_stocks = { "AARTIDRUGS.NS", "SEQUENT.NS", "LUPIN.NS", "GLAXO.NS", "BAX" };
	double[][] pharma_correlation_graph= {
			{1, 0.82, 0.48, 0.89, 0.31},
			{0.82, 1, 0.39, 0.77, -0.019},
			{0.48, 0.39, 1, 0.54, 0.58},
			{0.89, 0.77, 0.54, 1, 0.36},
			{0.31, -0.019, 0.58, 0.36, 1}
	};
	public void correlation_menu() {
		int ch;
		// TODO Auto-generated method stub
		do {
			

			 System.out.println("=====================================\n" + 
                     "               MENU                 \n" +
                     "=====================================\n" + 
                     " 1) Report price change and predict sector impact\n" +
                     " 2) View sector relationship graphs\n" + 
                     " 3) Back to main menu\n" +
                     "=====================================\n" + 
                     "Enter your choice: ");
			ch = sc.nextInt();

			sc.nextLine();

			switch (ch) {

			case 1:
				System.out.print("1. IT\n2.Automotive\n3.Consumer Goods\n4.Textile\n5.Pharmaceutical\nSelect Sector: ");
				int sector=sc.nextInt();
				sc.nextLine();
				
				switch(sector) {
				case 1:
					report_change(IT_stocks, IT_correlation_graph);
					break;
				case 2:
					report_change(automotive_stocks, auto_correlation_graph);
					break;
				case 3:
					report_change(cons_goods_stocks, cons_goods_correlation_graph);
					break;
				case 4:
					report_change(textile_stocks, textile_correlation_graph);
					break;
				case 5:
					report_change(pharmaceutical_stocks, pharma_correlation_graph);
					break;
				default:
					System.out.println("Wrong Sector chosen!");
				}	
				break;

			case 2:
				display_corr_graph();
				break;

			case 3:
				System.out.println("Thank you. Going Back to Simulator Menu");
				return;
				

			default:
				// default statement
				System.out.println("Please enter a correct input.");
				break;
			}
		} while (ch != 3);
	}
	
	public void display_corr_graph() {
		// Print IT stocks and correlation graph
		System.out.print("IT Stocks: ");
		for (String stock : IT_stocks) {
		    System.out.print(stock + " ");
		}
		System.out.println("\nIT Correlation Graph:");
		for (int i = 0; i < IT_correlation_graph.length; i++) {
		    for (int j = 0; j < IT_correlation_graph[i].length; j++) {
		        System.out.print(IT_correlation_graph[i][j] + " ");
		    }
		    System.out.println();
		}

		// Print Pharmaceutical stocks and correlation graph
		System.out.print("\nPharmaceutical Stocks: ");
		for (String stock : pharmaceutical_stocks) {
		    System.out.print(stock + " ");
		}
		System.out.println("\nPharma Correlation Graph:");
		for (int i = 0; i < pharma_correlation_graph.length; i++) {
		    for (int j = 0; j < pharma_correlation_graph[i].length; j++) {
		        System.out.print(pharma_correlation_graph[i][j] + " ");
		    }
		    System.out.println();
		}

		// Print Textile stocks and correlation graph
		System.out.print("\nTextile Stocks: ");
		for (String stock : textile_stocks) {
		    System.out.print(stock + " ");
		}
		System.out.println("\nTextile Correlation Graph:");
		for (int i = 0; i < textile_correlation_graph.length; i++) {
		    for (int j = 0; j < textile_correlation_graph[i].length; j++) {
		        System.out.print(textile_correlation_graph[i][j] + " ");
		    }
		    System.out.println();
		}

		// Print Automotive stocks and correlation graph
		System.out.print("\nAutomotive Stocks: ");
		for (String stock : automotive_stocks) {
		    System.out.print(stock + " ");
		}
		System.out.println("\nAutomotive Correlation Graph:");
		for (int i = 0; i < auto_correlation_graph.length; i++) {
		    for (int j = 0; j < auto_correlation_graph[i].length; j++) {
		        System.out.print(auto_correlation_graph[i][j] + " ");
		    }
		    System.out.println();
		}

		// Print Consumer Goods stocks and correlation graph
		System.out.print("\nConsumer Goods Stocks: ");
		for (String stock : cons_goods_stocks) {
		    System.out.print(stock + " ");
		}
		System.out.println("\nConsumer Goods Correlation Graph:");
		for (int i = 0; i < cons_goods_correlation_graph.length; i++) {
		    for (int j = 0; j < cons_goods_correlation_graph[i].length; j++) {
		        System.out.print(cons_goods_correlation_graph[i][j] + " ");
		    }
		    System.out.println();
		}

	}
	
	public void report_change(String[] stocks, double[][] correlation_graph) {
		boolean found=false;
		int index=-1;
		for (String stock : stocks) {
		    System.out.print(stock + " ");
		}
		System.out.print("\nEnter stock in which change occured: ");
		String current=sc.nextLine().trim();
		for (int i=0;i<stocks.length;i++) {
			String stock=stocks[i];
		    if (stock.equalsIgnoreCase(current)) {
		        found = true;
		        index=i;
		        break;
		    }
		}
		if(found) {
			System.out.print("Report change (in %): ");
			double change=sc.nextDouble();
			
			System.out.println("\nPredicted impact on other IT stocks:");
		    System.out.println("-------------------------------------------------");
		    for (int j = 0; j < stocks.length; j++) {
		        if (j != index) {
		            double predictedImpact = change * correlation_graph[index][j];
		            System.out.printf("%-15s : %.2f%%\n", stocks[j], predictedImpact);
		        }
		}	
	}
		else {System.out.println("Stock not found!");}
	}
	
}
