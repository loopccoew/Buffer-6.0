package Scheduling;

import java.util.Date;

public class Job {
    private String id;
    private String name;
    private Date timestamp;

    public Job(String id, String name, Date timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Date getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Job[ID=" + id + ", Name=" + name + ", Time=" + timestamp + "]";
    }
}
