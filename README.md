The problem statement:
Set and track personalized financial goals (vacation, car, emergency fund, etc.), calculating required savings and sending reminders. Predict loan repayment schedules without machine learning by analyzing loan amounts, interest rates, and payment history.              Detect suspicious transactions and send alerts based on predefined financial behavior patterns

The Smart Finance Assistant helps users manage their finances by:
1. Setting and tracking personalized financial goals (e.g., vacations, cars, emergency funds) with savings tracking.
2. Predicting loan repayment schedules without using machine learning—based on loan amount, interest rate, and paid history.
3. Logging and monitoring bank transactions, with scope for detecting suspicious patterns.
4. Allowing users to link their bank account, view balance, and simulate real-world financial decision-making (e.g., saving, loan payments).
5. The assistant provides a menu-driven interface for seamless interaction and financial monitoring.

In the Smart Finance Assistant Java program, several data structures are utilized to manage financial data efficiently. The program uses object-oriented classes such as Goal, Loan, Transaction, and BankAccount to encapsulate related attributes and behaviors. To store collections of these objects, it employs ArrayList, a dynamic data structure from Java’s Collection Framework. Specifically, an ArrayList<Goal> keeps track of all user-defined savings goals, while an ArrayList<Loan> maintains the list of active loans and their payment statuses. Similarly, an ArrayList<Transaction> is used to store and categorize individual expense transactions. A single instance of the BankAccount class holds the user’s bank details, including account number, IFSC code, and balance, allowing for secure verification and transactions. Additionally, the Scanner class is used for capturing user input throughout the program. These data structures collectively enable the system to model real-world financial operations in a modular, organized, and scalable manner.

The link to short video:
https://drive.google.com/file/d/15by2Sp1RhzFrNy6zSsMgnChexfQRcjHO/view?usp=sharing
