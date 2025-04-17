# 🧠 Unified Scheduling System

A Java-based simulation project that models real-world scheduling problems using CPU scheduling algorithms and custom data structures. This system includes modules like Cloud Job Scheduling, Ride Sharing, Sports Match Scheduling, Hospital Queue, Restaurant Orders, and Call Center Queue Management.

---

## 🎯 Objective

To demonstrate how core CPU scheduling algorithms and custom data structures can solve real-life scheduling and queuing problems efficiently.

---

## 🧩 Modules Overview

| Module                  | Real-World System          | Algorithm Used         | Custom Data Structure Used                     |
|------------------------|----------------------------|------------------------|-------------------------------------------------|
| CloudJobScheduler       | Cloud Job Management       | Shortest Job First (SJF) | Custom PriorityQueue, LinkedHashMap           |
| RideSharingSystem       | Ride Allocation            | First Come First Serve (FCFS) | Custom Queue, LinkedList               |
| SportsMatchScheduler    | Match Scheduling           | Round Robin             | Custom Circular List/Queue                     |
| HospitalQueueSystem     | ER Queue                   | Priority Scheduling      | Custom PriorityQueue (by severity & arrival)   |
| RestaurantOrderSystem   | Kitchen Order Prep         | Shortest Job First / Priority | Custom Sorted Queue or PriorityQueue     |
| CallCenterSystem        | Agent Assignment           | Multilevel Queue (MLQ)   | Multiple Custom Queues per skill/priority      |

---

# 🎥 Demo Focus – Hospital & Ride Sharing Systems

This demo highlights how custom data structures and CPU scheduling algorithms are applied in:

- 🏥 Hospital Queue System
- 🚕 Ride Sharing System

---

## 🏥 Hospital Queue System

### 🎯 Objective
Simulate an Emergency Room (ER) triage system where patients are treated based on severity and arrival order.

### ⚙️ Algorithm Used
**Priority Scheduling**  
Patients with higher severity (10 = most severe) are treated first. If severity is equal, the one who arrived earlier is attended first.

### 🧱 Custom Data Structure
- ✅ Custom PriorityQueue (based on severity and arrival order)
- Implemented using Java's PriorityQueue but with a custom comparator.
- Tracks patient arrival with a unique counter.

### 📌 Features & Edge Cases
- Add patient with name, symptom, severity (1–10).
- Error handling for invalid input (empty name, invalid severity).
- Patients are attended in priority order.

---

## 🚕 Ride Sharing System

### 🎯 Objective
Simulate a basic ride booking system where passengers are assigned to available drivers in the order they arrive.

### ⚙️ Algorithm Used
**First Come First Serve (FCFS)**  
Passengers are served in the order they request a ride. No prioritization.

### 🧱 Custom Data Structure
- ✅ Custom Queue
- Implemented using LinkedList or a custom queue class.
- Maintains order of arrival and matches riders to available drivers.

### 📌 Features & Edge Cases
- Add riders and drivers.
- Assign drivers based on arrival order.
- Cancel ride option included.
- Handles case when no drivers are available or duplicate IDs are entered.

---

## 🤖 Why Custom Data Structures?

- To deeply understand internal behavior of standard scheduling.
- To simulate low-level system operations.
- To customize prioritization and queuing logic specific to real-world needs.

---


