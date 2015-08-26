package net.minegeek360.jamesscript;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Lexer {

	public static Pattern pattern = Pattern.compile(Pattern.quote("|@"));

	public static void handleTokens(ArrayList<String> tokens) {
		boolean print = false;
		for (String token : tokens) {
			if (deepEquals(token, "sysout")) {
				print = true;
			}
			if (token.startsWith("STRING") && print) {
				token = token.replaceFirst("STRING:", "");
				int temp = pattern.split(token).length - 1;
				System.out.print(token.replaceAll(pattern.pattern() + "#", ""));
				for (int i = 0; i < temp; i++) {
					System.out.println("");
				}
			}
		}
	}

	public static ArrayList<String> interprate(String script) {
		ArrayList<String> tokens = new ArrayList<String>();
		String buffer = "";

		String string = "";
		int state = 0;

		for (int i = 0; i < script.length(); i++) {
			buffer += script.charAt(i) + "";
			if (buffer.startsWith(System.lineSeparator())) {
				buffer = buffer.replaceFirst(System.lineSeparator(), "");
			}
			// System.out.println(buffer);

			if (deepEquals(buffer, "sysout")) {
				tokens.add("sysout");
				buffer = "";
			} else if (deepEquals(buffer, "\"")) {
				// System.out.println("TRUE");
				if (state == 0) {
					state = 1;
				} else if (state == 1) {
					tokens.add("STRING:" + string);
					string = "";
					state = 0;
				}
				buffer = "";
			} else if (state == 0 && deepEquals(buffer, " ")) {
				buffer = "";
			} else if (state == 1) {
				string += script.charAt(i);
				buffer = "";
			}
		}

		System.out.println(tokens);
		return tokens;
	}

	public static ArrayList<String> tokenize(String fContent) {
		ArrayList<String> tokens = new ArrayList<String>();
		String string = "";
		int state = 0;

		int i = 0;
		String token = "";

		for (int j = 0; j < fContent.length(); j++) {
			token += fContent.charAt(j);
			i = j;

			System.out.println(token);

			if (deepEquals(token, "PRINT")) {
				tokens.add("PRINT");
				token = "";
			} else if (deepEquals(token, " ")) {
				if (state == 0) {
					token = "";
				}
			} else if (deepEquals(token, "\"")) {
				if (state == 0) {
					state = 1;
				} else if (state == 1) {
					tokens.add("STRING:" + string);
					string = "";
					state = 0;
				}

				token = "";
			} else if (state == 1) {
				string += fContent.charAt(j);
				token = "";
			}
		}

		System.out.println(tokens);

		return null;
	}

	public static boolean deepEquals(String a, String b) {
		return (a.equalsIgnoreCase(b));
	}

}
