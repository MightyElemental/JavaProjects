package net.iridgames.towerdefense.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.util.Log;

public class Level {

	public List<List<Character>>	levelLayout		= new ArrayList<List<Character>>();	// y, x
	public List<Point>				spawningPoints	= new ArrayList<Point>();

	private String	levelFile	= "";
	private int		levelNum	= 0;

	public int width, height;

	public Level(String level, int num) {
		levelFile = level;
		levelNum = num;
		// generateBlankLevel(25, 15);
		try {
			loadLevelFromFile();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		setSpawnPoints();
	}

	public void saveLevelToFile() throws IOException {
		String loc = "assets/levels/" + levelFile + "_" + findNextAvaliabeMapNumber("") + ".map";
		FileWriter fw = new FileWriter(loc);
		Log.info("Saving map to " + loc);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(levelLayout.get(0).size() + "\n");// x
		bw.write(levelLayout.size() + "\n");// y
		for ( int y = 0; y < levelLayout.size(); y++ ) {
			for ( int x = 0; x < levelLayout.get(y).size(); x++ ) {
				bw.write(levelLayout.get(y).get(x));
			}
			bw.write("\n");
		}
		bw.close();
	}

	public void clear() {
		for ( int y = 0; y < levelLayout.size(); y++ ) {
			for ( int x = 0; x < levelLayout.get(y).size(); x++ ) {
				setTile(x, y, 'x');
			}
		}
	}

	/** Returns a list of all path tiles in the map */
	public List<Point> getPathTiles() {
		List<Point> lp = new ArrayList<Point>();
		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				if ( getTile(x, y) == '-' ) {
					lp.add(new Point(x, y));
				}
			}
		}
		return lp;
	}

	private int findNextAvaliabeMapNumber(String suffix) {
		int i = 0;
		while (new File("assets/levels/" + levelFile + "_" + i + suffix + ".map").exists()) {
			i++;
		}
		return i;
	}

	public void generateBlankLevel(int width, int height) {
		levelLayout.clear();
		this.width = width;
		this.height = height;
		for ( int y = 0; y < height; y++ ) {
			List<Character> cl = new ArrayList<Character>();
			for ( int x = 0; x < width; x++ ) {
				cl.add('x');
			}
			levelLayout.add(cl);
		}
	}

	private boolean loadLevelFromFile() throws IOException, URISyntaxException {// TODO Add custom sizes
		if ( !(new File("assets/levels/" + levelFile + "_" + levelNum + ".map").exists()) ) return false;

		FileReader fr = new FileReader("assets/levels/" + levelFile + "_" + levelNum + ".map");
		if ( !fr.ready() ) {
			fr.close();
			return false;
		}
		BufferedReader br = new BufferedReader(fr);

		String line = "";
		width = Integer.parseInt(br.readLine());
		height = Integer.parseInt(br.readLine());
		while (((line = br.readLine()) != null) && levelLayout.size() <= height) {
			line = line.substring(0, width);
			List<Character> l = new ArrayList<Character>();
			char[] c = line.toCharArray();
			for ( int i = 0; i < line.length(); i++ ) {
				l.add(c[i]);
			}
			levelLayout.add(l);
		}
		br.close();
		return true;
	}

	public char getTile(int x, int y) {
		if ( y > levelLayout.size() ) return '-';
		if ( x > levelLayout.get(y).size() ) return '-';
		return levelLayout.get(y).get(x);
	}

	public Point getGoal() {
		for ( int y = 0; y < levelLayout.size(); y++ ) {
			for ( int x = 0; x < levelLayout.get(y).size(); x++ ) {
				if ( getTile(x, y) == 'g' ) { return new Point(x, y); }
			}
		}
		return null;
	}

	public void setTile(int x, int y, char c) {
		try {
			levelLayout.get(y).set(x, c);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Out of bounds");
		}
	}

	public void setSpawnPoints() {
		for ( int y = 0; y < levelLayout.size(); y++ ) {
			for ( int x = 0; x < levelLayout.get(y).size(); x++ ) {
				if ( getTile(x, y) == 's' ) {
					spawningPoints.add(new Point(x, y));
				}
			}
		}
	}

	public char getTile(float f, float g) {
		return getTile((int) f, (int) g);
	}
}
