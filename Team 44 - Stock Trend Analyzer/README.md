Problem Statement:
There is a lack of accessible and beginner-friendly tools that allow users to interactively manage and analyze stock market data using structured methods. Existing financial platforms are often complex, require domain expertise, and do not provide educational value for understanding the underlying data processing. There is a need for a simplified system that enables both administrative data control and user-driven stock trend analysis through a structured, interactive interface.
To address this, we developed the Stock Market Trend Analyser—a user-friendly, educational tool that combines core data structure principles with practical stock analysis. The system supports admin-controlled CRUD operations on stock entries and offers features like price trend detection, moving averages, technical summaries, and future price predictions.


Data Structures Used: 
1. HashMap<String, List<StockEntry>>:
   Used as the core data structure to map stock symbols (e.g., "AAPL", "GOOG") to their historical data entries for fast and direct access.

2. ArrayList<StockEntry>:
   Stores daily stock data (date, open, high, low, close, volume) in a flexible, indexed collection.

3. PriorityQueue<StockEntry>:
   Utilized for efficiently retrieving top-K highest or lowest stock prices by closing value within a specified time frame.

4. Comparator Interface:
   Helps in sorting stock entries based on custom criteria like date or price for trend analysis and averaging.


Video Link:
https://drive.google.com/drive/folders/1sHbAcMM7VmFwAhuseICz62esyAD0V8cE?usp=sharing
