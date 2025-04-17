# Buffer-6.0
4. Custom Data Structure
 Document Version Control System
Problem Statement This project addresses the challenge of managing complex document version histories, especially in collaborative or research environments. Traditional linear version systems struggle with non-linear changes, branching, and reconstruction. This system models versions as a graph, allowing flexible navigation, annotation, and recovery of versions.

Data Structures Used

Graph (Adjacency List): To represent document versions and their relationships. HashMap: For quick retrieval of versions, annotations, and relationships. ArrayList: To maintain collections of versions, annotations, and connections. File Overview

Java Backend Files:

HistoricalVersionControlSys.java: Main driver of the system. DocumentGraph.java: Core graph structure and traversal logic. Annotation.java: Stores metadata or notes per version. Manuscript.java: Wraps content and metadata. ReconstructionTheory.java: Contains logic to recover lost versions. Relationship.java: Models edges in the version graph. TextVersion.java: Represents a single document version. WebController.java: Connects backend to frontend using REST. Frontend HTML Files:

index.html: Main entry page with navigation. graphdetails.html: Displays version graph visually. theorydetails.html: Shows reconstruction logic and outcomes. versiondetails.html: Displays details of each document version. Features

Non-linear version tracking with graphsclass Annotation support Lost version reconstruction Clean HTML interface Use Cases

Historical document preservation Academic and research collaboration Manuscript revision tracking 
video link : https://drive.google.com/file/d/129dls0s7CW4SAxMka36FkI5SraTCE_sv/view?usp=drive_link
