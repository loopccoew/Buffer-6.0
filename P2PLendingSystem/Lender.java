package dsa_modules;

public class Lender {
    private int id;
    private String name;
    private int preferredRate;  // Changed from double to int
    private int maxAmount;      // Changed from double to int

    public Lender(int id, String name, int preferredRate, int maxAmount) {
        this.id = id;
        this.name = name;
        this.preferredRate = preferredRate;
        this.maxAmount = maxAmount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPreferredRate() {
        return preferredRate;  // Returns int
    }

    public int getMaxAmount() {
        return maxAmount;      // Returns int
    }

    // Setters (if needed)
    public void setName(String name) {
        this.name = name;
    }

    public void setPreferredRate(int preferredRate) {
        this.preferredRate = preferredRate;  // Accepts int
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;          // Accepts int
    }

    // Add setId method for auto-generated id from the database
    public void setId(int id) {
        this.id = id;
    }
}
