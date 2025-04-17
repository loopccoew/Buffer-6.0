# 📱 WishWallet – A Goal-Based Digital Piggy Bank

**WishWallet** is a smart savings Android application designed to help users set financial goals and automatically allocate their saved money toward those goals using classic DSA concepts like Priority Queue, HashMap, and a Greedy Algorithm.

---

## 🚀 Project Overview

WishWallet allows users to:

- Create multiple financial goals (e.g., Buy a phone, Trip to Manali)
- Assign a **target amount** and **priority** to each goal
- Add money to a digital piggy bank
- Automatically distribute money to goals using a **priority-based greedy approach**
- Track progress via a **visual progress bar**
- Remove completed goals from the dashboard with a success message

---

## 📚 DSA Concepts Used

| Concept         | Implementation Purpose |
|----------------|-------------------------|
| **HashMap**     | For storing and accessing goals using goal names |
| **PriorityQueue** | For automatically sorting goals by priority (lower number = higher priority) |
| **Greedy Algorithm** | For distributing added money efficiently among goals |
| **Custom Comparator** | Used to sort goals by priority and remaining amount |

---

## 🛠️ Features

- 🎯 Add financial goals with name, amount, and priority
- 💰 Add money to the piggy bank anytime
- 📊 View all current goals with dynamic progress bars
- ✅ Goals disappear automatically once completed, with a Toast congratulation message
- 📱 Clean, simple Android UI with intuitive buttons

---

## 💡 How It Works (Logic Breakdown)

1. When a user adds money, all goals are sorted using a **PriorityQueue**.
2. The app uses a **Greedy Allocation Algorithm** to:
   - Start with the **highest priority goal**
   - Allocate as much money as needed
   - Move to the next goal once the current one is filled
3. Internally, goal progress is tracked using the `savedAmount` vs `targetAmount`.
4. Completed goals are removed.

---

🖥️ Screenshots
Includes screenshots of:

Home screen
![WhatsApp Image 2025-04-17 at 17 27 15_83291917](https://github.com/user-attachments/assets/b8460bf5-ca52-41d8-81e5-c78005d52626)

Add Goal screen
![WhatsApp Image 2025-04-17 at 17 27 16_8db27ebe](https://github.com/user-attachments/assets/5bdf5cdc-f6f2-4516-b36d-04eab3a3d54c)
![WhatsApp Image 2025-04-17 at 17 27 16_a0557117](https://github.com/user-attachments/assets/c3488512-bbdb-4658-9244-4b26f43beaea)


Add Money: 
![WhatsApp Image 2025-04-17 at 17 27 16_2ace6ca5](https://github.com/user-attachments/assets/4bda54b7-6d2f-41ff-a71e-896e32220fef)

View Goals with progress bars
![WhatsApp Image 2025-04-17 at 17 27 16_162825f8](https://github.com/user-attachments/assets/0d2f15fd-2388-4ab5-b837-568e3eb5a6a7)

Completed goal toast
![WhatsApp Image 2025-04-17 at 17 30 17_0aba6c7c](https://github.com/user-attachments/assets/44014c66-f669-4385-a89b-dd0544f4d661)
---

📂 Folder Structure

WishWallet/
├── app/
│   ├── java/com/example/piggybank/
│   │   ├── MainActivity.java
│   │   ├── AddGoalActivity.java
│   │   ├── GoalManager.java
│   │   ├── Goal.java
│   │   └── GoalAdapter.java
│   └── res/
│       ├── layout/
│       │   └── activity_main.xml
│       │   └── activity_add_goal.xml
│       │   └── activity_view_goals.xml
│       └── values/
│           └── styles.xml

---

📦 Tools & Technologies
Language: Java

Framework: Android SDK

IDE: Android Studio

DSA Concepts: HashMap, PriorityQueue, Greedy Strategy

---

🎯 Future Scope
Add reminders to save daily/weekly

Visual analytics of monthly savings

Backup goals to cloud (Firebase)

Voice command integration

---
