package net.mightyelemental.neuralnet;

import java.util.HashMap;
import java.util.Map;

public class WordManager {

	public WordManager() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String, types> words = new HashMap<String, types>();

	static {
		words.put("james", types.MALE);
//		words.put("louis", types.MALE);
//		words.put("aaron", types.MALE);
//		words.put("evan", types.MALE);
//		words.put("jacob", types.MALE);
//		words.put("keean", types.MALE);
//		words.put("lewis", types.MALE);
//		words.put("oliver", types.MALE);
//		words.put("jake", types.MALE);
//		words.put("jack", types.MALE);
//		words.put("john", types.MALE);
//		words.put("fred", types.MALE);
//		words.put("jeff", types.MALE);
//		words.put("frank", types.MALE);
//		words.put("paul", types.MALE);
		
		words.put("yuki", types.FEMALE);
//		words.put("emma", types.FEMALE);
//		words.put("emily", types.FEMALE);
//		words.put("morgan", types.FEMALE);
//		words.put("lucy", types.FEMALE);
//		words.put("susan", types.FEMALE);
//		words.put("hannah", types.FEMALE);
//		words.put("sabrina", types.FEMALE);
//		words.put("sophie", types.FEMALE);
//		words.put("ellie", types.FEMALE);
//		words.put("gacie", types.FEMALE);
//		words.put("megan", types.FEMALE);
//		words.put("evie", types.FEMALE);
//		words.put("anna", types.FEMALE);
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

	public static types getType(String word) {
		return words.get(word);
	}

}
