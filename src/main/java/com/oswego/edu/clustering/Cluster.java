package com.oswego.edu.clustering;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import com.oswego.edu.btree.BTree;
import com.oswego.edu.similarity.CosineSimilarity;
import com.oswego.edu.similarity.SimilarityDocs;

public class Cluster {
	public List<String> wikiLinks;
	public List<BTree> documentList;
	public List<BTree> randomCentroid;
	private static Path path;
	public CosineSimilarity sim;
	public HashMap<String, double[]> documentMapAll = new HashMap<String, double[]>();
	public HashMap<String, double[]> documentMapCentroid = new HashMap<String, double[]>();
	public HashMap<String, ArrayList<String>> centroid = new HashMap<String, ArrayList<String>>();
	List<ClusterKeyVal> centroidCompareList = new ArrayList<ClusterKeyVal>();

	public void processDocuments() throws IOException {
		sim = new CosineSimilarity();
		documentList = new ArrayList<BTree>();
		path = Paths.get("links/articles.txt");
		wikiLinks = Files.readAllLines(path);
		System.out.println("Loading documents");
		for (String page : wikiLinks) {
			documentList.add(new BTree(page, 50));
		}
		sim.similarityMatrix(documentList);

		for (int k = 0; k < documentList.size(); k++) {
			String pageLink = documentList.get(k).getPageLink();
			double[] simValue = new double[documentList.size()];
			int i = 0;
			for (SimilarityDocs docs : documentList.get(k).getSimiDocList()) {
				simValue[i] = docs.getSim();
				i++;
			}
			documentMapAll.put(pageLink, simValue);
		}
		selectRandomCentroid(documentMapAll);
		findtheClosestArticle();
	}

	public void selectRandomCentroid(HashMap<String, double[]> allDocument) {
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			List<String> keysAsArray = new ArrayList<String>(allDocument.keySet());
			int randomPosition = r.nextInt(keysAsArray.size());
			String key = keysAsArray.get(randomPosition);
			double[] values = allDocument.get(key);
			documentMapCentroid.put(key, values);
			//documentMapAll.remove(key);
		}
		List<String> centroidArray = new ArrayList<String>(documentMapCentroid.keySet());
		for(int j = 0; j < documentMapCentroid.size(); j++) {
			centroid.put(centroidArray.get(j), new ArrayList<>());
		}
	}

	public void findtheClosestArticle() {
		for (int i = 0; i < documentMapAll.size(); i++) {
			List<String> documentKeyArray = new ArrayList<String>(documentMapAll.keySet());
			String docKey = documentKeyArray.get(i);
			double[] docVal = documentMapAll.get(docKey);
			for (int j = 0; j < documentMapCentroid.size(); j++) {
				List<String> centroidArray = new ArrayList<String>(documentMapCentroid.keySet());
				String centroidKey = centroidArray.get(j);
				double[] centroidVal = documentMapCentroid.get(centroidKey);
				EuclideanDistance ed = new EuclideanDistance();
				double distance = ed.compute(docVal, centroidVal);

				centroidCompareList.add(new ClusterKeyVal(centroidKey, distance));
			}
			ClusterKeyVal cluster = centroidCompareList.stream().min(Comparator.comparing(ClusterKeyVal::getVal))
					.orElseThrow(NoSuchElementException::new);
			centroidCompareList.clear();
			ArrayList<String> list = centroid.get(cluster.getKey());
			list.add(docKey);
			
			centroid.put(cluster.getKey(), list);
		}
	}

	public class ClusterKeyVal {
		public ClusterKeyVal(String k, double v) {
			key = k;
			val = v;
		}

		public String getKey() {
			return key;
		}

		public double getVal() {
			return val;
		}

		String key;
		double val;

	}
}
