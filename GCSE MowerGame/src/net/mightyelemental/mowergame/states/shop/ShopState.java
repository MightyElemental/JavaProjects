package net.mightyelemental.mowergame.states.shop;

import java.text.DecimalFormat;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MathHelper;
import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.entities.MowerType;
import net.mightyelemental.mowergame.gui.GUIListener;
import net.mightyelemental.mowergame.gui.GUIObject;
import net.mightyelemental.mowergame.gui.ScrollBar;

public class ShopState extends BasicGameState implements GUIListener {
	
	
	public Image background;
	public Image trump;
	public Image sign;
	
	public final int ID;
	
	public ShopButtons shopButtons;
	public ShopUpgradeButtons upgradeButtons;
	public ShopMowerButtons mowerButtons;
	public MowerInfo mowerInfo;
	
	public Purchases purchase;
	
	public static final int STATE_MAIN = 0;
	public static final int STATE_UPGRADE = 1;
	public static final int STATE_CHARACTERS = 2;
	public static final int STATE_MOWERS = 3;
	
	public int menuState = STATE_MAIN;
	
	public ShopState( int ID ) {
		this.ID = ID;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		shopButtons = new ShopButtons(gc);
		upgradeButtons = new ShopUpgradeButtons(gc);
		mowerButtons = new ShopMowerButtons(gc);
		purchase = new Purchases();
		mowerInfo = new MowerInfo(gc.getWidth() - 700, 100, this);
		background = MowerGame.resLoader.loadImage("shop.background").getScaledCopy(gc.getWidth(), gc.getHeight());
		trump = MowerGame.resLoader.loadImage("shop.trumpApproved").getScaledCopy(0.8f);
		sign = MowerGame.resLoader.loadImage("shop.sign").getScaledCopy(0.4f);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		g.drawImage(trump, 75, gc.getHeight() - trump.getHeight());
		g.drawImage(sign, 50, -10);
		g.setColor(Color.white);
		// Trump quote
		String text = "\"Getting rich is easy. Staying rich is harder.\"";
		if (MowerGame.money > 20) {
			text = "\"My father gave me a small loan of $1,000,000\"";
		} else if (MowerGame.money < -20) {
			text = "\"The wall just got 10ft higher\"";
		} else {
			text = "\"Getting rich is easy. Staying rich is harder.\"";
		}
		float x = 75 + trump.getWidth() / 2 - g.getFont().getWidth(text) / 2;
		if (x < 5) x = 5;
		g.fillRoundRect(x, gc.getHeight() - trump.getHeight() / 4, 6 + g.getFont().getWidth(text) + 5, 20, 3, 3);
		g.setColor(Color.black);
		g.drawString(text, 6 + x, gc.getHeight() - trump.getHeight() / 4);
		
		switch (menuState) {
			case STATE_MAIN:
				for (GUIObject b : shopButtons.objects) {
					b.draw(g);
				}
				break;
			case STATE_MOWERS:
				for (GUIObject b : mowerButtons.objects) {
					b.draw(g);
				}
				drawSelectedMower(g);
				break;
			case STATE_UPGRADE:
				for (GUIObject b : upgradeButtons.objects) {
					b.draw(g);
				}
				drawSelectedMower(g);
				break;
		}
		renderMoney(gc, sbg, g);
		if (infoDisplay) {
			mowerInfo.draw(gc, sbg, g, purchase.boughtMowers.get(upgradeButtons.mowerNumber), infoY);
		}
	}
	
	private void drawSelectedMower(Graphics g) {
		int h = (int) (upgradeButtons.selectMower.getHeight() - 20);
		float x = upgradeButtons.selectMower.getX() + upgradeButtons.selectMower.getWidth() - h - 10;
		float y = upgradeButtons.selectMower.getY() + 10;
		g.setColor(Color.white.darker());
		g.fillRoundRect(x, y, h + 1, h + 1, 15, 15);
		g.setColor(Color.black);
		g.drawRoundRect(x, y, h + 1, h + 1, 15, 15);
		g.drawImage(purchase.boughtMowers.get(upgradeButtons.mowerNumber).getDisplayIcon().getScaledCopy(h - 8, h - 8), x + 5, y + 5);
	}
	
	float infoY = 50;
	boolean infoDisplay = false;
	
