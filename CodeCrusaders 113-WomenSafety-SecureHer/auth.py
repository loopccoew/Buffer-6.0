import tkinter as tk            # Import tkinter for creating GUI windows and widgets
from tkinter import messagebox  # Import messagebox to show popup messages (like errors or success alerts)
from db import get_connection   # Import database connection function
from session import current_user# Import current user session variable to store logged-in user info
import main_menu                # Import main menu module to open after login/signup
import re                       # Import re module for regular expressions (used for validating contact and email)


def signup():    # Function to handle user signup
    def submit_signup():
        # Get user inputs
        name = name_entry.get().strip()
        contact = contact_entry.get().strip()
        email = email_entry.get().strip()
        password = password_entry.get().strip()

         # Validate contact number(10 digits)
        if not re.match(r"^[0-9]{10}$", contact):
            messagebox.showerror("Error", "Please enter a valid 10-digit contact number.")
            return

        # Validate email address format
        if not re.match(r"[^@]+@[^@]+\.[^@]+", email):
            messagebox.showerror("Error", "Please enter a valid email address.")
            return

        try:
            conn = get_connection()  #database connection
            cursor = conn.cursor()

            # Check if the contact number already exists in the database
            cursor.execute("SELECT * FROM users WHERE contact=%s", (contact,))
            if cursor.fetchone():
                messagebox.showerror("Error", "User already exists.")
                return

            # Insert the new user into the database
            cursor.execute("INSERT INTO users (name, contact, email, password) VALUES (%s, %s, %s, %s)", 
                           (name, contact, email, password))
            conn.commit() #save changes
            conn.close()  #close connection

            # Show success message and close signup window
            messagebox.showinfo("Success", "Signup successful. Please login.")
            signup_window.destroy()

        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {e}")
            signup_window.destroy()

    # Create a new top-level window for signup form
    signup_window = tk.Toplevel()
    signup_window.update_idletasks()   # Force update the window's tasks    
    signup_window.title("Signup")      # Set the window title
    signup_window.configure(bg="black")        

     # Define label styling for consistency
    label_style = {"bg": "black", "fg": "light blue", "font": ("Arial", 11)}

     # Create labels and entry fields for user input
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
    password_entry = tk.Entry(signup_window, show="*")   # Hide password input
    password_entry.grid(row=3, column=1, padx=10, pady=10)

    # Create Submit button to trigger signup process
    tk.Button(
        signup_window, text="Submit", command=submit_signup,
        bg="light blue", fg="black", font=("Arial", 10, "bold"),
        activebackground="#add8e6", activeforeground="black",
        width=15, height=1
    ).grid(row=4, column=1, pady=10)


# Function to handle user login
def login():
    def submit_login():
        name = name_entry.get()
        password = password_entry.get()

        conn = get_connection()
        cursor = conn.cursor()

         # Query to check if user credentials are valid
        cursor.execute("SELECT user_id, name, contact FROM users WHERE name=%s AND password=%s", (name, password))
        user = cursor.fetchone()
        conn.close()

         # If user exists, update session and open main menu
        if user:
            current_user[0] = user
            messagebox.showinfo("Success", f"Welcome {user[1]}!")
            login_window.destroy()
            main_menu.open_main_menu(user)
        else:
            messagebox.showerror("Error", "Invalid credentials.")

    # Create a new top-level window for login form
    login_window = tk.Toplevel()
    login_window.update_idletasks()    # Force the window to finish loading all its elements properly
    login_window.title("Login")
    login_window.configure(bg="black")

    label_style = {"bg": "black", "fg": "light blue", "font": ("Arial", 11)}  # Define label styling

    # Create labels and entry fields for username and password
    tk.Label(login_window, text="Name", **label_style).grid(row=0, column=0, padx=10, pady=10)
    name_entry = tk.Entry(login_window)
    name_entry.grid(row=0, column=1, padx=10, pady=10)

    tk.Label(login_window, text="Password", **label_style).grid(row=1, column=0, padx=10, pady=10)
    password_entry = tk.Entry(login_window, show="*")
    password_entry.grid(row=1, column=1, padx=10, pady=10)

    # Create Login button to trigger login process
    tk.Button(
        login_window, text="Login", command=submit_login,
        bg="light blue", fg="black", font=("Arial", 10, "bold"),
        activebackground="#add8e6", activeforeground="black",
        width=15, height=1
    ).grid(row=2, column=1, pady=10)
