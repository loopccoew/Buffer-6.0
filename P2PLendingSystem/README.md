A Java-based P2P lending platform that connects borrowers with lenders directly using efficient data structures and intelligent matching algorithms. Designed with modular components and integrated with a MySQL backend.

Features
1. Borrower & Lender Management Register, login, and manage profiles for borrowers and lenders.
2. Loan Request & Offer Handling Borrowers can request loans; lenders can offer based on preferred rates.
3. Automated Loan Matching Matches borrowers and lenders using optimized criteria like interest rate and amount.
4. Blacklist & Fraud Check Skips blacklisted borrowers during matching.
5. Real-Time Loan Status Updates Updates loan status (pending, matched, completed) in the database.
6. Data Structures
    BST: Borrower records
    Heap: Lender priority queue (by rate)
    Trie: Lender name search
    Segment Tree: Credit score range queries
    Graph: Relationship mapping for matching logic

Tech Stack 
Language: Java
Database: MySQL
Database Access: JDBC
IDE: Eclipse 
GUI (Graphical User Interface) toolkit : Java Swing
