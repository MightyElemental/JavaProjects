package net.mightyelemental.mowergame.entities;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

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
	
	public float maxHealth;
	public float health; // 100f
	
	public MovePath aiPath;
	
	public Circle bladeArea;
	
	public int animalsKilled;
	
	public int gnomesKilled;
	
	public boolean mowerHasAI;
	
	public EntityMower( float x, float y, World worldObj, boolean mowerHasAI ) {
		super(x, y, 110, 110, worldObj);
		try {
			mowerType = MowerGame.shopState.purchase.boughtMowers.get(MowerGame.shopState.upgradeButtons.mowerNumber);
		} catch (Exception e) {
		}
		if (!mowerHasAI) {
			// try {
			// mowerType = MowerGame.shopState.purchase.boughtMowers
			// .get(MowerGame.shopState.upgradeButtons.mowerNumber);
			// } catch (Exception e) {
			// }
			// mowerType = MowerGame.shopState.
		}
		this.setWidth(mowerType.getSize());
		this.setHeight(mowerType.getSize());
		this.setIcon(mowerType.getImgPath());
		maxHealth = mowerType.getDurability();
		health = mowerType.getDurability();
		// bladeRect = new Rectangle(x + width / 4, y + height / 4, width /
		// 2.5f, height / 2.5f);
		bladeArea = new Circle(x + width / 4, y + height / 4, width / 2.5f / 2);
		this.mowerHasAI = mowerHasAI;
	}
	
	float lastAngle = 360;
	float angleTemp;
	float angleDiff;
	
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
		
		vel += mowerType.getBaseAcceleration();
		if (vel >= mowerType.getSpeed()) {
			vel = mowerType.getSpeed(); // 5f
		}
		if (Math.abs(x - mouseX) + Math.abs(y - mouseY) > 60) {
			angleTemp = MathHelper.getAngle(new Point(x, y), new Point(mouseX, mouseY));
			
			angle = angleTemp;
			if (mowerType.isFixedAngle()) {
				getIcon().setRotation(mowerType.getAngleOffset() - 90);
			} else {
				getIcon().setRotation(angle - 90);
			}
		} else {
			vel -= mowerType.getAcceleration() * 2.2f;
		}
		if (vel <= 0) {
			vel = 0;
		}
		
		float amountToMoveX = (vel / 25f * delta * (float) Math.cos(Math.toRadians(angle)));
		float amountToMoveY = (vel / 25f * delta * (float) Math.sin(Math.toRadians(angle)));
		
		if (!mowerType.isFixedToMouse()) {
			this.setCenterX(this.getCenterX() - amountToMoveX);
			this.setCenterY(this.getCenterY() - amountToMoveY);
		} else {
			this.setCenterX(mouseX);
			this.setCenterY(mouseY);
		}
		
		if (!mowerHasAI) {
			processHealth();
		} else {
			if (worldObj.getCollidingEntity(bladeArea) != null) {
				worldObj.getCollidingEntity(bladeArea).setDead();
			}
		}
		if (mowerType.getCenter() == null) {
			bladeArea.setCenterX(this.getCenterX());
			bladeArea.setCenterY(this.getCenterY());
		} else {
			bladeArea.setCenterX(this.getCenterX() + mowerType.getCenter().getX());
			bladeArea.setCenterY(this.getCenterY() + mowerType.getCenter().getY());
		}
		boolean mowed = worldObj.grassCon.setMowed(bladeArea);
		
		if (mowed) {
			for (int i = 0; i < worldObj.rand.nextInt(5) + 3; i++) {
				float tx = 0;
				float ty = 0;
				if (!mowerType.isFixedAngle()) {
					tx = (float) Math.cos(Math.toRadians(angle)) * this.getWidth() / 2;
					ty = (float) Math.sin(Math.toRadians(angle)) * this.getHeight() / 2;
				}
				float tempX = getCenterX() + tx;
				float tempY = getCenterY() + ty;
				if (mowerType.getCenter() != null) {
					tempX += mowerType.getCenter().getX();
					tempY += mowerType.getCenter().getY();
				}
				worldObj.createParticle(new ParticleGrass(tempX, tempY, worldObj));
			}
		}
	}
	
	private void processHealth() {
		EntityLiving ent = worldObj.getCollidingEntity(bladeArea);
		if (ent != null) {
			health -= ent.damageToMower;
			if (health <= 0) {
				health = 0;
			}
			worldObj.getCollidingEntity(bladeArea).setDead();
			MowerGame.gameState.timeTotalMs += ent.timeGain * 1000;
			MowerGame.gameState.timeMs += ent.timeGain * 1000;
			if (ent instanceof EntityGnome) {
				gnomesKilled++;
			} else {
				animalsKilled++;
			}
		}
	}
	
	public void smoothRotate(int delta) {
		if (lastAngle > angleTemp - 0.01f || lastAngle < angleTemp + 0.01f) {// remove
			// jittering
			
			if (lastAngle != 0) {
				angleDiff += lastAngle - angleTemp;
			}
			if (angleTemp > 90 || angleTemp < 270) {
				if (lastAngle > 350 && angleTemp < 10) {
					angleDiff -= 360;
				}
				if (lastAngle < 10 && angleTemp > 350) {
					angleDiff += 360;
				}
			}
			if (angleDiff > 360) {
				angleDiff -= 360;
			} else if (angleDiff < -360) {
				angleDiff += 360;
			}
			
			lastAngle = angleTemp;
			
			if (angleDiff > 0) {
				angleDiff -= mowerType.getTurnAngle() * MathHelper.round((delta / 16f), 2);
				angle -= mowerType.getTurnAngle() * MathHelper.round((delta / 16f), 2);
			} else {
				angleDiff += mowerType.getTurnAngle() * MathHelper.round((delta / 16f), 2);
				angle += mowerType.getTurnAngle() * MathHelper.round((delta / 16f), 2);// causes
				// major
				// jitter
			}
		}
	}
	
}
