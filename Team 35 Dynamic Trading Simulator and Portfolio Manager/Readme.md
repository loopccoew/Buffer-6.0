
# Stock Simulator

**Link to Video:**  
https://drive.google.com/file/d/1CEa_L-JlaUI1p7VpXDyshOKYKqqpns8P/view?usp=sharing

**Link to Presentation:**  
[View Presentation on Canva](https://www.canva.com/design/DAGk52bG0c0/0BBVTRp2zXM9APE2e2vOQw/view?utm_content=DAGk52bG0c0&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=he709b225b0)

---

##  Project Description Summary

The **Stock Simulator** is an interactive Java-based application designed to replicate a virtual stock market environment. It enables users to create, view, filter, and manage a variety of stocks, helping them understand the dynamics of market behavior in a simulated setting.

---

##  Need and Purpose

The Stock Simulator is a comprehensive Java-based application designed to provide a realistic and interactive platform for understanding stock market dynamics. It empowers users to:

- Experiment with various investment strategies  
- Analyze market trends  
- Make informed investment decisions  
- Learn without risking real-world capital

---

##  Key Features and Implementations

###  Data Structures Used

- **Stack**: Stores buy and sell transactions for tracking historical data and analyzing investment performance.  
- **Priority Queue**: Organizes stocks based on market capitalization for efficient retrieval of top-performing stocks.  
- **Linked List**: Manages a user’s wishlist with dynamic addition/removal of stocks.  
- **HashMap**: Maintains a user’s stock portfolio with stock symbols as keys and their quantities as values.  
- **Binary Tree**: Efficiently stores and searches market stocks, enables filtering by cap size (large, mid, small) and sector (IT, FMCG, Textiles, Oil, etc.).  
- **Max Heap**: Retrieves top-performing stocks, giving a concise view of the market.  
- **Graph**: Represents inter-stock influence within sectors using an adjacency matrix. Influence strength is derived from a correlation matrix of 20-day price data, fetched using Python’s `yfinance` library.  
- **Set**: Tracks unique sectors in the user’s portfolio to evaluate diversification and suggest underrepresented sectors.

---

## 🔗 Real-Time Data Integration

- Fetches **real-time stock prices** using Java’s `Connection URL` package, ensuring the application reflects up-to-date market behavior.

---

## File Handling

- Uses **file I/O** to store and retrieve stock data from `stocks.dat`, ensuring persistent storage and easy recovery.

---
## Portfolio Analysis Dashboard
Provides an interactive analysis dashboard with candlestick pattern visualization, sector-wise portfolio distribution, and simulated profit/loss summaries. Built using Java Swing and JFreeChart, this module helps users evaluate performance trends and diversify their investment strategy effectively.

##  Benefits

The Stock Simulator is an ideal platform for both beginners and experienced users to:

- Gain **hands-on experience** with virtual investing  
- Test different strategies and risk levels  
- Understand market volatility and diversification  
- Learn key **financial and data structure concepts** interactively  

---


