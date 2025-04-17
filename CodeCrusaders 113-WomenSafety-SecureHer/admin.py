import tkinter as tk
from tkinter import messagebox
import mysql.connector

def admin_login():
    def check_credentials():
        username = entry_user.get()
        password = entry_pass.get()

        conn = mysql.connector.connect(
            host="localhost",
            user="root",
            password="root",
            database="SecureHer"
        )
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM admin WHERE adname=%s AND password=%s", (username, password))
        result = cursor.fetchone()
        conn.close()

        if result:
            messagebox.showinfo("Success", f"Welcome {username}!")
            login_window.destroy()
            view_reports()
        else:
            messagebox.showerror("Error", "Invalid admin credentials")

    login_window = tk.Toplevel()
    login_window.title("Admin Login")
    login_window.geometry("300x200")

    tk.Label(login_window, text="Admin Username").pack(pady=5)
    entry_user = tk.Entry(login_window)
    entry_user.pack(pady=5)

    tk.Label(login_window, text="Password").pack(pady=5)
    entry_pass = tk.Entry(login_window, show='*')
    entry_pass.pack(pady=5)

    tk.Button(login_window, text="Login", command=check_credentials).pack(pady=10)


def view_reports():
    report_window = tk.Toplevel()
    report_window.title("User Reports")
    report_window.geometry("900x600")

    report_text = tk.Text(report_window, wrap=tk.WORD, font=("Arial", 10))
    report_text.pack(expand=True, fill='both', padx=10, pady=10)

    try:
        conn = mysql.connector.connect(
            host="localhost",
            user="root",
            password="root",
            database="SecureHer"
        )
        cursor = conn.cursor()

        # Offline Reports
        cursor.execute("""
            SELECT o.id, o.name, o.contact, o.category, o.description, l.name AS location, o.time
            FROM offline_reports o
            LEFT JOIN locations l ON o.location_id = l.id
        """)
        offline_reports = cursor.fetchall()

        report_text.insert(tk.END, "==== OFFLINE REPORTS ====\n\n")
        for r in offline_reports:
            report_text.insert(tk.END, f"ID: {r[0]}\nName: {r[1]}\nContact: {r[2]}\nCategory: {r[3]}\nDescription: {r[4]}\nLocation: {r[5]}\nTime: {r[6]}\n\n")

        # Online Reports
        cursor.execute("""
            SELECT id, name, contact, category, description, platform, url, reported_time
            FROM online_reports
        """)
        online_reports = cursor.fetchall()

        report_text.insert(tk.END, "\n==== ONLINE REPORTS ====\n\n")
        for r in online_reports:
            report_text.insert(tk.END, f"ID: {r[0]}\nName: {r[1]}\nContact: {r[2]}\nCategory: {r[3]}\nDescription: {r[4]}\nPlatform: {r[5]}\nURL: {r[6]}\nReported Time: {r[7]}\n\n")

        conn.close()

    except mysql.connector.Error as err:
        messagebox.showerror("Database Error", f"Error: {err}")
