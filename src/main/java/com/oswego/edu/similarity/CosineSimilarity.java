package com.oswego.edu.similarity;

import java.util.List;

import com.oswego.edu.btree.BTree;
import com.oswego.edu.btree.BTree.Node;

public class CosineSimilarity {
	private String wiSuggestLink;
	public static final int degree = 50;
	public List<BTree> treeList;
	public List<BTree> treeWithSim;

	public List<BTree> similarityMatrix(List<BTree> documentList){
		treeList = documentList;
		for(int i = 0; i < treeList.size(); i++) {
			System.out.println("Traverse to calculate similarity matrix");
			
			traverForSimilarity(treeList.get(i),treeList.get(i).root);
			
			dotProduct(treeList.get(i), treeList);
			
		}
		return treeList;
	}
	private int numOfTermDocs(List<BTree> treeList, String k) {
		int numOfTermsDocs = 0;
		for (BTree tree : treeList) {
			if (tree.getVal(tree.root, k) > 0)
				numOfTermsDocs++;
		}
		return numOfTermsDocs;
	}
	
	public void traverForSimilarity(BTree tree, Node n) {
		int i;
		for (i = 0; i < n.numKey; i++) {

			if (!n.isLeaf) {
				traverForSimilarity(tree, tree.getDisk().diskRead(n.childern[i], degree));
			}
			calculateTFIDF(tree, n.keys[i].key.trim(), n.keys[i].val);
		}
		if (!n.isLeaf) {
			traverForSimilarity(tree, tree.getDisk().diskRead(n.childern[i], degree));
		}

	}
	
	public void calculateTFIDF(BTree currentTree, String key, int val) {
		String term;
		int numOfTermDocs = 0;
		double tf;
		double idf;
		double tf_idf;
		for(BTree tree : treeList) {
			TFIDF tf_idfObj = new TFIDF();			
			int wordFreq = tree.getVal(tree.root, key);
			if (wordFreq > 0) {
				numOfTermDocs = numOfTermDocs(treeList, key);
				// Save size somewhere in the persistent file
				tf = (double) wordFreq / tree.size;
				idf = Math.log10((double) treeList.size() / numOfTermDocs);
				tf_idf = tf * idf;
				tf_idfObj.setWord(key);
				tf_idfObj.setTf(tf);
				tf_idfObj.setIdf(idf);
				tf_idfObj.setTf_idf(tf_idf);
				tree.getTfidfList().add(tf_idfObj);
			} else {
				tf_idfObj.setWord(key);
				tf_idfObj.setTf(0);
				tf_idfObj.setIdf(0);
				tf_idfObj.setTf_idf(0);
				tree.getTfidfList().add(tf_idfObj);
			}
		}
	}
	
	private void dotProduct(BTree currentTree, List<BTree> treeList) {
		for (BTree iterateTree : treeList) {
			double product = 0;
			double similarity = 0;
			double userWikiMag = 0;
			double wikiMag = 0;
			for (int i = 0; i < currentTree.getTfidfList().size(); i++) {
				double userWikitf_idf = currentTree.getTfidfList().get(i).getTf_idf();
				double wikitf_idf = iterateTree.getTfidfList().get(i).getTf_idf();

				product += (userWikitf_idf * wikitf_idf);
				userWikiMag += Math.pow(userWikitf_idf, 2);
				wikiMag += Math.pow(wikitf_idf, 2);
			}
			similarity = product / (Math.sqrt(userWikiMag) * Math.sqrt(wikiMag));
			iterateTree.setSimilarity(similarity);
			currentTree.getSimiDocList().add(new SimilarityDocs(iterateTree.pageLink, iterateTree.getSimilarity()));
		}
	}
	public String getWiSuggestLink() {
		return wiSuggestLink;
	}
}
