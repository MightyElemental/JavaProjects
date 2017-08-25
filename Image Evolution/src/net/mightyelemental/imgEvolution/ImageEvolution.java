package net.mightyelemental.imgEvolution;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;

public class ImageEvolution implements Game {

	public static ImageGen target, guess;

	int	targetFit		= 0;
	int	generationCount	= 0;

	public ImageEvolution() throws SlickException {

	}

	public static void main(String[] args) {
		AppGameContainer appGc;
		try {
			SoundStore.get().init();
			appGc = new AppGameContainer(new ImageEvolution());
			appGc.setDisplayMode(800, (int) (800 / 16.0 * 9.0), false);
			appGc.setTargetFrameRate(60);
			appGc.setShowFPS(false);
			appGc.setAlwaysRender(true);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void newGeneration() throws SlickException {
		int bestFitness = calcFitness(guess);
		generationCount++;
		while (bestFitness == calcFitness(guess)) {
			ImageGen ig = new ImageGen(guess);
			ig.setPixel(rand.nextInt(target.imgSize), rand.nextInt(target.imgSize), rand.nextInt(256));
			if ( calcFitness(ig) > bestFitness ) {
				guess.setPixels(ig.pixels);
				break;
			}
		}
	}

	int pos = 0;

	public void newGenerationV2() {
		if ( pos == -1 ) return;
		int bestFitness = calcFitness(guess);
		generationCount++;
		int bestCol = -1;
		int prevFit = Integer.MIN_VALUE;
		for ( int i = 0; i < 256; i++ ) {
			guess.pixels[pos] = i;
			int f = calcFitness(guess);
			if ( f > bestFitness ) {
				bestCol = i;
				bestFitness = f;
			}
			if ( f < prevFit ) break;
			prevFit = f;
		}
		guess.pixels[pos] = bestCol;
		if ( pos % (guess.imgSize / 2) == 0 ) {
			guess.imgBeenEdited = true;
		}
		pos++;
		if ( pos >= guess.pixels.length ) pos = -1;
	}

	public void newGenerationV3() throws SlickException {
		int bestFitness = calcFitness(guess);
		generationCount++;
		int[] pix = new int[guess.pixels.length];
		int x = generationCount < (pix.length / 6) ? 100 : (generationCount < 2000 ? generationCount : 2000);
		for ( int i = 0; i < x; i++ ) {
			ImageGen ig = new ImageGen(guess);
			ig.setPixel(rand.nextInt(target.imgSize), rand.nextInt(target.imgSize), rand.nextInt(256));
			if ( calcFitness(ig) > bestFitness ) {
				pix = ig.pixels;
				bestFitness = calcFitness(ig);
			}
		}
		if ( bestFitness != calcFitness(guess) ) {
			guess.setPixels(pix);
		}
	}

	public void newGenerationV4() throws SlickException {
		int bestFitness = calcFitness(guess);
		generationCount++;
		for ( int i = 0; i < guess.pixels.length; i++ ) {
			guess.setPixel(i, guess.pixels[i] + 1);
			if ( calcFitness(guess) < bestFitness ) {
				guess.setPixel(i, guess.pixels[i] - 1);
			} else {
				bestFitness = calcFitness(guess);
			}
		}
	}

	public int calcFitness(ImageGen img) {
		int fitness = 0;
		int combo = 0;

		for ( int i = 0; i < img.pixels.length; i++ ) {
			if ( img.pixels[i] == target.pixels[i] ) {
				combo++;
				fitness += 1 + combo;
			} else {
				fitness -= Math.abs(img.pixels[i] - target.pixels[i]);
				combo = 0;
			}
		}

		return fitness;
	}

	@Override
	public boolean closeRequested() {
		return false;
	}

	@Override
	public String getTitle() {
		return "Image Evolution";
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setClearEachFrame(true);
		target = new ImageGen(ResourceLoader.loadImage("wolf1"));
		targetFit = calcFitness(target);
		guess = new ImageGen(target.imgSize);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		Image i = target.getImage().getScaledCopy(200, 200);
		g.setAntiAlias(false);
		g.drawImage(i, 50, 50);
		g.setColor(Color.green);
		g.drawRect(49, 49, i.getWidth() + 2, i.getHeight() + 2);
		i = guess.getImage().getScaledCopy(200, 200);
		g.drawImage(i, 260, 50);
		g.setColor(Color.red);
		g.drawRect(259, 49, i.getWidth() + 2, i.getHeight() + 2);
		g.setColor(Color.black);
		g.fillRect(200, 300, 500, 50);
		g.setColor(Color.white);
		g.drawString("Time " + (System.currentTimeMillis() - start) / 1000 + " seconds", 200, 300);
		g.drawString(generationCount + " generations", 200, 330);

	}

	Random rand = new Random();

	long start = System.currentTimeMillis();

	int ticks;

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// target.setPixel(rand.nextInt(target.imgSize), rand.nextInt(target.imgSize),
		// rand.nextInt(256));
		// ticks++;
		if ( gc.getInput().isKeyDown(Input.KEY_SPACE) ) {
			start = System.currentTimeMillis();
			ticks++;
		}

		if ( ticks > 1 ) {
			newGeneration();
		}

		if ( pos == -1 ) {
			System.err.println("TIME = " + (System.currentTimeMillis() - start) + " ("
					+ (System.currentTimeMillis() - start) / 1000 + ")");
			System.exit(0);
		}
	}

}
