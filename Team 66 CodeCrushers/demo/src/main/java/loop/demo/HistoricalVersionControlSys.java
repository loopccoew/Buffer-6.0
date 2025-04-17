package loop.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HistoricalVersionControlSys {

      private final List<DocumentGraph> graphs;

    public HistoricalVersionControlSys() {
        this.graphs = new ArrayList<>();
    }
    public static void main(String[] args) {
        SpringApplication.run(HistoricalVersionControlSys.class, args);
    }

    
    public List<DocumentGraph> getGraphs() {
        return graphs;
    }

    @jakarta.annotation.PostConstruct
    private void initializeExampleData() {
        // Create multiple graphs for different manuscripts
        DocumentGraph manuscript1Graph = createManuscriptGraph1();
        DocumentGraph manuscript2Graph = createManuscriptGraph2();

        // Add graphs to the list
        graphs.add(manuscript1Graph);
        graphs.add(manuscript2Graph);
    }

    private DocumentGraph createManuscriptGraph1() {
        DocumentGraph graph = new DocumentGraph("Mahabharat");

        // Add versions for Manuscript 1
        TextVersion northern = new TextVersion("v1", "Northern Recension", "North India", 
                                              "1500-1700 CE", 90000, 
                                              "More extensive battle descriptions", 0.95);
        TextVersion southern = new TextVersion("v2", "Southern Recension", "South India", 
                                              "1600-1800 CE", 95000, 
                                              "Additional philosophical sections", 0.9);
        
        graph.addVersion(northern);
        graph.addVersion(southern);
        
        // Add relationships for Manuscript 1
        Relationship rel1 = new Relationship("r1", northern, southern, "regional variant", 
                                            0.8, "Shares core narrative but with regional variations");
        graph.addRelationship(rel1);
        
        // Add annotations for Manuscript 1
        Annotation anno1 = new Annotation("a1", 
            "The Southern Recension contains philosophical material likely added after 1700 CE", 
            "Dr. Sharma", "v2", "version");
        graph.addAnnotation(anno1);
        
        // Add manuscripts for Manuscript 1
        Manuscript ms1 = new Manuscript("ms1", "v1", "British Museum", "Sanskrit", "1700 CE", "Good");
        graph.addManuscript(ms1);
     // Create theories for Manuscript 1
     ReconstructionTheory theory1 = new ReconstructionTheory("t1", "Theory of Northern Influence", 
     "Dr. Smith", "2023", "This theory suggests that the Northern Recension influenced the Southern Recension.");
 theory1.addVersion(northern);
 theory1.addVersion(southern);
 theory1.addRelationship(new Relationship("r1", northern, southern, "derived from", 
                                          0.8, "Northern Recension influenced Southern Recension"));

 // Create a child theory for Northern Influence
 ReconstructionTheory childTheory1 = new ReconstructionTheory("t1.1", "Sub-Theory of Northern Influence", 
     "Dr. Adams", "2024", "This sub-theory discusses specific examples of Northern influence on Southern texts.");
 childTheory1.addVersion(northern);
 childTheory1.addVersion(southern);
 theory1.addChildTheory(childTheory1); // Add the child theory to the parent theory
 
 // Create another theory for Manuscript 1
 ReconstructionTheory theory2 = new ReconstructionTheory("t2", "Theory of Southern Influence", 
     "Dr. Jones", "2024", "This theory suggests that the Southern Recension influenced the Northern Recension.");
 theory2.addVersion(northern);
 theory2.addVersion(southern);
 theory2.addRelationship(new Relationship("r2", southern, northern, "derived from", 
                                          0.7, "Southern Recension influenced Northern Recension"));

 graph.addTheory(theory1);
 graph.addTheory(theory2); // Add the second theory to the graph

        return graph;
    }

    private DocumentGraph createManuscriptGraph2() {
        DocumentGraph graph = new DocumentGraph("Ramayan");

// Add versions for Manuscript 2
TextVersion bengali = new TextVersion("v3", "Bengali Recension", "Bengal", 
                                       "1700-1800 CE", 92000, 
                                       "The Bengali Recension of the Ramayana is known for its unique narrative style and regional adaptations, incorporating local folklore and cultural elements. It reflects the socio-political context of Bengal during the 18th century, emphasizing themes of devotion and morality.", 0.85);

TextVersion critical = new TextVersion("v4", "Critical Edition", "Bhandarkar Institute", 
                                        "1919-1966 CE", 88000, 
                                        "The Critical Edition of the Ramayana, compiled by scholars at the Bhandarkar Institute, represents a comprehensive scholarly effort to collate and analyze various manuscripts of the text. This edition aims to provide a standardized version by comparing different recensions, highlighting textual variations, and offering critical commentary, thus serving as a foundational reference for future studies.", 1.0);
        
        graph.addVersion(bengali);
        graph.addVersion(critical);
        
        // Add relationships for Manuscript 2
        Relationship rel2 = new Relationship("r2", critical, bengali, "derived from", 
                                            0.9, "Critical edition heavily based on Bengali sources");
        graph.addRelationship(rel2);
        
        // Add annotations for Manuscript 2
        Annotation anno2 = new Annotation("a2", 
            "Bengali manuscript traditions show evidence of independent development", 
            "Prof. Das", "v3", "version");
        graph.addAnnotation(anno2);
        
        // Add manuscripts for Manuscript 2
        Manuscript ms2 = new Manuscript("ms2", "v2", "National Archives, India", "Sanskrit", "1800 CE", "Fair");
        graph.addManuscript(ms2);
        
        return graph;
    }
}

