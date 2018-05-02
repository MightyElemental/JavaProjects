package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIEntryPanel;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppCalculator extends AppWindow {

	private static final long serialVersionUID = 6922927509542762105L;

	public AppCalculator(float x, float y, float width, float height) {
		super(x, y, 400, 600, "Calculator");
		this.addGUIObject(new GUIButton(190, 20, "#x", this).setText("EXE"), 5, 210);
		this.addGUIObject(new GUIEntryPanel(390 - 2, 200, "#display", this), 5, 5);
		this.setSleepTime(50);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateContent(int delta) {
		// TODO Auto-generated method stub

	}

}
