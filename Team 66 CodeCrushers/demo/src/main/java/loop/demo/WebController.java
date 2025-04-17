package loop.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WebController {

    private final HistoricalVersionControlSys historicalVersionControlSys;

    public WebController(HistoricalVersionControlSys historicalVersionControlSys) {
        this.historicalVersionControlSys = historicalVersionControlSys;
    }

    @GetMapping("/")
    public String index() {
        return "index"; // This will return the index.html
    }

    @GetMapping("/graphs")
    @ResponseBody
    public List<DocumentGraph> getAllGraphs() {
        return historicalVersionControlSys.getGraphs(); // Return the list of graphs
    }

    @GetMapping("/graph/{id}")
    public String viewGraphDetails(@PathVariable String id, Model model) {
        DocumentGraph graph = historicalVersionControlSys.getGraphs().stream()
            .filter(g -> g.getId().equals(id))
            .findFirst()
            .orElse(null);
        
        if (graph != null) {
            model.addAttribute("graph", graph);
            model.addAttribute("versions", graph.getAllVersions());
            model.addAttribute("theories", graph.getAllTheories()); // Add theories to the model
            return "graphDetails"; // This will return graphDetails.html
        }
        return "error"; // Handle case where graph is not found
    }

    @GetMapping("/versions")
    @ResponseBody
    public List<TextVersion> getAllVersions() {
        // Collect all versions from all graphs
        return historicalVersionControlSys.getGraphs().stream()
            .flatMap(graph -> graph.getAllVersions().stream())
            .toList();
    }

    @GetMapping("/version/{id}")
    public String viewVersionDetails(@PathVariable String id, Model model) {
        // Find the version in all graphs
        TextVersion version = historicalVersionControlSys.getGraphs().stream()
            .flatMap(graph -> graph.getAllVersions().stream())
            .filter(v -> v.getId().equals(id))
            .findFirst()
            .orElse(null);
        
        if (version != null) {
            model.addAttribute("version", version);
            model.addAttribute("annotations", historicalVersionControlSys.getGraphs().stream()
                .flatMap(graph -> graph.getAnnotationsForVersion(id).stream())
                .toList());
            model.addAttribute("relationships", historicalVersionControlSys.getGraphs().stream()
                .flatMap(graph -> graph.getRelationshipsForVersion(id).stream())
                .toList());
        }
        return "versionDetails"; // This will return versionDetails.html
    }

    @GetMapping("/relationships/{id}")
    @ResponseBody
    public List<Relationship> getRelationships(@PathVariable String id) {
        // Collect relationships from all graphs for the given version ID
        return historicalVersionControlSys.getGraphs().stream()
            .flatMap(graph -> graph.getRelationshipsForVersion(id).stream())
            .toList();
    }

    @GetMapping("/theories")
    @ResponseBody
    public List<ReconstructionTheory> getAllTheories() {
        // Collect all theories from all graphs
        return historicalVersionControlSys.getGraphs().stream()
            .flatMap(graph -> graph.getAllTheories().stream())
            .toList();
    }
   @GetMapping("/theory/{id}")
public String viewTheoryDetails(@PathVariable String id, Model model) {
    // Find the theory in all graphs
    ReconstructionTheory theory = historicalVersionControlSys.getGraphs().stream()
        .flatMap(graph -> graph.getAllTheories().stream())
        .filter(t -> t.getId().equals(id))
        .findFirst()
        .orElse(null);
    
    if (theory != null) {
        model.addAttribute("theory", theory);
        model.addAttribute("children", theory.getChildren()); // Add child theories to the model
        return "theoryDetails"; // Corrected template name
    }
    return "error"; // Handle case where theory is not found
}
}