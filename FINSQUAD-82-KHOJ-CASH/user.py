from db_operations import insert_budget, fetch_budgets_by_user
from db_operations import insert_transaction, fetch_transactions_by_user
from db_operations import insert_saving_goal, update_savings, fetch_saving_goals_by_user
from db_operations import insert_debt, fetch_debts_by_user, mark_debt_as_paid
from db_operations import insert_autopay, fetch_autopays_by_user, fetch_due_autopay, update_next_autopay,delete_autopay_by_id
from db_operations import create_saving_pool, fetch_all_pools, add_contribution, fetch_pool_contributions

import streamlit as st
from datetime import datetime, timedelta
import plotly.express as px
import pandas as pd 
import heapq

# Page SetUp
st.set_page_config(page_title="KHOJ-CASH /USER", layout="wide")

# Stying
st.markdown("""
    <style>
        /* Add custom styles for cards, buttons, and layout */
        .card {
            border-radius: 10px;
            background-color: #f7f7f7;
            padding: 0.2rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .top-header {
            background: linear-gradient(to right,#4b6cb7, #182848);
            color: white;
            padding: 12px;
            text-align: center;
            font-size: 2.5rem;
            font-weight: 700;
            border-bottom: 4px solid #2563eb;
        }
        .stButton button {
            background-color: #4b6cb7;
            color: white;
            border-radius: 8px;
            padding: 0.4rem 1.2rem;
        }
        .stButton button:hover {
            background-color: #1e40af;
        }
        .stTextInput input {
            border-radius: 8px;
            padding: 0.6rem;
        }
        
    </style>
""", unsafe_allow_html=True)

# === Page Header ===
st.markdown('<div class="top-header">FINORA</div>', unsafe_allow_html=True)

# Sidebar content
with st.sidebar:
    st.markdown("""
        <style>
            /* Target the sidebar container more reliably */
            section[data-testid="stSidebar"] {
                background: linear-gradient(to right,#4b6cb7, #182848);
                color: white;
                padding: 20px;
            }

            /* Sidebar intro text */
            .sidebar-intro {
                color: white;
                font-size: 16px;
            }

            /* Style the selectbox inside the sidebar */
            div[data-baseweb="select"] {
                background-color: #ffffff;
                color: black;
                border-radius: 8px;
                padding: 5px;
            }
        </style>
        <div class="sidebar-intro">
            Your all-in-one space to: <br>
            📈 Build better financial habits <br>
            🎯 Stay on track with savings <br>
            🧠 Simplify decision-making <br><br>
            <strong>Because Financial Clarity leads to Freedom.<br></br><br></br></strong>
        </div>
    """, unsafe_allow_html=True)

    menu = st.selectbox("", ["Dashboard", "Transactions", "Savings", "Debt", "Budget", "Autopay", "Social Saving Pools"])


# === State ===
if 'transactions' not in st.session_state:
    st.session_state.transactions = []
if 'saving_goals' not in st.session_state:
    st.session_state.saving_goals = []
if 'debts' not in st.session_state:
    st.session_state.debts = []
if 'budgets' not in st.session_state:
    st.session_state.budgets = {}
if 'reminders' not in st.session_state:
    st.session_state.reminders = []
if 'autopay_heap' not in st.session_state:
    st.session_state.autopay_heap = []
if 'saving_pools' not in st.session_state:
    st.session_state.saving_pools = {}

