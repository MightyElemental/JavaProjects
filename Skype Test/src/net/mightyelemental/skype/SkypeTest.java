package net.mightyelemental.skype;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeTest implements ChatMessageListener {

	Chat chat = Skype.chat("wolfgangts");

	public SkypeTest() throws InterruptedException, SkypeException {
		Skype.addChatMessageListener(this);
		chat.send("temp@skype~");
		while (true) {
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws SkypeException, InterruptedException {
		new SkypeTest();
	}

	@Override
	public void chatMessageReceived(ChatMessage cm) throws SkypeException {
		if (cm.getChat().equals(chat)) {
			if (cm.getContent().equals("ls")) {
				chat.send(cm.getSenderDisplayName() + "@skype:~\nls\nchicken\nquestionable-command\nspiderOS\nspamMe\nadd\nfloat");
			} else if (cm.getContent().equals("chicken")) {
				chat.send(cm.getSenderDisplayName() + "@skype:~ Enjoy your tastey chicken!");
			} else if (cm.getContent().equals("questionable-command")) {
				chat.send(cm.getSenderDisplayName() + "@skype:~ VIRUS INSTALLED");
			} else if (cm.getContent().equals("spiderOS")) {
				chat.send(cm.getSenderDisplayName() + "@skype:~ Thank you for choosing spiderOS!");
				chat.send(cm.getSenderDisplayName() + "@skype:~ I'm sorry, your PC cannot handle the size of this particular spiderOS");
				chat.send(cm.getSenderDisplayName() + "@skype:~ You may want to try OSX");
			} else if (cm.getContent().equals("spamMe")) {
				chat.send(cm.getSenderDisplayName() + "@skype:~ SPAM");
				for (int i = 0; i < 20; i++) {
					chat.send("SPAM");
				}
				chat.send("YOU HAVE BEEN SPAMMED");
			} else if (cm.getContent().contains("add")) {
				String[] s = cm.getContent().split(" ");
				float temp = 0;
				for (int i = 1; i < s.length; i++) {
					try {
						temp += Float.parseFloat(s[i]);
					} catch (Exception e) {

					}
				}
				chat.send(cm.getSenderDisplayName() + "@skype:~ Answer: " + temp);
			}else if (cm.getContent().equals("float")){
				chat.send(cm.getSenderDisplayName() + "@skype:~ Get your own FLOAT!!!!");
			} else {
				chat.send(cm.getSenderDisplayName() + "@skype:~");
			}

		}
	}

	@Override
	public void chatMessageSent(ChatMessage cm) throws SkypeException {
	}

}
