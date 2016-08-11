package net.minegeek360.multisplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Commands {
	
	
	/** Usage of commands:<br>
	 * addString() - addString() [text];<br>
	 * print() - print();<br>
	 * println() - println();<br>
	 * clearString() - clearString();<br>
	 * setVar() - setVar() @[varName] [type] [value];<br>
	 * getVar() - getVar() @[varName];<br>
	 * getInput() - getInput() @[varName] [message text];<br>
	 * goto() - goto() [integer];<br>
	 * sleep() - sleep() [integer];<br>
	 * createGUI() - createGUI() @[frameVarName] [width] [height] [title];<br>
	 * if() - if() @[varName] [== <\ > !=] @[varName] [lineIfFalse]; <br>
	 * popup() - popup() [warning / info] [message];<br>
	 * showGUI() - showGUI();<br>
	 * hideGUI() - hideGUI();<br>
	 * drawGUI() - drawGUI(); - Requires a @draw function variable;<br>
	 * exec() - exec() @[funcName];<br>
	 * CREATE A NEW FUNCTION - setVar() @[funcName] function { [multiLineCode] }<br>
	 * initGUI() - initGUI() [width] [height] [title];<br>
	 * setGUIMode() - setGUIMode() [guiMode] - includes game/default<br>
	 * drawRect() - drawRect() [x] [y] [width] [height];<br>
	 * addScript() - addScript() [scriptName];<br>
	 * getString() - getString();<br>
	 * halt() - halt();<br>
	 * break() - break();<br>
	 * <\COM> - Comment (Without the backslash)<br>
	*/
	private Commands() {
	}
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	/** addString() - addString() [text]; */
	public static void addString(ArrayList<String> arr) {
		if (arr.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: addString() [text];");
		}
		StringBuilder sb = new StringBuilder(MSLexer.string);
		for (int i = 1; i < arr.size(); i++) {
			Object[] temp = wordCases(arr, i, true);
			String word = temp[0].toString();
			i = Integer.parseInt(temp[1] + "");
			sb.append(word + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		MSLexer.string = sb.toString();
	}
	
	public static void clearString() {
		MSLexer.string = "";
	}
	
	public static String getString() {
		return MSLexer.string;
	}
	
	public static void print(boolean newLine) {
		if (newLine) {
			System.out.println(MSLexer.string);
		} else {
			System.out.print(MSLexer.string);
		}
	}
	
	private static String getConsoleInput() {
		try {
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "INPUT ERROR";
	}
	
	public static Object getVarValue(String var) {
		if (MultiSplit.vars.get(var) == null) {
			Exceptions.varDoesNotExist(var);
		}
		return MultiSplit.vars.get(var)[1];
	}
	
	public static HashMap<String, Object[]> getAllVars() {
		return MultiSplit.vars;
	}
	
	public static String getVarType(String var) {
		if (MultiSplit.vars.get(var) == null) {
			Exceptions.varDoesNotExist(var);
		}
		return MultiSplit.vars.get(var)[0].toString();
	}
	
	/** setVar() - setVar() @[varName] [type] [value]; <br>
	 * Types include:<br>
	 * function<br>
	 * number<br>
	 * string<br>
	*/
	@SuppressWarnings( "unchecked" )
	public static void setVar(ArrayList<String> arr, MSLexer lex) {
		if (arr.size() < 4) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: setVar() @[varName] [type] [value];");
		}
		Object value = arr.get(3);
		if (!arr.get(1).startsWith("@")) {
			Exceptions.varCreationException("Variables MUST start with '@'");
		}
		if (arr.get(1).length() - 1 > MSLexer.VARNAME_LIMIT) {
			Exceptions.varNameTooLong();
		}
		String type = arr.get(2);
		switch (arr.get(2)) {
			case "string":
				value = getString(arr, 3);
				break;
			case "number":
				value = wordCases(arr, 3, true)[0].toString().replaceAll("[^0-9.-]", "");
				break;
			case "function":
				if (!arr.get(3).equals("{")) {
					Exceptions.expectedSymbol("'{' expected!");
				}
				value = defineFunction(arr, lex);
				break;
			case "random":
				if (arr.size() < 5) {
					Exceptions.wrongArgs("There are too few arguments!\n\tUsage: setVar() @[varName] random [min] [max];");
				}
				value = getRandom(arr, lex);
				type = "number";
				break;
			case "array":
				if (arr.size() < 5) {
					Exceptions.wrongArgs("There are too few arguments!\n\tUsage: setVar() @[varName] array [index] [value];");
				}
				if (!varExists(arr.get(1))) {
					value = new HashMap<Integer, Object>();
				} else {
					value = getVarValue(arr.get(1));
				}
				// System.err.println(value);
				((HashMap<Integer, Object>) value).put((int) Float.parseFloat(wordCases(arr, 3, true)[0] + ""), getString(arr, 4));
				break;
			default:
				Exceptions.unknownVarType(arr.get(2));
				// value = arr.get(3);
				break;
		}
		
		Object[] args = new Object[] { type, value };
		
		MultiSplit.vars.put(arr.get(1), args);
		
	}
	
	public static boolean varExists(String var) {
		return MultiSplit.vars.get(var) != null;
	}
	
	public static String getString(ArrayList<String> arr, int start) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < arr.size(); i++) {
			Object[] temp = wordCases(arr, i, false);
			String word = temp[0].toString();
			i = Integer.parseInt(temp[1] + "");
			sb.append(word + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	private static float getRandom(ArrayList<String> arr, MSLexer lex) {
		float r = 0;
		try {
			int min = (int) Float.parseFloat(wordCases(arr, 3, true)[0] + "");
			int max = (int) Float.parseFloat(wordCases(arr, 4, true)[0] + "");
			r = MultiSplit.rand.nextInt(max - min) + min;
		} catch (Exception e) {
			Exceptions.invalidNumber("Only numbers can be used!");
		}
		return r;
	}
	
	private static long getTime() {
		return System.currentTimeMillis();
	}
	
	@SuppressWarnings( "unchecked" )
	private static Object[] wordCases(ArrayList<String> arr, int i, boolean useMaths) {
		String word = arr.get(i);
		if (useMaths && (word.contains("+") || word.contains("-") || word.contains("/") || word.contains("*"))) {
			word = evalMaths(word) + "";
		} else if (word.startsWith("@")) {
			if (word.contains("[") && word.contains("]")) {
				if (varExists(word)) {
					if (!getVarType(word).equals("array")) {
						Exceptions.varWrongType("only arrays can contain square brackets!");
					}
				}
				String[] split = word.split("\\[");
				split[1] = split[1].replace("[", "").replace("]", "");
				ArrayList<String> a = new ArrayList<String>();
				a.add(split[1]);
				word = "" + ((HashMap<Integer, Object>) getVarValue(split[0])).get((int) Float.parseFloat(wordCases(a, 0, true)[0] + ""));
			} else {
				word = getVarValue(word) + "";
			}
		}
		switch (word) {
			case "<read>":
				word = getConsoleInput();
				break;
			case "getVar()":
				word = getVarValue(arr.get(i + 1)) + "";
				i++;
				break;
			case "getString()":
				word = getString();
				break;
			case "getTime()":
				word = getTime() + "";
				break;
		}
		try {
			int val = (int) Float.parseFloat(word);
			word = val + "";
		} catch (Exception e) {
		}
		return new Object[] { word, i };
	}
	
	/** getInput() - getInput() @[varName] [message text]; */
	public static void getInput(ArrayList<String> args, MSLexer lex) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: getInput() @[varName] [message text];");
		}
		String message = "";
		StringBuilder sb = new StringBuilder(message);
		if (args.size() > 2) {
			for (int i = 2; i < args.size(); i++) {
				sb.append(wordCases(args, i, false)[0] + " ");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("> ");
		message = sb.toString();
		System.out.print(message);
		ArrayList<String> var = new ArrayList<String>();
		var.add("setVar()");
		var.add(args.get(1));
		var.add("string");
		var.add("<read>");
		setVar(var, lex);
	}
	
	/** split() - split() [varToSaveTo] [stringVar] [regex] */
	public static void split(ArrayList<String> args, MSLexer lex) {
		if (args.size() < 3) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: getInput() @[varName] [message text];");
		}
	}
	
	private static List<Object> defineFunction(ArrayList<String> args, MSLexer lex) { // NEED TO ADD ABILITY TO GO TO
																						// DIFFERENT SCRIPTS
		List<Object> info = new ArrayList<Object>();
		
		info.add(lex.currentLine + 2);
		
		for (int i = lex.currentLine + 1; i < lex.scriptLines.size(); i++) {
			if (lex.scriptLines.get(i).get(0).startsWith("}")) {
				break;
			}
			lex.currentLine++;
		}
		info.add(lex.currentLine - 1);
		info.add(lex.scriptName);
		
		return info;
	}
	
	/** goto() - goto() [integer]:[scriptName]; */
	public static void gotoLine(ArrayList<String> args, MSLexer lex) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: goto() [integer]:[scriptName];");
		}
		int tempNum = 0;
		Object[] numScript = new Object[2];
		try {
			numScript = args.get(1).split(":");
			tempNum = Integer.parseInt(numScript[0] + "");
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer.\n\tUsage: goto() [integer]:[scriptName];");
		}
		if (numScript.length > 1) {
			try {
				lex = MultiSplit.lexers.get(numScript[1] + "");
				if (lex == null) { throw new Exception(); }
				tempNum++;
			} catch (Exception e) {
				Exceptions.scriptDoesNotExist();
			}
		}
		if (tempNum > lex.scriptLines.size()) {
			Exceptions.intOutOfBounds("Cannot go to a line that does not exist!");
		}
		lex.currentLine = tempNum - 2;
		try {
			if (numScript.length > 1) {
				lex.handleTokens(lex.scriptLines);
			}
		} catch (StackOverflowError e) {
			Exceptions.stackOverflow();
		}
	}
	
	/** sleep() - sleep() [integer] */
	public static void sleep(ArrayList<String> args) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: sleep() [integer];");
		}
		int tempNum = 0;
		try {
			tempNum = Integer.parseInt(args.get(1));
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer.\n\tUsage: sleep() [integer];");
		}
		try {
			Thread.sleep(tempNum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** createGUI() @[frameVarName] [width] [height] [title]; */
	@Deprecated
	public static void createGUI(ArrayList<String> args) {
		if (args.size() < 5) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: createGUI() @[frameVarName] [width] [height] [title];");
		}
		if (!args.get(1).startsWith("@")) {
			Exceptions.varCreationException("Variables MUST start with '@'");
		}
		if (varExists(args.get(1))) {
			Exceptions.varExistsException();
			return;
		}
		int width = 0;
		int height = 0;
		try {
			width = Integer.parseInt(args.get(2));
			height = Integer.parseInt(args.get(3));
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer.\n\tUsage: createGUI() @[frameVarName] [width] [height] [title];");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 4; i < args.size(); i++) {
			Object[] temp = wordCases(args, i, false);
			String word = temp[0].toString();
			i = Integer.parseInt(temp[1] + "");
			sb.append(word + " ");
		}
		String title = sb.toString();
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus();
		Object[] value = new Object[] { "gui", frame };
		MultiSplit.vars.put(args.get(1), value);
	}
	
	public static void halt() {
		System.exit(0);
	}
	
	/** if() @[varName] [== > < !=] @[varName] [lineIfFalse]; */
	public static void ifStatement(ArrayList<String> args, MSLexer lex) {
		if (args.size() < 5) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: if() @[varName] [== > < !=] @[varName] [lineIfTrue];");
		}
		if (!args.get(1).startsWith("@") || !args.get(3).startsWith("@")) {
			Exceptions.varCreationException("Variables MUST start with '@'");
		}
		ArrayList<String> gotoArgs = new ArrayList<String>();
		gotoArgs.add("goto()");
		gotoArgs.add(args.get(4));
		String var1 = getVarValue(args.get(1) + "") + "";
		String var2 = getVarValue(args.get(3) + "") + "";
		switch (args.get(2)) {
			case "==":
				if (!var1.equals(var2)) {
					gotoLine(gotoArgs, lex);
				}
				break;
			case "!=":
				if (var1.equals(var2)) {
					gotoLine(gotoArgs, lex);
				}
				break;
			case ">":
				if (getVarType(args.get(1)).equals("number") && getVarType(args.get(3)).equals("number")) {
					try {
						double temp1 = Double.parseDouble(var1);
						double temp2 = Double.parseDouble(var2);
						if (!(temp1 > temp2)) {
							gotoLine(gotoArgs, lex);
						}
					} catch (Exception e) {
						Exceptions
							.invalidNumber("Make sure you have written the number correctly. It can either be a decimal or an integer.");
					}
				} else {
					Exceptions.wrongArgs("You cannot use a non-number argument to test greater than with!");
				}
				break;
			case "<":
				if (getVarType(args.get(1)).equals("number") && getVarType(args.get(3)).equals("number")) {
					try {
						double temp1 = Double.parseDouble(var1);
						double temp2 = Double.parseDouble(var2);
						if (!(temp1 < temp2)) {
							gotoLine(gotoArgs, lex);
						}
					} catch (Exception e) {
						Exceptions
							.invalidNumber("Make sure you have written the number correctly. It can either be a decimal or an integer.");
					}
				} else {
					Exceptions.wrongArgs("You cannot use a non-number argument to test less than with!");
				}
				break;
			default:
				Exceptions.logicFunction();
				break;
		}
	}
	
	/** popup() - popup() [warning / info] [message]; */
	public static void popup(ArrayList<String> args) {
		if (args.size() < 3) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: popup() [warning / info] [message];");
		}
		StringBuilder message = new StringBuilder();
		for (int i = 2; i < args.size(); i++) {
			message.append(wordCases(args, i, false)[0] + " ");
		}
		message.deleteCharAt(message.length() - 1);
		switch (args.get(1)) {
			case "warning":
				JOptionPane.showMessageDialog(null, message.toString(), "WARNING!", JOptionPane.WARNING_MESSAGE);
				break;
			case "info":
				JOptionPane.showMessageDialog(null, message.toString(), "INFO", JOptionPane.INFORMATION_MESSAGE);
				break;
			default:
				Exceptions.wrongArgs("'warning' and 'info' are the only allowed arguments!\n\tUsage: popup() [warning / info] [message];");
				break;
		}
	}
	
	/** exec() @[funcName]; */
	public static void executeFunction(ArrayList<String> args, MSLexer lex) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: exec() @[funcName];");
		}
		if (!getVarType(args.get(1)).equals("function")) {
			Exceptions.varWrongType("Expected 'function' variable, got " + getVarType(args.get(1)) + " variable!");
		}
		try {
			@SuppressWarnings( "unchecked" )
			List<Object> info = (List<Object>) getVarValue(args.get(1));
			MultiSplit.returnToLine = lex.currentLine;
			MultiSplit.returnToScript = lex.scriptName;
			ArrayList<String> gotoCom = new ArrayList<String>();
			gotoCom.add("goto()");
			gotoCom.add(info.get(0) + ":" + info.get(2));
			// System.out.println(gotoCom);
			gotoLine(gotoCom, lex);
		} catch (Exception e) {
			e.printStackTrace();
			// Exceptions.varDoesNotExist(args.get(1));
		}
	}
	
	private static float evalMaths(String string) {
		float temp = 0f;
		Object[] keys = getAllVars().keySet().toArray();
		for (int i = 0; i < getAllVars().size(); i++) {
			if (string.contains(keys[i] + "")) {
				if (!getVarType(keys[i] + "").equals("number")) {
					Exceptions.varWrongType("A number needs to be used to perform maths!");
				}
				string = string.replace(keys[i] + "", getAllVars().get(keys[i])[1] + "");
			}
		}
		String old = string;
		// System.err.println("\t\t" + string);
		string = string.replaceAll("[^0-9.-/*+]", "");
		// System.err.println("\t\t" + string);
		if (!old.equals(string)) {
			Exceptions.expectedSymbol("Only numbers and math functions can be used!");
		}
		try {
			temp = Float.parseFloat(MSLexer.engine.eval(string) + "");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	/** addScript() [scriptName]; */
	public static void addScript(ArrayList<String> args, MSLexer lex) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: addScript() [scriptName];");
		}
		try {
			MultiSplit.loadScriptFileToString(args.get(1) + ".ms");
		} catch (IOException e) {
			Exceptions.scriptDoesNotExist();
		}
		MultiSplit.returnToScript = args.get(1);
		ArrayList<ArrayList<String>> script = MSLexer.interpret(MultiSplit.scripts.get(args.get(1)));
		MSLexer lexNew = new MSLexer(args.get(1));
		MultiSplit.regScript(lexNew);
		lexNew.handleTokens(script);
		MultiSplit.returnToScript = lex.scriptName;
	}
	
	/* GUI */
	
	/** initGUI() [width] [height] [title]; */
	public static void initGUI(ArrayList<String> args) {
		if (args.size() < 4) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: initGUI() [width] [height] [title];");
		}
		if (MultiSplit.frame == null) {
			MultiSplit.frame = new JFrame();
		}
		int width = 0;
		int height = 0;
		try {
			width = Integer.parseInt(args.get(1));
			height = Integer.parseInt(args.get(2));
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer.\n\tUsage: initGUI() [width] [height] [title];");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 3; i < args.size(); i++) {
			Object[] temp = wordCases(args, i, false);
			String word = temp[0].toString();
			i = Integer.parseInt(temp[1] + "");
			sb.append(word + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		String title = sb.toString();
		MultiSplit.frame.setSize(width, height);
		MultiSplit.frame.setTitle(title);
		MultiSplit.frame.setLocationRelativeTo(null);
		MultiSplit.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/** showGUI(); */
	public static void showGUI() {
		if (MultiSplit.frame == null) {
			Exceptions.noInitGUI();
		}
		MultiSplit.frame.setVisible(true);
	}
	
	/** hideGUI(); */
	public static void hideGUI() {
		if (MultiSplit.frame == null) {
			Exceptions.noInitGUI();
		}
		MultiSplit.frame.setVisible(false);
	}
	
	/** setGUIMode() [guiMode]; - includes game/default */
	public static void setGUIMode(ArrayList<String> args) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: setGUIMode() [guiMode];");
		}
		switch (args.get(1)) {
			case "default":
				MultiSplit.frame.remove(MultiSplit.gamePanel);
				MultiSplit.frame.add(MultiSplit.defaultPanel);
				break;
			case "game":
				try {
					MultiSplit.frame.remove(MultiSplit.defaultPanel);
				} catch (Exception e) {
				}
				MultiSplit.frame.add(MultiSplit.gamePanel);
				break;
			default:
				Exceptions.wrongArgs("You can only use'game' or 'default' mode!");
				break;
		}
	}
	
	/** drawRect() [x] [y] [width] [height]; */
	public static void drawRect(ArrayList<String> args) {
		if (args.size() < 5) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: drawRect() [x] [y] [width] [height];");
		}
		float x = 0;
		float y = 0;
		int width = 0;
		int height = 0;
		try {
			x = Float.parseFloat(wordCases(args, 1, true)[0] + "");
			y = Float.parseFloat(wordCases(args, 2, true)[0] + "");
			width = Integer.parseInt(args.get(3));
			height = Integer.parseInt(args.get(4));
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer for width and height.\n\tUsage: drawRect() [x] [y] [width] [height];");
		}
		MultiSplit.gamePanel.addRect((int) x, (int) y, width, height);
	}
	
	/** drawGUI(); */
	public static void drawGUI(MSLexer lex) {
		ArrayList<String> execFunc = new ArrayList<String>();
		execFunc.add("execFunc()");
		execFunc.add("@draw");
		MultiSplit.frame.repaint();
		MultiSplit.defaultPanel.repaint();
		MultiSplit.gamePanel.repaint();
		MultiSplit.gamePanel.objects.clear();
		executeFunction(execFunc, lex);
	}
	
}
