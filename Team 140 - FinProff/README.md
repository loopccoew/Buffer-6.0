# DSA Bank Management System - GUI Version

## Overview

This project implements a simple banking application featuring a graphical user interface (GUI) built with Java Swing. It allows users to create accounts, log in, manage funds (deposit, withdraw, transfer), view their transaction history, and trace paths of transactions between accounts. The system also includes an administrative backend providing features like viewing the overall transaction graph, searching for accounts efficiently, and generating reports.

The core focus, beyond basic banking functionality, is the application and demonstration of fundamental data structures and algorithms (like HashMaps, Graphs, BFS, Merge Sort, Binary Search) to handle banking operations and reporting.

## Features

**User Features:**

*   **Account Creation:** Securely create a new bank account with a username and password.
*   **User Login:** Log in using the assigned account number and password.
*   **Check Balance:** View the current account balance.
*   **Deposit:** Add funds to the account.
*   **Withdraw:** Remove funds from the account (with password validation).
*   **Transfer Money:** Transfer funds to another existing account (with password validation).
*   **View Transactions:** See a chronological log of personal account activity (deposits, withdrawals, transfers).
*   **Trace Transaction Path:** Find a path of transfers from the user's account to another specified account using BFS.
*   **Logout:** Securely end the user session.

**Admin Features:**

*   **Admin Login:** Access the administrative panel with dedicated credentials.
*   **View Transaction Graph:** Visualize the flow of money between accounts as a directed graph.
*   **Trace Path Between Accounts:** Trace the transaction path between any two accounts in the system.
*   **Search Account:** Efficiently find an account by its number using Binary Search.
*   **View Reports:** Generate a list of all accounts sorted by their current balance using Merge Sort.
*   **Logout:** Securely end the admin session.

## Data Structures & Algorithms Used

This project leverages several key data structures and algorithms:

*   **`HashMap <String, Account>`:**
    *   **Purpose:** Stores all user accounts, mapping unique `accountNumber` (String) keys to their corresponding `Account` objects.
    *   **Benefit:** Provides average O(1) time complexity for crucial operations like retrieving an account during login, deposit, withdrawal, and transfer lookups.
*   **`HashMap <String, List<TransactionEdge>>`:**
    *   **Purpose:** Represents the transaction graph as an adjacency list. The key is the source `accountNumber` (String), and the value is a `List` of `TransactionEdge` objects, each representing a transfer *from* the source account *to* a target account with a specific amount.
    *   **Benefit:** Efficiently stores and retrieves outgoing transactions for a given account, suitable for graph traversal algorithms like BFS.
*   **`List <String>` (specifically `LinkedList` or `ArrayList`):**
    *   **Purpose:** Used within the `Account` class to store the `transactionLog`. Also used dynamically within algorithms like BFS (for the queue and path reconstruction) and Merge Sort (for temporary storage).
    *   **Benefit:** `LinkedList` offers efficient additions/removals for the transaction log and BFS queue. `ArrayList` provides good performance for random access needed during Merge Sort merging steps.
*   **`Queue <String>` (implemented via `LinkedList`):**
    *   **Purpose:** Used in the Breadth-First Search (BFS) algorithm (`traceTransactionPath`) to manage the nodes (account numbers) to visit in a FIFO (First-In, First-Out) manner.
    *   **Benefit:** Essential for the level-by-level exploration required by BFS to find the shortest path in terms of transaction hops.
*   **`Set <String>` (implemented via `HashSet`):**
    *   **Purpose:** Used in the BFS algorithm (`traceTransactionPath`) to keep track of visited account numbers.
    *   **Benefit:** Provides average O(1) time complexity for checking if an account has already been visited, preventing infinite loops in cyclic transaction graphs.
*   **Breadth-First Search (BFS):**
    *   **Purpose:** Implemented in `traceTransactionPath` to find if a path of transactions exists between a start and end account.
    *   **Benefit:** Guarantees finding the shortest path in terms of the number of transfers (hops) in an unweighted graph representation.
*   **Merge Sort:**
    *   **Purpose:** Used in the admin's "View Reports" feature (`sortAccountsByBalance`) to sort all bank accounts based on their balance.
    *   **Benefit:** Provides an efficient and stable O(n log n) sorting algorithm suitable for generating ordered reports.
*   **Binary Search:**
    *   **Purpose:** Implemented in the admin's "Search Account" feature (`searchAccountBinary`). Requires the account list to be pre-sorted by account number.
    *   **Benefit:** Allows for very efficient O(log n) searching of accounts by number once the data is sorted.

## Demo Video

*https://drive.google.com/file/d/1exBkJqYAWLkcHlgxjD2cdJUraeAeVJFr/view?usp=sharing*



## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites

*   **Java Development Kit (JDK):** Version 8 or later installed. ([Download Link](https://www.oracle.com/java/technologies/javase-downloads.html) or use OpenJDK distributions)
*   **Git:** Required for cloning the repository. ([Download Link](https://git-scm.com/downloads))

### Installation & Setup

1.  **Clone the repository:**
    Open your terminal or command prompt and run:
    ```bash
    git clone [https://github.com/parnavi04/Buffer-6.0.git]
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd [Buffer-6.0] 
    ```

## Running the Application

1.  **Compile the Java Code:**
    Make sure you are in the project's root directory (where the `.java` files are located) in your terminal. Compile all Java source files:
    ```bash
    javac *.java
    ```
    *   This command creates the corresponding `.class` files. If you encounter any compilation errors, resolve them in the source code before proceeding.

2.  **Run the GUI Application:**
    Execute the main class that contains the GUI and the `main` method:
    ```bash
    java BankSystemGUI
    ```
    *   Do **not** add `.java` or `.class` to the command.
    *   This will launch the DSA Bank application window. You can now interact with the system through the graphical interface.

---
