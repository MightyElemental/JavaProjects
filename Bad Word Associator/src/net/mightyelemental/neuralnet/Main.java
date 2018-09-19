package net.mightyelemental.neuralnet;

import java.awt.Dimension;

import javax.swing.JOptionPane;

public class Main implements Runnable {

	public static final int	MUTATION	= 10;
	public static int		genNumber;
	public static double	bestFitnessLastGen;
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
		while (genNumber < 250) {
			Instance[] temp = g.getSeeds();
			bestFitnessLastGen = Integer.MIN_VALUE;
			for ( Instance i : temp ) {
				if ( i.getFitness() > bestFitnessLastGen ) {
					bestFitnessLastGen = i.getFitness();
				}
			}
			g = new Generation(g.getSeeds());
			genNumber++;
			// System.out.println(genNumber);
			System.out.println(genNumber + ">> " + g.instances[0].getFitness());
			// try {
			// if ( genNumber > 300 ) Thread.sleep(50);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		for ( int i = 0; i < 10; i++ ) {
			String word = JOptionPane.showInputDialog("Give me word now");
			double val = g.instances[0].result(word);
			boolean res = val > 0.5;
			int mod = (int) (200 * (res ? val - 0.5 : 0.5 - val));
			JOptionPane.showMessageDialog(null,
					"'" + word + "' is " + (res ? "good" : "not good") + "!\n" + (mod / 10.0) + "% sure");
		}
	}

}
