# Buffer 6.0

## Blockchain as a Data Structure in Java
This project implements a generic blockchain in Java, designed to understand and experiment with blockchain as a data structure. It includes core concepts such as hashing, proof-of-work, and chain validation, and supports multiple real-world applications using the same blockchain base.

---

## Problem Statement
Traditional data structures like arrays and linked lists provide linear or hierarchical storage but lack features like immutability, tamper detection, and integrity validation.  
This project proposes a custom blockchain data structure that extends the functionality of traditional data structures by linking blocks using cryptographic hashes and enforcing validation rules, thereby ensuring trust and traceability.

---

## Project Description
This project is a simplified implementation of a blockchain-based cryptocurrency using Java. It demonstrates the fundamental principles of blockchain technology including cryptographic hashing, proof-of-work, and block immutability.  
Each block in the blockchain contains a cryptographic hash, a reference to the previous block's hash (creating a linked structure), transaction data, and a timestamp.  
The primary goal of this project is to simulate how data integrity and consensus are achieved in decentralized systems like cryptocurrencies (e.g., Bitcoin) using core Java and object-oriented programming concepts.

---

## Project Structure

BlockPackage/
├── Block.java          // Represents each block in the chain
├── Blockchain.java     // Handles chain logic, mining, validation
├── Utils.java          // Provides SHA-256 hashing
Applications/
├── supplychain/        // Product flow & authenticity tracking
├── expense/            // Decentralized expense logging
├── certificates/       // Certificate verification system
├── crypto/             // Basic cryptocurrency simulation


---

## Demo Video & Reports
- Project Video Demo –    https://drive.google.com/file/d/18h2uRusag_oi7VAsteyySPQ83SuVp6xR/view?usp=sharing
- Final Project Report –  https://drive.google.com/file/d/1UMfviQPBiczd6AdE-jqbNqLvBP4wqKmW/view?usp=sharing
---

## Core Features
- Generic Block Support (<T>)  
- Proof of Work (PoW) mining
- Proof of Stake(PoS)  
- Blockchain Validation  
- Data Pool and Block Mining  
- Modular & Extendable for real-world applications  

---

## Data Structures Used
*ArrayList (ArrayList<Product>, ArrayList<Block<Product>>)*
Used to store uncommitted shipment entries and maintain the blockchain ledger. Enables dynamic resizing and fast iteration for adding, editing, and committing data.

*HashSet (Set<String>)*
Used in listAllIds() to collect and display unique product IDs without duplicates.

*HashMap (Map<String, Product>)*
Used in liveStatus() to map each product ID to its latest status, replacing older entries and ensuring the most recent information is retained.

*String & StringBuilder*
Used throughout the application for constructing messages, handling product attributes, and likely in the hashing process within Blockchain and Block classes (especially for SHA-256 generation).

*OOP Principles (Encapsulation, Abstraction, Generics)*
Used extensively to structure the system into reusable and modular components (LogisticsTracker, Blockchain, Block, and Product).
---

## Blockchain Core

### Block<T>
- Stores any type of data.  
- Computes hash using:  
  java
  Utils.applySHA256(index + previousHash + timestamp + data + nonce);
  

### Proof of Work
- mineBlock(int difficulty) changes the nonce until the hash meets difficulty.  
- Ensures computational effort for block creation.

### Proof of Stake
- Proof of Stake (PoS) selects block validators based on their ownership or "stake" in the network, not computational power. The more coins a participant holds, the higher their chances of being chosen to validate the next block.

---


## Applications

### 1. Supply Chain Tracking
- Each product movement is recorded as a block.  
- Tracks goods from origin to destination.  
- Increases transparency and prevents fraud.

### *2. Voting application *
- Each vote is a data entry inside a block.
- Immutable storage prevents vote tampering.
- Ensures voter anonymity and traceability. 

### 3. Certificate Verification
- Stores academic or official certificates on-chain.  
- Validates authenticity using hash comparison.

### 4. Cryptocurrency Simulation
- Basic crypto system built on the blockchain logic.  
- Supports transaction data, mining, and balances.

### Please Note: 
-This project involves multiple custom packages and files. Before compilation on a different system, adjust the package declarations and import statements to match your local folder structure.

---