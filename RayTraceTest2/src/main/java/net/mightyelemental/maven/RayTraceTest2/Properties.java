package net.mightyelemental.maven.RayTraceTest2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Properties {

	private Properties() {}

	public static int threads = Runtime.getRuntime().availableProcessors();

	public static String OS_NAME = System.getProperty( "os.name" );

	public static OSTYPE getOSType() {
		System.out.println( OS_NAME );
		if (OS_NAME.toLowerCase().contains( "win" )) { return OSTYPE.WINDOWS; }
		if (OS_NAME.toLowerCase().contains( "linux" )) { return OSTYPE.LINUX; }
		return OSTYPE.OTHER;
	}

	public enum OSTYPE {
		WINDOWS, LINUX, MAC, OTHER;
	}

	public static String getDefaultFileExplorerLinux() throws IOException {
		Process p = Runtime.getRuntime().exec( "xdg-mime query default inode/directory" );
		String def = new BufferedReader( new InputStreamReader( p.getInputStream() ) ).readLine();
		return def;
	}

}
