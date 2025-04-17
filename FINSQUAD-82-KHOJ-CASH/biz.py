import streamlit as st
from datetime import datetime, timedelta
import heapq
from collections import defaultdict
import numpy as np 
import streamlit as st
import requests
import matplotlib.pyplot as plt
from db_connection import (
    insert_transaction,
    add_debt,
    mark_debt_paid,
    get_debts,
    add_autopay,
    run_due_autopay,
    add_budget,
    update_budget,
    view_budgets,
    get_transactions,        
    get_autopay,
    update_budget_used             
)




#Page SetUp
st.set_page_config(page_title="KHOJ-CASH /BIZ", layout="wide")


# CSS STYLING
st.markdown("""
    <style>
    /* Sidebar Styling */
    section[data-testid="stSidebar"] {
        background: linear-gradient(to bottom, #2c3e50, #4b6cb7);
        color: white;
        padding: 2rem 1rem;
        height: 100vh;
    }

    section[data-testid="stSidebar"] h1,
    section[data-testid="stSidebar"] h2,
    section[data-testid="stSidebar"] h3,
    section[data-testid="stSidebar"] label {
        color: white;
    }

    .main-title {
        background: linear-gradient(to right, #4b6cb7, #182848);
        color: white;
        padding: 0rem;
        border-radius: 10px;
        text-align: center;
        margin-bottom: 0.5rem;
        font-family: 'Times New Roman' ;
    }

    .intro-text {
        font-family: 'Times New Roman';
        margin-top: 1rem;
    }

    .biztrack-features {
            color: #ffffff;
            font-size: 20px;
            filter: brightness(1.2) contrast(1.5);  /* Example filter */
        }
    .card {
        background-color: white;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        margin-bottom: 20px;
    }

    .stButton > button {
        background-color: #4b6cb7;
        color: white;
        border-radius: 8px;
        padding: 0.5rem 1rem;
        border: none;
    }

    .stButton > button:hover {
        background-color: #35507c;
    }

    section[data-testid="stSidebar"] .stSelectbox {
        margin-top: 1rem;
    }

    section[data-testid="stSidebar"] .stSelectbox > div {
        background-color: #f5f8ff;
        border-radius: 8px;
    }

    section[data-testid="stSidebar"] .stSelectbox div[data-baseweb="select"] {
        border: none;
    }

    /* Optional: Hover effects for menu */
    section[data-testid="stSidebar"] .stSelectbox div[data-baseweb="select"]:hover {
        background-color: #e0e8ff;
    }

    body {
        background-color: #f7f9fc;
        font-family: 'Segoe UI', sans-serif;
    }
    .budget-title {
            color: #4b6cb7;
            font-size: 24px;
            font-weight: bold;
        }

    h1, h2, h3 {
        color: #2C3E50;
    }

    .card {
        background-color: white;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        margin-bottom: 15px;
    }

    .stTextInput > div > input,
    .stNumberInput > div > input,
    .stDateInput > div > input,
    .stTextArea > label + div textarea,
    .stSelectbox > div {
        border-radius: 8px;
        padding: 8px;
        background-color: #f0f0f5;
    }
    </style>
""", unsafe_allow_html=True)



# ---------------- TRANSACTIONS (Linked List) ----------------

class TransactionNode:
   def __init__(self, business_id, transaction_id, transaction_type, amount, date, description):
        self.business_id = business_id
        self.transaction_id = transaction_id
        self.transaction_type = transaction_type
        self.amount = amount
        self.date = date
        self.description = description
        self.next = None

class TransactionList:
    def __init__(self):
        self.head = None
        self.transactions=[]
    # def __len__(self):
    #     return len(self.transactions)
    def __len__(self):
      count = 0
      current = self.head
      while current:
        count += 1
        current = current.next
      return count


    def add_transaction(self, business_id, transaction_id, transaction_type, amount, date, description):
        new_transaction = TransactionNode(business_id, transaction_id, transaction_type, amount, date, description)
        if  not self.head:
            self.head = new_transaction
        else:
            current = self.head
            while current.next:
                current = current.next
            current.next = new_transaction


    def view_transactions(self):
        current = self.head
        data = []
        while current:
            data.append({
                "ID": current.transaction_id,
                "Type": current.transaction_type,
                "Amount": current.amount,
                "Date": current.date,
                "Description": current.description
            })
            current = current.next
        return data

    def monthly_summary(self):
        summary = defaultdict(lambda: {"Income": 0.0, "Expense": 0.0})
        current = self.head
        while current:
            date_obj = datetime.strptime(current.date, "%Y-%m-%d")
            month_key = date_obj.strftime("%Y-%m")
            if current.transaction_type.lower() == "income":
                summary[month_key]["Income"] += current.amount
            elif current.transaction_type.lower() == "expense":
                summary[month_key]["Expense"] += current.amount
            current = current.next
        return summary

