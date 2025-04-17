package loop.demo;

public class TextVersion {
    private String id;
    private String name;
    private String region;
    private String approximateDate;
    private int verseCount;
    private String uniqueFeatures;
    private double completeness; // 0.0 to 1.0

    // Constructor
    public TextVersion(String id, String name, String region, String approximateDate, 
                      int verseCount, String uniqueFeatures, double completeness) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.approximateDate = approximateDate;
        this.verseCount = verseCount;
        this.uniqueFeatures = uniqueFeatures;
        this.completeness = completeness;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getApproximateDate() { return approximateDate; }
    public void setApproximateDate(String approximateDate) { this.approximateDate = approximateDate; }

    public int getVerseCount() { return verseCount; }
    public void setVerseCount(int verseCount) { this.verseCount = verseCount; }

    public String getUniqueFeatures() { return uniqueFeatures; }
    public void setUniqueFeatures(String uniqueFeatures) { this.uniqueFeatures = uniqueFeatures; }

    public double getCompleteness() { return completeness; }
    public void setCompleteness(double completeness) { this.completeness = completeness; }

    @Override
    public String toString() {
        return name + " (" + region + ", " + approximateDate + ")";
    }
}
