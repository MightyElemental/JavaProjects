package net.iridgames.munchkin;

import javax.swing.JOptionPane;

public class GameBreakException extends Exception {

	private static final long serialVersionUID = -2530317332794618569L;

	public GameBreakException() {
		super();
		crashMessage("Undefined");
	}

	public GameBreakException( String message ) {
		super(message);
		crashMessage(message);
	}

	public GameBreakException( String message, Throwable cause ) {
		super(message, cause);
		crashMessage(message + "\n" + cause.toString());
	}

	public GameBreakException( Throwable cause ) {
		super(cause);
		crashMessage(cause.toString());
	}

	private static void crashMessage(String message) {
		JOptionPane.showMessageDialog(null, "Oops! A fatal error has occurred: \n" + message, Munchkin.GAME_NAME + " | Crash Report",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

}
