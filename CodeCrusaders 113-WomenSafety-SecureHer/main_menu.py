import tkinter as tk
from report_selection import open_report_selection_window
from view_reports import view_reports_window
from safe_route_ui import open_safe_route_window
from location_danger_history import open_danger_location_ui
from helpline_ui import show_helpline_numbers  # ⬅ Import helpline function
from evidence import open_evidence_ui  

def open_main_menu(user):
    menu = tk.Toplevel()
    menu.update_idletasks()
    menu.title("Main Menu")
    menu.geometry("320x500")
    menu.configure(bg="black")

    tk.Label(
        menu,
        text=f"Welcome, {user[1]}",
        font=("Arial", 14, "bold"),
        fg="light blue",
        bg="black"
    ).pack(pady=15)

    button_style = {
        "bg": "light blue",
        "fg": "black",
        "font": ("Arial", 11, "bold"),
        "width": 20,
        "height": 2,
        "activebackground": "#add8e6",
        "activeforeground": "black"
    }

    tk.Button(menu, text="Add Report", command=lambda: open_report_selection_window(user), **button_style).pack(pady=6)
    tk.Button(menu, text="View My Reports", command=lambda: view_reports_window(user), **button_style).pack(pady=6)
    tk.Button(menu, text="Get Safe Route", command=open_safe_route_window, **button_style).pack(pady=6)
    tk.Button(menu, text="High Risk Locations", command=open_danger_location_ui, **button_style).pack(pady=6)
    tk.Button(menu, text="Emergency Helplines", command=show_helpline_numbers, **button_style).pack(pady=6)
    
    # Evidence Storage Button
    tk.Button(
        menu,
        text="Evidence Storage",
        command=lambda: [ open_evidence_ui(user, lambda: open_main_menu(user))],
        **button_style
    ).pack(pady=6)

    tk.Button(menu, text="Logout", command=menu.destroy, **button_style).pack(pady=15)