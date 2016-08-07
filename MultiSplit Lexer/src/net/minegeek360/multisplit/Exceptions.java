package net.minegeek360.multisplit;

public class Exceptions {
	
	
	public static void varCreationException(String message) {
		exception("VariableCreation", message, true);
	}
	
	/** Says:<br>
	 * Your variable name is too long! Make sure it is # or less! */
	public static void varNameTooLong() {
		exception("VarNameOverLimit", "Your variable name is too long! Make sure it is " + MSLexer.VARNAME_LIMIT + " or less!", true);
	}
	
	/** Says:<br>
	 * Advanced Variable already exists! Cannot overwrite! */
	public static void varExistsException() {
		exception("AdvancedVariableAlreadyExists", "Advanced Variable already exists! Cannot overwrite!", false);
	}
	
	public static void varWrongType(String message) {
		exception("WrongVariableType", message, false);
	}
	
	/** Says:<br>
	 * Make sure you have spelt the script correctly and WITHOUT '.ms' at the end. */
	public static void scriptDoesNotExist() {
		exception("ScriptDoesNotExist", "Make sure you have spelt the script correctly and WITHOUT '.ms' at the end."
			+ "\n\tMake sure that you have the file in the correct location."
			+ "\n\tAlso make sure that you have loaded the script into MultiSplit.", true);
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
	
	public static void deprecated(String string) {
		exception("RunningDeprecatedFuntion", string, false);
	}
	
	public static void stackOverflow() {
		exception("StackOverflow", "StackOverflow!", true);
	}
	
	private static void exception(String type, String message, boolean stop) {
		try {
			System.err.println(" " + type + "Exception in script '" + MultiSplit.currentScript + "' on line "
				+ (MultiSplit.currentLineNum + 1) + ": \n\t" + MultiSplit.currentLine + "\n\t" + message);
		} catch (Exception e) {
			System.err.println(" " + type + "Exception on line " + (MultiSplit.currentLineNum + 1) + ": \n\t" + message);
		}
		if (stop) {
			System.exit(0);
		}
	}
	
}
