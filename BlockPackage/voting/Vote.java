package BlockPackage.voting;

public class Vote {
    public String voterId;
    public String candidateName;
    public String constituency;

    public Vote(String voterId, String candidateName, String constituency) {
        this.voterId = voterId;
        this.candidateName = candidateName;
        this.constituency = constituency;
    }

    @Override
    public String toString() {
        return "VoterID: " + voterId + ", Candidate: " + candidateName + ", Constituency: " + constituency;
    }
}
