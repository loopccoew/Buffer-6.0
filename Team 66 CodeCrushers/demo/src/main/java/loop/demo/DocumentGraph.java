package loop.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

public class DocumentGraph {
    private String id; // Unique identifier for the graph
    private Map<String, TextVersion> versions;
    private List<Relationship> relationships;
    private Map<String, Annotation> annotations;
    private Map<String, Manuscript> manuscripts;
    private List<ReconstructionTheory> theories; // List to hold theories

    // Constructor
    public DocumentGraph(String id) {
        this.id = id;
        this.versions = new HashMap<>();
        this.relationships = new ArrayList<>();
        this.annotations = new HashMap<>();
        this.manuscripts = new HashMap<>();
        this.theories = new ArrayList<>(); // Initialize the theories list
    }

    // Methods to add items
    public void addTheory(ReconstructionTheory theory) {
        theories.add(theory);
    }

    public List<ReconstructionTheory> getAllTheories() {
        return theories;
    }
    // Getter for ID
    public String getId() {
        return id;
    }

    // Methods to add items
    public void addVersion(TextVersion version) {
        versions.put(version.getId(), version);
    }
    
    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }
    
    public void addAnnotation(Annotation annotation) {
        annotations.put(annotation.getId(), annotation);
    }
    
    public void addManuscript(Manuscript manuscript) {
        manuscripts.put(manuscript.getId(), manuscript);
    }
    
    
    // Methods to retrieve items
    public TextVersion getVersion(String id) {
        return versions.get(id);
    }
    
    public List<TextVersion> getAllVersions() {
        return new ArrayList<>(versions.values());
    }
    
    // Methods to find related items
    public List<Relationship> getRelationshipsForVersion(String versionId) {
        List<Relationship> result = new ArrayList<>();
        for (Relationship rel : relationships) {
            if (rel.getSource().getId().equals(versionId) || 
                rel.getTarget().getId().equals(versionId)) {
                result.add(rel);
            }
        }
        return result;
    }
    
    public List<Annotation> getAnnotationsForVersion(String versionId) {
        List<Annotation> result = new ArrayList<>();
        for (Annotation anno : annotations.values()) {
            if (anno.getTargetId().equals(versionId) && 
                anno.getTargetType().equals("version")) {
                result.add(anno);
            }
        }
        return result;
    }
    
  
}