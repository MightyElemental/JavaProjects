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
	
	public ShopState( int ID ) {
		this.ID = ID;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		shopButtons = new ShopButtons(gc);
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
		g.fillRoundRect(0, gc.getHeight() - trump.getHeight() / 4, 6+g.getFont().getWidth("\"My father gave me a small loan of $1,000,000\"") + 5,
			20, 3, 3);
		g.setColor(Color.black);
		g.drawString("\"My father gave me a small loan of $1,000,000\"", 6, gc.getHeight() - trump.getHeight() / 4);
		for (GUIObject b : shopButtons.objects) {
			b.draw(g);
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (returnToMenu) {
			sbg.enterState(MowerGame.STATE_MENU);
			returnToMenu = false;
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		shopButtons.mousePressed(button, x, y);
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
