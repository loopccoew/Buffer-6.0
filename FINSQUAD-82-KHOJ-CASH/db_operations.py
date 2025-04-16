import mysql.connector

def connect():
    return mysql.connector.connect(
        host="localhost",
        user="root",
        password="root",
        database="finaro"
    )

def insert_transaction(user_id, t_type, amount, category):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO transactions (user_id, type, amount, category) VALUES (%s, %s, %s, %s)",
        (user_id, t_type, amount, category)
    )
    conn.commit()
    conn.close()

def fetch_transactions_by_user(user_id):
    conn = connect()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM transactions WHERE user_id = %s", (user_id,))
    results = cursor.fetchall()
    conn.close()
    return results


def insert_budget(user_id, category, amount, deadline):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO budgets (user_id, category, amount, deadline) VALUES (%s, %s, %s, %s)",
        (user_id, category, amount, deadline)
    )
    conn.commit()
    conn.close()

def fetch_budgets_by_user(user_id):
    conn = connect()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM budgets WHERE user_id = %s", (user_id,))
    results = cursor.fetchall()
    conn.close()
    return results

def insert_saving_goal(user_id, goal_name, target_amount):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO savings_goals (user_id, goal_name, target_amount) VALUES (%s, %s, %s)",
        (user_id, goal_name, target_amount)
    )
    conn.commit()
    conn.close()

def update_savings(user_id, goal_name, amount_to_add):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "UPDATE savings_goals SET saved_amount = saved_amount + %s WHERE user_id = %s AND goal_name = %s",
        (amount_to_add, user_id, goal_name)
    )
    conn.commit()
    conn.close()

def fetch_saving_goals_by_user(user_id):
    conn = connect()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM savings_goals WHERE user_id = %s", (user_id,))
    results = cursor.fetchall()
    conn.close()
    return results

def insert_debt(user_id, name, amount, due_date):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO debts (user_id, name, amount, due_date) VALUES (%s, %s, %s, %s)",
        (user_id, name, amount, due_date)
    )
    conn.commit()
    conn.close()

def fetch_debts_by_user(user_id):
    conn = connect()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM debts WHERE user_id = %s", (user_id,))
    results = cursor.fetchall()
    conn.close()
    return results

def mark_debt_as_paid(user_id, name):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "UPDATE debts SET status = 'Paid' WHERE user_id = %s AND name = %s",
        (user_id, name)
    )
    conn.commit()
    conn.close()

from datetime import timedelta

def insert_autopay(user_id, user_name, amount, purpose, frequency, next_payment):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO autopay (user_id, user_name, amount, purpose, frequency, next_payment) VALUES (%s, %s, %s, %s, %s, %s)",
        (user_id, user_name, amount, purpose, frequency, next_payment)
    )
    conn.commit()
    conn.close()

def fetch_due_autopay():
    conn = connect()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM autopay WHERE next_payment <= NOW()")
    results = cursor.fetchall()
    conn.close()
    return results

def update_next_autopay(autopay_id, frequency, current_next_date):
    conn = connect()
    cursor = conn.cursor()
    if frequency == 'daily':
        new_date = current_next_date + timedelta(days=1)
    elif frequency == 'weekly':
        new_date = current_next_date + timedelta(weeks=1)
    elif frequency == 'monthly':
        new_date = current_next_date + timedelta(days=30)
    else:
        new_date = current_next_date + timedelta(days=1)  # fallback

    cursor.execute(
        "UPDATE autopay SET next_payment = %s WHERE id = %s",
        (new_date, autopay_id)
    )
    conn.commit()
    conn.close()

def fetch_autopays_by_user(user_id):
    conn = connect()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM autopay WHERE user_id = %s", (user_id,))
    results = cursor.fetchall()
    conn.close()
    return results

def delete_autopay_by_id(autopay_id):
    conn=connect()
    c = conn.cursor()
    query = "DELETE FROM autopay WHERE id = %s"
    c.execute(query, (autopay_id,))
    conn.commit()



def create_saving_pool(pool_name, creator_id, target, members_list):
    members_str = ",".join(str(m) for m in members_list)
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO saving_pools (pool_name, creator_id, target, members) VALUES (%s, %s, %s, %s)",
        (pool_name, creator_id, target, members_str)
    )
    conn.commit()
    conn.close()

def fetch_all_pools():
    conn = connect()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM saving_pools")
    results = cursor.fetchall()
    conn.close()
    return results

def add_contribution(pool_id, contributor_id, amount):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO pool_contributions (pool_id, contributor_id, amount) VALUES (%s, %s, %s)",
        (pool_id, contributor_id, amount)
    )
    conn.commit()
    conn.close()

def fetch_pool_contributions(pool_id):
    conn = connect()
    cursor = conn.cursor()
    cursor.execute("SELECT SUM(amount) FROM pool_contributions WHERE pool_id = %s", (pool_id,))
    total = cursor.fetchone()[0]
    conn.close()
    return total or 0


