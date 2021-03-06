package net.mightyelemental.skype;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Parser {

	static String commandPrefix = "!";

	public static String[] getArgs(String command) {
		if (!command.startsWith(commandPrefix) && !command.startsWith("=")) { return null; }
		String[] t = command.split(" ");
		if (commandPrefix != "=") {
			t = command.replaceFirst(commandPrefix, "").split(" ");
		}
		return t;

	}

	public static void commands(String[] args, ChatMessage cm) throws SkypeException {
		if (args == null) { return; }
		if (args[0].equals("ls")) {
			Commands.ls(cm);
		} else if (args[0].equals("add")) {
			Commands.add(cm, args);
		} else if (args[0].equals("time")) {
			Commands.time(cm);
		} else if (args[0].equals("spamme")) {
			Commands.spam(cm);
		} else if (args[0].equals("wave")) {
			Commands.wave(cm);
		} else if (args[0].equals("reverse")) {
			Commands.reverse(cm);
		} else if (args[0].startsWith("=")) {
			Commands.doMaths(cm);
		} else if (args[0].equals("angry")) {
			Commands.angry(cm);
		}
	}

}
