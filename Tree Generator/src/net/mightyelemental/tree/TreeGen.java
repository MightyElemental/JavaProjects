package net.mightyelemental.tree;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TreeGen extends StateBasedGame {
	
	
	public TreeGen( String name ) {
		super(name);
		addState(new Display());
	}
	
	public static int mainBranches, iterations;
	
	public static Random rand = new Random(System.nanoTime());
	
	// agrs = # branches | # iterations | # branches per iteration
	public static void main(String[] args) {
		try {
			if (args[0].equals("rand")) {
				updateRandom();
			} else {
				mainBranches = Integer.parseInt(args[0]);
				iterations = Integer.parseInt(args[1]);
			}
		} catch (Exception e) {
			updateRandom();
		}
		setupClient();
	}
	
	public static void updateRandom() {
		mainBranches = rand.nextInt(5) + 4;
		iterations = rand.nextInt(1) + 5;
	}
	
	private static void setupClient() {
		
		AppGameContainer appGc;
		try {
			appGc = new AppGameContainer(new TreeGen("Tree Gen"));
			appGc.setDisplayMode(1280, (int) (1280 / 16.0 * 9.0), false);
			appGc.setTargetFrameRate(60);
			appGc.setShowFPS(false);
			appGc.setAlwaysRender(true);
			appGc.setUpdateOnlyWhenVisible(false);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.enterState(0);
	}
}
