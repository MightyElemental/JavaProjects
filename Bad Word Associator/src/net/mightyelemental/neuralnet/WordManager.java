package net.mightyelemental.neuralnet;

import java.util.HashMap;
import java.util.Map;

public class WordManager {

	public WordManager() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String, Boolean> words = new HashMap<String, Boolean>();

	static {
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
		words.put("him", true);
		words.put("led", true);
		words.put("bin", false);
		words.put("guy", true);
		words.put("pet", true);
		words.put("age", true);
		words.put("ape", true);
		words.put("bad", true);
		words.put("bed", false);
		words.put("oof", true);
		words.put("pun", true);
		words.put("barry", true);
		words.put("paul", true);
		words.put("nazi", false);
		words.put("funny", true);
		words.put("awful", false);
		words.put("chess", true);
		words.put("trees", true);
		words.put("apple", true);
		words.put("steak", true);
		words.put("fries", true);
		words.put("phone", true);
		words.put("gopro", true);
		words.put("steam", true);
		words.put("lick", false);
		words.put("anime", true);
		words.put("filth", false);
		words.put("loop", false);
		words.put("music", false);
		words.put("yanny", false);
		words.put("candy", true);
		words.put("cute", true);
		words.put("atom", true);
		words.put("maths", true);
		words.put("maths", true);
		words.put("xeon", true);
		words.put("intel", true);
		words.put("vegan", false);
		words.put("brush", true);
		words.put("blade", true);
		words.put("bacon", true);
		words.put("burke", false);
		words.put("trump", false);
		words.put("blast", false);
		words.put("birth", false);
		words.put("blond", false);
		words.put("bland", false);
		words.put("beard", true);
		words.put("brass", true);
		words.put("gold", true);
		words.put("iron", true);
		words.put("zinc", true);
		words.put("blink", true);
		words.put("hertz", true);
		words.put("hyper", true);
		words.put("horid", false);
		words.put("humid", false);
		words.put("moist", false);
		words.put("hello", true);
		words.put("human", true);
		words.put("hoist", true);
		words.put("honor", true);
		words.put("hippy", false);
		words.put("heron", false);
		words.put("halal", false);
		words.put("hames", true);
		words.put("hamez", true);
		words.put("emily", true);
		words.put("thick", true);
		words.put("think", true);
		words.put("tenth", true);
		words.put("taste", true);
		words.put("terry", false);
		words.put("theft", false);
		words.put("toxic", false);
		words.put("train", false);
		words.put("taxes", false);
		words.put("teddy", true);
		words.put("thumb", false);
		words.put("tweed", true);
		words.put("torso", false);
		words.put("towel", false);
		words.put("torch", true);
		System.out.println(words.size());
	}

	public static boolean getGood(String word) {
		if ( word == null ) return false;
		return words.get(word);
	}

}
