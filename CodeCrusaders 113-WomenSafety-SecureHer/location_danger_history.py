# location_danger_history.py

import tkinter as tk
from tkinter import scrolledtext
from db import get_connection


def get_location_history_text():
    connection = get_connection()
    cursor = connection.cursor()

    cursor.execute("""
        SELECT l.name, d.danger_score, d.last_reported
        FROM location_danger d
        JOIN locations l ON d.location_id = l.id
        ORDER BY d.danger_score DESC, d.last_reported DESC
    """)
    result = cursor.fetchall()

    cursor.close()
    connection.close()

    if not result:
        return "⚠️ No danger zones reported yet."
    
    history_text = "📍 High-Risk Locations:\n\n"
    for row in result:
        history_text += f"- {row[0]} | Danger Score: {row[1]} | Last Reported: {row[2]}\n"
    return history_text


def open_danger_location_ui():
    history_text = get_location_history_text()
    window = tk.Toplevel()
    window.title("High Risk Locations")
    window.geometry("600x400")

    text_area = scrolledtext.ScrolledText(window, width=80, height=20)
    text_area.pack(fill=tk.BOTH, expand=True)
    text_area.insert(tk.END, history_text)
    text_area.config(state=tk.DISABLED)
