package net.mightyelemental.mowergame.entities;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.World;
import net.mightyelemental.mowergame.entities.living.EntityGnome;
import net.mightyelemental.mowergame.entities.living.EntityLiving;
import net.mightyelemental.mowergame.entities.living.MovePath;
import net.mightyelemental.mowergame.particles.ParticleGrass;

public class EntityMower extends Entity {
	
	
	private static final long serialVersionUID = 4112241142072508351L;
	
	public MowerType mowerType = MowerType.MowveMonster;
	
	public float vel = 0f; // 8f
	public float maxVel = mowerType.getSpeed();
	
	public float maxHealth = mowerType.getDurability();
	public float health = mowerType.getDurability(); // 100f
	
	public MovePath aiPath;
	
	public Rectangle bladeRect;
	
	public int animalsKilled;
	
	public int gnomesKilled;
	
	public boolean mowerHasAI;
	
	public EntityMower( float x, float y, World worldObj, boolean mowerHasAI ) {
		super(x, y, 110, 110, worldObj);
		this.setWidth(mowerType.getSize());
		this.setHeight(mowerType.getSize());
		this.setIcon(mowerType.getImgPath());
		bladeRect = new Rectangle(x + width / 4, y + height / 4, width / 2.5f, height / 2.5f);
		this.mowerHasAI = mowerHasAI;
	}
	
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (aiPath == null && mowerHasAI) {
			aiPath = new MovePath(worldObj.rand.nextInt(1280), worldObj.rand.nextInt(720));
		}
		if (aiPath != null) {
			if (this.getCenterY() > aiPath.getY() - 60 && this.getCenterY() < aiPath.getY() + 60) {
				if (this.getCenterX() > aiPath.getX() - 60 && this.getCenterX() < aiPath.getX() + 60) {
					aiPath.hasReached = true;
				}
			}
		}
		if (aiPath != null && aiPath.hasReached) {
			aiPath = null;
			aiPath = new MovePath(worldObj.rand.nextInt(1280), worldObj.rand.nextInt(720));
		}
		
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();
		
		if (mowerHasAI) {
			mouseX = aiPath.getX();
			mouseY = aiPath.getY();
		}
		
		// Move towards mouse
		int x = (int) this.getCenterX();
		int y = (int) this.getCenterY();
		
		// System.out.println(Math.abs(x - mouseX) + Math.abs(y - mouseY));
		
		vel += mowerType.getAcceleration();
		if (vel >= maxVel) {
			vel = maxVel; // 5f
		}
		if (Math.abs(x - mouseX) + Math.abs(y - mouseY) > 60) {
			angle = MathHelper.getAngle(new Point(x, y), new Point(mouseX, mouseY));
			getIcon().setRotation(angle - 90);
		} else {
			vel -= mowerType.getAcceleration() * 2.2f;
		}
		if (vel <= 0) {
			vel = 0;
		}
		
		float amountToMoveX = (vel / 25f * delta * (float) Math.cos(Math.toRadians(angle)));
		float amountToMoveY = (vel / 25f * delta * (float) Math.sin(Math.toRadians(angle)));
		
		this.setCenterX(this.getCenterX() - amountToMoveX);
		this.setCenterY(this.getCenterY() - amountToMoveY);
		
		if (!mowerHasAI) {
			processHealth();
		} else {
			if (worldObj.getCollidingEntity(bladeRect) != null) {
				worldObj.getCollidingEntity(bladeRect).setDead();
			}
		}
		bladeRect.setCenterX(this.getCenterX());
		bladeRect.setCenterY(this.getCenterY());
		
		boolean mowed = worldObj.grassCon.setMowed(bladeRect);
		
		if (mowed) {
			for (int i = 0; i < worldObj.rand.nextInt(5) + 3; i++) {
				float tx = (float) Math.cos(Math.toRadians(angle)) * this.getWidth() / 2;
				float ty = (float) Math.sin(Math.toRadians(angle)) * this.getHeight() / 2;
				worldObj.createParticle(new ParticleGrass(getCenterX() + tx, getCenterY() + ty, worldObj));
			}
		}
	}
	
	private void processHealth() {
		EntityLiving ent = worldObj.getCollidingEntity(bladeRect);
		if (ent != null) {
			health -= ent.damageToMower;
			if (health <= 0) {
				health = 0;
			}
			worldObj.getCollidingEntity(bladeRect).setDead();
			MowerGame.gameState.timeTotalMs += ent.timeGain * 1000;
			MowerGame.gameState.timeMs += ent.timeGain * 1000;
			if (ent instanceof EntityGnome) {
				gnomesKilled++;
			} else {
				animalsKilled++;
			}
		}
	}
	
}
