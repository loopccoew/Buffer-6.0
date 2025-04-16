Team name : Runtime Terror
Member names :
  Vaishnavi Sable
  Yogashree Gedam
  Sayli Watpal
  Shubhashree Shinde

Problem statement description :
  In a global currency exchange system, converting money between currencies through intermediate steps can sometimes yield a better return than direct conversion. The   challenge is to determine the most profitable path to convert a source currency into a destination currency using available exchange rates. If no such path exists     or leads to inconsistent results, the system should suggest direct conversion as the fallback.

Data Structures
1. Adjacency List (Map<Integer, List<ForexRate>> adjList)
Represents the directed graph of currency conversions.
Each key is a currency index (int), and the value is a list of ForexRate objects representing edges to other currencies.

2. ForexRate Class
A custom class that defines a directed edge in the currency graph.
Fields:
from → Source currency index
to → Destination currency index
weight → Edge weight (-log(exchangeRate)) used to convert multiplication of rates into addition for shortest path calculation.

3. Currency Index Mapping
a. Map<String, Integer> currencyToIndex
Maps currency code (e.g., "USD", "JPY") to a unique integer index.
Used for graph indexing and lookups.

b. Map<Integer, String> indexToCurrency
Reverse of the above: maps integer index back to currency code.
Helps in displaying results in a user-friendly format.

4. Exchange Rate Table (HashMap<String, Double> exchangeRatesToUSD)
Stores each currency's exchange rate with respect to USD.
Used to compute pairwise exchange rates when building the graph.

5. Bellman-Ford Arrays
a. double[] dist
dist[i] holds the shortest distance (minimum cost) from the source currency to currency i in terms of negative log exchange rate.
Initialized to POSITIVE_INFINITY, except dist[source] = 0.
b. int[] prev
Stores the previous node (prev[i]) for each currency in the shortest path.
Used to reconstruct the most profitable path via backtracking.

Algorithm :
Bellman-Ford Algorithm
Solves the single-source shortest path problem in a graph with negative weights.
Suitable for detecting arbitrage and computing best conversion paths.

Video link : https://drive.google.com/file/d/1O3rj_pQXw34v0RfMNxQCW3SN0lUdFRPc/view?usp=sharing
