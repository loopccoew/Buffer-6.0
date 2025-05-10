# safe_route_ui.py

import tkinter as tk
from tkinter import messagebox
from db import get_connection  # Import database connection function
from safe_route import get_safe_route  # Import function to find the safe route


def fetch_location_names():
    """
    Fetches all location names from the 'locations' table, ordered by ID.
    Returns a list of location names.
    """
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT name FROM locations ORDER BY id")
    names = [row[0] for row in cursor.fetchall()]
    cursor.close()
    conn.close()
    return names


def open_safe_route_window():
    """
    Opens a new window that allows the user to select start and destination
    locations and find the safest route between them.
    """
    locations = fetch_location_names()

    if not locations or len(locations) < 2:
        messagebox.showerror("Error", "Not enough locations in the database.")
        return

    # === Create main window for safe route finder ===
    route_window = tk.Toplevel()
    route_window.title("SecureHer - Safe Route Finder")
    route_window.geometry("600x500")
    route_window.configure(bg="black")  # Set background color to black

    # === Animated Heading ===
    heading = tk.Label(route_window, text="SecureHer", font=("Helvetica", 20, "bold"), fg="skyblue", bg="black")
    heading.pack(pady=(30, 10))

    # Define colors and index for animation
    colors = ["deepskyblue", "cyan", "aqua", "lightblue"]
    color_index = [0]  # Use list to allow modification inside inner function

    def animate_heading():
        """
        Cycles through colors to animate the heading text.
        """
        heading.config(fg=colors[color_index[0]])
        color_index[0] = (color_index[0] + 1) % len(colors)
        route_window.after(500, animate_heading)  # Call itself after 500ms

    animate_heading()  # Start animation

    # === Subheading ===
    subheading = tk.Label(route_window, text="Safe Route Finder", font=("Helvetica", 12), fg="white", bg="black")
    subheading.pack(pady=(0, 30))

    # === Dropdown Styling Parameters ===
    label_style = {"font": ("Helvetica", 12), "fg": "white", "bg": "black"}
    option_style = {"font": ("Helvetica", 12)}  # (Prepared for customization if needed)

    # === Start Location Dropdown ===
    tk.Label(route_window, text="Start Location", **label_style).pack(pady=5)
    start_var = tk.StringVar()
    start_var.set(locations[0])  # Set default start location
    tk.OptionMenu(route_window, start_var, *locations).pack()

    # === Destination Location Dropdown ===
    tk.Label(route_window, text="Destination", **label_style).pack(pady=5)
    dest_var = tk.StringVar()
    dest_var.set(locations[1])  # Set default destination
    tk.OptionMenu(route_window, dest_var, *locations).pack()

    # === Button Styling Parameters ===
    btn_style = {"bg": "lightblue", "fg": "black", "width": 25, "height": 2, "font": ("Helvetica", 12, "bold")}

    def find_route():
        """
        Callback when 'Get Safe Route' button is clicked.
        Validates input and calls get_safe_route function.
        """
        start = start_var.get()
        dest = dest_var.get()

        if start == dest:
            messagebox.showwarning("Invalid", "Start and destination cannot be the same.")
            return

        try:
            get_safe_route(start, dest)
        except Exception as e:
            messagebox.showerror("Error", f"Error: {e}")

    # === Buttons ===
    tk.Button(route_window, text="Get Safe Route", command=find_route, **btn_style).pack(pady=30)
    tk.Button(route_window, text="Close", command=route_window.destroy, **btn_style).pack()
