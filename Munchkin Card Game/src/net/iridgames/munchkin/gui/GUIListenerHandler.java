package net.iridgames.munchkin.gui;

import java.util.ArrayList;
import java.util.List;

public class GUIListenerHandler {

	private List<GUIListener> listeners = new ArrayList<GUIListener>();

	public void addListener(GUIListener toAdd) {
		listeners.add(toAdd);
	}

	public void onButtonPushed(Button b, int button) {
		// Notify everybody that may be interested.
		for (GUIListener bl : listeners) {
			bl.onButtonPushed(b, button);
		}
	}

	public void onCheckBoxClicked(CheckBox cb) {
		cb.setChecked(!cb.isChecked());
		for (GUIListener bl : listeners) {
			bl.onCheckBoxClicked(cb);
		}
	}

	public void onScrollBarClicked(ScrollBar sb, float x) {
		sb.changeBarPos(x - sb.getBar().getWidth() / 2);
		for (GUIListener bl : listeners) {
			bl.onScrollBarClicked(sb, x);
		}
	}

	public void onScrollBarDragged(ScrollBar sb, int x) {
		sb.changeBarPos(x - sb.getBar().getWidth() / 2);
		for (GUIListener bl : listeners) {
			bl.onScrollBarDragged(sb, x);
		}
		System.out.println(sb.getValue());
	}

}
