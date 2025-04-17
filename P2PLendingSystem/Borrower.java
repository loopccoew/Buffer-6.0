package dsa_modules;

public class Borrower {
    private int id;
    private String name;
    private int creditScore;
    private int requestedAmount;
    private double requestedRate;
    public Borrower left, right;

    /**
     * Full constructor including all fields.
     */
    public Borrower(int id, String name, int creditScore, int requestedAmount, double requestedRate) {
        this.id = id;
        this.name = name;
        this.creditScore = creditScore;
        this.requestedAmount = requestedAmount;
        this.requestedRate = requestedRate;
        this.left = null;
        this.right = null;
    }

    /**
     * Constructor for DAO usage when only id, name, and creditScore are known.
     * requestedAmount and requestedRate default to 0.
     */
    public Borrower(int id, String name, int creditScore) {
        this(id, name, creditScore, 0, 0.0);
    }

    /**
     * Constructor for in-memory creation before DB assignment of ID.
     */
    public Borrower(String name, int creditScore, int requestedAmount, double requestedRate) {
        this(0, name, creditScore, requestedAmount, requestedRate);
    }

    // ID getter/setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Name getter/setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Credit score getter/setter
    public int getCreditScore() {
        return creditScore;
    }
    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    // Requested amount getter/setter
    public int getRequestedAmount() {
        return requestedAmount;
    }
    public void setRequestedAmount(int requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    // Requested rate getter/setter
    public double getRequestedRate() {
        return requestedRate;
    }
    public void setRequestedRate(double requestedRate) {
        this.requestedRate = requestedRate;
    }
}
