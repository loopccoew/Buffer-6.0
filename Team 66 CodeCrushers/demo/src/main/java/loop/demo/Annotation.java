package loop.demo;

public class Annotation {
    private String id;
    private String content;
    private String author;
    private String targetId;
    private String targetType;

    // Constructor
    public Annotation(String id, String content, String author, String targetId, String targetType) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.targetId = targetId;
        this.targetType = targetType;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
}