# ---------------- BUDGET PLANNER ----------------

class BudgetPlanner:
    def __init__(self):
        self.budgets = []

    def add_budget(self, category, amount):
        self.budgets.append({'category': category, 'amount': amount, 'used': 0.0})

    def update_expense(self, category, expense):
        for item in self.budgets:
            if item['category'].lower() == category.lower():
                item['used'] += expense
                return True
        return False

    def view_budgets(self):
        return self.budgets

# ---------------- DEBT MANAGEMENT ----------------

class DebtNode:
    def __init__(self, name, amount, due_date):
        self.name = name
        self.amount = amount
        self.due_date = due_date
        self.status = "Unpaid"
        self.next = None

class DebtList:
    def __init__(self):
        self.head = None

    def add_debt(self, name, amount, due_date):
        new_debt = DebtNode(name, amount, due_date)
        if not self.head:
            self.head = new_debt
        else:
            current = self.head
            while current.next:
                current = current.next
            current.next = new_debt

    def view_debts(self):
        current = self.head
        data = []
        while current:
            data.append({
                "Name": current.name,
                "Amount": current.amount,
                "Due Date": current.due_date,
                "Status": current.status
            })
            current = current.next
        return data

    def mark_paid(self, name):
        current = self.head
        while current:
            if current.name.lower() == name.lower() and current.status == "Unpaid":
                current.status = "Paid"
                return True
            current = current.next
        return False

# ---------------- AUTOPAY SYSTEM ----------------

class AutoPay:
    def __init__(self, user, amount, purpose, frequency, next_payment):
        self.user = user
        self.amount = amount
        self.purpose = purpose
        self.frequency = frequency
        self.next_payment = next_payment

    def schedule_next(self):
        if self.frequency == 'daily':
            self.next_payment += timedelta(days=1)
        elif self.frequency == 'weekly':
            self.next_payment += timedelta(weeks=1)
        elif self.frequency == 'monthly':
            self.next_payment += timedelta(days=30)

    def process_payment(self):
        return f"💳{self.user} paid ₹{self.amount} for {self.purpose} on {self.next_payment.strftime('%Y-%m-%d')}"

    def __lt__(self, other):
        return self.next_payment < other.next_payment

# ---------------- STREAMLIT UI ----------------

# Initialize session state
if "transactions" not in st.session_state:
    st.session_state.transactions = TransactionList()
if "budgets" not in st.session_state:
    st.session_state.budgets = BudgetPlanner()
if "debts" not in st.session_state:
    st.session_state.debts = DebtList()
if "autopay" not in st.session_state:
    st.session_state.autopay = []

 # Sidebar
with st.sidebar:
    st.markdown('<div class="biztrack-features">BIZTRACK FEATURES</div>', unsafe_allow_html=True)
    st.markdown(
        """
        <div style='color:#ffffff; font-size: 14px; line-height: 1.5;'>
            Streamline your business finance with precision.<br>
            <ul style='padding-left: 20px'>
                <li>📊 Track income & expenses</li>
                <li>💰 Manage debts & budgets</li>
                <li>🔁 Set up autopay routines</li>
                <li>📈 View monthly summaries</li>
            </ul>
        </div>
        """,
        unsafe_allow_html=True
    )
    st.markdown("""
    <h2 style='
        font-family: Poppins, sans-serif;
        font-weight: 700;
        font-size: 26px;
        color: #4b0082;
        margin-bottom: 20px;
        padding-left: 10px;
    '></h2>
""", unsafe_allow_html=True)

#SIDEBAR MENU
menu = st.sidebar.selectbox("MENU", ["Dashboard", "Transactions", "Budgets", "Debts", "Autopay", "Summary"])

