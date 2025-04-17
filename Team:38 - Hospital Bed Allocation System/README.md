**Problem Statement**
During emergencies, hospitals struggle to allocate beds efficiently, leading to delays in critical patient care. A system is needed that assigns hospital beds to patients based on priority and hospital availability while considering geographical distance and required medical specialty.

**Solution Overview**

This system utilizes:

✅ Max-Heap → Patient Prioritization
A binary heap to ensure the most critical patients receive beds first.

✅ Graph → Hospital Network & Bed Availability
Nodes: Hospitals (including their location, specialties, and available beds).
Edges: Roads connecting hospitals (weighted by distance/time).

Graph Algorithms:
Dijkstra’s Algorithm: Finds the nearest hospital with the required specialty and available beds.
BFS/DFS: Can be used for alternative search methods.





**Key Components & Features**

**1. Custom Data Structures**
A. Max-Heap (Patient Priority Queue)
A binary heap where patients are prioritized based on:

1. Severity of condition (Critical > Serious > Mild).
2. Age (Elderly & infants may get higher priority).
3. Distance from the nearest suitable hospital.
4. Arrival time (FIFO for patients with the same priority level).
Heap Operations:
insert(patient): Adds a patient to the priority queue.
extractMax(): Removes and assigns the highest-priority patient.
increasePriority(patient, newPriority): Updates priority if the patient’s condition worsens.


B. Graph (Hospital Network & Bed Availability)
Hospitals as Nodes, roads as weighted edges (distance/time-based).
Attributes of each Hospital:
Location (latitude, longitude).
Available beds count.
Specialties offered (e.g., physiotherapy, dermatology, eye care, etc.).

Graph Operations:
addHospital(hospitalID, location, availableBeds, specialties).
updateBedAvailability(hospitalID, beds).
findNearestHospital(patientLocation, requiredSpecialty).



**2. Functional Features**

✅ Real-time Bed Availability Tracking
Each hospital updates its available beds dynamically.

✅ Specialty-Based Search
Patients can search for hospitals based on required specialties.

✅ Efficient Hospital Search
Uses Dijkstra’s algorithm to locate the nearest hospital with beds in the required specialty.


✅ Dynamic Bed Allocation & Reallocation
If a patient is assigned a bed but an emergency occurs, reallocation can happen.

✅ User Dashboard for Monitoring
Admin panel to check patient queue, hospital capacity, and live updates.

**Video Link**
https://drive.google.com/file/d/1zrLJwyYXoyTbkfgp1203wplsZfm9NU3R/view?usp=sharing
