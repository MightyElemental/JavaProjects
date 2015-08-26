package net.minegeek360.multisplit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.minegeek360.multisplit.graphics.GamePanel;

public class MSLexer {

	public static String	string			= "";
	public static int		currentLine		= 0;
	public static int		returnToLine	= -1;

	public static JFrame	frame;
	public static JPanel	defaultPanel	= new JPanel();
	public static GamePanel	gamePanel		= new GamePanel();

	public static ScriptEngineManager	manager	= new ScriptEngineManager();
	public static ScriptEngine			engine	= manager.getEngineByName("js");

	public static final int VARNAME_LIMIT = (int) Math.pow(2, 7);

	public static ArrayList<ArrayList<String>> scriptLines = new ArrayList<ArrayList<String>>();

	/** Give it a var name and it will return a type and value of the var */
	public static HashMap<String, Object[]> vars = new HashMap<String, Object[]>();

	/** Splits the lines and args up and compiles it into an ArrayList<ArrayList<String>> */
	public static ArrayList<ArrayList<String>> interpret(ArrayList<String> script) {
		ArrayList<ArrayList<String>> commands = new ArrayList<ArrayList<String>>();

		for (String line : script) {
			if (line.length() < 1) {
				line = "<COM>";
			}
			StringBuilder sb = new StringBuilder(line);
			while (sb.toString().startsWith(" ")) {
				sb.deleteCharAt(0);
			}
			line = sb.toString();

			if (!line.endsWith(";") && !line.startsWith("}") && !line.endsWith("{") && !line.startsWith("<COM>")) {
				Exceptions.expectedSymbol("';' expected at line " + (currentLine + 1) + " char " + line.length());
			}

			String[] coms = line.split(";");
			for (String tempCom : coms) {
				String[] commAndArgs = tempCom.split(" ");
				ArrayList<String> args = new ArrayList<String>();
				Collections.addAll(args, commAndArgs);
				commands.add(args);
			}
			if (coms.length > 1) {
				Exceptions.multiCommandLine();
			}
			currentLine++;
		}

		// for (ArrayList<String> temp : commands) {
		// System.out.println(temp);
		// }
		currentLine = 0;
		return commands;
	}

	private static boolean skipLine = false;

	private static boolean skipLine(ArrayList<String> arr) {
		if (arr.get(0).equals("}")) {
			skipLine = false;
		}
		try {
			ArrayList<String> lastLine = MSLexer.scriptLines.get(MSLexer.currentLine - 1);
			if (lastLine.get(lastLine.size() - 1).equals("{") && returnToLine < 0) {
				skipLine = true;
			}
		} catch (Exception e) {
		}

		return skipLine;
	}

	/** This goes through every line (unless something in the code tells it to loop, skip ect) and runs the functions */
	public static void handleTokens(ArrayList<ArrayList<String>> args) {
		if (args == null) { return; }
		if (args.size() <= 0) { return; }
		if (args.get(0).size() <= 0) { return; }
		scriptLines = args;

		forLoop: for (currentLine = 0; currentLine < args.size(); currentLine++) {
			ArrayList<String> arr = args.get(currentLine);
			if (skipLine(arr)) {
				continue forLoop;
			}
			if (arr.get(0).startsWith("<COM>")) {
				continue forLoop;
			}
			if (!arr.get(0).endsWith("()") && !arr.get(0).endsWith("}") && !arr.get(0).isEmpty()) {
				Exceptions.expectedSymbol("Brackets expected at end of function.");
			}
			switch (arr.get(0)) {
				case "addString()":
					Commands.addString(arr);
					break;
				case "print()":
					Commands.print(false);
					break;
				case "println()":
					Commands.print(true);
					break;
				case "clearString()":
					Commands.clearString();
					break;
				case "setVar()":
					Commands.setVar(arr);
					break;
				case "getInput()":
					Commands.getInput(arr);
					break;
				case "goto()":
					Commands.gotoLine(arr);
					break;
				case "sleep()":
					Commands.sleep(arr);
					break;
				case "createGUI()":
					Commands.createGUI(arr);
					break;
				case "halt()":
					Commands.exit();
					break;
				case "if()":
					Commands.ifStatement(arr);
					break;
				case "popup()":
					Commands.popup(arr);
					break;
				case "exec()":
					Commands.executeFunction(arr);
					break;
				case "}":
					if (returnToLine > 0) {
						currentLine = returnToLine;
					}
					returnToLine = -1;
					break;
				case "showGUI()":
					Commands.showGUI();
					break;
				case "hideGUI()":
					Commands.hideGUI();
					break;
				case "initGUI()":
					Commands.initGUI(arr);
					break;
				case "setGUIMode()":
					Commands.setGUIMode(arr);
					break;
				case "drawGUI()":
					Commands.drawGUI();
					break;
				case "drawRect()":
					Commands.drawRect(arr);
					break;
				default:
					Exceptions.functionNotRecognised();
					break;
			}
		}

	}

}
