import java.util.*;
import java.util.stream.Collectors;

class User {
	String name, mail, contact;
	Set<String> topics;
	Set<String> slots;
	Map<String, Integer> marks;
	List<Session> log;

	User(String name, String mail, String contact, Set<String> topics, Set<String> slots, Map<String, Integer> marks) {
		this.name = name;
		this.mail = mail;
		this.contact = contact;
		this.topics = topics;
		this.slots = slots;
		this.marks = marks;
		this.log = new ArrayList<>();
	}
}

class Session {
	String day, topic;
	boolean present;
	int feedback;

	Session(String day, String topic, boolean present, int feedback) {
		this.day = day;
		this.topic = topic;
		this.present = present;
		this.feedback = feedback;
	}
}

class GraphNode {
	User user;
	List<GraphEdge> edges;

	GraphNode(User user) {
		this.user = user;
		this.edges = new ArrayList<>();
	}
}

class GraphEdge {
	GraphNode target;
	double weight;

	GraphEdge(GraphNode target, double weight) {
		this.target = target;
		this.weight = weight;
	}
}

public class BuddyMatch {
	Map<String, GraphNode> nodeMap = new HashMap<>();
	Map<String, List<String>> bestList = new HashMap<>();
	Map<String, String> waitingList = new HashMap<>();
	Map<String, String> sentList = new HashMap<>();
	Map<String, String> buddies = new HashMap<>();

	void register(User u) {
		nodeMap.put(u.name, new GraphNode(u));
	}

	double slotMatch(Set<String> a, Set<String> b) {
		Set<String> same = new HashSet<>(a);
		same.retainAll(b);
		return same.size() == a.size() && same.size() == b.size() ? 1.0 : !same.isEmpty() ? 0.5 : 0.0;
	}

	double getScore(User a, User b) {
		if (a.topics.isEmpty() || b.topics.isEmpty())
			return 0.0;

		double commonTopics = a.topics.stream().filter(b.topics::contains).count() / (double) a.topics.size();
		double slotComp = slotMatch(a.slots, b.slots);

		double perf = a.topics.stream().filter(b.marks::containsKey)
				.mapToDouble(sub -> 1 - Math.abs(a.marks.get(sub) - b.marks.get(sub)) / 100.0).average().orElse(0.0);

		return (commonTopics * 0.428 + slotComp * 0.286 + perf * 0.286) * 100;
	}

	void buildNetwork() {
		nodeMap.values().forEach(node -> node.edges.clear());
		List<GraphNode> nodes = new ArrayList<>(nodeMap.values());

		for (int i = 0; i < nodes.size(); i++) {
			GraphNode a = nodes.get(i);
			for (int j = 0; j < nodes.size(); j++) {
				if (i != j) {
					GraphNode b = nodes.get(j);
					double score = getScore(a.user, b.user);
					a.edges.add(new GraphEdge(b, score));
				}
			}
		}
	}

	void suggestMatches(String name, int top) {
		if (!nodeMap.containsKey(name)) {
			System.out.println("No such user.");
			return;
		}

		PriorityQueue<GraphEdge> heap = new PriorityQueue<>((a, b) -> Double.compare(b.weight, a.weight));
		GraphNode source = nodeMap.get(name);

		source.edges.stream().filter(e -> !buddies.containsKey(e.target.user.name))
				.filter(e -> !buddies.containsValue(e.target.user.name)).forEach(heap::add);

		List<String> picks = new ArrayList<>();
		while (!heap.isEmpty() && picks.size() < top) {
			picks.add(heap.poll().target.user.name);
		}

		bestList.put(name, picks);
	}

	void printBuddyInfo(String name) {
		if (!buddies.containsKey(name)) {
			System.out.println("No confirmed buddy.");
			return;
		}

		User b = nodeMap.get(buddies.get(name)).user;
		System.out.println("Buddy Info:");
		System.out.println("Name: " + b.name);
		System.out.println("Email: " + b.mail);
		System.out.println("Contact: " + b.contact);
		System.out.println("Slots: " + String.join(", ", b.slots));
		System.out.println("Topics: " + String.join(", ", b.topics));
	}

