package studyflow;


import java.util.*;

import java.util.concurrent.*;

import java.util.*;

import java.util.concurrent.*;



// Main Class

public class StudyFlow {

 public static void main(String[] args) {

 Scanner scanner = new Scanner(System.in);

 SchedulerEngine scheduler = new SchedulerEngine();

 FlashcardStack flashcardStack = new FlashcardStack();

 SpacedRevisionReminder spacedReminder = new SpacedRevisionReminder();

 ProgressTracker progressTracker = new ProgressTracker(); // Dynamic progress tracker



 while (true) {

 System.out.println("\nWelcome to the Smart Study Scheduler!");

 System.out.println("Choose an option:");

 System.out.println("1. Study Scheduler");

 System.out.println("2. Flashcard Revision");

 System.out.println("3. Spaced Revision Reminder");

 System.out.println("4. Progress Tracker");

 System.out.println("5. Exit");



 int choice = scanner.nextInt();

 scanner.nextLine(); // Consume newline



 switch (choice) {

 case 1:

 // Study Scheduler

 System.out.println("Choose a track:");

 System.out.println("1. DSA Comprehensive (Arrays → Strings → Linked Lists → Stacks → Queues → Trees → Graphs → Sorting → Searching → Dynamic Programming)");

 System.out.println("2. Core Subjects (OS, DBMS, CN, OOP)");

 System.out.println("3. Custom Track");



 int trackChoice = scanner.nextInt();

 scanner.nextLine(); // Consume newline



 List<String> selectedTopics = new ArrayList<>();

 switch (trackChoice) {

 case 1:

 List<String> dsaComprehensiveTopics = Arrays.asList("Arrays", "Strings", "Linked Lists", "Stacks", "Queues", "Trees", "Graphs", "Sorting", "Searching", "Dynamic Programming");

 scheduler.addTrack("DSA Comprehensive", dsaComprehensiveTopics);

 selectedTopics.addAll(dsaComprehensiveTopics);

 // Define some basic dependencies based on difficulty

 scheduler.addDependency("Arrays", "Linked Lists");

 scheduler.addDependency("Arrays", "Stacks");

 scheduler.addDependency("Arrays", "Queues");

 scheduler.addDependency("Linked Lists", "Trees");

 scheduler.addDependency("Stacks", "Trees");

 scheduler.addDependency("Queues", "Trees");

 scheduler.addDependency("Trees", "Graphs");

 scheduler.addDependency("Arrays", "Sorting");

 scheduler.addDependency("Arrays", "Searching");

 scheduler.addDependency("Searching", "Dynamic Programming");

 scheduler.addDependency("Sorting", "Dynamic Programming");

 break;

 case 2:

 List<String> coreSubjectsTopics = Arrays.asList("Operating Systems", "DBMS", "Computer Networks", "OOP");

 scheduler.addTrack("Core Subjects", coreSubjectsTopics);

 selectedTopics.addAll(coreSubjectsTopics);

 break;

 case 3:

 System.out.println("Enter custom topics (comma-separated):");

 String customTopics = scanner.nextLine();

 selectedTopics.addAll(Arrays.asList(customTopics.split(",")));

 scheduler.addTrack("Custom Track", selectedTopics);



 System.out.println("Enter dependencies in the format 'topic1-topic2' (topic2 depends on topic1):");

 String dependency = scanner.nextLine();

 while (!dependency.isEmpty()) {

 String[] parts = dependency.split("-");

 scheduler.addDependency(parts[0].trim(), parts[1].trim());

 System.out.println("Enter next dependency or leave blank:");

 dependency = scanner.nextLine();

 }

 break;



 default:

 System.out.println("Invalid track choice.");

 return;

 }



 System.out.println("Enter available study time per day (in hours):");

 int dailyTime = scanner.nextInt();

 System.out.println("Enter the final deadline (in days from today):");

 int deadline = scanner.nextInt();



 // Get estimated time for each topic

 Map<String, Integer> topicTimes = new HashMap<>();

 System.out.println("\nEnter estimated study time (in hours) for each topic:");

 for (String topic : selectedTopics) {

 System.out.print("Time for " + topic + ": ");

 int time = scanner.nextInt();

 topicTimes.put(topic, time);

 scheduler.setEstimatedTime(topic, time); // Set estimated time in scheduler

 }

 scanner.nextLine(); // Consume newline



 List<String> dayWiseSchedule = scheduler.generateDayWiseSchedule(selectedTopics, dailyTime, deadline);

 System.out.println("\nYour Day-Wise Study Schedule:");

 for (String daySchedule : dayWiseSchedule) {

 System.out.println(daySchedule);

 }

 break;



 case 2:

 // Flashcard Revision

 System.out.println("Flashcard Revision:");

 System.out.println("1. Add a flashcard");

 System.out.println("2. Revise flashcards");



 int flashcardChoice = scanner.nextInt();

 scanner.nextLine(); // Consume newline



 if (flashcardChoice == 1) {

 // Add a flashcard

 System.out.println("Enter flashcard category (e.g., Formulas, Theory):");

 String category = scanner.nextLine();

 System.out.println("Enter a flashcard question:");

 String question = scanner.nextLine();

 System.out.println("Enter the answer:");

 String answer = scanner.nextLine();

 flashcardStack.addFlashcard(category, question, answer);

 } else if (flashcardChoice == 2) {

 // Revise flashcards

 flashcardStack.reviseFlashcards();

 } else {

 System.out.println("Invalid choice. Please try again.");

 }

 break;



 case 3:

 // Spaced Revision Reminder

 System.out.println("Enter a topic for spaced revision:");

 String topic = scanner.nextLine();

 spacedReminder.addTopic(topic);



 System.out.println("Starting reminders...");

 spacedReminder.startReminder();



 System.out.println("Press 0 to stop reminders.");

 int stopChoice = scanner.nextInt();

 if (stopChoice == 0) {

 spacedReminder.stopReminder();

 }

 break;



 case 4:

 // Progress Tracker

 System.out.println("Mark progress for completed topics.");

 progressTracker.markComplete();

 break;



 case 5:

 System.out.println("Exiting... Happy Studying!");

 scanner.close();

 return;



 default:

 System.out.println("Invalid choice. Please try again.");

 }

 }

 }

}



