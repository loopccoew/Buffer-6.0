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
4. Completed goals are removed using:
   ```java
   goalsMap.values().removeIf(goal -> goal.getRemainingAmount() <= 0);

