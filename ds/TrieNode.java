package buffer.ds;

import java.util.HashMap; 
import java.util.Map;

public class TrieNode { 
	public Map<Character, TrieNode> children; 
	public boolean isEndOfWord;
	public TrieNode() {
	    this.children = new HashMap<>();
	    this.isEndOfWord = false;
	}
}