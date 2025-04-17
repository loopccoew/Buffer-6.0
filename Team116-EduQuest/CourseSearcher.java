package com.example.demo.service;

import com.example.demo.model.CourseInfo;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

// Trie Data Structure
class TrieNode {
    TrieNode[] children = new TrieNode[26]; // for each letter a-z
    boolean isEndOfWord;
    List<CourseInfo> courseInfos = new ArrayList<>(); // Store courses at this node
}

@Service
public class CourseSearcher {
    private static Trie courseTrie = new Trie();
    private static Map<String, List<String>> prerequisitesMap = new HashMap<>();

    private static List<CourseInfo> courseList = List.of(
		    new CourseInfo("C101", "Java for Beginners", "6 weeks", "1 year", "Free", "Basic", true, 4.5, "Great for newbies!"),
		    new CourseInfo("C102", "Advanced DSA", "8 weeks", "1 year", "Paid (Rs. 999)", "Advanced", true, 4.7, "Challenging but worth it"),
		    new CourseInfo("C103", "Web Dev Bootcamp", "10 weeks", "Lifetime", "Paid (Rs. 1499)", "Intermediate", true, 4.8, "Best full-stack course"),
		    new CourseInfo("C104", "UI/UX Design Fundamentals", "4 weeks", "6 months", "Free", "Basic", true, 4.6, "Good start for designers"),
		    new CourseInfo("C105", "Python for Data Science", "7 weeks", "1 year", "Paid (Rs. 799)", "Intermediate", true, 4.7, "Very informative and practical"),
		    new CourseInfo("C106", "Machine Learning A-Z", "12 weeks", "Lifetime", "Paid (Rs. 1999)", "Advanced", true, 4.9, "Hands-on and in-depth"),
		    new CourseInfo("C107", "DBMS Essentials", "5 weeks", "1 year", "Free", "Intermediate", true, 4.4, "Clear explanations with examples"),
		    new CourseInfo("C108", "Operating Systems", "6 weeks", "6 months", "Free", "Advanced", true, 4.3, "Great for CS students"),
		    new CourseInfo("C109", "Cybersecurity Basics", "4 weeks", "1 year", "Free", "Basic", false, 4.2, "Short and secure!"),
		    new CourseInfo("C110", "React Frontend Mastery", "9 weeks", "Lifetime", "Paid (Rs. 1299)", "Advanced", true, 4.8, "Modern UI development explained well"),
		    new CourseInfo("C111", "Agile & Scrum Training", "3 weeks", "3 months", "Free", "Intermediate", true, 4.5, "Very helpful for team leads"),
		    new CourseInfo("C112", "Cloud Computing with AWS", "6 weeks", "1 year", "Paid (Rs. 1599)", "Advanced", true, 4.6, "Well structured and real-world use cases"),
		    new CourseInfo("C113", "AI for Everyone", "5 weeks", "6 months", "Free", "Basic", true, 4.5, "Perfect for non-tech learners curious about AI"),
		    new CourseInfo("C114", "Kotlin for Android Development", "8 weeks", "1 year", "Paid (Rs. 1099)", "Intermediate", true, 4.6, "Covers all Android fundamentals"),
		    new CourseInfo("C115", "DevOps Masterclass", "10 weeks", "Lifetime", "Paid (Rs. 1799)", "Advanced", true, 4.8, "Great for CI/CD & deployment"),
		    new CourseInfo("C116", "Soft Skills & Communication", "3 weeks", "3 months", "Free", "Basic", false, 4.3, "Helpful for interviews and teamwork"),
		    new CourseInfo("C117", "Blockchain Basics", "6 weeks", "1 year", "Paid (Rs. 1299)", "Intermediate", true, 4.4, "Clear and conceptual course"),
		    new CourseInfo("C118", "Intro to Git & GitHub", "2 weeks", "1 year", "Free", "Basic", true, 4.6, "Essential for developers"),
		    new CourseInfo("C119", "Data Visualization with Tableau", "5 weeks", "6 months", "Paid (Rs. 899)", "Intermediate", true, 4.5, "Clean dashboards and use cases"),
		    new CourseInfo("C120", "Natural Language Processing", "8 weeks", "1 year", "Paid (Rs. 1499)", "Advanced", true, 4.7, "Very hands-on with real NLP tasks"),
		    new CourseInfo("C121", "Intro to Game Development", "6 weeks", "6 months", "Paid (Rs. 999)", "Basic", false, 4.2, "Fun and creative starting point"),
		    new CourseInfo("C122", "Excel & Google Sheets Mastery", "4 weeks", "1 year", "Free", "Basic", true, 4.4, "Very practical and work-friendly"),
		    new CourseInfo("C123", "Core Java Essentials", "5 weeks", "6 months", "Free", "Basic", true, 4.4, "Concise and beginner-friendly"),
		    new CourseInfo("C124", "Learn Java with Projects", "7 weeks", "1 year", "Paid (Rs. 799)", "Basic", true, 4.6, "Good hands-on experience"),
		    new CourseInfo("C125", "Mastering DSA in Java", "10 weeks", "Lifetime", "Paid (Rs. 1199)", "Advanced", true, 4.9, "Best for interviews"),
		    new CourseInfo("C126", "Competitive Programming Bootcamp", "9 weeks", "1 year", "Free", "Advanced", false, 4.3, "Great for contests"),
		    new CourseInfo("C127", "Full-Stack Web Development", "12 weeks", "Lifetime", "Paid (Rs. 1599)", "Intermediate", true, 4.8, "Covers frontend + backend thoroughly"),
		    new CourseInfo("C128", "Responsive Web Design", "6 weeks", "1 year", "Free", "Intermediate", true, 4.5, "Excellent for UI/UX lovers"),
		    new CourseInfo("C129", "AI Foundations", "4 weeks", "6 months", "Free", "Basic", true, 4.4, "Simple explanations and case studies"),
		    new CourseInfo("C130", "Beginner’s Guide to Machine Learning", "6 weeks", "1 year", "Paid (Rs. 999)", "Basic", true, 4.6, "Visual approach to ML"),
		    new CourseInfo("C131", "CI/CD Pipeline with Jenkins", "8 weeks", "1 year", "Paid (Rs. 1399)", "Advanced", true, 4.7, "Practical DevOps guide"),
		    new CourseInfo("C132", "Docker & Kubernetes Bootcamp", "10 weeks", "Lifetime", "Paid (Rs. 1699)", "Advanced", true, 4.9, "Excellent for real-world DevOps"),
		    new CourseInfo("C133", "Git Essentials", "2 weeks", "6 months", "Free", "Basic", false, 4.3, "Short and effective"),
		    new CourseInfo("C134", "Version Control using Git", "3 weeks", "1 year", "Paid (Rs. 499)", "Basic", true, 4.5, "Perfect for beginners"),
		    new CourseInfo("C135", "Applied NLP with Python", "8 weeks", "1 year", "Paid (Rs. 1499)", "Advanced", true, 4.7, "Real-world use cases"),
		    new CourseInfo("C136", "NLP for Beginners", "5 weeks", "6 months", "Free", "Intermediate", true, 4.4, "Clear introduction to NLP techniques"),
		    new CourseInfo("C137", "Business Communication Skills", "4 weeks", "3 months", "Free", "Basic", true, 4.2, "Good for workplace etiquette"),
		    new CourseInfo("C138", "Public Speaking Mastery", "6 weeks", "1 year", "Paid (Rs. 599)", "Intermediate", true, 4.6, "Builds confidence and clarity"),
		    new CourseInfo("C139", "Java OOP Mastery", "6 weeks", "1 year", "Paid (Rs. 899)", "Intermediate", true, 4.6, "Solid OOP concepts in Java"),
		    new CourseInfo("C140", "Java Multithreading & Concurrency", "5 weeks", "6 months", "Free", "Advanced", false, 4.4, "In-depth and hands-on"),
		    new CourseInfo("C141", "DSA Crash Course", "4 weeks", "3 months", "Free", "Intermediate", false, 4.3, "Quick revision for interviews"),
		    new CourseInfo("C142", "DSA with C++", "8 weeks", "1 year", "Paid (Rs. 1199)", "Advanced", true, 4.7, "Perfect for competitive programmers"),
		    new CourseInfo("C143", "React for Beginners", "6 weeks", "1 year", "Free", "Intermediate", true, 4.6, "Simple UI building blocks"),
		    new CourseInfo("C144", "Backend with Node.js", "7 weeks", "Lifetime", "Paid (Rs. 1399)", "Intermediate", true, 4.8, "Practical REST API creation"),
		    new CourseInfo("C145", "Deep Learning with TensorFlow", "9 weeks", "1 year", "Paid (Rs. 1599)", "Advanced", true, 4.9, "In-depth neural networks training"),
		    new CourseInfo("C146", "Intro to Web Scraping with Python", "5 weeks", "6 months", "Free", "Basic", false, 4.4, "Scrape real-time data!"),
		    new CourseInfo("C147", "Python for Automation", "6 weeks", "Lifetime", "Free", "Intermediate", true, 4.5, "Automate your daily tasks"),
		    new CourseInfo("C148", "Data Science with R", "8 weeks", "1 year", "Paid (Rs. 1199)", "Intermediate", true, 4.7, "Great for stats & analysis"),
		    new CourseInfo("C149", "Swift for iOS Development", "7 weeks", "6 months", "Paid (Rs. 1099)", "Intermediate", true, 4.8, "Create amazing iOS apps"),
		    new CourseInfo("C150", "Introduction to Data Science", "10 weeks", "Lifetime", "Free", "Basic", true, 4.6, "Good starter for aspiring data scientists"),
		    new CourseInfo("C151", "C++ for Beginners", "5 weeks", "6 months", "Free", "Basic", true, 4.5, "Master basic C++ concepts"),
		    new CourseInfo("C152", "Advanced SQL Queries", "6 weeks", "1 year", "Paid (Rs. 999)", "Advanced", true, 4.7, "Master complex queries"),
		    new CourseInfo("C153", "Web App Security", "6 weeks", "Lifetime", "Paid (Rs. 1799)", "Advanced", true, 4.8, "Focus on security best practices"),
		    new CourseInfo("C154", "Intro to Swift Programming", "5 weeks", "1 year", "Paid (Rs. 799)", "Intermediate", true, 4.6, "Great for iOS development"),
		    new CourseInfo("C155", "SQL for Data Science", "8 weeks", "1 year", "Free", "Basic", true, 4.5, "Strong foundation in SQL"),
		    new CourseInfo("C156", "Digital Marketing Fundamentals", "5 weeks", "Lifetime", "Paid (Rs. 1299)", "Basic", false, 4.4, "Introduction to digital strategies"),
		    new CourseInfo("C157", "SEO for Beginners", "4 weeks", "1 year", "Free", "Basic", true, 4.5, "Optimize your website for Google"),
		    new CourseInfo("C158", "Introduction to Docker", "5 weeks", "Lifetime", "Free", "Basic", true, 4.6, "Perfect for containerizing apps"),
		    new CourseInfo("C159", "Introduction to Cloud Computing", "7 weeks", "1 year", "Paid (Rs. 999)", "Basic", true, 4.7, "Understand cloud fundamentals"),
		    new CourseInfo("C160", "Networking Basics", "6 weeks", "1 year", "Free", "Basic", true, 4.4, "Learn about network protocols"),
		    new CourseInfo("C161", "JavaScript: The Definitive Guide", "8 weeks", "Lifetime", "Paid (Rs. 1399)", "Intermediate", true, 4.9, "Master the JavaScript language"),
		    new CourseInfo("C162", "React Native Basics", "7 weeks", "6 months", "Free", "Intermediate", true, 4.6, "Mobile app development with React Native"),
		    new CourseInfo("C163", "Advanced CSS Techniques", "6 weeks", "1 year", "Paid (Rs. 999)", "Advanced", true, 4.7, "CSS mastery for modern websites"),
		    new CourseInfo("C164", "Advanced Data Science with Python", "9 weeks", "Lifetime", "Paid (Rs. 1999)", "Advanced", true, 4.8, "Work on complex data problems"),
		    new CourseInfo("C165", "Intro to Mobile Game Development", "8 weeks", "1 year", "Paid (Rs. 1299)", "Intermediate", true, 4.5, "Start building games for mobile"),
		    new CourseInfo("C166", "GraphQL Masterclass", "5 weeks", "Lifetime", "Paid (Rs. 999)", "Advanced", true, 4.6, "Master the modern query language"),
		    new CourseInfo("C167", "Building APIs with Flask", "6 weeks", "Lifetime", "Paid (Rs. 1299)", "Intermediate", true, 4.7, "Flask for building REST APIs"),
		    new CourseInfo("C168", "Python for Cybersecurity", "8 weeks", "1 year", "Paid (Rs. 1599)", "Intermediate", true, 4.9, "Learn cybersecurity with Python"),
		    new CourseInfo("C169", "Android App Development", "10 weeks", "Lifetime", "Paid (Rs. 1399)", "Intermediate", true, 4.8, "Build Android apps from scratch"),
		    new CourseInfo("C170", "Intro to Augmented Reality", "6 weeks", "Lifetime", "Paid (Rs. 1799)", "Intermediate", true, 4.6, "Get hands-on with AR development"),new CourseInfo("C170", "Intro to Augmented Reality", "6 weeks", "Lifetime", "Paid (Rs. 1799)", "Intermediate", true, 4.6, "Get hands-on with AR development"),
		    new CourseInfo("C171", "Introduction to Quantum Computing", "7 weeks", "1 year", "Free", "Advanced", true, 4.8, "Cutting-edge technology for future computing"),
		    new CourseInfo("C172", "Building Chatbots with Python", "8 weeks", "Lifetime", "Paid (Rs. 1499)", "Intermediate", true, 4.7, "Create your own conversational agents"),
		    new CourseInfo("C173", "Intro to Data Structures with Python", "6 weeks", "1 year", "Free", "Basic", true, 4.5, "Understanding basic data structures"),
		    new CourseInfo("C174", "Intro to Deep Learning with Python", "7 weeks", "Lifetime", "Paid (Rs. 1699)", "Intermediate", true, 4.8, "Deep dive into neural networks"),
		    new CourseInfo("C175", "UI/UX Design with Figma", "8 weeks", "1 year", "Paid (Rs. 999)", "Intermediate", true, 4.6, "Create beautiful UI/UX designs"),
		    new CourseInfo("C176", "Responsive Web Design with CSS Grid", "6 weeks", "Lifetime", "Free", "Intermediate", true, 4.7, "Master responsive layouts with Grid"),
		    new CourseInfo("C177", "R for Data Science", "8 weeks", "1 year", "Paid (Rs. 1299)", "Intermediate", true, 4.8, "A hands-on approach to data science with R"),
		    new CourseInfo("C178", "iOS Development with Swift", "10 weeks", "Lifetime", "Paid (Rs. 1999)", "Advanced", true, 4.9, "Build iOS apps from scratch using Swift"),
		    new CourseInfo("C179", "Blockchain for Developers", "7 weeks", "1 year", "Paid (Rs. 1499)", "Intermediate", true, 4.7, "Learn to build blockchain applications"),
		    new CourseInfo("C180", "React Native with Expo", "6 weeks", "Lifetime", "Paid (Rs. 1199)", "Intermediate", true, 4.6, "Cross-platform app development with React Native"),
		    new CourseInfo("C181", "Advanced CSS Layouts", "5 weeks", "6 months", "Paid (Rs. 999)", "Advanced", true, 4.8, "Master layouts using Flexbox and Grid"),
		    new CourseInfo("C182", "Intro to Functional Programming in JavaScript", "6 weeks", "1 year", "Free", "Intermediate", true, 4.5, "Get started with functional programming"),
		    new CourseInfo("C183", "Digital Illustration with Adobe Illustrator", "8 weeks", "Lifetime", "Paid (Rs. 1299)", "Intermediate", true, 4.7, "Learn how to create vector art and illustrations"),
		    new CourseInfo("C184", "Intro to Python for Data Science", "7 weeks", "1 year", "Free", "Basic", true, 4.6, "Start your journey with Python in Data Science"),
		    new CourseInfo("C185", "Automated Testing with Selenium", "6 weeks", "1 year", "Paid (Rs. 1499)", "Intermediate", true, 4.8, "Learn to automate web app testing with Selenium"),
		    new CourseInfo("C186", "Advanced Git Techniques", "5 weeks", "6 months", "Free", "Advanced", true, 4.7, "Master Git for version control"),
		    new CourseInfo("C187", "Intro to Software Engineering", "9 weeks", "1 year", "Paid (Rs. 1499)", "Intermediate", true, 4.6, "Learn software development best practices"),
		    new CourseInfo("C188", "Advanced Python Programming", "8 weeks", "1 year", "Paid (Rs. 1599)", "Advanced", true, 4.8, "Become a Python expert with advanced concepts"),
		    new CourseInfo("C189", "Intro to Ethical Hacking", "7 weeks", "Lifetime", "Paid (Rs. 1799)", "Advanced", true, 4.7, "Learn ethical hacking and cybersecurity practices"),
		    new CourseInfo("C190", "TensorFlow for Deep Learning", "9 weeks", "1 year", "Paid (Rs. 1799)", "Advanced", true, 4.9, "Deep dive into TensorFlow for deep learning models"),
		    new CourseInfo("C191", "Intro to Game Design", "5 weeks", "Lifetime", "Free", "Basic", true, 4.6, "Understand the basics of game mechanics and design"),
		    new CourseInfo("C192", "Web Performance Optimization", "6 weeks", "1 year", "Paid (Rs. 1299)", "Intermediate", true, 4.7, "Speed up your websites for better performance"),
		    new CourseInfo("C193", "Graph Databases with Neo4j", "7 weeks", "Lifetime", "Paid (Rs. 1399)", "Advanced", true, 4.8, "Learn graph databases with Neo4j"),
		    new CourseInfo("C194", "Angular Advanced Techniques", "9 weeks", "Lifetime", "Paid (Rs. 1599)", "Advanced", true, 4.9, "Master Angular with advanced features"),
		    new CourseInfo("C195", "Data Science with SQL", "6 weeks", "1 year", "Paid (Rs. 999)", "Intermediate", true, 4.7, "Combine SQL with data science techniques"),
		    new CourseInfo("C196", "Creating APIs with Django", "8 weeks", "Lifetime", "Paid (Rs. 1499)", "Intermediate", true, 4.8, "Learn API development with Django framework"),
		    new CourseInfo("C197", "Python for Web Development", "7 weeks", "1 year", "Free", "Intermediate", true, 4.6, "Build dynamic web apps using Python"),
		    new CourseInfo("C198", "Cloud Engineering with Google Cloud", "10 weeks", "1 year", "Paid (Rs. 1999)", "Advanced", true, 4.9, "Learn how to build and manage cloud applications"),
		    new CourseInfo("C199", "Machine Learning with Scikit-Learn", "8 weeks", "Lifetime", "Paid (Rs. 1399)", "Intermediate", true, 4.8, "Learn machine learning techniques using Scikit-learn"),
		    new CourseInfo("C200", "Building Scalable Web Applications", "9 weeks", "Lifetime", "Paid (Rs. 1799)", "Advanced", true, 4.9, "Learn to scale and optimize your web apps"),
		    new CourseInfo("C201", "Complete Digital Marketing", "12 weeks", "Lifetime", "Paid (Rs. 1499)", "Basic", false, 4.7, "Covering all aspects of digital marketing strategies"),
		    new CourseInfo("C202", "Advanced SQL for Data Analysis", "7 weeks", "1 year", "Paid (Rs. 1299)", "Advanced", true, 4.8, "Deep dive into SQL for data analysis"),
		    new CourseInfo("C203", "Intro to Cloud-Native Development", "8 weeks", "Lifetime", "Paid (Rs. 1599)", "Intermediate", true, 4.6, "Develop cloud-native apps using modern frameworks"),
		    new CourseInfo("C204", "Intro to Kubernetes", "5 weeks", "Lifetime", "Free", "Intermediate", true, 4.7, "Learn container orchestration with Kubernetes"),
		    new CourseInfo("C205", "AI-Powered Data Analytics", "10 weeks", "Lifetime", "Paid (Rs. 1799)", "Advanced", true, 4.9, "Learn AI and analytics for advanced business insights"),
		    new CourseInfo("C206", "Vue.js Essentials", "6 weeks", "1 year", "Paid (Rs. 1199)", "Intermediate", true, 4.7, "A complete guide to mastering Vue.js framework"),
		    new CourseInfo("C207", "Creating Web Applications with Flask", "7 weeks", "Lifetime", "Paid (Rs. 1499)", "Intermediate", true, 4.8, "Build scalable web apps with Flask"),
		    new CourseInfo("C208", "Advanced JavaScript ES6+", "6 weeks", "1 year", "Paid (Rs. 1299)", "Advanced", true, 4.9, "Master the latest features of JavaScript"),
		    new CourseInfo("C209", "Data Engineering with Python", "8 weeks", "Lifetime", "Paid (Rs. 1599)", "Advanced", true, 4.8, "Learn how to manage and process large data sets"),
		    new CourseInfo("C210", "DevOps Automation with Ansible", "9 weeks", "1 year", "Paid (Rs. 1799)", "Advanced", true, 4.7, "Automate your infrastructure management with Ansible"),
		    new CourseInfo("C211", "Design Thinking for Developers", "6 weeks", "Lifetime", "Free", "Intermediate", true, 4.7, "Learn how to approach problem-solving like a designer"),
		    new CourseInfo("C212", "Deep Learning with Keras", "8 weeks", "1 year", "Paid (Rs. 1699)", "Advanced", true, 4.9, "Create neural networks with Keras and TensorFlow"),
		    new CourseInfo("C213", "Introduction to 3D Modeling", "7 weeks", "Lifetime", "Paid (Rs. 1399)", "Basic", true, 4.5, "Learn the basics of 3D design and modeling"),
		    new CourseInfo("C214", "Augmented Reality with Unity", "10 weeks", "1 year", "Paid (Rs. 1999)", "Intermediate", true, 4.8, "Create AR experiences using Unity"),
		    new CourseInfo("C215", "Docker for Developers", "6 weeks", "Lifetime", "Paid (Rs. 1299)", "Intermediate", true, 4.7, "Learn to containerize your applications with Docker"),
		    new CourseInfo("C216", "Natural Language Processing with Python", "8 weeks", "1 year", "Paid (Rs. 1599)", "Intermediate", true, 4.8, "Work on real-world NLP problems using Python"),
		    new CourseInfo("C217", "Advanced React Concepts", "6 weeks", "Lifetime", "Paid (Rs. 1399)", "Advanced", true, 4.9, "Level up your React skills with advanced concepts"),
		    new CourseInfo("C218", "Python for Data Visualization", "5 weeks", "Lifetime", "Free", "Intermediate", true, 4.6, "Visualize data using libraries like Matplotlib"),
		    new CourseInfo("C219", "Introduction to Financial Modeling", "7 weeks", "1 year", "Paid (Rs. 1499)", "Intermediate", true, 4.7, "Learn financial modeling techniques for business"),
		    new CourseInfo("C220", "Advanced Ethical Hacking", "8 weeks", "Lifetime", "Paid (Rs. 1799)", "Advanced", true, 4.8, "Dive deep into penetration testing and security"),
		    new CourseInfo("C221", "Intro to Mobile Web Development", "6 weeks", "1 year", "Free", "Intermediate", true, 4.6, "Develop mobile-friendly websites with modern techniques"),
		    new CourseInfo("C222", "Building Scalable Databases with MongoDB", "8 weeks", "Lifetime", "Paid (Rs. 1599)", "Intermediate", true, 4.8, "Learn how to design and scale MongoDB databases"),
		    new CourseInfo("C223", "AWS Certified Solutions Architect", "10 weeks", "Lifetime", "Paid (Rs. 1999)", "Advanced", true, 4.9, "Prepare for AWS Solutions Architect certification"),
		    new CourseInfo("C224", "API Development with Node.js", "7 weeks", "Lifetime", "Paid (Rs. 1299)", "Intermediate", true, 4.7, "Learn how to build APIs using Node.js and Express"),
		    new CourseInfo("C225", "Creating Cross-Platform Apps with Flutter", "8 weeks", "Lifetime", "Paid (Rs. 1699)", "Intermediate", true, 4.8, "Develop native cross-platform apps with Flutter"),
		    new CourseInfo("C226", "Mastering Python for Machine Learning", "9 weeks", "Lifetime", "Paid (Rs. 1799)", "Advanced", true, 4.9, "Deep dive into Python for advanced machine learning"),
		    new CourseInfo("C227", "Deep Learning with PyTorch", "8 weeks", "1 year", "Paid (Rs. 1599)", "Advanced", true, 4.8, "Work on real-world deep learning projects using PyTorch"),
		    new CourseInfo("C228", "Understanding Big Data", "6 weeks", "Lifetime", "Paid (Rs. 999)", "Intermediate", true, 4.7, "Learn how to handle and process big data systems"),
		    new CourseInfo("C229", "UX/UI Research and Testing", "6 weeks", "1 year", "Paid (Rs. 1299)", "Intermediate", true, 4.6, "Conduct research and usability testing for UI/UX"),
		    new CourseInfo("C230", "Advanced Node.js", "7 weeks", "Lifetime", "Paid (Rs. 1599)", "Advanced", true, 4.9, "Master Node.js and build scalable applications"),
		    new CourseInfo("C231", "Building Mobile Apps with Kotlin", "9 weeks", "Lifetime", "Paid (Rs. 1799)", "Intermediate", true, 4.8, "Learn Android development with Kotlin"),
		    new CourseInfo("C232", "Intro to WebAssembly", "6 weeks", "1 year", "Paid (Rs. 1199)", "Intermediate", true, 4.7, "Explore high-performance applications with WebAssembly"),
		    new CourseInfo("C233", "Intro to Digital Art Creation", "5 weeks", "Lifetime", "Paid (Rs. 999)", "Basic", true, 4.6, "Start creating digital art and illustrations"),
		    new CourseInfo("C234", "Developing with Angular", "8 weeks", "Lifetime", "Paid (Rs. 1399)", "Intermediate", true, 4.8, "Learn Angular for modern web applications"),
		    new CourseInfo("C235", "Intro to Data Visualization with D3.js", "7 weeks", "Lifetime", "Paid (Rs. 1499)", "Intermediate", true, 4.7, "Master data visualization techniques with D3.js"),
		    new CourseInfo("C236", "Introduction to Scala for Data Science", "6 weeks", "1 year", "Free", "Intermediate", true, 4.6, "Leverage Scala for data science applications"),
		    new CourseInfo("C237", "Getting Started with SwiftUI", "8 weeks", "Lifetime", "Paid (Rs. 1299)", "Intermediate", true, 4.7, "Learn modern iOS development with SwiftUI"),
		    new CourseInfo("C238", "AI in Healthcare", "9 weeks", "1 year", "Paid (Rs. 1599)", "Advanced", true, 4.8, "Explore AI applications in the healthcare industry"),
		    new CourseInfo("C239", "Intro to Arduino Programming", "6 weeks", "Lifetime", "Free", "Basic", true, 4.6, "Learn how to create hardware projects with Arduino"),
		    new CourseInfo("C240", "Automation with Python Scripts", "7 weeks", "1 year", "Paid (Rs. 1299)", "Intermediate", true, 4.8, "Automate tasks and processes with Python scripting"),
		    new CourseInfo("C241", "Security for Web Developers", "6 weeks", "Lifetime", "Paid (Rs. 1499)", "Intermediate", true, 4.7, "Understand security concepts for web applications"),
		    new CourseInfo("C242", "Deep Learning for NLP", "8 weeks", "Lifetime", "Paid (Rs. 1599)", "Advanced", true, 4.9, "Build deep learning models for NLP tasks"),
		    new CourseInfo("C243", "React Redux for State Management", "6 weeks", "1 year", "Paid (Rs. 1299)", "Intermediate", true, 4.8, "Master state management with Redux in React"),
		    new CourseInfo("C244", "Mastering GitHub for Developers", "5 weeks", "Lifetime", "Free", "Basic", true, 4.7, "Learn version control and collaboration with GitHub"),
		    new CourseInfo("C245", "Introduction to Web Design with HTML & CSS", "5 weeks", "Lifetime", "Free", "Basic", true, 4.5, "Learn the fundamentals of web design with HTML & CSS"),
		    new CourseInfo("C246", "Serverless Architecture with AWS Lambda", "8 weeks", "Lifetime", "Paid (Rs. 1499)", "Advanced", true, 4.8, "Build scalable serverless applications with AWS Lambda"),
		    new CourseInfo("C247", "Mastering API Security", "9 weeks", "1 year", "Paid (Rs. 1699)", "Advanced", true, 4.9, "Learn to secure your APIs with modern techniques"),
		    new CourseInfo("C248", "Building Real-time Applications with Node.js", "8 weeks", "Lifetime", "Paid (Rs. 1599)", "Intermediate", true, 4.8, "Learn to build real-time apps using Node.js"),
		    new CourseInfo("C249", "Business Intelligence with Power BI", "7 weeks", "1 year", "Paid (Rs. 1399)", "Intermediate", true, 4.7, "Leverage Power BI for creating business intelligence solutions"),
		    new CourseInfo("C250", "Deep Learning for Computer Vision", "10 weeks", "Lifetime", "Paid (Rs. 1999)", "Advanced", true, 4.9, "Create advanced computer vision models with deep learning")
		);

