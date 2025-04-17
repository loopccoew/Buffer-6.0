# safe_route_ui.py
import tkinter as tk
from tkinter import messagebox
from db import get_connection
from safe_route import get_safe_route

def fetch_location_names():
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT name FROM locations ORDER BY id")
    names = [row[0] for row in cursor.fetchall()]
    cursor.close()
    conn.close()
    return names

def open_safe_route_window():
    locations = fetch_location_names()
    if not locations or len(locations) < 2:
        messagebox.showerror("Error", "Not enough locations in the database.")
        return

    # Create window
    route_window = tk.Toplevel()
    route_window.title("SecureHer - Safe Route Finder")
    route_window.geometry("600x500")
    route_window.configure(bg="black")

    # === Animated Heading ===
    heading = tk.Label(route_window, text="SecureHer", font=("Helvetica", 20, "bold"), fg="skyblue", bg="black")
    heading.pack(pady=(30, 10))

    colors = ["deepskyblue", "cyan", "aqua", "lightblue"]
    color_index = [0]

    def animate_heading():
        heading.config(fg=colors[color_index[0]])
        color_index[0] = (color_index[0] + 1) % len(colors)
        route_window.after(500, animate_heading)

    animate_heading()

    # === Subheading ===
    subheading = tk.Label(route_window, text="Safe Route Finder", font=("Helvetica", 12), fg="white", bg="black")
    subheading.pack(pady=(0, 30))

    # === Dropdown Styling ===
    label_style = {"font": ("Helvetica", 12), "fg": "white", "bg": "black"}
    option_style = {"font": ("Helvetica", 12)}

    tk.Label(route_window, text="Start Location", **label_style).pack(pady=5)
    start_var = tk.StringVar()
    start_var.set(locations[0])
    tk.OptionMenu(route_window, start_var, *locations).pack()

    tk.Label(route_window, text="Destination", **label_style).pack(pady=5)
    dest_var = tk.StringVar()
    dest_var.set(locations[1])
    tk.OptionMenu(route_window, dest_var, *locations).pack()

    # === Button Styling ===
    btn_style = {"bg": "lightblue", "fg": "black", "width": 25, "height": 2, "font": ("Helvetica", 12, "bold")}

    def find_route():
        start = start_var.get()
        dest = dest_var.get()
        if start == dest:
            messagebox.showwarning("Invalid", "Start and destination cannot be the same.")
            return
        try:
            get_safe_route(start, dest)
        except Exception as e:
            messagebox.showerror("Error", f"Error: {e}")

    tk.Button(route_window, text="Get Safe Route", command=find_route, **btn_style).pack(pady=30)
    tk.Button(route_window, text="Close", command=route_window.destroy, **btn_style).pack()

