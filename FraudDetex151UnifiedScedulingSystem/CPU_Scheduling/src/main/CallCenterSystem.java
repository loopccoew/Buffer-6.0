package main;

import Scheduling.Agent;
import java.util.*;

public class CallCenterSystem {
    private Map<String, Queue<Agent>> skillAgents;

    public CallCenterSystem() {
        skillAgents = new HashMap<>();
    }

    public void addAgent(String skill, Agent agent) {
        skill = skill.toLowerCase(); // Normalize skill for case-insensitive match
        skillAgents.computeIfAbsent(skill, k -> new LinkedList<>()).add(agent);
        System.out.println("Added Agent " + agent.getName() + " with skill " + skill);
    }

    public void routeCall(String skillRequired) {
        skillRequired = skillRequired.toLowerCase();
        if (skillAgents.isEmpty()) {
            System.out.println("No agents exist in the system.");
            return;
        }

        Queue<Agent> agents = skillAgents.getOrDefault(skillRequired, new LinkedList<>());
        if (agents.isEmpty()) {
            System.out.println("No available agents with skill: " + skillRequired);
            return;
        }

        Agent agent = agents.poll(); // remove from front
        if (agent.isAvailable()) {
            agent.setAvailable(false);
            System.out.println("Call routed to agent: " + agent.getName() + " (Skill: " + skillRequired + ")");
        } else {
            System.out.println("Agent " + agent.getName() + " is currently busy, escalating call.");
        }

        agents.add(agent); // round-robin: push agent to back whether available or not
    }

    public void run(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Call Center Queue System ---");
            System.out.println("1. Add Agent");
            System.out.println("2. Route Call");
            System.out.println("3. Show All Agents");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                System.out.print("Enter agent name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Enter agent skill: ");
                String skill = scanner.nextLine().trim();

                // Edge Case 4: Validate name and skill
                if (name.isEmpty() || !name.matches("[A-Za-z0-9 _-]+")) {
                    System.out.println("Invalid agent name.");
                    continue;
                }
                if (skill.isEmpty() || !skill.matches("[A-Za-z]+")) {
                    System.out.println("Invalid skill. Only alphabetic skills allowed.");
                    continue;
                }

                Agent agent = new Agent(name, skill, true);
                addAgent(skill, agent);

            } else if (choice == 2) {
                System.out.print("Enter required skill for call routing: ");
                String requiredSkill = scanner.nextLine().trim();

                if (requiredSkill.isEmpty()) {
                    System.out.println("Skill is required for routing.");
                    continue;
                }

                routeCall(requiredSkill);

            } else if (choice == 3) {
                System.out.println("Current Agent List:");
                if (skillAgents.isEmpty()) {
                    System.out.println("No agents available.");
                } else {
                    for (String skill : skillAgents.keySet()) {
                        System.out.println("Skill: " + skill);
                        for (Agent agent : skillAgents.get(skill)) {
                            System.out.println(" - " + agent.getName() + " (Available: " + agent.isAvailable() + ")");
                        }
                    }
                }

            } else if (choice == 0) {
                back = true;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }
}
