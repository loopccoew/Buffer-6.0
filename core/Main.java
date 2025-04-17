package buffer.core;



import buffer.Crawler.GoogleScholarCrawler;





import java.util.*;

import java.io.*;

import buffer.Crawler.Crawler;

import buffer.ds.CitationGraph;
import buffer.Crawler.ArvixCrawler;
//import buffer.Crawler.ArxivCrawler;

import buffer.model.Paper;

import buffer.ds.Trie;  // Import your Trie class

import java.util.stream.Collectors;	//for filters

//import java.util.List;

//import java.util.Scanner;

//import java.io.BufferedReader;

//import java.io.FileInputStream;

//import java.io.FileReader;

//import java.io.IOException;

//import java.io.InputStream;





public class Main {

    public static void main(String[] args) {

        System.out.println("🔍 Welcome to Smart Research Paper Crawler");



        // Initialize Trie and add sample keywords

        Trie trie = new Trie();

        loadKeywordsFromCSV("src/buffer/keywords.csv", trie);



        // Ask user for a prefix

        Scanner sc = new Scanner(System.in);

        System.out.print("💡 Enter topic keyword prefix: ");

        String prefix = sc.nextLine();



        // Show suggestions

        List<String> suggestions = trie.suggest(prefix);

        // adding keyword to the trie if not already present    
        
        boolean keywordAdded = false;

        if (suggestions.isEmpty()) {
            System.out.println("❌ No keyword suggestions found. Using typed keyword.");
            suggestions.add(prefix);  // fallback to original input
            appendKeywordToCSV("src/buffer/keywords.csv", prefix);
            keywordAdded = true;
        } else {
            System.out.println("🔎 Suggestions:");
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + suggestions.get(i));
            }

