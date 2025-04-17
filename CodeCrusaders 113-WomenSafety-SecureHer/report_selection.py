# report_selection.py

import tkinter as tk
from online_report_ import open_online_report_window
from offline_report import open_offline_report_window

def open_report_selection_window(user):
    window = tk.Toplevel()
    window.title("Add Report")
    window.geometry("280x200")
    window.configure(bg="black")

    # Heading label
    tk.Label(
        window,
        text="Choose Report Type",
        font=("Arial", 13, "bold"),
        fg="light blue",
        bg="black"
    ).pack(pady=20)

    # Common button style
    button_style = {
        "bg": "light blue",
        "fg": "black",
        "font": ("Arial", 11, "bold"),
        "width": 20,
        "height": 2,
        "activebackground": "#add8e6",
        "activeforeground": "black"
    }

    tk.Button(window, text="Online Report", command=lambda: [window.destroy(), open_online_report_window(user)], **button_style).pack(pady=6)
    tk.Button(window, text="Offline Report", command=lambda: [window.destroy(), open_offline_report_window(user)], **button_style).pack(pady=6)
