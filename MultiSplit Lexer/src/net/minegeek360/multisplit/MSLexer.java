package net.minegeek360.multisplit;

import java.util.ArrayList;
import java.util.Collections;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class MSLexer {
	
	
	public static String string = "";
	public int currentLine = 0;
	
	public String scriptName = "NULL";
	
	public static ScriptEngineManager manager = new ScriptEngineManager();
	public static ScriptEngine engine = manager.getEngineByName("js");
	
	public static final int VARNAME_LIMIT = (int) Math.pow(2, 7);
	
	public ArrayList<ArrayList<String>> scriptLines = new ArrayList<ArrayList<String>>();
	
	public MSLexer( String name ) {
		this.scriptName = name;
	}
	
	public MSLexer() {
		
	}
	
	/** Splits the lines and args up and compiles it into an ArrayList<ArrayList<String>> */
	public static ArrayList<ArrayList<String>> interpret(ArrayList<String> script) {
		ArrayList<ArrayList<String>> commands = new ArrayList<ArrayList<String>>();
		
		int currL = 0;
		
		for (String line : script) {
			if (line.length() < 1) {
				line = "<COM>";
			}
			StringBuilder sb = new StringBuilder(line);
			while (sb.toString().startsWith(" ")) {
				sb.deleteCharAt(0);
			}
			while (sb.toString().endsWith(" ")) {
				sb.deleteCharAt(sb.length() - 1);
			}
			line = sb.toString();
			
			if (!line.endsWith(";") && !line.startsWith("}") && !line.endsWith("{") && !line.startsWith("<COM>")) {
				Exceptions.expectedSymbol("';' expected at line " + (currL + 1) + " char " + line.length());
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
			currL++;
		}
		
		// for (ArrayList<String> temp : commands) {
		// System.out.println(temp);
		// }
		return commands;
	}
	
	private static boolean skipLine = false;
	
	private boolean skipLine(ArrayList<String> arr) {
		if (arr.get(0).equals("}")) {
			skipLine = false;
		}
		try {
			ArrayList<String> lastLine = scriptLines.get(currentLine - 1);
			if (lastLine.get(lastLine.size() - 1).equals("{") && MultiSplit.returnToLine < 0) {
				skipLine = true;
			}
		} catch (Exception e) {
		}
		
		return skipLine;
	}
	
	/** This goes through every line (unless something in the code tells it to loop, skip ect) and runs the functions */
	@SuppressWarnings( "unused" )
	public void handleTokens(ArrayList<ArrayList<String>> args) {
		if (args == null) { return; }
		if (args.size() <= 0) { return; }
		if (args.get(0).size() <= 0) { return; }
		scriptLines = args;
		
		forLoop: for (int i = 0; currentLine < args.size(); currentLine++) {
			ArrayList<String> arr = args.get(currentLine);
			MultiSplit.currentLineNum = currentLine;
			MultiSplit.currentScript = scriptName;
			MultiSplit.currentLine = arr.toString();
			if (skipLine(arr)) {
				continue forLoop;
			}
			if (arr.get(0).startsWith("<COM>")) {
				continue forLoop;
			}
			if (!arr.get(0).endsWith("()") && !arr.get(0).endsWith("}") && !arr.get(0).isEmpty()) {
				Exceptions.expectedSymbol("Brackets expected at end of function.");
			}
			//System.err.println(arr);
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
					Commands.setVar(arr, this);
					break;
				case "getInput()":
					Commands.getInput(arr, this);
					break;
				case "goto()":
					Commands.gotoLine(arr, this);
					break;
				case "sleep()":
					Commands.sleep(arr);
					break;
				case "createGUI()":
					Commands.createGUI(arr);
					Exceptions.deprecated("createGUI() is no longer supported and will not function properly");
					break;
				case "halt()":
					Commands.halt();
					break;
				case "if()":
					Commands.ifStatement(arr, this);
					break;
				case "popup()":
					Commands.popup(arr);
					break;
				case "exec()":
					Commands.executeFunction(arr, this);
					break;
				case "}":
					if (MultiSplit.returnToLine > 0) {
						currentLine = MultiSplit.returnToLine;
						if (!MultiSplit.returnToScript.equals(scriptName)) { return; }
					}
					MultiSplit.returnToLine = -1;
					break;
				case "break()":
					if (MultiSplit.returnToLine > 0) {
						currentLine = MultiSplit.returnToLine;
						if (!MultiSplit.returnToScript.equals(scriptName)) { return; }
					}
					MultiSplit.returnToLine = -1;
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
					Commands.drawGUI(this);
					break;
				case "drawRect()":
					Commands.drawRect(arr);
					break;
				case "addScript()":
					Commands.addScript(arr, this);
					break;
				default:
					Exceptions.functionNotRecognised();
					break;
			}
		}
		
	}
	
}
