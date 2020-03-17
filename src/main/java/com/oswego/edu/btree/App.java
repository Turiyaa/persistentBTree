package com.oswego.edu.btree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Hello world!
 *
 */
public class App {
	public static int counter = 0;

	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();
		processArticles();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Duration "+duration);
		System.out.println(counter);
	}
	private static Path path;
	private static List<String> wikiPageList;

	public static void processArticles() throws IOException {
		path = Paths.get("links/articles2.txt");
		wikiPageList = Files.readAllLines(path);

		for (String page : wikiPageList) {
			System.out.println("Starting to put nodes in file:"+page);
			long start = System.nanoTime();
			Document wikiPage = Jsoup.parseBodyFragment(Jsoup.connect(page).get().toString());
			String allText = wikiPage.text().toLowerCase();
			allText = allText.replaceAll("\\p{Punct}", "");
			String[] wordList = allText.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
			BTree hashWikiPage = new BTree(page, 50);
			System.out.println("Writing "+hashWikiPage.pageLink);
			for (String word : wordList) {
				counter++;
				if (!word.equals("")) {
					hashWikiPage.insert(word);
				}
			}
			System.out.println("Done writing "+ hashWikiPage.pageLink);
			long end = System.nanoTime();
			long duration = (end - start);
			System.out.println(duration);
		}
	}
}
