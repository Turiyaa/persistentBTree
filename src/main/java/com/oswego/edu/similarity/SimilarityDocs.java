package com.oswego.edu.similarity;

public class SimilarityDocs {
private String link;
private double sim;
public String getLink() {
	return link;
}
public SimilarityDocs(String url, double simlarity) {
	link = url;
	sim = simlarity;
}
public void setLink(String link) {
	this.link = link;
}
public double getSim() {
	return sim;
}
public void setSim(double sim) {
	this.sim = sim;
}
}
