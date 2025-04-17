## Domain: Next-Gen Academic Solutions

### Problem Statement:

We have developed an intelligent recommendation system, primarily based on a decision tree, that assists individuals—be it students uncertain about their future, working professionals exploring a career transition, or anyone seeking direction—in identifying career paths that best align with their current skills and interests.

Beyond just suggesting suitable careers, our solution also recommends targeted courses that are essential for success in the chosen field. Each course recommendation is accompanied by its prerequisites, enabling users to clearly understand and navigate the learning path required to thrive in their desired career.

### Data Structures used:

#### 1. DecisionTree
The DecisionTree uses a recursive node structure where each node stores:
- **Leaf Status**: Whether the node is a leaf or decision node.
- **Label/Feature**: For leaf nodes, it holds the class label; for decision nodes, it holds the feature used for splitting.
- **Children**: The child nodes representing further data splits.

#### 2. Map<String, DecisionTree> (HashMap)
This HashMap stores child nodes based on feature values:
- The **key** is a String representing the feature value.
- The **value** is a DecisionTree instance for that feature value.

#### 3. 2D Array of Doubles (Training Data)
A 2D array of doubles stores training data, where each row represents user responses. Values are:
- `1`: Positive response.
- `0`: Negative response.
- `0.5`: Neutral response.

#### 4. Map<Double, Integer>
This HashMap counts occurrences of class labels for entropy and majority label calculations:
- The **key** is a Double representing the class label.
- The **value** is the count of that label.


### Working Demonstration Of Project
https://github.com/user-attachments/assets/862b5c88-1c2e-42f8-a19f-0d4fb5da4168


