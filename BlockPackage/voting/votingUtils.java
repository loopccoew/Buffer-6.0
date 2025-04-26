package BlockPackage.voting;

import java.util.List;

import BlockPackage.DataStructure.*;

public class votingUtils {

    // ✅ Check if a voter has already voted
    public static boolean hasVoted(Blockchain<Vote> chain, String voterId) {
        for (Block<Vote> block : chain.getChain()) {
            for (Vote vote : block.data) {
                if (vote.voterId.equals(voterId)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ✅ Clear the blockchain (votes)
    public static void clearVotes(Blockchain<Vote> chain) {
        chain.getChain().clear(); // Clear existing chain
        chain.createGenesisBlock(); // Reinitialize with genesis block
    }

    // ✅ Search all votes for a candidate name
    public static void searchByCandidate(Blockchain<Vote> chain, String candidateName) {
        boolean found = false;
        for (Block<Vote> block : chain.getChain()) {
            for (Vote vote : block.data) {
                if (vote.candidateName.equalsIgnoreCase(candidateName)) {
                    System.out.println(vote);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("❌ No votes found for candidate: " + candidateName);
        }
    }

    // ✅ Count total votes for a candidate
    public static int countVotesForCandidate(Blockchain<Vote> chain, String candidateName) {
        int count = 0;
        for (Block<Vote> block : chain.getChain()) {
            for (Vote vote : block.data) {
                if (vote.candidateName.equalsIgnoreCase(candidateName)) {
                    count++;
                }
            }
        }
        return count;
    }
     
    public static void mineAndAddVoteBlock(Blockchain<Vote> chain, Block<Vote> newBlock, int difficulty) {
        // Mine the block using Blockchain method
    chain.mineBlock(newBlock, difficulty);

    // Add the mined block to the blockchain
    chain.addBlock(newBlock);
    }
    

    
}
