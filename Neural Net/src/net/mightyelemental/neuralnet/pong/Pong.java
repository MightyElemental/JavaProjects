package net.mightyelemental.neuralnet.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JPanel;

public class Pong extends JPanel implements Runnable {

	private static final long serialVersionUID = -8886292068713122951L;

	private boolean endGame;

	private Thread thisThread = new Thread(this);

	private int directionStep = 360 / 8;

	private int ballDirection = 0;
	private ballTrend trend = ballTrend.right;

	private Rectangle ball;
	private Rectangle player1;
	private Rectangle player2;

	private enum ballTrend {
		left, right;
	}

	public Pong(Dimension dim) {
		resetField();
		thisThread.start();
	}

	private void resetField() {
		Dimension dim = new Dimension(800, 600);
		ball = new Rectangle(dim.width / 2, dim.height / 2, 10, 10);
		player1 = new Rectangle(20, dim.height / 2, 10, 80);
		player2 = new Rectangle(dim.width - 45, dim.height / 2, 10, 80);
	}

	@Override
	public void run() {
		while (!endGame) {
			logic();
			this.repaint();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.repaint();
		System.out.println(player1wins + " | " + player2wins + " | " + getFitness());
	}

	private void moveBall() {
		int x = ball.x;
		int y = ball.y;

		x += Math.cos(Math.toRadians(ballDirection)) * 7;
		y += Math.sin(Math.toRadians(ballDirection)) * 7;

		ball.setLocation(x, y);
	}

	Random rand = new Random();

	private int player1wins;
	private int player2wins;

	private void logic() {
		moveBall();
		if (player1.getCenterY() < ball.y) {
			player1.y += 5;
		} else if (player1.getCenterY() > ball.y) {
			player1.y -= 5;
		}
		if (ball.intersects(player1)) {
			ball.setLocation(player1.x + player1.width + 1, ball.y);
			ballDirection += (rand.nextInt(2) + 3) * directionStep;
			trend = ballTrend.right;
		}
		if (ball.intersects(player2)) {
			ball.setLocation(player2.x - 1 - ball.width, ball.y);
			ballDirection += (rand.nextInt(2) + 3) * directionStep;
			trend = ballTrend.left;
		}
		if (ball.x > 800) {
			// ballDirection += (rand.nextInt(2) + 3) * directionStep;
			// trend = ballTrend.left;
			player1wins++;
			resetField();
		}
		if (ball.x < 0) {
			// ballDirection += (rand.nextInt(2) + 3) * directionStep;
			// trend = ballTrend.left;
			player2wins++;
			resetField();
		}
		if (ball.intersectsLine(0, 0, 800, 0)) {
			if (trend.equals(ballTrend.left)) {
				ballDirection = 3 * directionStep;
			} else {
				ballDirection = directionStep;
			}
			ball.setLocation(ball.x, 0);
		}
		if (ball.intersectsLine(0, 535, 800, 535)) {
			if (trend.equals(ballTrend.left)) {
				ballDirection = -3 * directionStep;
			} else {
				ballDirection = -directionStep;
			}
			ball.setLocation(ball.x, 535 - ball.height);
			System.out.println(ballDirection);
		}
		while (ballDirection < 0)
			ballDirection += 360;
		if (ballDirection == 90 || ballDirection == 270) {
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
		if (player1wins >= 11 || player2wins >= 11) {
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
		g.fillOval(ball.x, ball.y, ball.width, ball.height);
		g.drawLine(0, 535, 800, 535);
		g.drawLine(400, 40, 400, 500);
		g.drawString(player1wins + ":" + player2wins, 390, 10);
	}

	public int getFitness() {
		return player2wins * 10 - player1wins * 2;
	}

	public int[] getNodeVars() {
		return new int[] { ball.x, ball.y, player1.y, player2.y, ballDirection };
	}

}
