package com.oswego.edu.btree;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.oswego.edu.clustering.Cluster;

public class ClusterTest {
	public HashMap<String, double[]> documentMapAll = new HashMap<String, double[]>();
	public HashMap<String, double[]> documentMapCentroid = new HashMap<String, double[]>();
	@Test
	public void testSimilarityMatrix() throws IOException {
		Cluster cluster = new Cluster();

		cluster.processDocuments();
		cluster.documentMapAll.forEach((k, v) -> printAll(k, v));
		cluster.documentMapCentroid.forEach((k, v) -> printAll(k, v));
		System.out.println(cluster.centroid);

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
}

