package net.mightyelemental.mowergame.particles;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import net.mightyelemental.mowergame.World;

public class ParticleBloodSplat extends Particle {

	private static final long serialVersionUID = 2375105190211764804L;

	public List<Shape> splatParts = new ArrayList<Shape>();
	public List<Color> splatColors = new ArrayList<Color>();

	public ParticleBloodSplat(float x, float y, World worldObj) {
		super(x, y, 70, 70, worldObj);
		this.setIcon("particles.bloodsplat");
		// this.setWidth(worldObj.rand.nextInt(70) + 70);
		// this.setHeight(this.getWidth());
		this.setCenterX(x);
		this.setCenterY(y);
		generateBlood();
	}

	public void generateBlood() {
		for (int i = 0; i < worldObj.rand.nextInt(17) + 10; i++) {
			float x = getCenterX() + worldObj.rand.nextInt(70) - 35;
			float y = getCenterY() + worldObj.rand.nextInt(70) - 35;
			Circle c = new Circle(x, y, worldObj.rand.nextInt(5) + 4);
			splatParts.add(c);
			float d = worldObj.rand.nextInt(10) / 100f;
			splatColors.add(new Color(255 - d, 0, 0, 1f));
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		for (int i = 0; i < splatParts.size(); i++) {
			Shape s = splatParts.get(i);
			splatColors.get(i).a -= (0.025f / s.getHeight()) * (delta / 17f);
			System.out.println(splatColors.get(i).a);
		}
	}

}
