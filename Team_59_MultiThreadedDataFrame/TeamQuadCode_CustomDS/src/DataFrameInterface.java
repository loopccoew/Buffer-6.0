import java.util.*;
import java.io.*;

public class DataFrameInterface {
    private MultiThreadedDataFrame df;
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        try {
            System.out.print("Enter path to CSV file: ");
            String path = scanner.nextLine();
            df = new MultiThreadedDataFrame(path);
            System.out.println("\nCSV Loaded. Rows: " + df.getRowCount());
            df.printBenchmarks();

            boolean running = true;
            while (running) {
                showMenu();
                int choice = getValidChoice();

                System.out.println("\n----------------------------------");

                switch (choice) {
                    case 1 -> {
                        df.printTopRows(6);
                        autoExport();
                        waitForEnter();
                    }
                    case 2 -> {
                        handleSort();
                        autoExport();
                        waitForEnter();
                    }
                    case 3 -> {
                        handleFilter();
                        autoExport();
                        waitForEnter();
                    }
                    case 4 -> {
                        handleGroupBy();
                        waitForEnter();
                    }
                    case 0 -> {
                        System.out.println("\nExiting... Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a valid option.");
                }

                System.out.println("----------------------------------\n");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showMenu() {
        System.out.println("""
            ==================================
            📊   DataFrame Interface Menu
            ==================================
            1. Print top 6 rows
            2. Sort by column
            3. Filter rows
            4. Group by + Aggregate
            0. Exit
        """);
        System.out.print("Choose an option: ");
    }

    private int getValidChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 0 && choice <= 4) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 0 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void handleSort() {
        System.out.print("Enter the column to sort by: ");
        String col = scanner.nextLine();

        if (!df.columnExists(col)) {
            System.out.println("Error: Column '" + col + "' does not exist.");
            return;
        }

        System.out.print("Ascending? (true/false): ");
        boolean asc = Boolean.parseBoolean(scanner.nextLine());
        df.sortBy(col, asc);
        System.out.println("Data sorted by column: " + col + (asc ? " in ascending order." : " in descending order."));
        df.printBenchmarks();
    }

    private void handleFilter() {
        System.out.print("Enter the column to filter by: ");
        String col = scanner.nextLine();

        if (!df.columnExists(col)) {
            System.out.println("Error: Column '" + col + "' does not exist.");
            return;
        }

        System.out.print("Enter the minimum value for filtering: ");
        int threshold = getValidIntInput();

        df = df.filter(row -> Integer.parseInt(row.get(col)) > threshold);
        System.out.println("Rows filtered where '" + col + "' > " + threshold);
    }

    private void handleGroupBy() {
        System.out.print("Enter the column to group by: ");
        String groupCol = scanner.nextLine();

        if (!df.columnExists(groupCol)) {
            System.out.println("Error: Column '" + groupCol + "' does not exist.");
            return;
        }

        System.out.print("Enter the column to aggregate: ");
        String aggCol = scanner.nextLine();

        if (!df.columnExists(aggCol)) {
            System.out.println("Error: Column '" + aggCol + "' does not exist.");
            return;
        }

        System.out.print("Enter the aggregation operation (sum/avg/min/max): ");
        String op = scanner.nextLine();

        Map<String, Double> result = df.groupByAggregate(groupCol, aggCol, op);
        if (result.isEmpty()) {
            System.out.println("No data to display for the selected aggregation.");
        } else {
            System.out.println("\nGrouped Data:");
            result.forEach((k, v) -> System.out.printf("%s => %.2f\n", k, v));
        }

        df.printBenchmarks();
    }

    private int getValidIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private void autoExport() {
        try {
            df.exportToCSV("src/processed.csv");
            System.out.println("🔄 Auto-exported to: src/processed.csv");
        } catch (IOException e) {
            System.out.println("Auto-export failed: " + e.getMessage());
        }
    }

    private void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();

        // Simulate screen clear with newlines
        for (int i = 0; i < 40; i++) System.out.println();
    }
}
