package net.mightyelemental.skype;

import java.util.Random;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeTest implements ChatMessageListener {

	String user = "wolfgangts";

	Chat chat = Skype.chat(user);

	String prefix = user + "@skype:~ ";

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

	public SkypeTest() throws InterruptedException, SkypeException {
		Skype.addChatMessageListener(this);
		// chat.send(prefix + "Start by typing 'ls'");
		while (true) {
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws SkypeException, InterruptedException {
		new SkypeTest();
	}

	@Override
	public void chatMessageReceived(ChatMessage cm) throws SkypeException {
		prefix = cm.getSenderDisplayName() + "@SkypeOS-Bot:~ ";

		String message = cm.getContent().toLowerCase();

		boolean flag = true;

		chat = cm.getChat();

		if (chat.getAllMembers().length <= 1) { return; }

		// if (cm.getChat().equals(chat)) {
		if (message.equals("ls")) {
			chat.send(prefix
					+ "\nls\nchicken\nquestionable-command\nspiderOS\nspamMe\nadd [...]\nfloat\nhalt\nsmile\ninternet\nDOND\nlang\ngender\nmuteSelf\nbestCountry");
		} else if (message.equals("chicken")) {
			chat.send(prefix + "Enjoy your tastey chicken!");
		} else if (message.equals("questionable-command")) {
			chat.send(prefix + "VIRUS INSTALLED");
		} else if (message.equals("spiderOS")) {
			chat.send(prefix + "Thank you for choosing spiderOS!");
			chat.send(prefix + "I'm sorry, your PC cannot handle the size of this particular spiderOS");
			chat.send(prefix + "You may want to try OSX");
		} else if (message.equals("spamMe 20")) {
			String spam = "\n";
			for (int i = 0; i < 20; i++) {
				spam += "SPAM\n";
			}
			chat.send(prefix + spam + "YOU HAVE BEEN SPAMMED");
		} else if (message.contains("add")) {
			String[] s = message.split(" ");
			float temp = 0;
			for (int i = 1; i < s.length; i++) {
				try {
					temp += Float.parseFloat(s[i]);
				} catch (Exception e) {

				}
			}
			chat.send(prefix + "Answer: " + temp);
		} else if (message.equals("float")) {
			chat.send(prefix + "Get your own FLOAT!!!!");
		} else if (message.equals("smile")) {
			chat.send(prefix + ":D");
		} else if (message.equals("internet")) {
			chat.send(prefix + "\nwww.agor.io\nwww.aname.com\nwww.youtube.com\n\nHave fun!");
		} else if (message.equals("lang")) {
			chat.send(prefix + "You speak " + cm.getSender().getLanguage() + ". well done!");
		} else if (message.equals("gender")) {
			chat.send(prefix + "You are " + cm.getSender().getSex().toString().toLowerCase());
		} else if (message.equals("DOND")) {
			if (rand.nextInt(99) + 1 > 50) {
				chat.send(prefix + "It's a deal!");
			} else {
				chat.send(prefix + "No deal!");
			}

		} else if (message.equals("halt")) {
			chat.send(prefix + "What do you mean 'halt'?");
			// System.exit(0);

		} else if (message.equals("bestCountry")) {
			chat.send(prefix + "(flag:gb)");
		} else if (message.equals("!help")) {
			chat.send(prefix + "No help is avaliable right now. Try again later or use 'ls'");
		} else if (message.equals("!list")) {
			String list = prefix + " Current Members:";
			for (User s : cm.getChat().getAllMembers()) {
				list += "\n" + s.getFullName();
			}
			chat.send(list);
		} else if (message.equals("muteSelf")) {
			try {
				chat.send("/setrole " + cm.getSenderId() + " listener");
			} catch (Exception e) {
			}
		} else {
			// chat.send(cm.getSenderDisplayName() + "@skype:~");
		}

		for (String s : bannedWords) {
			if (message.toLowerCase().contains(s)) {
				try {
					flag = false;
					if (cm.isEditable()) {
						cm.setContent("Gorrila");
					}
					cm.getSender().chat().send("[Skype] The use of banned language will not be tolerated");
					chat.send(cm.getSenderDisplayName() + " has been muted due to the use of banned language");
					chat.send("/setrole " + cm.getSenderId() + " listener");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (flag) {
			String temp = "";

			for (int i = 0; i < message.length(); i++) {
				int t = ((int) message.charAt(i));
				temp += t + " ";
				if (t == 32) {
					temp += "| ";
				}
			}

			System.out.println(message + " - " + temp);
		}

		// }
	}

	@Override
	public void chatMessageSent(ChatMessage cm) throws SkypeException {
		// chatMessageReceived(cm);
		if (cm.getContent().equals("!kick grassy")) {
			// Skype.chat("jzouellette").send("[Skype] spam will not be tolerated");
			// chat.send("jzouellette has been muted due to spam");
			cm.getChat().send("/setrole jzouellette listener");
		}
	}

}
