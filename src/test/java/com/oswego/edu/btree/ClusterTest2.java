package com.oswego.edu.btree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.junit.Test;

import com.oswego.edu.clustering.Cluster;
import com.oswego.edu.similarity.SimilarityDocs;

public class ClusterTest2 {
	public HashMap<String, double[]> documentMapAll = new HashMap<String, double[]>();
	public HashMap<String, double[]> documentMapCentroid = new HashMap<String, double[]>();
	public HashMap<String, List<String>> centroid = new HashMap<String, List<String>>();
	@Test
	public void testSimilarityMatrix() throws IOException {
		Cluster cluster = new Cluster();

		cluster.processDocuments();
		System.out.println("");
		// sim.similarityMatrix(tree1, tree1.root);
		for (int i = 0; i < cluster.randomCentroid.size(); i++) {
			System.out.println(cluster.randomCentroid.get(i).pageLink);

			for (SimilarityDocs docs : cluster.randomCentroid.get(i).getSimiDocList()) {
				// System.out.print(docs.getSim() + "\t");
			}
			System.out.println();
		}
		for (int j = 0; j < cluster.documentList.size(); j++) {
			for (SimilarityDocs docs : cluster.documentList.get(j).getSimiDocList()) {
				// System.out.print(docs.getSim() + "\t");
			}
			System.out.println("");
		}

		for (int k = 0; k < cluster.documentList.size(); k++) {
			String pageLink = cluster.documentList.get(k).getPageLink();
			double[] simValue = new double[6];
			/*
			 * for(int l = 0; l < cluster.documentList.get(k).getSimiDocList().size(); l++)
			 * { simValue[l] = cluster.documentList.get(k).getSimiDocList().get(l).getSim();
			 * }
			 */
			int i = 0;
			for (SimilarityDocs docs : cluster.documentList.get(k).getSimiDocList()) {
				simValue[i] = docs.getSim();
				i++;
			}
			documentMapAll.put(pageLink, simValue);
		}
		selectRandomCentroid(documentMapAll);
		System.out.println("Distance");
		documentMapAll.forEach((k, v) -> printAll(k, v));
		documentMapCentroid.forEach((k, v) -> printAll(k, v));
		System.out.println("**********************************Distance**************************************");

		findtheClosestArticle();
		System.out.println("**********************************SPLASH**************************************");
		
		findtheClosestArticle1();
		System.out.println("");

		System.out.println("");

	}

	public void printAll(String k, double[] d) {
		System.out.println(k);
		for (int i = 0; i < d.length; i++) {
			System.out.println(d[i]);
		}

	}

	public void printAllTest(double[] d) {
		System.out.println("Achim");
		for (int i = 0; i < d.length; i++) {
			System.out.println(d[i]);
		}

	}

	public void selectRandomCentroid(HashMap<String, double[]> allDocument) {
		List<String> keysAsArray = new ArrayList<String>(allDocument.keySet());
		Random r = new Random();
		for (int i = 0; i < 2; i++) {
			int randomPosition = r.nextInt(keysAsArray.size());
			String key = keysAsArray.get(randomPosition);
			double[] values = allDocument.get(key);
			documentMapCentroid.put(key, values);
			documentMapAll.remove(key);
		}
	}

	public void calculateDistance(double[] centroid, double[] otherDoc) {
		EuclideanDistance ed = new EuclideanDistance();
		double distance = ed.compute(centroid, otherDoc);
		System.out.println(distance);
	}

	public void findtheClosestArticle() {
		for (int i = 0; i < documentMapCentroid.size(); i++) {
			List<String> centroidArray = new ArrayList<String>(documentMapCentroid.keySet());
			String centroidKey = centroidArray.get(i);
			double[] centroidVal = documentMapCentroid.get(centroidKey);

			for (int j = 0; j < documentMapAll.size(); j++) {
				List<String> documentKeyArray = new ArrayList<String>(documentMapAll.keySet());
				String docKey = documentKeyArray.get(j);
				double[] docVal = documentMapAll.get(docKey);
				EuclideanDistance ed = new EuclideanDistance();
				double distance = ed.compute(docVal, centroidVal);
				System.out.println(distance);
			}
		}
	}
	public void findtheClosestArticle1() {
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
				
				System.out.println(distance);
			}
		}
	}
}