    static {
        // Insert all courses into the Trie for efficient search
        for (CourseInfo course : courseList) {
            courseTrie.insert(course.getCourseName(), course);
        }
    }

    // Get all courses
    public List<CourseInfo> getAllCourses() {
        return courseList;
    }

    // Search courses by name using Trie
    public List<CourseInfo> searchCourses(String courseName) {
        return courseTrie.search(courseName);
    }

    // Get courses ranked by rating (Ranking)
    public List<CourseInfo> getRankedCourses() {
        return courseList.stream()
                .sorted(Comparator.comparingDouble(CourseInfo::getRating).reversed())
                .collect(Collectors.toList());
    }

    // Get courses sorted by duration (Sorting)
    public List<CourseInfo> getSortedCoursesByDuration() {
        return courseList.stream()
                .sorted(Comparator.comparingInt(course -> Integer.parseInt(course.getDuration().split(" ")[0]))) // Sorting by numeric value of duration
                .collect(Collectors.toList());
    }
    

    // Get courses with prerequisites
    public List<CourseInfo> getCoursesWithPrerequisites() {
        List<CourseInfo> coursesWithPrerequisites = new ArrayList<>();
        for (CourseInfo course : courseList) {
            if (prerequisitesMap.containsKey(course.getCourseId())) {
                coursesWithPrerequisites.add(course);
            }
        }
        return coursesWithPrerequisites;
    }

