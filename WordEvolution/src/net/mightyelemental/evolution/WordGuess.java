package net.mightyelemental.evolution;

public class WordGuess {
	
	
	public String word = "";
	public int fitness = 0;
	
	public WordGuess( String wordToMan, boolean f ) {
		word = wordToMan;
		fitness = WordEvolution.calculateFitness(word);
	}
	
	public WordGuess( String wordToMan ) {
		if (wordToMan.length() <= 0) { return; }
		StringBuilder sb = new StringBuilder(wordToMan);
		for (int i = 0; i < 2; i++) {
			if (WordEvolution.rand.nextInt(3) == 0) {
				sb = changeRandomChar(sb);
			} else {
				sb = addRemoveRandChar(sb);
			}
		}
		word = sb.toString();
		fitness = WordEvolution.calculateFitness(word);
	}
	
	public StringBuilder changeRandomChar(StringBuilder sb) {
		int r = WordEvolution.rand.nextInt(sb.length());
		sb.replace(r, r + 1, "" + WordEvolution.Randomletters[WordEvolution.rand.nextInt(WordEvolution.Randomletters.length)]);
		return sb;
	}
	
	public StringBuilder addRemoveRandChar(StringBuilder sb) {
		int r = WordEvolution.rand.nextInt(sb.length());
		if (WordEvolution.rand.nextInt(2) == 0) {
			sb.deleteCharAt(r);
		} else {
			sb.insert(r, "" + WordEvolution.Randomletters[WordEvolution.rand.nextInt(WordEvolution.Randomletters.length)]);
		}
		return sb;
	}
	
}
