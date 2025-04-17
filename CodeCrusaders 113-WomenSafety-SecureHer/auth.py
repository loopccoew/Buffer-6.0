import tkinter as tk
from tkinter import messagebox
from db import get_connection
from session import current_user
import main_menu
import re

def signup():
    def submit_signup():
        name = name_entry.get().strip()
        contact = contact_entry.get().strip()
        email = email_entry.get().strip()
        password = password_entry.get().strip()

        if not re.match(r"^[0-9]{10}$", contact):
            messagebox.showerror("Error", "Please enter a valid 10-digit contact number.")
            return

        if not re.match(r"[^@]+@[^@]+\.[^@]+", email):
            messagebox.showerror("Error", "Please enter a valid email address.")
            return

        try:
            conn = get_connection()
            cursor = conn.cursor()

            # Check if the contact number already exists in the database
            cursor.execute("SELECT * FROM users WHERE contact=%s", (contact,))
            if cursor.fetchone():
                messagebox.showerror("Error", "User already exists.")
                return

            # Insert the new user into the database
            cursor.execute("INSERT INTO users (name, contact, email, password) VALUES (%s, %s, %s, %s)", 
                           (name, contact, email, password))
            conn.commit()
            conn.close()

            messagebox.showinfo("Success", "Signup successful. Please login.")
            signup_window.destroy()

        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {e}")
            signup_window.destroy()

    signup_window = tk.Toplevel()
    signup_window.update_idletasks()
    signup_window.title("Signup")
    signup_window.configure(bg="black")

    label_style = {"bg": "black", "fg": "light blue", "font": ("Arial", 11)}

    tk.Label(signup_window, text="Name", **label_style).grid(row=0, column=0, padx=10, pady=10)
    name_entry = tk.Entry(signup_window)
    name_entry.grid(row=0, column=1, padx=10, pady=10)

    tk.Label(signup_window, text="Contact", **label_style).grid(row=1, column=0, padx=10, pady=10)
    contact_entry = tk.Entry(signup_window)
    contact_entry.grid(row=1, column=1, padx=10, pady=10)

    tk.Label(signup_window, text="Email", **label_style).grid(row=2, column=0, padx=10, pady=10)
    email_entry = tk.Entry(signup_window)
    email_entry.grid(row=2, column=1, padx=10, pady=10)

    tk.Label(signup_window, text="Password", **label_style).grid(row=3, column=0, padx=10, pady=10)
    password_entry = tk.Entry(signup_window, show="*")
    password_entry.grid(row=3, column=1, padx=10, pady=10)

    tk.Button(
        signup_window, text="Submit", command=submit_signup,
        bg="light blue", fg="black", font=("Arial", 10, "bold"),
        activebackground="#add8e6", activeforeground="black",
        width=15, height=1
    ).grid(row=4, column=1, pady=10)


def login():
    def submit_login():
        name = name_entry.get()
        password = password_entry.get()

        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT user_id, name, contact FROM users WHERE name=%s AND password=%s", (name, password))
        user = cursor.fetchone()
        conn.close()

        if user:
            current_user[0] = user
            messagebox.showinfo("Success", f"Welcome {user[1]}!")
            login_window.destroy()
            main_menu.open_main_menu(user)
        else:
            messagebox.showerror("Error", "Invalid credentials.")

    login_window = tk.Toplevel()
    login_window.update_idletasks()
    login_window.title("Login")
    login_window.configure(bg="black")

    label_style = {"bg": "black", "fg": "light blue", "font": ("Arial", 11)}

    tk.Label(login_window, text="Name", **label_style).grid(row=0, column=0, padx=10, pady=10)
    name_entry = tk.Entry(login_window)
    name_entry.grid(row=0, column=1, padx=10, pady=10)

    tk.Label(login_window, text="Password", **label_style).grid(row=1, column=0, padx=10, pady=10)
    password_entry = tk.Entry(login_window, show="*")
    password_entry.grid(row=1, column=1, padx=10, pady=10)

    tk.Button(
        login_window, text="Login", command=submit_login,
        bg="light blue", fg="black", font=("Arial", 10, "bold"),
        activebackground="#add8e6", activeforeground="black",
        width=15, height=1
    ).grid(row=2, column=1, pady=10)
