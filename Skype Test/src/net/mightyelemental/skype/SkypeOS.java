package net.mightyelemental.skype;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageEditListener;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeOS implements ChatMessageListener, ChatMessageEditListener {

	public static String user = "wantspam";

	public static Chat chat;

	public Map<String, List<Object>> spamDetector = new HashMap<String, List<Object>>();

	public static String[] bannedWords = { getSFB(new Byte[] { 102, 117, 99, 107 }), getSFB(new Byte[] { 115, 104, 105, 116 }), "(finger)",
			getSFB(new Byte[] { 99, 117, 110, 116, 115 }), "religion is wrong", getSFB(new Byte[] { 100, 105, 99, 107 }),
			getSFB(new Byte[] { 118, 97, 103, 105, 110, 97 }), getSFB(new Byte[] { 112, 117, 115, 115, 121 }),
			getSFB(new Byte[] { 112, 101, 110, 105, 115 }), getSFB(new Byte[] { 99, 117, 109 }),
			getSFB(new Byte[] { 119, 97, 110, 107, 101, 114 }), getSFB(new Byte[] { 112, 111, 114, 110 }),
			getSFB(new Byte[] { 115, 101, 120 }), getSFB(new Byte[] { 97, 114, 115, 101 }),
			getSFB(new Byte[] { 114, 105, 109, 32, 106, 111, 98 }) };

	Random rand = new Random();//

	public static String getSFB(Byte[] b) {
		String temp = "";
		for (Byte b1 : b) {
			temp += (char) b1.intValue();
		}
		return temp;
	}

	public String stringToByte(String message) {
		String temp = "";

		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == ' ') {
				temp += "#" + ((int) (message.charAt(i))) + "# | ";
			} else {
				temp += ((int) (message.charAt(i))) + ", ";
			}
		}

		return temp;
	}

	public static String prefix = user + "@skype:~ ";

	public SkypeOS() throws SkypeException, InterruptedException {
		chat = Skype.chat(user);
		Skype.addChatMessageListener(this);
		while (true) {
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws SkypeException, InterruptedException {
		new SkypeOS();
	}

	public void chat(ChatMessage cm, String message, boolean host) throws SkypeException {
		chat = cm.getChat();
		if (chat.getAllMembers().length < 3) { return; }

		prefix = cm.getSenderDisplayName().replaceAll(" ", "-") + "@SkypeOS-Bot:~ ";

		conditionMessage(message);
		System.out.println("[Skype] '" + message + "' = " + stringToByte(message));

		boolean flag = removeBannedWords(cm, message);

		/*
		 * if (!host && !flag && message.length() > 6) { reverse(cm, message); }
		 */

		if (flag) {
			// cm.getSender().chat().send("[Skype] The use of banned phrases will not be tolerated");
			// chat.send(cm.getSenderDisplayName() + " has been muted due to the use of banned phrases");
			// chat.send("/setrole " + cm.getSenderId() + " listener");
			chat.send("[Skype] User '" + cm.getSenderId() + "' has violated the T.O.S. by using banned phrases");
			System.out.println(cm.getSenderId() + "> CONTAINS BANNED (a) PHRASE(S)");
		} else if (message.length() > 1) {
			System.out.println(cm.getSenderId() + "> " + message);
		}

		String[] t = Parser.getArgs(message);

		try {
			Parser.commands(t, cm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reverse(ChatMessage cm, String message) throws SkypeException {

		String sendTemp = "[Skype] ";

		for (int i = message.length() - 1; i >= 0; i--) {
			sendTemp += message.charAt(i);
		}

		if (!removeBannedWords(cm, sendTemp)) {
			chat.send(sendTemp);
		} else {
			chat.send("[Skype] If I were to send message backwords, it would contain a banned phrase. Therefore I am not going to send it");
		}
	}

	public static boolean removeBannedWords(ChatMessage cm, String message) {
		for (String s : bannedWords) {
			if (message.toLowerCase().contains(s)) {
				try {
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public void conditionMessage(String message) throws SkypeException {
		if (message.contains("apple")) {
			chat.send("[Skype] (company) Apple is crap. Apple should go bankrupt. Apple should never have existed");
		}

		// if (message.contains("james")) {
		// chat.send("James is currently unavailable, please talk to his account directly or not at all, thanks.");
		// }

		if (message.contains("league")) {
			chat.send("https://www.youtube.com/watch?v=Xhyuey4xU3Q");
		}
		if (message.contains("disney")) {
			chat.send("[Skype] Disney needs to stop making terrible movies");
		}
		if (message.equals("idc")) {
			chat.send("[Skype] You should care. It is just plain rude not to.");
		}
		if (message.contains("thanks skype")) {
			chat.send("[Skype] You're welcome.");
		}

		// Smite commands
		String messTemp = "";
		switch (message) {
			case "vvn":
				messTemp += "n - no";
				break;
			case "vva":
				messTemp += "a - ok";
				break;
			case "vvy":
				messTemp += "y - yes";
				break;
			case "vvb":
				messTemp += "y - be right back";
				break;
			case "vvs":
				messTemp += "y - sorry";
				break;
			case "vvt":
				messTemp += "y - thanks";
				break;
		}
		if (messTemp.length() > 0) {
			chat.send("[Skype] [Meaning] vv" + messTemp);
		}
	}

	public boolean isUserSpamming(ChatMessage cm) throws SkypeException {
		if (spamDetector.containsKey(cm.getSenderId())) {
			Date t = (Date) spamDetector.get(cm.getSenderId()).get(0);
			long timeDif = (cm.getTime().getTime() - t.getTime()) / 1000;
			int times = ((int) spamDetector.get(cm.getSenderId()).get(1));
			if (timeDif < 3) {
				times++;
			} else {
				times = 1;
			}
			spamDetector.put(cm.getSenderId(), Arrays.asList(new Object[] { cm.getTime(), times }));
			System.out.println(timeDif + "s | " + times + " times");
			if (times > 4) { return true; }
		} else {
			spamDetector.put(cm.getSenderId(), Arrays.asList(new Object[] { cm.getTime(), 1 }));
		}
		return false;
	}

	@Override
	public void chatMessageReceived(ChatMessage cm) throws SkypeException {
		String message = cm.getContent().toLowerCase();
		chat(cm, message, false);
		boolean a = isUserSpamming(cm);
		if (a) {
			cm.getChat().send(cm.getSenderDisplayName() + " has been found guilty of spamming!");
			try {
				cm.getChat().send(cm.getSenderDisplayName() + " has been muted! Please contact an admin to be unmuted.");
				cm.getChat().send("/setrole " + cm.getSenderId() + " listener");
			} catch (Exception e) {
				// System.err.println("MUTING FAILED! " + cm.getSenderId().toUpperCase() + " CAN STILL TALK!!");
				// e.printStackTrace();
			}
		}
	}

	@Override
	public void chatMessageSent(ChatMessage cm) throws SkypeException {
		String message = cm.getContent().toLowerCase();
		chat(cm, message, true);
	}

	@Override
	public void chatMessageEdited(ChatMessage cm, Date d, User u) {
		try {
			String message = cm.getContent().toLowerCase();
			chat(cm, message, false);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

}
