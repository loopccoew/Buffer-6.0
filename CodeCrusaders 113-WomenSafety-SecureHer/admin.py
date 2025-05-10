import tkinter as tk 
from tkinter import messagebox
import mysql.connector
 

def admin_login():  # Function to handle admin login
    def check_credentials():
        username = entry_user.get()  # Retrieve the username
        password = entry_pass.get()  # and password entered by the admin

        # Establish connection to the MySQL database
        conn = mysql.connector.connect(
            host="localhost",
            user="root",
            password="root",
            database="SecureHer"
        )
        cursor = conn.cursor()  # Create a cursor to interact with the database
        
        # Query to check if entered credentials match with the admin table
        cursor.execute("SELECT * FROM admin WHERE adname=%s AND password=%s", (username, password))
        result = cursor.fetchone()  # Fetch the result of the query
        conn.close()  # Close the database connection

        # If credentials match, show success message and open report view
        if result:
            messagebox.showinfo("Success", f"Welcome {username}!")
            login_window.destroy()  # Close the login window
            view_reports()  # Proceed to view the reports
        else:
            messagebox.showerror("Error", "Invalid admin credentials")  # Show error if credentials don't match

    # Create a new window for admin login
    login_window = tk.Toplevel()
    login_window.title("Admin Login")
    login_window.geometry("300x200")  # Size of the window

    # Username field for admin login
    tk.Label(login_window, text="Admin Username").pack(pady=5)
    entry_user = tk.Entry(login_window)  # Entry widget for username
    entry_user.pack(pady=5)

    # Password field for admin login
    tk.Label(login_window, text="Password").pack(pady=5)
    entry_pass = tk.Entry(login_window, show='*')  # Entry widget for password with hidden characters
    entry_pass.pack(pady=5)

    # Button to trigger the check_credentials function
    tk.Button(login_window, text="Login", command=check_credentials).pack(pady=10)


# Function to view all the reports (offline and online)
def view_reports():
    report_window = tk.Toplevel()  # Create a new window to display the reports
    report_window.title("User Reports")  # Title of the report window
    report_window.geometry("900x600")  # Size of the report window

    # Text widget to display the reports
    report_text = tk.Text(report_window, wrap=tk.WORD, font=("Arial", 10))
    report_text.pack(expand=True, fill='both', padx=10, pady=10)

    # Establish connection to the MySQL database
    try:
        conn = mysql.connector.connect(
            host="localhost",
            user="root",
            password="root",
            database="SecureHer"
        )
        cursor = conn.cursor()

        # Query to retrieve offline reports
        cursor.execute("""
            SELECT o.id, o.name, o.contact, o.category, o.description, l.name AS location, o.time
            FROM offline_reports o
            LEFT JOIN locations l ON o.location_id = l.id
        """)
        offline_reports = cursor.fetchall()

        # Insert offline reports into the text widget
        report_text.insert(tk.END, "==== OFFLINE REPORTS ====\n\n")
        for r in offline_reports:
            report_text.insert(tk.END, f"ID: {r[0]}\nName: {r[1]}\nContact: {r[2]}\nCategory: {r[3]}\nDescription: {r[4]}\nLocation: {r[5]}\nTime: {r[6]}\n\n")

        # Query to retrieve online reports
        cursor.execute("""
            SELECT id, name, contact, category, description, platform, url, reported_time
            FROM online_reports
        """)
        online_reports = cursor.fetchall()

        # Insert online reports into the text widget
        report_text.insert(tk.END, "\n==== ONLINE REPORTS ====\n\n")
        for r in online_reports:
            report_text.insert(tk.END, f"ID: {r[0]}\nName: {r[1]}\nContact: {r[2]}\nCategory: {r[3]}\nDescription: {r[4]}\nPlatform: {r[5]}\nURL: {r[6]}\nReported Time: {r[7]}\n\n")

        conn.close()  # Close the database connection

    # Handle database connection errors
    except mysql.connector.Error as err:
        messagebox.showerror("Database Error", f"Error: {err}") 