# DASHBOARD
if menu == "Dashboard":
    # INTRO
    st.markdown('<div class="main-title"><h1>BizTrack</h1></div>', unsafe_allow_html=True)
    
    st.markdown("""
    <div class="intro-text">
        <p style="font-size: 1.2rem; color: #34495e; text-align: center; font-family: 'Times New Roman;">
        BizTrack is your comprehensive personal finance management tool, designed to empower you to take control of your business finance journey. With BizTrack, you can seamlessly manage your finances by tracking every aspect of your spending, budgeting, debt management, and autopay schedules in one central location.
        Our intuitive platform allows you to easily record and categorize your transactions, giving you a clear overview of your financial behavior. Set personalized budget limits for each category and monitor your progress to ensure you're staying on track with your financial goals.
        Take charge of your financial well-being with features that enable you to efficiently manage your debts, whether you're borrowing or lending, and schedule autopayments to ensure timely bill payments without the hassle.
        </p>
    </div>
    """, unsafe_allow_html=True)

    #QUICK STATS
    total_budget = sum(b['amount'] for b in st.session_state.budgets.view_budgets())
    total_debts = sum(d['Amount'] for d in st.session_state.debts.view_debts() if d['Status'] == "Unpaid")

    st.markdown(f"""
<div class="card" style="background :linear-gradient(to right, #4b6cb7, #182848); padding: 20px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.5); margin-top: 20px;">
    <h3 style="color: #ffffff; text-align:center; text-decoration : underline">QUICK STATS</h3>
    <ul style="font-size: 1.2rem; color: #34495e; list-style-type:none; padding-left: 0; text-align:left;">
        <li style="color: #ffffff;">Total Transactions: {len(st.session_state.transactions)}</li>
        <li style="color: #ffffff;">Total Budget Set: ₹{total_budget:.2f}</li>
        <li style="color: #ffffff;">Total Debts: ₹{total_debts:.2f}</li>
        <li style="color: #ffffff;">Upcoming Autopay Payments: {len(st.session_state.autopay)}</li>
    </ul>
</div>
""", unsafe_allow_html=True)



#TRANSACTIONS
elif menu == "Transactions":
    st.markdown("## 🧾 <span style='color:#4b6cb7'>ADD NEW TRANSACTION</span>", unsafe_allow_html=True)
    with st.form("transaction_form"):
        bid = st.text_input("Business ID") 
        tid = st.text_input("Transaction ID")
        ttype = st.selectbox("Type", ["Income", "Expense"])
        amount = st.number_input("Amount", min_value=0.0)
        date = st.date_input("Date")
        desc = st.text_input("Description")
        submitted = st.form_submit_button("Add Transaction")
        if submitted:
            st.session_state.transactions.add_transaction(
                bid, tid, ttype, amount, date.strftime("%Y-%m-%d"), desc
            )
            insert_transaction(bid, tid, ttype, amount, date.strftime("%Y-%m-%d"), desc)
            st.success("Transaction Added!")

    st.subheader("📄 All Transactions")
    # data = st.session_state.transactions.view_transactions()
    data = get_transactions()
    if data:
        st.table(data)
    else:
        st.info("No transactions found.")


# BUDGETS 
elif menu == "Budgets":
    st.markdown("## 💵<span style='color:#4b6cb7'>BUDGET PLANNER </span>", unsafe_allow_html=True)
    with st.form("budget_form"):
        cat = st.text_input("Category")
        amt = st.number_input("Budget Amount", min_value=0.0)
        submitted = st.form_submit_button("Add Budget")
        if submitted:
            st.session_state.budgets.add_budget(cat, amt)
            add_budget(cat, amt) 
            st.success("Budget Added!")

    with st.form("update_budget"):
        cat = st.text_input("Update Category")
        exp = st.number_input("Expense Amount", min_value=0.0)
        update_btn = st.form_submit_button("Update Expense")
        if update_btn:
            updated = st.session_state.budgets.update_expense(cat, exp)
            update_budget_used(cat, exp)
            if updated:
                st.success("Expense updated.")
            else:
                st.error("Category not found.")

    st.markdown('<div class="budget-title">📃 BUDGET OVERVIEW</div>', unsafe_allow_html=True)
    # st.table(st.session_state.budgets.view_budgets())
    data = view_budgets()
    if not data.empty:
      st.table(data)
    else:
      st.info("No budgets found.")

