package dsa_modules;

public class Match {
    private int borrowerId;
    private int lenderId;
    private double interestRate;

    // Constructor with parameters
    public Match(int borrowerId, int lenderId, double interestRate) {
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.interestRate = interestRate;
    }

    // Getters
    public int getBorrowerId() {
        return borrowerId;
    }

    public int getLenderId() {
        return lenderId;
    }

    public double getInterestRate() {
        return interestRate;
    }

    // Setters
    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    // Add toString method for debugging/logging purposes
    @Override
    public String toString() {
        return "Match [borrowerId=" + borrowerId + ", lenderId=" + lenderId + ", interestRate=" + interestRate + "]";
    }
}

