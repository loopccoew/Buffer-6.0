import tkinter as tk
from tkinter import messagebox
import mysql.connector
import re

def open_online_report_window(user):
    window = tk.Toplevel()
    window.title("SecureHer - Online Report")
    window.geometry("600x550")
    window.configure(bg="black")

    # === Animated Heading ===
    heading = tk.Label(window, text="SecureHer", font=("Helvetica", 20, "bold"), fg="skyblue", bg="black")
    heading.pack(pady=(30, 10))

    colors = ["deepskyblue", "cyan", "aqua", "lightblue"]
    color_index = [0]

    def animate_heading():
        heading.config(fg=colors[color_index[0]])
        color_index[0] = (color_index[0] + 1) % len(colors)
        window.after(500, animate_heading)

    animate_heading()

    subheading = tk.Label(window, text="Online Harassment Report", font=("Helvetica", 12), fg="white", bg="black")
    subheading.pack(pady=(0, 30))

    label_style = {"font": ("Helvetica", 12), "fg": "white", "bg": "black"}

    # === Form Fields ===
    tk.Label(window, text="Description", **label_style).pack()
    description_entry = tk.Text(window, height=4, width=50, font=("Helvetica", 11))
    description_entry.pack(pady=5)

    tk.Label(window, text="Platform", **label_style).pack()
    platform_entry = tk.Entry(window, width=50, font=("Helvetica", 11))
    platform_entry.pack(pady=5)

    tk.Label(window, text="Post URL", **label_style).pack()
    url_entry = tk.Entry(window, width=50, font=("Helvetica", 11))
    url_entry.pack(pady=5)

    tk.Label(window, text="Screenshot URL/Path", **label_style).pack()
    screenshot_entry = tk.Entry(window, width=50, font=("Helvetica", 11))
    screenshot_entry.pack(pady=5)

    tk.Label(window, text="Category", **label_style).pack()
    category_var = tk.StringVar(window)
    categories = ["Harassment", "Assault", "Theft", "Other"]
    category_menu = tk.OptionMenu(window, category_var, *categories)
    category_menu.config(font=("Helvetica", 12))
    category_menu.pack(pady=5)
    category_var.set("Harassment")

    # === Other Category Option ===
    other_category_label = tk.Label(window, text="If Other, specify:", **label_style)
    other_category_entry = tk.Entry(window, width=50, font=("Helvetica", 11))

    def toggle_other(*args):
        if category_var.get() == "Other":
            other_category_label.pack()
            other_category_entry.pack(pady=5)
        else:
            other_category_label.pack_forget()
            other_category_entry.pack_forget()

    category_var.trace_add("write", toggle_other)

    # === Button Style ===
    btn_style = {"bg": "lightblue", "fg": "black", "width": 20, "height": 2, "font": ("Helvetica", 12, "bold")}

    # === Submit Logic ===
    def submit_report():
        description = description_entry.get("1.0", tk.END).strip()
        platform = platform_entry.get().strip()
        url = url_entry.get().strip()
        screenshot = screenshot_entry.get().strip()
        category = category_var.get()
        if category == "Other":
            category = other_category_entry.get().strip()

        if not all([description, platform, url, screenshot, category]):
            messagebox.showwarning("Input Error", "Please fill out all fields.")
            return

        url_pattern = r"^(http|https)://[a-zA-Z0-9-_.]+(\.[a-zA-Z]{2,})+.*$"
        if not re.match(url_pattern, url):
            messagebox.showerror("Invalid URL", "Please enter a valid URL starting with http:// or https://.")
            return
        if not re.match(url_pattern, screenshot):
            messagebox.showerror("Invalid Screenshot URL", "Please enter a valid screenshot URL.")
            return

        try:
            conn = mysql.connector.connect(
                host="localhost",
                user="root",
                password="root",
                database="secureher"
            )
            cursor = conn.cursor()
            cursor.execute("""
                INSERT INTO online_reports (user_id, name, contact, description, platform, url, screenshot, category)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            """, (user[0], user[1], user[2], description, platform, url, screenshot, category))
            conn.commit()
            conn.close()
            messagebox.showinfo("Success", "Online report submitted successfully.")
           
        except Exception as e:
            messagebox.showerror("Database Error", f"Error submitting report: {str(e)}")

    tk.Button(window, text="Submit Report", command=submit_report, **btn_style).pack(pady=20)
    tk.Button(window, text="Close", command=window.destroy, **btn_style).pack()
