package net.mightyelemental.neuralnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instance implements Serializable {// TODO: training data. Strength of response and compared to the rest of the
												// responses = fitness

	private static final long serialVersionUID = 2796869107367636584L;

	private Node[] nodes;

	private int[] shape = { 8, 26, 20, 2 };

	String word;

	BetterRandom rand = new BetterRandom();

	public int shapeTotal() {
		int total = 0;
		for ( int i : shape ) {
			total += i;
		}
		return total;
	}

	public Instance(String word) {
		this.word = word;
		nodes = new Node[shapeTotal()];
		int totalTemp = shape[0];
		int tempLayer = 0;
		for ( int x = 0; x < nodes.length; x++ ) {
			Node n = new Node(this);
			n.ID = x;
			if ( x < totalTemp ) {
				n.layer = tempLayer;
			} else {
				totalTemp += shape[tempLayer + 1];
				tempLayer++;
				n.layer = tempLayer;
			}
			nodes[x] = n;
		}
		List<List<Node>> layers = new ArrayList<List<Node>>();
		for ( int lay = 0; lay < shape.length; lay++ ) {
			layers.add(Arrays.asList(getNodeLayer(lay)));
		}
		Node[] lay1 = layers.get(1).toArray(new Node[layers.get(1).size()]);
		for ( Node n : layers.get(0) ) {
			n.addConnections(lay1, true);
		}
		for ( int lay = 1; lay < layers.size() - 1; lay++ ) {
			Node[] nods1 = layers.get(lay - 1).toArray(new Node[layers.get(lay - 1).size()]);
			Node[] nods2 = layers.get(lay + 1).toArray(new Node[layers.get(lay + 1).size()]);
			for ( Node n : layers.get(lay) ) {
				n.addConnections(nods2, true);
				n.addConnections(nods1, false);
			}
		}

		Node[] lay = layers.get(layers.size() - 2).toArray(new Node[layers.get(layers.size() - 2).size()]);
		for ( Node n : layers.get(layers.size() - 1) ) {
			n.addConnections(lay, false);
		}
	}

	public Instance(Node[] oldNodes, String word) {
		this.word = word;
		this.nodes = new Node[shapeTotal()];
		for ( int x = 0; x < nodes.length; x++ ) {
			Node n = new Node(this, oldNodes[x].nextLayer, oldNodes[x].previousLayer, oldNodes[x].ID);
			n.setValue(-10000);
			n.layer = oldNodes[x].layer;
			nodes[x] = n;
		}
	}

	public void updateInitialNodes(double[] inputVars) {
		Node[] layer1 = getNodeLayer(0);
		for ( int i = 0; i < inputVars.length; i++ ) {
			layer1[i].setValue(inputVars[i]);
		}
	}

	public Node[] getNodeLayer(int layer) {
		List<Node> r = new ArrayList<Node>();
		for ( Node n : nodes ) {
			if ( n.layer == layer ) {
				r.add(n);
			}
		}
		return r.toArray(new Node[r.size()]);
	}

	public Instance mutate() {
		Node n = rand.randFromArray(nodes);
		if ( n.nextLayer.size() > 0 ) {
			int newN = rand.randKeyFromMap(n.nextLayer);
			n.setWeight(newN, rand.nextBoolInt(n.getWeight(newN), rand.nextDouble() / 5.0));// rand.nextDouble() / 5.0
		} else {
			int newN = rand.randKeyFromMap(n.previousLayer);
			n.setWeight(newN, rand.nextBoolInt(n.getWeight(newN), rand.nextDouble() / 5.0));
		}
		return this;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public Node getNodeByID(int nodeID) {
		return nodes[nodeID];
	}

	public void updateValues(double[] vals) {
		double[] vars = new double[shape[0]];
		for ( int i = 0; i < vars.length && i < vals.length; i++ ) {
			vars[i] = (vals[i] - 96) / 27.0;
		}
		/*
		 * { (vals[0] - 96) / 27.0, (vals[1] - 96) / 27.0, (vals[2] - 96) / 27.0, /
		 * (vals[3] - 96) / 27.0, (vals[4] - 96) / 27.0 };
		 */

		for ( int i = 0; i < vars.length; i++ ) {
			if ( nodes[i].layer != 0 ) {
				System.err.println("asd " + nodes[i].layer);
			}
			if ( vars[i] < 0 || vars[i] > 1 )
				throw new IndexOutOfBoundsException("input var " + i + " is out of bounds (" + vars[i] + ")");
			nodes[i].setValue(vars[i]);
		}
		for ( int lay = 1; lay < shape.length; lay++ ) {
			for ( Node n : getNodeLayer(lay) ) {
				n.updateValue();
			}
		}
	}

	private double[] getChars() {
		double[] d = new double[5];
		char[] chars = word.toCharArray();
		for ( int i = 0; i < d.length; i++ ) {
			if ( i > chars.length - 1 ) {
				d[i] = 96;
				continue;
			}
			d[i] = chars[i];
		}
		return d;
	}

	public double[] result(String word) {
		this.word = word;
		return result();
	}

	public double[] result() {
		updateValues(getChars());
		double[] resultNodes = new double[shape[shape.length - 1]];
		for ( int i = 0; i < resultNodes.length; i++ ) {
			resultNodes[i] = nodes[nodes.length - 1 - i].getValue();
		}
		return resultNodes;
	}

	public double getFitness() {
		double[] val = result();
		// val[0] - english
		// val[1] - japanese
		short choice = (short) (val[0] > val[1] ? 1 : -1);
		WordManager.types t = WordManager.getType(word);
		short sign = (short) (t == WordManager.types.MALE ? 1 : -1);
		return val[0] * 10000 * sign - val[1] * 10000 * sign + (choice == sign ? 4000 : 0);
	}

	public void printLastNodeData() {
		System.out.println(nodes[nodes.length - 2].previousLayer);
	}

	public void printConnections() {
		System.out.println("Non Zero Connections");
		for ( Node n : nodes ) {
			// System.out.println(n);
			if ( n.nextLayer.isEmpty() ) {
				System.out.println(n.ID + "->" + n.previousLayer);
			} else {
				System.out.println(n.ID + "->" + n.nextLayer);
			}
		}
	}

	public void printNodes() {
		System.out.println("Nodes");
		for ( Node n : nodes ) {
			System.out.println(n);
		}
	}

	@Override
	public String toString() {
		return "f:" + getFitness();
	}

	public void printFitnessAllWords() {
		double total = 0;
		int correct = 0;
		for ( String word : WordManager.words.keySet() ) {
			this.word = word;
			double fit = getFitness();
			if ( fit > 4000 ) correct++;
			total += getFitness();
		}
		System.out.println(Main.genNumber + ">> " + correct + " >> " + total);
	}

}
