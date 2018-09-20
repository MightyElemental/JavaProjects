package net.mightyelemental.neuralnet;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import net.mightyelemental.neuralnet.pong.Pong;

public class Main implements Runnable {

	public static final int	MUTATION	= 10;
	public static int		genNumber;
	public static double	bestFitnessLastGen;
	private JFrame			frame;

	Dimension d = new Dimension(800, 600);

	public static final int FAST_GEN_LIMIT = 2000;

	public Main() {
		frame = new JFrame();
		frame.setTitle("pong");
		frame.setSize(d);
		frame.setLocationRelativeTo(null);
		frame.add(g.instances[0].getPongGame());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
					g.instances[0].printConnections();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new Thread(this).start();
	}

	private static Pong			bestPongLast;
	public static Generation	g	= new Generation();

	public static void main(String[] args) {
		// Instance i = new Instance();
		// i.updateValues(new double[] { 300, 300, 180, 101 });
		// i.printNodes();
		new Main();
	}

	@Override
	public void run() {
		while (true) {
			if ( g.isComplete() ) {
				if ( bestPongLast != null ) frame.remove(bestPongLast);
				Instance[] temp = g.getSeeds();
				bestFitnessLastGen = Integer.MIN_VALUE;
				for ( Instance i : temp ) {
					if ( i.getFitness() > bestFitnessLastGen ) {
						bestFitnessLastGen = i.getFitness();
					}
				}
				bestPongLast = new Pong(g.instances[0]);
				g = new Generation(g.getSeeds());
				frame.add(bestPongLast);
				frame.revalidate();
				genNumber++;
				// System.out.println(genNumber);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isMainGameDone() {
		return g.instances[0].getPongGame().endGame;
	}

}
