package net.mightyelemental.mowergame.grass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

public class GrassController {
	
	
	public List<Grass> grassList = new ArrayList<Grass>();
	
	private Random rand;
	
	public GrassController( Random rand ) {
		this.rand = rand;
	}
	
	public void generateGrass(GameContainer gc, int size) throws SlickException {
		for (int w = 2 * size; w < gc.getWidth() - 4 * size; w += size) {
			for (int h = 2 * size; h < gc.getHeight() - 4 * size; h += size) {
				Grass g = new Grass(w, h, size, size);
				grassList.add(g);
			}
		}
	}
	
	public void setMowed(int x, int y) throws SlickException {
		for (int i = 0; i < grassList.size(); i++) {
			if (grassList.get(i).contains(x, y)) {
				grassList.get(i).mowGrass();
			}
		}
	}
	
	public boolean setMowed(Shape rect) throws SlickException {
		boolean flag = false;
		for (int i = 0; i < grassList.size(); i++) {
			if (grassList.get(i).intersects(rect)) {
				boolean temp = grassList.get(i).mowGrass();
				if (temp) {
					flag = true;
				}
			}
		}
		return flag;
	}
	
	public float getPercentageMowed() {
		float total = grassList.size();
		float mowed = 0;
		for (int i = 0; i < grassList.size(); i++) {
			if (grassList.get(i).isMowed()) {
				mowed += 1;
			}
		}
		return mowed / total * 100f;
	}
	
	@Deprecated
	protected int generateColor() {
		if (rand.nextInt(10) == 0) {
			return rand.nextInt(200) + 56;
		} else {
			return rand.nextInt(150) + 106;
		}
	}
	
}
