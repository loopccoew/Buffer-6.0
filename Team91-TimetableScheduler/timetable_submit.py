import random
from prettytable import PrettyTable
from collections import defaultdict

class Subject:
    def __init__(self, code, name, credits, faculties, is_lab=False):
        self.code = code
        self.name = name
        self.credits = credits
        self.faculties = faculties  # List of faculty names
        self.is_lab = is_lab


class Faculty:
    def __init__(self, name):
        self.name = name
        self.assigned_slots = set()  # Global (day, slot) assignments


class Batch:
    def __init__(self, name):
        self.name = name
        self.timetable = [['' for _ in range(8)] for _ in range(5)]  # 5 days, 8 slots per day
        self.theory_count_per_day = [0] * 5  # Track theory lectures per day (Monday to Friday)
        self.lab_schedule = {'CPL': 0, 'PHL': 0, 'EGL': 0}  # Track labs per week
        self.lab_days = set()  # Track days with labs
        self.subject_counts = defaultdict(int)  # Track scheduled lectures per subject


def check_hard_constraints(batch, faculty, day, slot, subject_code, is_lab):
    if (day, slot) in faculty.assigned_slots:
        return False
    if batch.timetable[day][slot] != '':
        return False
    if not is_lab and subject_code in batch.timetable[day]:  # No more than one lecture of the same subject per day
        return False
    return True


def assign_faculty(faculty_names, faculties, day, slot):
    available_faculties = [f for f in faculty_names if (day, slot) not in faculties[f].assigned_slots]
    if available_faculties:
        return faculties[random.choice(available_faculties)]
    return None


def schedule_labs(batch, subjects, faculties, global_lab_slots):
    lab_subjects = [s for s in subjects if s.is_lab]
    random.shuffle(lab_subjects)  # Randomize lab scheduling order
    
    for subject in lab_subjects:
        if subject.code == 'VSEC101L':  # C Programming Lab (2 sessions per week)
            required_sessions = 2
            lab_code = 'CPL'
            duration = 2  # Labs take 2 consecutive slots
        elif subject.code == 'BSC101L':  # Physics Lab (1 session per week)
            required_sessions = 1
            lab_code = 'PHL'
            duration = 2
        elif subject.code == 'ESC101L':  # Engineering Graphics Lab (1 session per week)
            required_sessions = 1
            lab_code = 'EGL'
            duration = 2
        else:
            continue

        while batch.lab_schedule[lab_code] < required_sessions:
            # Try to find a suitable day and slot
            for day in random.sample(range(5), 5):
                if day in batch.lab_days:
                    continue  # Only one lab per day
                
                for slot in random.sample(range(8 - duration + 1), 8 - duration + 1):
                    # Check if slots are available and not in global lab slots
                    slots_available = all(batch.timetable[day][s] == '' for s in range(slot, slot + duration))
                    global_conflict = any((day, s) in global_lab_slots for s in range(slot, slot + duration))
                    
                    if slots_available and not global_conflict:
                        f = assign_faculty(subject.faculties, faculties, day, slot)
                        if f:
                            # Assign the lab
                            for s in range(slot, slot + duration):
                                batch.timetable[day][s] = lab_code
                                f.assigned_slots.add((day, s))
                                global_lab_slots.add((day, s))
                            batch.lab_schedule[lab_code] += 1
                            batch.lab_days.add(day)
                            break
                if batch.lab_schedule[lab_code] >= required_sessions:
                    break


def schedule_theory(batch, subjects, faculties):
    theory_subjects = [s for s in subjects if not s.is_lab]
    random.shuffle(theory_subjects)  # Randomize scheduling order
    
    for subject in theory_subjects:
        required_lectures = subject.credits
        scheduled_lectures = 0
        
        while scheduled_lectures < required_lectures:
            # Try to schedule remaining lectures
            for day in random.sample(range(5), 5):
                if day in batch.lab_days and batch.theory_count_per_day[day] >= 6:
                    continue  # Don't overload days with labs
                    
                for slot in random.sample(range(8), 8):
                    if (batch.timetable[day][slot] == '' and 
                        batch.theory_count_per_day[day] < 7 and  # Max 7 theory lectures per day
                        subject.code not in batch.timetable[day]):
                        
                        f = assign_faculty(subject.faculties, faculties, day, slot)
                        if f:
                            batch.timetable[day][slot] = subject.code
                            f.assigned_slots.add((day, slot))
                            batch.theory_count_per_day[day] += 1
                            scheduled_lectures += 1
                            batch.subject_counts[subject.code] += 1
                            break
                if scheduled_lectures >= required_lectures:
                    break


