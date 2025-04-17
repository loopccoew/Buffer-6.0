Problem Statement Description:
In cities, roads often get congested due to too many vehicles using the same route. This leads to traffic jams, longer travel times, and inefficiency.

Our project aims to:
Simulate traffic movement and use smart algorithms to always find the fastest, least-congested path for each vehicle in real-time.

Data Structures Used:
unordered_map--->	To store the graph (roads between places)	Fast lookups of neighbors
vector<Edge>----->	List of all connected roads for each node	Helps model all roads from a location
priority_queue----->	To always pick the shortest available path (Dijkstra)	Ensures efficient path finding
struct Edge	Stores road info: destination, distance, load, etc.	Customizable for real-world road features
vector<pair<double, double>>---->Tracks vehicles on a road over time	Used for real-time traffic simulation

Video link:
https://drive.google.com/file/d/10Ps9S82_c-JObuv5ASygLllU0lvHmldl/view?usp=sharing