// Scheduler Engine Class

class SchedulerEngine {

 private Map<String, List<String>> tracks;

 private Map<String, List<String>> dependencies;

 private Map<String, Integer> priorities;

 private Map<String, Integer> estimatedTime; // Estimated time for each topic (in hours)



 public SchedulerEngine() {

 tracks = new HashMap<>();

 dependencies = new HashMap<>();

 priorities = new HashMap<>();

 estimatedTime = new HashMap<>();

 }

 public void setEstimatedTime(String topic, int time) {

 estimatedTime.put(topic, time);

 }



 public void addTrack(String trackName, List<String> topics) {

 tracks.put(trackName, topics);

 for (String topic : topics) {

 dependencies.putIfAbsent(topic, new ArrayList<>());

 priorities.put(topic, 1); // Default priority

 // You might want to prompt the user for estimated time here or set a default

 estimatedTime.put(topic, 1); // Default to 1 hour per topic

 }

 }



 public void addDependency(String topic1, String topic2) {

 dependencies.get(topic1).add(topic2);

 }



 public List<String> generateDayWiseSchedule(List<String> allTopics, int dailyTime, int deadline) {

 List<String> sortedTopics = topologicalSort(allTopics);

 Map<String, Integer> remainingTime = new HashMap<>();

 for (String topic : sortedTopics) {

 // You might want to let the user set estimated time per topic

 remainingTime.put(topic, 1); // Default to 1 hour

 }



 List<String> dayWiseSchedule = new ArrayList<>();

 int currentDay = 1;

 List<String> topicsForToday = new ArrayList<>();

 int timeSpentToday = 0;

 int topicIndex = 0;



 while (topicIndex < sortedTopics.size() && currentDay <= deadline) {

 String currentTopic = sortedTopics.get(topicIndex);

 int topicTime = remainingTime.get(currentTopic);



 if (timeSpentToday + topicTime <= dailyTime) {

 topicsForToday.add(currentTopic);

 timeSpentToday += topicTime;

 topicIndex++;

 } else {

 if (!topicsForToday.isEmpty()) {

 dayWiseSchedule.add("Day " + currentDay + ": " + String.join(", ", topicsForToday));

 }

 currentDay++;

 topicsForToday = new ArrayList<>();

 timeSpentToday = 0;

 }

 }



 // Add any remaining topics for the last day

 if (!topicsForToday.isEmpty()) {

 dayWiseSchedule.add("Day " + currentDay + ": " + String.join(", ", topicsForToday));

 }



 // Handle cases where not all topics can be scheduled within the deadline

 if (topicIndex < sortedTopics.size()) {

 System.out.println("\nWarning: Not all topics could be scheduled within the " + deadline + " days.");

 List<String> remaining = sortedTopics.subList(topicIndex, sortedTopics.size());

 System.out.println("Remaining topics: " + String.join(", ", remaining));

 }



 return dayWiseSchedule;

 }



 private List<String> topologicalSort(List<String> topicsToSort) {

 Map<String, Integer> inDegree = new HashMap<>();

 Map<String, List<String>> adjList = new HashMap<>();



 // Initialize inDegree and adjacency list based on the provided topics

 for (String topic : topicsToSort) {

 inDegree.put(topic, 0);

 adjList.put(topic, new ArrayList<>());

 }



 // Populate inDegree based on dependencies relevant to the selected topics

 for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {

 String fromTopic = entry.getKey();

 if (inDegree.containsKey(fromTopic)) {

 for (String toTopic : entry.getValue()) {

 if (inDegree.containsKey(toTopic)) {

 adjList.get(fromTopic).add(toTopic);

 inDegree.put(toTopic, inDegree.get(toTopic) + 1);

 }

 }

 }

 }



 Queue<String> queue = new LinkedList<>();

 for (String topic : inDegree.keySet()) {

 if (inDegree.get(topic) == 0) {

 queue.add(topic);

 }

 }



 List<String> result = new ArrayList<>();

 while (!queue.isEmpty()) {

 String topic = queue.poll();

 result.add(topic);



 for (String neighbor : adjList.getOrDefault(topic, new ArrayList<>())) {

 inDegree.put(neighbor, inDegree.get(neighbor) - 1);

 if (inDegree.get(neighbor) == 0) {

 queue.add(neighbor);

 }

 }

 }



 // Check for cycles (if the result size is not equal to the number of topics)

 if (result.size() != topicsToSort.size()) {

 System.out.println("Warning: Cycle detected in dependencies! Schedule might not be optimal.");

 }



 return result;

 }

}