if menu == "Dashboard":
    st.markdown("""
        <div style='font-size:1.2rem;text-align:center; padding-bottom:0 20px 10px 20px; font-colour:#34495e;font-family: 'Times New Roman;'>
            <br></br>Finora is your personal command center for managing money — track income, expenses, savings and much more in one smart dashboard.
    Set budgets, view insights, and build better financial habits. Earn badges as you hit savings goals — because progress should feel rewarding..<br></br>
        </div>
    """, unsafe_allow_html=True)

    st.markdown('<span style="color:#4b6cb7; font-size:35px; font-family:Poppins">📊 QUICK STATS</span>', unsafe_allow_html=True)
    uid = st.number_input("Enter your User ID for dashboard overview", step=1)

    # Metrics DisplayS
    transactions = fetch_transactions_by_user(uid)
    goals = fetch_saving_goals_by_user(uid)
    debts = fetch_debts_by_user(uid)

    income = sum(float(t['amount']) for t in transactions if t['type'].lower() == "income")
    expense = sum(float(t['amount']) for t in transactions if t['type'].lower() == "expense")
    total_savings = sum(float(goal["saved_amount"]) for goal in goals) if goals else 0.0
    total_debt_paid = sum(
    float(debt["amount"])
    for debt in debts
    if debt.get("status") and debt["status"].lower() == "paid"
    ) if debts else 0.0


    balance = float(income) - float(expense) - float(total_savings) - float(total_debt_paid)

    


    st.markdown(f"""
        <div class="card">
            <h4>Total Income</h4>
            <p>₹{income}</p>
        </div>
        <div class="card">
            <h4>Total Expense</h4>
            <p>₹{expense}</p>
        </div>
        <div class="card">
            <h4>Debt Paid</h4>
            <p>₹{total_debt_paid}</p>
        </div>
         <div class="card">
             <h4>Total Savings(Left For Use)</h4>
             <p>₹{total_savings}</p>
        </div>
        <div class="card">
            <h4>Balance(Left For Use)</h4>
            <p>₹{balance}</p>
        </div>
       

    """, unsafe_allow_html=True)

    # Pie Chart for Expense Breakdown
    transactions = fetch_transactions_by_user(uid)
    if len(transactions) > 0:
        expense_data = {
            "Category": [],
            "Amount": []
        }
        st.subheader("Your Transactions")
        df = pd.DataFrame(transactions)
        df["amount"] = df["amount"].astype(float)  # Optional, if 'Decimal'
        df["created_at"] = pd.to_datetime(df["created_at"])

        st.dataframe(df) 

        for transaction in transactions:
            if transaction["type"].lower() == "expense":  # Added .lower() to handle case-insensitive matches
                expense_data["Category"].append(transaction["category"])
                expense_data["Amount"].append(transaction["amount"])

        if expense_data["Category"]:
            expense_df = pd.DataFrame(expense_data)
            fig_expenses = px.pie(expense_df, names="Category", values="Amount", title="Expenses Breakdown")
            st.plotly_chart(fig_expenses)
        else:
            st.write("No expense data available.")
    else:
        st.write("No transactions data available.")

    # Pie Chart for Savings Breakdown
    if len(goals) > 0:
        saving_data = {
          "Goal": [],
          "Amount": []
    }

        for goal in goals:
           saving_data["Goal"].append(goal["goal_name"])
           saving_data["Amount"].append(goal["saved_amount"])

        if saving_data["Goal"]:
           saving_df = pd.DataFrame(saving_data)
           fig_savings = px.pie(saving_df, names="Goal", values="Amount", title="Savings Breakdown")
           st.plotly_chart(fig_savings)
        else:
           st.write("No savings data available.")
    else:
        st.write("No saving goals data available.")


    # Autopay Breakdown
    autopays = fetch_autopays_by_user(uid)
    if len(autopays) > 0:
       autopay_data = {
          "Payee": [],
          "Amount": []
    }

       for autopay in autopays:
           autopay_data["Payee"].append(autopay["user_name"])
           autopay_data["Amount"].append(autopay["amount"])

       if autopay_data["Payee"]:
          autopay_df = pd.DataFrame(autopay_data)
          fig_autopay = px.pie(autopay_df, names="Payee", values="Amount", title="Autopay Breakdown")
          st.plotly_chart(fig_autopay)
       else:
        st.write("No autopay data available.")
    else:
       st.write("No autopay data available.")

# === Transactions ===
elif menu == "Transactions":
    st.markdown('<span style="color:#4b6cb7;font-size:35px;font-family:Times New Roman"><br>💸 TRANSACTIONS</span>', unsafe_allow_html=True)
    with st.form("add_transaction"):
        Id = st.number_input("User ID", step=1)
        t_type = st.selectbox("Type", ["Income", "Expense"])
        amount = st.number_input("Amount", min_value=0.0)
        category = st.text_input("Category")
        submitted = st.form_submit_button("Add Transaction")
        if submitted:
           insert_transaction(Id, t_type, amount, category)
           st.success("Transaction saved to DB!")

    st.subheader("Your Transactions")
    transactions = fetch_transactions_by_user(Id)
    if transactions:
            df = pd.DataFrame(transactions)
            df["amount"] = df["amount"].astype(float)  # Optional, if 'Decimal'
            df["created_at"] = pd.to_datetime(df["created_at"])

            st.dataframe(df) 
    else:
        st.info("No transactions found.")
