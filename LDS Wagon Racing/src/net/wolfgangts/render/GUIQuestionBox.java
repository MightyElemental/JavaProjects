package net.wolfgangts.render;

import java.awt.Rectangle;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.ldsracing.LDSRacing;

public class GUIQuestionBox {

	public GUIButton[] buttons;
	private String[] answers;
	private String question;
	private Color color = Color.red;
	private Color fontColor = Color.black;

	public float sizeRatio = 1f / 10f;

	public Rectangle body;
	public GUIButton questionBox;

	public GUIQuestionBox(String question, String[] answers, int correctAnswerIndex, GameContainer gc) {
		if (answers == null) { throw new NullPointerException("Answer array cannot be null!"); }
		if (answers.length > 3) { throw new ArrayIndexOutOfBoundsException("Answer array cannot contain more than 3 answers!"); }
		this.answers = answers;
		this.question = question;
		this.buttons = new GUIButton[answers.length];
		init(gc);
		initButtons();
	}

	public GUIQuestionBox(String question, String[] answers, int correctAnswerIndex, GameContainer gc, Color col1, Color col2) {
		this(question, answers, correctAnswerIndex, gc);
		this.color = col1;
		this.fontColor = col2;
	}

	private Random rand = new Random();

	private void initButtons() {
		int buttonWidth = body.width / 4;
		int buttonHeight = body.height / 4;

		int temp = (body.width - (buttonWidth * answers.length)) / (answers.length);

		for (int i = 0; i < answers.length; i++) {

			final String tempStr = answers[i];

			buttons[i] = new GUIButton(body.x + (temp * (i + 0.5f)) + (buttonWidth * i),
					body.y + body.height - buttonHeight - (body.height / 9), buttonWidth, buttonHeight, tempStr)
							.setHoverEvent(new Runnable() {
								public void run() {
									GUIToolTip.setHint(tempStr);
								}
							}).setClickEvent(new Runnable() {

								@Override
								public void run() {
									LDSRacing.playState.isInQuestion = false;
									LDSRacing.playState.setNextQuestionTime(rand.nextInt(4) + 3);
									LDSRacing.playState.gui.currentQuestion = -1;
								}

							});
		}
	}

	public void init(GameContainer gc) {
		body = new Rectangle((int) (gc.getWidth() * sizeRatio), (int) (gc.getHeight() * sizeRatio), (int) (gc.getWidth() * sizeRatio * 8),
				(int) (gc.getHeight() * sizeRatio * 8));
		questionBox = new GUIButton(body.x + (body.width / 18), body.y + (body.height / 18), (int) (body.width / 18.0 * 16),
				(int) (body.height / 18.0 * 6)).setHoverEvent(new Runnable() {
					public void run() {
						GUIToolTip.setHint(question);
					}
				});
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(color);
		g.fillRoundRect(body.x, body.y, body.width, body.height, 5);
		g.setColor(color.darker());
		g.fillRoundRect(questionBox.x, questionBox.y, questionBox.width, questionBox.height, 5);
		g.setColor(fontColor);
		if (LDSRacing.font.getWidth(this.question) < questionBox.width - 10) {
			g.drawString(question, questionBox.x + (questionBox.width / 2) - (LDSRacing.font.getWidth(this.question) / 2),
					questionBox.y + (questionBox.height / 2) - (LDSRacing.font.getHeight(this.question) / 2));
		} else {
			int height = LDSRacing.font.getHeight();
			int temp = LDSRacing.font.getWidth("a");
			int temp2 = (questionBox.width - 5) / temp;
			StringBuilder sb = new StringBuilder(this.question);
			sb.setLength(temp2 - 1);
			int width = LDSRacing.font.getWidth(sb.toString() + "...");
			g.drawString(sb.toString() + "...", questionBox.x + questionBox.width / 2 - width / 2,
					questionBox.y + questionBox.height / 2 - height / 2);
		}
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].render(gc, sbg, g);
		}
		initButtons();
	}

}
