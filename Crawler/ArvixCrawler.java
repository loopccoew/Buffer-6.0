package buffer.Crawler;
import buffer.model.Paper; 
import org.jsoup.Jsoup; 
import org.jsoup.nodes.Document; 
import org.jsoup.nodes.Element; 
import org.jsoup.select.Elements; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.List;

public class ArvixCrawler implements buffer.Crawler.Crawler{
	private static final String BASE_URL = "https://arxiv.org/search/?query=";
	private static final int MAX_RESULTS = 5;

	@Override
	public List<Paper> search(String keyword) {
	    List<Paper> results = new ArrayList<>();
	    try {
	        String searchUrl = BASE_URL + keyword.replace(" ", "+") + "&searchtype=all";
	        Document doc = Jsoup.connect(searchUrl)
	                .userAgent("Mozilla/5.0")
	                .timeout(20_000)
	                .get();

	        Elements paperCards = doc.select("li.arxiv-result");

	        int count = 0;
	        for (Element card : paperCards) {
	            if (count >= MAX_RESULTS) break;

	            // Title
	            Element titleElem = card.selectFirst("p.title");
	            String title = titleElem != null ? titleElem.text().trim() : "N/A";

	            // Authors
	            Element authorsElem = card.selectFirst("p.authors");
	            List<String> authors = new ArrayList<>();
	            if (authorsElem != null) {
	                Elements authorLinks = authorsElem.select("a");
	                for (Element a : authorLinks) {
	                    authors.add(a.text());
	                }
	            }

	            // Abstract
	            Element abstractElem = card.selectFirst("span.abstract-full");
	            String abstractText = abstractElem != null ? abstractElem.text().trim().replaceAll("^Abstract: ", "") : "N/A";

	            // Link
	            Element linkElem = card.selectFirst("p.list-title a[href]");
	            String link = linkElem != null ? linkElem.attr("href") : "N/A";

	            // Date (optional)
	            Element dateElem = card.selectFirst("p.is-size-7");
	            String date = dateElem != null ? dateElem.text().replaceAll("Submitted ", "").trim() : "N/A";

	            results.add(new Paper(title, authors, abstractText, date, link, "arXiv"));
	            count++;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return results;
	}


}

