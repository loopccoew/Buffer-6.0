import streamlit as st
import heapq
import random

# Predefined locations
locations = {
    'Library': {'crime_rate': 0.2},
    'Cafeteria': {'crime_rate': 0.3},
    'SafeZone': {'crime_rate': 0.1},
    'Bookstore': {'crime_rate': 0.25},
    'Campus Center': {'crime_rate': 0.3},
    'Hostel': {'crime_rate': 0.75},
    'Dark Alley': {'crime_rate': 0.9},
    'Parking Lot': {'crime_rate': 0.85},
    'Abandoned Building': {'crime_rate': 0.95},
    'Empty Street': {'crime_rate': 0.8}
}

# Distances between locations (graph)
graph = {
    'Library': {'Cafeteria': 0.5, 'SafeZone': 1.0},
    'Cafeteria': {'Library': 0.5, 'Hostel': 1.0},
    'SafeZone': {'Library': 1.0, 'Campus Center': 1.5},
    'Bookstore': {'Campus Center': 0.7},
    'Campus Center': {'Bookstore': 0.7, 'SafeZone': 1.5},
    'Hostel': {'Cafeteria': 1.0, 'Dark Alley': 1.5},
    'Dark Alley': {'Hostel': 1.5, 'Parking Lot': 1.0},
    'Parking Lot': {'Dark Alley': 1.0, 'Abandoned Building': 1.0},
    'Abandoned Building': {'Parking Lot': 1.0, 'Empty Street': 0.5},
    'Empty Street': {'Abandoned Building': 0.5}
}

# Dijkstra’s Algorithm
def dijkstra(graph, start, end):
    queue = [(0, start)]
    distances = {start: 0}
    previous = {start: None}

    while queue:
        dist, node = heapq.heappop(queue)
        if node == end:
            path = []
            while node:
                path.append(node)
                node = previous[node]
            return path[::-1]
        for neighbor, weight in graph.get(node, {}).items():
            new_dist = dist + weight
            if neighbor not in distances or new_dist < distances[neighbor]:
                distances[neighbor] = new_dist
                previous[neighbor] = node
                heapq.heappush(queue, (new_dist, neighbor))
    return None

# Safety Check
def check_safety(location):
    rate = locations[location]['crime_rate']
    if rate <= 0.3:
        return "Safe Zone"
    elif rate <= 0.7:
        return "Moderate Risk"
    else:
        return "Unsafe Zone"

# Harassment Detection
class HarassmentMonitor:
    def __init__(self):
        if "blocked_users" not in st.session_state:
            st.session_state.blocked_users = set()
        if "user_reports" not in st.session_state:
            st.session_state.user_reports = {}
        self.abusive_words = {
    "inappropriate", "offensive", "rude", "harsh", "disrespectful", "insult"
}


    def contains_abusive_word(self, message):
        lower_msg = message.lower()
        return any(bad_word in lower_msg for bad_word in self.abusive_words)

    def handle_abusive_message(self, sender, recipient, message):
        if self.contains_abusive_word(message):
            st.warning("Message flagged as abusive content.")

            st.markdown("---")
            st.subheader("Actions you can take:")
            col1, col2, col3, col4, col5 = st.columns(5)

            # Block
            if col1.button("\U0001F6D1 Block", key=f"block_{sender}"):
                st.session_state.blocked_users.add(sender)
                st.session_state[f"{sender}_action"] = "Blocked"

            # Restrict
            if col2.button("\U0001F512 Restrict", key=f"restrict_{sender}"):
                st.session_state[f"{sender}_action"] = "Restricted"

            # Remove
            if col3.button("\u274E Remove", key=f"remove_{sender}"):
                st.session_state[f"{sender}_action"] = "Removed"

            # Report
            if col4.button("\U0001F4E3 Report", key=f"report_{sender}"):
                st.session_state.user_reports[sender] = st.session_state.user_reports.get(sender, 0) + 1
                st.session_state[f"{sender}_action"] = "Reported"

            # Ignore
            if col5.button("\U0001F648 Ignore", key=f"ignore_{sender}"):
                st.session_state[f"{sender}_action"] = "Ignored"

            # Show status
            action = st.session_state.get(f"{sender}_action")
            if action:
                st.info(f"User **{sender}** has been **{action.lower()}**.")

            # Log
            with st.expander("View Block/Report Status Log"):
                st.write("Blocked Users:", list(st.session_state.blocked_users))
                if sender in st.session_state.user_reports:
                    st.write(f"{sender} has been reported {st.session_state.user_reports[sender]} times.")
        else:
            st.success(f"Message sent to **{recipient}** successfully.")

# Streamlit UI
def main():
    st.set_page_config(page_title="SheShield Safety System", page_icon="\U0001F6E1")
    st.title("\U0001F6E1 SheShield Safety System")

    menu = ["Safety Navigation", "Harassment Detection"]
    choice = st.sidebar.selectbox("Choose a Feature", menu)

    if choice == "Safety Navigation":
        st.subheader("\U0001F4CD Safety Navigation System")

        if "selected_location" not in st.session_state:
            st.session_state.selected_location = None

        st.markdown("### Available Locations:")
        st.markdown(", ".join([f"**{loc}**" for loc in locations.keys()]))

        st.markdown("### Select Your Current Location:")
        col_buttons = st.columns(5)
        for idx, loc in enumerate(locations.keys()):
            if col_buttons[idx % 5].button(loc):
                st.session_state.selected_location = loc

        if st.session_state.selected_location:
            selected_location = st.session_state.selected_location
            safety = check_safety(selected_location)
            st.info(f"{selected_location} is classified as: **{safety}**")

            if safety == "Unsafe Zone":
                if st.button("Find Path to SafeZone"):
                    path = dijkstra(graph, selected_location, "SafeZone")
                    if path:
                        st.success("Shortest path to SafeZone:")
                        st.markdown(" → ".join(path))
                        distance = sum([graph[path[i]][path[i + 1]] for i in range(len(path) - 1)])
                        st.info(f"Distance: **{distance} km**")
                        crowd = random.randint(3, 20)
                        st.info(f"People in SafeZone: **{crowd}**")
                    else:
                        st.error("No path to SafeZone found.")
            else:
                st.success("You are in a safe or moderately safe area.")
        else:
            st.warning("Please select your current location from the options above.")

    elif choice == "Harassment Detection":
        st.subheader("\U0001F4E8 Harassment Detection System")
        monitor = HarassmentMonitor()

        sender = st.text_input("Sender Username:", placeholder="e.g., User123")
        recipient = st.text_input("Recipient Username:", placeholder="e.g., Friend456")
        message = st.text_area("Enter your message:", placeholder="Type here...")

        if st.button("Send Message"):
            if sender and recipient and message.strip():
                monitor.handle_abusive_message(sender, recipient, message)
            else:
                st.error("Please fill in all fields before sending the message.")

if __name__ == "__main__":
    main()
