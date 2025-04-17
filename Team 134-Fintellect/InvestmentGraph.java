import java.util.*;

class InvestmentGraph {
    private Map<String, List<String>> goalDependencies = new HashMap<>();
    private Map<String, Integer> goalPriorities = new HashMap<>();
    private PriorityQueue<String> priorityQueue;

    public InvestmentGraph() {
        priorityQueue = new PriorityQueue<>((a, b) -> goalPriorities.get(b) - goalPriorities.get(a));
    }

    public void addGoal(String goal, int priority) {
        if (!goalDependencies.containsKey(goal)) {
            goalDependencies.put(goal, new ArrayList<>());
        }
        goalPriorities.put(goal, priority);
        priorityQueue.add(goal);
    }

    public void addDependency(String goal, String dependency) {
        goalDependencies.putIfAbsent(dependency, new ArrayList<>());
        goalDependencies.get(dependency).add(goal);
    }

    public void displayGoalsByPriority() {
        System.out.println("Goals sorted by priority:");
        while (!priorityQueue.isEmpty()) {
            String goal = priorityQueue.poll();
            System.out.println(goal + " (Priority: " + goalPriorities.get(goal) + ")");
        }
    }

    public void displayGoalOrder() {
        Map<String, Integer> indegree = new HashMap<>();
        for (String goal : goalDependencies.keySet()) {
            indegree.put(goal, 0);
        }
        for (List<String> dependencies : goalDependencies.values()) {
            for (String dependent : dependencies) {
                indegree.put(dependent, indegree.getOrDefault(dependent, 0) + 1);
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (String goal : indegree.keySet()) {
            if (indegree.get(goal) == 0) {
                queue.add(goal);
            }
        }

        System.out.println("Goal Completion Order:");
        while (!queue.isEmpty()) {
            String current = queue.poll();
            System.out.println(current);
            for (String dependent : goalDependencies.getOrDefault(current, new ArrayList<>())) {
                indegree.put(dependent, indegree.get(dependent) - 1);
                if (indegree.get(dependent) == 0) {
                    queue.add(dependent);
                }
            }
        }
    }
}