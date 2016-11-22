package net.mightyelemental.skype;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Commands {

	public static String[] commands = { "ls", "time", "add", "spamMe", "wave", "reverse", "doMaths (=)", "angry" };

	public static ScriptEngineManager	manager	= new ScriptEngineManager();
	public static ScriptEngine			engine	= manager.getEngineByName("js");

	private static String prefix = SkypeOS.prefix;

	public static void ls(ChatMessage cm) throws SkypeException {
		String temp = "Commands:";
		for (String s : commands) {
			temp += "\n" + s;
		}
		send(temp, cm);
	}

	public static void add(ChatMessage cm, String[] args) throws SkypeException {
		float temp = 0;
		for (int i = 1; i < args.length; i++) {
			try {
				temp += Float.parseFloat(args[i]);
			} catch (Exception e) {

			}
		}
		send("Answer: " + temp, cm);
	}

	public static void time(ChatMessage cm) throws SkypeException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		send("The time is currently " + time, cm);
	}

	public static void wave(ChatMessage cm) throws SkypeException {
		send("(wave)", cm);
	}

	public static void spam(ChatMessage cm) throws SkypeException {
		String spam = "";
		for (int i = 0; i < 20; i++) {
			spam += "\nSPAM";
		}
		cm.getSender().chat().send(spam);
	}

	public static void send(String mess, ChatMessage cm) throws SkypeException {
		cm.getChat().send(mess);
	}

	public static void doMaths(ChatMessage cm) throws SkypeException {
		String evalRaw = cm.getContent().replaceFirst("=", "").replaceAll("[^0-9.%^*/()+-]", "").replaceAll(" ", "");
		System.out.println(evalRaw);
		try {
			Object answer = engine.eval(evalRaw);
			if (cm.isEditable() && cm.getChat().getAllMembers().length < 3) {
				cm.setContent(evalRaw + "\n=" + answer);
			} else {
				send(evalRaw + "\n=" + answer, cm);
			}
		} catch (ScriptException e) {
			send("Syntax Error!", cm);
			System.err.println("Error!");
		}
	}

	public static void angry(ChatMessage cm) throws SkypeException {
		send("\u0028\u251b\u25c9\u0414\u25c9\u0029\u251b\u5f61\u253b\u2501\u253b", cm);
	}

	public static void reverse(ChatMessage cm) throws SkypeException {
		String temp = "\n";
		String message = cm.getContent().replaceFirst(Parser.commandPrefix + "reverse", "");
		if (message.length() < 4) { return; }
		for (int i = message.length() - 1; i >= 0; i--) {
			temp += message.charAt(i);
		}

		if (!SkypeOS.removeBannedWords(cm, temp)) {
			send("[Skype] " + temp, cm);
		} else {
			send("[Skype] If I were to send message backwords, it would contain a banned phrase. Therefore I am not going to send it", cm);
		}

	}

}
