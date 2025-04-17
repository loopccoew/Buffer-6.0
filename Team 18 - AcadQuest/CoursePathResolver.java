import java.util.*;

public class CoursePathResolver {

    // Get course map based on recommended career
    public static Map<String, List<String>> getCourseMapForCareer(String career) {
        Map<String, List<String>> courseMap = new HashMap<>();

        switch (career) {
            case "Data Scientist":
                courseMap.put("Math Basics", List.of());
                courseMap.put("Python", List.of());
                courseMap.put("Statistics", List.of("Math Basics"));
                courseMap.put("Linear Algebra", List.of("Math Basics"));
                courseMap.put("Probability", List.of("Statistics"));
                courseMap.put("Pandas & NumPy", List.of("Python"));
                courseMap.put("Data Visualization", List.of("Pandas & NumPy"));
                courseMap.put("Exploratory Data Analysis", List.of("Pandas & NumPy", "Statistics"));
                courseMap.put("Machine Learning", List.of("Linear Algebra", "Probability", "Exploratory Data Analysis"));
                courseMap.put("Model Evaluation", List.of("Machine Learning"));
                courseMap.put("Deep Learning", List.of("Machine Learning"));
                courseMap.put("NLP", List.of("Machine Learning"));
                courseMap.put("Project Deployment", List.of("Model Evaluation", "Deep Learning"));
                break;

            case "Business Analyst":
                courseMap.put("Excel", List.of());
                courseMap.put("Statistics", List.of());
                courseMap.put("SQL", List.of("Excel"));
                courseMap.put("Tableau", List.of("Excel"));
                courseMap.put("Business Communication", List.of());
                courseMap.put("Power BI", List.of("SQL"));
                courseMap.put("Data Cleaning", List.of("SQL", "Statistics"));
                courseMap.put("Business Case Analysis", List.of("Power BI", "Statistics", "Business Communication"));
                courseMap.put("KPI Dashboards", List.of("Power BI", "Tableau"));
                courseMap.put("Decision Making", List.of("Business Case Analysis", "KPI Dashboards"));
                break;

            case "Marketing Manager":
                courseMap.put("Marketing Basics", List.of());
                courseMap.put("Market Research", List.of("Marketing Basics"));
                courseMap.put("Content Strategy", List.of("Market Research"));
                courseMap.put("SEO", List.of("Content Strategy"));
                courseMap.put("Social Media Strategy", List.of("Marketing Basics"));
                courseMap.put("Google Ads", List.of("SEO"));
                courseMap.put("Email Marketing", List.of("Content Strategy"));
                courseMap.put("Campaign Analytics", List.of("Google Ads", "Email Marketing"));
                courseMap.put("Conversion Optimization", List.of("Campaign Analytics"));
                break;

            case "HR Manager":
                courseMap.put("HR Fundamentals", List.of());
                courseMap.put("Employee Engagement", List.of("HR Fundamentals"));
                courseMap.put("Labor Law", List.of("HR Fundamentals"));
                courseMap.put("Recruitment Strategy", List.of("HR Fundamentals"));
                courseMap.put("Interviewing Skills", List.of("Recruitment Strategy"));
                courseMap.put("Onboarding Process", List.of("Recruitment Strategy"));
                courseMap.put("Payroll System", List.of("Labor Law"));
                courseMap.put("HR Tech Tools", List.of("Payroll System", "Onboarding Process"));
                break;

            case "Financial Analyst":
                courseMap.put("Accounting Basics", List.of());
                courseMap.put("Excel", List.of("Accounting Basics"));
                courseMap.put("Statistics", List.of());
                courseMap.put("Financial Modeling", List.of("Excel", "Statistics"));
                courseMap.put("Investment Analysis", List.of("Financial Modeling"));
                courseMap.put("Valuation Techniques", List.of("Financial Modeling"));
                courseMap.put("Risk Management", List.of("Investment Analysis"));
                courseMap.put("Portfolio Management", List.of("Investment Analysis", "Valuation Techniques"));
                break;

            default:
                System.out.println("Unknown career: " + career);
        }

        return courseMap;
    }


    // Topological sort without known skills filtering
    public static List<String> getCustomCoursePath(Map<String, List<String>> courses) {
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> graph = new HashMap<>();

        for (String course : courses.keySet()) {
            graph.putIfAbsent(course, new ArrayList<>());
            inDegree.putIfAbsent(course, 0);
            for (String prereq : courses.get(course)) {
                graph.putIfAbsent(prereq, new ArrayList<>());
                graph.get(prereq).add(course);
                inDegree.put(course, inDegree.getOrDefault(course, 0) + 1);
            }
        }

        // Topological Sort (Kahn’s Algorithm)
        Queue<String> queue = new LinkedList<>();
        for (String course : inDegree.keySet()) {
            if (inDegree.get(course) == 0) {
                queue.offer(course);
            }
        }

        List<String> courseOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            courseOrder.add(current);

            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return courseOrder;
    }

    public static void printCoursePathWithDependencies(Map<String, List<String>> courses) {
        // Build the graph and in-degree map
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> graph = new HashMap<>();

        for (String course : courses.keySet()) {
            graph.putIfAbsent(course, new ArrayList<>());
            inDegree.putIfAbsent(course, 0);
            for (String prereq : courses.get(course)) {
                graph.putIfAbsent(prereq, new ArrayList<>());
                graph.get(prereq).add(course);
                inDegree.put(course, inDegree.getOrDefault(course, 0) + 1);
            }
        }

        // 📊 Print the dependency chain
        System.out.println();
        System.out.println();
        System.out.println("📊 Course Dependency Chain:");
        for (String course : courses.keySet()) {
            List<String> prereqs = courses.get(course);
            if (prereqs.isEmpty()) {
                System.out.println("  🟢 " + course + " → No prerequisites");
            } else {
                System.out.println("  🟢 " + course + " → " + String.join(", ", prereqs));
            }
        }

        // ✅ Topological Sort
        Queue<String> queue = new LinkedList<>();
        for (String course : inDegree.keySet()) {
            if (inDegree.get(course) == 0) {
                queue.offer(course);
            }
        }

        List<String> courseOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            courseOrder.add(current);

            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }
        System.out.println();
        System.out.println();

        // 🧾 Print course path
        System.out.println("📚Resolved Course Path:");
        for (int i = 0; i < courseOrder.size(); i++) {
            System.out.println("  🟢 " + courseOrder.get(i));
        }
    }


}
