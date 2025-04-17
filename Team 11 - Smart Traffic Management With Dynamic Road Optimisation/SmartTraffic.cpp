//step1
#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>
#include <limits>
#include <string>
#include <sstream>
#include <algorithm>
using namespace std;

// Each edge represents a road between intersections.
// It stores:
// - destination node,
// - distance (km),
// - capacity (maximum allowed load),
// - currentLoad (the total load currently on the road),
// - onRoadVehicles: a list of events (each event is a pair: <departureTime, load>)
//   to know when a vehicle will leave the road.
struct Edge {
    string dest;
    int distance;            // road distance in km
    double capacity;         // maximum allowed load (e.g., a road can support 3.0 load units)
    double currentLoad;      // current load on the road
    vector<pair<double, double>> onRoadVehicles; // each event: <departure time, load added>
    
    Edge(const string &d, int dist, double cap)
        : dest(d), distance(dist), capacity(cap), currentLoad(0.0) {}
};

using Graph = unordered_map<string, vector<Edge>>;

//step2
// Adds an undirected edge between src and dest with a specified distance and capacity.
void addEdge(Graph &G, const string &src, const string &dest, int distance, double capacity) {
    G[src].push_back(Edge(dest, distance, capacity));
    G[dest].push_back(Edge(src, distance, capacity));
}

// Creates a sample road network.
// Adjust distances and capacities as needed.
Graph createRoadNetwork() {
    Graph G;
    addEdge(G, "A", "B", 4, 3.0);
    addEdge(G, "A", "C", 2, 2.0);
    addEdge(G, "B", "C", 1, 2.0);
    addEdge(G, "B", "D", 5, 3.0);
    addEdge(G, "C", "D", 8, 2.0);
    addEdge(G, "C", "E", 10, 3.0);
    addEdge(G, "D", "E", 2, 2.0);
    addEdge(G, "D", "F", 6, 3.0);
    addEdge(G, "E", "F", 3, 3.0);
    return G;
}

//step3
// Helper structure for the priority queue in Dijkstra’s algorithm.
struct NodeInfo {
    string node;
    int dist;
    bool operator>(const NodeInfo &other) const {
        return dist > other.dist;
    }
};

// Dijkstra’s algorithm: Finds the shortest path (by road distance).
// Returns a pair: vector of nodes in the path and total distance.
pair<vector<string>, int> findShortestPath(const Graph &G, const string &start, const string &destination) {
    unordered_map<string, int> distances;
    unordered_map<string, string> previous;
    
    for (auto &pair : G)
        distances[pair.first] = numeric_limits<int>::max();
    distances[start] = 0;
    
    priority_queue<NodeInfo, vector<NodeInfo>, greater<NodeInfo>> pq;
    pq.push({start, 0});
    
    while (!pq.empty()) {
        NodeInfo current = pq.top();
        pq.pop();
        if (current.node == destination)
            break;
        for (const Edge &edge : G.at(current.node)) {
            int newDist = current.dist + edge.distance;
            if (newDist < distances[edge.dest]) {
                distances[edge.dest] = newDist;
                previous[edge.dest] = current.node;
                pq.push({edge.dest, newDist});
            }
        }
    }
    
    vector<string> path;
    if (distances[destination] == numeric_limits<int>::max())
        return {path, numeric_limits<int>::max()};
    
    // Reconstruct the path
    for (string at = destination; at != ""; at = previous.count(at) ? previous[at] : "") {
        path.push_back(at);
        if (at == start)
            break;
    }
    reverse(path.begin(), path.end());
    return {path, distances[destination]};
}

//step4
// Returns a pair: <average speed in km/h, load factor> for the given vehicle type.
pair<double, double> getVehicleInfo(const string &type) {
    if (type == "car")
        return {60.0, 1.0};
    else if (type == "bus")
        return {40.0, 3.0};
    else if (type == "2wheeler" || type == "2-wheeler")
        return {50.0, 0.5};
    else if (type == "bicycle")
        return {15.0, 0.2};
    else
        return {60.0, 1.0}; // default to car
}

//step5
// Removes expired vehicle events for an edge.
// currentTime represents the simulation time (in hours).
void cleanEdgeEvents(Edge &edge, double currentTime) {
    vector<pair<double, double>> remain;
    for (auto &event : edge.onRoadVehicles) {
        if (event.first > currentTime) {
            remain.push_back(event);
        } else {
            edge.currentLoad -= event.second;
        }
    }
    edge.onRoadVehicles = remain;
    if (edge.currentLoad < 0)
        edge.currentLoad = 0;
}