	public static void main(String[] args) {
		BuddyMatch app = new BuddyMatch();
		Scanner sc = new Scanner(System.in);
		System.out.print("How many users to add? ");
		int count = Integer.parseInt(sc.nextLine());

		for (int i = 0; i < count; i++) {
			System.out.println("\nNew User #" + (i + 1));
			String name;
			while (true) {
				System.out.print("Username: ");
				name = sc.nextLine().trim();
				if (name.isEmpty())
					System.out.println("Required!");
				else if (app.nodeMap.containsKey(name))
					System.out.println("Already exists!");
				else
					break;
			}

			String mail;
			while (true) {
				System.out.print("Email: ");
				mail = sc.nextLine().trim();
				if (mail.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$"))
					break;
				System.out.println("Invalid email.");
			}

			String contact;
			while (true) {
				System.out.print("Contact (10+ digits): ");
				contact = sc.nextLine().trim();
				if (contact.matches("\\d{10,}"))
					break;
				System.out.println("Invalid number.");
			}

			Set<String> topics;
			while (true) {
				System.out.print("Topics (comma-separated): ");
				topics = Arrays.stream(sc.nextLine().split(",\\s*")).map(String::trim).filter(s -> !s.isEmpty())
						.collect(Collectors.toSet());
				if (!topics.isEmpty())
					break;
				System.out.println("At least one topic required.");
			}

			Map<String, Integer> marks = new HashMap<>();
			for (String t : topics) {
				while (true) {
					System.out.print("Marks in " + t + " (0-100): ");
					try {
						int mark = Integer.parseInt(sc.nextLine());
						if (mark >= 0 && mark <= 100) {
							marks.put(t, mark);
							break;
						}
						System.out.println("Out of range.");
					} catch (NumberFormatException e) {
						System.out.println("Invalid.");
					}
				}
			}

			Set<String> slots;
			while (true) {
				System.out.print("Free slots (morning/afternoon/evening/night, comma-separated): ");
				slots = Arrays.stream(sc.nextLine().toLowerCase().split(",\\s*")).map(String::trim)
						.filter(s -> s.matches("morning|afternoon|evening|night")).collect(Collectors.toSet());
				if (!slots.isEmpty())
					break;
				System.out.println("Provide valid slots.");
			}

			app.register(new User(name, mail, contact, topics, slots, marks));
		}

		app.buildNetwork();

		int opt;
		do {
			System.out.println("\nOptions:");
			System.out.println("1. Show Match Suggestions");
			System.out.println("2. Request Match");
			System.out.println("3. Compatibility Scores");
			System.out.println("4. My Buddy Info");
			System.out.println("5. Exit");
			System.out.print("Pick: ");

			try {
				opt = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Enter number between 1-5.");
				opt = 0;
				continue;
			}

			switch (opt) {
			case 1 -> showMatches(app, sc);
			case 2 -> sendReq(app, sc);
			case 3 -> viewScores(app, sc);
			case 4 -> checkBuddy(app, sc);
			case 5 -> System.out.println("Bye!");
			default -> System.out.println("Wrong option.");
			}
		} while (opt != 5);
	}

	private static void showMatches(BuddyMatch app, Scanner sc) {
		System.out.print("Your username: ");
		String uname = sc.nextLine().trim();

		if (!app.nodeMap.containsKey(uname)) {
			System.out.println("Not found.");
			return;
		}

		if (app.buddies.containsKey(uname)) {
			System.out.println("Already matched with " + app.buddies.get(uname));
			return;
		}

		if (app.waitingList.containsKey(uname)) {
			String sender = app.waitingList.get(uname);
			System.out.print("Request from " + sender + ". Accept? (yes/no): ");
			if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
				app.buddies.put(uname, sender);
				app.buddies.put(sender, uname);
				app.waitingList.remove(uname);
				app.sentList.remove(sender);
				System.out.println("Buddy confirmed with " + sender);
			} else {
				app.waitingList.remove(uname);
				app.sentList.remove(sender);
				System.out.println("Request declined.");
			}
			return;
		}

		if (app.sentList.containsKey(uname)) {
			System.out.println("You already sent a request to " + app.sentList.get(uname));
			return;
		}

		app.suggestMatches(uname, 3);
		List<String> best = app.bestList.getOrDefault(uname, Collections.emptyList());

		System.out.println("\nSuggestions:");
		if (best.isEmpty()) {
			System.out.println("No good matches found.");
		} else {
			for (String s : best) {
				GraphNode node = app.nodeMap.get(uname);
				node.edges.stream().filter(e -> e.target.user.name.equals(s)).findFirst()
						.ifPresent(e -> System.out.printf("- %s (%.1f%%)\n", s, e.weight));
			}
		}
	}

	private static void sendReq(BuddyMatch app, Scanner sc) {
		System.out.print("Your username: ");
		String uname = sc.nextLine().trim();

		if (!app.nodeMap.containsKey(uname)) {
			System.out.println("Not found.");
			return;
		}

		if (app.buddies.containsKey(uname)) {
			System.out.println("You already have a buddy.");
			return;
		}

		List<String> list = app.bestList.getOrDefault(uname, Collections.emptyList());
		if (list.isEmpty()) {
			System.out.println("No matches to request. Try option 1 first.");
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			System.out.println((i + 1) + ". " + list.get(i));
		}

		System.out.print("Pick a number to request: ");
		try {
			int pick = Integer.parseInt(sc.nextLine());
			if (pick < 1 || pick > list.size()) {
				System.out.println("Invalid.");
				return;
			}
			String to = list.get(pick - 1);
			if (app.buddies.containsKey(to)) {
				System.out.println("Already matched.");
				return;
			}
			app.waitingList.put(to, uname);
			app.sentList.put(uname, to);
			System.out.println("Request sent to " + to);
		} catch (NumberFormatException e) {
			System.out.println("Invalid number.");
		}
	}

	private static void viewScores(BuddyMatch app, Scanner sc) {
		System.out.print("Your username: ");
		String uname = sc.nextLine().trim();

		if (!app.nodeMap.containsKey(uname)) {
			System.out.println("User not found.");
			return;
		}

		for (GraphEdge e : app.nodeMap.get(uname).edges) {
			System.out.printf("%s: %.2f%%\n", e.target.user.name, e.weight);
		}
	}

	private static void checkBuddy(BuddyMatch app, Scanner sc) {
		System.out.print("Your username: ");
		String uname = sc.nextLine().trim();

		if (!app.nodeMap.containsKey(uname)) {
			System.out.println("Not found.");
			return;
		}

		app.printBuddyInfo(uname);
	}
}
