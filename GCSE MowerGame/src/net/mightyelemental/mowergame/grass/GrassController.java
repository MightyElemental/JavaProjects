package net.mightyelemental.mowergame.grass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class GrassController {

	public List<Grass> grassList = new ArrayList<Grass>();

	private Random rand;

	public GrassController(Random rand) {
		this.rand = rand;
	}

	public void generateGrass(GameContainer gc, int size) throws SlickException {
		for (int w = 0; w < gc.getWidth(); w += size) {
			for (int h = 0; h < gc.getHeight(); h += size) {
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

	public boolean setMowed(Rectangle rect) throws SlickException {
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
