import java.io.*;
import java.util.*;

public class NextGenAcademicSolution {

    static Scanner sc = new Scanner(System.in);
    static final String DOUBT_FILE = "doubts.txt";

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n------ Next-Gen Academic Solution ------");
            System.out.println("1. ASK Doubt");
            System.out.println("2. SEARCH Similar Doubt");
            System.out.println("3. VIEW ALL asked Doubts");
            System.out.println("4. ANSWER a Question");
            System.out.println("5. VIEW ANSWER to a Doubt");
            System.out.println("6. DELETE an Answer");
            System.out.println("7. DELETE a Question (and its answers)");
            System.out.println("8. EXIT");
            System.out.print("Choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> askDoubt();
                case 2 -> searchDoubt();
                case 3 -> viewAllDoubts();
                case 4 -> answerQuestion();
                case 5 -> viewAnswers();
                case 6 -> deleteAnswer();
                case 7 -> deleteQuestion();
                case 8 -> System.out.println("Exiting... Thank you!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 8);
    }

    static void askDoubt() {
        System.out.print("Enter your doubt: ");
        String question = sc.nextLine().trim();

        System.out.print("Post as anonymous? (1 = Yes, 0 = No): ");
        int anon = Integer.parseInt(sc.nextLine());
        String name = anon == 1 ? "Anonymous" : getName();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOUBT_FILE, true))) {
            writer.write(question + "|" + name);
            writer.newLine();
            System.out.println("\nYour question has been posted successfully!");
        } catch (IOException e) {
            System.out.println("Error writing doubt.");
        }
    }

    static String getName() {
        System.out.print("Enter your full name: ");
        return sc.nextLine().trim();
    }

    static void viewAllDoubts() {
        List<String> lines = readLines(DOUBT_FILE);
        if (lines.isEmpty()) {
            System.out.println("No doubts found.");
            return;
        }
        System.out.println("\nAll Asked Doubts:");
        int i = 1;
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2)
                System.out.println(i++ + ". " + parts[1] + ": " + parts[0]);
        }
    }

    static void answerQuestion() {
        List<String> lines = readLines(DOUBT_FILE);
        if (lines.isEmpty()) {
            System.out.println("No doubts available.");
            return;
        }

        displayQuestions(lines);
        System.out.print("Select question number to answer: ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice < 1 || choice > lines.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        String question = lines.get(choice - 1).split("\\|")[0];
        System.out.println("Enter your answer (type END on a new line to finish):");
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = sc.nextLine();
            if (line.equals("END")) break;
            sb.append(line).append(System.lineSeparator());
        }
        writeToFile(getAnswerFilename(question), sb.toString(), true);
        System.out.println("Your answer has been added!");
    }

    static void viewAnswers() {
        List<String> lines = readLines(DOUBT_FILE);
        if (lines.isEmpty()) {
            System.out.println("No doubts found.");
            return;
        }

        displayQuestions(lines);
        System.out.print("Enter question number to view answers: ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice < 1 || choice > lines.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        String question = lines.get(choice - 1).split("\\|")[0];
        List<String> answers = readLines(getAnswerFilename(question));

        System.out.println("\nAnswers for: " + question);
        if (answers.isEmpty()) {
            System.out.println("No answers yet.");
        } else {
            answers.forEach(a -> System.out.println("- " + a));
        }
    }

    static void deleteAnswer() {
        List<String> lines = readLines(DOUBT_FILE);
        if (lines.isEmpty()) {
            System.out.println("No doubts found.");
            return;
        }

        displayQuestions(lines);
        System.out.print("Enter question number to delete answer from: ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice < 1 || choice > lines.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        String question = lines.get(choice - 1).split("\\|")[0];
        List<String> answers = readLines(getAnswerFilename(question));

        if (answers.isEmpty()) {
            System.out.println("No answers to delete.");
            return;
        }

        System.out.println("Answers:");
        for (int i = 0; i < answers.size(); i++) {
            System.out.println((i + 1) + ". " + answers.get(i));
        }

        System.out.print("Enter answer number to delete: ");
        int del = Integer.parseInt(sc.nextLine());
        if (del < 1 || del > answers.size()) {
            System.out.println("Invalid number.");
            return;
        }

        answers.remove(del - 1);
        overwriteFile(getAnswerFilename(question), answers);
        System.out.println("Answer deleted.");
    }

    static void deleteQuestion() {
        List<String> lines = readLines(DOUBT_FILE);
        if (lines.isEmpty()) {
            System.out.println("No doubts found.");
            return;
        }

        displayQuestions(lines);
        System.out.print("Enter question number to delete: ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice < 1 || choice > lines.size()) {
            System.out.println("Invalid number.");
            return;
        }

        String question = lines.get(choice - 1).split("\\|")[0];
        lines.remove(choice - 1);
        overwriteFile(DOUBT_FILE, lines);

        File answerFile = new File(getAnswerFilename(question));
        if (answerFile.exists()) answerFile.delete();

        System.out.println("Question and its answers deleted.");
    }

    static void searchDoubt() {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine().toLowerCase();
        List<String> lines = readLines(DOUBT_FILE);
        boolean found = false;

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2 && parts[0].toLowerCase().contains(keyword)) {
                found = true;
                System.out.println("\nFound: " + parts[1] + ": " + parts[0]);
                List<String> answers = readLines(getAnswerFilename(parts[0]));
                if (answers.isEmpty()) {
                    System.out.println("No answers yet.");
                } else {
                    answers.forEach(a -> System.out.println("- " + a));
                }
            }
        }

        if (!found) {
            System.out.println("No similar doubts found.");
        }
    }

    static List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ignored) {
        }
        return lines;
    }

    static void writeToFile(String filename, String content, boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, append))) {
            bw.write(content);
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    static void overwriteFile(String filename, List<String> content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String line : content) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    static void displayQuestions(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|");
            if (parts.length >= 2)
                System.out.println((i + 1) + ". " + parts[1] + ": " + parts[0]);
        }
    }

    static String getAnswerFilename(String question) {
        return question.replaceAll("[^a-zA-Z0-9]", "_") + "_answers.txt";
    }
}