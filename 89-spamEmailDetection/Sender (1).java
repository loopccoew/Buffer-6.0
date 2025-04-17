import java.util.*;

class Sender {
    User obj;

    Sender(User obj) {
        this.obj = obj;
    }

    Scanner sc = new Scanner(System.in);

    void create(Set<String> suspiciousDomains, Set<String> urgencyWords) {
        LinkedHashMap<String, String> email = new LinkedHashMap<>();

        System.out.print("From: ");
        email.put("From", sc.nextLine());
        if(obj.blockedEmails.contains(email.get("From"))) 
        {
            System.out.println("You are blocked!🚫");
            return;
        }

        System.out.print("To: ");
        email.put("To", sc.nextLine());

        System.out.print("Cc: ");
        email.put("Cc", sc.nextLine());

        System.out.print("Bcc: ");
        email.put("Bcc", sc.nextLine());

        System.out.print("Subject: ");
        String subject = sc.nextLine();
        email.put("Subject", subject);

        System.out.print("Date: ");
        email.put("Date", sc.nextLine());

        email.put("Content-Type", "text/plain; charset=\"UTF-8\"");
        StringBuilder body = new StringBuilder();
        Functions obj2 = new Functions();

        while (true) {
            System.out.println("Enter Body (type 'End.' to finish):");
            while (true) {
                String line = sc.nextLine();
                if (line.equalsIgnoreCase("End.")) break;
                body.append(line).append("\n");
            }

            List<String> cleanUpList = obj2.cleanupSuggestions(body.toString());
            if (!cleanUpList.isEmpty()) {
                System.out.println("Clean Up Suggestions:");
                for (String suggest : cleanUpList) {
                    System.out.println(suggest);
                }
                System.out.println("Enter Body again:");
                body.setLength(0); // clear for next input
            } else break;
        }
        SubjectTrie subjectTrie = new SubjectTrie();
        email.put("Body", body.toString());
       
        if(!obj.blockedEmails.contains(email.get("From"))) {
        obj.emails.add(email);
        obj.subjectTrie.insert(subject);}
        // subjectTrie.insert(email.get("Subject"));
        System.out.println("✅ Email saved successfully!\n");
        obj.analyzeEmailsForSpam(suspiciousDomains,urgencyWords);
    }

    void showEmails() {
        if (obj.emails.isEmpty()) {
            System.out.println("📭 No emails to display.");
            return;
        }

        System.out.println("📧 Inbox:");
        int index = 1;
        for (LinkedHashMap<String, String> email : obj.emails) {
            System.out.println("=========================================");
            System.out.println("Email #" + index++);
            obj.displayEmail(email);
            System.out.println();
        }
    }
}
