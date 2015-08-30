package net.mightyelemental.ldsracing;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.ldsracing.wagon.Wagon;
import net.mightyelemental.ldsracing.wagon.parts.WagonParts;
import net.wolfgangts.render.GUIRender;
import net.wolfgangts.render.GUIToolTip;

public class PlayState extends BasicGameState {

	private final int	ID;
	public GUIRender	gui;
	private long		nextQuestionTime;
	private Random		rand	= new Random();

	public Wagon[] wagons = new Wagon[5];

	public boolean isInQuestion;

	public PlayState( int playState ) {
		this.ID = playState;
	}

	String[][]	answerOption	= { { "A Boat", "A House", "A Chair" }, { "Jonah", "Moses", "Daniel" },
			{ "Thomas S. Monson", "Gordon B. Hinckley", "Joseph Smith Jr." }, { "a puddle", "a pillar of salt", "a cow" } };
	int[]		answers			= { 0, 1, 0, 1 };
	String[]	questions		= { "What did the lord command Nephi to build?", "Which prophet got swallowed by a big fish?",
			"Who is the prophet right now?", "What did Lot's wife turn into?" };

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gui = new GUIRender();

		for (int i = 0; i < questions.length; i++) {
			gui.addQuestionBox(questions[i], answerOption[i], answers[i], gc);
		}
		setNextQuestionTime(2);
		wagons[0] = new Wagon(WagonParts.Cart_Type_1, WagonParts.Wheel_Type_4, WagonParts.Cover_Type_1, WagonParts.Horse_Type_1);
	}

	public void setNextQuestionTime(double seconds) {
		nextQuestionTime = (long) (System.currentTimeMillis() + (seconds * 1000));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (int i = 0; i < wagons.length; i++) {
			if(wagons[i] != null){
				wagons[i].draw(gc, sbg, g);
			}
		}
		gui.render(gc, sbg, g);
		GUIToolTip.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		gui.update(gc, sbg, delta);
		if (!isInQuestion) {
			if (System.currentTimeMillis() >= nextQuestionTime) {
				gui.currentQuestion = rand.nextInt(questions.length - 1);
				isInQuestion = true;
			}
			if (nextQuestionTime - System.currentTimeMillis() % 10 == 0) {
				System.out.println((nextQuestionTime - System.currentTimeMillis()) / 1000.0);
			}
		}
		GUIToolTip.update();
	}

	@Override
	public int getID() {
		return ID;
	}

}
