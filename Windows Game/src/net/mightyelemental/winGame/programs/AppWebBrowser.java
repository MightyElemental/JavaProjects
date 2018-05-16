package net.mightyelemental.winGame.programs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUITextBox;
import net.mightyelemental.winGame.guiComponents.GUIhtmlViewer;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppWebBrowser extends AppWindow {

	private static final long serialVersionUID = 7935648659277287522L;

	GUIhtmlViewer	panel;
	GUITextBox		text;

	public AppWebBrowser(float x, float y, float width, float height) {
		super(x, y, 1000, 1000 / 16f * 9f, "Corner");
		height = 1000 / 16f * 9f;
		width = 1000;
		text = new GUITextBox(width - 34, 20, "#URL_BAR");
		this.addGUIObject(text, 5, 5);
		this.addGUIObject(new GUIButton(20, 20, "#go", this).setText("->").setColor(Color.green), width - 25, 5);
		panel = new GUIhtmlViewer(width - 10, height - 85, "Panel", this);
		this.addGUIObject(panel, 5, 50);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		this.clearScreen();
	}

	@Override
	public void updateContent(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPressed(int key, char c) {

	}

	@Override
	public void onComponentPressed(int button, GUIComponent c) {
		if ( c.getUID().equals("#GO") ) {
			try {
				downloadPage(text.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			text.clearText();
		}
	}

	public void downloadPage(String url) throws IOException {
		System.out.println("|" + url + "|");
		URL link = new URL(url);
		HttpURLConnection con = (HttpURLConnection) link.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);

		}
		panel.setHTML(content);
		in.close();
	}

}
