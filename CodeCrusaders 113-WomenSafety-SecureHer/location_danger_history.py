# location_danger_history.py

import tkinter as tk
from tkinter import scrolledtext
from db import get_connection  # Importing function to connect to the database


def get_location_history_text():
    """
    Fetches and formats the history of high-risk (dangerous) locations 
    from the database, ordered by danger score and last reported time.
    Returns a formatted string.
    """
    connection = get_connection()  # Establish database connection
    cursor = connection.cursor()

    # Query to fetch location names, danger scores, and last reported timestamps
    cursor.execute("""
        SELECT l.name, d.danger_score, d.last_reported
        FROM location_danger d
        JOIN locations l ON d.location_id = l.id
        ORDER BY d.danger_score DESC, d.last_reported DESC
    """)
    result = cursor.fetchall()  # Fetch all results

    cursor.close()
    connection.close()  # Always close the cursor and connection after use

    if not result:
        return "⚠️ No danger zones reported yet."  # Return message if no data

    # Format the fetched data into readable text
    history_text = "📍 High-Risk Locations:\n\n"
    for row in result:
        history_text += f"- {row[0]} | Danger Score: {row[1]} | Last Reported: {row[2]}\n"
    return history_text


def open_danger_location_ui():
    """
    Creates and opens a new Tkinter window to display
    the list of high-risk locations using a scrollable text area.
    """
    history_text = get_location_history_text()  # Get the danger history text

    window = tk.Toplevel()  # Create a new pop-up window (child window)
    window.title("High Risk Locations")
    window.geometry("600x400")  # Set the window size

    # Create a scrollable text area
    text_area = scrolledtext.ScrolledText(window, width=80, height=20)
    text_area.pack(fill=tk.BOTH, expand=True)  # Make it expand and fill the window
    text_area.insert(tk.END, history_text)  # Insert the fetched text into the text area
    text_area.config(state=tk.DISABLED)  # Disable editing inside the text area
