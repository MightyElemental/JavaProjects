package net.mightyelemental.mowergame.states.shop;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.gui.Button;
import net.mightyelemental.mowergame.gui.GUIObject;

public class ShopMowerButtons {
	
	
	public Button select;
	public Button buy;
	public Button back;
	
	public ShopMowerButtons( GameContainer gc ) {
		int x = gc.getWidth() - 400;
		select = new Button(x, 50, 340, 100).setText("Select", gc.getGraphics());
		objects.add(select);
		buy = new Button(x, 200, 340, 100).setText("Buy", gc.getGraphics());
		objects.add(buy);
		buy.setEnabled(false);
		back = new Button(x, 500, 340, 100).setText("Back", gc.getGraphics());
		objects.add(back);
	}
	
	public List<GUIObject> objects = new ArrayList<GUIObject>();
	
	public void mousePressed(int button, int x, int y) {
		guiPush(button, x, y, objects);
	}
	
	public void mouseMoved(int x, int y) {
		for (GUIObject b : objects) {
			if (b.contains(x, y)) {
				MowerGame.shopState.onObjectHovered(b, x, y);
				continue;
			}
		}
	}
	
	public void guiPush(int button, int x, int y, List<GUIObject> list) {
		for (GUIObject b : list) {
			if (b.contains(x, y) && b.isEnabled()) {
				MowerGame.shopState.onObjectPushed(b, button, x, y);
				continue;
			}
		}
	}
	
}
