Problem Statement
How can we build an AI-powered system that intelligently connects students with similar learning goals for peer-to-peer collaboration and group study sessions?
Students often struggle to find peers who share similar learning goals and study styles.
We aim to build an AI-powered system that identifies and connects students based on their learning patterns.
This will encourage effective peer-to-peer collaboration and group study sessions for better learning outcomes.
Data Structures and Algorithms
To implement the core functionality of matching students based on their learning styles, we applied Data Structures and Algorithms (DSA) concepts to develop the quiz pattern-matching logic:
Quiz Mapping:
Each user's answer in the quiz is mapped to one of four study types (A, B, C, or D).
Hash Map Usage:
We use a hash map (dictionary) to track the frequency of each study type selected by the user.
Advantages:
O(1) lookup and update time: This ensures fast and efficient frequency counting regardless of the number of answers.
O(1) means that the time taken by the algorithm remains constant, no matter the size of the data.
Dominant Study Type Determination:
A greedy algorithm is applied to select the study type with the highest frequency, representing the user's dominant learning pattern.
Efficiency:
List Traversal (O(n)): We traverse the user's answers once to update the frequency counts, where n is the number of answers.
O(n) indicates that the algorithm's running time increases linearly with the amount of input data, keeping the process highly efficient.
Classification:
The final step effectively acts as a lightweight classification algorithm, labeling each user based on their quiz response patterns.
Demo Video
https://drive.google.com/file/d/1boxAr7otOBNRMVLrDn2iOEdWS63De7Kb/view?usp=sharing