# DEBTS
elif menu == "Debts":
    st.markdown("## 💰<span style='color:#4b6cb7'>DEBT MANAGEMENT</span>", unsafe_allow_html=True)
    with st.form("debt_form"):
        name = st.text_input("Debt Name")
        amt = st.number_input("Amount", min_value=0.0)
        due = st.date_input("Due Date")
        submitted = st.form_submit_button("Add Debt")
        if submitted:
            st.session_state.debts.add_debt(name, amt, due.strftime("%Y-%m-%d"))
            add_debt(name, amt, due.strftime("%Y-%m-%d"))
            st.success("Debt Added!")

    with st.form("mark_debt"):
        dname = st.text_input("Debt Name to Mark Paid")
        pay_btn = st.form_submit_button("Mark as Paid")
        if pay_btn:
            paid = mark_debt_paid(dname)
            # paid = st.session_state.debts.mark_paid(dname)
            if paid:
                st.success("Marked as Paid.")
            else:
                st.error("Debt not found or already paid.")
            mark_debt_paid(dname)
            st.success(f"Debt '{dname}' marked as Paid in the database.")

    st.markdown('<div class="budget-title">📌CURRENT DEBTS</div>', unsafe_allow_html=True)
    # st.table(st.session_state.debts.view_debts())
    debts = get_debts()
    if debts:
        st.table(debts)
    else:
        st.info("No debts found.")

#AUTOPAY
elif menu == "Autopay":
    st.markdown("## 🔀<span style='color:#4b6cb7'>AUTOPAY SCHEDULER</span>", unsafe_allow_html=True)
    with st.form("autopay_form"):
        user = st.text_input("Name")
        amt = st.number_input("Amount", min_value=0.0)
        purpose = st.text_input("Purpose")
        freq = st.selectbox("Frequency", ["daily", "weekly", "monthly"])
        delay = st.number_input("Start in (seconds)", min_value=0)
        submit_ap = st.form_submit_button("Schedule Autopay")
        if submit_ap:
            start_time = datetime.now() + timedelta(seconds=delay)
            heapq.heappush(st.session_state.autopay, AutoPay(user, amt, purpose, freq, start_time))
            add_autopay(user, amt, purpose, freq, start_time)
            st.success("Autopay Scheduled!")
    autopay_data = get_autopay()
    if autopay_data:
        st.table(autopay_data)
    else:
        st.info("No autopay details found.")

    st.markdown('<div class="budget-title">⌛RUN AUTOPAY</div>', unsafe_allow_html=True)
    run = st.button("Run Due Payments Now")
    if run:
        now = datetime.now()
        processed = []
        while st.session_state.autopay and st.session_state.autopay[0].next_payment <= now:
            task = heapq.heappop(st.session_state.autopay)
            processed.append(task.process_payment())
            task.schedule_next()
            heapq.heappush(st.session_state.autopay, task)
        if processed:
            for msg in processed:
                st.success(msg)
        else:
            st.info("No payments due right now.")

# -------- SUMMARY --------
elif menu == "Summary":
    st.markdown("## 📊<span style='color:#4b6cb7'>MONTHLY ANALYSIS BY BUSINESS ID'S</span>", unsafe_allow_html=True)
    #Summary by business ID
    summary_by_id = defaultdict(lambda: defaultdict(lambda: {"Income": 0.0, "Expense": 0.0}))
    current = st.session_state.transactions.head
    while current:
        date_obj = datetime.strptime(current.date, "%Y-%m-%d")
        month_key = date_obj.strftime("%Y-%m")
        business_id = getattr(current, "business_id", "Default")
        if current.transaction_type.lower() == "income":
            summary_by_id[business_id][month_key]["Income"] += current.amount
        elif current.transaction_type.lower() == "expense":
            summary_by_id[business_id][month_key]["Expense"] += current.amount
        current = current.next

    #  Bar Plot 
    for biz_id, summary in summary_by_id.items():
        st.subheader(f"📁 Business ID: {biz_id}")
        months = sorted(summary.keys())
        income = [summary[month]["Income"] for month in months]
        expense = [summary[month]["Expense"] for month in months]
        profit_loss = [summary[month]["Income"] - summary[month]["Expense"] for month in months]

        fig, ax = plt.subplots(figsize=(10, 5))
        bar_width = 0.25
        index = np.arange(len(months))

        ax.bar(index - bar_width, income, bar_width, label="Income", color='green')
        ax.bar(index, expense, bar_width, label="Expense", color='red')
        ax.bar(index + bar_width, profit_loss, bar_width, label="Profit/Loss", color='blue')

        ax.set_xlabel('Month')
        ax.set_ylabel('Amount (₹)')
        ax.set_title(f'Business {biz_id}: Income, Expense & Profit/Loss by Month')
        ax.set_xticks(index)
        ax.set_xticklabels(months)
        ax.legend()

        st.pyplot(fig)

        for month in months:
            inc = summary[month]["Income"]
            exp = summary[month]["Expense"]
            prof = inc - exp
            st.write(f"**{month}** → Income: ₹{inc:.2f}, Expense: ₹{exp:.2f}, Profit/Loss: ₹{prof:.2f}")
