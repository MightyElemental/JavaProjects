package net.mightyelemental.mowergame.grass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;

public class GrassController {
	
	
	public List<Grass> grassList = new ArrayList<Grass>();
	
	private Random rand;
	
	public GrassController( Random rand ) {
		this.rand = rand;
	}
	
	public void generateGrass(GameContainer gc, int size) {
		grassList.clear();
		for (int w = 0; w < gc.getWidth(); w += size) {
			for (int h = 0; h < gc.getHeight(); h += size) {
				Grass g = new Grass(w, h, size, size, generateColor());
				grassList.add(g);
			}
		}
	}
	
	protected int generateColor() {
		if (rand.nextInt(10) == 0) {
			return rand.nextInt(200) + 56;
		} else {
			return rand.nextInt(150) + 106;
		}
	}
	
}
