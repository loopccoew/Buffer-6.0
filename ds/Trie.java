package buffer.ds;

import java.util.ArrayList; 
import java.util.List;
public class Trie { 
	private final TrieNode root;
	public Trie() {
	    root = new TrieNode();
	}

	public void insert(String word) {
	    TrieNode curr = root;
	    for (char ch : word.toLowerCase().toCharArray()) {
	        curr.children.putIfAbsent(ch, new TrieNode());
	        curr = curr.children.get(ch);
	    }
	    curr.isEndOfWord = true;
	}

	public boolean search(String word) {
	    TrieNode curr = root;
	    for (char ch : word.toLowerCase().toCharArray()) {
	        if (!curr.children.containsKey(ch)) return false;
	        curr = curr.children.get(ch);
	    }
	    return curr.isEndOfWord;
	}

	public List<String> suggest(String prefix) {
	    List<String> suggestions = new ArrayList<>();
	    TrieNode node = root;
	    for (char ch : prefix.toLowerCase().toCharArray()) {
	        if (!node.children.containsKey(ch)) return suggestions;
	        node = node.children.get(ch);
	    }
	    dfs(node, new StringBuilder(prefix.toLowerCase()), suggestions);
	    return suggestions;
	}

	private void dfs(TrieNode node, StringBuilder path, List<String> suggestions) {
	    if (node.isEndOfWord) {
	        suggestions.add(path.toString());
	    }
	    for (char ch : node.children.keySet()) {
	        path.append(ch);
	        dfs(node.children.get(ch), path, suggestions);
	        path.deleteCharAt(path.length() - 1);
	    }
	}

}