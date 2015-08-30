package net.wolfgangts.render;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class Render3D {
	private ArrayList<float[]> objects = new ArrayList<float[]>();
	private float gameTime;
	private float fov = 600;
	private float renderDistance = 5000;

	private Random random = new Random();

	public Render3D() {
		this.gameTime = 0;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (float[] item : this.objects) {
			float x, y, z;

			x = item[0];
			y = item[1];
			z = item[2];

			if (z > 0 && z < this.renderDistance) {
				g.fillOval(x / z * this.fov + gc.getWidth() / 2, y / z * this.fov + gc.getHeight() / 2, 12500f / z, 12500f / z);
			}
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		float deltaDivisor = delta / 10f;

		if (this.gameTime == 0) {
			for (int i = 0; i < 1000; i++) {
				this.objects.add(new float[] { this.random.nextFloat() * gc.getWidth(), this.random.nextFloat() * gc.getWidth(),
						this.random.nextFloat() * (gc.getWidth() + gc.getHeight()) / 2, this.random.nextFloat() * 5,
						this.random.nextFloat() * 5, this.random.nextFloat() * 5 });
			}
		}

		if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
			translate(5 * deltaDivisor, 0, 0);
		}

		if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
			translate(-5 * deltaDivisor, 0, 0);
		}

		if (gc.getInput().isKeyDown(Input.KEY_UP)) {
			translate(0, 5 * deltaDivisor, 0);
		}

		if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
			translate(0, -5 * deltaDivisor, 0);
		}

		if (gc.getInput().isKeyDown(Input.KEY_Q)) {
			rotate(rz(0.01f * deltaDivisor));
		}

		if (gc.getInput().isKeyDown(Input.KEY_E)) {
			rotate(rz(-0.01f * deltaDivisor));
		}

		if (gc.getInput().isKeyDown(Input.KEY_A)) {
			rotate(ry(0.01f * deltaDivisor));
		}

		if (gc.getInput().isKeyDown(Input.KEY_D)) {
			rotate(ry(-0.01f * deltaDivisor));
		}

		if (gc.getInput().isKeyDown(Input.KEY_W)) {
			rotate(rx(0.01f * deltaDivisor));
		}

		if (gc.getInput().isKeyDown(Input.KEY_S)) {
			rotate(rx(-0.01f * deltaDivisor));
		}

		if (gc.getInput().isMouseButtonDown(0)) {
			translate(0, 0, -10 * deltaDivisor);
		}

		if (gc.getInput().isMouseButtonDown(1)) {
			translate(0, 0, 10 * deltaDivisor);
		}

		this.gameTime += 1 * delta / 50f;

		/*
		 * for (float[] item : this.objects) { }
		 */
	}

	public void translate(float x, float y, float z) {
		for (float[] item : this.objects) {
			item[0] += x;
			item[1] += y;
			item[2] += z;
		}
	}

	public float[][] rx(float r) {
		float cosR = (float) Math.cos(r);
		float sinR = (float) Math.sin(r);

		float[][] matrix = { { 1, 0, 0 }, { 0, cosR, -sinR }, { 0, sinR, cosR } };

		return matrix;
	}

	public float[][] ry(float r) {
		float cosR = (float) Math.cos(r);
		float sinR = (float) Math.sin(r);

		float[][] matrix = { { cosR, 0, sinR }, { 0, 1, 0 }, { -sinR, 0, cosR } };

		return matrix;
	}

	public float[][] rz(float r) {
		float cosR = (float) Math.cos(r);
		float sinR = (float) Math.sin(r);

		float[][] matrix = { { cosR, -sinR, 0 }, { sinR, cosR, 0 }, { 0, 0, 1 } };

		return matrix;
	}

	public void rotate(float[][] m) {
		for (float[] item : this.objects) {
			float x, y, z;
			x = item[0];
			y = item[1];
			z = item[2];

			// Mutate the position using matrix.
			item[0] = x * m[0][0] + y * m[0][1] + z * m[0][2];
			item[1] = x * m[1][0] + y * m[1][1] + z * m[1][2];
			item[2] = x * m[2][0] + y * m[2][1] + z * m[2][2];
		}
	}
}
