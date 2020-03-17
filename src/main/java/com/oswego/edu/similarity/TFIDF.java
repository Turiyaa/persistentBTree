package com.oswego.edu.similarity;

public class TFIDF {
	
	String word;
	double tf;
	double tf_idf;
	double idf;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getTf() {
		return tf;
	}

	public void setTf(double tf) {
		this.tf = tf;
	}

	public double getTf_idf() {
		return tf_idf;
	}

	public void setTf_idf(double tf_idf) {
		this.tf_idf = tf_idf;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public String toString() {
		String str = "Term: " + getWord() + "\ttf :" + getTf() + "\tidf : "+getIdf() +"\ttf_idf: " + getTf_idf();
		return str;
	}
}
