import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class User {
    ArrayList<String> blockedEmails = new ArrayList<>();
    public List<LinkedHashMap<String, String>> emails = new ArrayList<>();
    public static int totSpamCnt = 0;
    public Map<String, Integer> mpp = new HashMap<>();
    SubjectTrie subjectTrie = new SubjectTrie();

    public void acceptEmails(Set<String> suspiciousDomains, Set<String> urgencyWords) {
      LinkedHashMap<String, String> email1 = new LinkedHashMap<>();
      email1.put("From", "\"Lottery Department\" <lottery@winbig.com>");
      email1.put("To", "jane.doe@example.com");
      email1.put("Cc", "support@winbig.com");
      email1.put("Bcc", "hiddenuser123@example.com");
      email1.put("Subject", "Congratulations! You’ve won $1,000,000!");
      email1.put("Date", "Mon, 15 Apr 2025 10:24:37 +0530");
      email1.put("Content-Type", "text/plain; charset=\"UTF-8\"");
      email1.put("Body", "Dear Jane Doe,\n\nYou’ve won $1M...");
  
      LinkedHashMap<String, String> email2 = new LinkedHashMap<>();
      email2.put("From", "\"Tech Support\" <support@yourbank.com>");
      email2.put("To", "john.smith@example.com");
      email2.put("Cc", "itdesk@yourbank.com");
      email2.put("Bcc", "admin@yourbank.com");
      email2.put("Subject", "Urgent: Account Verification Required");
      email2.put("Date", "Tue, 16 Apr 2025 09:10:12 +0530");
      email2.put("Content-Type", "text/plain; charset=\"UTF-8\"");
      email2.put("Body", "Dear John Smith,\n\nWe have detected unusual activity...");
  
// Define the common "From" address
String commonFrom = "\"John Doe\" <john.doe@phishdomain.org>";

// Spam email 1
LinkedHashMap<String, String> spam1 = new LinkedHashMap<>();
spam1.put("From", commonFrom);
spam1.put("To", "john.doe@example.com");
spam1.put("Subject", "URGENT: Account locked, take action now!");
spam1.put("Date", "Mon, 15 Apr 2025 09:24:37 +0530");
spam1.put("Content-Type", "text/plain; charset=\"UTF-8\"");
spam1.put("Body", "Dear John Doe,\n\nYour account has been locked due to suspicious activity. " +
        "\n" +
        "Take action now!");

// Spam email 2
LinkedHashMap<String, String> spam2 = new LinkedHashMap<>();
spam2.put("From", commonFrom);
spam2.put("To", "jane.doe@example.com");
spam2.put("Subject", "FINAL WARNING: Verify your account or face suspension!");
spam2.put("Date", "Tue, 16 Apr 2025 10:15:30 +0530");
spam2.put("Content-Type", "text/plain; charset=\"UTF-8\"");
spam2.put("Body", "Dear Jane Doe,\n\nWe have detected unusual activity in your account. " +
        "Please verify your details ASAP to avoid account suspension. Click here: http://secure-update.info\n" +
        "Important: Action required immediately!");

// Spam email 3
LinkedHashMap<String, String> spam3 = new LinkedHashMap<>();
spam3.put("From", "alice.smith@example.com");
spam3.put("To", "alice.smith@example.com");
spam3.put("Subject", "URGENT: Your bank account has been compromised!");
spam3.put("Date", "Mon, 18 Apr 2025 11:45:37 +0530");
spam3.put("Content-Type", "text/plain; charset=\"UTF-8\"");
spam3.put("Body", "Dear Alice Smith,\n\nWe have detected unauthorized access to your account. Please click " +
        "here to verify your details immediately: http://login-verification.xyz. Act now to prevent account suspension!");

// Spam email 4
LinkedHashMap<String, String> spam4 = new LinkedHashMap<>();
spam4.put("From", "alice.smith@example.com");
spam4.put("To", "charlie.brown@example.com");
spam4.put("Subject", "ACT NOW: Download your exclusive malware protection!");
spam4.put("Date", "Tue, 19 Apr 2025 14:12:54 +0530");
spam4.put("Content-Type", "text/plain; charset=\"UTF-8\"");
spam4.put("Body", "Dear Charlie Brown,\n\nClick here to download the latest malware protection tool that " +
        "guarantees your privacy. Don't miss out, this limited-time offer is only valid for today! " +
        "Download now: http://malware-download.net");

// Spam email 5
LinkedHashMap<String, String> spam5 = new LinkedHashMap<>();
spam5.put("From", "alice.smith@example.com");
spam5.put("To", "emma.green@example.com");
spam5.put("Subject", "Security Notice: Immediate action required!");
spam5.put("Date", "Wed, 20 Apr 2025 09:30:12 +0530");
spam5.put("Content-Type", "text/plain; charset=\"UTF-8\"");
spam5.put("Body", "Dear Emma Green,\n\nYour account has been flagged for unusual activity. Please click the " +
        "link to verify your identity and restore your account: http://secure-update.info. Your account will be locked " +
        "if you do not act immediately!");

// Spam email 6
LinkedHashMap<String, String> spam6 = new LinkedHashMap<>();
spam6.put("From", "alice.smith@example.com");
spam6.put("To", "david.jones@example.com");
spam6.put("Subject", "FINAL WARNING: Update your billing information or lose access!");
spam6.put("Date", "Thu, 21 Apr 2025 08:50:24 +0530");
spam6.put("Content-Type", "text/plain; charset=\"UTF-8\"");
spam6.put("Body", "Dear David Jones,\n\nOur system has detected a billing issue with your account. To avoid " +
        "suspension, please update your billing information immediately: http://update-billing.info. Final warning!");

// Spam email 7
LinkedHashMap<String, String> spam7 = new LinkedHashMap<>();
spam7.put("From", "alice.smith@example.com");
spam7.put("To", "lucy.miller@example.com");
spam7.put("Subject", "Account Recovery - Immediate Action Required!");
spam7.put("Date", "Fri, 22 Apr 2025 10:20:30 +0530");
spam7.put("Content-Type", "text/plain; charset=\"UTF-8\"");
spam7.put("Body", "Dear Lucy Miller,\n\nYour account has been flagged for suspicious activity. To avoid permanent " +
        "suspension, you need to verify your account immediately: http://account-recovery-mail.net. Act fast!");

        LinkedHashMap<String, String> nonspambyjohn1 = new LinkedHashMap<>();
nonspambyjohn1.put("From", "\"John Doe\" <john.doe@nonspambyjohn.com>");
nonspambyjohn1.put("To", "mary.jones@example.com");
nonspambyjohn1.put("Cc", "help@nonspambyjohn.com");
nonspambyjohn1.put("Bcc", "admin@nonspambyjohn.com");
nonspambyjohn1.put("Subject", "Your recent account activity summary");
nonspambyjohn1.put("Date", "Thu, 19 Apr 2025 08:12:15 +0530");
nonspambyjohn1.put("Content-Type", "text/plain; charset=\"UTF-8\"");
nonspambyjohn1.put("Body", "Dear Mary Jones,\n\nWe have attached the activity summary for your account from the last month. Please let us know if you have any questions.");

LinkedHashMap<String, String> nonspambyjohn2 = new LinkedHashMap<>();
nonspambyjohn2.put("From", "\"John Doe\" <john.doe@nonspambyjohn.com>");
nonspambyjohn2.put("To", "kevin.white@example.com");
nonspambyjohn2.put("Subject", "Your upcoming flight details");
nonspambyjohn2.put("Date", "Fri, 20 Apr 2025 16:45:00 +0530");
nonspambyjohn2.put("Content-Type", "text/plain; charset=\"UTF-8\"");
nonspambyjohn2.put("Body", "Dear Kevin White,\n\nThis is a reminder about your upcoming flight with Airline XYZ. Your flight departs on 23rd April at 9:00 AM. Please ensure you arrive 2 hours before departure.");

LinkedHashMap<String, String> nonspambyjohn3 = new LinkedHashMap<>();
nonspambyjohn3.put("From", "\"John Doe\" <john.doe@nonspambyjohn.com>");
nonspambyjohn3.put("To", "lisa.patel@example.com");
nonspambyjohn3.put("Subject", "Thank you for your purchase");
nonspambyjohn3.put("Date", "Sat, 21 Apr 2025 11:30:22 +0530");
nonspambyjohn3.put("Content-Type", "text/plain; charset=\"UTF-8\"");
nonspambyjohn3.put("Body", "Dear Lisa Patel,\n\nThank you for purchasing from our online store! Your order has been successfully processed and will be shipped to your address shortly.");

LinkedHashMap<String, String> nonspambyjohn4 = new LinkedHashMap<>();
nonspambyjohn4.put("From", "\"John Doe\" <john.doe@nonspambyjohn.com>");
nonspambyjohn4.put("To", "mark.brown@example.com");
nonspambyjohn4.put("Subject", "Your subscription renewal reminder");
nonspambyjohn4.put("Date", "Sun, 22 Apr 2025 10:00:00 +0530");
nonspambyjohn4.put("Content-Type", "text/plain; charset=\"UTF-8\"");
nonspambyjohn4.put("Body", "Dear Mark Brown,\n\nThis is a friendly reminder that your subscription to our service will be renewed on 1st May 2025. If you need to make any changes, please contact us.");

LinkedHashMap<String, String> nonspambyjohn5 = new LinkedHashMap<>();
nonspambyjohn5.put("From", "\"John Doe\" <john.doe@nonspambyjohn.com>");
nonspambyjohn5.put("To", "mark.brown@example.com");
nonspambyjohn5.put("Subject", "Your subscription renewal reminder");
nonspambyjohn5.put("Date", "Sun, 22 Apr 2025 10:00:00 +0530");
nonspambyjohn5.put("Content-Type", "text/plain; charset=\"UTF-8\"");
nonspambyjohn5.put("Body", "Dear Mark Brown,\n\nThis is a friendly reminder that your subscription to our service will be renewed on 1st May 2025. If you need to make any changes, please contact.");



        // Adding spam emails to the list
        emails.add(email1);
        emails.add(email2);
        emails.add(spam2);
        emails.add(spam3);
        emails.add(spam4);
        emails.add(spam5);
        emails.add(spam6);
        emails.add(spam1);
        emails.add(nonspambyjohn1);
        emails.add(nonspambyjohn2);
        emails.add(nonspambyjohn3);
        emails.add(nonspambyjohn4);
        emails.add(nonspambyjohn5);
        analyzeEmailsForSpam(suspiciousDomains,urgencyWords);

      // Insert subjects to Trie
      subjectTrie.insert(email1.get("Subject"));
      subjectTrie.insert(email2.get("Subject"));
      subjectTrie.insert(spam1.get("Subject"));
      subjectTrie.insert(spam2.get("Subject"));
      subjectTrie.insert(spam3.get("Subject"));
      subjectTrie.insert(spam4.get("Subject"));
      subjectTrie.insert(spam5.get("Subject"));
      subjectTrie.insert(spam6.get("Subject"));
      subjectTrie.insert(nonspambyjohn1.get("Subject"));
      subjectTrie.insert(nonspambyjohn2.get("Subject"));
      subjectTrie.insert(nonspambyjohn3.get("Subject"));
      subjectTrie.insert(nonspambyjohn4.get("Subject"));
      subjectTrie.insert(nonspambyjohn5.get("Subject"));

    }
    public void analyzeEmailsForSpam(Set<String> suspiciousDomains, Set<String> urgencyWords) {
        Functions obj = new Functions();
    
        for (int i = 0; i < emails.size(); i++) {
            LinkedHashMap<String, String> email = emails.get(i);
            String from = email.get("From");
            String body = email.get("Body");
    
            boolean isSpam = obj.isSpamEmail(body, suspiciousDomains, urgencyWords);
            if (isSpam) {
                totSpamCnt++;
                mpp.put(from, mpp.getOrDefault(from, 0) + 1);
            }
            // Optionally store spam status if needed in preview
            email.put("SpamStatus", String.valueOf(isSpam));
        }
    }
    public void displayEmailPreviews(Set<String> suspiciousDomains, Set<String> urgencyWords) {
        System.out.println("📧 Inbox:");
        Functions obj = new Functions();
        for (int i = 0; i < emails.size(); i++) {
            LinkedHashMap<String, String> email = emails.get(i);
            String from = email.get("From");
            String subject = email.get("Subject");
            String body = email.get("Body");
    
            String[] bodyWords = body.split("\\s+");
            StringBuilder preview = new StringBuilder();
            for (int j = 0; j < Math.min(10, bodyWords.length); j++) {
                preview.append(bodyWords[j]).append(" ");
            }
    
            boolean isSpam = obj.isSpamEmail(body, suspiciousDomains,urgencyWords);
    
            System.out.println("-------------------------------------------------");
            System.out.println("Email #" + i);
            System.out.println("Subject: " + subject);
            System.out.println("From: " + from);
            System.out.println("Preview: " + preview.toString().trim() + "...");
            System.out.println("Spam: " + (isSpam ? "✅ Yes" : "❌ No"));
            System.out.println("-------------------------------------------------\n");
        }

    }

    public void letsBlock() {
        System.out.println("🔍 Analyzing spam statistics...");
        Map<String, Integer> totalEmailsFromSender = new HashMap<>();
    
        // Count total emails from each sender
        for (LinkedHashMap<String, String> email : emails) {
            String sender = email.get("From");
            totalEmailsFromSender.put(sender, totalEmailsFromSender.getOrDefault(sender, 0) + 1);
        }
    
        // Determine and block spammers
        for (String sender : mpp.keySet()) {
            int spamCount = mpp.get(sender);
            //int totalCount = totalEmailsFromSender.getOrDefault(sender, 0);
    
            double ratio = (double) spamCount /totSpamCnt;
    
            if (ratio >= 0.4) {  // You can adjust this threshold
                if (!blockedEmails.contains(sender)) {
                    blockedEmails.add(sender);
                }
            }
        }
    
        // Print blocked users
        if (blockedEmails.isEmpty()) {
            System.out.println("✅ No users were blocked.");
        } else {
            System.out.println("🚫 Blocked Users:");
            for (String user : blockedEmails) {
                System.out.println("🔒 " + user);
            }
        }
    }
    

    public void searchBySubject() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter subject prefix to search: ");
        String prefix = sc.nextLine().toLowerCase();

        List<String> matchedSubjects = subjectTrie.searchByPrefix(prefix);
        if (matchedSubjects.isEmpty()) {
            System.out.println("❌ No subjects starting with: " + prefix);
            return;
        }

        boolean found = false;
        int count = 1;
        for (LinkedHashMap<String, String> email : emails) {
            String subject = email.get("Subject").toLowerCase();
            if (matchedSubjects.stream().anyMatch(subject::equalsIgnoreCase)) {
                System.out.println("========== MATCHED EMAIL " + count + " ==========");
                displayEmail(email);
                System.out.println();
                found = true;
            }
            count++;
        }

        if (!found) System.out.println("❌ Subjects found, but no matching email data.");
    }

    public void displayEmail(LinkedHashMap<String, String> email) {
        for (Map.Entry<String, String> entry : email.entrySet()) {
            if (entry.getKey().equals("Body")) {
                System.out.println("\n" + entry.getValue());
            } else {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public void sortEmailsByDate() {
        PriorityQueue<LinkedHashMap<String, String>> pq = new PriorityQueue<>(
            Comparator.comparing(e -> parseEmailDate(e.get("Date")))
        );

        pq.addAll(emails);
        System.out.println("📅 Emails sorted by date:");
        while (!pq.isEmpty()) {
            System.out.println("=========================================");
            displayEmail(pq.poll());
            System.out.println();
        }
    }

    public static Date parseEmailDate(String dateStr) {
        try {
            return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }
}

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> subjects = new ArrayList<>();
    boolean isEndOfWord = false;
}

class SubjectTrie {
    TrieNode root = new TrieNode();

    public void insert(String subject) {
        TrieNode node = root;
        for (char ch : subject.toLowerCase().toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
            node.subjects.add(subject);
        }
        node.isEndOfWord = true;
    }

    public List<String> searchByPrefix(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) return new ArrayList<>();
            node = node.children.get(ch);
        }
        return new ArrayList<>(new HashSet<>(node.subjects));
    }
}
