package net.mightyelemental.mowergame.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.entities.living.EntityGnome;

public class EndGameOverlay {
	
	
	private GameState gs;
	
	public Color blackOverlay = new Color(20, 20, 20, 0);
	public Color endTextColor = new Color(255, 255, 255, 0);
	public float pauseTime = 400;
	public float pauseTime2 = 400;
	public float textOffset = 0;
	public Color income = new Color(50, 255, 50, 0);
	public Color outgoings = new Color(255, 50, 50, 0);
	public Color totalMoney = new Color(255, 255, 255, 0);
	public Color spaceContinue = new Color(255, 255, 255, 0);
	public int speedUpLevel = 0;
	
	public boolean canSkip = false;
	
	public EndGameOverlay( GameState gs ) {
		this.gs = gs;
		costPerAnimal = (gs.rand.nextInt(2000) / 100f) + 10;
		MowerGame.money += getTotal();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!gs.running) {
			g.setColor(blackOverlay);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			if (blackOverlay.a >= 0.8f) {
				g.setColor(endTextColor);
				int wid = g.getFont().getWidth("---GAME OVER---");
				g.drawString("---GAME OVER---", gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 - textOffset);
				String text = "You mowed " + MathHelper.round(gs.worldObj.grassCon.getPercentageMowed(), 1) + "% of the lawn";
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 30 - textOffset);
				
				renderGains(gc, sbg, g);
				renderLosses(gc, sbg, g);
				
				g.setColor(totalMoney);
				float totalMoney = getTotal();
				String min = "";
				if (totalMoney < 0) {
					min = "-";
				}
				text = "Total | " + min + "\u00A3" + Math.abs(totalMoney);
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 55);
				g.setColor(spaceContinue);
				text = "Press Space To Continue";
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 90);
			}
		}
	}
	
	public void renderGains(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		float moneyEarned = getGrassIncome();
		g.setColor(income);
		float wid = g.getFont().getWidth("Money Earned | \u00A3" + moneyEarned);
		g.drawString("Money Earned | \u00A3" + moneyEarned, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 - 30);
		String text = "Gnomes Killed | \u00A3" + getGnomeIncome();
		wid = g.getFont().getWidth(text);
		g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 - 10);
		
	}
	
	public void renderLosses(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(outgoings);
		float mowerDamage = getMowerCosts();
		mowerDamage = MathHelper.round(mowerDamage, 2);
		String text = "Mower Repairs | \u00A3" + mowerDamage;
		float wid = g.getFont().getWidth(text);
		g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 10);
		float animalCost = getAnimalCosts();
		animalCost = MathHelper.round(animalCost, 2);
		text = "Animals Killed | \u00A3" + animalCost;
		wid = g.getFont().getWidth(text);
		g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 30);
	}
	
	public float getGrassIncome() {
		float moneyEarned = 0;
		if (gs.worldObj.grassCon.getPercentageMowed() > 25) {
			moneyEarned = gs.worldObj.grassCon.grassList.size() * (gs.worldObj.grassCon.getPercentageMowed() / 100f);
			moneyEarned = moneyEarned / 2304f * 150f;
		}
		moneyEarned = MathHelper.round(moneyEarned, 2);
		return moneyEarned;
	}
	
	public float getGnomeIncome() {
		return MathHelper.round(EntityGnome.moneyGain * gs.worldObj.lawnMower.gnomesKilled, 2);
	}
	
	public float getIncome() {
		return getGrassIncome() + getGnomeIncome();
	}
	
	public float getMowerCosts() {
		return ((gs.worldObj.lawnMower.maxHealth - gs.worldObj.lawnMower.health) / 10f) * 9.0f;
	}
	
	public float costPerAnimal;
	
	public float getAnimalCosts() {
		return gs.worldObj.lawnMower.animalsKilled * costPerAnimal;
	}
	
	public float getOutgoings() {
		return getMowerCosts() + getAnimalCosts();
	}
	
	public float getTotal() {
		return MathHelper.round(getIncome() - getOutgoings(), 2);
	}
	
	public void update(int delta) {
		float s = 1 + (speedUpLevel * 0.5f);
		if (blackOverlay.a <= 0.8f) {
			blackOverlay.a += s * (1f / 17f / 6f) * (delta / 17f);
		} else if (endTextColor.a < 1f) {
			endTextColor.a += s * (1f / 17f / 2f) * (delta / 17f);
		} else if (pauseTime >= 0) {
			pauseTime -= s * delta;
		} else if (textOffset < 90) {
			textOffset += s * 1.2f * (delta / 17f);
		} else if (income.a < 1f) {
			income.a += s * (1f / 17f / 3f) * (delta / 17f);
		} else if (outgoings.a < 1f) {
			outgoings.a += s * (1f / 17f / 4f) * (delta / 17f);
		} else if (totalMoney.a < 1f) {
			totalMoney.a += s * (1f / 17f / 1f) * (delta / 17f);
		} else if (pauseTime2 >= 0) {
			pauseTime2 -= s * delta;
		} else if (spaceContinue.a <= 1f) {
			spaceContinue.a += s * (1f / 19f) * (delta / 17f);
			canSkip = true;
		}
	}
	
}
