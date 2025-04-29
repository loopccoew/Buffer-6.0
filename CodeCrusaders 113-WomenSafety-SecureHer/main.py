import tkinter as tk
from auth import signup, login # open signup, login options on main page
from admin import admin_login 

def main():
    root = tk.Tk() # creating main application window (a blank canvas)
    root.title("SecureHer - Women Safety App") #title of root(Main) window
    root.geometry("500x400") #(width x height of window in pixels)
    root.configure(bg="black")

    # Animation logic for heading
    colors = ["skyblue", "deepskyblue", "lightblue", "cyan", "aqua"]
    color_index = [0]  # Using list to make it mutable in nested function

    def animate_heading(): # method to have animation for heading
        heading.config(fg=colors[color_index[0]]) # font color 
        color_index[0] = (color_index[0] + 1) % len(colors)
        root.after(500, animate_heading) #vcolor changes after every 1/2 second i.e 500 mili seconds

    # Heading
    
    heading = tk.Label(root, text="SecureHer", font=("Helvetica", 20, "bold"), fg="skyblue", bg="black")
    heading.pack(pady=(30, 10)) # y-axis padding
    animate_heading()  # Start animation

    # Subheading
    subheading = tk.Label(root, text="Empowering Safety for Every Woman", font=("Helvetica", 12), fg="white", bg="black")
    subheading.pack(pady=(0, 30))

    # Button style (Dictionary)
    btn_style = {"bg": "lightblue", "fg": "black", "width": 25, "height": 2, "font": ("Helvetica", 12, "bold")}

    tk.Button(root, text="Signup", command=signup, **btn_style).pack(pady=8) # call signup method by clicking this button
    tk.Button(root, text="Login", command=login, **btn_style).pack(pady=8) # call login method by clicking this button
    tk.Button(root, text="Admin Login", command=admin_login, **btn_style).pack(pady=8) # call admin_login method by clicking this button
    tk.Button(root, text="Exit", command=root.quit, **btn_style).pack(pady=8) # exit main page by clicking this button

    root.mainloop() #event loop, waits for users clicks

if __name__ == "__main__":
    main()
