# Buffer-6.0

*Problem Statement:* Spam Email Detection Using Customised Data Structures 

Spam emails cause inbox clutter, phishing risks, and security threats. This project aims to develop a lightweight and efficient spam detection system purely using Data Structures & Algorithms, ensuring fast processing and real-time filtering.

*📌 Overview*
Spam Email Detection is a Java-based application designed for analyzing and detecting spam emails using keyword and sender-based techniques. It’s primarily a console-based application focusing on backend logic rather than front-end technologies.

*🚀 Features*
- Detect spam emails using predefined keywords.
- Track and analyze sender behavior i.e gives suggestions for mail content. 
- Efficient keyword search using Trie.
- Console-based user interaction.
Video link: https://drive.google.com/drive/folders/1CCRFNtV4QYxWFp3MZPZ2LH0TvXuGbbAj?usp=sharing
*🛠️ Technologies Used*
1. Java
2. Object-Oriented Programming
3. Trie Data Structure
4. Linked Hash Map, HashMaps, HashSets and Priority Queues
5. ArryaLists and Arrays

The project primarily uses the Trie data structure, which is highly efficient for storing and searching strings—especially useful for keyword-based spam detection. Each spam keyword is inserted into the Trie, and incoming messages are scanned word by word to check for matches. This allows fast lookups compared to traditional string-matching techniques.

Other common Java structures like *ArrayLists, HashMaps, Linked HashMaps, Hash Sets and Arrays, Priority Queues* are also used internally for storing emails, senders, and index logs.

📂 Project Structure

main.java              // Entry Point 
sender.java            // Sender Analysis and Storage
spamdetect.java   
// Keyword-based Spam Detection using Trie and Blocking of Spammers using appropriate ratio
user.java               
// User interface and Actions for navigations like Search, Sort and Display
