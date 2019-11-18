package net.mightyelemental.maven.RayTraceTest2.thread;

import java.util.ArrayList;
import java.util.List;

import net.mightyelemental.maven.RayTraceTest2.App;
import net.mightyelemental.maven.RayTraceTest2.Properties;
import net.mightyelemental.maven.RayTraceTest2.Ray;
import net.mightyelemental.maven.RayTraceTest2.Utils;

public class ThreadHandler {

	public List<RenderChunk> chunks = new ArrayList<RenderChunk>();

	int screenWidth;
	int screenHeight;

	public ThreadHandler(int chunkWidth, int chunkHeight, int screenWidth,
			int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		// defineChunks(chunkWidth, chunkHeight, screenWidth, screenHeight);
		chunks.addAll( Utils.generateRenderChunks( screenWidth, screenHeight ) );
	}

//	private void defineChunks(int chunkWidth, int chunkHeight, int screenWidth, int screenHeight) {
//		for (int x = 0; x < screenWidth; x += chunkWidth) {
//			for (int y = 0; y < screenHeight; y += chunkHeight) {
//				int height = Math.min(screenHeight - (y), chunkHeight);
//				int width = Math.min(screenWidth - (x), chunkWidth);
//				RenderChunk rc = new RenderChunk(x, y, width, height);
//				chunks.add(rc);
//			}
//		}
//	}

	public List<Thread> setupRenderingThreads(App a, int[] pixels,
			int pixelSize) {
		for (RenderChunk c : chunks) { c.reset(); }
		List<Thread> renderingThreads = new ArrayList<Thread>();
		for (int core = 0; core < Properties.cores / 2; core++) {
			final int threadNum = core;
			Thread rt = new Thread( "t" + threadNum ) {

				public void run() {
					for (RenderChunk c : chunks) {
						if (!c.startedRender) {
							c.startRender();

							for (int x = c.x; x < c.x
									+ RenderChunk.CHUNK_SIZE; x += pixelSize) {
								for (int y = c.y; y < c.y
										+ RenderChunk.CHUNK_SIZE; y += pixelSize) {
									// System.out.println(x+"|"+y);
									Ray r = a.cam.createRay( x, y );
									int col = a.getIntFromVector( a.trace( r, 0 ) );
									for (int i = 0; i < pixelSize
											&& x + i < screenWidth; i++) {
										for (int j = 0; j < pixelSize
												&& y + j < screenHeight; j++) {
											a.setPixel( pixels, screenWidth, x + i, y + j,
													col );
										}
									} // TODO: fix issue of not rendering fully

								}
							}

							c.endRender();
						}
					}
				}
			};
			renderingThreads.add( rt );
		}
		return renderingThreads;
	}

}
