package loop.demo;

public class Relationship {
    private String id;
    private TextVersion source;
    private TextVersion target;
    private String type; // "regional variant", "derived from", etc.
    private double similarity; // 0.0 to 1.0
    private String notes;

    // Constructor
    public Relationship(String id, TextVersion source, TextVersion target, 
                       String type, double similarity, String notes) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        this.similarity = similarity;
        this.notes = notes;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TextVersion getSource() { return source; }
    public void setSource(TextVersion source) { this.source = source; }

    public TextVersion getTarget() { return target; }
    public void setTarget(TextVersion target) { this.target = target; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getSimilarity() { return similarity; }
    public void setSimilarity(double similarity) { this.similarity = similarity; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return source.getName() + " " + type + " " + target.getName() + 
               " (similarity: " + similarity + ")";
    }
}
