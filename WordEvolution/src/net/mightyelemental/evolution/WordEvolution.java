package net.mightyelemental.evolution;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordEvolution {
	
	
	public String baseString = "Good starting point";
	
	public WordEvolution() {
		long time1 = System.currentTimeMillis();
		String s = "";
		for (char c : targetChar) {
			s += c;
		}
		System.out.println("Target\t\t| " + target);
		System.out.println("Letters Used\t| " + s);
		WordGuess g = createNewGeneration(baseString);
		System.out.println("First Gen\t| " + baseString);
		System.out.println("Fitness\t\t| " + calculateFitness(baseString));
		int i = 0;
		for (i = 0; !g.word.equals(target); i++) {
			if (i % 10 == 0) {
				System.err.println("Best Generation\t| " + g.word);
				System.out.println("Fitness\t\t| " + g.fitness + "(" + round((((double) g.fitness / (double) maxFitness) * 100), 2) + "%)");
				// System.err.println("Worst\t\t| " + worst);
			}
			// try {
			// Thread.sleep(1);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			g = createNewGeneration(g);
		}
		System.out.println("Target\t\t| " + target);
		System.out.println("Base\t\t| " + baseString);
		System.out.println("End Generation\t| " + g.word);
		System.out.println("Fitness\t\t| " + g.fitness + "(" + round((((double) g.fitness / (double) maxFitness) * 100), 2) + "%)");
		System.out.println("Generations\t| " + (i + 1));
		System.err.println("Time Taken\t| " + (System.currentTimeMillis() - time1) + "ms");
	}
	
	public static Random rand = new Random(System.nanoTime());
	// only use a-z, A-Z, space, !?.,
	public static final String target = "Could you please guess my sentence? That would be very helpful to me. So thanks again for guessing correctly.";
	public static final String[] targetWords = target.split(" ");
	public static final char[] targetChar = getLettersUsed(target);
	public static final char[] Randomletters = "qwertyuiopasdfghjklzxcvbnm QWERTYUIOPASDFGHJKLZXCVBNM!?.,".toCharArray();
	public static final int maxFitness = calculateFitness(target);
	public static final int numOfWords = target.split(" ").length;
	
	public static float round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (float) ((double) Math.round(value * scale) / scale);
	}
	
	public static int calculateFitness(String word) {
		int fitness = 0;
		int l = Math.abs(target.length() - word.length());// length
		fitness -= l;
		
//		l = Math.abs(numOfWords - word.split(" ").length);// number of words
//		fitness -= l;
//		
//		for (int i = 0; i < targetChar.length; i++) {// contains correct chars
//			if (word.contains(targetChar[i] + "")) {
//				fitness += 1;
//				if (targetChar[i] == ' ') {// contains correct spaces
//					fitness += 1;
//				}
//			}
//		}
//		String[] words = word.split(" ");
//		int wordCombo = 0;
//		for (int i = 0; i < words.length && i < targetWords.length; i++) {// correct word placement
//			if (words[i].equals(targetWords[i])) {
//				wordCombo++;
//				fitness += 2 + wordCombo;
//			} else {
//				wordCombo = 0;
//			}
//		}
		int charCombo = 0;
		for (int i = 0; i < word.length() && i < target.length(); i++) {// correct char placement and combo
			if (word.charAt(i) == target.charAt(i)) {
				fitness += 1 + charCombo;
				charCombo++;
				if (word.charAt(i) == ' ') {
					charCombo = 0;
				}
			} else {
				charCombo = 0;
			}
		}
		return fitness;
	}
	
	public static void main(String[] args) {
		new WordEvolution();
	}
	
	public WordGuess createNewGeneration(String best) {
		return createNewGeneration(new WordGuess(best, true));
	}
	
	public String worst;
	
	public WordGuess createNewGeneration(WordGuess best) {
		List<WordGuess> guesses = new ArrayList<WordGuess>();
		for (int i = 0; i < 10000; i++) {
			guesses.add(new WordGuess(best.word));
		}
		double p = ((double) best.fitness / (double) maxFitness);
		if (p > 0.01 && (p < 0.8 || p > 0.9)) {
			best.fitness = 0;
		}
		WordGuess worst = new WordGuess("");
		worst.fitness = Integer.MAX_VALUE;
		for (int i = 0; i < 10000; i++) {
			if (guesses.get(i).fitness > best.fitness) {
				best = guesses.get(i);
			}
			if (guesses.get(i).fitness < worst.fitness) {
				worst = guesses.get(i);
			}
		}
		this.worst = worst.word;
		return best;
	}
	
	public static char[] getLettersUsed(String string) {
		char[] chars = string.toCharArray();
		Set<Character> charSet = new LinkedHashSet<Character>();
		for (char c : chars) {
			charSet.add(c);
		}
		
		StringBuilder sb = new StringBuilder();
		for (Character character : charSet) {
			sb.append(character);
		}
		return sb.toString().toCharArray();
	}
	
}
