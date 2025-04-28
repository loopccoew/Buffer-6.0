# location_safe_route.py

import webbrowser     # Allows opening URLs in the default web browser (used for Google Maps)
from db import get_connection  # Function to establish a database connection


def get_location_graph():
    """
    Builds a graph of locations where:
    - Each node is a location ID
    - Edges represent neighboring locations with base distances
    - Danger scores are factored in by adding penalties to the weights
    Returns the adjusted graph.
    """
    connection = get_connection()
    cursor = connection.cursor()

    # Base map: 30 locations with base distances (unadjusted for danger)
    base_graph = {     
        1: {2: 10, 3: 15}, 2: {3: 25}, 3: {4: 30}, 4: {5: 5}, 5: {6: 20},
        6: {7: 35}, 7: {8: 10}, 8: {9: 15}, 9: {10: 40}, 10: {11: 10},
        11: {12: 25}, 12: {13: 5}, 13: {14: 20}, 14: {15: 15}, 15: {16: 25},
        16: {17: 30}, 17: {18: 10}, 18: {19: 20}, 19: {20: 15}, 20: {21: 25},
        21: {22: 20}, 22: {23: 10}, 23: {24: 25}, 24: {25: 30}, 25: {26: 5},
        26: {27: 15}, 27: {28: 35}, 28: {29: 10}, 29: {30: 40}
    }

    # Fetch current danger scores for each location
    cursor.execute("SELECT location_id, danger_score FROM location_danger")
    danger_scores = dict(cursor.fetchall())

    danger_penalty = 10  # Penalty multiplier for danger scores

    # Create a graph applying danger penalties to edges
    graph = {}
    for node, neighbors in base_graph.items():
        if node not in graph:
            graph[node] = {}
        for neighbor, base_weight in neighbors.items():
            # Add penalty based on the danger score of the starting location
            penalty = danger_scores.get(node, 0) * danger_penalty
            adjusted_weight = base_weight + penalty
            graph[node][neighbor] = adjusted_weight

            # Make it bidirectional (penalty can be different in reverse direction)
            if neighbor not in graph:
                graph[neighbor] = {}
            reverse_penalty = danger_scores.get(neighbor, 0) * danger_penalty
            reverse_weight = base_weight + reverse_penalty
            graph[neighbor][node] = reverse_weight

    cursor.close()
    connection.close()
    return graph


def dijkstra(graph, start, destination):
    """
    Standard Dijkstra's algorithm to find the shortest path
    considering adjusted edge weights.
    Returns the path as a list of node IDs and the total distance.
    """
    distances = {node: float('inf') for node in graph}  # Initialize all distances as infinity
    previous = {node: None for node in graph}  # For path reconstruction
    visited = set()
    distances[start] = 0  # Distance to start node is 0

    while len(visited) < len(graph):
        # Select the unvisited node with the smallest distance
        min_node = None
        min_distance = float('inf')
        for node in graph:
            if node not in visited and distances[node] < min_distance:
                min_distance = distances[node]
                min_node = node

        if min_node is None:
            break  # Remaining nodes are inaccessible

        visited.add(min_node)

        # Update distances for neighboring nodes
        for neighbor, weight in graph[min_node].items():
            if neighbor in visited:
                continue
            new_dist = distances[min_node] + weight
            if new_dist < distances[neighbor]:
                distances[neighbor] = new_dist
                previous[neighbor] = min_node

    # Reconstruct the path
    path = []
    current = destination
    while current is not None:
        path.insert(0, current)
        current = previous[current]

    if distances[destination] == float('inf'):
        return None, float('inf')  # No path found

    return path, distances[destination]


def get_safe_route(start_name, destination_name):
    """
    Finds the safest route from start to destination,
    factoring in danger scores, and opens the route in Google Maps.
    """
    connection = get_connection()
    cursor = connection.cursor()

    # Fetch start location details
    cursor.execute("SELECT id, latitude, longitude FROM locations WHERE name = %s", (start_name,))
    start_row = cursor.fetchone()
    if not start_row:
        print(f"❌ Start location '{start_name}' not found.")
        return
    start_id, start_lat, start_lon = start_row

    # Fetch destination location details
    cursor.execute("SELECT id, latitude, longitude FROM locations WHERE name = %s", (destination_name,))
    dest_row = cursor.fetchone()
    if not dest_row:
        print(f"❌ Destination location '{destination_name}' not found.")
        return
    dest_id, dest_lat, dest_lon = dest_row

    # Build danger-adjusted graph and find safe path
    graph = get_location_graph()
    path_ids, total_distance = dijkstra(graph, start_id, dest_id)

    if not path_ids:
        print("⚠️ No route found.")
        return

    # Collect the coordinates along the path (optional if you want detailed directions)
    coordinates = []
    for loc_id in path_ids:
        cursor.execute("SELECT latitude, longitude FROM locations WHERE id = %s", (loc_id,))
        lat, lon = cursor.fetchone()
        coordinates.append(f"{lat},{lon}")

    # Create Google Maps link
    base_url = "https://www.google.com/maps/dir/?api=1"
    origin = f"{start_lat},{start_lon}"
    destination = f"{dest_lat},{dest_lon}"
    maps_link = f"{base_url}&origin={origin}&destination={destination}"

    # Print and open the route in browser
    print(f"Google Maps: {maps_link}")
    webbrowser.open(maps_link)

    cursor.close()
    connection.close()
