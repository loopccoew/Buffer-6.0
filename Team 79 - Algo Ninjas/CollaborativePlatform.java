package buffer;
import java.util.*;
import java.util.stream.Collectors;

class User {
    String username;
    String password;
    String tagline;
    String joinReason;
    List<String> projectTypes;
    List<String> certifications;
    List<String> achievements;
    List<String> leadershipRoles;
    List<String> volunteeringInterests;
    List<String> nicheAreas;
    List<String> interests;
    int experienceLevel;
    int collaborationValue;
    List<User> friends;
    List<Group> groups;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.friends = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.projectTypes = new ArrayList<>();
        this.certifications = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.leadershipRoles = new ArrayList<>();
        this.volunteeringInterests = new ArrayList<>();
        this.nicheAreas = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.collaborationValue = 0;
    }

    public void calculateCollaborationValue() {
        int value = 0;
        value += projectTypes.size() * 5;
        value += certifications.size() * 3;
        value += achievements.size() * 10;
        value += leadershipRoles.size() * 7;
        value += volunteeringInterests.size() * 4;
        value += nicheAreas.size() * 3;
        value += interests.size() * 2;
        this.collaborationValue = value;
    }

    @Override
    public String toString() {
        return "Username: " + username + "\n" +
               "Tagline: " + tagline + "\n" +
               "Why I joined: " + joinReason + "\n" +
               "Project Types: " + String.join(", ", projectTypes) + "\n" +
               "Certifications: " + String.join(", ", certifications) + "\n" +
               "Achievements: " + String.join(", ", achievements) + "\n" +
               "Leadership Roles: " + String.join(", ", leadershipRoles) + "\n" +
               "Volunteering Interests: " + String.join(", ", volunteeringInterests) + "\n" +
               "Niche Areas: " + String.join(", ", nicheAreas) + "\n" +
               "Interests: " + String.join(", ", interests) + "\n" +
               "Experience Level: " + experienceLevel + "\n" +
               "Collaboration Value: " + collaborationValue + "\n" +
               "Friends: " + friends.size() + "\n" +
               "Groups: " + groups.size();
    }
}

class Group {
    String name;
    List<User> members;
    User admin;

