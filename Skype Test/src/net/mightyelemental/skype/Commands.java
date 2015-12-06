package net.mightyelemental.skype;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Commands {

	public static String[] commands = { "ls", "time", "add", "spamMe", "wave" };

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

	private static void send(String mess, ChatMessage cm) throws SkypeException {
		cm.getChat().send(prefix + mess);
	}

}
