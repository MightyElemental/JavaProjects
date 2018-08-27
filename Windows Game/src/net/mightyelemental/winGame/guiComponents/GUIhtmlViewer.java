package net.mightyelemental.winGame.guiComponents;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class GUIhtmlViewer extends GUIComponent {

	private static final long serialVersionUID = 3497817514161328855L;

	private enum commands {
		newLine, para, endPara, link, endLink;
	}

	private StringBuffer html;

	private List<Object> values = new ArrayList<Object>();

	public GUIhtmlViewer(float width, float height, String uid, AppWindow aw) {
		super(width, height, uid, aw);
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.draw(gc, sbg, g);
		int line = 0;
		int xOff = 0;
		for (Object o : values) {
			if (o.equals(commands.para)) {
				xOff += 20;
			} else if (o.equals(commands.endPara)) {
				xOff -= 20;
			} else if (o.equals(commands.newLine)) {
				line += 20;
			} else {
				g.drawString(o.toString(), 5 + xOff + this.getX(), line + this.getY());
			}
		}
	}

	public GUIhtmlViewer setHTML(StringBuffer html) {
		this.html = html;
		parse();
		return this;
	}

	public void parse() {// TODO: FIX THIS
		values.clear();
		char[] chars = html.toString().toCharArray();
		boolean tag = false;
		StringBuffer buff = new StringBuffer();
		StringBuffer tagBuff = new StringBuffer();
		for (char c : chars) {
			if (c == '<') {
				if (buff.length() > 0)
					values.add(buff.toString());
				if (buff.length() > 0)
					buff.delete(0, buff.length());
				if (tagBuff.length() > 0)
					tagBuff.delete(0, tagBuff.length());
				System.out.println(buff.toString());
				tag = true;
			}
			if (tag) {
				tagBuff.append(c);
			} else {
				buff.append(c);
			}
			if (c == '>') {
				// if (buff.length() > 0)buff.delete(0, buff.length() - 1);
				tag = false;

				switch (tagBuff.toString()) {
				case "<a>":
				case "<a ":
					values.add(commands.link);
					break;
				case "<p>":
				case "<div>":
				case "<p ":
					values.add(commands.para);
					break;
				case "</div>":
				case "</p>":
					values.add(commands.endPara);
					break;
				case "</a>":
					values.add(commands.endLink);
					break;
				case "<br>":
				case "<br/>":
				case "</br>":
					values.add(commands.newLine);
					break;
				}
			}
		}
		values.add(buff.toString());
		System.out.println(values);
	}

}
