package net.mightyelemental.neuralnet;

import java.util.HashMap;
import java.util.Map;

public class WordManager {

	public WordManager() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String, types> words = new HashMap<String, types>();

	static {
		words.put("bread", types.ENGLISH);
		words.put("beard", types.ENGLISH);
		words.put("apple", types.ENGLISH);
		words.put("funny", types.ENGLISH);
		words.put("amaze", types.ENGLISH);
		words.put("trees", types.ENGLISH);
		words.put("toast", types.ENGLISH);
		words.put("piano", types.ENGLISH);
		words.put("oily", types.ENGLISH);
		words.put("fish", types.ENGLISH);
		words.put("steak", types.ENGLISH);
		words.put("grape", types.ENGLISH);
		words.put("grate", types.ENGLISH);
		words.put("lick", types.ENGLISH);
		words.put("cute", types.ENGLISH);
		words.put("face", types.ENGLISH);
		words.put("fear", types.ENGLISH);

		words.put("obtenir", types.FRENCH);
		words.put("reposer", types.FRENCH);
		words.put("italien", types.FRENCH);
		words.put("docteur", types.FRENCH);
		words.put("barrage", types.FRENCH);
		words.put("globe", types.FRENCH);
		words.put("milice", types.FRENCH);
		words.put("barreau", types.FRENCH);
		words.put("montee", types.FRENCH);
		words.put("contrer", types.FRENCH);
		words.put("alcool", types.FRENCH);
		words.put("figurer", types.FRENCH);
		words.put("chinois", types.FRENCH);
		words.put("huit", types.FRENCH);
		words.put("neuf", types.FRENCH);
		words.put("deceler", types.FRENCH);
		words.put("sortant", types.FRENCH);

		words.put("kleiner", types.GERMAN);
		words.put("fruh", types.GERMAN);
		words.put("susser", types.GERMAN);
		words.put("mantel", types.GERMAN);
		words.put("schutz", types.GERMAN);
		words.put("wozu", types.GERMAN);
		words.put("wirken", types.GERMAN);
		words.put("anderer", types.GERMAN);
		words.put("gewiss", types.GERMAN);
		words.put("nazi", types.GERMAN);
		words.put("gekauft", types.GERMAN);
		words.put("deiner", types.GERMAN);
		words.put("durfen", types.GERMAN);
		words.put("gelernt", types.GERMAN);
		words.put("raten", types.GERMAN);
		words.put("staat", types.GERMAN);
		words.put("tot", types.GERMAN);
		
		words.put("yuki", types.JAPANESE);
		words.put("chigau", types.JAPANESE);
		words.put("wakaru", types.JAPANESE);
		words.put("itoko", types.JAPANESE);
		words.put("itai", types.JAPANESE);
		words.put("utau", types.JAPANESE);
		words.put("tsugi", types.JAPANESE);
		words.put("yomu", types.JAPANESE);
		words.put("otto", types.JAPANESE);
		words.put("shaberu", types.JAPANESE);
		words.put("haru", types.JAPANESE);
		words.put("seiseki", types.JAPANESE);
		words.put("kami", types.JAPANESE);
		words.put("gasu", types.JAPANESE);
		words.put("otoko", types.JAPANESE);
		words.put("gyuniku", types.JAPANESE);
		words.put("sansu", types.JAPANESE);
		System.out.println(words.size());
	}

	/*
	 * @Deprecated public static boolean getGood(String word) { if (word == null)
	 * return false; return words.get(word); }
	 */

	public static enum types {
		ENGLISH, GERMAN, JAPANESE, FRENCH;
	}

	public static types getType(int i) {
		return types.values()[i];
	}

	public static types getWord(String word) {
		return words.get(word);
	}

}
