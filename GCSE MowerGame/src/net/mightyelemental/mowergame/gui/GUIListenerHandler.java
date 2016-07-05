package net.mightyelemental.mowergame.gui;

import java.util.ArrayList;
import java.util.List;

public class GUIListenerHandler {

	private List<GUIListener> listeners = new ArrayList<GUIListener>();

	public void addListener(GUIListener toAdd) {
		listeners.add(toAdd);
	}

	public void onObjectPushed(GUIObject b, int button, int x, int y) {
		// Notify everybody that may be interested.
		for (GUIListener bl : listeners) {
			bl.onObjectPushed(b, button, x, y);
		}
	}

	public void onScrollBarDragged(ScrollBar sb, int x) {
		sb.changeBarPos(x - sb.getBar().getWidth() / 2);
		for (GUIListener bl : listeners) {
			bl.onScrollBarDragged(sb, x);
		}
	}

}
