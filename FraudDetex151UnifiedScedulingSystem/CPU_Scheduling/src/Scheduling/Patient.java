package Scheduling;

public class Patient {
    private String name;
    private String symptom;
    private int severity;
    private int arrivalOrder;

    // Constructor
    public Patient(String name, String symptom, int severity) {
        this.name = name;
        this.symptom = symptom;
        this.severity = severity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSymptom() {
        return symptom;
    }

    public int getSeverity() {
        return severity;
    }

    public int getArrivalOrder() {
        return arrivalOrder;
    }

    // Setter
    public void setArrivalOrder(int arrivalOrder) {
        this.arrivalOrder = arrivalOrder;
    }

    @Override
    public String toString() {
        return name + " - " + symptom + " (Severity: " + severity + ")";
    }
}