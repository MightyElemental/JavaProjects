package net.mightyelemental.tree;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Display extends BasicGameState {
	
	
	/** Lines grouped by iteration */
	List<List<Branch>> lines;
	
	private boolean aa = true, nodes = false, leaves = true;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		lines = TreeGenerator.generateTree(1280 / 2, 700);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setAntiAlias(aa);
		g.setColor(Color.white);
		g.drawString("Time:" + (16 - ticks / 100) + "\nBranches: " + TreeGen.mainBranches + "\nIterations: " + TreeGen.iterations, 10, 50);
		g.drawString("LEAVES " + (leaves ? "ON" : "OFF") + "\nNODES " + (nodes ? "ON" : "OFF") + "\nANTI ALIASING " + (aa ? "ON" : "OFF"),
			10, 200);
		growTree(gc, sbg, g);
		g.setColor(Color.red);
		for (int i = 0; i < lines.size(); i++) {
			for (int x = 0; x < lines.get(i).size(); x++) {
				Branch l = lines.get(i).get(x);
				int size = 4 - i;
				if (size < 1) {
					size = 1;
				}
				if (l.drawn() && i == lines.size() - 1 && leaves) {
					g.setColor(Color.green);
					size = 4;
					g.fillOval(l.getEnd().x - size, l.getEnd().y - size, size * 2, size * 2);
				}
				g.setColor(Color.red);
				if (nodes && i != lines.size() - 1) {
					if (l.drawn()) {
						g.fillOval(l.getEnd().x - size, l.getEnd().y - size, size * 2, size * 2);
					}
				}
				g.setColor(Color.orange);
				if (i == 0) {
					g.fillOval(l.getStart().x - size, l.getStart().y - size, size * 2, size * 2);
				}
			}
		}
	}
	
	public void growTree(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (int i = 0; i < lines.size(); i++) {
			g.setLineWidth(lines.size() - i);
			for (int x = 0; x < lines.get(i).size(); x++) {
				Branch l = lines.get(i).get(x);
				if (!l.drawn()) {
					if (l.getParent() == null) {
						g.draw(l.getProgressLine());
					} else if (l.getParent().drawn()) {
						g.draw(l.getProgressLine());
					}
				} else {
					g.draw(l);
				}
			}
		}
	}
	
	int ticks = 0;
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		for (int i = 0; i < lines.size(); i++) {
			for (int x = 0; x < lines.get(i).size(); x++) {
				Branch l = lines.get(i).get(x);
				if (!l.drawn()) {
					if (l.getParent() == null) {
						l.nextStep(delta);
					} else if (l.getParent().drawn()) {
						l.nextStep(delta);
					}
				}
			}
		}
		if (gc.getInput().isKeyPressed(Input.KEY_A)) {
			aa = !aa;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_N)) {
			nodes = !nodes;
		}
		if (gc.getInput().isKeyPressed(Input.KEY_L)) {
			leaves = !leaves;
		}
		ticks += delta;
		if (ticks > 1700) {
			// TreeGen.updateRandom();
			// lines = TreeGenerator.generateTree(1280 / 2, 700);
			ticks = 0;
		}
	}
	
	@Override
	public int getID() {
		return 0;
	}
	
}
