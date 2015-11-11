package net.iridgames.munchkin.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.iridgames.munchkin.Munchkin;
import net.iridgames.munchkin.gui.Button;
import net.iridgames.munchkin.gui.CheckBox;
import net.iridgames.munchkin.gui.GUIListener;
import net.iridgames.munchkin.gui.ScrollBar;
import net.iridgames.munchkin.gui.TextBox;

public class StateGameLoading extends BasicGameState implements GUIListener {

	private final int ID;

	public StateGameLoading( int id ) {
		this.ID = id;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Munchkin.buttonHandler.addListener(this);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {

	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {

	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void onButtonPushed(Button b, int button) {

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