// Flashcard System Class

class FlashcardStack {

 private Map<String, List<String>> flashcards;



 public FlashcardStack() {

 flashcards = new HashMap<>();

 }



 public void addFlashcard(String category, String question, String answer) {

 flashcards.computeIfAbsent(category, k -> new ArrayList<>())

 .add(question + " | " + answer);

 }



 public void reviseFlashcards() {

 if (flashcards.isEmpty()) {

 System.out.println("No flashcards available. Please add some first.");

 return;

 }



 Random random = new Random();

 List<String> allCards = new ArrayList<>();



 for (List<String> cards : flashcards.values()) {

 allCards.addAll(cards);

 }



 String randomCard = allCards.get(random.nextInt(allCards.size()));

 String[] splitCard = randomCard.split(" \\| ");

 System.out.println("Question: " + splitCard[0]);



 Scanner scanner = new Scanner(System.in);

 System.out.println("Press Enter to see the answer...");

 scanner.nextLine();



 System.out.println("Answer: " + splitCard[1]);

 }

}



// Spaced Revision Reminder Class

class SpacedRevisionReminder {

 private Queue<String> queue;

 private Map<String, Date> lastReviewed;

 private ScheduledExecutorService scheduler;



 public SpacedRevisionReminder() {

 queue = new LinkedList<>();

 lastReviewed = new HashMap<>();

 scheduler = Executors.newScheduledThreadPool(1);

 }



 public void addTopic(String topic) {

 queue.add(topic);

 lastReviewed.put(topic, new Date());

 }



 public void startReminder() {

 scheduler.scheduleAtFixedRate(() -> {

 System.out.println("Reminder: Review these topics:");

 for (String topic : queue) {

 System.out.println(topic + " - Last reviewed: " + lastReviewed.get(topic));

 }

 }, 0, 24, TimeUnit.SECONDS); // Reminders every 24 hours

 }

 public void stopReminder() {

 scheduler.shutdown();

 System.out.println("Reminders stopped.");

 }

 }

class ProgressTracker {

 private Set<String> completedTopics;

 private Scanner scanner;

 private List<String> schedule;



 public ProgressTracker() {

 completedTopics = new HashSet<>();

 scanner = new Scanner(System.in);

 schedule = new ArrayList<>();

 }



 public void setSchedule(List<String> schedule) {

 this.schedule = schedule;

 }



 public void markComplete() {

 System.out.println("Enter the name of the topic you have completed (or leave blank to finish):");

 String topic = scanner.nextLine().trim();

 while (!topic.isEmpty()) {

 completedTopics.add(topic);

 System.out.println("Marked '" + topic + "' as complete.");

 System.out.println("Enter the next completed topic or leave blank:");

 topic = scanner.nextLine().trim();

 }

 System.out.println("Completed topics: " + completedTopics);

 }



 public void markScheduleComplete() {

 if (schedule.isEmpty()) {

 System.out.println("No schedule available yet. Please generate a schedule first.");

 return;

 }



 Set<String> allScheduledTopics = new HashSet<>();

 for (String daySchedule : schedule) {

 String[] parts = daySchedule.split(": ");

 if (parts.length > 1) {

 String[] topics = parts[1].split(",");

 for (String topic : topics) {

 allScheduledTopics.add(topic.trim().split("\\(")[0].trim()); // Remove time info

 }

 }

 }



 System.out.println("\nMark completion based on your schedule:");

 for (String daySchedule : schedule) {

 System.out.println("\n" + daySchedule);

 String[] parts = daySchedule.split(": ");

 if (parts.length > 1) {

 String[] topics = parts[1].split(",");

 for (int i = 0; i < topics.length; i++) {

 String topic = topics[i].trim().split("\\(")[0].trim();

 if (!completedTopics.contains(topic)) {

 System.out.print("Have you completed '" + topic + "'? (yes/no): ");

 String response = scanner.nextLine().trim().toLowerCase();

 if (response.equals("yes")) {

 completedTopics.add(topic);

 }

 } else {

 System.out.println("'" + topic + "' is already marked as completed.");

 }

 }

 }

 }

 displayProgress();

 }



 public void displayProgress() {

 if (schedule.isEmpty()) {

 System.out.println("No schedule to track progress against.");

 return;

 }

}}