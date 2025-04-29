import tkinter as tk
from report_selection import open_report_selection_window #to have functioning of report selection import this method from related module
from view_reports import view_reports_window #to have functioning of view reports import this method from related module
from safe_route_ui import open_safe_route_window
from location_danger_history import open_danger_location_ui
from helpline_ui import show_helpline_numbers  # ⬅ Import helpline function
from evidence import open_evidence_ui  
 #import functions from other modules

# Function to open the main menu window after user logs in
def open_main_menu(user):
    
    menu = tk.Toplevel()
    menu.update_idletasks()# Update the window tasks to reflect changes immediately
    menu.title("Main Menu") # tilte of this page 
    menu.geometry("320x500")
    menu.configure(bg="black") # black background colour

# Create a welcome label displaying the user's name
    tk.Label(
        menu,
        text=f"Welcome, {user[1]}",# user[1] is the name part from user data (like username)
        font=("Arial", 14, "bold"),
        fg="light blue",
        bg="black"
    ).pack(pady=15)


    button_style = {# Define a common button style to reuse for all buttons (using dictionary and ** unpacking)
        "bg": "light blue",
        "fg": "black",
        "font": ("Arial", 11, "bold"),
        "width": 35,
        "height": 3,
        "activebackground": "#add8e6", # Button background color when clicked
        "activeforeground": "black" # Button text color when clicked
    }

# Create buttons for each feature
    tk.Button(menu, text="Report", command=lambda: open_report_selection_window(user), **button_style).pack(pady=6)# When clicked, open report selection window, passing user info
    tk.Button(menu, text="View My Reports", command=lambda: view_reports_window(user), **button_style).pack(pady=6) # Open user's reports
    tk.Button(menu, text="Get Safe Route", command=open_safe_route_window, **button_style).pack(pady=6)# Open safe route window (no user data needed)
    tk.Button(menu, text="High Risk Locations", command=open_danger_location_ui, **button_style).pack(pady=6)
    tk.Button(menu, text="Emergency Helplines", command=show_helpline_numbers, **button_style).pack(pady=6)# Evidence Storage Button
    tk.Button(menu,text="Evidence Storage",command=lambda: [ open_evidence_ui(user, lambda: open_main_menu(user))],**button_style).pack(pady=6)
    tk.Button(menu, text="Logout", command=menu.destroy, **button_style).pack(pady=15)# Logout button to close this window and return to login page
