import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Predicate;

public class MultiThreadedDataFrame {
    private List<String> columns;  // List of column names
    private Map<String, List<String>> data;  // Column-wise storage
    private int rowCount;
    private Map<String, Long> benchmarks = new LinkedHashMap<>();  // Benchmark storage

    public MultiThreadedDataFrame(String csvFilePath) throws IOException {
        columns = new ArrayList<>();
        data = new LinkedHashMap<>();
        long start = System.nanoTime();
        loadCSV(csvFilePath);
        long end = System.nanoTime();
        benchmarks.put("CSV Load", (end - start) / 1_000_000);
    }

    private MultiThreadedDataFrame() {
        // Used internally for filter results
    }

    private void loadCSV(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        // Read header
        if ((line = br.readLine()) != null) {
            columns = Arrays.asList(line.split(","));
            for (String col : columns) {
                data.put(col, new ArrayList<>());
            }
        }

        // Read rows
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length != columns.size()) continue;

            for (int i = 0; i < columns.size(); i++) {
                String value = values[i].trim();
                data.get(columns.get(i)).add(value.isEmpty() ? null : value); // Store null for empty cells
            }

            rowCount++;
        }

        br.close();
    }

    public void print() {
        System.out.println(String.join("\t", columns));
        for (int i = 0; i < rowCount; i++) {
            for (String col : columns) {
                String value = data.get(col).get(i);
                System.out.print((value == null ? "NULL" : value) + "\t");
            }
            System.out.println();
        }
    }

    private List<Row> getRows() {
        List<Row> rows = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Map<String, String> rowMap = new HashMap<>();
            for (String col : columns) {
                rowMap.put(col, data.get(col).get(i));
            }
            rows.add(new Row(rowMap));
        }
        return rows;
    }

    public MultiThreadedDataFrame sortBy(String columnName, boolean ascending) {
        long start = System.nanoTime();

        List<Row> rows = getRows();

        Comparator<Row> comparator = Comparator.comparing(row -> row.get(columnName));

        try {
            comparator = Comparator.comparingInt(row -> Integer.parseInt(row.get(columnName)));
        } catch (NumberFormatException ignored) {}

        if (!ascending) {
            comparator = comparator.reversed();
        }

        rows = rows.parallelStream()
                .sorted(comparator)
                .collect(Collectors.toList());

        for (String col : columns) {
            List<String> colData = new ArrayList<>();
            for (Row row : rows) {
                colData.add(row.get(col));
            }
            data.put(col, colData);
        }

        long end = System.nanoTime();
        benchmarks.put("Parallel Sort by " + columnName, (end - start) / 1_000_000);

        return this;
    }

    public int getRowCount() {
        return rowCount;
    }

    private class Row {
        Map<String, String> values;

        public Row(Map<String, String> values) {
            this.values = values;
        }

        public String get(String column) {
            return values.get(column);
        }
    }

    private MultiThreadedDataFrame fromRows(List<Row> rows) {
        MultiThreadedDataFrame newDF = new MultiThreadedDataFrame();

        newDF.columns = new ArrayList<>(this.columns);
        newDF.data = new LinkedHashMap<>();
        newDF.rowCount = rows.size();

        for (String col : columns) {
            newDF.data.put(col, new ArrayList<>());
        }

        for (Row row : rows) {
            for (String col : columns) {
                newDF.data.get(col).add(row.get(col));
            }
        }

        return newDF;
    }

    public MultiThreadedDataFrame filter(Predicate<Map<String, String>> condition) {
        long start = System.nanoTime();  // Start time for benchmarking

        List<Row> rows = getRows();

        // Perform the filtering operation
        List<Row> filteredRows = rows.parallelStream()
                .filter(row -> condition.test(row.values))
                .collect(Collectors.toList());

        long end = System.nanoTime();  // End time for benchmarking

        // Record the time taken for filtering in the benchmarks map
        benchmarks.put("Parallel Filter", (end - start) / 1_000_000);  // Time in milliseconds

        System.out.println("Filter applied. Rows left: " + filteredRows.size());  // Show number of rows left after filtering

        return fromRows(filteredRows);  // Return a new DataFrame with the filtered data
    }



    public Map<String, Double> groupByAggregate(String groupByCol, String aggCol, String operation) {
        long start = System.nanoTime();

        Map<String, List<Double>> groupedData = new HashMap<>();

        for (int i = 0; i < rowCount; i++) {
            String groupKey = data.get(groupByCol).get(i);
            String valStr = data.get(aggCol).get(i);

            if (valStr == null) continue; // Skip null values

            try {
                double value = Double.parseDouble(valStr);
                groupedData.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(value);
            } catch (NumberFormatException ignored) {
            }
        }

        Map<String, Double> result = new LinkedHashMap<>();

        groupedData.entrySet().parallelStream().forEach(entry -> {
            String key = entry.getKey();
            List<Double> values = entry.getValue();
            double agg;

            switch (operation.toLowerCase()) {
                case "sum":
                    agg = values.stream().mapToDouble(Double::doubleValue).sum();
                    break;
                case "avg":
                    agg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    break;
                case "max":
                    agg = values.stream().mapToDouble(Double::doubleValue).max().orElse(0);
                    break;
                case "min":
                    agg = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported operation: " + operation);
            }

            synchronized (result) {
                result.put(key, agg);
            }
        });

        long end = System.nanoTime();
        benchmarks.put("GroupBy " + groupByCol + " " + operation.toUpperCase() + " of " + aggCol, (end - start) / 1_000_000);

        return result;
    }

    public void exportToCSV(String outputPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

        writer.write(String.join(",", columns));
        writer.newLine();

        for (int i = 0; i < rowCount; i++) {
            List<String> row = new ArrayList<>();
            for (String col : columns) {
                String value = data.get(col).get(i);
                if (value == null) value = "NULL";  // Replace null with a placeholder string
                else if (value.contains(",") || value.contains("\"")) {
                    value = "\"" + value.replace("\"", "\"\"") + "\"";
                }
                row.add(value);
            }
            writer.write(String.join(",", row));
            writer.newLine();
        }

        writer.flush();
        writer.close();
        System.out.println("Data exported to " + outputPath);
    }

    public void printBenchmarks() {
        System.out.println("\n--- Benchmark Results (ms) ---");
        for (Map.Entry<String, Long> entry : benchmarks.entrySet()) {
            System.out.printf("%-60s : %d ms\n", entry.getKey(), entry.getValue());
        }
    }
 // Inside your MultiThreadedDataFrame class
    public boolean columnExists(String columnName) {
        return columns.contains(columnName);
    }
    
    public void printTopRows(int n) {
        // Determine column widths
        Map<String, Integer> columnWidths = new HashMap<>();
        for (String col : columns) {
            int maxLen = col.length();
            List<String> values = data.get(col);
            for (int i = 0; i < Math.min(n, rowCount); i++) {
                String val = values.get(i);
                if (val == null) val = "NULL";
                maxLen = Math.max(maxLen, val.length());
            }
            columnWidths.put(col, maxLen);
        }

        // Print headers
        for (String col : columns) {
            System.out.print(padRight(col, columnWidths.get(col) + 2)); // add spacing
        }
        System.out.println();

        // Print rows
        for (int i = 0; i < Math.min(n, rowCount); i++) {
            for (String col : columns) {
                String value = data.get(col).get(i);
                if (value == null) value = "NULL";
                System.out.print(padRight(value, columnWidths.get(col) + 2));
            }
            System.out.println();
        }
    }

    // Utility method for padding strings
    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }




}
