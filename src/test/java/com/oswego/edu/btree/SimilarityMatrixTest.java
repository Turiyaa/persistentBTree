package com.oswego.edu.btree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oswego.edu.similarity.CosineSimilarity;
import com.oswego.edu.similarity.SimilarityDocs;

public class SimilarityMatrixTest {

	@Test
	public void testSimilarityMatrix() throws IOException {
		BTree tree1 = new BTree("https://en.wikipedia.org/wiki/Anglican_prayer_beads", 50);
		BTree tree2 = new BTree("https://en.wikipedia.org/wiki/Rolyat_Hotel", 50);
		BTree tree3 = new BTree("https://en.wikipedia.org/wiki/Bruna_Abdullah", 50);
		BTree tree4 = new BTree("https://en.wikipedia.org/wiki/Arboretum_de_P%C3%A9zanin", 50);
		BTree tree5 = new BTree("https://en.wikipedia.org/wiki/Takeshi_Onaga", 50);
		BTree tree6 = new BTree("https://en.wikipedia.org/wiki/Real_Madrid_CF", 50);
		ArrayList<BTree> treeList = new ArrayList<>();
		treeList.add(tree1);
		treeList.add(tree2);
		treeList.add(tree3);
		treeList.add(tree4);
		treeList.add(tree5);
		treeList.add(tree6);

		List<BTree> treeList2= new ArrayList<BTree>();
		CosineSimilarity sim = new CosineSimilarity();
		treeList2 = sim.similarityMatrix(treeList);
		for (int i = 0; i < treeList2.size(); i++) {
			for (SimilarityDocs docs : treeList.get(i).getSimiDocList()) {
				System.out.print(docs.getSim() + "\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}
}
