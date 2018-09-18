package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Graphics;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppHarmony extends AppWindow {

	private static final long serialVersionUID = 1046548414901267080L;

	private FirebaseAuth	fbAuth;
	private FirebaseApp		fbApp;

	public AppHarmony(float x, float y, float width, float height) {
		super(x, y, width, height, "Harmony");

		FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
		builder.setDatabaseUrl("https://xendos-chat.firebaseio.com/");
		fbApp = FirebaseApp.initializeApp(builder.build(), "Harmony");
		fbAuth = FirebaseAuth.getInstance(fbApp);
		System.out.println(fbApp.getName());
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {

	}

	@Override
	public void updateContent(int delta) {

	}

}
