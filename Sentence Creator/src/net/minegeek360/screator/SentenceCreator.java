package net.minegeek360.screator;

import java.util.Random;

import javax.swing.JOptionPane;

public class SentenceCreator {
	
	public static Random rand = new Random();
	
	public static void main(String[] args){
		String sentences = "";
		for(int i = 0; i < 10; i++){
			sentences += (i+1)+") "+generateSentence()+"\n";
			System.out.println((i+1)+") "+generateSentence());
		}
		JOptionPane.showMessageDialog(null, sentences);
	}
	
	public static String generateSentence(){
		String temp = "";
		String s = " ";
		String noun = Dictionary.nouns[rand.nextInt(Dictionary.nouns.length)];
		String adjective = Dictionary.adjectives[rand.nextInt(Dictionary.adjectives.length)];
		String article = genArticle(adjective);
		String verb = Dictionary.verb[rand.nextInt(Dictionary.verb.length)];
		String adverb = Dictionary.adverb[rand.nextInt(Dictionary.adverb.length)];
		String preposition = Dictionary.preposition[rand.nextInt(Dictionary.preposition.length)];
		int complex = randNum(3);
		int dadj = randNum(2);
		int addadverb = randNum(2);
		int prep = randNum(2);
		if(complex == 1){
			temp = "As "+article+s+adjective;
		}else{
			temp = article+s+adjective;
		}
		if(dadj == 1){
			adjective = Dictionary.adjectives[rand.nextInt(Dictionary.adjectives.length)];
			temp+=", "+adjective+s;
		}else{
			temp += s;
		}
		temp += noun+s;
		if(addadverb == 1){
			temp += adverb+s;
		}
		temp += verb;
		if(prep == 1){
			noun = Dictionary.nouns[rand.nextInt(Dictionary.nouns.length)];
			article = genArticle(noun);
			temp += s+preposition+s+article+s+noun;
		}
		if(complex == 1){
			noun = Dictionary.nouns[rand.nextInt(Dictionary.nouns.length)];
			article = genArticle(noun);
			verb = Dictionary.verb[rand.nextInt(Dictionary.verb.length)];
			temp += ", "+article+s+noun+s+verb;
		}
		return temp;
	}
	
	public static String genArticle(String c){
		String temp = Dictionary.defArticle[rand.nextInt(Dictionary.defArticle.length)];
		if(temp.equals("a")){
			if(c.startsWith("a") || c.startsWith("e") || c.startsWith("i") || c.startsWith("o") || c.startsWith("u")){
				temp += "n";
			}
		}
		return temp;
	}
	
	public static int randNum(int max){
		return rand.nextInt(max);
	}
	
}
