public class RiskProfileSimulator {

    static class UserProfile {
        int age, savings, income, numDependents;
        boolean hasLoans;
        int investmentKnowledge;

        UserProfile(int age, int savings, int income, boolean hasLoans, int numDependents, int investmentKnowledge) {
            this.age = age;
            this.savings = savings;
            this.income = income;
            this.hasLoans = hasLoans;
            this.numDependents = numDependents;
            this.investmentKnowledge = investmentKnowledge;
        }
    }

    public static String determineRiskType(UserProfile profile) {
        if (profile.age < 30 && profile.savings > 50000 && !profile.hasLoans) {
            return "Aggressive Investor";
        } else if (profile.age >= 30 && profile.age <= 50 && profile.income > 30000) {
            return "Balanced Investor";
        } else {
            return "Conservative Investor";
        }
    }
}