    public Group(String name, User admin) {
        this.name = name;
        this.admin = admin;
        this.members = new ArrayList<>();
        this.members.add(admin);
    }
}

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;
    List<String> words;

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.words = new ArrayList<>();
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current = current.children.computeIfAbsent(ch, c -> new TrieNode());
            current.words.add(word.toLowerCase());
        }
        current.isEndOfWord = true;
    }

    public List<String> autocomplete(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return Collections.emptyList();
            }
            current = node;
        }
        return current.words.stream()
                .filter(word -> word.startsWith(prefix.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAllWords() {
        List<String> words = new ArrayList<>();
        getAllWordsFromNode(root, "", words);
        return words;
    }

    private void getAllWordsFromNode(TrieNode node, String currentWord, List<String> words) {
        if (node.isEndOfWord) {
            words.add(currentWord);
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            getAllWordsFromNode(entry.getValue(), currentWord + entry.getKey(), words);
        }
    }
}

class DisjointSet {
    private Map<String, String> parent;

    public DisjointSet() {
        parent = new HashMap<>();
    }

    public void makeSet(String user) {
        parent.put(user, user);
    }

    public String find(String user) {
        if (parent.get(user).equals(user)) {
            return user;
        }
        return find(parent.get(user));
    }

    public void union(String user1, String user2) {
        String root1 = find(user1);
        String root2 = find(user2);
        if (!root1.equals(root2)) {
            parent.put(root2, root1);
        }
    }

    public boolean areConnected(String user1, String user2) {
        return find(user1).equals(find(user2));
    }
}

public class CollaborativePlatform {
    private static Map<String, User> users = new HashMap<>();
    private static Map<Integer, List<User>> usersByExperience = new HashMap<>();
    private static Trie interestsTrie = new Trie();
    private static List<Group> groups = new ArrayList<>();
    private static DisjointSet groupConnections = new DisjointSet();
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeInterests();
        showMainMenu();
    }

    private static void initializeInterests() {
        String[] defaultInterests = {
            "web development", "machine learning", "app development", 
            "data science", "cybersecurity", "iot", "creative", 
            "analytical", "technical", "leadership", "design", 
            "research", "python", "java", "javascript", "c++", 
            "react", "angular", "node.js", "docker", "kubernetes"
        };
        for (String interest : defaultInterests) {
            interestsTrie.insert(interest);
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("========== COLLABKAR ==========");
            System.out.println("1. Enter the platform");
            System.out.println("2. Search Users");
            System.out.println("   a. By Experience Level ");
            System.out.println("   b. Smart Matchmaking ");
            System.out.println("3. Friend & Collaboration Network");
            System.out.println("   a. Add Friend");
            System.out.println("   b. View Friends / Connections");
            System.out.println("   c. Suggest Friends ");
            System.out.println("4. Study Group / Project Teams");
            System.out.println("   a. Create Group");
            System.out.println("   b. Add Member to Group");
            System.out.println("   c. Check if Two Users Are in Same Group ");
            System.out.println("   d. Merge Groups");
            System.out.println("5. Interest Management");
            System.out.println("   a. View All Interests ");
            System.out.println("6. Random User Connect");
            System.out.println("   a. Random Match");
            System.out.println("   b. Random Based on Interests");
            System.out.println("7. User Profile");
            System.out.println("   a. View My Profile");
            System.out.println("   b. View Others' Profiles");
            System.out.println("8. Exit");
            System.out.println("====================================");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                	System.out.println("\n1. New User - Register");
                	System.out.println("2. Existing User - Login");
                	System.out.print("Enter your choice: ");
                	int regChoice = Integer.parseInt(scanner.nextLine());

                	if (regChoice == 1) {
                	    registerNewUser();
                	} else if (regChoice == 2) {
                	    loginUser();
                	} else {
                	    System.out.println("Invalid choice. Returning to main menu...");
                	}
                	
                    break;
                case "2":
                    System.out.print("Enter search option (a/b): ");
                    String searchOption = scanner.nextLine().toLowerCase();
                    handleSearchOptions(searchOption);
                    break;
                case "3":
                    System.out.print("Enter network option (a/b/c): ");
                    String networkOption = scanner.nextLine().toLowerCase();
                    handleNetworkOptions(networkOption);
                    break;
                case "4":
                    System.out.print("Enter group option (a/b/c/d): ");
                    String groupOption = scanner.nextLine().toLowerCase();
                    handleGroupOptions(groupOption);
                    break;
                case "5":
                    System.out.print("Enter interest option (a): ");
                    String interestOption = scanner.nextLine().toLowerCase();
                    handleInterestOptions(interestOption);
                    break;
                case "6":
                    System.out.print("Enter random option (a/b): ");
                    String randomOption = scanner.nextLine().toLowerCase();
                    handleRandomOptions(randomOption);
                    break;
                case "7":
                    System.out.print("Enter profile option (a/b): ");
                    String profileOption = scanner.nextLine().toLowerCase();
                    handleProfileOptions(profileOption);
                    break;
                case "8":
                    System.out.println("Exiting the platform. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerNewUser() {
        System.out.println("\n=== USER REGISTRATION ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please login instead.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);

        // Project Types
        System.out.println("\nWhat types of projects have you worked on?");
        String[] projectOptions = {"Web Development", "Machine Learning", "App Development", "Data Science", "Cybersecurity", "IoT", "Other"};
        List<String> selectedProjects = getSelectedOptions(projectOptions);
        for (String type : selectedProjects) {
            newUser.projectTypes.add(type);
            if (!interestsTrie.autocomplete(type).contains(type.toLowerCase())) {
                interestsTrie.insert(type.toLowerCase());
            }
        }

        // Certifications
        System.out.println("\nWhich certifications or courses have you completed?");
        String[] certOptions = {"Coursera/edX", "Internships", "Hackathons", "None", "Other"};
        newUser.certifications.addAll(getSelectedOptions(certOptions));

        // Achievements
        System.out.println("\nAchievements/Recognitions earned so far?");
        String[] achievementOptions = {"National level", "State/College", "Club based", "None"};
        newUser.achievements.addAll(getSelectedOptions(achievementOptions));

        // Leadership Roles
        System.out.println("\nHave you taken any leadership roles?");
        String[] leadershipOptions = {"Yes – Clubs", "Yes – Teams", "No"};
        newUser.leadershipRoles.addAll(getSelectedOptions(leadershipOptions));

        // Volunteering Interests
        System.out.println("\nWhat are your volunteering interests?");
        String[] volunteerOptions = {"Social Impact", "Technical Teaching", "Community Work", "Not yet"};
        newUser.volunteeringInterests.addAll(getSelectedOptions(volunteerOptions));

        // Niche Areas
        System.out.println("\nChoose your niche area(s):");
        String[] nicheOptions = {"Creative", "Analytical", "Technical", "Leadership", "Design", "Research"};
        List<String> selectedNiches = getSelectedOptions(nicheOptions);
        for (String niche : selectedNiches) {
            newUser.nicheAreas.add(niche);
            if (!interestsTrie.autocomplete(niche).contains(niche.toLowerCase())) {
                interestsTrie.insert(niche.toLowerCase());
            }
        }

        // Tagline and Join Reason
        System.out.print("\nEnter a short tagline about yourself: ");
        newUser.tagline = scanner.nextLine();

        System.out.print("\nWhy did you join this platform? ");
        newUser.joinReason = scanner.nextLine();

        // Interests (comma-separated since it's free-form)
        System.out.print("\nEnter your interests (comma separated): ");
        String[] interests = scanner.nextLine().split(",");
        for (String interest : interests) {
            newUser.interests.add(interest.trim());
            if (!interestsTrie.autocomplete(interest.trim()).contains(interest.trim().toLowerCase())) {
                interestsTrie.insert(interest.trim().toLowerCase());
            }
        }

        // Experience Level
        System.out.print("\nEnter your experience level (1-10): ");
        newUser.experienceLevel = Integer.parseInt(scanner.nextLine());

        // Calculate collaboration value
        newUser.calculateCollaborationValue();

        // Add user to data structures
        users.put(username, newUser);
        usersByExperience.computeIfAbsent(newUser.experienceLevel, k -> new ArrayList<>()).add(newUser);
        groupConnections.makeSet(username);

        System.out.println("\nRegistration successful! Profile created.");
        System.out.println("Redirecting to login page...");
        loginUser(); // Ensure this method exists
    }
    private static List<String> getSelectedOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.print("Enter your choices (space-separated numbers): ");
        String[] inputs = scanner.nextLine().trim().split(" ");
        List<String> selected = new ArrayList<>();

        for (String input : inputs) {
            try {
                int index = Integer.parseInt(input.trim()) - 1;
                if (index >= 0 && index < options.length) {
                    selected.add(options[index]);
                } else {
                    System.out.println("Invalid option number: " + (index + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + input);
            }
        }

        return selected;
    }
    private static void loginUser() {
        System.out.println("\n=== USER LOGIN ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).password.equals(password)) {
            currentUser = users.get(username);
            System.out.println("Login successful! Welcome, " + username + ".");
            // showMenu(); // or whichever method displays menu/options
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private static void handleSearchOptions(String option) {
        if (currentUser == null) {
            System.out.println("Please login or register first.");
            return;
        }
        
        switch (option) {
            case "a":
                searchByExperience();
                break;
            case "b":
                smartMatchmaking();
                break;
            default:
                System.out.println("Invalid search option.");
        }
    }

   

    private static void searchByExperience() {
        System.out.print("Enter minimum experience level (1-10): ");
        int minExp = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Enter maximum experience level (1-10): ");
        int maxExp = Integer.parseInt(scanner.nextLine());
        
        List<User> matchedUsers = new ArrayList<>();
        for (int i = minExp; i <= maxExp; i++) {
            if (usersByExperience.containsKey(i)) {
                matchedUsers.addAll(usersByExperience.get(i));
            }
        }
        
        matchedUsers = matchedUsers.stream()
                .filter(u -> !u.username.equals(currentUser.username))
                .sorted((u1, u2) -> Integer.compare(u2.experienceLevel, u1.experienceLevel))
                .collect(Collectors.toList());
        
        System.out.println("\nUsers with experience between " + minExp + " and " + maxExp + ":");
        for (User user : matchedUsers) {
            System.out.println("- " + user.username + " (Experience: " + user.experienceLevel + 
                              ", Collab Value: " + user.collaborationValue + ")");
        }
    }

    private static void smartMatchmaking() {
        // Create max heap based on match score
        PriorityQueue<User> heap = new PriorityQueue<>(
            (u1, u2) -> {
                int score1 = calculateMatchScore(currentUser, u1);
                int score2 = calculateMatchScore(currentUser, u2);
                return Integer.compare(score2, score1);
            }
        );
        
        // Add all other users to the heap
        for (User user : users.values()) {
            if (!user.username.equals(currentUser.username) && !currentUser.friends.contains(user)) {
                heap.add(user);
            }
        }
        
        System.out.println("\nTop 10 matchmaking suggestions:");
        for (int i = 0; i < 10 && !heap.isEmpty(); i++) {
            User match = heap.poll();
            int score = calculateMatchScore(currentUser, match);
            System.out.println((i + 1) + ". " + match.username + 
                             " (Match Score: " + score + 
                             ", Experience: " + match.experienceLevel + 
                             ", Collab Value: " + match.collaborationValue + ")");
        }
    }

    private static int calculateMatchScore(User user1, User user2) {
        int score = 0;
        
        // Common interests
        Set<String> commonInterests = new HashSet<>(user1.interests);
        commonInterests.retainAll(user2.interests);
        score += commonInterests.size() * 5;
        
        // Common project types
        Set<String> commonProjects = new HashSet<>(user1.projectTypes);
        commonProjects.retainAll(user2.projectTypes);
        score += commonProjects.size() * 4;
        
        // Common niche areas
        Set<String> commonNiches = new HashSet<>(user1.nicheAreas);
        commonNiches.retainAll(user2.nicheAreas);
        score += commonNiches.size() * 3;
        
        // Experience level proximity
        score += 10 - Math.abs(user1.experienceLevel - user2.experienceLevel);
        
        // Collaboration value
        score += user2.collaborationValue / 10;
        
        return score;
    }

    private static void handleNetworkOptions(String option) {
        if (currentUser == null) {
            System.out.println("Please login or register first.");
            return;
        }
        
        switch (option) {
            case "a":
                addFriend();
                break;
            case "b":
                viewFriends();
                break;
            case "c":
                suggestFriends();
                break;
            default:
                System.out.println("Invalid network option.");
        }
    }

    private static void addFriend() {
        System.out.print("Enter username to add as friend: ");
        String username = scanner.nextLine();
        
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }
        
        User friend = users.get(username);
        if (currentUser.friends.contains(friend)) {
            System.out.println(username + " is already your friend.");
            return;
        }
        
        currentUser.friends.add(friend);
        friend.friends.add(currentUser);
        System.out.println(username + " added to your friends list.");
    }

    private static void viewFriends() {
        if (currentUser.friends.isEmpty()) {
            System.out.println("You have no friends yet.");
            return;
        }
        
        System.out.println("\nYour friends:");
        for (User friend : currentUser.friends) {
            System.out.println("- " + friend.username + " (Experience: " + friend.experienceLevel + 
                              ", Collab Value: " + friend.collaborationValue + ")");
        }
    }

    private static void suggestFriends() {
        // BFS to find friends of friends (mutual connections)
        Set<User> suggestions = new HashSet<>();
        Queue<User> queue = new LinkedList<>();
        Set<User> visited = new HashSet<>();
        
        queue.add(currentUser);
        visited.add(currentUser);
        
        int level = 0;
        while (!queue.isEmpty() && level < 2) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                User user = queue.poll();
                for (User friend : user.friends) {
                    if (!visited.contains(friend)) {
                        visited.add(friend);
                        if (level == 1 && !currentUser.friends.contains(friend)) {
                            suggestions.add(friend);
                        }
                        queue.add(friend);
                    }
                }
            }
            level++;
        }
        
        if (suggestions.isEmpty()) {
            System.out.println("No friend suggestions available.");
            return;
        }
        
        System.out.println("\nFriend suggestions (mutual connections):");
        int i = 1;
        for (User suggestion : suggestions) {
            System.out.println(i++ + ". " + suggestion.username + 
                             " (Experience: " + suggestion.experienceLevel + 
                             ", Collab Value: " + suggestion.collaborationValue + ")");
        }
    }

    
    private static void handleGroupOptions(String option) {
        if (currentUser == null) {
            System.out.println("Please login or register first.");
            return;
        }
        
        switch (option) {
            case "a":
                createGroup();
                break;
            case "b":
                addMemberToGroup();
                break;
            case "c":
                checkSameGroup();
                break;
            case "d":
                mergeGroups();
                break;
            default:
                System.out.println("Invalid group option.");
        }
    }

    private static void createGroup() {
        System.out.print("Enter group name: ");
        String groupName = scanner.nextLine();
        
        Group newGroup = new Group(groupName, currentUser);
        groups.add(newGroup);
        currentUser.groups.add(newGroup);
        
        System.out.println("Group '" + groupName + "' created successfully!");
    }

    private static void addMemberToGroup() {
        if (currentUser.groups.isEmpty()) {
            System.out.println("You are not in any groups. Create a group first.");
            return;
        }
        
        System.out.println("Your groups:");
        for (int i = 0; i < currentUser.groups.size(); i++) {
            System.out.println((i + 1) + ". " + currentUser.groups.get(i).name);
        }
        
        System.out.print("Select a group (number): ");
        int groupChoice = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (groupChoice < 0 || groupChoice >= currentUser.groups.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        
        Group selectedGroup = currentUser.groups.get(groupChoice);
        if (selectedGroup.admin != currentUser) {
            System.out.println("Only the group admin can add members.");
            return;
        }
        
        System.out.print("Enter username to add: ");
        String username = scanner.nextLine();
        
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }
        
        User member = users.get(username);
        if (selectedGroup.members.contains(member)) {
            System.out.println(username + " is already in this group.");
            return;
        }
        
        selectedGroup.members.add(member);
        member.groups.add(selectedGroup);
        groupConnections.union(currentUser.username, username);
        
        System.out.println(username + " added to group '" + selectedGroup.name + "'");
    }

    private static void checkSameGroup() {
        System.out.print("Enter first username: ");
        String user1 = scanner.nextLine();
        System.out.print("Enter second username: ");
        String user2 = scanner.nextLine();
        
        if (!users.containsKey(user1) || !users.containsKey(user2)) {
            System.out.println("One or both users not found.");
            return;
        }
        
        boolean connected = groupConnections.areConnected(user1, user2);
        if (connected) {
            System.out.println(user1 + " and " + user2 + " are in the same group or connected groups.");
        } else {
            System.out.println(user1 + " and " + user2 + " are not in the same group.");
        }
    }

    private static void mergeGroups() {
        if (currentUser.groups.size() < 2) {
            System.out.println("You need to be in at least 2 groups to merge.");
            return;
        }
        
        System.out.println("Your admin groups:");
        List<Group> adminGroups = currentUser.groups.stream()
                .filter(g -> g.admin == currentUser)
                .collect(Collectors.toList());
        
        if (adminGroups.size() < 2) {
            System.out.println("You need to be admin of at least 2 groups to merge.");
            return;
        }
        
        for (int i = 0; i < adminGroups.size(); i++) {
            System.out.println((i + 1) + ". " + adminGroups.get(i).name);
        }
        
        System.out.print("Select first group to merge (number): ");
        int group1Choice = Integer.parseInt(scanner.nextLine()) - 1;
        System.out.print("Select second group to merge (number): ");
        int group2Choice = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (group1Choice < 0 || group1Choice >= adminGroups.size() || 
            group2Choice < 0 || group2Choice >= adminGroups.size() || 
            group1Choice == group2Choice) {
            System.out.println("Invalid choices.");
            return;
        }
        
        Group group1 = adminGroups.get(group1Choice);
        Group group2 = adminGroups.get(group2Choice);
        
        // Merge group2 into group1
        for (User member : group2.members) {
            if (!group1.members.contains(member)) {
                group1.members.add(member);
                member.groups.remove(group2);
                member.groups.add(group1);
                groupConnections.union(group1.admin.username, member.username);
            }
        }
        
        groups.remove(group2);
        System.out.println("Groups merged successfully. All members are now in '" + group1.name + "'");
    }

    private static void handleInterestOptions(String option) {
        switch (option) {
            case "a":
                viewAllInterests();
                break;
            default:
                System.out.println("Invalid interest option.");
        }
    }

    private static void viewAllInterests() {
        List<String> allInterests = interestsTrie.getAllWords();
        System.out.println("\nAll interests in the platform:");
        for (String interest : allInterests) {
            System.out.println("- " + interest);
        }
    }

    private static void suggestInterests() {
        if (currentUser == null) {
            System.out.println("Please login or register first.");
            return;
        }
        
        System.out.print("Start typing an interest: ");
        String prefix = scanner.nextLine().toLowerCase();
        
        List<String> suggestions = interestsTrie.autocomplete(prefix);
        if (suggestions.isEmpty()) {
            System.out.println("No suggestions found.");
            return;
        }
        
        System.out.println("\nSuggested interests:");
        for (int i = 0; i < suggestions.size(); i++) {
            System.out.println((i + 1) + ". " + suggestions.get(i));
        }
    }

    private static void handleRandomOptions(String option) {
        if (currentUser == null) {
            System.out.println("Please login or register first.");
            return;
        }
        
        switch (option) {
            case "a":
                randomMatch();
                break;
            case "b":
                randomBasedOnInterests();
                break;
            default:
                System.out.println("Invalid random option.");
        }
    }

    private static void randomMatch() {
        List<User> allUsers = new ArrayList<>(users.values());
        allUsers.remove(currentUser);
        
        if (allUsers.isEmpty()) {
            System.out.println("No other users available for matching.");
            return;
        }
        
        Random random = new Random();
        User randomUser = allUsers.get(random.nextInt(allUsers.size()));
        
        System.out.println("\nRandom user suggestion:");
        System.out.println("- Username: " + randomUser.username);
        System.out.println("- Experience: " + randomUser.experienceLevel);
        System.out.println("- Collaboration Value: " + randomUser.collaborationValue);
        System.out.println("- Common Interests: " + 
            getCommonInterests(currentUser, randomUser).stream().collect(Collectors.joining(", ")));
    }

    private static void randomBasedOnInterests() {
        if (currentUser.interests.isEmpty()) {
            System.out.println("You have no interests listed. Update your profile first.");
            return;
        }
        
        // Find users with at least one common interest
        List<User> potentialMatches = users.values().stream()
                .filter(u -> !u.username.equals(currentUser.username))
                .filter(u -> !Collections.disjoint(u.interests, currentUser.interests))
                .collect(Collectors.toList());
        
        if (potentialMatches.isEmpty()) {
            System.out.println("No users with matching interests found.");
            return;
        }
        
        Random random = new Random();
        User match = potentialMatches.get(random.nextInt(potentialMatches.size()));
        
        System.out.println("\nRandom user with matching interests:");
        System.out.println("- Username: " + match.username);
        System.out.println("- Experience: " + match.experienceLevel);
        System.out.println("- Collaboration Value: " + match.collaborationValue);
        System.out.println("- Common Interests: " + 
            getCommonInterests(currentUser, match).stream().collect(Collectors.joining(", ")));
    }

    private static Set<String> getCommonInterests(User user1, User user2) {
        Set<String> common = new HashSet<>(user1.interests);
        common.retainAll(user2.interests);
        return common;
    }

    private static void handleProfileOptions(String option) {
        switch (option) {
            case "a":
                if (currentUser == null) {
                    System.out.println("Please login or register first.");
                } else {
                    viewMyProfile();
                }
                break;
            case "b":
                viewOthersProfile();
                break;
            default:
                System.out.println("Invalid profile option.");
        }
    }

    private static void viewMyProfile() {
        System.out.println("\n=== YOUR PROFILE ===");
        System.out.println(currentUser);
    }

    private static void viewOthersProfile() {
        System.out.print("Enter username to view profile: ");
        String username = scanner.nextLine();
        
        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }
        
        User user = users.get(username);
        System.out.println("\n=== PROFILE OF " + username.toUpperCase() + " ===");
        System.out.println(user);
    }
}