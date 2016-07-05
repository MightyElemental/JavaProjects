package net.mightyelemental.mowergame.gui;

public interface GUIListener {
	
	
	public void onButtonPushed(Button b, int button);
	
	public void onCheckBoxClicked(CheckBox cb);
	
	public void onScrollBarClicked(ScrollBar sb, float x);
	
	public void onScrollBarDragged(ScrollBar sb, int x);
	
	public void onTextBoxClicked(TextBox tb, int x, int y);
	
	public void onObjectHovered(GUIObject b, int x, int y);
	
}
