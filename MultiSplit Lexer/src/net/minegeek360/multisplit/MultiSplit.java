package net.minegeek360.multisplit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.minegeek360.multisplit.graphics.GamePanel;

public class MultiSplit {
	
	
	public static Map<String, ArrayList<String>> scripts = new HashMap<String, ArrayList<String>>();
	public static Map<String, MSLexer> lexers = new HashMap<String, MSLexer>();
	
	/** Give it a var name and it will return a type and value of the var */
	public static HashMap<String, Object[]> vars = new HashMap<String, Object[]>();
	
	public static JFrame frame;
	public static JPanel defaultPanel = new JPanel();
	public static GamePanel gamePanel = new GamePanel();
	
	public static String currentLine;
	public static String currentScript;
	public static int currentLineNum;
	
	public static int returnToLine = -1;
	public static String returnToScript = "main";
	
	public MultiSplit() {
		try {
			loadScriptFileToString("main.ms");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<ArrayList<String>> temp = MSLexer.interpret(scripts.get("main"));
		// createNewInterprateThread(temp);
		MSLexer lex = new MSLexer("main");
		regScript(lex);
		lex.handleTokens(temp);
	}
	
	private static int scriptNum = -1;
	
	public static boolean pauseScript = false;
	
	public static void regScript(MSLexer lex) {
		lexers.put(lex.scriptName, lex);
	}
	
	@Deprecated
	public static synchronized void createNewInterprateThread(final ArrayList<ArrayList<String>> script) {
		Thread thread = new Thread() {
			
			
			public void run() {
				System.out.println("started " + this.getName());
				MSLexer lex = new MSLexer();
				lex.handleTokens(script);
				try {
					System.out.println("stopped " + this.getName());
					this.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		thread.setName("THREAD_" + scriptNum + " " + script.get(0).get(0));
		thread.start();
	}
	
	public MultiSplit( String file ) {
		try {
			loadScriptFileToString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<ArrayList<String>> temp = MSLexer.interpret(scripts.get("file"));
		MSLexer lex = new MSLexer();
		lex.handleTokens(temp);
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			System.out.println("Attempting to load " + args[0]);
			new MultiSplit(args[0]);
		} else {
			new MultiSplit();
		}
	}
	
	public static void loadScriptFileToString(String file) throws IOException {
		File file2 = new File("scripts/" + file);
		BufferedReader br = new BufferedReader(new FileReader(file2));
		ArrayList<String> lines = new ArrayList<String>();
		String fileName = file2.getName();
		try {
			String line = br.readLine();
			
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			scripts.put(fileName.replace(".ms", ""), lines);
			scriptNum++;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}
	
}
