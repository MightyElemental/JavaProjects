package net.mightyelemental.sul;

import java.util.List;

public class SULExceptions {

	private SULExceptions() {
	}

	private static void exception(String type, String message, String code) {
		System.err.println("\n" + type + "Exception\n\t" + message + "\n\t[" + code + "]");
	}

	public static void invalidSyntaxException(String message, String code) {
		exception("InvalidSyntax", message, code);
	}

	public static void varNotSetException(String code) {
		exception("VariableNotFound", "The following variable has not been assigned", code);
	}

	public static void commandNotRecognised(String code) {
		exception("CommandNotRecognised", "The following variable has not been assigned", code);
	}

	public static void commandNotRecognised(List<String> line) {
		String code = "";

		for ( String x : line ) {
			code += x + " ";
		}

		commandNotRecognised(code);
	}

	public static void invalidSyntaxException(String message, List<String> line) {
		String code = "";

		for ( String x : line ) {
			code += x + " ";
		}
		invalidSyntaxException(message, code);
	}

}
