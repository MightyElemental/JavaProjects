package net.mightyelemental.neuralnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordManager {

	public WordManager() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String, types> words = new HashMap<String, types>();

	static {
		try {
			loadWords();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(words.size());
	}

	/*
	 * @Deprecated public static boolean getGood(String word) { if (word == null)
	 * return false; return words.get(word); }
	 */

	public static enum types {
		MALE, FEMALE;
	}

	public static types getType(int i) {
		return types.values()[i];
	}

	private static void loadWords() throws IOException {
		File maleNames = new File("names_male.txt");

		String readLine = "";

		BufferedReader b = new BufferedReader(new FileReader(maleNames));
		while ((readLine = b.readLine()) != null) {
			words.put(readLine.toLowerCase().replaceAll("[^a-z]", ""), types.MALE);
		}
		b.close();
		File femaleNames = new File("names_female.txt");

		readLine = "";

		b = new BufferedReader(new FileReader(femaleNames));
		while ((readLine = b.readLine()) != null) {
			words.put(readLine.toLowerCase().replaceAll("[^a-z]", ""), types.FEMALE);
		}
		b.close();
	}

	public static types getType(String word) {
		return words.get(word);
	}

}
