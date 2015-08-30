package net.mightyelemental.ldsracing;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.wolfgangts.render.GUIButton;
import net.wolfgangts.render.GUIRender;
import net.wolfgangts.render.GUIToolTip;

public class MenuState extends BasicGameState {

	private final int ID;

	private GUIRender gui;
	private int stateToChange = LDSRacing.STATE_MENU;

	/* End */

	public MenuState(int stateMenu) {
		this.ID = stateMenu;

	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gui = new GUIRender();

		gui.addButton(new GUIButton(0, gc.getHeight() / 2, gc.getWidth(), 50, "Start").setColor(new Color(255, 0, 0, 255))
				.setClickEvent(new Runnable() {
					public void run() {
						stateToChange = LDSRacing.STATE_PLAY;
					}
				}).setHoverEvent(new Runnable() {
					public void run() {
						GUIToolTip.setHint("Start the game");
					}
				}));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		gui.render(gc, sbg, g);
		GUIToolTip.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		gui.update(gc, sbg, delta);
		if (sbg.getCurrentState().getID() != stateToChange) sbg.enterState(stateToChange);
		GUIToolTip.update();

	}

	@Override
	public int getID() {
		return ID;
	}

}
