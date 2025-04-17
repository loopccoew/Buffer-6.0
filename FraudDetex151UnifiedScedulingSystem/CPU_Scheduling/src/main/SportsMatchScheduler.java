package main;

import java.util.*;

public class SportsMatchScheduler {
    private List<Queue<String>> rounds;
    private Set<String> matchHistory;

    public SportsMatchScheduler() {
        rounds = new ArrayList<>();
        matchHistory = new HashSet<>();
    }

    public void scheduleRoundRobin(List<String> teams) {
        System.out.println("Scheduling Round Robin Matches:");
        int n = teams.size();
        for (int i = 0; i < n; i++) {
       4     for (int j = i + 1; j < n; j++) {
                String match = teams.get(i) + " vs " + teams.get(j);
                if (!matchHistory.contains(match)) {
                    matchHistory.add(match);
                    Queue<String> round = new LinkedList<>();
                    round.add(match);
                    rounds.add(round);
                }
            }
        }
        printSchedule();
    }

    public void scheduleKnockout(List<String> teams) {
        System.out.println("Scheduling Knockout Matches:");
        Queue<String> matches = new LinkedList<>();
        for (int i = 0; i < teams.size(); i += 2) {
            if (i + 1 < teams.size()) {
                matches.add(teams.get(i) + " vs " + teams.get(i + 1));
            } else {
                matches.add(teams.get(i) + " advances (no opponent)");
            }
        }
        rounds.add(matches);
        printSchedule();
    }

    private void printSchedule() {
        for (int i = 0; i < rounds.size(); i++) {
            System.out.println("Round " + (i + 1) + ":");
            for (String match : rounds.get(i)) {
                System.out.println(" - " + match);
            }
        }
    }

    public void run(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Sports Match Scheduler ---");
            System.out.println("1. Schedule Round Robin");
            System.out.println("2. Schedule Knockout");
            System.out.println("3. Clear Schedule");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (option == 1 || option == 2) {
                if (!rounds.isEmpty()) {
                    System.out.print("Previous schedule exists. Clear it before proceeding? (yes/no): ");
                    String ans = scanner.nextLine().trim().toLowerCase();
                    if (ans.equals("yes")) {
                        rounds.clear();
                        matchHistory.clear();
                        System.out.println("Previous schedule cleared.");
                    } else {
                        System.out.println("Returning to menu...");
                        continue;
                    }
                }

                System.out.print("Enter number of teams: ");
                int n = scanner.nextInt();
                scanner.nextLine();

                if (n <= 1) {
                    System.out.println("At least 2 teams are required.");
                    continue;
                }

                if (n > 1000) {
                    System.out.print("You entered a large number of teams (" + n + "). Continue? (yes/no): ");
                    if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) continue;
                }

                Set<String> teamSet = new HashSet<>();
                List<String> teams = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    System.out.print("Enter name for team " + (i + 1) + ": ");
                    String name = scanner.nextLine().trim();

                    if (name.isEmpty() || !name.matches("[A-Za-z0-9 _-]+")) {
                        System.out.println("Invalid name. Use letters, numbers, spaces, underscores or hyphens.");
                        i--; // retry input
                        continue;
                    }

                    if (teamSet.contains(name)) {
                        System.out.println("Duplicate name. Enter a unique team name.");
                        i--; // retry input
                        continue;
                    }

                    teamSet.add(name);
                    teams.add(name);
                }

                if (option == 1) {
                    scheduleRoundRobin(teams);
                } else {
                    scheduleKnockout(teams);
                }

            } else if (option == 3) {
                rounds.clear();
                matchHistory.clear();
                System.out.println("Schedule cleared.");
            } else if (option == 0) {
                back = true;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }
}