            System.out.print("👉 Choose a number from the suggestions or 0 to use your original input: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            if (choice > 0 && choice <= suggestions.size()) {
                prefix = suggestions.get(choice - 1);
            } else {
                if (!suggestions.contains(prefix)) {
                    appendKeywordToCSV("src/buffer/keywords.csv", prefix);
                    keywordAdded = true;
                }
            }
        }

        if (keywordAdded) {
            trie.insert(prefix);  // add to Trie as well so it's usable in current session
            System.out.println("📝 New keyword added to CSV and Trie: " + prefix);
        }


        //paper search

        System.out.println("🔍 Searching papers for: " + prefix);

        List<Paper> results = new ArrayList<>();

        buffer.Crawler.Crawler arxivCrawler = new ArvixCrawler();

        buffer.Crawler.Crawler googleScholarCrawler = new GoogleScholarCrawler();





        results.addAll(arxivCrawler.search(prefix));

        results.addAll(googleScholarCrawler.search(prefix));  // Use Google Scholar results







        if (results.isEmpty()) {

            System.out.println("❌ No results found.");

            return;

        }



        for (Paper paper : results) {

            System.out.println("\n📄 Title: " + paper.getTitle());

            System.out.println("✍ Authors: " + String.join(", ", paper.getAuthors()));

            System.out.println("📅 Published: " + paper.getPublicationDate());

            System.out.println("🔗 Link: " + paper.getLink());

            System.out.println("📄 Abstract: " + paper.getAbstractText());

        }

        

        ///CitationGragh

        CitationGraph graph = new CitationGraph();

        for (Paper paper : results) {

            graph.addPaper(paper.getTitle());

        }



        // Simulate citations between top 3 results

        if (results.size() >= 3) {

            graph.addCitation(results.get(0).getTitle(), results.get(1).getTitle());

            graph.addCitation(results.get(1).getTitle(), results.get(2).getTitle());

        }



        System.out.println("\n📊 Citation Graph:");

        graph.printGraph();



        // Suggest relevant papers for the first paper

        if (!results.isEmpty()) {

            String baseTitle = results.get(0).getTitle();

            System.out.println("\n🤖 Related papers to: " + baseTitle);

            List<String> related = graph.suggestRelevant(baseTitle);

            if (related.isEmpty()) {

                System.out.println("⚠ No related papers found.");

            } else {

                for (String t : related) {

                    System.out.println("➡ " + t);

                }

            }

        }



        // Show isolated nodes (papers not citing or cited)

        List<String> isolated = graph.detectIsolatedPapers();

        if (!isolated.isEmpty()) {

            System.out.println("\n🧩 Isolated papers (not citing or cited):");

            for (String t : isolated) {

                System.out.println("• " + t);

            }

        } else {

            System.out.println("\n✅ All papers are connected in the citation network.");

        }

        

        // Show filters

        applyFiltersInteractive(sc, results);

    }



    public static void applyFiltersInteractive(Scanner sc, List<Paper> papers) {

        List<Paper> filtered = new ArrayList<>(papers);

        while (true) {

            System.out.println("\n🧰 Apply a filter:");

            System.out.println("[1] Filter by Author");

            System.out.println("[2] Filter by Year");

            System.out.println("[0] Show current results & exit");

            System.out.print("➡ Your choice: ");

            int opt = sc.nextInt();

            sc.nextLine(); // consume newline



            switch (opt) {

                case 1:

                    System.out.print("👤 Enter author name to filter by: ");

                    String author = sc.nextLine().toLowerCase();

                    filtered = filtered.stream()

                            .filter(p -> p.getAuthors().stream().anyMatch(a -> a.toLowerCase().contains(author)))

                            .collect(Collectors.toList());

                    System.out.println("✅ Filtered by author: " + author);

                    break;



                case 2:

                    System.out.print("📅 Enter minimum publication year (e.g., 2020): ");

                    int year = sc.nextInt();

                    sc.nextLine(); // consume newline

                    filtered = filtered.stream()

                    	    .filter(p -> {

                    	        String date = p.getPublicationDate();

                    	        if (date != null) {

                    	            // Use regex to extract the first 4-digit year

                    	            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\b(\\d{4})\\b").matcher(date);

                    	            if (matcher.find()) {

                    	                int pubYear = Integer.parseInt(matcher.group(1));

                    	                return pubYear >= year;

                    	            }

                    	        }

                    	        return false;

                    	    })

                    	    .collect(Collectors.toList());





                        System.out.println("✅ Filtered by year >= " + year);

                        break;



                case 0:

                    System.out.println("\n📄 Final filtered results:");

                    printPaperList(filtered);

                    return;



                default:

                    System.out.println("⚠ Invalid choice.");

            }

        }



    }



    private static void printPaperList(List<Paper> results) {

    	for (Paper paper : results) {

            System.out.println("\n📄 Title: " + paper.getTitle());

            System.out.println("✍ Authors: " + String.join(", ", paper.getAuthors()));

            System.out.println("📅 Published: " + paper.getPublicationDate());

            System.out.println("🔗 Link: " + paper.getLink());

            System.out.println("📄 Abstract: " + paper.getAbstractText());

        }

	}



	//CSV loader for keywords

	public static void loadKeywordsFromCSV(String filePath, Trie trie) { 

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { 

			String line; 

			while ((line = br.readLine()) != null) { 

				line = line.trim(); 

				if (!line.isEmpty()) { 

					trie.insert(line); } 

				} 

			} catch (IOException e) { 

				System.out.println("⚠ Error reading keywords: " + e.getMessage()); } 
		}

	private static void appendKeywordToCSV(String filePath, String keyword) {
	    try (FileWriter fw = new FileWriter(filePath, true);
	         BufferedWriter bw = new BufferedWriter(fw);
	         PrintWriter out = new PrintWriter(bw)) {
	        out.println(keyword);
	    } catch (IOException e) {
	        System.out.println("⚠ Error writing keyword to CSV: " + e.getMessage());
	    }
	}




}