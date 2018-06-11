package net.mightyelemental.neuralnet;

import java.util.ArrayList;
import java.util.List;

public class Instance {

	private Node[] nodes;

	public Instance() {
		nodes = new Node[5 + 4 + 2];
		for (int x = 0; x < nodes.length; x++) {
			Node n = new Node();
			if (x < 5 + 4 + 2) {
				n.layer = 2;
			} else if (x < 5 + 4) {
				n.layer = 1;
			} else {
				n.layer = 0;
			}
			nodes[x] = n;
		}

	}

	public void updateInitialNodes(double[] inputVars) {
		Node[] layer1 = getNodeLayer(0);
		for (int i = 0; i < inputVars.length; i++) {
			layer1[i].setValue(inputVars[i]);
		}
	}

	public Node[] getNodeLayer(int layer) {
		List<Node> r = new ArrayList<Node>();
		for (Node n : nodes) {
			if (n.layer == layer) {
				r.add(n);
			}
		}
		return r.toArray(new Node[r.size()]);
	}

	public Instance(Node[] nodes) {
		this.nodes = null;
		this.nodes = nodes;
	}

	public void mutate() {

	}

}
