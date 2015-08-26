package net.minegeek360.multisplit;

public class Exceptions {

	public static void varCreationException(String message) {
		exception("VariableCreation", message, true);
	}

	public static void varNameTooLong() {
		exception("VarNameOverLimit", "Your variable name is too long! Make sure it is " + MSLexer.VARNAME_LIMIT + " or less!", true);
	}

	public static void varExistsException() {
		exception("AdvancedVariableAlreadyExists", "Advanced Variable already exists! Cannot overwrite!", false);
	}

	public static void varWrongType(String message) {
		exception("WrongVariableType", message, false);
	}

	public static void varDoesNotExist() {
		exception("VarDoesNotExist", "Make sure you have spelt the variable correctly with the correct case and with a '@' at the start."
				+ "\n\tAlso make sure that you have defined the variable before calling it.", true);
	}

	public static void multiCommandLine() {
		exception("MultipleCommandsOnSingleLine",
				"This is a very bad practice because goto() will not be acurate, nor will if(). You have been warned!", false);
	}

	public static void logicFunction() {
		exception("InvalidLogicFunction", "Only use: == < > !=", true);
	}

	public static void genException(String message) {
		exception("General", message, true);
	}

	public static void functionNotRecognised() {
		exception("FunctionNotRecognised", "Make sure you have spelt the function correctly with the correct case.", true);
	}

	public static void wrongArgs(String message) {
		exception("WrongArguments", message, true);
	}

	public static void invalidNumber(String message) {
		exception("InvalidNumberFormat", message, true);
	}

	public static void intOutOfBounds(String message) {
		exception("IntegerOutOfBounds", message, true);
	}

	public static void expectedSymbol(String message) {
		exception("ExpectedSymbol", message, true);
	}

	public static void noInitGUI() {
		exception("GUIHasNotBeenInitialized", "Make sure you have initialized the GUI!", true);
	}

	private static void exception(String type, String message, boolean stop) {
		try {
			System.err.println(" " + type + "Exception on line " + (MSLexer.currentLine + 1) + ": \n\t"
					+ MSLexer.scriptLines.get(MSLexer.currentLine).toString() + "\n\t" + message);
		} catch (Exception e) {
			System.err.println(" " + type + "Exception on line " + (MSLexer.currentLine + 1) + ": \n\t" + message);
		}
		if (stop) {
			System.exit(0);
		}
	}

}
