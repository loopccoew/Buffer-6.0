package buffer.ds;
import java.util.*;


public class CitationGraph {
	private final Map<String, Set<String>> adjacencyList;
	
	public CitationGraph() {
	    this.adjacencyList = new HashMap<>();
	}

	public void addPaper(String title) {
	    adjacencyList.putIfAbsent(title, new HashSet<>());
	}

	public void addCitation(String fromTitle, String toTitle) {
	    addPaper(fromTitle);
	    addPaper(toTitle);
	    adjacencyList.get(fromTitle).add(toTitle);
	}

	public Set<String> getCitations(String title) {
	    return adjacencyList.getOrDefault(title, new HashSet<>());
	}

	public Set<String> getCitedBy(String title) {
	    Set<String> citedBy = new HashSet<>();
	    for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
	        if (entry.getValue().contains(title)) {
	            citedBy.add(entry.getKey());
	        }
	    }
	    return citedBy;
	}

	public List<String> suggestRelevant(String title) {
	    Set<String> visited = new HashSet<>();
	    List<String> suggestions = new ArrayList<>();
	    dfs(title, visited, suggestions);
	    suggestions.remove(title); // don’t include the original paper
	    return suggestions;
	}

	private void dfs(String current, Set<String> visited, List<String> result) {
	    if (!adjacencyList.containsKey(current) || visited.contains(current)) return;
	    visited.add(current);
	    for (String neighbor : adjacencyList.get(current)) {
	        result.add(neighbor);
	        dfs(neighbor, visited, result);
	    }
	}

	public List<String> detectIsolatedPapers() {
	    List<String> isolated = new ArrayList<>();
	    for (String paper : adjacencyList.keySet()) {
	        if (adjacencyList.get(paper).isEmpty() && getCitedBy(paper).isEmpty()) {
	            isolated.add(paper);
	        }
	    }
	    return isolated;
	}

	public void printGraph() {
	    for (String paper : adjacencyList.keySet()) {
	        System.out.println("📄 " + paper + " cites ➤ " + adjacencyList.get(paper));
	    }
	}

}
