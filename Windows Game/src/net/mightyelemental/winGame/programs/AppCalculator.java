package net.mightyelemental.winGame.programs;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUIEntryPanel;
import net.mightyelemental.winGame.guiComponents.GUIPanel;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppCalculator extends AppWindow {

	private static final long serialVersionUID = 6922927509542762105L;

	GUIEntryPanel entryPan = new GUIEntryPanel(390 - 2, 200, "#display", this);

	public static ScriptEngineManager manager = new ScriptEngineManager();
	public static ScriptEngine engine = manager.getEngineByName("js");

	public AppCalculator(float x, float y, float width, float height) {
		super(x, y, 400, 600, "Calculator");
		GUIPanel p = new GUIPanel(400 - 12, 600 - 260, this).setGridLayout(4, 5).setColor(Color.red);
		// this.addGUIObject(new GUIButton(190, 20, "#x", this).setText("EXE"), 5, 210);
		this.addGUIObject(entryPan, 5, 5);
		this.addGUIObject(p, 5, 210);
		for (int i = 0; i <= 9; i++) {
			p.addGUIObject(new GUIButton(190, 20, "#num_" + i, this).setText("#" + i), 0, 0);
		}
		p.addGUIObject(new GUIButton(0, 0, "#+", this).setText("+"), 0, 0);
		p.addGUIObject(new GUIButton(0, 0, "#*", this).setText("x"), 0, 0);
		p.addGUIObject(new GUIButton(0, 0, "#/", this).setText("/"), 0, 0);
		p.addGUIObject(new GUIButton(0, 0, "#-", this).setText("-"), 0, 0);
		p.addGUIObject(new GUIButton(0, 0, "#.", this).setText("."), 0, 0);
		p.addGUIObject(new GUIButton(0, 0, "#del", this).setText("DEL"), 0, 0);
		p.addGUIObject(new GUIButton(190, 20, "#exe", this).setText("EXE"), 0, 0);
		this.setSleepTime(50);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		this.clearScreen();
	}

	@Override
	public void updateContent(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComponentPressed(int button, GUIComponent c) {
		String nid = c.getNID();
		if (entryPan.getLatestEntry() == null) {
			entryPan.addEntry("");
		}
		if (entryPan.getLatestEntry().isFinalized()) {
			entryPan.addEntry("");
		}
		if (nid.startsWith("NUM_")) {
			if (!(entryPan.getLatestEntry().getText().length() == 0 && nid.equals("NUM_0"))) {
				entryPan.getLatestEntry().getBuilder().append(nid.replaceFirst("NUM_", ""));
			}
		} else if (nid.equals("EXE")) {
			try {
				Object o = engine.eval(entryPan.getLatestEntry().getText());
				entryPan.getLatestEntry().setFinalized();
				entryPan.addEntry(o.toString(), true, true);
			} catch (ScriptException e) {
				e.printStackTrace();
			}

		} else if (nid.equals("DEL") && entryPan.getLatestEntry().getBuilder().length() > 0) {
			entryPan.getLatestEntry().getBuilder().deleteCharAt(entryPan.getLatestEntry().getBuilder().length() - 1);
		} else {
			entryPan.getLatestEntry().getBuilder().append(nid);
		}
	}

}
