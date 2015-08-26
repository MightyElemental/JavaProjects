package net.minegeek360.multisplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
			Object[] temp = wordCases(arr, i);
			String word = temp[0].toString();
			i = Integer.parseInt(temp[1] + "");
			sb.append(word + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		MSLexer.string = sb.toString();
	}

	public static void print(boolean newLine) {
		if (newLine) {
			System.out.println(MSLexer.string);
		} else {
			System.out.print(MSLexer.string);
		}
	}

	public static void clearString() {
		MSLexer.string = "";
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
		if (MSLexer.vars.get(var) == null) {
			Exceptions.varDoesNotExist();
		}
		return MSLexer.vars.get(var)[1];
	}

	public static String getVarType(String var) {
		if (MSLexer.vars.get(var) == null) {
			Exceptions.varDoesNotExist();
		}
		return MSLexer.vars.get(var)[0].toString();
	}

	private static boolean varExists(String var) {
		return MSLexer.vars.get(var) != null;
	}

	private static Object[] wordCases(ArrayList<String> arr, int i) {
		String word = arr.get(i);
		if (word.startsWith("@")) {
			word = getVarValue(word) + "";
		}
		if (word.contains("+")) {
			word = evalMaths(word) + "";
		}
		switch (word) {
			case "<read>":
				word = getConsoleInput();
				break;
			case "getVar()":
				word = getVarValue(arr.get(i + 1)) + "";
				i++;
				break;
		}
		return new Object[] { word, i };
	}

	/** getInput() - getInput() @[varName] [message text]; */
	public static void getInput(ArrayList<String> args) {
		if (args.size() < 3) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: getInput() @[varName] [message text];");
		}
		String message = "";
		StringBuilder sb = new StringBuilder(message);
		for (int i = 2; i < args.size(); i++) {
			sb.append(wordCases(args, i)[0] + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("> ");
		message = sb.toString();
		System.out.print(message);
		ArrayList<String> var = new ArrayList<String>();
		var.add("setVar()");
		var.add(args.get(1));
		var.add("string");
		var.add("<read>");
		setVar(var);
	}

	/** setVar() - setVar() @[varName] [type] [value]; <br>
	 * Types include:<br>
	 * gui<br>
	 * number<br>
	 * string<br>
	*/
	public static void setVar(ArrayList<String> arr) {
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
		switch (arr.get(2)) {
			case "string":
				StringBuilder sb = new StringBuilder();
				for (int i = 3; i < arr.size(); i++) {
					Object[] temp = wordCases(arr, i);
					String word = temp[0].toString();
					i = Integer.parseInt(temp[1] + "");
					sb.append(word + " ");
				}
				sb.deleteCharAt(sb.length() - 1);
				value = sb.toString();
				break;
			case "number":
				value = wordCases(arr, 3)[0].toString().replaceAll("[^0-9.]", "");
				break;
			case "function":
				if (!arr.get(3).equals("{")) {
					Exceptions.expectedSymbol("'{' expected!");
				}
				value = defineFunction(arr);
				break;
			default:
				value = arr.get(3);
				break;
		}

		Object[] args = new Object[] { arr.get(2), value };

		MSLexer.vars.put(arr.get(1), args);

	}

	private static Object defineFunction(ArrayList<String> args) {
		int[] startEnd = new int[2];

		startEnd[0] = MSLexer.currentLine + 2;

		for (int i = MSLexer.currentLine + 1; i < MSLexer.scriptLines.size(); i++) {
			if (MSLexer.scriptLines.get(i).get(0).startsWith("}")) {
				break;
			}
			MSLexer.currentLine++;
		}
		startEnd[1] = MSLexer.currentLine - 1;

		return startEnd;
	}

	/** goto() - goto() [integer number]; */
	public static void gotoLine(ArrayList<String> args) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: goto() [integer];");
		}
		int tempNum = 0;
		try {
			tempNum = Integer.parseInt(args.get(1));
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer.\n\tUsage: goto() [integer];");
		}
		if (tempNum > MSLexer.scriptLines.size()) {
			Exceptions.intOutOfBounds("Cannot go to a line that does not exist!");
		}
		MSLexer.currentLine = tempNum - 2;
	}

	public static void sleep(ArrayList<String> args) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: gotoLine() [integer];");
		}
		int tempNum = 0;
		try {
			tempNum = Integer.parseInt(args.get(1));
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer.\n\tUsage: gotoLine() [integer];");
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
			Object[] temp = wordCases(args, i);
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
		MSLexer.vars.put(args.get(1), value);
	}

	public static void exit() {
		System.exit(0);
	}

	/** if() @[varName] [== > < !=] @[varName] [lineIfFalse]; */
	public static void ifStatement(ArrayList<String> args) {
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
					gotoLine(gotoArgs);
				}
				break;
			case "!=":
				if (var1.equals(var2)) {
					gotoLine(gotoArgs);
				}
				break;
			case ">":
				if (getVarType(args.get(1)).equals("number") && getVarType(args.get(3)).equals("number")) {
					try {
						double temp1 = Double.parseDouble(var1);
						double temp2 = Double.parseDouble(var2);
						if (!(temp1 > temp2)) {
							gotoLine(gotoArgs);
						}
					} catch (Exception e) {
						Exceptions.invalidNumber(
								"Make sure you have written the number correctly. It can either be a decimal or an integer.");
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
							gotoLine(gotoArgs);
						}
					} catch (Exception e) {
						Exceptions.invalidNumber(
								"Make sure you have written the number correctly. It can either be a decimal or an integer.");
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
			message.append(wordCases(args, i)[0] + " ");
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
	public static void executeFunction(ArrayList<String> args) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: exec() @[funcName];");
		}
		if (!getVarType(args.get(1)).equals("function")) {
			Exceptions.varWrongType("Expected 'function' variable, got " + getVarType(args.get(1)) + "!");
		}
		int[] startEnd = (int[]) getVarValue(args.get(1));
		MSLexer.returnToLine = MSLexer.currentLine;
		ArrayList<String> gotoCom = new ArrayList<String>();
		gotoCom.add("goto()");
		gotoCom.add(startEnd[0] + "");
		gotoLine(gotoCom);
	}

	private static float evalMaths(String string) {
		float temp = 0f;
		try {
			temp = Float.parseFloat(MSLexer.engine.eval(string) + "");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/* GUI */

	/** initGUI() [width] [height] [title]; */
	public static void initGUI(ArrayList<String> args) {
		if (args.size() < 4) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: initGUI() [width] [height] [title];");
		}
		if (MSLexer.frame == null) {
			MSLexer.frame = new JFrame();
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
			Object[] temp = wordCases(args, i);
			String word = temp[0].toString();
			i = Integer.parseInt(temp[1] + "");
			sb.append(word + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		String title = sb.toString();
		MSLexer.frame.setSize(width, height);
		MSLexer.frame.setTitle(title);
		MSLexer.frame.setLocationRelativeTo(null);
		MSLexer.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/** showGUI(); */
	public static void showGUI() {
		if (MSLexer.frame == null) {
			Exceptions.noInitGUI();
		}
		MSLexer.frame.setVisible(true);
	}

	/** hideGUI(); */
	public static void hideGUI() {
		if (MSLexer.frame == null) {
			Exceptions.noInitGUI();
		}
		MSLexer.frame.setVisible(false);
	}

	/** setGUIMode() [guiMode]; - includes game/default */
	public static void setGUIMode(ArrayList<String> args) {
		if (args.size() < 2) {
			Exceptions.wrongArgs("There are too few arguments!\n\tUsage: setGUIMode() [guiMode];");
		}
		switch (args.get(1)) {
			case "default":
				MSLexer.frame.remove(MSLexer.gamePanel);
				MSLexer.frame.add(MSLexer.defaultPanel);
				break;
			case "game":
				MSLexer.frame.remove(MSLexer.defaultPanel);
				MSLexer.frame.add(MSLexer.gamePanel);
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
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		try {
			x = Integer.parseInt(args.get(1));
			y = Integer.parseInt(args.get(2));
			width = Integer.parseInt(args.get(3));
			height = Integer.parseInt(args.get(4));
		} catch (Exception e) {
			Exceptions.wrongArgs("Can only accept an integer.\n\tUsage: drawRect() [x] [y] [width] [height];");
		}
		MSLexer.gamePanel.addRect(x, y, width, height);
	}

	/** drawGUI(); */
	public static void drawGUI() {
		ArrayList<String> execFunc = new ArrayList<String>();
		execFunc.add("execFunc()");
		execFunc.add("@draw");
		executeFunction(execFunc);
		MSLexer.gamePanel.repaint();
		MSLexer.frame.repaint();
		MSLexer.defaultPanel.repaint();
		MSLexer.gamePanel.objects.clear();
	}

}
