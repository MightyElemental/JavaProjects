package net.mightyelemental.sul;

public class SULExceptions {

	private SULExceptions() {
	}

	private static void exception(String type, String message, String code) {
		System.err.println("\n"+type + "Exception\n\t" + message + "\n\t[" + code + "]");
	}
	
	public static void invalidSyntaxException(String message, String code) {
		exception("InvalidSyntax", message, code);
	}
	
	public static void varNotSetException(String code) {
		exception("VariableNotFound", "The following variable has not been assigned", code);
	}

}
