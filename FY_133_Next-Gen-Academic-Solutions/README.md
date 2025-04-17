# Team Byte Knights

## Automated Timetable Generator (Java)

This project is a Java-based solution for generating a weekly lecture timetable for a classroom. It automatically schedules subjects, manages faculty assignments, and respects important constraints like breaks and classroom availability. The system reduces the need for manual planning and helps avoid scheduling conflicts.

---

##  Introduction

Creating an efficient and conflict-free timetable is a challenging task for educational institutions. It involves careful coordination of class schedules, faculty availability, and classroom resources while adhering to various constraints. Traditional manual methods are time-consuming and often lead to scheduling conflicts, underutilization of resources, and dissatisfaction among stakeholders.

This project aims to address these challenges by developing an **Automated Timetable Generator** using Java. The system uses algorithmic logic to allocate lectures across the week while considering critical constraints like classroom availability, faculty workloads, and predefined breaks.

---

##  Project Description

This Java program generates an automated weekly lecture timetable for a classroom, considering multiple scheduling constraints. It ensures optimal allocation of subjects, faculty, and time slots while avoiding conflicts.

###  Key Features

- **Weekly Timetable Generation:**  
  Automatically creates a timetable for Monday to Friday with 6 time slots each day.

- **Fixed Break Handling:**  
  The third slot (11 AM – 12 PM) is reserved as a fixed break and is not used for scheduling lectures.

- **No Repetition of Subjects per Day:**  
  Each subject is scheduled only once per day to maintain variety.

- **Faculty Workload Management:**  
  Faculty are assigned lectures based on their max allowed count and are not overbooked.

- **Classroom Availability Tracking:**  
  Tracks and displays free/occupied slots across the week.

- **Randomized but Constrained Scheduling:**  
  Uses randomness to place lectures, while respecting all constraints.

---

##  Input Parameters

- **Courses:**  
  A list of subjects to schedule, with the number of weekly lectures each requires.

- **Faculty List:**  
  Each faculty member is linked to a subject and has a limit on how many lectures they can take.

- **Classroom:**  
  One classroom (hardcoded as "Classroom 25") is considered with availability across time slots.

- **Time Slots:**  
  Monday to Friday, with 6 slots each day (including a break from 11 AM to 12 PM).

---

##  Constraints Handled

-  One lecture per subject per day  
-  Faculty not scheduled beyond their max limit  
-  Break time (11–12 PM) is always free  
-  Classroom slot availability is respected  
-  Lecture assignments are randomized but validated

—

## Conclusion

The development of the Timetable Generator aims to streamline the scheduling process in educational institutions, reduce administrative workload, and improve the overall educational experience for students and faculty. By addressing the complexities of timetable generation through automation, this project seeks to provide a valuable tool for educational administrators.
