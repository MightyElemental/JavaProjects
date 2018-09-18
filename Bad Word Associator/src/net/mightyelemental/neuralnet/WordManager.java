package net.mightyelemental.neuralnet;

import java.util.HashMap;
import java.util.Map;

public class WordManager {

	public WordManager() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String, Boolean> words = new HashMap<String, Boolean>();

	static {
		words.put("and", true);
		words.put("nah", false);
		words.put("yes", true);
		words.put("die", false);
		words.put("cat", true);
		words.put("dog", false);
		words.put("pot", false);
		words.put("top", true);
		words.put("rat", false);
		words.put("ksi", false);
		words.put("you", true);
		words.put("leg", false);
		words.put("her", false);
		words.put("him", true);
		words.put("bar", false);
		words.put("led", true);
		words.put("bin", false);
		words.put("gal", false);
		words.put("guy", true);
		words.put("pet", true);
	}

	public static boolean getGood(String word) {
		if (word == null)
			return false;
		return words.get(word);
	}

}
