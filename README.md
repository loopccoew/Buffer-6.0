# Buffer-6.0
Team Name:Tech Pioneers
Year:Second Year
Theme:Custom Data Structure

Problem Statement:
In densely populated urban areas, finding an available parking spot is often time-consuming and inefficient due to limited space visibility and lack of real-time updates. This leads to increased traffic congestion and user frustration. The project aims to build a Smart Parking Management System using core data structures and algorithms in Java to efficiently track, search, and allocate and book parking slots through a command-line interface

Key Features:
Location Management: Add and manage multiple parking locations with details like total slots, pricing, and EV charging availability.
Booking System: Users can book parking slots for specific time periods.
User Management: User registration, login, blocking/unblocking, and messaging.
Feedback & Ratings: Users can rate and provide feedback for parking locations.
Analytics: Track occupancy, revenue, and user statistics.
EV Charging: Find parking locations with EV charging facilities.
Messaging System: Communication between users and administrators.
Revenue Tracking: Monitor revenue generated from each location.

Data Structures Used
1.Maps (HashMap)
Map<String, User>: Store user information
Map<String, SegmentTree>: Track available slots
Map<String, List<Booking>>: Store bookings
Map<String, Double>: Track revenue

2.Lists (ArrayList)
Store collections of bookings, messages, feedbacks, locations, and users

3.Priority Queue
Store feedbacks sorted by rating

4.Graph (LocationGraph)
Represent connections between parking locations
Find nearest locations and EV charging stations

5.Segment Tree
Efficiently manage slot availability
O(log n) operations for booking/releasing slots

6.Custom Classes
Location: Parking location details
User: User information and history
Booking: Slot booking details
Feedback: User ratings and comments

7.Date/Time Objects
LocalDateTime: Track booking times

Video Link: https://drive.google.com/file/d/1Qn5rDZVxyGBw20eEJdtAkC8ajcDV6jBc/view?usp=sharing



