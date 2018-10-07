package net.mightyelemental.neuralnet;

import java.awt.Dimension;

import javax.swing.JOptionPane;

public class Main implements Runnable {

	public static final int MUTATION = 10;
	public static int genNumber;
	public static double bestFitnessLastGen;
	// private JFrame frame;

	Dimension d = new Dimension(800, 600);

	public Main() {
		/*
		 * frame = new JFrame(); frame.setTitle("pong"); frame.setSize(d);
		 * frame.setLocationRelativeTo(null); frame.add(g.instances[0].getPongGame());
		 * frame.setResizable(false);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.setVisible(true);
		 * frame.addKeyListener(new KeyListener() {
		 * 
		 * @Override public void keyPressed(KeyEvent e) { if ( e.getKeyCode() ==
		 * KeyEvent.VK_SPACE ) { g.instances[0].printConnections(); } }
		 * 
		 * @Override public void keyReleased(KeyEvent arg0) { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * @Override public void keyTyped(KeyEvent arg0) { // TODO Auto-generated method
		 * stub
		 * 
		 * } });
		 */

		new Thread(this).start();
	}

	public static Generation g = new Generation();

	public static void main(String[] args) {
		// Instance i = new Instance();
		// i.updateValues(new double[] { 300, 300, 180, 101 });
		// i.printNodes();
		new Main();
	}

	BetterRandom rand = new BetterRandom();

	@Override
	public void run() {
		while (genNumber < 4440) {
			Instance[] temp = g.getSeeds();
			bestFitnessLastGen = Integer.MIN_VALUE;
			for (Instance i : temp) {
				if (i.getFitness() > bestFitnessLastGen) {
					bestFitnessLastGen = i.getFitness();
				}
			}
			g.instances[0].printFitnessAllWords();
			g = new Generation(g.getSeeds());
			genNumber++;
			// System.out.println(genNumber);
			// try {
			// if ( genNumber > 300 ) Thread.sleep(50);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		g.instances[0].printConnections();
		while (true) {
			String word = JOptionPane.showInputDialog("Give me word now");
			double[] val = g.instances[0].result(word);
			WordManager.types t = WordManager.getType(val[0] > val[1] ? 0 : 1);
			JOptionPane.showMessageDialog(null,
					"'" + word + "' is " + t.toString() + "!\n" + val[0] * 100 + "\n" + val[1] * 100);
		}
	}

}
