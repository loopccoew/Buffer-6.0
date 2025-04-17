# 🏷 BidSure Auction Platform

BidSure is a feature-rich auction platform that allows users to participate in dynamic and reverse auctions, with integrated payment processing, fraud detection, and currency conversion.

---

## 📋 Table of Contents

- [Features](#features)
- [Data Structures Used](#data-structures-used)
- [Usage](#usage)
-[Working demo]


---

## ✨ Features

- 🎯 Dynamic and reverse auction modes
- ⏱ Anti-sniping & sudden death auction strategies
- 💳 Integrated payment processing
- 🛡 Fraud detection via bidding behavior
- 🌍 Currency conversion with real-time rates

---

## 🧰 Tech Stack

- *Language:* Java ( javascipt and html for UI)
- *Data Structures:* HashMap, ArrayList, PriorityQueue, LinkedList (Queue)


---

## 📦 Data Structures Used

### 🔹 HashMap (User Data)
- *Purpose:* Stores user data (username, balance, currency) and tracks bid counts for fraud detection.
- *Why:* Fast lookups (O(1)) and ensures unique keys.

### 🔹 ArrayList
- *Purpose:* Stores lists of users and auctions.
- *Why:* Dynamic resizing and efficient iteration.

### 🔹 PriorityQueue
- *Purpose:* Manages and auto-sorts bids by amount.
- *Why:* Efficient highest-bid retrieval in O(log n) time.

### 🔹 Queue (LinkedList)
- *Purpose:* Handles payment transactions in FIFO order.
- *Why:* Ensures first-come-first-serve with O(1) operations.

### 🔹 HashMap (Currency Rates)
- *Purpose:* Stores real-time currency exchange rates.
- *Why:* Fast and direct rate access in O(1) time.

## Wroking Demo Links:

https://drive.google.com/file/d/1wYrAk7j1-YRm-91FI2sn7QBPmc4KtjCB/view?usp=sharing 

https://drive.google.com/file/d/1MAxQejUfg7xIMDXLBFcCZsOkLLgfJBUi/view?usp=sharing 
---