package net.minegeek360.multisplit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MultiSplit {

	public ArrayList<ArrayList<String>> scripts = new ArrayList<ArrayList<String>>();

	public MultiSplit() {
		try {
			this.loadScriptFileToString(new File("scripts/main.ms"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<ArrayList<String>> temp = MSLexer.interpret(scripts.get(0));
		MSLexer.handleTokens(temp);
	}

	public MultiSplit( String file ) {
		try {
			this.loadScriptFileToString(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<ArrayList<String>> temp = MSLexer.interpret(scripts.get(0));
		MSLexer.handleTokens(temp);
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			System.out.println("Attempting to load " + args[0]);
			new MultiSplit(args[0]);
		} else {
			new MultiSplit();
		}
	}

	public void loadScriptFileToString(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList<String> lines = new ArrayList<String>();
		try {
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			scripts.add(lines);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

}
