Problem Statement: Implementing Shesheild, a safety system that detects threats, provides the shortest path to safe zones, enables discreet emergency alerts, and safeguards users from online harassment through real-time monitoring and warnings.

1. Emergency Alert System
Function: Sends discreet emergency messages and continuously shares the user’s location with listed emergency contacts.

Data Structures Used:
1. List: Stores emergency contacts for iteration during message dispatch.
2. Timer/Loop Control: Handles repetitive location sharing at fixed intervals.

2. Online Harassment Detector
Function: Detects abusive content in messages and offers the recipient options to report, block, or restrict the sender.

Data Structures Used:
1. Set: Maintains a list of offensive words for fast lookups.
2. Dictionary: Tracks the number of reports per user.
3. Set: Stores blocked users for quick access and verification.

3. Safe Zone Navigator
Function: Guides a user to the nearest safe zone (e.g., police station or hospital) using shortest path algorithms.

Data Structures Used:
1. Graph (Adjacency List): Models the map with locations and paths.
2. Priority Queue: Used in Dijkstra’s Algorithm to compute shortest paths.
3. List/Array: Tracks distances and previous nodes for path reconstruction.


Link to video : https://drive.google.com/drive/folders/1bFDvTsIMjSMDcaurcrhHFKaF7auEHTK8?usp=sharing
