package BlockPackage.voting;

import BlockPackage.DataStructure.*;
import java.util.*;

public class VotingMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Blockchain<Vote> voteChain = new Blockchain<>(3);
        int difficulty = 3;

        while (true) {
            System.out.println("\n Voting Blockchain Menu:");
            System.out.println("1. Add Vote(s)");
            System.out.println("2. View Blockchain");
            System.out.println("3. Search by Candidate");
            System.out.println("4. Count Votes for Candidate");
            System.out.println("5. Reset Blockchain");
            System.out.println("6. Exit");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
             
                case 1:
                System.out.print("Enter number of votes to add: ");
                int numVotes = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            
                List<Vote> votes = new ArrayList<>();
                for (int i = 0; i < numVotes; i++) {
                    System.out.print("Voter ID: ");
                    String voterId = scanner.nextLine();
            
                    // Check if the voter has already voted
                    if (votingUtils.hasVoted(voteChain, voterId)) {
                        System.out.println(" Voter with ID '" + voterId + "' has already voted. Vote rejected.");
                        i--; // Retry current vote
                        continue;
                    }
            
                    System.out.print("Candidate Name: ");
                    String candidate = scanner.nextLine();
                    System.out.print("Constituency: ");
                    String location = scanner.nextLine();
            
                    // Add the vote to the list
                    votes.add(new Vote(voterId, candidate, location));
                }
            
                // Generate the timestamp here
                String timestamp = String.valueOf(System.currentTimeMillis());
            
                // Create the new block with the timestamp and votes
                Block<Vote> newBlock = new Block<>(voteChain.getSize(), timestamp, votes, voteChain.getLatestBlock().getHash());
                
                // Use the updated method to mine and add the block
                votingUtils.mineAndAddVoteBlock(voteChain, newBlock, difficulty);
            
                System.out.println("✅ Block added with " + votes.size() + " vote(s).");
                break;
            

                case 2:
                    voteChain.printChain();
                    break;

                case 3:
                    System.out.print("Enter candidate name to search: ");
                    String name = scanner.nextLine();
                    votingUtils.searchByCandidate(voteChain, name);
                    break;

                case 4:
                    System.out.print("Enter candidate name to count votes: ");
                    String countName = scanner.nextLine();
                    int count = votingUtils.countVotesForCandidate(voteChain, countName);
                    System.out.println("Total votes for " + countName + ": " + count);
                    break;

                case 5:
                votingUtils.clearVotes(voteChain);
                    System.out.println("Blockchain reset successfully.");
                    break;

                case 6:
                    System.out.println("Exiting Voting App.");
                    return;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
