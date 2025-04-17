package buffer.Crawler;

import buffer.model.Paper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class GoogleScholarCrawler implements Crawler {
    private static final String BASE_URL = "https://scholar.google.com/scholar?hl=en&q=";
    private static final int MAX_RESULTS = 5;

    @Override
    public List<Paper> search(String keyword) {
        List<Paper> results = new ArrayList<>();
        try {
            String searchUrl = BASE_URL + keyword.replace(" ", "+");
            Document doc = Jsoup.connect(searchUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(20_000)
                    .get();

            Elements paperCards = doc.select(".gs_r");

            int count = 0;
            for (Element card : paperCards) {
                if (count >= MAX_RESULTS) break;

                // Title
                Element titleElem = card.selectFirst(".gs_rt a");
                String title = titleElem != null ? titleElem.text().trim() : "N/A";

                // Authors
                Element authorElem = card.selectFirst(".gs_a");
                String authors = authorElem != null ? authorElem.text().trim() : "N/A";

                // Abstract
                Element abstractElem = card.selectFirst(".gs_rs");
                String abstractText = abstractElem != null ? abstractElem.text().trim() : "N/A";

                // Link
                String link = titleElem != null ? titleElem.attr("href") : "N/A";

                // Date (optional, in format [Year] at the end of author)
                String date = "N/A";  // Google Scholar doesn't give an explicit "date" in all cases

                results.add(new Paper(title, List.of(authors.split(",")), abstractText, date, link, "Google Scholar"));
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}