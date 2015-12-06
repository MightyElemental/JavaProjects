package net.mightyelemental.skype;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Parser {

	static String commandPrefix = "asdf!";

	public static String[] getArgs(String command) {
		if (!command.startsWith(commandPrefix)) { return null; }
		String[] t = command.replaceFirst(commandPrefix, "").split(" ");
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
		}
	}

}
