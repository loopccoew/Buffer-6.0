import java.util.*;

public class CurrencyArbitrage {

    private final HashMap<String, Double> exchangeRatesToUSD = new HashMap<>(); ////A hashmap that stores the exchange rate of all currencies relative to USD
    private final HashMap<String, Integer> currencyToIndex = new HashMap<>(); //Assigns each currency an index
    private final HashMap<Integer, String> indexToCurrency = new HashMap<>(); //Converts each index back to currency
    private final Map<Integer, List<ForexRate>> adjList = new HashMap<>(); //Building the graph

    public CurrencyArbitrage() {
        initializeExchangeRates(exchangeRatesToUSD);
        mappingCurrencies(currencyToIndex, indexToCurrency, exchangeRatesToUSD);
        buildGraph(exchangeRatesToUSD, adjList);
    }

    //A graph that will store the foreign exchange rates for all currency combinations
    public static class ForexRate {

        // curr1(to) -> curr(2) : Exchange rate = e^(-weight)
        private final int curr1, curr2; //Indices of currencies
        private final double weight;

        ForexRate(int curr1, int curr2, double rate) {
            this.curr1 = curr1;
            this.curr2 = curr2;
            this.weight = -Math.log(rate); //For detecting arbitrage(profit)
        }

        public int getFrom() {
            return curr1;
        }

        public int getTo() {
            return curr2;
        }

        public double getWeight() {
            return weight;
        }
    }

    // Class to store path information
    private static class Path {
        List<Integer> nodes;
        double totalWeight;

        Path() {
            this.nodes = new ArrayList<>();
            this.totalWeight = 0.0;
        }

        Path(Path other) {
            this.nodes = new ArrayList<>(other.nodes);
            this.totalWeight = other.totalWeight;
        }

        void add(int node, double edgeWeight) {
            nodes.add(node);
            totalWeight += edgeWeight;
        }
    }

    // Method to initialize exchange rates to USD
    private void initializeExchangeRates(HashMap<String, Double> rates) {
        rates.put("USD", 1.0);
        rates.put("EUR", 1.09);
        rates.put("GBP", 1.28);
        rates.put("JPY", 0.0068);
        rates.put("AUD", 0.60);
        /*
        rates.put("CAD", 0.70);
        rates.put("CHF", 1.17);
        rates.put("CNY", 0.14);
        rates.put("INR", 0.012);
        rates.put("NZD", 0.56);
        rates.put("SGD", 0.74);
        rates.put("HKD", 0.13);
        rates.put("SEK", 0.091);
        rates.put("NOK", 0.11);
        rates.put("MXN", 0.048);
        rates.put("BRL", 0.1711);
        rates.put("ZAR", 0.051);
        rates.put("AED", 0.27);
        rates.put("ARS", 0.00093);
        rates.put("CLP", 0.0010);
        rates.put("COP", 0.00023);
        rates.put("DKK", 0.15);
        rates.put("HUF", 0.0027);
        rates.put("IDR", 0.000067);
        rates.put("ILS", 0.27);
        rates.put("MYR", 0.226);
        rates.put("PHP", 0.017);
        rates.put("THB", 0.029);
        rates.put("TWD", 0.030);
        rates.put("VND", 0.000038);
        rates.put("PKR", 0.0036);
        rates.put("NGN", 0.00064);
        rates.put("RUB", 0.012);
        rates.put("CZK", 0.043);
        rates.put("RON", 0.22);
        rates.put("SAR", 0.27);
        rates.put("QAR", 0.27);
        rates.put("KWD", 3.25);
        rates.put("BHD", 2.65);
        rates.put("OMR", 2.60);
        rates.put("JOD", 1.41);

         */
    }

    //Mapping currencies to index
    private void mappingCurrencies(HashMap<String, Integer> currencyToIndex,
                                   HashMap<Integer, String> indexToCurrency,
                                   HashMap<String, Double> exchangeRatesToUSD) {
        int idx = 0;
        for(String currency : exchangeRatesToUSD.keySet()) {
            currencyToIndex.put(currency, idx);
            indexToCurrency.put(idx++, currency);
        }
    }

    //Populating the graph
    private void buildGraph(HashMap<String, Double> exchangeRatesToUSD,
                            Map<Integer, List<ForexRate>> adjList) {
        for (String from : exchangeRatesToUSD.keySet()) {
            int fromIdx = currencyToIndex.get(from);
            adjList.putIfAbsent(fromIdx, new ArrayList<>());

            for (String to : exchangeRatesToUSD.keySet()) {
                if (!from.equals(to)) {
                    double rate = exchangeRatesToUSD.get(from) / exchangeRatesToUSD.get(to);
                    int toIdx = currencyToIndex.get(to);
                    adjList.get(fromIdx).add(new ForexRate(fromIdx, toIdx, rate));
                }
            }
        }
    }

