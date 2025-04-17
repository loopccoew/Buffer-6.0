# Buffer-6.0

Dscription of problem statement:
The SOSAlert SOS Service is a Java-based desktop application designed to trigger emergency alerts efficiently and intelligently. It is aimed at enhancing personal safety by notifying emergency contacts and alerting nearby police stations when a user is in danger. The alert can be activated by pressing a single SOS button three times within 5 seconds, ensuring both ease of use and intentionality.

Data Structures used:
	1.	List (ArrayList) – Used to store emergency contacts and nearby police stations.
	2.	Comparator – Used to sort police stations based on their distance from the user.
	3.	Stream API – Helps in filtering and processing the list of stations (e.g., picking the nearest 3).
	4.	String – Stores names, phone numbers, messages, and logs.
	5.	Date – Captures the timestamp when an SOS is sent.
	6.	Primitive types – double for coordinates, int for button press count, and long for timing logic.

Key Features:
	1.	Triple-Click SOS Trigger:
	•	Users must press the SOS button 3 times within 5 seconds to avoid accidental activation.
	•	Ensures quick access in emergency scenarios with minimal interaction.
	2.	Automatic Alert System:
	•	Sends a real-time notification message to predefined emergency contacts (like family and friends).
	•	Also sends the alert to the 3 nearest police stations, based on geolocation.
	3.	Distance-Based Alerting:
	•	Uses the Haversine formula to calculate the geographical distance between the user and each police station.
	•	Sorts police stations by proximity to prioritize the nearest help.
	4.	Real-Time Log Display:
	•	A clean console-like JTextArea shows the status of button presses, contact notifications, and police alerts.
	•	Helps in debugging, transparency, and user feedback.

 link to video:
 https://drive.google.com/drive/folders/12JcPHr4M53wiNs-GNcjZG9_YWQ9bfV6B?usp=sharing
 
