package Scheduling;

public class Order implements Comparable<Order> {
    private String name;      // Order item name
    private int table;        // Table number
    private int preparationTime; // Prep time in minutes

    public Order(String name, int table, int preparationTime) {
        this.name = name;
        this.table = table;
        this.preparationTime = preparationTime;
    }

    public String getName() {
        return name;
    }

    public int getTable() {
        return table;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    @Override
    public int compareTo(Order other) {
        return Integer.compare(this.preparationTime, other.preparationTime);
    }

    @Override
    public String toString() {
        return "Order{" + 
                "name='" + name + '\'' +
                ", table=" + table +
                ", prepTime=" + preparationTime + " mins}";
    }
}