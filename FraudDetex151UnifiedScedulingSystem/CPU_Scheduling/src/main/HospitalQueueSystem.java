package main;

import Scheduling.Patient;
import java.util.*;

public class HospitalQueueSystem {
    private PriorityQueue<Patient> patientQueue;

    public HospitalQueueSystem() {
        patientQueue = new PriorityQueue<>((a, b) -> {
            int cmp = Integer.compare(b.getSeverity(), a.getSeverity());
            return cmp != 0 ? cmp : Integer.compare(a.getArrivalOrder(), b.getArrivalOrder());
        });
    }

    private int arrivalCounter = 0;

    public void addPatient(Patient patient) {
        patient.setArrivalOrder(arrivalCounter++);
        patientQueue.add(patient);
        System.out.println("[INFO] Added Patient: " + patient);
    }

    public void attendNextPatient() {
        if (!patientQueue.isEmpty()) {
            Patient patient = patientQueue.poll();
            System.out.println("[INFO] Attending Patient: " + patient);
        } else {
            System.out.println("[WARNING] No patients in queue.");
        }
    }

    public void run(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Hospital Queue System ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Attend Next Patient");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choiceStr = scanner.nextLine().trim();
            if (choiceStr.equals("1")) {
                System.out.print("Enter patient name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("[ERROR] Patient name cannot be empty.");
                    continue;
                }

                System.out.print("Enter symptom (e.g., Fever, Headache): ");
                String symptom = scanner.nextLine().trim();
                if (symptom.isEmpty()) {
                    System.out.println("[ERROR] Symptom cannot be empty.");
                    continue;
                }

                System.out.print("Enter severity (1-10): ");
                String severityStr = scanner.nextLine().trim();

                int severity;
                try {
                    severity = Integer.parseInt(severityStr);
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Severity must be a number between 1 and 10.");
                    continue;
                }

                if (severity < 1 || severity > 10) {
                    System.out.println("[ERROR] Severity must be between 1 and 10.");
                    continue;
                }

                addPatient(new Patient(name, symptom, severity));
            } else if (choiceStr.equals("2")) {
                attendNextPatient();
            } else if (choiceStr.equals("0")) {
                back = true;
            } else {
                System.out.println("[ERROR] Invalid option. Try again.");
            }
        }
    }
}