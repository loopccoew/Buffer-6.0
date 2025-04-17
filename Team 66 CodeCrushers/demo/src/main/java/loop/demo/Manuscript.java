package loop.demo;
public class Manuscript {
    private String id;
    private String name;
    private String content;
    private String versionId;
    private String author;
    private String date;

    // Updated constructor with more arguments
    public Manuscript(String id, String name, String content, String versionId, String author, String date) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.versionId = versionId;
        this.author = author;
        this.date = date;
    }

    // Getters and Setters for all properties
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Manuscript{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", versionId='" + versionId + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
