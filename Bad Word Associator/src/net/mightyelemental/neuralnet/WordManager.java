package net.mightyelemental.neuralnet;

import java.util.HashMap;
import java.util.Map;

public class WordManager {

	public WordManager() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String, types> words = new HashMap<String, types>();

	static {
		words.put("bread", types.NOUN);
		words.put("chess", types.NOUN);
		words.put("grape", types.NOUN);
		words.put("tread", types.VERB);
		words.put("stand", types.VERB);
		words.put("knead", types.VERB);
		words.put("young", types.ADJECTIVE);
		words.put("brown", types.ADJECTIVE);
		words.put("older", types.ADJECTIVE);
		words.put("fwifo", types.FAKE);
		words.put("louis", types.FAKE);
		words.put("henry", types.FAKE);
		// again
		words.put("chair", types.NOUN);
		words.put("plank", types.NOUN);
		words.put("field", types.NOUN);
		words.put("taped", types.VERB);
		words.put("shift", types.VERB);
		words.put("flown", types.VERB);
		words.put("black", types.ADJECTIVE);
		words.put("white", types.ADJECTIVE);
		words.put("swift", types.ADJECTIVE);
		words.put("jerry", types.FAKE);
		words.put("memes", types.FAKE);
		words.put("europ", types.FAKE);
		// again
		words.put("two", types.NOUN);
		words.put("three", types.NOUN);
		words.put("five", types.NOUN);
		words.put("run", types.VERB);
		words.put("look", types.VERB);
		words.put("tuck", types.VERB);
		words.put("hairy", types.ADJECTIVE);
		words.put("long", types.ADJECTIVE);
		words.put("short", types.ADJECTIVE);
		words.put("truth", types.FAKE);
		words.put("keean", types.FAKE);
		words.put("food", types.FAKE);
		System.out.println(words.size());
	}

	/*
	 * @Deprecated public static boolean getGood(String word) { if (word == null)
	 * return false; return words.get(word); }
	 */

	public static enum types {
		NOUN, VERB, ADJECTIVE, FAKE;
	}

	public static types getType(int i) {
		return types.values()[i];
	}

	public static types getWord(String word) {
		if (word == null)
			return types.FAKE;
		return words.get(word);
	}

}
