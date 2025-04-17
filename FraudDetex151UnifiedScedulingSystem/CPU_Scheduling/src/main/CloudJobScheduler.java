package main;

import Scheduling.Job;

import java.util.*;

public class CloudJobScheduler {
    
    private Map<String, Job> scheduledJobs;

    public CloudJobScheduler() {
        scheduledJobs = new LinkedHashMap<>();
    }

    public void addJob(Job job) {
        if (job.getName() == null || job.getName().trim().isEmpty()) {
            System.out.println("[ERROR] Job name cannot be empty.");
            return;
        }

        if (scheduledJobs.containsKey(job.getId())) {
            System.out.println("[ERROR] Duplicate job ID not allowed: " + job.getId());
            return;
        }

        if (job.getTimestamp().before(new Date())) {
            System.out.println("[ERROR] Cannot schedule job in the past: " + job.getTimestamp());
            return;
        }

        scheduledJobs.put(job.getId(), job);
        System.out.println("[INFO] Scheduled job: " + job);
    }

    public void cancelJob(String jobId) {
        if (scheduledJobs.remove(jobId) != null) {
            System.out.println("[INFO] Job cancelled: " + jobId);
        } else {
            System.out.println("[INFO] No job found with ID: " + jobId);
        }
    }

    public void printSchedule() {
        System.out.println("\n[STATE] Scheduled Jobs:");
        if (scheduledJobs.isEmpty()) {
            System.out.println("No jobs scheduled.");
        } else {
            for (Job job : scheduledJobs.values()) {
                System.out.println(job);
            }
        }
    }

    public void run(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Cloud Job Scheduler ---");
            System.out.println("1. Add Job");
            System.out.println("2. Cancel Job");
            System.out.println("3. Show All Jobs");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int option;
            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] Invalid input.");
                scanner.nextLine();
                continue;
            }

            switch (option) {
                case 1:
                    System.out.print("Enter Job ID: ");
                    String id = scanner.nextLine().trim();

                    System.out.print("Enter Job Name: ");
                    String name = scanner.nextLine().trim();

                    System.out.print("Enter execution time (yyyy-MM-dd HH:mm): ");
                    String timeStr = scanner.nextLine().trim();

                    try {
                        Date timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeStr);
                        Job job = new Job(id, name, timestamp);
                        addJob(job);
                    } catch (Exception e) {
                        System.out.println("[ERROR] Invalid date format.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Job ID to cancel: ");
                    String cancelId = scanner.nextLine().trim();
                    cancelJob(cancelId);
                    break;

                case 3:
                    printSchedule();
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("[ERROR] Invalid option.");
            }
        }
    }
}
