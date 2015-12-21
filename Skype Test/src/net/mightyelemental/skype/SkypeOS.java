package net.mightyelemental.skype;

import java.util.Random;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeOS implements ChatMessageListener {

	public static String user = "wolfgangts";

	public static Chat chat;

	public String[] bannedWords = { getSFB(new Byte[] { 102, 117, 99, 107 }), getSFB(new Byte[] { 115, 104, 105, 116 }), "(finger)",
			getSFB(new Byte[] { 99, 117, 110, 116, 115 }) };

	Random rand = new Random();

	public String getSFB(Byte[] b) {
		String temp = "";
		for (Byte b1 : b) {
			temp += (char) b1.intValue();
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

	public void chat(ChatMessage cm, String message) throws SkypeException {
		// if (chat.getAllMembers().length > 1) { return; }
		if (message.startsWith("auto>")) { return; }

		prefix = cm.getSenderDisplayName() + "@SkypeOS-Bot:~ ";

		System.out.println(message);

		chat = cm.getChat();

		conditionMessage(message);

		// String sendTemp = "";

		/*
		 * for (int i = message.length()-1; i >= 0; i--) { sendTemp += message.charAt(i); }
		 */

		// chat.send(sendTemp);

		for (String s : bannedWords) {
			if (message.toLowerCase().contains(s)) {
				try {
					if (cm.isEditable()) {
						cm.setContent("Gorrila");
					}
					cm.getSender().chat().send("[Skype] The use of banned phrases will not be tolerated");
					chat.send(cm.getSenderDisplayName() + " has been muted due to the use of banned phrases");
					chat.send("/setrole " + cm.getSenderId() + " listener");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		String[] t = Parser.getArgs(message);

		try {
			Parser.commands(t, cm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void conditionMessage(String message) throws SkypeException {
		if (message.contains("apple")) {
			chat.send("auto> (company) Apple is crap. Apple should go bankrupt. Apple should never have existed");
		}

		// if (message.contains("james")) {
		// chat.send("James is currently unavailable, please talk to his account directly or not at all, thanks.");
		// }

		if (message.contains("league")) {
			chat.send("https://www.youtube.com/watch?v=Xhyuey4xU3Q");
		}
		if (message.contains("disney")) {
			chat.send("auto> Disney needs to stop making terrible movies");
		}
		if (message.equals("idc")) {
			chat.send("auto> You should care. It is just plain rude not to.");
		}
	}

	@Override
	public void chatMessageReceived(ChatMessage cm) throws SkypeException {
		String message = cm.getContent().toLowerCase();
		chat(cm, message);
	}

	@Override
	public void chatMessageSent(ChatMessage cm) throws SkypeException {
		String message = cm.getContent().toLowerCase();
		chat(cm, message);
	}

}
