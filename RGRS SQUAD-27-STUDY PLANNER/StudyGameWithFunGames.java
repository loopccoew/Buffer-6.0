import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudyGameWithFunGames {
    static final int BOARD_SIZE = 100;
    static final int[] snakes = {17, 54, 62, 64, 87, 93, 95, 99, 45, 52, 78, 85, 90};
    static final int[] snakeEnds = {7, 34, 19, 36, 24, 73, 75, 78, 25, 42, 60, 65, 72};
    static final int[] ladders = {3, 6, 20, 36, 63, 68, 9, 14, 28, 40, 50, 61};
    static final int[] ladderEnds = {22, 25, 70, 55, 95, 98, 31, 37, 46, 59, 66, 79};
    static final String[] questions = {
            "Solve: 2x + 3 = 11. What is x?",
            "Find the roots of the equation: x^2 - 5x + 6 = 0. Enter smaller root.",
            "What is the value of sin(30 degrees)? (Enter as decimal, e.g., 0.5)",
            "Find the 10th term of the AP: 2, 5, 8, 11, ...",
            "What is the probability of getting a 3 on a fair die? (Enter as decimal)",
            "Find the area of a circle with radius 7. (Use pi = 3.14)",
            "What is the mode of: 3, 4, 4, 5, 6, 6, 6, 7?",
            "The sum of the first 10 natural numbers is?",
            "Find the HCF of 36 and 60",
            "If tan A = 1, what is angle A in degrees?"
    };
    static final String[] answers = {
            "4", "2", "0.5", "29", "0.166", "153.86", "6", "55", "12", "45"
    };

    // Study Planner data structures
    static Map<String, ArrayList<StudySession>> studyPlan = new HashMap<>();
    static String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeStudyPlan();

        System.out.println("🎓 Welcome to the Study + Game Quiz and Planner!");

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Subject Quiz + Games");
            System.out.println("2. Study Planner");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    runSubjectQuiz(scanner);
                    break;
                case "2":
                    runStudyPlanner(scanner);
                    break;
                case "3":
                    System.out.println("👋 Goodbye! Happy studying!");
                    return;
                default:
                    System.out.println("❗ Please enter a valid option (1-3).");
            }
        }
    }

    public static void runSubjectQuiz(Scanner scanner) {
        int totalCorrect = 0;
        boolean mathDone = false, scienceDone = false, englishDone = false;

        System.out.println("\n🎓 Welcome to the Study + Game Quiz!");
        System.out.println("Choose a subject to start (Math / Science / English)");

        while (!mathDone || !scienceDone || !englishDone) {
            System.out.print("\n📘 Enter subject (or 'back' to return to main menu): ");
            String subject = scanner.nextLine().trim().toLowerCase();

            if (subject.equals("back")) {
                return;
            }

            switch (subject) {
                case "math":
                    if (mathDone) {
                        System.out.println("✅ Math is already completed.");
                        break;
                    }
                    System.out.println("📚 Subject: Math");
                    totalCorrect += askQuestion(scanner, "What is 5 + 3?", "8", "math");
                    totalCorrect += askQuestion(scanner, "What is 10 / 2?", "5", "math");
                    totalCorrect += askQuestion(scanner, "Is 7 an even number? (yes/no)", "no", "math");
                    mathDone = true;
                    break;

                case "science":
                    if (scienceDone) {
                        System.out.println("✅ Science is already completed.");
                        break;
                    }
                    System.out.println("📚 Subject: Science");
                    totalCorrect += askQuestion(scanner, "What planet is known as the Red Planet?", "Mars", "science");
                    totalCorrect += askQuestion(scanner, "Do humans breathe carbon dioxide? (yes/no)", "no", "science");
                    totalCorrect += askQuestion(scanner, "How many states of matter? (Enter number)", "3", "science");
                    scienceDone = true;
                    break;

                case "english":
                    if (englishDone) {
                        System.out.println("✅ English is already completed.");
                        break;
                    }
                    System.out.println("📚 Subject: English");
                    totalCorrect += askQuestion(scanner, "Unscramble this: 'ESUOH'", "HOUSE", "english");
                    totalCorrect += askQuestion(scanner, "Fill the blank: She ___ happy.", "is", "english");
                    totalCorrect += askQuestion(scanner, "What rhymes with 'cat'?", "hat", "english");
                    englishDone = true;
                    break;

                default:
                    System.out.println("❗ Please enter a valid subject: Math, Science, or English.");
            }
        }

        System.out.println("\n🎉 All subjects completed!");
        System.out.println("✅ You answered " + totalCorrect + " out of 9 questions correctly!");
    }

    public static void runStudyPlanner(Scanner scanner) {
        while (true) {
            System.out.println("\n📅 Study Planner Menu:");
            System.out.println("1. Add Study Session");
            System.out.println("2. View Study Plan");
            System.out.println("3. Remove Study Session");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option (1-4): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addStudySession(scanner);
                    break;
                case "2":
                    viewStudyPlan();
                    break;
                case "3":
                    removeStudySession(scanner);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("❗ Please enter a valid option (1-4).");
            }
        }
    }

    public static void initializeStudyPlan() {
        for (String day : daysOfWeek) {
            studyPlan.put(day, new ArrayList<StudySession>());
        }
    }

    public static void addStudySession(Scanner scanner) {
        System.out.println("\n➕ Add a Study Session");

        // Get day of week
        System.out.println("Available days: Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday");
        System.out.print("Enter day: ");
        String day = scanner.nextLine().trim();

        if (!studyPlan.containsKey(day)) {
            System.out.println("❗ Invalid day. Please enter a valid day of the week.");
            return;
        }

        // Get subject
        System.out.print("Enter subject (Math/Science/English): ");
        String subject = scanner.nextLine().trim().toLowerCase();
        if (!subject.equals("math") && !subject.equals("science") && !subject.equals("english")) {
            System.out.println("❗ Invalid subject. Please enter Math, Science, or English.");
            return;
        }

        // Get time
        System.out.print("Enter start time (e.g., 14:30): ");
        String startTime = scanner.nextLine().trim();
        System.out.print("Enter end time (e.g., 15:30): ");
        String endTime = scanner.nextLine().trim();

        // Get topic
        System.out.print("Enter topic to study: ");
        String topic = scanner.nextLine().trim();

        // Create and add study session
        StudySession session = new StudySession(subject, startTime, endTime, topic);
        studyPlan.get(day).add(session);

        System.out.println("✅ Study session added successfully!");
    }

    public static void viewStudyPlan() {
        System.out.println("\n📅 Your Weekly Study Plan:");

        for (String day : daysOfWeek) {
            System.out.println("\n" + day + ":");
            ArrayList<StudySession> sessions = studyPlan.get(day);

            if (sessions.isEmpty()) {
                System.out.println("  No study sessions scheduled");
            } else {
                for (int i = 0; i < sessions.size(); i++) {
                    StudySession session = sessions.get(i);
                    System.out.println("  " + (i+1) + ". " + session);
                }
            }
        }
    }

    public static void removeStudySession(Scanner scanner) {
        System.out.println("\n➖ Remove a Study Session");
        System.out.print("Enter day: ");
        String day = scanner.nextLine().trim();

        if (!studyPlan.containsKey(day)) {
            System.out.println("❗ Invalid day. Please enter a valid day of the week.");
            return;
        }

        ArrayList<StudySession> sessions = studyPlan.get(day);
        if (sessions.isEmpty()) {
            System.out.println("No study sessions scheduled for " + day);
            return;
        }

        System.out.println("Scheduled sessions for " + day + ":");
        for (int i = 0; i < sessions.size(); i++) {
            System.out.println((i+1) + ". " + sessions.get(i));
        }

        System.out.print("Enter the number of the session to remove: ");
        try {
            int sessionNum = Integer.parseInt(scanner.nextLine().trim());
            if (sessionNum < 1 || sessionNum > sessions.size()) {
                System.out.println("❗ Invalid session number.");
                return;
            }

            StudySession removed = sessions.remove(sessionNum - 1);
            System.out.println("✅ Removed: " + removed);
        } catch (NumberFormatException e) {
            System.out.println("❗ Please enter a valid number.");
        }
    }
    public static int askQuestion(Scanner scanner, String question, String correctAnswer, String subject) {
        System.out.println("❓ " + question);
        System.out.print("Your answer: ");
        String userAnswer = scanner.nextLine().trim();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            System.out.println("✅ Correct!");
            playGame(scanner, subject);
            return 1;
        } else {
            System.out.println("❌ Incorrect.");
            return 0;
        }
    }

    // Fun games based on subject
    public static void playGame(Scanner scanner, String subject) {
        switch (subject) {
            case "math":
                System.out.println("🎮 Math Game: Snake & Ladder");
                snakeAndLadder(scanner);
                break;

            case "science":
                System.out.println("🎮 Science Game: Rock-Paper-Scissors");
                rockPaperScissors(scanner);
                break;

            case "english":
                System.out.println("🎮 English Game: Tic-Tac-Toe");
                ticTacToe(scanner);
                break;
        }
    }

    // Updated Snake & Ladder game
    public static void snakeAndLadder(Scanner scanner) {
        System.out.println("🎲 Welcome to Snake & Ladder!");
        Random rand = new Random();
        int playerPosition = 0;

        while (playerPosition < BOARD_SIZE) {
            System.out.println("You are at position: " + playerPosition);
            System.out.println("Press Enter to roll the dice...");
            scanner.nextLine();

            int diceRoll = rand.nextInt(6) + 1;
            System.out.println("You rolled a: " + diceRoll);
            playerPosition += diceRoll;

            if (playerPosition > BOARD_SIZE) {
                playerPosition = BOARD_SIZE;
            }

            playerPosition = checkSnakesAndLadders(playerPosition, scanner);
        }

        System.out.println("Congratulations! You reached the end!");
    }

    private static int checkSnakesAndLadders(int position, Scanner scanner) {
        for (int i = 0; i < snakes.length; i++) {
            if (position == snakes[i]) {
                System.out.println("Oh no! You stepped on a snake! Answer 3 Class 10 maths questions to avoid sliding down.");
                if (!askNCERTQuestions(scanner, 3)) {
                    System.out.println("You go down to " + snakeEnds[i]);
                    return snakeEnds[i];
                } else {
                    System.out.println("You stay at " + position);
                }
            }
        }

        for (int i = 0; i < ladders.length; i++) {
            if (position == ladders[i]) {
                System.out.println("Yay! You found a ladder! Answer 3 Class 10 maths questions to climb.");
                if (!askNCERTQuestions(scanner, 3)) {
                    System.out.println("Oops! You stay at " + position);
                    return position;
                } else {
                    System.out.println("Correct! You climb to " + ladderEnds[i]);
                    return ladderEnds[i];
                }
            }
        }

        return position;
    }

    private static boolean askNCERTQuestions(Scanner scanner, int count) {
        Random rand = new Random();
        int correct = 0;

        for (int i = 0; i < count; i++) {
            int index = rand.nextInt(questions.length);
            System.out.println("Q" + (i + 1) + ": " + questions[index]);
            String response = scanner.nextLine().trim();

            if (response.equalsIgnoreCase(answers[index])) {
                System.out.println("Correct!");
                correct++;
            } else {
                System.out.println("Wrong! Correct answer: " + answers[index]);
                return false; // Fail on any wrong answer
            }
        }

        return true;
    }

    // Rock-Paper-Scissors game
    public static void rockPaperScissors(Scanner scanner) {
        System.out.println("🪞 Welcome to Rock-Paper-Scissors!");
        String[] options = {"Rock", "Paper", "Scissors"};
        Random rand = new Random();
        System.out.print("Choose Rock, Paper, or Scissors: ");
        String userChoice = scanner.nextLine().trim();

        String computerChoice = options[rand.nextInt(3)];
        System.out.println("Computer chose: " + computerChoice);

        if (userChoice.equalsIgnoreCase(computerChoice)) {
            System.out.println("It's a tie!");
        } else if ((userChoice.equalsIgnoreCase("Rock") && computerChoice.equalsIgnoreCase("Scissors")) ||
                (userChoice.equalsIgnoreCase("Paper") && computerChoice.equalsIgnoreCase("Rock")) ||
                (userChoice.equalsIgnoreCase("Scissors") && computerChoice.equalsIgnoreCase("Paper"))) {
            System.out.println("🎉 You win!");
        } else {
            System.out.println("❌ You lose.");
        }
    }

    // Tic-Tac-Toe game
    public static void ticTacToe(Scanner scanner) {
        System.out.println("🧩 Welcome to Tic-Tac-Toe!");
        char[][] board = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };
        printBoard(board);
        char currentPlayer = 'X';

        for (int turn = 0; turn < 9; turn++) {
            System.out.println("Player " + currentPlayer + "'s turn.");
            System.out.print("Enter row (1-3): ");
            int row = scanner.nextInt() - 1;
            System.out.print("Enter column (1-3): ");
            int col = scanner.nextInt() - 1;
            scanner.nextLine();  // clear buffer

            if (board[row][col] != ' ') {
                System.out.println("❌ Cell already taken. Try again.");
                turn--;  // Stay on the same turn
            } else {
                board[row][col] = currentPlayer;
                printBoard(board);
                if (checkWin(board, currentPlayer)) {
                    System.out.println("🎉 Player " + currentPlayer + " wins!");
                    return;
                }
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';  // Switch player
            }
        }
        System.out.println("It's a tie!");
    }

    // Print Tic-Tac-Toe board
    public static void printBoard(char[][] board) {
        for (int i = 0; i < 3; i++) {
            System.out.println(" " + board[i][0] + " | " + board[i][1] + " | " + board[i][2]);
            if (i < 2) System.out.println("---|---|---");
        }
    }

    // Check for Tic-Tac-Toe win
    public static boolean checkWin(char[][] board, char player) {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
            return true;
        }
        return false;
    }
}



class StudySession {
    private String subject;
    private String startTime;
    private String endTime;
    private String topic;

    public StudySession(String subject, String startTime, String endTime, String topic) {
        this.subject = subject.substring(0, 1).toUpperCase() + subject.substring(1);
        this.startTime = startTime;
        this.endTime = endTime;
        this.topic = topic;
    }

    @Override
    public String toString() {
        return subject + " (" + startTime + "-" + endTime + "): " + topic;
    }
}