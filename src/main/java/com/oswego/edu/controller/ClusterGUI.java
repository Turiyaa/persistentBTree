package com.oswego.edu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oswego.edu.clustering.Cluster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClusterGUI extends Application {
	public HashMap<String, ArrayList<String>> clusters = new HashMap<String, ArrayList<String>>();
	Cluster cluster;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<Hyperlink> hpLink = new ArrayList<Hyperlink>();
		ScrollPane scrollPane = new ScrollPane();
		VBox root = new VBox();
		scrollPane.setContent(root);
		cluster = new Cluster();
    	System.out.println("Starting program:");
    	long start = System.nanoTime();
		cluster.processDocuments();
    	long end = System.nanoTime();
    	long duration = (end -start);
    	System.out.println(duration);
    	System.out.println("Ending program:");

		clusters = cluster.centroid;
		for (int i = 0; i < clusters.size(); i++) {
			List<String> centroidArray = new ArrayList<String>(clusters.keySet());
			String centroidKey = centroidArray.get(i);
			hpLink.add(new Hyperlink("******************Cluster: "+i+"*******************************"));
			hpLink.add(new Hyperlink(""));

			hpLink.add(new Hyperlink(centroidKey));

			ArrayList<String> list = clusters.get(centroidKey);
			for (String str : list) {
				hpLink.add(new Hyperlink(str));

			}
			hpLink.add(new Hyperlink(""));
			hpLink.add(new Hyperlink(""));
		}
		root.getChildren().addAll(hpLink);
		Scene scene = new Scene(scrollPane, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Clustering");
		primaryStage.show();
	}
}