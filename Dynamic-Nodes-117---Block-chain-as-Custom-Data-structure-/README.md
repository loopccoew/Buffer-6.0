# *Buffer 6.0*

## *Blockchain as a Data Structure in Java*
This project implements a generic blockchain in Java, designed to understand and experiment with blockchain as a data structure. It includes core concepts such as hashing, proof-of-work, and chain validation, and supports multiple real-world applications using the same blockchain base.

---

## *Problem Statement*
Traditional data structures like arrays and linked lists provide linear or hierarchical storage but lack features like immutability, tamper detection, and integrity validation.  
This project proposes a custom blockchain data structure that extends the functionality of traditional data structures by linking blocks using cryptographic hashes and enforcing validation rules, thereby ensuring trust and traceability.

---

## *Project Description*
This project is a simplified implementation of a blockchain-based cryptocurrency using Java. It demonstrates the fundamental principles of blockchain technology including cryptographic hashing, proof-of-work, and block immutability.  
Each block in the blockchain contains a cryptographic hash, a reference to the previous block's hash (creating a linked structure), transaction data, and a timestamp.  
The primary goal of this project is to simulate how data integrity and consensus are achieved in decentralized systems like cryptocurrencies (e.g., Bitcoin) using core Java and object-oriented programming concepts.

---

## *Project Structure*
```
BlockPackage/
├── Block.java          // Represents each block in the chain
├── Blockchain.java     // Handles chain logic, mining, validation
├── Utils.java          // Provides SHA-256 hashing
Applications/
├── supplychain/        // Product flow & authenticity tracking
├── expense/            // Decentralized expense logging
├── certificates/       // Certificate verification system
├── crypto/             // Basic cryptocurrency simulation
```

---

## *Demo Video & Reports*
- *Project Video Demo* –    https://drive.google.com/drive/folders/1HbWZuEinzNpNn8Q-aTtlZpRE8D5Go2Mo?usp=sharing 
- *Final Project Report* –  https://drive.google.com/file/d/1Q0ms8zrkBOc5x9t0xftDzBeEr4rAxmcB/view?usp=sharing
---

## *Core Features*
- Generic Block Support (<T>)  
- Proof of Work (PoW) mining  
- Blockchain Validation  
- Data Pool and Block Mining  
- Modular & Extendable for real-world applications  

---

## *Data Structures Used*

**ArrayList (ArrayList<Block>)**  
Used to maintain the blockchain — a dynamic list of blocks. Each block is appended sequentially. Enables quick access and iteration for chain validation or ledger display.

*String & StringBuilder*  
Used for cryptographic operations like generating SHA-256 hashes.

*Custom Block Class (OOP Structure)*  
Each block encapsulates its own data, previous hash, current hash, timestamp, and nonce.

---

## *Blockchain Core*

### *Block<T>*
- Stores any type of data.  
- Computes hash using:  
  java
  Utils.applySHA256(index + previousHash + timestamp + data + nonce);
  

### *Proof of Work*
- mineBlock(int difficulty) changes the nonce until the hash meets difficulty.  
- Ensures computational effort for block creation.

### *Chain Validation*
- isChainValid() ensures:
  - No tampering with block hashes.  
  - Correct linkage to previous blocks.

---


## *Applications*

### *1. Supply Chain Tracking*
- Each product movement is recorded as a block.  
- Tracks goods from origin to destination.  
- Increases transparency and prevents fraud.

### *2. Expense Tracking*
- Logs personal or team expenses block by block.  
- Tamper-proof financial history.

### *3. Certificate Verification*
- Stores academic or official certificates on-chain.  
- Validates authenticity using hash comparison.

### *4. Cryptocurrency Simulation*
- Basic crypto system built on the blockchain logic.  
- Supports transaction data, mining, and balances.

> 🔧 All applications use the same blockchain engine but customize the **data structure (<T>)** in each block.

--- 
