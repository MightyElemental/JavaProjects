package net.mightyelemental.mowergame.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import net.mightyelemental.mowergame.MowerGame;
import net.mightyelemental.mowergame.gui.Button;
import net.mightyelemental.mowergame.gui.CheckBox;
import net.mightyelemental.mowergame.gui.GUIObject;
import net.mightyelemental.mowergame.gui.ScrollBar;
import net.mightyelemental.mowergame.gui.TextBox;

public class ShopUpgradeButtons {

	public Button selectMower;
	public Button speed;
	public Button durability;
	public Button back;

	public int durabilityLevel = 0;
	public int speedLevel = 0;

	public ShopUpgradeButtons(GameContainer gc) {
		int x = gc.getWidth() - 400;
		selectMower = new Button(x, 50, 340, 100).setText("Select", gc.getGraphics());
		objects.add(selectMower);
		speed = new Button(x, 200, 340, 100).setText("Speed Upgrade", gc.getGraphics());
		objects.add(speed);
		durability = new Button(x, 350, 340, 100).setText("Durability Upgrade", gc.getGraphics());
		objects.add(durability);
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