    // Add prerequisites to a course
    public void addPrerequisite(String courseId, String prerequisiteId) {
        prerequisitesMap.computeIfAbsent(courseId, k -> new ArrayList<>()).add(prerequisiteId);
    }

    // Add a course
    public boolean addCourse(CourseInfo course) {
        return courseList.add(course);
    }

    // Update an existing course
    public boolean updateCourse(String courseId, CourseInfo updatedCourse) {
        for (int i = 0; i < courseList.size(); i++) {
            CourseInfo course = courseList.get(i);
            if (course.getCourseId().equals(courseId)) {
                courseList.set(i, updatedCourse);
                return true;
            }
        }
        return false;
    }

    // Delete a course
    public boolean deleteCourse(String courseId) {
        return courseList.removeIf(course -> course.getCourseId().equals(courseId));
    }

    // Trie class for efficient course search
    static class Trie {
        private TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String courseName, CourseInfo courseInfo) {
            TrieNode node = root;
            for (char c : courseName.toLowerCase().toCharArray()) {
                if (Character.isAlphabetic(c)) {  // Only process alphabetic characters
                    int index = c - 'a'; // Calculate index for alphabetic characters
                    if (node.children[index] == null) {
                        node.children[index] = new TrieNode();
                    }
                    node = node.children[index];
                }
            }
            node.isEndOfWord = true;
            node.courseInfos.add(courseInfo);
        }

        public List<CourseInfo> search(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toLowerCase().toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    return new ArrayList<>(); // Return empty if no match
                }
                node = node.children[c - 'a'];
            }
            return collectCourses(node);
        }

        private List<CourseInfo> collectCourses(TrieNode node) {
            List<CourseInfo> courses = new ArrayList<>();
            if (node.isEndOfWord) {
                courses.addAll(node.courseInfos);
            }
            for (TrieNode child : node.children) {
                if (child != null) {
                    courses.addAll(collectCourses(child));
                }
            }
            return courses;
        }
    }
}
