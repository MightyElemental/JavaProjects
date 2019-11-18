package net.mightyelemental.maven.RayTraceTest2.thread;

public class RenderChunk {

	public boolean startedRender, finishedRender;

	public int x, y;

	public static final int CHUNK_SIZE = 64;

	/** starting and ending times for the render */
	public long startingTime, finishTime;

	public RenderChunk(int x, int y) { this.x = x; this.y = y; }

	public void startRender() {
		startedRender = true;
		startingTime = System.currentTimeMillis();
	}

	public void endRender() {
		finishedRender = true;
		finishTime = System.currentTimeMillis();
		// System.out.println(x + "|" + y + " took " + (finishTime - startingTime) + "ms to render");
	}

	public void reset() {
		startedRender = false;
		finishedRender = false;
		startingTime = 0;
		finishTime = 0;
	}

}
