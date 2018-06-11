package net.mightyelemental.neuralnet;

import java.util.HashMap;
import java.util.Map;

public class Node {

	private Map<Node, Double> connections = new HashMap<Node, Double>();

	private double value;
	public int layer = 0;

	public Node() {

	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void addConnection(Node n, double weight) {

	}

}
