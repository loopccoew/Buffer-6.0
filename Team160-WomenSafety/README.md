# Women's Safety Application

## 🚨 Problem Statement

In today’s world, personal safety—especially for women—remains a major concern, often compromised by delayed emergency responses, unclear or unsafe routes, and difficulty accessing timely legal support. Many existing safety solutions fall short; they lack real-time risk alerts, predictive analysis, and trustworthy options for reporting crimes anonymously. This gap leaves individuals feeling vulnerable and unsupported in moments when swift action is critical.

There is a growing need for a reliable, all-in-one platform that not only identifies safer routes using real-time data but also enables quick access to emergency contacts, highlights crime-prone areas, facilitates secure and anonymous crime reporting, and connects users to legal aid when they need it the most. Such a solution could empower users to make faster, more informed decisions and allow authorities to respond more efficiently—ultimately creating safer communities for everyone.

---

## 🎯 Project Goals

The platform is designed to enhance women's safety by:
- ✅ Providing access to legal support  
- ✅ Finding the safest routes across Pune based on time and weather  
- ✅ Ensuring emergency preparedness with contacts  
- ✅ Offering login functionality for users and lawyers  

---

## 🌟 Key Features

### 👤 User & Lawyer Login
- Users can register with emergency contacts and email.
- Lawyers can register with specialization and contact info.

### 🧑‍⚖ Lawyer Directory
- View all available lawyers.
- Search lawyers by specialization (e.g., rape, domestic violence).
- Add the information for interested lawyers in your profile for future contact.

### 🛣 Safest Route Finder
- Route suggestions based on:
  - Day or Night travel
  - Weather (rainy conditions)
- Safety score, estimated time, and recommended path shown.
- Powered by a CSV-based route dataset (`safety_data_updated.csv`).

### 🚨 Emergency Alerts
- Instantly alert registered emergency contacts via SMS or email.

---

## 🛠 Tech Stack

- **Programming Language:** Java (JDK 8)

---

## 📊 Data Structures and Algorithms Used

### 🗺 Graph (HashMap)
- **Use:** To model city routes between areas.
- **Why:** Nodes = areas, Edges = routes with safety & time scores.

### 🔍 Dijkstra’s Algorithm
- **Use:** To find the safest and fastest route.
- **Why:** Optimal path selection using safety + time as weights.

### ⏳ PriorityQueue
- **Use:** Selects next best node (route) in Dijkstra’s.
- **Why:** Efficient node traversal based on total score.

### 📚 Array & ArrayList
- **Use:**
  - Array: Emergency contacts (fixed size).
  - ArrayList: Lawyers & route paths (dynamic lists).
- **Why:** Arrays are fast, ArrayLists are flexible.

### 🧭 HashMap
- **Use:** Store graph data and lookup locations.
- **Why:** Fast O(1) access to area connections.

---

## 📁 Dataset

- `safety_data_updated.csv` – Contains route safety and time data for path calculation.

---

## 🚀 Getting Started

1. Clone the repository  
   ```bash
   git clone https://github.com/your-username/womens-safety-app.git
   cd womens-safety-app
