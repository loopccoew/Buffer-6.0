import tkinter as tk
from tkinter import ttk, messagebox

class SubjectNode:
    def __init__(self, name, difficulty):
        self.name = name
        self.difficulty = difficulty  # 1-3 scale (1=easy, 2=medium, 3=hard)
        self.allocated_minutes = 0    # will be calculated
        self.next = None

class StudyPlan:
    def __init__(self):
        self.head = None

    #adds subject to the linked list
    def add_subject(self, name, difficulty):
        new_subject = SubjectNode(name, difficulty)
        
        if not self.head:
            self.head = new_subject
        else:
            current = self.head
            while current.next:
                current = current.next
            current.next = new_subject
        return True

    def calculate_time_allocation(self, total_available_hours):
        total_available_min = round(total_available_hours * 60)
        
        # Calculate total difficulty weight
        total_difficulty = 0
        current = self.head
        while current:
            total_difficulty += current.difficulty
            current = current.next
        
        if total_difficulty == 0:
            return False
        
        # Allocate time based on difficulty percentage
        current = self.head
        while current:
            percentage = current.difficulty / total_difficulty
            current.allocated_minutes = round(percentage * total_available_min)
            current = current.next
        return True

    #displays time in the readable format Ex., 1hours 15 mins
    def format_time(self, minutes):
        """Convert minutes to clean hours and minutes format"""
        hours = minutes // 60
        mins = minutes % 60
        if hours > 0 and mins > 0:
            return f"{hours}h {mins}m"
        elif hours > 0:
            return f"{hours}h"
        else:
            return f"{mins}m"
    
    def get_all_subjects(self):
        subjects = []
        current = self.head
        while current:
            time_formatted = self.format_time(current.allocated_minutes)
            subjects.append({
                'name': current.name,
                'difficulty': self.get_difficulty_name(current.difficulty),
                'time': time_formatted
            })
            current = current.next
        return subjects
    
    def get_difficulty_name(self, difficulty):
        if difficulty == 1:
            return "Easy"
        elif difficulty == 2:
            return "Medium"
        else:
            return "Hard"

class StudyPlanApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Study Plan Generator")
        self.root.geometry("600x500")
        self.plan = StudyPlan()
        
        self.create_widgets()
    
    def create_widgets(self):
        # Main frame
        main_frame = ttk.Frame(self.root, padding="10")
        main_frame.pack(fill=tk.BOTH, expand=True)
        
        # Add Subject Section
        add_frame = ttk.LabelFrame(main_frame, text="Add New Subject", padding="10")
        add_frame.pack(fill=tk.X, pady=5)
        
        ttk.Label(add_frame, text="Subject Name:").grid(row=0, column=0, sticky=tk.W)
        self.subject_name = ttk.Entry(add_frame, width=25)
        self.subject_name.grid(row=0, column=1, padx=5)
        
        ttk.Label(add_frame, text="Difficulty:").grid(row=1, column=0, sticky=tk.W)
        self.difficulty = ttk.Combobox(add_frame, values=["Easy", "Medium", "Hard"], state="readonly")
        self.difficulty.grid(row=1, column=1, padx=5, sticky=tk.W)
        self.difficulty.current(0)
        
        add_btn = ttk.Button(add_frame, text="Add Subject", command=self.add_subject)
        add_btn.grid(row=2, column=0, columnspan=2, pady=5)
        
        # Generate Plan Section
        gen_frame = ttk.LabelFrame(main_frame, text="Generate Plan", padding="10")
        gen_frame.pack(fill=tk.X, pady=5)
        
        ttk.Label(gen_frame, text="Total Available Time (hours):").grid(row=0, column=0, sticky=tk.W)
        self.total_hours = ttk.Entry(gen_frame, width=10)
        self.total_hours.grid(row=0, column=1, padx=5, sticky=tk.W)
        
        gen_btn = ttk.Button(gen_frame, text="Generate Study Plan", command=self.generate_plan)
        gen_btn.grid(row=1, column=0, columnspan=2, pady=5)
        
        # Results Section
        results_frame = ttk.LabelFrame(main_frame, text="Study Plan", padding="10")
        results_frame.pack(fill=tk.BOTH, expand=True, pady=5)
        
        # Treeview for displaying results
        self.tree = ttk.Treeview(results_frame, columns=('Subject', 'Difficulty', 'Time'), show='headings')
        self.tree.heading('Subject', text='Subject')
        self.tree.heading('Difficulty', text='Difficulty')
        self.tree.heading('Time', text='Time')
        self.tree.column('Subject', width=200)
        self.tree.column('Difficulty', width=150)
        self.tree.column('Time', width=150)
        
        scrollbar = ttk.Scrollbar(results_frame, orient=tk.VERTICAL, command=self.tree.yview)
        self.tree.configure(yscroll=scrollbar.set)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        self.tree.pack(fill=tk.BOTH, expand=True)
        
        # Total time label
        self.total_label = ttk.Label(results_frame, text="Total study time: ")
        self.total_label.pack(side=tk.BOTTOM, fill=tk.X)
    
    def add_subject(self):
        name = self.subject_name.get().strip()
        difficulty = self.difficulty.current() + 1  # Convert to 1-3 scale
        
        if not name:
            messagebox.showerror("Error", "Please enter a subject name")
            return
        
        if self.plan.add_subject(name, difficulty):
            messagebox.showinfo("Success", f"Added {name} to study plan")
            self.subject_name.delete(0, tk.END)
        else:
            messagebox.showerror("Error", "Failed to add subject")
    
    def generate_plan(self):
        try:
            hours = float(self.total_hours.get())
            if hours <= 0:
                raise ValueError
        except ValueError:
            messagebox.showerror("Error", "Please enter a valid positive number for hours")
            return
        
        if not self.plan.head:
            messagebox.showerror("Error", "No subjects added to generate plan")
            return
        
        if self.plan.calculate_time_allocation(hours):
            # Clear previous results
            for item in self.tree.get_children():
                self.tree.delete(item)
            
            # Add new results
            total_minutes = 0
            current = self.plan.head
            while current:
                time_formatted = self.plan.format_time(current.allocated_minutes)
                self.tree.insert('', tk.END, values=(current.name, 
                                                    self.plan.get_difficulty_name(current.difficulty), 
                                                    time_formatted))
                total_minutes += current.allocated_minutes
                current = current.next
            
            # Update total label
            self.total_label.config(text=f"Total study time: {self.plan.format_time(total_minutes)}")
        else:
            messagebox.showerror("Error", "Failed to generate study plan")

if __name__ == "__main__":
    root = tk.Tk()
    app = StudyPlanApp(root)
    root.mainloop()
