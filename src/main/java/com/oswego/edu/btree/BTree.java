package com.oswego.edu.btree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.oswego.edu.similarity.SimilarityDocs;
import com.oswego.edu.similarity.TFIDF;

public class BTree {
	public static int t = 50;
	public Node root;
	public int size;
	public boolean isDuplicateKey;
	private Disk disk;

	// For clustering
	public String pageLink;
	private double similarity;
	private List<TFIDF> tfidfList = new ArrayList<TFIDF>();
	private List<BTree> simList= new ArrayList<BTree>();
	private List<SimilarityDocs> simiDocList = new ArrayList<SimilarityDocs>();
	


	public BTree(String filename, int degree) {
		try {
			// Keep copy of page link here, problem accessing link from bytebuffer
			pageLink = filename;
			// name file after https://en.wikipedia.org/wiki/
			filename = filename.substring(30);
			disk = new Disk(filename);
			if (disk.isEmpty()) {
				t = degree;
				disk = new Disk(filename);
				disk.diskWriteMetaData(degree, size);
				root = allocateNode();
			}
			Node x = new Node();
			x.isLeaf = true;
			x.numKey = 0;
			root = x;
			size = disk.diskReadMetaData(t, root);
			root = disk.diskRead(root.offset, t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Node search(Node x, String k) {
		int i = 0;

		while ((i < x.numKey) && k.compareTo(x.keys[i].key.trim()) > 0) {
			i++;
		}
		if (i < x.numKey && k.compareTo(x.keys[i].key.trim()) == 0) {
			System.out.println(x.keys[i]);
			System.out.println(x.keys[i].val);
			return x;
		}

		if (x.isLeaf) {
			return null;
		} else {
			return search(disk.diskRead(x.childern[i], t), k);
		}
	}

	public int getVal(Node x, String k) {
		int i = 0;

		while ((i < x.numKey) && k.compareTo(x.keys[i].key.trim()) > 0) {
			i++;
		}
		if (i < x.numKey && k.compareTo(x.keys[i].key.trim()) == 0) {
			return x.keys[i].val;
		}

		if (x.isLeaf) {
			return 0;
		} else {
			return getVal(disk.diskRead(x.childern[i], t), k);
		}
	}

	public void incrementWordFrequency(Node x, String k) throws IOException {
		int i = 0;

		while ((i < x.numKey) && k.compareTo(x.keys[i].key.trim()) > 0) {
			i++;
		}
		if (i < x.numKey && k.compareTo(x.keys[i].key.trim()) == 0) {
			x.keys[i].val++;
			isDuplicateKey = true;
			disk.diskWrite(x, t);
			return;
		}

		if (x.isLeaf) {
			return;
		} else {
			incrementWordFrequency(disk.diskRead(x.childern[i], t), k);
		}
	}

	public void traverse(Node n) {
		int i;
		for (i = 0; i < n.numKey; i++) {

			if (!n.isLeaf) {
				traverse(disk.diskRead(n.childern[i], t));
			}
			System.out.println(n.keys[i].key + "  " + n.keys[i].val);
		}
		if (!n.isLeaf) {
			traverse(disk.diskRead(n.childern[i], t));
		}
		System.out.println("totalNode: " + size);
		System.out.println("rootOffset: " + root.offset);

	}

	public void insert(String k) throws IOException {
		if (k.equals(null) || k.isEmpty()) {
			return;
		}
		Node r = root;
		incrementWordFrequency(r, k);
		if (!isDuplicateKey) {
			size++;
			disk.writeTreeSize(size);
			if (r.numKey == 2 * t - 1) {
				Node s = allocateNode();
				root = s;
				System.out.println("New Root Node" + s.offset);
				disk.writeRootNode(root);
				s.isLeaf = false;
				s.numKey = 0;
				s.childern[0] = r.offset;
				split(s, 0, r);
				insertNonFull(s, k);
			} else {
				insertNonFull(r, k);
			}
		}
		isDuplicateKey = false;
	}

	public void split(Node x, int i, Node y) {
		Node z = allocateNode();
		z.isLeaf = y.isLeaf;
		z.numKey = t - 1;
		for (int j = 0; j < t - 1; j++) {
			z.keys[j] = y.keys[j + t];
		}
		if (!y.isLeaf) {
			for (int j = 0; j < t; j++) {
				z.childern[j] = y.childern[j + t];
			}
		}
		y.numKey = t - 1;

		for (int j = x.numKey; j >= i; j--) {
			x.childern[j + 1] = x.childern[j];
		}

		x.childern[i + 1] = z.offset;

		for (int j = x.numKey - 1; j >= i; j--) {
			x.keys[j + 1] = x.keys[j];
		}
		x.keys[i] = y.keys[t - 1];
		x.numKey = x.numKey + 1;

		try {
			disk.diskWrite(z, t);
			disk.diskWrite(x, t);
			disk.diskWrite(y, t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void insertNonFull(Node x, String k) {

		int i = x.numKey - 1;
		if (x.isLeaf) {
			while (i >= 0 && k.compareTo(x.keys[i].key.trim()) < 0) {
				x.keys[i + 1] = x.keys[i];
				i = i - 1;
			}
			x.keys[i + 1] = new Key(k, 1);
			x.numKey = x.numKey + 1;
			try {
				disk.diskWrite(x, t);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			while (i >= 0 && k.compareTo(x.keys[i].key.trim()) < 0) {
				i = i - 1;
			}
			i = i + 1;
			/*
			 * if(i == 2 && k.equals("of")) { i--; }
			 */
			Node child = disk.diskRead(x.childern[i], t);
			if (child.numKey == 2 * t - 1) {
				split(x, i, child);
				if (k.compareTo(x.keys[i].key.trim()) > 0) {
					i = i + 1;
				}
			}

			/*
			 * //child problem if(x.childern[i] == 0) { System.out.println(); }
			 */
			insertNonFull(disk.diskRead(x.childern[i], t), k);
		}
	}

	public Key frequency(Node x, String k) throws IOException {
		int i = 0;

		while ((i < x.numKey) && k.compareTo(x.keys[i].key.trim()) > 0) {
			i++;
		}
		if (i < x.numKey && k.compareTo(x.keys[i].key.trim()) == 0) {
			return x.keys[i];
		}

		if (x.isLeaf) {
			return null;
		} else {
			return frequency(disk.diskRead(x.childern[i], t), k);
		}
	}

	public static String fixedLengthString(String string, int length) {
		return String.format("%1$" + length + "s", string);
	}

	public Node allocateNode() {
		Node newNode = new Node();
		try {
			newNode.offset = disk.nextNodeOffset();
			disk.diskWrite(newNode, t);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return newNode;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public List<TFIDF> getTfidfList() {
		return tfidfList;
	}

	public void setTfidfList(List<TFIDF> tfidfList) {
		this.tfidfList = tfidfList;
	}

	public Disk getDisk() {
		return disk;
	}

	public List<BTree> getSimList() {
		return simList;
	}

	public void setSimList(List<BTree> simList) {
		this.simList = simList;
	}

	public List<SimilarityDocs> getSimiDocList() {
		return simiDocList;
	}

	public void setSimiDocList(List<SimilarityDocs> simiDocList) {
		this.simiDocList = simiDocList;
	}

	public static class Key {
		public String key;
		public int val;

		public Key(String k, int v) {
			if (k.length() >= 20) {
				key = k.substring(0, 20);
			} else {
				key = fixedLengthString(k, 20);
			}
			val = v;
		}

		public Key() {

		}

	}

	public static class Node {
		public int numKey;
		public Key[] keys;
		public long offset;
		public boolean isLeaf;
		public long[] childern;

		public Node() {
			numKey = 0;
			childern = new long[2 * t];
			isLeaf = true;
			keys = new Key[2 * t - 1];
			for (int i = 0; i < keys.length; i++) {
				keys[i] = new Key("", 0);
			}
		}
	}

}
