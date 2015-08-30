package net.minegeek360.reacthack;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Reaction {

	static Color col = new Color(75, 219, 106);

	public static void main(String[] args) {
		while (true) {
			try {
				Robot robot = new Robot();

				//
				// The the pixel color information at 20, 20
				//
				Color color = robot.getPixelColor(219, 326);

				//
				// Print the RGB information of the pixel color
				//
				if (col.getRGB() != color.getRGB()) {
					System.out.println("Red   = " + color.getRed());
					System.out.println("Green = " + color.getGreen());
					System.out.println("Blue  = " + color.getBlue());
					col = color;
				}
				if (color.getRed() == 75 && color.getGreen() == 219 && color.getBlue() == 106) {
					Thread.sleep(15);
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					//robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				}
			} catch (AWTException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
