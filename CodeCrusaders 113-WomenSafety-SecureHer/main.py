import tkinter as tk
from auth import signup, login
from admin import admin_login

def main():
    root = tk.Tk()
    root.title("SecureHer - Women Safety App")
    root.geometry("500x400")
    root.configure(bg="black")

    # Animation logic for heading
    colors = ["skyblue", "deepskyblue", "lightblue", "cyan", "aqua"]
    color_index = [0]  # Using list to make it mutable in nested function

    def animate_heading():
        heading.config(fg=colors[color_index[0]])
        color_index[0] = (color_index[0] + 1) % len(colors)
        root.after(500, animate_heading)

    # Heading
    heading = tk.Label(root, text="SecureHer", font=("Helvetica", 20, "bold"), fg="skyblue", bg="black")
    heading.pack(pady=(30, 10))
    animate_heading()  # Start animation

    # Subheading
    subheading = tk.Label(root, text="Empowering Safety for Every Woman", font=("Helvetica", 12), fg="white", bg="black")
    subheading.pack(pady=(0, 30))

    # Button style
    btn_style = {"bg": "lightblue", "fg": "black", "width": 25, "height": 2, "font": ("Helvetica", 12, "bold")}

    tk.Button(root, text="Signup", command=signup, **btn_style).pack(pady=8)
    tk.Button(root, text="Login", command=login, **btn_style).pack(pady=8)
    tk.Button(root, text="Admin Login", command=admin_login, **btn_style).pack(pady=8)
    tk.Button(root, text="Exit", command=root.quit, **btn_style).pack(pady=8)

    root.mainloop()

if __name__ == "__main__":
    main()
