# 🛕 Temple Darshan Queue Management System

This project is a **command-line based queue management system** for organizing darshan (visits) at a temple. The system ensures **fair, efficient, and prioritized** handling of visitors based on their category such as VIPs, senior citizens, disabled individuals, and general public. It uses a round-robin approach to serve visitors in a fair manner while also giving priority to those who need it most.

## 🚀 Features

- Add new visitors with details like name, age, and category.
- Automatically upgrade visitors aged 60 and above from General to Senior category.
- Fair processing of visitors using a **round-robin algorithm** across all categories.
- Separate queues for VIP, Disabled, Senior, and General visitors.
- Real-time queue display for each category.
- Easy-to-use text-based interface.

---

## 📊 Data Structures Used

### 🔁 `deque` (from `collections` library)

- **Purpose**: Used for each category’s queue.
- **Why**: `deque` is optimized for fast appends and pops from both ends. This makes it perfect for queue operations (FIFO - First In, First Out).
- Four separate deques are maintained for:
  - `VIP`
  - `DISABLED`
  - `SENIOR`
  - `GENERAL`

---

## 🧠 Algorithm & Logic

### 🧾 Visitor Addition Logic

1. Every new visitor is assigned a unique `token` (auto-incremented).
2. If a visitor is aged 60 or more and belongs to the `GENERAL` category, they are **automatically upgraded to the `SENIOR` category**.
3. Based on the final category, the visitor is added to the respective queue.

### 🔄 Fair Processing Algorithm (Round-Robin)

1. Categories are arranged in a fixed round-robin order:
   - `VIP` → `DISABLED` → `SENIOR` → `GENERAL`
2. The system maintains a `category_index` to track the current category to process.
3. On every process request:
   - Check if the queue for the current category is non-empty.
   - If yes, dequeue (process) one visitor from it.
   - Move to the next category in the order for future processing.
   - If no visitors are present in the current category, skip to the next.
4. This ensures **fairness** by allowing every category a turn while prioritizing categories with people waiting.

---

## 🛠️ Tech Stack

- **Language**: Python 3
- **Library Used**: `collections.deque`
- **Interface**: GUI Interface

---
## 🔄 System Workflow

1. User is shown a menu with 4 options:
   - Add Visitor
   - Process Next Visitor
   - Show Queue
   - Exit

2. **Add Visitor**:
   - User enters:
     - Name
     - Age
     - Category (`VIP`, `Disabled`, `Senior`, or `General`)
   - A visitor token is assigned.
   - If age ≥ 60 and category is `GENERAL`, visitor is upgraded to `SENIOR`.

3. **Process Next Visitor**:
   - The system checks each queue in round-robin fashion.
   - If a visitor is found in the current category, they are processed (removed from the queue).
   - Category index is updated to ensure fairness.

4. **Show Queue**:
   - Displays all waiting visitors in each category.

5. **Exit**:
   - Ends the program.

---

## 🎯 Why This Approach?

- **Priority Handling**: Important groups like VIPs, disabled, and senior citizens get prioritized without starving the general public.
- **Efficiency**: `deque` provides O(1) time complexity for enqueue and dequeue operations.
- **Fairness**: Round-robin ensures all categories are served without bias.
- **Simplicity**: The system is lightweight and easy to maintain or expand in the future.

---

## 📸 Sample Output
---- Temple Darshan Ticketing ----

1.Add Visitor

2.Process Next Visitor

3.Show Queue

4.Exit

---
## ✨ Demo Video Link

https://drive.google.com/drive/folders/1cI9gAvxb97MRhUBG3dP5dYN6VELSyLQ_?usp=sharing
---
## ✨ Future Enhancements

- Real-time visitor tracking and estimated wait times.
- Integration with RFID or QR code scanners for automated check-ins.
---

## 👥 Contributors

- Komal Sikchi
- Mira Vadjikar
---
