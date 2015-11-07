package net.iridgames.munchkin.gui;

import java.util.ArrayList;
import java.util.List;

public class ButtonListenerHandler {

	private List<ButtonListener> listeners = new ArrayList<ButtonListener>();

	public void addListener(ButtonListener toAdd) {
		listeners.add(toAdd);
	}

	public void onButtonPushed(Button b, int button) {
		// Notify everybody that may be interested.
		for (ButtonListener bl : listeners) {
			bl.onButtonPushed(b, button);
		}
	}

}
