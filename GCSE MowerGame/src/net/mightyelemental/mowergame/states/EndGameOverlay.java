package net.mightyelemental.mowergame.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MathHelper;

public class EndGameOverlay {

	private GameState gs;

	public Color blackOverlay = new Color(20, 20, 20, 0);
	public Color endTextColor = new Color(255, 255, 255, 0);
	public float pauseTime = 1000;
	public float textOffset = 0;
	public Color income = new Color(50, 255, 50, 0);
	public Color outgoings = new Color(255, 50, 50, 0);
	public Color totalMoney = new Color(255, 255, 255, 0);

	public EndGameOverlay(GameState gs) {
		this.gs = gs;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (!gs.running) {
			g.setColor(blackOverlay);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			if (blackOverlay.a >= 0.8f) {
				g.setColor(endTextColor);
				int wid = g.getFont().getWidth("---GAME OVER---");
				g.drawString("---GAME OVER---", gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 - textOffset);
				String text = "You mowed " + MathHelper.round(gs.worldObj.grassCon.getPercentageMowed(), 1)
						+ "% of the lawn";
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 30 - textOffset);

				float moneyEarned = 0;
				if (gs.worldObj.grassCon.getPercentageMowed() > 85) {
					moneyEarned = 0.35f * gs.worldObj.grassCon.grassList.size()
							* (gs.worldObj.grassCon.getPercentageMowed() / 100f);
				}
				moneyEarned = MathHelper.round(moneyEarned, 2);
				g.setColor(income);
				wid = g.getFont().getWidth("Money Earned | \u00A3" + moneyEarned);
				g.drawString("Money Earned | \u00A3" + moneyEarned, gc.getWidth() / 2 - wid / 2,
						gc.getHeight() / 2 - 30);
				wid = g.getFont().getWidth("Gnomes Killed | \u00A30.0");
				g.drawString("Gnomes Killed | \u00A30.0", gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 - 10);

				g.setColor(outgoings);
				float mowerDamage = ((100 - gs.worldObj.lawnMower.health) / 10f) * 24.49f;
				mowerDamage = MathHelper.round(mowerDamage, 2);
				text = "Mower Repairs | \u00A3" + mowerDamage;
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 10);
				float animalCost = gs.worldObj.animalsKilled * 99.99f;
				animalCost = MathHelper.round(animalCost, 2);
				text = "Animals Killed | \u00A3" + animalCost;
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 30);

				g.setColor(totalMoney);
				float totalMoney = (moneyEarned + 0 - animalCost - mowerDamage);
				totalMoney = MathHelper.round(totalMoney, 2);
				String min = "";
				if (totalMoney < 0) {
					min = "-";
				}
				text = "Total | " + min + "\u00A3" + Math.abs(totalMoney);
				wid = g.getFont().getWidth(text);
				g.drawString(text, gc.getWidth() / 2 - wid / 2, gc.getHeight() / 2 + 60);
			}
		}
	}

	public void update(int delta) {
		if (blackOverlay.a <= 0.8f) {
			blackOverlay.a += (1f / 17f / 6f) * (delta / 17f);
		} else if (endTextColor.a < 1f) {
			endTextColor.a += (1f / 17f / 2f) * (delta / 17f);
		} else if (pauseTime >= 0) {
			pauseTime -= delta;
		} else if (textOffset < 170) {
			textOffset += 1.2f * (delta / 17f);
		} else if (income.a < 1f) {
			income.a += (1f / 17f / 3f) * (delta / 17f);
		} else if (outgoings.a < 1f) {
			outgoings.a += (1f / 17f / 4f) * (delta / 17f);
		} else if (totalMoney.a < 1f) {
			totalMoney.a += (1f / 17f / 1f) * (delta / 17f);
		}
	}

}
