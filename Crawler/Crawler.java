package buffer.Crawler;

import buffer.model.Paper; 
import java.util.List;

public interface Crawler { 
	List<Paper> search(String keyword); 
	
}