# Women's Safety Application 

A command-line based C++ application to assist with women's safety by providing functionalities such as:
Finding the nearest police stations
Calculating the safest route between two locations
Ranking areas by safety
Accessing a safety-focused chatbot

-------------------------------------------------------------------------------------------------------------------------------------------------
## 🧩 Problem Statement
Women Travel safety application as - Women and often face safety challenges in urban environments or rather when they travel to new cities. In new and unknown places, women don't know about the nearby police stations, safe routes for travelling alone or at nights, crime rate of particular places making their experience of travelling to new city worrisome and consists of high risks of their individual safety.

-------------------------------------------------------------------------------------------------------------------------------------------------
## ✅ Solution
This project processes crime index data and police station networks to build a safety-aware system for urban navigation and alerting. It includes:

City Selection to localize the search space
Crime-weighted Routing using Dijkstra’s algorithm
Nearby Searches using Breadth-First Search (BFS)
Top-K Safety Analytics based on average crime indices using Heaps
Chatbot Integration to guide users interactively

-------------------------------------------------------------------------------------------------------------------------------------------------
## 📦 Data Structures Used

1. unordered_map<Type T, Type <T>>:    1. Adjacency list with crime-weighted edges for Dijkstra
                            	    2. Adjacency list for BFS (non-weighted graph)
                                    3. To keep crime scores for Dijkstra
                                    4. Parent tracking for route reconstruction

2. priority_queue<pair<int, string>>:	Min-heap and max-heap for ranking areas by crime index
3. queue<string>:  Queue used for BFS traversal
4. unordered_set<string>:  To track visited locations in BFS

5. Graph:   Places in city are nodes, while crime-weighted or nearest locations from that place form the edges.
6. Heap:    Min-heap and max-heap to implementation for top k safest or dangerous places

-------------------------------------------------------------------------------------------------------------------------------------------------
## 📊 Algorithms Used

1. Dijkstra’s Algorithm:    To find the safest (least crime) path between two locations
2. Breadth-First Search (BFS):	To discover and rank nearby police stations by proximity
3. Heap Sort via Priority Queue:	To list top-K safest and most dangerous areas

-------------------------------------------------------------------------------------------------------------------------------------------------
## 🔍 Requirements

1. C++17 or later standard libraries:
    a. <bits/stdc++.h>, 
    b. <iostream>, 
    c. <fstream>, 
    d. <sstream>
2. Python 3.13.3 installed
3. pip install google-generativeai

-------------------------------------------------------------------------------------------------------------------------------------------------
## ▶️ How to Run
1. g++ main.cpp -o safety_app
2. ./safety_app

-------------------------------------------------------------------------------------------------------------------------------------------------
## 💻 Demo Video Link:

[Click here to watch](https://drive.google.com/file/d/1xdixcDiubu2yRcDQvSOAHIXWOIzwUvfp/view?usp=sharing)