# === Savings ===
elif menu == "Savings":
    st.markdown('<span style="color:#4b6cb7;font-size:35px;font-family:Times New Roman"><br>💰YOUR SAVINGS</span>', unsafe_allow_html=True)
    
    uid = st.number_input("User ID", step=1)
    name = st.text_input("Goal Name")
    target = st.number_input("Target Amount", min_value=0.0)
    
    if st.button("Add Goal"):
        insert_saving_goal(uid, name, target)
        st.success("Saving goal added to DB.")

    st.subheader("ADD TO SAVINGS")
    goals = fetch_saving_goals_by_user(uid)
    goal_names = [g['goal_name'] for g in goals]
    
    if goal_names:
        selected = st.selectbox("Select Goal", goal_names)
        amt = st.number_input("Amount to Add", min_value=0.0)
        
        if st.button("Add to Savings"):
            update_savings(uid, selected, amt)
            st.success("Savings updated.")

    st.subheader("PROGRESS")
    for g in goals:
        pct = (g['saved_amount'] / g['target_amount']) * 100 if g['target_amount'] > 0 else 0
        st.progress(min(int(pct), 100), text=f"{g['goal_name']}: ₹{g['saved_amount']} / ₹{g['target_amount']} ({pct:.1f}%)")

    # Badge logic
    total_savings = sum(float(goal["saved_amount"]) for goal in goals) if goals else 0.0

    def get_savings_badge(total_savings):
        if total_savings >= 10000:
            return "Wealth Wizard 💎"
        elif total_savings >= 5000:
            return "Saving Streak 🚀"
        elif total_savings >= 3000:
            return "Smart Saver 🧠"
        elif total_savings >= 1000:
            return "Budget Buddy 💰"
        elif total_savings >= 500:
            return "Beginner Saver 🐣"
        else:
            return "Start Saving 🆕"

    if "prev_badge" not in st.session_state:
        st.session_state.prev_badge = None

    # Get the current badge based on the total savings
    current_badge = get_savings_badge(total_savings)

    if st.session_state.prev_badge != current_badge:
        if current_badge != "Start Saving 🆕":
            st.balloons()
        
        st.session_state.prev_badge = current_badge

    # Display the current badge
    st.markdown(f"<h4 style='color:#4b6cb7;padding:15px;'>YOUR SAVING BADGE🎖️ : {current_badge}</h4>", unsafe_allow_html=True)

# Debt 
elif menu == "Debt":
    st.markdown('<span style="color:#4b6cb7;font-size:45px;font-family: Times New Roman"><br>📃DEBTS TRACKER</span>', unsafe_allow_html=True)
    uid = st.number_input("User ID", step=1)
    name = st.text_input("Debt Name")
    amount = st.number_input("Debt Amount", min_value=0.0)
    due_date = st.date_input("Due Date")
    if st.button("Add Debt"):
       insert_debt(uid, name, amount, due_date)
       st.success("Debt added to DB.")


    st.subheader("Your Debts")
    debts = fetch_debts_by_user(uid)
    if debts:
       for d in debts:
           st.write(f"{d['name']} - ₹{d['amount']} - Due: {d['due_date']} - Status: {d['status']}")
           if d["status"] == "Unpaid":
               if st.button(f"Mark Paid: {d['name']}", key=f"debt_{d['id']}"):
                   mark_debt_as_paid(uid, d["name"])
                   st.success(f"Marked '{d['name']}' as Paid")
                   st.rerun()  # Refresh to show updated status
    else:
       st.info("No debts found.")


