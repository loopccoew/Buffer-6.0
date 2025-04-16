#Business database(BIZTRACK) 
import mysql.connector
from mysql.connector import Error
from datetime import datetime, timedelta
import pandas as pd

# MySQL connection
def get_db_connection():
    try:
        conn = mysql.connector.connect(
            host="localhost",  # MySQL server host (e.g., localhost or an IP address)
            user="root",  # MySQL username
            password="root",  # MySQL password
            database="biztrack"  # Name of your database
        )
        
        if conn.is_connected():
            print("Successfully connected to the database.")
            return conn
        else:
            print("Failed to connect to the database.")
            return None

    except Error as e:
        print(f"Error: {e}")
        return None

# Insert transaction 
def insert_transaction(business_id, transaction_id, transaction_type, amount, date, description):
    conn = get_db_connection()
    if conn:
        try:
            cursor = conn.cursor()
            query = """
            INSERT INTO transactions (business_id, transaction_id, transaction_type, amount, date, description)
            VALUES (%s, %s, %s, %s, %s, %s)
            """
            values = (business_id, transaction_id, transaction_type, amount, date, description)
            cursor.execute(query, values)
            conn.commit()
            cursor.close()
            conn.close()
            print("Transaction inserted successfully.")
        except mysql.connector.Error as err:
            print(f"MySQL Error: {err}")

# Add debt 
def add_debt(name, amount, due_date):
    conn = get_db_connection()
    if conn:
        try:
            cursor = conn.cursor()
            query = '''
            INSERT INTO debts (name, amount, due_date, status)
            VALUES (%s, %s, %s, %s)
            '''
            cursor.execute(query, (name, amount, due_date, "Unpaid"))
            conn.commit()
            print("Debt added successfully.")
        except Error as e:
            print(f"Error while adding debt: {e}")
        finally:
            conn.close()

# Mark a debt as paid
def mark_debt_paid(debt_name):
    conn = get_db_connection()
    if conn:
        cursor = conn.cursor()
        query = '''
        UPDATE debts 
        SET status = %s
        WHERE name = %s AND status = %s
        '''
        cursor.execute(query, ("Paid", debt_name, "Unpaid"))
        conn.commit()
        conn.close()
        print(f"Debt {debt_name} marked as paid.")

# Get unpaid debts
def get_debts():
    conn = get_db_connection()
    if conn:
        cursor = conn.cursor()
        cursor.execute("SELECT name, amount, due_date, status FROM debts WHERE status = 'Unpaid'")
        debts = cursor.fetchall()
        conn.close()
        return debts
    return []

# Add autopay entry 
def add_autopay(user, amount, purpose, frequency, start_time):
    conn = get_db_connection()
    if conn:
        try:
            cursor = conn.cursor()
            query = """
            INSERT INTO autopay (user, amount, purpose, frequency, next_payment)
            VALUES (%s, %s, %s, %s, %s)
            """
            cursor.execute(query, (user, amount, purpose, frequency, start_time.strftime("%Y-%m-%d %H:%M:%S")))
            conn.commit()
            cursor.close()
            conn.close()
            print("Autopay added successfully.")
        except mysql.connector.Error as err:
            print(f"MySQL Error: {err}")

# Run due autopay
def run_due_autopay():
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    conn = get_db_connection()
    if conn:
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM autopay WHERE next_payment <= %s", (now,))
        due_tasks = cursor.fetchall()
        processed = []
        for task in due_tasks:
            user, amount, purpose, freq, next_time = task[1], task[2], task[3], task[4], task[5]
            processed.append(f"{user} paid {amount} for {purpose}")
            # Schedule next payment
            next_dt = datetime.strptime(next_time, "%Y-%m-%d %H:%M:%S")
            if freq == "daily":
                next_dt += timedelta(days=1)
            elif freq == "weekly":
                next_dt += timedelta(weeks=1)
            elif freq == "monthly":
                next_dt += timedelta(days=30)
            cursor.execute("UPDATE autopay SET next_payment = %s WHERE id = %s", (next_dt.strftime("%Y-%m-%d %H:%M:%S"), task[0]))
        conn.commit()
        conn.close()
        return processed
    return []

# Add or update budget 
def add_budget(category, amount):
    conn = get_db_connection()
    if conn:
        try:
            cursor = conn.cursor()
            query = "INSERT INTO budgets (category, amount) VALUES (%s, %s) ON DUPLICATE KEY UPDATE amount = %s"
            cursor.execute(query, (category, amount, amount))
            conn.commit()
            cursor.close()
            conn.close()
            print("Budget updated successfully.")
        except mysql.connector.Error as err:
            print(f"MySQL Error: {err}")

# Update budget
def update_budget(category, amount):
    conn = get_db_connection()
    if conn:
        cursor = conn.cursor()
        query = "UPDATE budgets SET amount = %s WHERE category = %s"
        cursor.execute(query, (amount, category))
        conn.commit()
        conn.close()
        return cursor.rowcount > 0

# View budgets
def view_budgets():
    conn = get_db_connection()
    if conn:
        return pd.read_sql_query("SELECT * FROM budgets", conn)
    return pd.DataFrame()

# Get all transactions
def get_transactions():
    conn = get_db_connection()
    if conn:
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM transactions ORDER BY date DESC")
        results = cursor.fetchall()
        cursor.close()
        conn.close()
        return results
    return []

# Get all autopay
def get_autopay():
    conn = get_db_connection()
    if conn:
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT user, amount, purpose, frequency, next_payment FROM autopay")
        results = cursor.fetchall()
        cursor.close()
        conn.close()
        return results
    return []

# Update used amount in budget
def update_budget_used(category, used_amount):
    conn = get_db_connection()
    if conn:
        cursor = conn.cursor()
        query = "UPDATE budgets SET used = used + %s WHERE category = %s"
        cursor.execute(query, (used_amount, category))
        conn.commit()
        conn.close()
        return cursor.rowcount > 0