# helpline_ui.py
import tkinter as tk  # Import tkinter for GUI

def show_helpline_numbers():
    helpline_window = tk.Toplevel()  # Create a new top-level window
    helpline_window.title("Emergency Helpline Numbers")  # Set window title
    helpline_window.geometry("350x350")  # Set window size

    tk.Label(helpline_window, text="Emergency Helplines", font=("Arial", 14, "bold")).pack(pady=10)  # Heading label

    helplines = [  # List of helpline names and their numbers
        ("Police", "100"),
        ("Women's Helpline", "1091"),
        ("Ambulance", "102"),
        ("National Emergency", "112"),
        ("Fire Brigade", "101"),
        ("Child Helpline", "1098"),
        ("Disaster Management", "108"),
        ("Cyber Crime", "1930"),
        ("Mental Health Helpline", "9152987821"),
    ]

    for name, number in helplines:  # Loop through each helpline
        tk.Label(helpline_window, text=f"{name}: {number}", font=("Arial", 11)).pack(anchor="w", padx=20)  # Display each helpline

    tk.Button(helpline_window, text="Close", command=helpline_window.destroy).pack(pady=10)  # Button to close the window
