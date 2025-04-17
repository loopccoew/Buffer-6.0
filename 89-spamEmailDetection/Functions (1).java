import java.util.*;
import java.util.regex.*;

class Functions{
    public static List<String> detectPhishingLinks(String emailContent, Set<String> suspiciousDomains) {
        List<String> phishingLinks = new ArrayList<>();
        Pattern urlPattern = Pattern.compile("https?://(\\S+)");
        Matcher matcher = urlPattern.matcher(emailContent);
    
        while (matcher.find()) {
            String fullUrl = matcher.group();      // Full URL including http/https
            String domain = matcher.group(1);      // Everything after http:// or https://
    
            for (String suspicious : suspiciousDomains) {
                if (domain.contains(suspicious)) {
                    phishingLinks.add(fullUrl);
                    // No break — we collect all matching links
                }
            }
        }
    
        return phishingLinks;
    }
    
public static String sentimentAnalysis(String content, Set<String> positiveWords, Set<String> negativeWords) {
    String[] words = content.toLowerCase().split("\\W+");
    int pos = 0, neg = 0;
    for (String word : words) {
        if (positiveWords.contains(word)) pos++;
        if (negativeWords.contains(word)) neg++;
    }
    return pos > neg ? "Positive" : (neg > pos ? "Negative" : "Neutral");
}

//in sender
public static List<String> cleanupSuggestions(String email) {
    List<String> suggestions = new ArrayList<>();
    
    String[] paragraphs = email.split("\n\n");
    for (String para : paragraphs) {
        if (para.length() > 300) suggestions.add("Consider splitting a long paragraph.");
    }
    
    if (email.matches(".*!{2,}.*")) suggestions.add("Too many exclamations!");
    String[] words=email.split(" ");
    List<String> capitalWords=new ArrayList<>();
    for(String word:words)
    {
        if (word.equals(word.toUpperCase()))
        {
            capitalWords.add(word);
        }
    }
    double ratio=capitalWords.size()/words.length;
    if(ratio>0.4)
    {
        suggestions.add("Too many capital letters.");
    }
    String[] lines = email.split("\n");
boolean missingPunctuation = false;
for (String line : lines) {
    line = line.trim();
    if (!line.isEmpty() && !line.matches(".*[.?!]$")) {
        missingPunctuation = true;
        break;
    }
}
if (missingPunctuation) {
    suggestions.add("Possible missing punctuation.");
}


    
    return suggestions;
}

//not done
public static List<String> extractKeywords(String content, int topN) {
    Map<String, Integer> freq = new HashMap<>();
    String[] words = content.toLowerCase().split("\\W+");
    for (String word : words) {
        freq.put(word, freq.getOrDefault(word, 0) + 1);
    }
    PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
    pq.addAll(freq.entrySet());

    List<String> topKeywords = new ArrayList<>();
    while (!pq.isEmpty() && topN-- > 0) {
        topKeywords.add(pq.poll().getKey());
    }
    return topKeywords;
}
public static void detectAndPrintSchedule(String email) {
    String[] patterns = {
        "\\b\\d{1,2}/\\d{1,2}/\\d{2,4}\\b",                      // Dates like 12/04/2025
        "\\b(Monday|Tuesday|Wednesday|Tomorrow|Next week)\\b"   // Common schedule words
    };

    boolean found = false;

    for (String p : patterns) {
        Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        while (matcher.find()) {
            System.out.println("📅 Schedule Mention Detected: " + matcher.group());
            found = true;
        }
    }

    if (!found) {
        System.out.println("No schedule-related content found.");
    }
}
//=========Method for sender==========
// public static boolean detectMissingAttachment(String email, boolean hasAttachment) {
//     if (email.toLowerCase().contains("attached") && !hasAttachment) {
//         return true;
//     }
//     return false;
// }

//not done
public static String detectLanguage(String content, Set<String> englishWords, Set<String> hindiWords) {
    int en = 0, hi = 0;
    String[] words = content.toLowerCase().split("\\W+");
    for (String word : words) {
        if (englishWords.contains(word)) en++;
        if (hindiWords.contains(word)) hi++;
    }
    return en > hi ? "English" : (hi > en ? "Hindi" : "Unknown");
}
public static String classifyEmail(String content, Map<String, String> keywordCategoryMap) {
    Map<String, Integer> categoryScore = new HashMap<>();
    String[] words = content.toLowerCase().split("\\W+");

    for (String word : words) {
        String category = keywordCategoryMap.get(word);
        if (category != null) {
            categoryScore.put(category, categoryScore.getOrDefault(category, 0) + 1);
        }
    }

    return categoryScore.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey).orElse("Uncategorized");
}
public static boolean detectUrgency(String content, Set<String> urgencyWords) {
    String[] words = content.toLowerCase().split("\\W+");
    for (String word : words) {
        if (urgencyWords.contains(word)) return true;
    }
    return false;
}



////Main check for Spam
public static boolean isSpamEmail(String emailBody, Set<String> suspiciousDomains, Set<String> urgencyWords) {
    Set<String> spamWords = new HashSet<>(Arrays.asList(
    "free", 
    "winner", 
    "cash", 
    "offer", 
    "urgent", 
    "exclusive", 
    "limited", 
    "credit", 
    "act now"
));

    // 1. Check for Phishing Links
    List<String> phishingLinks = detectPhishingLinks(emailBody, suspiciousDomains);
    if (!phishingLinks.isEmpty()) {
        return true; // Spam due to phishing links
    }

    // 2. Check for Urgency
    boolean isUrgent = detectUrgency(emailBody, urgencyWords);
    if (isUrgent) {
        return true; // Spam due to urgency
    }

    // 3. Check for Spammy Words
    String[] words = emailBody.toLowerCase().split("\\W+");
    int spamCount = 0;
    for (String word : words) {
        if (spamWords.contains(word)) {
            spamCount++;
        }
    }
    if (spamCount >= 2) {
        return true; // Spam due to spam words
    }

    // 4. Check for Formatting Issues (suggestions)
    List<String> cleanupSuggestionsList = cleanupSuggestions(emailBody);
    if (cleanupSuggestionsList.size()>2) {
        return true; // Spam due to formatting issues
    }

    // No issues found, not spam
    return false;
}

}