# === Budget ===
elif menu == "Budget":
    st.markdown('<span style="color:#4b6cb7; font-size:35px; font-family:Times New Roman"><br>🗓️BUDGET TRACKER</span>', unsafe_allow_html=True)
    uid = st.number_input("User ID", step=1)
    category = st.text_input("Category")
    amount = st.number_input("Budget Amount", min_value=0.0)
    days = st.number_input("Deadline (days)", min_value=0, step=1)
    if st.button("Set Budget"):
       deadline = datetime.now() + timedelta(days=int(days))
       insert_budget(uid, category, amount, deadline)
       st.success("Budget saved to database.")

    st.subheader("Your Budgets")
    budgets = fetch_budgets_by_user(uid)
    if budgets:
       for b in budgets:
          st.write(f"{b['category']}: ₹{b['amount']} - Deadline: {b['deadline']}")
    else:
       st.info("No budgets found.")

# === Autopay ===
elif menu == "Autopay":
    st.markdown('<span style="color:#4b6cb7;font-size:35px;font-family:Times New Roman"><br>🔁 AUTOPAY</span>', unsafe_allow_html=True)
    user_id = st.number_input("User ID", step=1)
    user = st.text_input("User Name")
    amount = st.number_input("Amount", min_value=0.0)
    purpose = st.text_input("Purpose")
    frequency = st.selectbox("Frequency", ["daily", "weekly", "monthly"])

    # Add autopay
    if st.button("Add Autopay"):
       next_payment = datetime.now() + timedelta(days=1)
       insert_autopay(user_id, user, amount, purpose, frequency, next_payment)
       st.success("Autopay added to DB.")


    # Run autopay now
    if st.button("Run Autopay Now"):
       due_autopays = fetch_due_autopay()
       if due_autopays:
           for auto in due_autopays:
               st.success(f"User {auto['user_id']} ({auto['user_name']}) paid ₹{auto['amount']} for {auto['purpose']} on {auto['next_payment'].strftime('%Y-%m-%d')}")
               update_next_autopay(auto['id'], auto['frequency'], auto['next_payment'])
       else:
           st.info("No autopayments due at the moment.")

    st.subheader("Your Scheduled Autopays")
    autos = fetch_autopays_by_user(user_id)
    if autos:
       for a in autos:
         col1, col2 = st.columns([4, 1])
         with col1:
            st.write(f"🔁 **{a['purpose']}** - ₹{a['amount']} every {a['frequency']} - Next: {a['next_payment'].strftime('%Y-%m-%d')}")
         with col2:
            if st.button(f"❌ Stop", key=f"stop_{a['id']}"):
                delete_autopay_by_id(a['id'])  # call your DB delete function
                st.warning(f"Autopay for {a['purpose']} has been stopped.")
                st.rerun()  # refreshes the view

    else:
        st.info("No autopays scheduled.")



# === Social Saving Pools ===
elif menu == "Social Saving Pools":
    st.markdown('<span style="color:#4b6cb7;font-size:35px;font-family:Times New Roman"><br>👥 SOCIAL SAVINGS POOL</span>', unsafe_allow_html=True)
    creator_id = st.number_input("Creator ID", step=1)
    pool_name = st.text_input("Pool Name")
    target = st.number_input("Target Amount", min_value=0.0)
    members_input = st.text_input("Eligible Member IDs (comma separated)")
    if st.button("Create Pool"):
        members = [int(uid.strip()) for uid in members_input.split(",") if uid.strip().isdigit()]
        create_saving_pool(pool_name, creator_id, target, members)
        st.success("Pool saved to database")


    st.subheader("Contribute to Pool")
    contributor_id = st.number_input("Your User ID", step=1, key="cont_id")
    pools = fetch_all_pools()
    pool_dict = {p['pool_name']: p for p in pools}

    pool = st.selectbox("Select Pool", list(pool_dict.keys()))
    if pool:
       pool_data = pool_dict[pool]
       members = [int(m.strip()) for m in pool_data['members'].split(',') if m.strip().isdigit()]
       amount = st.number_input("Amount", min_value=0.0, key="contribute_amt")

       if st.button("Contribute"):
           if contributor_id in members:
              add_contribution(pool_data['id'], contributor_id, amount)
              st.success("Contribution added")
           else:
              st.error("You are not authorized for this pool")


    st.subheader("View Pool Status")
    for pool_name, data in pool_dict.items():
        total = fetch_pool_contributions(data['id'])
        st.info(f"{pool_name}: ₹{total} / ₹{data['target']}")