    //Method to print all the exchange rates for all currencies to USD
    public void printExchangeRates() {
        System.out.println("Currency to USD Exchange Rates:");
        for (String currency : exchangeRatesToUSD.keySet()) {
            System.out.printf("1 %s = %.4f USD%n", currency, exchangeRatesToUSD.get(currency));
        }
    }

    //Method to print graph
    public void printGraph() {
        System.out.println("\n--- Currency Exchange Graph ---");
        for (Map.Entry<Integer, List<ForexRate>> entry : adjList.entrySet()) {
            String fromCurrency = indexToCurrency.get(entry.getKey());
            for (ForexRate edge : entry.getValue()) {
                String toCurrency = indexToCurrency.get(edge.getTo());
                double actualRate = Math.exp(-edge.getWeight());
                System.out.printf("1 %s -> %.4f %s%n", fromCurrency, actualRate, toCurrency);
            }
        }
    }

    public boolean findMostProfitablePath(String source, String destination) {
        int n = getCurrencyCount();
        double[] dist = new double[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);

        Integer srcIdx = currencyToIndex.get(source);
        Integer destIdx = currencyToIndex.get(destination);

        if (srcIdx == null || destIdx == null) {
            System.out.println("Invalid currency input.");
            return true;
        }

        dist[srcIdx] = 0.0;

        // Bellman-Ford: Relax edges (n - 1) times
        for (int i = 0; i < n - 1; i++) {
            for (Map.Entry<Integer, List<ForexRate>> entry : adjList.entrySet()) {
                for (ForexRate edge : entry.getValue()) {
                    if (dist[edge.getFrom()] + edge.getWeight() < dist[edge.getTo()]) {
                        dist[edge.getTo()] = dist[edge.getFrom()] + edge.getWeight();
                        prev[edge.getTo()] = edge.getFrom();
                    }
                }
            }
        }

        // Backtrack to find the most profitable path
        List<String> path = new ArrayList<>();
        Set<Integer> visited = new HashSet<>(); // To keep track of visited nodes to prevent loops
        for (int at = destIdx; at != -1; at = prev[at]) {
            if(at == srcIdx) {
                path.add(indexToCurrency.get(at));
                break;
            }
            // Check if we've already visited this node; if so, print the direct path message
            if (visited.contains(at)) {
                double finalRate = 1.0 / exchangeRatesToUSD.get(source) * exchangeRatesToUSD.get(destination);
                System.out.printf("%nMost Profitable Path: Directly converting from %s to %s%n", source, destination);
                System.out.printf("%nFinal Exchange Rate: 1 %s = %.6f %s%n", source, finalRate, destination);
                return false;
            }
            visited.add(at);
            path.add(indexToCurrency.get(at));
        }

        if (path.size() == 1) {
            // Only the destination currency is found in the path, meaning there is no valid path
            double finalRate = 1.0 / exchangeRatesToUSD.get(source) * exchangeRatesToUSD.get(destination);
            System.out.printf("%nMost Profitable Path: Directly converting from %s to %s%n", source, destination);
            System.out.printf("%nFinal Exchange Rate: 1 %s = %.6f %s%n", source, finalRate, destination);
            return false;
        }

        Collections.reverse(path); // Reverse the path to display it from source to destination

        // Check if the backtracked path starts from the source
        if (!path.get(0).equals(source)) {
            // If the backtracked path does not start from the source, print the direct conversion message
            double finalRate = 1.0 / exchangeRatesToUSD.get(source) * exchangeRatesToUSD.get(destination);
            System.out.printf("%nMost Profitable Path: Directly converting from %s to %s%n", source, destination);
            System.out.printf("%nFinal Exchange Rate: 1 %s = %.6f %s%n", source, finalRate, destination);
            return false;
        }

        // Print the most profitable path
        double finalRate = Math.exp(-dist[destIdx]);
        System.out.println("\nMost Profitable Path:");
        for (int i = 0; i < path.size(); i++) {

            System.out.print(path.get(i));
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.printf("%nFinal Exchange Rate: 1 %s = %.6f %s%n", source, finalRate, destination);

        return false;
    }

    // Getters (if needed)
    public Map<String, Integer> getCurrencyToIndex() {
        return currencyToIndex;
    }

    public Map<Integer, String> getIndexToCurrency() {
        return indexToCurrency;
    }

    public Map<Integer, List<ForexRate>> getAdjList() {
        return adjList;
    }

    public int getCurrencyCount() {
        return exchangeRatesToUSD.size();
    }

}
