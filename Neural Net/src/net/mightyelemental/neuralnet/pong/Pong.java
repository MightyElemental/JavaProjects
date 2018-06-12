package net.mightyelemental.neuralnet.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import net.mightyelemental.neuralnet.BetterRandom;
import net.mightyelemental.neuralnet.Instance;
import net.mightyelemental.neuralnet.Main;

public class Pong extends JPanel implements Runnable {

	private static final long serialVersionUID = -8886292068713122951L;

	public boolean endGame;

	private Thread thisThread = new Thread(this);

	private int directionStep = 360 / 8;

	private int			ballDirection	= 0;
	private ballTrend	trend			= ballTrend.right;

	private Ball		ball;
	private Rectangle	player1;
	private Rectangle	player2;

	public boolean slow = false;

	private enum ballTrend {
		left, right;
	}

	private Instance	inst;
	Dimension			dim	= new Dimension(800, 600);

	public Pong(Instance i) {
		inst = i;
		resetField();
		player1 = new Rectangle(20, dim.height / 2, 10, 80);
		player2 = new Rectangle(dim.width - 45, dim.height / 2, 10, 80);
		thisThread.start();
	}

	private void resetField() {
		ball = new Ball(dim.width / 2.0, dim.height / 2.0, 10.0, 10.0);
		ballDirection = (rand.nextBoolean() ? 1 : 0) * 180;
	}

	@Override
	public void run() {
		while (!endGame) {
			logic();
			this.repaint();
			if ( Main.genNumber >= 750) {// subTime % 10 == 0
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.repaint();
		// System.out.println(player1wins + " | " + player2wins + " | " + getFitness());
	}

	private void moveBall() {
		double x = ball.xDouble;
		double y = ball.yDouble;

		x += Math.cos(Math.toRadians(ballDirection)) * 7.825;
		y += Math.sin(Math.toRadians(ballDirection)) * 7.825;

		ball.setLoc(x, y);
	}

	BetterRandom rand = new BetterRandom();

	private int	player1wins;
	private int	player2wins;

	private int totalTimeWins, subTime;

	private void logic() {
		subTime++;
		moveBall();
		if ( inst.moveUp(getNodeVars()) ) {
			player2.y -= 5;
		} else {
			player2.y += 5;
		}
		// if ( player1.getCenterY() < ball.yDouble ) {
		// player1.y += 5;
		// } else if ( player1.getCenterY() > ball.yDouble ) {
		// player1.y -= 5;
		// }
		if ( player1.y < 0 ) player1.y = 0;
		if ( player2.y < 0 ) player2.y = 0;
		if ( player1.y + player1.getHeight() > 535 ) player1.y = (int) (535 - player1.getHeight());
		if ( player2.y + player2.height > 535 ) player2.y = (int) (535 - player2.getHeight());

		if ( ball.intersects(player1) ) {
			ball.setLoc(player1.x + player1.width + 1, ball.yDouble);
			ballDirection += rand.nextInt(3, 5) * directionStep;
			trend = ballTrend.right;
		}
		if ( ball.intersects(player2) ) {
			ball.setLoc(player2.x - 1 - ball.width, ball.yDouble);
			ballDirection += rand.nextInt(3, 5) * directionStep;
			trend = ballTrend.left;
		}
		if ( ball.xDouble > 800 ) {
			// ballDirection += (rand.nextInt(2) + 3) * directionStep;
			// trend = ballTrend.left;
			player1wins++;
			totalTimeWins += subTime / 10.0;
			subTime = 0;
			resetField();
		}
		if ( ball.xDouble < 0 ) {
			// ballDirection += (rand.nextInt(2) + 3) * directionStep;
			// trend = ballTrend.left;
			player2wins++;
			// System.out.println("amazing");
			totalTimeWins += subTime / 10.0;
			subTime = 0;
			resetField();
		}
		if ( ball.intersectsLine(0, 0, 800, 0) ) {
			if ( trend.equals(ballTrend.left) ) {
				ballDirection = 3 * directionStep;
			} else {
				ballDirection = directionStep;
			}
			ball.setLoc(ball.xDouble, 0);
		}
		if ( ball.intersectsLine(0, 535, 800, 535) ) {
			if ( trend.equals(ballTrend.left) ) {
				ballDirection = -3 * directionStep;
			} else {
				ballDirection = -directionStep;
			}
			ball.setLoc(ball.xDouble, 535 - ball.height);
			// System.out.println(ballDirection);
		}
		while (ballDirection < 0)
			ballDirection += 360;
		if ( ballDirection == 90 || ballDirection == 270 ) {
			switch (trend) {
			case left:
				ballDirection = (rand.nextInt(2) + 3) * directionStep;
				break;
			case right:
				ballDirection = (rand.nextInt(2) - 1) * directionStep;
				break;
			default:
				break;
			}
		}
		ballDirection = ballDirection % 360;
		if ( player1wins >= 10 || player2wins >= 10 ) {
			endGame = true;
		}
	}

	@Override
	public void paint(Graphics gOld) {
		Graphics2D g = (Graphics2D) gOld;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		g.setColor(Color.white);
		g.fill(player1);
		g.fill(player2);
		g.fillOval((int) ball.xDouble, (int) ball.yDouble, ball.width, ball.height);
		g.drawLine(0, 535, 800, 535);
		g.drawLine(400, 40, 400, 500);
		g.drawString(player1wins + ":" + player2wins, 390, 10);
		g.drawString("Generation " + Main.genNumber + " | Best Fitness (LG) " + Main.bestFitnessLastGen, 10, 10);
		g.drawString("Val " + inst.getNodes()[inst.shapeTotal() - 1].getValue(), 10, 25);
	}

	public int getFitness() {
		return player2wins * 10 + totalTimeWins;
	}

	public double[] getNodeVars() {
		return new double[] { ball.xDouble, ball.yDouble, ballDirection, player2.y };
	}

}
