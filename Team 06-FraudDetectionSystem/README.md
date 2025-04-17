💻 HackoHolics – Fraud Detection System
👩‍💻 Authors:
Rachna Pillai

Aarya Patankar

Mrunal Deore

Siddhi Chavhan

📌 Project Overview
HackoHolics is a simple yet effective Fraud Detection System developed in Java with a user-friendly GUI built using Swing. It helps analyze transaction networks and detect suspicious behavior like fraud rings or abnormal transaction chains.

At its core, the system simulates financial transactions between users, models them as a graph, and uses graph traversal algorithms to spot red flags. We’ve also integrated SQLite to manage and persist transaction data locally, making it easy to test and scale.

🧠 Key Technologies & Concepts
Java Swing: For building the graphical user interface (GUI).

SQLite: Lightweight local database used to store and retrieve transaction data.

Graphs: Used to represent users and their transactions as nodes and edges.

Adjacency List: Efficiently stores the transaction network in memory.

Graph Traversal (DFS & BFS): Helps identify loops, long transaction chains, and other anomalies that may signal fraud.

⚙️ How to Run the Project
🛠️ Prerequisites:
Java installed (version 8 or higher recommended)

sqlite-jdbc-3.49.1.0.jar file present in the same directory

🧾 Compilation:
bash
Copy
Edit
javac -cp ".;sqlite-jdbc-3.49.1.0.jar" FDS.java DatabaseHelper.java Graph.java Transaction.java FDSGUI.java
▶️ Run:
bash
Copy
Edit
java -cp ".;sqlite-jdbc-3.49.1.0.jar" FDSGUI
<br><br>
CHECK OUT THE OUTPUT VIDEO
https://drive.google.com/file/d/1IK0hqi4ACZWLvbBkxSlfmS0ghLqEFqLH/view?usp=drive_link


