Overview:
This Blood Donation Web App enables efficient communication between blood donors and recipients. It supports basic registration, login, and donor-recipient matching based on blood group compatibility and location proximity using graph-based search.

Tech stack :
Backend: Java Servlets & JSP

Frontend: HTML, CSS, JavaScript

Database: MySQL

Libraries/Collections Used: HashMap, ArrayList, Queue(Priority Queue), Set (from Java Collections Framework),Graph(BFS)

Key Features:
Donor & Recipient Registration/Login: Users can register as donors or recipients and securely register.
Blood Group Compatibility Matching: Uses HashMap to quickly match compatible blood groups.
Nearby Donor Search: Implements Breadth-First Search (BFS) over a manually defined pincode graph using HashMap and Queue to find nearby donors.

Database Integration: Uses JDBC for real-time data storage and retrieval.

Utility Classes:

BloodCompatibility.java: Provides predefined compatibility rules using HashMap.
GraphSearchUtil.java: Performs BFS to find neighboring pincodes within a given range.
jdbcUtil.java: Manages database connection and cleanup.

You can see video of the working of our project by clicking on the link below: 
[Video Link](https://drive.google.com/file/d/14wyyF9vLRP8KyKye4AOsMo4bpYCmqYbg/view?usp=drive_link)



