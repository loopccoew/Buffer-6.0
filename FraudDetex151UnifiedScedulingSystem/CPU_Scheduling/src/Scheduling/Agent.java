package Scheduling;

public class Agent {
    private String name;
    private String skill;
    private boolean available;

    public Agent(String name, String skill, boolean available) {
        this.name = name;
        this.skill = skill;
        this.available = available;
    }

    public String getName() {
        return name;
    }
    

    public String getSkill() {
        return skill;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", skill='" + skill + '\'' +
                ", available=" + available +
                '}';
    }
}