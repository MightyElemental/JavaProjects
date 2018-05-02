package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIPanel;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppCalculator extends AppWindow {

	private static final long serialVersionUID = 6922927509542762105L;

	public AppCalculator(float x, float y, float width, float height) {
		super(x, y, 400, 600, "Calculator");
		GUIPanel p = new GUIPanel(400 - 12, 600 - 260, this).setGridLayout(4, 3).setColor(Color.red);
		// this.addGUIObject(new GUIButton(190, 20, "#x", this).setText("EXE"), 5, 210);
		// this.addGUIObject(new GUIEntryPanel(390 - 2, 200, "#display", this), 5, 5);
		this.addGUIObject(p, 5, 210);
		for ( int i = 1; i <= 9; i++ ) {
			p.addGUIObject(new GUIButton(190, 20, "#" + i, this).setText("#" + i), 0, 0);
		}
		p.addGUIObject(new GUIButton(190, 20, "#x", this).setText("EXE"), 0, 0);
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
