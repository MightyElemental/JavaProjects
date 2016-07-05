package net.mightyelemental.mowergame.gui;

public interface GUIListener {

	public void onObjectPushed(GUIObject b, int button, int x, int y);

	public void onScrollBarDragged(ScrollBar sb, int x);

	public void onObjectHovered(GUIObject b, int x, int y);

}
