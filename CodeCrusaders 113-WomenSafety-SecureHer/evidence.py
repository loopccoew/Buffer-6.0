import tkinter as tk
from tkinter import filedialog, messagebox
import os

# Directory for storing encrypted files
ENCRYPTED_DIR = "encrypted_evidence"
os.makedirs(ENCRYPTED_DIR, exist_ok=True)

def encrypt_file(filepath):
    """Encrypt the file by reversing its content and save it."""
    with open(filepath, 'rb') as f:
        content = f.read()
    encrypted = content[::-1]
    filename = os.path.basename(filepath)
    encrypted_path = os.path.join(ENCRYPTED_DIR, f"{filename}.enc")
    with open(encrypted_path, 'wb') as f:
        f.write(encrypted)
    messagebox.showinfo("Success", f"Encrypted and saved:\n{encrypted_path}")

def open_evidence_ui(user, return_to_main_menu):
    """Opens the UI for uploading evidence only."""
    window = tk.Toplevel()
    window.title("Evidence Storage")
    window.geometry("350x400")
    window.configure(bg="black")

    def clear_widgets():
        for widget in window.winfo_children():
            widget.destroy()

    def show_main_menu():
        clear_widgets()
        tk.Label(window, text="🔐 Secure Her", font=("Arial", 16, "bold"), fg="light blue", bg="black").pack(pady=50)
        tk.Button(window, text="Upload Evidence", width=25, height=2, command=upload_evidence, **button_style).pack(pady=30)
        tk.Button(window, text="Back", width=25, height=2, command=go_back_to_main_menu, **button_style).pack(pady=30)

    def upload_evidence():
        filepath = filedialog.askopenfilename(title="Select file to encrypt")
        if filepath:
            encrypt_file(filepath)
            # Stay on the same window after upload

    def go_back_to_main_menu():
        window.destroy()
        return_to_main_menu()  # This returns to the main menu

    button_style = {
        "bg": "light blue",
        "fg": "black",
        "font": ("Arial", 11, "bold"),
        "activebackground": "#add8e6",
        "activeforeground": "black"
    }

    show_main_menu()
