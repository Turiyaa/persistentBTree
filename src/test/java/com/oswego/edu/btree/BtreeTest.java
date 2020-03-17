package com.oswego.edu.btree;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BtreeTest {
	BTree tree;
	
	@Test
	public void testInsert() throws IOException {
	tree = new BTree("https://en.wikipedia.org/wiki/Ch%C3%A2teau_de_Lutzelbourginserttest",2);
	List<String> listOfServer = testData();
	for(String tr: listOfServer) {
		tree.insert(tr);
	}
	assertEquals(tree.size, 10);
	}
	
	@Test
	public void testInsertDupeKey() throws IOException {
	List<String> listOfServer= testData();
	listOfServer.add("Tomcat");
	listOfServer.add("RedHat");
	listOfServer.add("Pivtol");
	listOfServer.add("Ngix");
	listOfServer.add("Oracle");
	listOfServer.add("Nodejs");
	tree = new BTree("https://en.wikipedia.org/wiki/Dixon,_Wyomingdupetest",2);
	for(String tr: listOfServer) {
		tree.insert(tr);
	}
	assertEquals(10,tree.size);
	}
	
	@Test
	public void testGetFrequency() throws IOException {
	List<String> listOfServer= testData();
	listOfServer.add("Tomcat");
	listOfServer.add("Tomcat");
	listOfServer.add("RedHat");
	listOfServer.add("Pivtol");
	listOfServer.add("Ngix");
	listOfServer.add("Oracle");
	listOfServer.add("Nodejs");
	BTree tree = new BTree("https://en.wikipedia.org/wiki/William_Chanfrequencytest",2);
	for(String tr: listOfServer) {
		tree.insert(tr.toLowerCase());
	}
	String actual = "tomcat";
	assertEquals(actual,tree.frequency(tree.root, actual).key.trim());
	assertEquals(3,tree.frequency(tree.root, actual).val);
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