	DecimalFormat df = new DecimalFormat("#,##0.00");
	
	public void renderMoney(GameContainer gc, StateBasedGame sbg, Graphics g) {
		int x = 200;
		int y = 50;
		g.setColor(Color.white);
		g.fillRect(x, gc.getHeight() - y, 100, 20);
		g.setColor(Color.black);
		if (MowerGame.money < 0) {
			g.setColor(Color.red);
		}
		MowerGame.money = MathHelper.round(MowerGame.money, 2);
		String mon = "\u00A3" + df.format(MowerGame.money);
		g.drawString(mon, x + 1, gc.getHeight() - y);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (returnToMenu) {
			sbg.enterState(MowerGame.STATE_MENU);
			returnToMenu = false;
		}
		MowerType mower = purchase.boughtMowers.get(upgradeButtons.mowerNumber);
		upgradeButtons.durability.setText(
			"Durability Upgrade \n\u00A3" + df.format(upgradeButtons.prices[1] * (Math.pow(mower.getDurabilityLevel(), 2.45))),
			gc.getGraphics());
		upgradeButtons.speed.setText(
			"Speed Upgrade \n\u00A3" + df.format(upgradeButtons.prices[0] * (Math.pow(mower.getSpeedLevel(), 2.15))), gc.getGraphics());
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		switch (this.menuState) {
			case STATE_MAIN:
				shopButtons.mousePressed(button, x, y);
				break;
			case STATE_UPGRADE:
				upgradeButtons.mousePressed(button, x, y);
				break;
			case STATE_MOWERS:
				mowerButtons.mousePressed(button, x, y);
				break;
		}
		
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
	public boolean returnToMenu;
	
	@Override
	public void onObjectPushed(GUIObject b, int button, int x, int y) {
		if (b.equals(shopButtons.back)) {
			returnToMenu = true;
		}
		if (b.equals(shopButtons.upgrades)) {
			this.menuState = STATE_UPGRADE;
		}
		if (b.equals(shopButtons.mowers)) {
			this.menuState = STATE_MOWERS;
		}
		if (b.equals(upgradeButtons.back) || b.equals(mowerButtons.back)) {
			this.menuState = STATE_MAIN;
		}
		MowerType mower = purchase.boughtMowers.get(upgradeButtons.mowerNumber);
		if (b.equals(upgradeButtons.durability)) {
			if (mower.getDurabilityLevel() < 4) {
				mower.setDurabilityLevel(mower.getDurabilityLevel() + 1);
				mower.setDurability(purchase.boughtMowers.get(upgradeButtons.mowerNumber).getDurability() + 1);
				upgradeButtons.durability.setText("Upgrade Durability");
			}
		}
		if (b.equals(upgradeButtons.speed)) {
			if (mower.getSpeedLevel() < 4) {
				mower.setSpeedLevel(mower.getSpeedLevel() + 1);
				purchase.boughtMowers.get(upgradeButtons.mowerNumber)
					.setSpeed(purchase.boughtMowers.get(upgradeButtons.mowerNumber).getSpeed() + 1);
				upgradeButtons.speed.setText("Speed Upgrade");
			}
		}
		if (b.equals(upgradeButtons.selectMower) || b.equals(mowerButtons.select)) {
			upgradeButtons.mowerNumber++;
			if (upgradeButtons.mowerNumber >= purchase.boughtMowers.size()) {
				upgradeButtons.mowerNumber = 0;
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		switch (this.menuState) {
			case STATE_MAIN:
				shopButtons.mouseMoved(newx, newy);
				break;
			case STATE_UPGRADE:
				upgradeButtons.mouseMoved(newx, newy);
				break;
			case STATE_MOWERS:
				mowerButtons.mouseMoved(newx, newy);
				break;
		}
	}
	
	@Override
	public void onScrollBarDragged(ScrollBar sb, int x) {
	}
	
	@Override
	public void onObjectHovered(GUIObject b, int x, int y) {
		if (!b.equals(upgradeButtons.back) && (menuState == ShopState.STATE_UPGRADE || menuState == ShopState.STATE_MOWERS)) {
			infoY = b.getY();
			infoDisplay = true;
		} else {
			infoDisplay = false;
		}
	}
	
}
