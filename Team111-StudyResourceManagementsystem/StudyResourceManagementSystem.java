//UCE2023509-Anushka Bora
//UCE2023511-Harshada Chaudhari
//UCE2023517-Shruti Desai

//Problem Statemnt:Develop a  study assistance platform for engineering students with:

//•Study Material Recommendations
//•Adaptive Question Paper Generation
//•Deadline & Task Manager 


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class StudyResourceManagementSystem {

    // User classes
    static abstract class User {
        String username;
        String password;

        User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    static class Admin extends User {
        Admin(String username, String password) {
            super(username, password);
        }
    }

    static class Student extends User {
        Map<Integer, String> submittedAssignments = new HashMap<>();

        Student(String username, String password) {
            super(username, password);
        }
    }

    // Study Resources
    static abstract class StudyResource {
        int id;
        String title;
        String Subject;
        String type;

        StudyResource(int id, String title, String Subject, String type) {
            this.id = id;
            this.title = title;
            this.Subject = Subject;
            this.type = type;
        }

        abstract void display();
    }

    static class YouTubeResource extends StudyResource {
        String url;

        YouTubeResource(int id, String title, String Subject, String url) {
            super(id, title, Subject, "YouTube");
            this.url = url;
        }

        @Override
        void display() {
            System.out.println("[" + id + "] YouTube Video: " + title + " | Subject: " + Subject + " | URL: " + url);
        }
    }

    static class BookResource extends StudyResource {
        String author;
        String link;

        BookResource(int id, String title, String Subject, String author, String link) {
            super(id, title, Subject, "Book");
            this.author = author;
            this.link = link;
        }

        @Override
        void display() {
            System.out.println("[" + id + "] Book: " + title + " by " + author + " | Subject: " + Subject + " | Link: " + link);
        }
    }

    static class AssignmentResource extends StudyResource {
        LocalDate deadline;

        AssignmentResource(int id, String title, String Subject, LocalDate deadline) {
            super(id, title, Subject, "Assignment");
            this.deadline = deadline;
        }

        @Override
        void display() {
            System.out.println("[" + id + "] Assignment: " + title + " | Subject: " + Subject + " | Deadline: " + deadline);
        }
    }

    static class ClassSession {
        String subject;
        Teacher teacher;
        Room room;

        ClassSession(String subject, Teacher teacher, Room room) {
            this.subject = subject;
            this.teacher = teacher;
            this.room = room;
        }
    }

    static class Teacher {
        String name;

        Teacher(String name) {
            this.name = name;
        }
    }

    static class Room {
        String name;

        Room(String name) {
            this.name = name;
        }
    }

    static class TimeSlot {
        String time;

        TimeSlot(String time) {
            this.time = time;
        }
    }

    static class Question {
        String questionText;
        String Subject;
        String difficulty; // "Easy", "Medium", "Hard"
        int marks;

        Question(String questionText, String Subject, String difficulty, int marks) {
            this.questionText = questionText;
            this.Subject = Subject;
            this.difficulty = difficulty;
            this.marks = marks;
        }
    }

    static class QuestionWithAnswer extends Question {
        String answer;

        QuestionWithAnswer(String questionText, String Subject, String difficulty, int marks, String answer) {
            super(questionText, Subject, difficulty, marks);
            this.answer = answer;
        }
    }

    // Data storage
    private static Map<String, Admin> admins = new HashMap<>();
    private static Map<String, Student> students = new HashMap<>();
    private static Map<Integer, StudyResource> resources = new HashMap<>();
    private static List<Question> questionBank = new ArrayList<>(); // Pool of questions
    private static int resourceIdCounter = 1;

    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        System.out.println("-----------Welcome to Study Resources Management System-----------");
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Admin");
            System.out.println("2. Student");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    adminMenu();
                    break;
                case "2":
                    studentMenu();
                    break;
                case "3":
                    System.out.println("Exiting !!!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    adminSignUp();
                    break;
                case "2":
                    Admin admin = adminLogin();
                    if (admin != null) adminActions(admin);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void adminSignUp() {
        System.out.print("Enter new admin username: ");
        String username = scanner.nextLine();
        if (admins.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        admins.put(username, new Admin(username, password));
        System.out.println("Admin registered successfully.");
    }

    private static Admin adminLogin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        Admin admin = admins.get(username);
        if (admin != null && admin.password.equals(password)) {
            System.out.println("Admin login successful.");
            return admin;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }

    private static void adminActions(Admin admin) {
        while (true) {
            System.out.println("\n=== Admin Actions ===");
            System.out.println("1. Add Book Resource");
            System.out.println("2. Add YouTube Resource");
            System.out.println("3. Add Assignment Resource");
            System.out.println("4. View All Resources");
            System.out.println("5. Generate Question Paper");
            System.out.println("6. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addBookResource();
                    break;
                case "2":
                    addYouTubeResource();
                    break;
                case "3":
                    addAssignmentResource();
                    break;
                case "4":
                    displayAllResources();
                    break;
                case "5":
                    generateQuestionPaper();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addYouTubeResource() {
        System.out.print("Enter Subject: ");
        String Subject = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();      
        System.out.print("Enter URL: ");
        String url = scanner.nextLine();
        resources.put(resourceIdCounter, new YouTubeResource(resourceIdCounter++, title, Subject, url));
        System.out.println("YouTube resource added.");
    }

    private static void addBookResource() {
        System.out.print("Enter Subject: ");
        String Subject = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();        
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter link: ");
        String link = scanner.nextLine();
        resources.put(resourceIdCounter, new BookResource(resourceIdCounter++, title, Subject, author, link));
        System.out.println("Book resource added.");
    }

    private static void addAssignmentResource() {
        System.out.print("Enter Subject: ");
        String Subject = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();        
        System.out.print("Enter deadline (yyyy-MM-dd): ");
        try {
            LocalDate deadline = LocalDate.parse(scanner.nextLine(), dateFormatter);
            resources.put(resourceIdCounter, new AssignmentResource(resourceIdCounter++, title, Subject, deadline));
            System.out.println("Assignment resource added.");
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    private static void displayAllResources() {
        if (resources.isEmpty()) {
            System.out.println("No resources available.");
            return;
        }
        System.out.println("\n=== All Study Resources ===");
        for (StudyResource res : resources.values()) {
            res.display();
        }
    }

    private static void generateQuestionPaper() {
        System.out.print("Enter subject name: ");
        String subject = scanner.nextLine();
        System.out.print("Enter total marks for the exam: ");
        int totalMarks = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter the number of questions to add: ");
        int numQuestions = Integer.parseInt(scanner.nextLine());

        List<Question> questionsForSubject = new ArrayList<>();
        for (int i = 1; i <= numQuestions; i++) {
            System.out.println("\nEnter details for question " + i + ":");
            System.out.print("Enter question text: ");
            String questionText = scanner.nextLine();
            System.out.print("Enter marks for the question: ");
            int marks = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter difficulty level (Easy, Medium, Hard): ");
            String difficulty = scanner.nextLine();
            System.out.print("Enter the answer for the question: ");
            String answer = scanner.nextLine();

            questionsForSubject.add(new QuestionWithAnswer(questionText, subject, difficulty, marks, answer));
        }

        questionBank.addAll(questionsForSubject);
        System.out.println("\nQuestions added successfully to the question bank.");

        List<Question> selectedQuestions = generateQuestionPaperFromBank(totalMarks, subject);
        if (selectedQuestions.isEmpty()) {
            System.out.println("Unable to generate a question paper with the given constraints.");
            return;
        }

        System.out.println("\n=== Generated Question Paper ===");
        int questionNumber = 1;
        for (Question q : selectedQuestions) {
            System.out.println(questionNumber++ + ". " + q.questionText + " (" + q.marks + " marks, " + q.difficulty + ")");
        }
    }

    private static List<Question> generateQuestionPaperFromBank(int totalMarks, String subject) {
        List<Question> selectedQuestions = new ArrayList<>();
        int marksAccumulated = 0;

        List<Question> filteredQuestions = questionBank.stream()
            .filter(q -> q.Subject.equalsIgnoreCase(subject))
            .collect(Collectors.toList());

        Collections.shuffle(filteredQuestions);

        for (Question q : filteredQuestions) {
            if (marksAccumulated + q.marks <= totalMarks) {
                selectedQuestions.add(q);
                marksAccumulated += q.marks;
            }
            if (marksAccumulated >= totalMarks) break;
        }

        return selectedQuestions;
    }

    private static void studentMenu() {
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    studentSignUp();
                    break;
                case "2":
                    Student student = studentLogin();
                    if (student != null) studentActions(student);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void studentSignUp() {
        System.out.print("Enter new student username: ");
        String username = scanner.nextLine();
        if (students.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        students.put(username, new Student(username, password));
        System.out.println("Student registered successfully.");
    }

    private static Student studentLogin() {
        System.out.print("Enter student username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        Student student = students.get(username);
        if (student != null && student.password.equals(password)) {
            System.out.println("Student login successful.");
            return student;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }

    private static void studentActions(Student student) {
        while (true) {
            System.out.println("\n=== Student Actions ===");
            System.out.println("1. View All Resources");
            System.out.println("2. Search Resources by Subject");
            System.out.println("3. Assignments Details");
            System.out.println("4. Upload Assignment");
            System.out.println("5. View Submitted Assignments"); 
                       
            System.out.println("6. Make Timetable");
            System.out.println("7. Solve Question Paper");
            System.out.println("8. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayAllResources();
                    break;
                case "2":
                    searchResourcesBySubject();
                    break;
                case "3":
                    createTimetable(student);                   
                    break;
                case "4":
                    uploadAssignment(student);                   
                    break;
                case "5":
                    viewSubmittedAssignments(student);
                    break;
                case "6":
                    createClassTimetable();
                    break;
                case "7":
                    solveQuestionPaper();
                    break;
                case "8":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void searchResourcesBySubject() {
        System.out.print("Enter Subject to search: ");
        String Subject = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (StudyResource res : resources.values()) {
            if (res.Subject.toLowerCase().equals(Subject)) {
                res.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No resources found under Subject: " + Subject);
        }
    }

    private static void uploadAssignment(Student student) {
        System.out.print("Enter Assignment ID to submit: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            StudyResource res = resources.get(id);
            if (res instanceof AssignmentResource) {
                AssignmentResource assignment = (AssignmentResource) res;
                if (LocalDate.now().isAfter(assignment.deadline)) {
                    System.out.println("Deadline has passed. Cannot submit.");
                    return;
                }
                System.out.print("Enter submission detail or file path: ");
                String submission = scanner.nextLine();
                student.submittedAssignments.put(id, submission);
                System.out.println("Submission successful.");
            } else {
                System.out.println("Invalid Assignment ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter numeric ID.");
        }
    }

    private static void viewSubmittedAssignments(Student student) {
        if (student.submittedAssignments.isEmpty()) {
            System.out.println("No assignments submitted.");
            return;
        }
        for (Map.Entry<Integer, String> entry : student.submittedAssignments.entrySet()) {
            StudyResource res = resources.get(entry.getKey());
            if (res != null) {
                System.out.println("Assignment: " + res.title + " | Submission: " + entry.getValue());
            }
        }
    }

    private static void createTimetable(Student student) {
        PriorityQueue<AssignmentResource> timetable = new PriorityQueue<>(Comparator.comparing(a -> a.deadline));
        for (StudyResource res : resources.values()) {
            if (res instanceof AssignmentResource) {
                timetable.add((AssignmentResource) res);
            }
        }

        if (timetable.isEmpty()) {
            System.out.println("No assignments available to create a timetable.");
            return;
        }

        System.out.println("\n=== Timetable ===");
        while (!timetable.isEmpty()) {
            AssignmentResource assignment = timetable.poll();
            System.out.println("Assignment: " + assignment.title + " | Deadline: " + assignment.deadline);
        }
    }

    private static boolean schedule(int classIndex, List<ClassSession> sessions, Map<Teacher, Set<TimeSlot>> teacherSchedule, Map<Room, Set<TimeSlot>> roomSchedule, List<TimeSlot> availableTimeSlots) {
        if (classIndex == sessions.size()) return true; // all scheduled

        ClassSession current = sessions.get(classIndex);
        for (TimeSlot slot : availableTimeSlots) {
            if (isAvailable(current, slot, teacherSchedule, roomSchedule)) {
                assignSlot(current, slot, teacherSchedule, roomSchedule);
                if (schedule(classIndex + 1, sessions, teacherSchedule, roomSchedule, availableTimeSlots)) return true;
                unassignSlot(current, slot, teacherSchedule, roomSchedule);
            }
        }
        return false; // backtrack
    }

    private static boolean isAvailable(ClassSession session, TimeSlot slot, Map<Teacher, Set<TimeSlot>> teacherSchedule, Map<Room, Set<TimeSlot>> roomSchedule) {
        return !teacherSchedule.getOrDefault(session.teacher, new HashSet<>()).contains(slot) &&
               !roomSchedule.getOrDefault(session.room, new HashSet<>()).contains(slot);
    }

    private static void assignSlot(ClassSession session, TimeSlot slot, Map<Teacher, Set<TimeSlot>> teacherSchedule, Map<Room, Set<TimeSlot>> roomSchedule) {
        teacherSchedule.computeIfAbsent(session.teacher, k -> new HashSet<>()).add(slot);
        roomSchedule.computeIfAbsent(session.room, k -> new HashSet<>()).add(slot);
    }

    private static void unassignSlot(ClassSession session, TimeSlot slot, Map<Teacher, Set<TimeSlot>> teacherSchedule, Map<Room, Set<TimeSlot>> roomSchedule) {
        teacherSchedule.get(session.teacher).remove(slot);
        roomSchedule.get(session.room).remove(slot);
    }

    private static void createClassTimetable() {
        List<ClassSession> sessions = new ArrayList<>();
        Map<Teacher, Set<TimeSlot>> teacherSchedule = new HashMap<>();
        Map<Room, Set<TimeSlot>> roomSchedule = new HashMap<>();
        Set<TimeSlot> availableTimeSlots = new HashSet<>(); // Use Set to ensure unique time slots

        System.out.println("Enter the number of sessions to schedule:");
        int sessionCount = Integer.parseInt(scanner.nextLine());

        Set<String> uniqueSessions = new HashSet<>();
        for (int i = 0; i < sessionCount; i++) {
            System.out.println("Enter details for session " + (i + 1) + ":");
            System.out.print("Subject: ");
            String subject = scanner.nextLine();
            if (uniqueSessions.contains(subject)) {
                System.out.println("Duplicate session detected. Skipping...");
                continue;
            }
            uniqueSessions.add(subject);

            System.out.print("Teacher Name: ");
            Teacher teacher = new Teacher(scanner.nextLine());
            System.out.print("Room Name: ");
            Room room = new Room(scanner.nextLine());
            System.out.print("Session Timing (e.g., 9:00-10:00): ");
            TimeSlot sessionTime = new TimeSlot(scanner.nextLine());

            if (!availableTimeSlots.add(sessionTime)) { // Ensure unique time slots
                System.out.println("Duplicate time slot detected. Skipping...");
                continue;
            }

            sessions.add(new ClassSession(subject, teacher, room));
            teacherSchedule.computeIfAbsent(teacher, k -> new HashSet<>()).add(sessionTime);
            roomSchedule.computeIfAbsent(room, k -> new HashSet<>()).add(sessionTime);
        }

        System.out.println("\n=== Timetable Created ===");
        for (ClassSession session : sessions) {
            TimeSlot assignedSlot = null;
            for (TimeSlot slot : availableTimeSlots) {
                if (teacherSchedule.get(session.teacher).contains(slot) && roomSchedule.get(session.room).contains(slot)) {
                    assignedSlot = slot;
                    break;
                }
            }
            if (assignedSlot != null) {
                System.out.println("Subject: " + session.subject + " | Teacher: " + session.teacher.name + " | Room: " + session.room.name + " | Time: " + assignedSlot.time);
            }
        }
    }

    private static void solveQuestionPaper() {
        System.out.print("Enter subject name to solve: ");
        String subject = scanner.nextLine();

        List<Question> subjectQuestions = questionBank.stream()
            .filter(q -> q.Subject.equalsIgnoreCase(subject))
            .collect(Collectors.toList());

        if (subjectQuestions.isEmpty()) {
            System.out.println("No question paper available for the subject: " + subject);
            return;
        }

        int totalMarks = subjectQuestions.stream().mapToInt(q -> q.marks).sum();
        System.out.println("\nSubject: " + subject);
        System.out.println("Total Questions: " + subjectQuestions.size());
        System.out.println("Total Marks: " + totalMarks);

        System.out.print("\nDo you want to start solving? (yes/no): ");
        if (!scanner.nextLine().equalsIgnoreCase("yes")) {
            return;
        }

        int score = 0;
        for (int i = 0; i < subjectQuestions.size(); i++) {
            QuestionWithAnswer question = (QuestionWithAnswer) subjectQuestions.get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + question.questionText + " (" + question.marks + " marks)");
            System.out.print("Your Answer: ");
            String studentAnswer = scanner.nextLine();

            if (studentAnswer.equalsIgnoreCase(question.answer)) {
                score += question.marks;
                System.out.println("Correct!");
            } else {
                System.out.println("Wrong! Correct Answer: " + question.answer);
            }
        }

        System.out.println("\n=== Result ===");
        System.out.println("Your Score: " + score + "/" + totalMarks);
    }
}
