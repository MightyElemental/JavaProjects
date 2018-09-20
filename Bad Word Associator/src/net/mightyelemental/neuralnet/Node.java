package net.mightyelemental.neuralnet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Node implements Serializable {

	private static final long serialVersionUID = 1031535626551050566L;

	public Map<Integer, Double>	nextLayer		= new HashMap<Integer, Double>();
	public Map<Integer, Double>	previousLayer	= new HashMap<Integer, Double>();

	private double	value;
	public int		layer	= -1;

	private Instance i;

	public Node(Instance i) {
		this.i = i;
	}

	public Node(Instance i, Map<Integer, Double> nextLayer, Map<Integer, Double> previousLayer, int id) {
		this(i);
		this.nextLayer = Utils.deepCopy(nextLayer);
		this.previousLayer = Utils.deepCopy(previousLayer);
		this.ID = id;
	}

	public int ID = -1;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void updateValue() {
		double total = 0;
		double totalWeight = 0;
		// System.out.println("previous: "+previousLayer);
		for ( int nodeID : previousLayer.keySet() ) {
			total += i.getNodeByID(nodeID).getValue() * previousLayer.get(nodeID);
			totalWeight += previousLayer.get(nodeID);
		}
		this.value = total / totalWeight;/// (double) previousLayer.keySet().size();
		if ( value < 0 || value > 1 ) { throw new IndexOutOfBoundsException(toString()); }
	}

	public void addConnection(int nodeID, boolean nextl) {
		if ( nextl ) {
			nextLayer.put(nodeID, 0.5);// Math.random() / 1.5
		} else {
			previousLayer.put(nodeID, i.getNodeByID(nodeID).nextLayer.get(this.ID));
		}
	}

	public double getWeight(int nodeID) {
		if ( nextLayer.containsKey(nodeID) ) { return nextLayer.get(nodeID); }
		if ( previousLayer.containsKey(nodeID) ) { return previousLayer.get(nodeID); }
		return -1;
	}

	public boolean setWeight(int nodeID, double weight) {
		if ( weight > 1 ) {
			weight = 1;
		}
		if ( weight < 0 ) {
			weight = 0;
		}
		if ( nextLayer.containsKey(nodeID) ) {
			nextLayer.put(nodeID, weight);
			i.getNodeByID(nodeID).previousLayer.put(this.ID, weight);
		}
		if ( previousLayer.containsKey(nodeID) ) {
			previousLayer.put(nodeID, weight);
			i.getNodeByID(nodeID).nextLayer.put(this.ID, weight);
		}
		return nextLayer.containsKey(nodeID) || previousLayer.containsKey(nodeID);
	}

	public void addConnections(Node[] nextLayer, boolean nextl) {
		for ( Node n : nextLayer ) {
			addConnection(n.ID, nextl);
		}
	}

	public String toString() {
		return ID + ":(L" + layer + ", " + nextLayer.size() + " connections, value " + (int) this.getValue() + ")";
	}

}