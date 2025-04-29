import tkinter as tk
from tkinter import filedialog, messagebox # Allows to open file picker dialogs, Allows pop-up messages for success/error notifications.
import os # Used for file and directory operations.

ENCRYPTED_DIR = "encrypted_evidence"# Directory for storing encrypted files
os.makedirs(ENCRYPTED_DIR, exist_ok=True) # creates the folder only if it doesn't already exist

def encrypt_file(filepath): # filepath of The original file selected by the user.
    """Encrypt the file by reversing its content and save it."""
    with open(filepath, 'rb') as f: # Reads the file in binary mode 
        content = f.read()
    encrypted = content[::-1] # Reverses the binary data
    filename = os.path.basename(filepath)
    encrypted_path = os.path.join(ENCRYPTED_DIR, f"{filename}.enc")#Stores the file with a .enc extension in encrypted_evidence/.
    with open(encrypted_path, 'wb') as f:
        f.write(encrypted) 
    messagebox.showinfo("Success", f"Encrypted and saved:\n{encrypted_path}")#Shows a success pop-up via messagebox.showinfo.

def open_evidence_ui(user, return_to_main_menu): # Opens the UI for uploading and viewing evidence.
    
    window = tk.Toplevel()
    window.title("Evidence Storage")
    window.geometry("350x500")
    window.configure(bg="black")

    def clear_widgets():#Deletes all elements/widgets inside the current window.
        #Used when navigating between views (like switching to view mode from upload mode).
        for widget in window.winfo_children():
            widget.destroy()

    def show_main_menu():
        clear_widgets() # Clears current widgets (in case of switching back).
        #Each button calls a respective function on click.
        tk.Label(window, text="🔐 Secure Her", font=("Arial", 16, "bold"), fg="light blue", bg="black").pack(pady=30)
        tk.Button(window, text="Upload Evidence", width=25, height=2, command=upload_evidence, **button_style).pack(pady=20)
        tk.Button(window, text="View Evidence", width=25, height=2, command=view_evidence, **button_style).pack(pady=20)
        tk.Button(window, text="Back", width=25, height=2, command=go_back_to_main_menu, **button_style).pack(pady=20)

    def upload_evidence():
        filepath = filedialog.askopenfilename(title="Select file to encrypt")#Opens a file browser.
        if filepath: # If a file is selected, calls encrypt_file() to process it.
            encrypt_file(filepath)

    def view_evidence():
        clear_widgets()
        tk.Label(window, text="🗂 Uploaded Evidences", font=("Arial", 16, "bold"), fg="light blue", bg="black").pack(pady=20) 
        # Lists all files in the encrypted directory using os.listdir().
        files = os.listdir(ENCRYPTED_DIR)
        if not files:
            tk.Label(window, text="No evidence found.", font=("Arial", 12), fg="white", bg="black").pack(pady=10)#Displays "No evidence found" if empty.
        else:
            for filename in files:
                tk.Label(window, text=filename, font=("Arial", 10), fg="white", bg="black").pack(pady=2)#Displays filenames as labels if files exist.

        tk.Button(window, text="Back to Menu", width=25, height=2, command=show_main_menu, **button_style).pack(pady=30)#"Back to Menu" button reloads the main menu.

    def go_back_to_main_menu():
        window.destroy()#Closes the evidence window.
        return_to_main_menu()#Calls the return_to_main_menu() function (likely reopens the main app menu).

    button_style = { # All buttons will have consistent style (same color, font, etc.).


        "bg": "light blue",
        "fg": "black",
        "font": ("Arial", 11, "bold"),
        "activebackground": "#add8e6",
        "activeforeground": "black"
    }
#This is the initial view shown when the evidence window opens.
#Upload/View/Back buttons are available from the start.
    show_main_menu()
