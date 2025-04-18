This Python program is a timetable scheduler designed to automatically generate weekly timetables for multiple divisions (batches) of students in an educational institution. The scheduler must:

1. Allocate Subjects & Teachers:

Schedule both theory classes (e.g., Physics, Linear Algebra) and lab sessions (e.g., C Programming Lab, Physics Lab).
Assign teachers to each class while ensuring no teacher is double-booked in the same time slot.

2. Respect Constraints:

Each division must have at least 2 theory lectures per day.
No more than 7 theory lectures per day (since labs take 2 slots).
Labs must be scheduled in 2 consecutive slots and only once per day per division.
No duplicate subject lectures on the same day for a division.

3. Teacher Management:

Each subject has 3 available teachers (e.g., PHY1, PHY2, PHY3 for Physics).
Teachers cannot be assigned to two classes at the same time across divisions.

4. Lab Requirements:

C Programming Lab (CPL): 2 sessions per week.
Physics Lab (PHL): 1 session per week.
Engineering Graphics Lab (EGL): 1 session per week.

5. Technologies & Concepts Used:
   
i) Object-Oriented Programming (OOP):

Classes (Subject, Faculty, Batch) to model real-world entities.
Methods to handle scheduling logic (schedule_labs, schedule_theory).

ii) Data Structures:

Lists for timetables and faculty assignments.
Dictionaries (defaultdict) to track subject counts and lab schedules.
Sets to avoid teacher slot conflicts (global_lab_slots, assigned_slots).

iii) Randomization:

random.shuffle() to distribute classes evenly across days and slots.
PrettyTable

Used to display timetables in a clean, tabular format.

Constraints Handling:

Hard constraints (no teacher overlaps, max lectures per day) are strictly enforced.

Soft constraints (min lectures per day) are adjusted post-scheduling.
