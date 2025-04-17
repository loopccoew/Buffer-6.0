package loop.demo;

import java.util.ArrayList;
import java.util.List;

public class ReconstructionTheory {
    private String id;
    private String name;
    private String scholar;
    private String date;
    private String description;
    private List<TextVersion> versions; // The versions associated with this theory
    private List<Relationship> relationships; // Relationships specific to this theory
    private List<ReconstructionTheory> children; // List to hold child theories

    // Constructor
    public ReconstructionTheory(String id, String name, String scholar, 
                               String date, String description) {
        this.id = id;
        this.name = name;
        this.scholar = scholar;
        this.date = date;
        this.description = description;
        this.versions = new ArrayList<>(); // Initialize the versions list
        this.relationships = new ArrayList<>(); // Initialize the relationships list
        this.children = new ArrayList<>(); // Initialize the children list
    }
    
    public void addVersion(TextVersion version) {
        versions.add(version);
    }

    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    public void addChildTheory(ReconstructionTheory child) {
        children.add(child);
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getScholar() { return scholar; }
    public void setScholar(String scholar) { this.scholar = scholar; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<TextVersion> getVersions() { return versions; }
    public List<Relationship> getRelationships() { return relationships; }
    public List<ReconstructionTheory> getChildren() { return children; }
}