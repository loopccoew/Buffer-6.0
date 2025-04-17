package buffer.model;
import java.util.List;
public class Paper {
	
	private final String title; 
	private final List<String> authors; 
	private final String abstractText; 
	private final String publicationDate; 
	private final String link; 
	private final String source; // IEEE, ArXiv, etc.


	public Paper(String title, List<String> authors, String abstractText, String publicationDate, String link, String source) {
		this.title = title;
		this.authors = authors;
		this.abstractText = abstractText;
		this.publicationDate = publicationDate;
		this.link = link;
		this.source = source;
		}
		
	public String getTitle() {
		return title;
		}
		
	public List<String> getAuthors() {
		return authors;
		}
		
	public String getAbstractText() {
		return abstractText;
		}
		
	public String getPublicationDate() {
		return publicationDate;
		}
		
	public String getLink() {
		return link;
		}
		
	public String getSource() {
		return source;
		}
		
		@Override
	public String toString() {
		return title + " by " + String.join(", ", authors);
		}

//		public String getTitle() {
//			// TODO Auto-generated method stub
//			return null;
//		}

//		public String getTitle() {
//			// TODO Auto-generated method stub
//			return null;
//		}
}