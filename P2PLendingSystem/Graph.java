package dsa_modules;

import java.util.*;

public class Graph {
    private Map<Integer, List<Match>> adjList = new HashMap<>();

    // Add a lender-borrower match to the graph
    public void addMatch(int lenderId, Match match) {
        adjList.putIfAbsent(lenderId, new ArrayList<>());
        adjList.get(lenderId).add(match);
    }

    // Add a borrower node (to ensure borrower is known in the system)
    public void addBorrower(int borrowerId) {
        adjList.putIfAbsent(borrowerId, new ArrayList<>());
    }

    // Add a lender node (to ensure lender is known in the system)
    public void addLender(int lenderId) {
        adjList.putIfAbsent(lenderId, new ArrayList<>());
    }

    // Get all matches from the entire graph (useful for matching logic)
    public List<Match> getMatches(int dummy) {
        List<Match> allMatches = new ArrayList<>();
        for (List<Match> matches : adjList.values()) {
            allMatches.addAll(matches);
        }
        return allMatches;
    }

    // Remove all matches involving borrowers in the provided list
    public void removeBorrowers(List<Borrower> borrowersToRemove) {
        Set<Integer> borrowerIds = new HashSet<>();
        for (Borrower b : borrowersToRemove) {
            borrowerIds.add(b.getId());
        }

        for (List<Match> matches : adjList.values()) {
            matches.removeIf(m -> borrowerIds.contains(m.getBorrowerId()));  // Use getter for borrowerId
        }
    }

    // Remove all matches involving lenders in the provided list
    public void removeLenders(List<Lender> lendersToRemove) {
        Set<Integer> lenderIds = new HashSet<>();
        for (Lender l : lendersToRemove) {
            lenderIds.add(l.getId());
        }

        // Remove lender entries entirely
        for (Integer id : lenderIds) {
            adjList.remove(id);
        }

        // Also remove matches referencing these lenders from other entries
        for (List<Match> matches : adjList.values()) {
            matches.removeIf(m -> lenderIds.contains(m.getLenderId()));  // Use getter for lenderId
        }
    }
}
