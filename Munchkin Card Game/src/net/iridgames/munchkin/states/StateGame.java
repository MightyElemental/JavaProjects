package net.iridgames.munchkin.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.network.UDPServer;
import net.mightyelemental.network.client.UDPClient;

public class StateGame extends BasicGameState {

	private final int ID;

	public UDPClient client;

	public UDPServer server;

	public boolean connectToServer(String ipAddress, int port) {
		client = new UDPClient(ipAddress, port);
		client.setup();
		return client.running;
	}

	public void createServer(int port) {
		server = new UDPServer(port);
		server.setupServer();
		System.out.println("Server has been setup!");
	}

	public StateGame( int id ) {
		this.ID = id;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	@Override
	public int getID() {
		return ID;
	}

}
