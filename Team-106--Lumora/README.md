# Lumora

### Project info
An interactive **educational website** that delivers essential sexual education through fun, engaging gameplay — all while integrating **core Data Structures & Algorithms (DSA)** concepts.

**WEB URL**: https://buffer20.vercel.app/

**DEMO URL**: https://drive.google.com/file/d/1lZiausc5b6QeAMKWrm5-Z-LeL0SIHueh/view?usp=sharing

### **Project Highlights**

- 4 Topics, each with **10 Levels** of increasing difficulty
- **Educational Content** and feedback for every wrong answer
- **Coin Rewards** for correct answers
- Retry system for progression
- **Performance tracking**, leaderboard, and story mode

### **DSA Concepts Integrated**

This project combines learning with real-world DSA usage:

**1. Binary Tree – Level Unlocking**
- *How it works*: Each level is a node; you can unlock the next level only by completing the current one.
- *DSA*: Binary Tree Preorder Traversal (parent → left → right)

**2. Stack – Navigation History**
- *How it works*: Tracks previously visited questions for back navigation.
- *DSA*: Stack (LIFO) mimics browser-style navigation.

**3. Queue – Timed Question Buffer**
- *How it works*: Questions are served in order, with timers.
- *DSA*: Queue or Circular Queue for FIFO flow control.

**4. Heap – Leaderboard**
- *How it works*: Tracks top 10 users by coin score.
- *DSA*: Min/Max Heap for efficient top-k user tracking.

**5. HashMap – Performance Stats**
- *How it works*: Tracks user accuracy, attempts, speed by topic.
- *DSA*: Hash Table enables fast stat access and updates.

**6. Decision Tree – Story Mode**
- *How it works*: Interactive scenarios with multiple paths based on user choices.
- *DSA*: Tree traversal through branching narrative paths.

### **What technologies are used for this project?**

This project is built with .

- *Frontend*: React + TypeScript
- *Styling*: TailwindCSS
- *Bundler*: Vite
- *State Management*: Context API
- *Optional*: Firebase (for user auth and data)

### **How to run this project?**

```sh
# Step 1: Clone the repository using the project's Git URL.
git clone <GIT_URL>

# Step 2: Navigate to the project directory.
cd <PROJECT_NAME>

# Step 3: Install the necessary dependencies.
npm i

# Step 4: Start the development server with auto-reloading and an instant preview.
npm run dev
```


