# report_selection.py

# Import tkinter module for GUI
import tkinter as tk

# Import functions to open the respective report windows
from online_report_ import open_online_report_window
from offline_report import open_offline_report_window

# Function to open the report selection window
def open_report_selection_window(user):
    # Create a new top-level window
    window = tk.Toplevel()
    window.title("Add Report")  # Set window title
    window.geometry("280x200")  # Set window size
    window.configure(bg="black")  # Set background color to black

    # Heading label for the window
    tk.Label(
        window,
        text="Choose Report Type",
        font=("Arial", 13, "bold"),
        fg="light blue",
        bg="black"
    ).pack(pady=20)

    # Define a common style for the buttons
    button_style = {
        "bg": "light blue",  # Button background color
        "fg": "black",  # Button text color
        "font": ("Arial", 11, "bold"),  # Button font
        "width": 20,  # Button width
        "height": 2,  # Button height
        "activebackground": "#add8e6",  # Button background when clicked
        "activeforeground": "black"  # Button text color when clicked
    }

    # Button to open the Online Report window
    tk.Button(
        window,
        text="Online Report",
        command=lambda: [window.destroy(), open_online_report_window(user)],
        **button_style
    ).pack(pady=6)

    # Button to open the Offline Report window
    tk.Button(
        window,
        text="Offline Report",
        command=lambda: [window.destroy(), open_offline_report_window(user)],
        **button_style
    ).pack(pady=6)
