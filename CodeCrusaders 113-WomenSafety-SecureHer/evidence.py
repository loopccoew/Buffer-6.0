import tkinter as tk
from tkinter import filedialog, messagebox
import os


ENCRYPTED_DIR = "encrypted_evidence"
DECRYPTED_DIR = "decrypted_temp"

os.makedirs(ENCRYPTED_DIR, exist_ok=True)
os.makedirs(DECRYPTED_DIR, exist_ok=True)

def encrypt_file(filepath):
    with open(filepath, 'rb') as f:
        content = f.read()
    encrypted = content[::-1]
    filename = os.path.basename(filepath)
    encrypted_path = os.path.join(ENCRYPTED_DIR, f"{filename}.enc")
    with open(encrypted_path, 'wb') as f:
        f.write(encrypted)
    messagebox.showinfo("Success", f"Encrypted and saved:\n{encrypted_path}")

def decrypt_all_files():
    for file in os.listdir(DECRYPTED_DIR):
        os.remove(os.path.join(DECRYPTED_DIR, file))

    decrypted_files = []
    for enc_file in os.listdir(ENCRYPTED_DIR):
        if enc_file.endswith(".enc"):
            enc_path = os.path.join(ENCRYPTED_DIR, enc_file)
            with open(enc_path, 'rb') as f:
                content = f.read()
            decrypted = content[::-1]
            original_name = enc_file.replace(".enc", "")
            decrypted_path = os.path.join(DECRYPTED_DIR, original_name)
            with open(decrypted_path, 'wb') as f:
                f.write(decrypted)
            decrypted_files.append(decrypted_path)
    return decrypted_files

def open_evidence_ui(user, return_to_main_menu):
    window = tk.Toplevel()
    window.title("Evidence Storage")
    window.geometry("350x450")
    window.configure(bg="black")

    def clear_widgets():
        for widget in window.winfo_children():
            widget.destroy()

    def show_main_menu():
        clear_widgets()
        tk.Label(window, text="🔐 Secure Her", font=("Arial", 16, "bold"), fg="light blue", bg="black").pack(pady=30)
        tk.Button(window, text="Upload Evidence", width=25, height=2, command=upload_evidence, **button_style).pack(pady=10)
        tk.Button(window, text="View Evidence", width=25, height=2, command=view_evidence, **button_style).pack(pady=10)
        tk.Button(window, text="Back", width=25, height=2, command=go_back_to_main_menu, **button_style).pack(pady=30)

    def upload_evidence():
        filepath = filedialog.askopenfilename(title="Select file to encrypt")
        if filepath:
            encrypt_file(filepath)
            # stay on same window after upload

    def view_evidence():
        clear_widgets()
        tk.Label(window, text="📂 Decrypted Evidence", font=("Arial", 14), fg="light blue", bg="black").pack(pady=15)
        decrypted_files = decrypt_all_files()

        if not decrypted_files:
            tk.Label(window, text="No encrypted files found.", fg="white", bg="black").pack(pady=10)
        else:
            for filepath in decrypted_files:
                filename = os.path.basename(filepath)
                tk.Button(window, text=filename, width=40, command=lambda path=filepath: os.startfile(path)).pack(pady=5)

        tk.Button(window, text="⬅ Back", command=show_main_menu, **button_style).pack(pady=20)

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
