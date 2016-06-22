package net.mightyelemental.mowergame.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.gui.Button;
import net.mightyelemental.mowergame.gui.CheckBox;
import net.mightyelemental.mowergame.gui.GUIListener;
import net.mightyelemental.mowergame.gui.GUIObject;
import net.mightyelemental.mowergame.gui.ScrollBar;
import net.mightyelemental.mowergame.gui.TextBox;

public class ShopState extends BasicGameState implements GUIListener {

	public Image background;
	public Image trump;
	public Image sign;

	public final int ID;

	public ShopButtons shopButtons;
	public ShopUpgradeButtons upgradeButtons;

	public static final int STATE_MAIN = 0;
	public static final int STATE_UPGRADE = 1;
	public static final int STATE_CHARACTERS = 2;
	public static final int STATE_MOWERS = 3;

	public int menuState = STATE_MAIN;

	public ShopState(int ID) {
		this.ID = ID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		shopButtons = new ShopButtons(gc);
		upgradeButtons = new ShopUpgradeButtons(gc);
		background = MowerGame.resLoader.loadImage("shop.background").getScaledCopy(gc.getWidth(), gc.getHeight());
		trump = MowerGame.resLoader.loadImage("shop.trumpApproved").getScaledCopy(0.8f);
		sign = MowerGame.resLoader.loadImage("shop.sign").getScaledCopy(0.4f);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		g.drawImage(trump, 50, gc.getHeight() - trump.getHeight());
		g.drawImage(sign, 50, -10);
		g.setColor(Color.white);
		g.fillRoundRect(0, gc.getHeight() - trump.getHeight() / 4,
				6 + g.getFont().getWidth("\"My father gave me a small loan of $1,000,000\"") + 5, 20, 3, 3);
		g.setColor(Color.black);
		g.drawString("\"My father gave me a small loan of $1,000,000\"", 6, gc.getHeight() - trump.getHeight() / 4);
		switch (menuState) {
		case STATE_MAIN:
			for (GUIObject b : shopButtons.objects) {
				b.draw(g);
			}
			break;
		case STATE_UPGRADE:
			for (GUIObject b : upgradeButtons.objects) {
				b.draw(g);
			}
			break;
		}
		renderMoney(gc, sbg, g);
	}

	public void renderMoney(GameContainer gc, StateBasedGame sbg, Graphics g) {
		int x = 200;
		int y = 50;
		g.setColor(Color.white);
		g.fillRect(x, gc.getHeight() - y, 100, 20);
		g.setColor(Color.black);
		g.drawString("\u00A30", x, gc.getHeight() - y);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (returnToMenu) {
			sbg.enterState(MowerGame.STATE_MENU);
			returnToMenu = false;
		}
		upgradeButtons.durability.setText(upgradeButtons.durability.getText(), gc.getGraphics());
		upgradeButtons.speed.setText(upgradeButtons.speed.getText(), gc.getGraphics());
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
		}

	}

	@Override
	public int getID() {
		return ID;
	}

	public boolean returnToMenu;

	@Override
	public void onButtonPushed(Button b, int button) {
		if (b.equals(shopButtons.back)) {
			returnToMenu = true;
		}
		if (b.equals(shopButtons.upgrades)) {
			this.menuState = STATE_UPGRADE;
		}
		if (b.equals(upgradeButtons.back)) {
			this.menuState = STATE_MAIN;
		}
		if (b.equals(upgradeButtons.durability)) {
			if (upgradeButtons.durabilityLevel < 4) {
				upgradeButtons.durabilityLevel++;
				MowerGame.gameState.worldObj.lawnMower.maxHealth += 10;
				MowerGame.gameState.worldObj.lawnMower.health += 10;
				upgradeButtons.durability
						.setText("Upgrade Durability (" + MowerGame.gameState.worldObj.lawnMower.maxHealth + "+10)");
			}
		}
		if (b.equals(upgradeButtons.speed)) {
			if (upgradeButtons.speedLevel < 4) {
				upgradeButtons.speedLevel++;
				MowerGame.gameState.worldObj.lawnMower.maxVel += 1;
				upgradeButtons.speed.setText("Speed Upgrade (" + MowerGame.gameState.worldObj.lawnMower.maxVel + "+1)");
			}
		}
	}

	@Override
	public void onCheckBoxClicked(CheckBox cb) {

	}

	@Override
	public void onScrollBarClicked(ScrollBar sb, float x) {

	}

	@Override
	public void onScrollBarDragged(ScrollBar sb, int x) {
	}

	@Override
	public void onTextBoxClicked(TextBox tb, int x, int y) {
	}

}
