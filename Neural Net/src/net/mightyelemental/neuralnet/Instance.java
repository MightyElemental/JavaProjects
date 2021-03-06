package net.mightyelemental.neuralnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mightyelemental.neuralnet.pong.Move;
import net.mightyelemental.neuralnet.pong.Pong;

public class Instance implements Serializable {// TODO: training data. Strength of response and compared to the rest of the
												// reponses = fitness

	private static final long serialVersionUID = 2796869107367636584L;

	private Node[] nodes;

	private Pong pongGame;

	private int[] shape = { 4, 3, 3, 1 };

	BetterRandom rand = new BetterRandom();

	public int shapeTotal() {
		int total = 0;
		for ( int i : shape ) {
			total += i;
		}
		return total;
	}

	public Instance() {
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
		pongGame = new Pong(this);
	}

	public Instance(Node[] oldNodes) {
		this.nodes = new Node[shapeTotal()];
		for ( int x = 0; x < nodes.length; x++ ) {
			Node n = new Node(this, oldNodes[x].nextLayer, oldNodes[x].previousLayer, oldNodes[x].ID);
			n.setValue(-10000);
			n.layer = oldNodes[x].layer;
			nodes[x] = n;
		}
		pongGame = new Pong(this);
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
			n.setWeight(newN, rand.nextBoolInt(n.getWeight(newN), rand.nextDouble() / 5.0));// rand.nextDouble() / 4.5
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

	public Pong getPongGame() {
		return pongGame;
	}

	public void updateValues(double[] vals) {
		double[] vars = { vals[0] / 808.0, vals[1] / 535.0, vals[2] / 360.0, vals[3] / 490.0 };// , vals[4] / 490.0
		for ( int i = 0; i < vars.length; i++ ) {
			if ( nodes[i].layer != 0 ) {
				System.err.println("asd " + nodes[i].layer);
			}
			nodes[i].setValue(vars[i]);
		}
		for ( int lay = 1; lay < shape.length; lay++ ) {
			for ( Node n : getNodeLayer(lay) ) {
				n.updateValue();
			}
		}
	}

	public Move result(double[] vals) {
		updateValues(vals);
		double x = nodes[nodes.length - 1].getValue();
		// System.out.println(pongGame == null);
		// try {
		// if ( pongGame.slow ) System.out.println(x1 + "|" + x2);
		// } catch (NullPointerException e) {
		// }
		// double val = rand.nextDouble() * (x1 + x2);
		// if ( val <= x1 ) { return true; }
		if ( x < 1.0 / 3.0 ) {
			return Move.Up;
		} else if ( x < 2.0 / 3.0 ) {
			return Move.Not;
		} else {
			return Move.Down;
		}
	}

	public double getFitness() {
		return pongGame.getFitness();
	}

	public void printLastNodeData() {
		System.out.println(nodes[nodes.length - 1].previousLayer);
	}

	public void printConnections() {
		System.out.println("Non Zero Connections");
		for ( Node n : nodes ) {
			// System.out.println(n);
			StringBuilder cons = new StringBuilder("{");
			if ( n.nextLayer.isEmpty() ) {
				for ( int x : n.previousLayer.keySet() ) {
					cons.append(x + "=" + ((int) (n.previousLayer.get(x) * 1000)) / 1000.0 + ", ");
				}
				cons.setLength(cons.length() - 2);
				cons.append("}");
				System.out.println(n.ID + "(" + (int) (n.getValue() * 1000) / 1000.0 + ")<-" + cons.toString());
			} else {
				for ( int x : n.nextLayer.keySet() ) {
					cons.append(x + "=" + ((int) (n.nextLayer.get(x) * 1000)) / 1000.0 + ", ");
				}
				cons.setLength(cons.length() - 2);
				cons.append("}");
				System.out.println(n.ID + "(" + (int) (n.getValue() * 1000) / 1000.0 + ")->" + cons.toString());
			}
		}
	}

	public void printNodes() {
		System.out.println("Nodes");
		for ( Node n : nodes ) {
			System.out.println(n);
		}
	}

}
