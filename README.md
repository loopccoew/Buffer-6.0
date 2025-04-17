# Buffer-6.0

The themes for Buffer 6.0 are -

4. Custom Data Structure

Problem Statement:
Urban carbon emissions are escalating due to unaware transportation choices. Citizens often lack insight into how daily commuting impacts the environment. There is a growing need for an intuitive platform that not only calculates carbon emissions but also educates users and rewards eco-friendly decisions, encouraging steps toward sustainability.

To address this, we propose a Java-based application named "Track.C" that helps users track carbon-friendly journeys in urban areas. The app calculates CO₂ emissions based on the selected vehicle type, uses Dijkstra’s algorithm to find the shortest and most eco-friendly route, rewards users for choosing low-emission options, and visualizes the route on an embedded graph image.


Data Structures & Components Used:

Graph (Adjacency List):
Models the city layout with nodes (locations) and edges (routes) where weights represent distance or carbon emissions.

PriorityQueue (Min-Heap):
Used in Dijkstra’s Algorithm for efficient optimal pathfinding based on shortest distance or lowest emissions.

HashMap & ArrayList:
Manage user accounts, transport graph data, journey history, and reward points efficiently.

Algorithm – Dijkstra’s Algorithm:
Calculates the shortest or most eco-friendly route between selected locations.

GUI – Java Swing Components:
Built using JFrame, JPanel, JLabel, JButton, JOptionPane, etc., for user interaction, vehicle selection, displaying journey details, and visualizing the embedded graph.


Video Link: 
