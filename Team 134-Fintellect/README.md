### **README: Fintellect - Smart Financial Planner**

---

## **Project Title**
**Fintellect: Smart Financial Planner**

---

## **Problem Statement**
Despite increasing financial independence among young adults, there exists a significant gap between income generation and informed financial decision-making. Existing tools are often too complex or generic, lacking educational value.  

This project proposes an advanced, algorithm-driven financial simulator leveraging data structures and real-world data to empower users with actionable insights in investment strategies, savings optimization, tax efficiency, and goal prioritization—bridging the critical gap between financial literacy and strategic execution.

---

## **Features**
1. **Portfolio Optimization**:
   - Helps users maximize returns within a given budget.
   - Uses the **Fractional Knapsack Algorithm** to select full or partial investments based on return-to-cost ratio.

2. **Goal Dependency Tracker**:
   - Organizes and prioritizes financial goals.
   - Uses a **Graph-Based Approach** to map dependencies and resolve them using **Topological Sorting**.

3. **Risk Profile Simulator**:
   - Determines the user's risk tolerance based on financial attributes like age, savings, and loans.
   - Categorizes users as "Conservative," "Balanced," or "Aggressive."

4. **Personalized Fund Recommendations**:
   - Suggests funds based on user preferences for return, risk, and tenure.
   - Uses a **Weighted Scoring Algorithm** to rank funds.

---

## **How It Works**
### **Step 1: Input Collection**
- Users provide:
  - Total budget.
  - Maximum acceptable risk level.
  - Details of available investment options (name, cost, expected return, and risk).

### **Step 2: Sorting Investments**
- Investments are sorted by their **return-to-cost ratio** in descending order using a custom comparator.
- This ensures that the most profitable investments are considered first.

### **Step 3: Fractional Knapsack Algorithm**
- The algorithm iterates through the sorted investments:
  - Selects investments fully if the budget allows.
  - Selects a fraction of an investment if the remaining budget is insufficient for full selection.
- Outputs the selected investments and the maximized return.

### **Step 4: Output**
- Displays:
  - List of selected investments (full or partial).
  - Total maximized return.

---

## **Data Structures and Algorithms**
### **Data Structures**
1. **Custom Class (`Investment`)**:
   - Encapsulates investment details (`name`, `cost`, `expectedReturn`, `risk`).
   - Provides a clean and modular way to manage investment data.

2. **Array (`Investment[]`)**:
   - Stores all investment options for efficient sorting and iteration.

3. **HashMap**:
   - Used in other modules (e.g., `PortfolioAllocator`) to store and retrieve funds for quick lookups.

4. **Graph**:
   - Used in the **Goal Dependency Tracker** to represent dependencies between financial goals.

5. **Priority Queue**:
   - Used in the **Goal Dependency Tracker** to prioritize goals based on user-defined priorities.

### **Algorithms**
1. **Fractional Knapsack Algorithm**:
   - Maximizes returns within a budget by selecting full or partial investments.
   - Greedy approach ensures optimal use of the budget.

2. **Sorting**:
   - Investments are sorted by their **return-to-cost ratio** in descending order.
   - Ensures that the most profitable investments are processed first.

3. **Topological Sorting**:
   - Resolves dependencies between financial goals in the **Goal Dependency Tracker**.

4. **Weighted Scoring**:
   - Ranks funds based on user-defined preferences for return, risk, and tenure.

---

## **Code Walkthrough**
### **Key Files**
1. **`InvestmentPlanner.java`**:
   - Implements portfolio optimization using the fractional knapsack algorithm.
   - Handles user input, sorting, and investment selection.

2. **`PortfolioAllocator.java`**:
   - Implements fund recommendations using weighted scoring.
   - Suggests funds based on user preferences for return, risk, and tenure.

3. **`InvestmentGraph.java`**:
   - Manages goal dependencies using a graph-based approach.
   - Resolves dependencies using topological sorting.

4. **`RiskProfileSimulator.java`**:
   - Simulates user risk profiling based on financial attributes.
   - Categorizes users into risk profiles like "Conservative," "Balanced," or "Aggressive."

5. **`Main.java`**:
   - Provides a menu-driven interface to access all features.

---

## **Sample Input and Output**
### **Portfolio Optimization**
**Input**:
```
Enter your total budget (₹): 50000
Enter your maximum acceptable risk level (e.g., 1 to 10): 5
How many investment options do you want to enter? 3

Enter details for Investment 1
Name: Tech ETF
Cost (₹): 20000
Expected Return (₹): 30000
Risk (1-10): 4

Enter details for Investment 2
Name: Government Bonds
Cost (₹): 15000
Expected Return (₹): 18000
Risk (1-10): 2

Enter details for Investment 3
Name: SIP Balanced
Cost (₹): 25000
Expected Return (₹): 35000
Risk (1-10): 5
```

**Output**:
```
Selected: SIP Balanced (Full)
Selected: Government Bonds (Full)
Maximized Return: ₹53000
```

---

## **Technologies Used**
- **Programming Language**: Java
- **Development Environment**: Visual Studio Code
- **Data Structures**: Arrays, HashMaps, Graphs, Custom Classes
- **Algorithms**: Fractional Knapsack, Weighted Scoring, Topological Sorting

---

## **Strengths of the Project**
1. **Efficiency**:
   - The use of the **Fractional Knapsack Algorithm** ensures optimal budget utilization.
   - Sorting investments by return-to-cost ratio improves decision-making.

2. **User-Friendliness**:
   - Simple and interactive interface for entering investment details and viewing results.

3. **Scalability**:
   - Can handle a large number of investments efficiently due to \(O(n \log n)\) sorting complexity.

4. **Real-World Applicability**:
   - Provides actionable insights for users to make smarter financial decisions.

---

## **Future Enhancements**
1. **Integration with Real-Time Market Data**:
   - Fetch live data for investments to provide more accurate recommendations.

2. **Risk-Based Filtering**:
   - Automatically filter out investments that exceed the user's risk tolerance.

3. **Mobile Application**:
   - Develop a mobile-friendly version for wider accessibility.

4. **Tax Optimization**:
   - Suggest tax-saving investments based on user profiles.

---

## **How to Run the Project**
1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE (e.g., Visual Studio Code, IntelliJ IDEA).
3. Compile and run the Main.java file.
4. Follow the on-screen instructions to input your budget, risk tolerance, and investment details.

---

## **Team Members**
- **Aadya Singh**
- **Apoorva Vaidya**

