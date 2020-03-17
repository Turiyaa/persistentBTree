package com.oswego.edu.btree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DataBaseTest {
	
	@Test
	public void testWriteNode() throws IOException {
		System.out.println("tomcat".compareTo("              apache")<0);
		System.out.println("tomcat".compareTo("              apache".trim())<0);
		System.out.println("glassfish".compareTo("              tomcat")<0);
		System.out.println("glassfish".compareTo("              tomcat".trim())<0);
		BTree tree = new BTree("https://en.wikipedia.org/wiki/Koilamasuchus",50);		
		List<String> listOfServer = testData();
		for(String tr: listOfServer) {
			//tree.insert(tr.toLowerCase());
		}
		tree.traverse(tree.root);
		System.out.println(tree.size);
		System.out.println(tree.pageLink.trim());
		
	}
	private List<String> testData(){
		List<String> listOfServer= new ArrayList<String>();
		listOfServer.add("Apache");
		listOfServer.add("Tomcat");
		listOfServer.add("GlassFish");
		listOfServer.add("RedHat");
		listOfServer.add("Pivtol");
		listOfServer.add("Ngix");
		listOfServer.add("Oracle");
		listOfServer.add("Nodejs");
		listOfServer.add("Tornado");
		listOfServer.add("Wiawatha");
		return listOfServer;
	}
}