def ensure_min_lectures_per_day(batch, subjects):
    for day in range(5):
        theory_lectures = sum(1 for slot in batch.timetable[day] 
                            if slot != '' and not any(slot == lab for lab in ['CPL', 'PHL', 'EGL']))
        
        if theory_lectures < 2:  # Need at least 2 theory lectures per day
            # Find available slots and subjects that need more lectures
            available_slots = [s for s in range(8) if batch.timetable[day][s] == '']
            needed_subjects = [s for s in subjects 
                             if not s.is_lab and batch.subject_counts.get(s.code, 0) < s.credits]
            
            for slot in available_slots:
                if theory_lectures >= 2:
                    break
                
                if needed_subjects:
                    # Try to schedule a needed subject
                    for subject in needed_subjects:
                        if subject.code not in batch.timetable[day]:  # No duplicate subjects per day
                            f = assign_faculty(subject.faculties, faculties, day, slot)
                            if f:
                                batch.timetable[day][slot] = subject.code
                                f.assigned_slots.add((day, slot))
                                batch.theory_count_per_day[day] += 1
                                batch.subject_counts[subject.code] += 1
                                theory_lectures += 1
                                break


def display_timetable(batch):
    print(f"\nTimetable for {batch.name}:")
    table = PrettyTable()
    table.field_names = ["Day", "Slot 1", "Slot 2", "Slot 3", "Slot 4", 
                        "Slot 5", "Slot 6", "Slot 7", "Slot 8"]
    days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']
    
    # Mapping from subject codes to short forms
    subject_mapping = {
        'BSC103': 'LAUC',  # Linear Algebra and Univariate Calculus
        'BSC101': 'PHY',   # Physics
        'IKS101': 'IKS',   # Indian Knowledge System
        'ESC101': 'EG',    # Engineering Graphics
        'CC101': 'LL',     # Liberal Learning
        'AEC101': 'PC',    # Professional Communication
        'VSEC101L': 'CPL', # C Programming Lab
        'BSC101L': 'PHL',  # Physics Lab
        'ESC101L': 'EGL'   # Engineering Graphics Lab
    }
    
    for i, row in enumerate(batch.timetable):
        display_row = [subject_mapping.get(slot, slot) for slot in row]
        table.add_row([days[i]] + display_row)
    print(table)


if __name__ == '__main__':
    # Define all faculties (3 per subject)
    faculties = {}
    
    # Create faculties for each subject
    subjects_data = [
        ('PHY', 3, 'Physics'),
        ('LAUC', 3, 'Linear Algebra & Calculus'),
        ('EG', 3, 'Engineering Graphics'),
        ('IKS', 3, 'Indian Knowledge System'),
        ('LL', 3, 'Liberal Learning'),
        ('PC', 3, 'Professional Communication'),
        ('CPL', 3, 'C Programming Lab'),
    ]
    
    for prefix, count, name in subjects_data:
        for i in range(1, count+1):
            faculties[f"{prefix}{i}"] = Faculty(f"{prefix}{i}")

    # Subjects with their requirements
    subjects = [
        Subject('BSC101', 'Physics', 3, ['PHY1', 'PHY2', 'PHY3']),
        Subject('BSC103', 'Linear Algebra & Calculus', 3, ['LAUC1', 'LAUC2', 'LAUC3']),
        Subject('ESC101', 'Engineering Graphics', 2, ['EG1', 'EG2', 'EG3']),
        Subject('IKS101', 'Indian Knowledge System', 2, ['IKS1', 'IKS2', 'IKS3']),
        Subject('CC101', 'Liberal Learning', 1, ['LL1', 'LL2', 'LL3']),
        Subject('AEC101', 'Professional Communication', 2, ['PC1', 'PC2', 'PC3']),
        Subject('BSC101L', 'Physics Lab', 1, ['PHY1', 'PHY2', 'PHY3'], is_lab=True),
        Subject('ESC101L', 'Graphics Lab', 1, ['EG1', 'EG2', 'EG3'], is_lab=True),
        Subject('VSEC101L', 'C Programming Lab', 2, ['CPL1', 'CPL2', 'CPL3'], is_lab=True)
    ]

    # Create 5 divisions
    divisions = [Batch(f"Div-{chr(65+i)}") for i in range(5)]

    # Shared lab slot tracker
    global_lab_slots = set()

    # Schedule for each division
    for division in divisions:
        # Reset counts for each division
        division.subject_counts.clear()
        
        # Schedule labs first
        schedule_labs(division, subjects, faculties, global_lab_slots)
        
        # Then schedule theory subjects
        schedule_theory(division, subjects, faculties)
        
        # Ensure minimum lectures per day
        ensure_min_lectures_per_day(division, subjects)
        
        # Display timetable
        display_timetable(division)
