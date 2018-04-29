package net.mightyelemental.winGame.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

public class ProgramLoader {

	public static synchronized void loadJar(String path) {
		try {
			JarFileLoader loader = new JarFileLoader(new URL[] {});
			loader.addFile(path);
			System.out.println(Arrays.asList(loader.getURLs()));
			Class<?> c = Class.forName("Main", true, loader);
			Method method = c.getMethod("initWindow", Object.class);
			method.setAccessible(true); /* promote the method to public access */
			System.out.println(Arrays.asList(method.getParameters()));
			method.invoke(c.newInstance(), "asd");
		} catch (IOException | IllegalArgumentException | NoSuchMethodException | SecurityException | ClassNotFoundException
				| IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}

}
