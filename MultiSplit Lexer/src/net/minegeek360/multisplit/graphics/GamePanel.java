package net.minegeek360.multisplit.graphics;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings( "serial" )
public class GamePanel extends JPanel {

	public ArrayList<Object> objects = new ArrayList<Object>();

	public GamePanel() {

	}

	@Override
	public void paint(Graphics g) {
		// super.paintComponent(g);
		for (Object obj : objects) {
			if (obj instanceof Rectangle) {
				Rectangle rect = (Rectangle) obj;
				g.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
			if (obj instanceof Object[]) {
				Object[] str = (Object[]) obj;
				int x = (int) str[1];
				int y = (int) str[2];
				g.drawString(str[0] + "", x, y);
			}
		}
	}

	public void addRect(int x, int y, int width, int height) {
		objects.add(new Rectangle(x, y, width, height));
	}

	public void addString(String str, int x, int y) {
		objects.add(new Object[] { str, x, y });
	}

}
