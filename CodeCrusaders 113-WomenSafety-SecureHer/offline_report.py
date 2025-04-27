import tkinter as tk
from tkinter import messagebox
import mysql.connector

# === Predefined List of Locations ===
LOCATIONS = [
    "Alankar Police Station", "Bharti Vidyapeeth More Vidyalaya", "City Pride Kothrud",
    "Cummins College", "Dashabhuja Ganpati", "Deccan Bus Stop", "Eklavya College", "Erandwane",
    "Hingane Home Colony", "Ideal Colony", "Jog High School", "Karishma Society",
    "Karve Nagar Bus Stop", "Karve Nagar Garden", "Karvenagar Chowk", "Kothrud Depot",
    "MIT College Gate", "MITCON Institute", "Mahesh Vidyalaya", "More Vidyalaya Karvenagar",
    "Nal Stop", "Parvati Gaon", "Paud Phata", "Rajaram Bridge", "S.M. Joshi School",
    "SNDT College", "Sarasbaug", "Vanaz Depot", "Vitthalwadi", "Warje Bridge"
]

def open_offline_report_window(user):
    window = tk.Toplevel()
    window.title("SecureHer - Offline Report")
    window.geometry("600x500")
    window.configure(bg="black")

    # === Animated Heading ===
    heading = tk.Label(window, text="SecureHer", font=("Helvetica", 20, "bold"), fg="skyblue", bg="black")
    heading.pack(pady=(30, 10))

    colors = ["deepskyblue", "cyan", "aqua", "lightblue"]
    color_index = [0]  # Using list to make index mutable inside function

    # Function to animate the heading color
    def animate_heading():
        heading.config(fg=colors[color_index[0]])
        color_index[0] = (color_index[0] + 1) % len(colors)
        window.after(500, animate_heading)

    animate_heading()

    # === Subheading ===
    subheading = tk.Label(window, text="Offline Harassment Report", font=("Helvetica", 12), fg="white", bg="black")
    subheading.pack(pady=(0, 30))

    # === Common Label Style ===
    label_style = {"font": ("Helvetica", 12), "fg": "white", "bg": "black"}

    # === Form Fields ===
    # Category selection
    tk.Label(window, text="Category:", **label_style).pack()
    category_var = tk.StringVar()
    category_dropdown = tk.OptionMenu(window, category_var, "Harassment", "Theft", "Assault", "Suspicious Activity", "Other")
    category_dropdown.config(font=("Helvetica", 12))
    category_dropdown.pack(pady=5)

    # Description input
    tk.Label(window, text="Description:", **label_style).pack()
    description_entry = tk.Text(window, height=4, width=50, font=("Helvetica", 11))
    description_entry.pack(pady=5)

    # Location selection
    tk.Label(window, text="Location:", **label_style).pack()
    location_var = tk.StringVar()
    location_dropdown = tk.OptionMenu(window, location_var, *LOCATIONS)
    location_dropdown.config(font=("Helvetica", 12))
    location_dropdown.pack(pady=5)

    # === Button Style ===
    btn_style = {"bg": "lightblue", "fg": "black", "width": 20, "height": 2, "font": ("Helvetica", 12, "bold")}

    # === Submit Report Function ===
    def submit_report():
        category = category_var.get()
        description = description_entry.get("1.0", tk.END).strip()
        location_name = location_var.get()

        # Check if all fields are filled
        if not category or not description or not location_name:
            messagebox.showerror("Error", "Please fill in all fields")
            return

        try:
            # Connect to database
            conn = mysql.connector.connect(
                host="localhost",
                user="root",
                password="root",
                database="secureher"
            )
            cursor = conn.cursor()

            # Find location_id corresponding to the selected location
            cursor.execute("SELECT id FROM locations WHERE name = %s", (location_name,))
            result = cursor.fetchone()
            if not result:
                messagebox.showerror("Error", "Location not found in database.")
                return
            location_id = result[0]

            # Insert the offline report into the database
            cursor.execute(
                "INSERT INTO offline_reports (name, contact, category, description, location_id, user_id) VALUES (%s, %s, %s, %s, %s, %s)",
                (user[1], user[2], category, description, location_id, user[0])
            )

            # Update location danger score, or insert if not present
            cursor.execute("""
                INSERT INTO location_danger (location_id, danger_score)
                VALUES (%s, 2)
                ON DUPLICATE KEY UPDATE
                danger_score = danger_score + 1,
                last_reported = CURRENT_TIMESTAMP
            """, (location_id,))

            conn.commit()  # Save changes
            conn.close()
            messagebox.showinfo("Success", "Report submitted successfully!")

        except mysql.connector.Error as err:
            messagebox.showerror("Database Error", f"Error: {err}")

    # === Buttons for Submit and Close ===
    tk.Button(window, text="Submit Report", command=submit_report, **btn_style).pack(pady=20)
    tk.Button(window, text="Close", command=window.destroy, **btn_style).pack()
