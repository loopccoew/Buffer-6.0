
Problem Statement Description: 
In many rural or low-connectivity regions, traditional digital payment systems fail due to their reliance on internet access. This limits users from making quick, secure transactions when network coverage is poor or unavailable. Additionally, existing systems often involve complex user interfaces and pose risks of unauthorized access or fraud. There is a pressing need for a simple, secure, and efficient payment solution that works even in offline environments. 

Solution Proposed: 

Our project presents a secure offline peer-to-peer payment system that allows users to send and receive money without internet connectivity. It leverages local data storage, encrypted user authentication using hashed passwords and UPI PIN verification, and dynamically generated QR codes for transaction requests. Priority-based transaction handling using a heap ensures efficient processing. The system also includes features like transaction history, balance checks, and fraud prevention, making it both user-friendly and robust in offline settings. 

Data Structures Used: 

1. HashMap 
Stores user details (user_id, upi_id, balance, etc.) for constant-time access. 
Manages server and local user databases. 
Used to keep track of pending and completed transactions separately with unique transaction IDs as keys. 

2. Stack 
Maintains transaction history per user in LIFO order. 
Useful for showing recent transactions and enabling quick reversal if needed. 

3. Priority Queue (Max Heap) 
Handles transaction processing based on defined priorities (e.g., time or amount based(larger given higher,smaller-lower)). 
Ensures high-priority offline payments are processed first. 

4. ArrayList 
Stores dynamic lists like users, recent UPI records, or contributions. 
Facilitates easy iteration, addition, and removal. 


 Algorithms Used: 

 1. AES (Advanced Encryption Standard) 
Used for symmetric encryption to secure sensitive transaction data. AES ensures that only authorized users can decrypt and access transaction details, maintaining confidentiality and data integrity. 

2. SHA (Secure Hash Algorithm) 
Utilized to hash user passwords and transaction IDs. SHA ensures data integrity by producing a fixed-size hash value that makes it computationally infeasible to reverse-engineer the original data. 

3. QR Code Generation (QR Code Writer Algorithm) 
Uses the QR Code Writer class to encode transaction details (e.g., UPI ID, transaction amount) into a QR code. This QR code can be scanned to facilitate offline peer-to-peer transactions without requiring an internet connection. 

 Video link: https://drive.google.com/file/d/1WOc2hOydTWkUQF8Edbpr-defkggVpOv6/view?usp=drive_link

 
