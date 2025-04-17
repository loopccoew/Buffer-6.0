package Scheduling;

public class Driver {
    private String name;
    private String location;
    private boolean available;

    public Driver(String name, String location, boolean available) {
        this.name = name;
        this.location = location;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", available=" + available +
                '}';
    }
}
