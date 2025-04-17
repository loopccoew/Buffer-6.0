package Scheduling;



public class Match {
 private String teamA;
 private String teamB;
 private String round;
 private boolean isPlayed;
 private String winner;

 public Match(String teamA, String teamB, String round) {
     this.teamA = teamA;
     this.teamB = teamB;
     this.round = round;
     this.isPlayed = false;
     this.winner = null;
 }

 public String getTeamA() {
     return teamA;
 }

 public String getTeamB() {
     return teamB;
 }

 public String getRound() {
     return round;
 }

 public boolean isPlayed() {
     return isPlayed;
 }

 public String getWinner() {
     return winner;
 }

 public void playMatch(String winner) {
     if (!winner.equals(teamA) && !winner.equals(teamB)) {
         throw new IllegalArgumentException("Winner must be either teamA or teamB");
     }
     this.isPlayed = true;
     this.winner = winner;
 }

 @Override
 public String toString() {
     return "Match{" +
             "teamA='" + teamA + '\'' +
             ", teamB='" + teamB + '\'' +
             ", round='" + round + '\'' +
             ", isPlayed=" + isPlayed +
             ", winner='" + winner + '\'' +
             '}';
 }
}