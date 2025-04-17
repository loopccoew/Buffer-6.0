package dsa_modules;

import dao.FraudDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FraudDetector {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
    }

    private TrieNode root = new TrieNode();
    private FraudDAO fraudDAO;

    // Constructor with DAO
    public FraudDetector(FraudDAO fraudDAO) {
        this.fraudDAO = fraudDAO;
        loadFromDatabase(); // Populate trie with existing fraud names
    }

    // Constructor without DAO (for testing / offline)
    public FraudDetector() {}

    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.isEndOfWord = true;

        if (fraudDAO != null) {
            fraudDAO.addFraudulentName(word);
        }
    }

    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.get(c);
            if (current == null) return false;
        }
        return current.isEndOfWord;
    }

    // Load fraud names from DB into Trie
    private void loadFromDatabase() {
        if (fraudDAO != null) {
            List<String> names = fraudDAO.getAllFraudulentNames();
            for (String name : names) {
                insertWithoutSaving(name); // Avoid re-saving to DB
            }
        }
    }

    // Helper to insert into Trie without saving to DB
    private void insertWithoutSaving(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.isEndOfWord = true;
    }
}

