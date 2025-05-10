
 UPI Transaction Analyzer 

This Java application reads a UPI transaction CSV file and categorizes each transaction based on predefined keywords. It calculates category-wise spending, compares it to preset budget limits, and provides insightful summaries such as top overspent categories and weekly spending patterns.


 Features

-  Categorizes UPI transactions (Food, Shopping, Travel, etc.)
-  Tracks and compares spending against budget limits
-  Highlights top overspent categories
-  Performs weekly spending analysis
-  Clean console output with date, details, amount, and category



 Project Structure
TransactionAnalyzer/ │ ├── ex.java # Main Java class to read and analyze UPI transactions ├── UPI_pay2.csv # Sample CSV file with UPI transaction data └── README.md # Project documentation
---

 How It Works

1. *Input:* Reads from a .csv file with UPI transactions.
2. *Categorization:* Matches transaction tags/remarks with keywords to determine category.
3. *Spending Summary:*
   - Outputs a table of transactions with category labels.
   - Computes total spent per category.
   - Checks against budget and shows overspent areas.
4. *Weekly Analysis:*
   - Groups transactions by week.
   - Identifies the week with the highest spending.
   - Breaks down spending by category for that week.



 CSV Format

Ensure your CSV file (UPI_pay2.csv) follows this format:
Date,Time,Details,Amount,UPI,Remarks,Tags 01/04/2024,12:30 PM,Coffee Shop,-50,abc@upi,evening coffee,coffee cafe ...



 Sample Output
Date                                       Details                                 Amount       Category
  15/04/2025                  Paid to Suswaad Canteen Mkss        -18            food
  15/04/2025        Paid to Myntra Designs Private Limited      -117        shopping
  14/04/2025                   Paid to Shruti Prakash Gole              -35            food
  14/04/2025         Money sent to Sambhaji Devidas Shinde   -197       transfers
  13/04/2025                Paid to Prashant Shantram Rane          -80       groceries
  13/04/2025                             Paid to Irctc Uts                       -100          travel

  ---

 How to Run

1. Open the project in your favorite IDE or terminal.
2. Ensure Java is installed:  
   ```bash
   java -version
   javac ex.java
   java ex
