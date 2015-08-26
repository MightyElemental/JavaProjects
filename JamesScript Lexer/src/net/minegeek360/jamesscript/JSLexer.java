package net.minegeek360.jamesscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSLexer {

	public ArrayList<String> scripts = new ArrayList<String>();

	public JSLexer() {
		try {
			this.loadScriptFileToString(new File("scripts/main.jamscript"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> temp = Lexer.interprate(scripts.get(0));
		Lexer.handleTokens(temp);
	}

	public static void main(String[] args) {
		new JSLexer();
	}

	public void loadScriptFileToString(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			scripts.add(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

}
