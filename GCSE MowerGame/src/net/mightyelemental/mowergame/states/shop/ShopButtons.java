package net.mightyelemental.mowergame.states.shop;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.gui.Button;
import net.mightyelemental.mowergame.gui.CheckBox;
import net.mightyelemental.mowergame.gui.GUIObject;
import net.mightyelemental.mowergame.gui.ScrollBar;
import net.mightyelemental.mowergame.gui.TextBox;

public class ShopButtons {
	
	
	public Button mowers;
	public Button character;
	public Button upgrades;
	public Button back;
	
	public ShopButtons( GameContainer gc ) {
		int x = gc.getWidth() - 400;
		mowers = new Button(x, 50, 340, 100).setText("Mowers", gc.getGraphics());
		objects.add(mowers);
		character = new Button(x, 200, 340, 100).setText("Character Upgrades", gc.getGraphics());
		objects.add(character);
		upgrades = new Button(x, 350, 340, 100).setText("Upgrades", gc.getGraphics());
		objects.add(upgrades);
		back = new Button(x, 500, 340, 100).setText("Back", gc.getGraphics());
		objects.add(back);
	}
	
	public List<GUIObject> objects = new ArrayList<GUIObject>();
	
	public void mousePressed(int button, int x, int y) {
		guiPush(button, x, y, objects);
	}
	
	public void guiPush(int button, int x, int y, List<GUIObject> list) {
		for (GUIObject b : list) {
			if (b instanceof Button) {
				if (b.contains(x, y)) {
					MowerGame.shopState.onButtonPushed((Button) b, button);
					continue;
				}
			}
			if (b instanceof CheckBox) {
				if (b.contains(x, y)) {
					MowerGame.shopState.onCheckBoxClicked((CheckBox) b);
					continue;
				}
			}
			if (b instanceof ScrollBar) {
				if (b.contains(x, y)) {
					MowerGame.shopState.onScrollBarClicked((ScrollBar) b, x);
					continue;
				}
			}
			if (b instanceof TextBox) {
				if (b.contains(x, y)) {
					MowerGame.shopState.onTextBoxClicked((TextBox) b, x, y);
					continue;
				}
			}
		}
	}
	
}