//step6
// For a given route and vehicle type, update the traffic on each edge.
// startTime is the simulation time when the vehicle starts the journey.
// Returns the vehicle's final arrival time.
double updateTrafficWithVehicle(Graph &G, const vector<string> &route, double startTime, const string &vehicleType) {
    double currentTime = startTime;
    auto vehicleInfo = getVehicleInfo(vehicleType);
    double avgSpeed = vehicleInfo.first;    // km/h
    double loadFactor = vehicleInfo.second;   // load units
    
    for (size_t i = 0; i < route.size() - 1; i++) {
        string u = route[i], v = route[i+1];
        // Locate edge from u to v and update traffic.
        for (Edge &edge : G[u]) {
            if (edge.dest == v) {
                cleanEdgeEvents(edge, currentTime);
                edge.currentLoad += loadFactor;
                double travelTime = static_cast<double>(edge.distance) / avgSpeed; // time in hours
                double departureTime = currentTime + travelTime;
                edge.onRoadVehicles.push_back({departureTime, loadFactor});
                cout << "Vehicle (" << vehicleType << ") on road " << u << "-" << v 
                     << " added load " << loadFactor << " until time " << departureTime << endl;
                // Update the reverse edge (since the graph is undirected).
                for (Edge &revEdge : G[v]) {
                    if (revEdge.dest == u) {
                        cleanEdgeEvents(revEdge, currentTime);
                        revEdge.currentLoad += loadFactor;
                        revEdge.onRoadVehicles.push_back({departureTime, loadFactor});
                    }
                }
                currentTime = departureTime; // Advance time after traversing this edge.
                break;
            }
        }
    }
    return currentTime; // Return final arrival time.
}

//step7
// Checks if any edge on the given route is congested at the current simulation time.
bool isRouteCongestedGraph(Graph &G, const vector<string> &route, double currentTime) {
    for (size_t i = 0; i < route.size() - 1; i++) {
        string u = route[i], v = route[i+1];
        for (Edge &edge : G[u]) {
            if (edge.dest == v) {
                cleanEdgeEvents(edge, currentTime);
                if (edge.currentLoad > edge.capacity)
                    return true;
            }
        }
    }
    return false;
}

//step8
// Builds an optimized graph by including only edges that are not congested (i.e. currentLoad <= capacity).
// Then returns the shortest path on that graph.
pair<vector<string>, int> optimizeRouteForTraffic(const Graph &G, const string &start, const string &destination) {
    Graph G_opt;
    for (auto &pair : G) {
        const string &node = pair.first;
        for (const Edge &edge : pair.second) {
            if (edge.currentLoad <= edge.capacity)
                G_opt[node].push_back(edge);
        }
    }
    auto result = findShortestPath(G_opt, start, destination);
    if (result.first.empty()) {
        cout << "No available alternative route found." << endl;
    }
    return result;
}

//step9
void runMultipleVehiclesSimulationAdvanced() {
    Graph G = createRoadNetwork();
    int numVehicles;
    cout << "Enter the number of vehicles: ";
    cin >> numVehicles;
    double globalTime = 0.0;  // simulation starts at time zero (in hours)
    
    for (int vehicle = 1; vehicle <= numVehicles; vehicle++) {
        cout << "\n=== Vehicle " << vehicle << " ===" << endl;
        string vehicleType;
        cout << "Enter vehicle type (car, bus, 2wheeler, bicycle): ";
        cin >> vehicleType;
        string start, destination;
        cout << "Enter the start point for vehicle " << vehicle << ": ";
        cin >> start;
        cout << "Enter the destination point for vehicle " << vehicle << ": ";
        cin >> destination;
        
        // Find the shortest path by road distance.
        auto result = findShortestPath(G, start, destination);
        vector<string> path = result.first;
        int distance = result.second;
        if (path.empty()) {
            cout << "No route found for vehicle " << vehicle << " from " 
                 << start << " to " << destination << endl;
            continue;
        }
        cout << "Suggested route: ";
        for (const auto &node : path)
            cout << node << " ";
        cout << "with total distance = " << distance << endl;
        
        // Update traffic along the chosen path.
        double arrivalTime = updateTrafficWithVehicle(G, path, globalTime, vehicleType);
        cout << "Vehicle " << vehicle << " (" << vehicleType << ") arrives at time " << arrivalTime << endl;
        
        // Check if any segment of the route is congested after this vehicle.
        if (isRouteCongestedGraph(G, path, arrivalTime)) {
            cout << "The route is congested due to current load." << endl;
            auto newResult = optimizeRouteForTraffic(G, start, destination);
            vector<string> newPath = newResult.first;
            int newDistance = newResult.second;
            if (!newPath.empty()) {
                cout << "Optimized route for vehicle " << vehicle << ": ";
                for (const auto &node : newPath)
                    cout << node << " ";
                cout << "with total distance = " << newDistance << endl;
                double newArrivalTime = updateTrafficWithVehicle(G, newPath, globalTime, vehicleType);
                cout << "Vehicle " << vehicle << " (" << vehicleType << ") takes optimized route and arrives at time " 
                     << newArrivalTime << endl;
            } else {
                cout << "No alternative route available due to heavy congestion." << endl;
            }
        } else {
            cout << "The chosen route is clear of heavy congestion." << endl;
        }
        
        // Advance global simulation time.
        if (arrivalTime > globalTime)
            globalTime = arrivalTime;
        cout << "Global simulation time is now: " << globalTime << " hours" << endl;
    }
}

//step 10
int main() {
    runMultipleVehiclesSimulationAdvanced();
    return 0;
}
