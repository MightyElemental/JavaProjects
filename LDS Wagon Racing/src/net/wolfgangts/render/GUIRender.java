package net.wolfgangts.render;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class GUIRender {
	private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
	private ArrayList<GUIQuestionBox> question = new ArrayList<GUIQuestionBox>();
	public int currentQuestion = -1;

	public GUIRender() {

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (Object buttonObj : buttons.toArray()) {
			GUIButton button = (GUIButton) buttonObj;
			button.render(gc, sbg, g);
		}
		if (currentQuestion >= 0) {
			Object[] buttonObj = question.toArray();
			GUIQuestionBox question = (GUIQuestionBox) buttonObj[currentQuestion];
			question.render(gc, sbg, g);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		if (gc.getInput().isMouseButtonDown(0)) {
			for (Object buttonObj : buttons.toArray()) {
				GUIButton button = (GUIButton) buttonObj;
				Rectangle position = button.getPosition();

				if (position.contains(new Point(gc.getInput().getMouseX(), gc.getInput().getMouseY()))) {
					button.callClickEvent();
				}
			}
			if (currentQuestion >= 0) {
				Object[] buttonObj = question.toArray();

				GUIButton[] button = ((GUIQuestionBox) buttonObj[currentQuestion]).buttons;
				for (int i = 0; i < button.length; i++) {
					Rectangle position = button[i].getPosition();

					if (position.contains(new Point(gc.getInput().getMouseX(), gc.getInput().getMouseY()))) {
						button[i].callClickEvent();
					}
				}
			}

		} else {
			for (Object buttonObj : buttons.toArray()) {
				GUIButton button = (GUIButton) buttonObj;
				Rectangle position = button.getPosition();

				if (position.contains(new Point(gc.getInput().getMouseX(), gc.getInput().getMouseY()))) {
					button.callHoverEvent();
				}
			}

			if (currentQuestion >= 0) {
				Object[] buttonObj = question.toArray();

				GUIButton buttonQ = ((GUIQuestionBox) buttonObj[currentQuestion]).questionBox;
				Rectangle positionQ = buttonQ.getPosition();

				if (positionQ.contains(new Point(gc.getInput().getMouseX(), gc.getInput().getMouseY()))) {
					buttonQ.callHoverEvent();
				}

				GUIButton[] button = ((GUIQuestionBox) buttonObj[currentQuestion]).buttons;
				for (int i = 0; i < button.length; i++) {
					Rectangle position = button[i].getPosition();

					if (position.contains(new Point(gc.getInput().getMouseX(), gc.getInput().getMouseY()))) {
						button[i].callHoverEvent();
					}
				}
			}
		}
	}

	public ArrayList<GUIButton> getButtons() {
		return this.buttons;
	}

	public void addButton(GUIButton button) {
		buttons.add(button);
	}

	public GUIQuestionBox addQuestionBox(String question, String[] answers, int correctAnswerIndex, GameContainer gc) {
		GUIQuestionBox newButton = new GUIQuestionBox(question, answers, correctAnswerIndex, gc);
		this.question.add(newButton);
		return newButton;
	}

	public GUIButton addButton(int x, int y, int i, int j) {
		GUIButton newButton = new GUIButton(x, y, i, j);

		buttons.add(newButton);
		return newButton;
	}

	public GUIButton addButton(int x, int y, int i, int j, String title) {
		GUIButton newButton = new GUIButton(x, y, i, j, title);

		buttons.add(newButton);
		return newButton;
	}
}
