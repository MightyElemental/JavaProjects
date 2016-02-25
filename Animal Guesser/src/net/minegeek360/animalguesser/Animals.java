package net.minegeek360.animalguesser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Animals implements Runnable{
	
	//The way this is set up is, ARGS: # of legs, has tail, makes milk for humans, size, color, sound, name, has wings, 2+ colors, food chain
	public AnimalBase cow = new AnimalBase(4, true, true, AnimalBase.SIZE_CATTLE, AnimalBase.COLOR_BROWN, AnimalBase.MOO, "cow", false, true, AnimalBase.MIDDLE);
	public AnimalBase sheep = new AnimalBase(4, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_WHITE, AnimalBase.BAA, "sheep", false, false, AnimalBase.MIDDLE);
	public AnimalBase dog = new AnimalBase(4, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_OTHER, AnimalBase.WOOF, "dog", false, true, AnimalBase.TOP);
	public AnimalBase pig = new AnimalBase(4, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_PINK, AnimalBase.OINK, "pig", false, true, AnimalBase.MIDDLE);
	public AnimalBase spider = new AnimalBase(8, false, false, AnimalBase.SIZE_INSECT, AnimalBase.COLOR_OTHER, AnimalBase.NONE, "spider", false, true, AnimalBase.MIDDLE);
	public AnimalBase chicken = new AnimalBase(2, false, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_WHITE, AnimalBase.CLUCK, "chicken", true, true, AnimalBase.MIDDLE);
	public AnimalBase cat = new AnimalBase(4, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_OTHER, AnimalBase.MEOW, "cat", false, true, AnimalBase.TOP);
	public AnimalBase hamster = new AnimalBase(4, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_OTHER, AnimalBase.SQUEAK, "hamster", false, true, AnimalBase.MIDDLE);
	public AnimalBase owl = new AnimalBase(2, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_OTHER, AnimalBase.TWITTWOO, "owl", true, true, AnimalBase.TOP);
	public AnimalBase cockroach = new AnimalBase(8, false, false, AnimalBase.SIZE_INSECT, AnimalBase.COLOR_BROWN, AnimalBase.CLICK, "cockroach", true, true, AnimalBase.MIDDLE);
	public AnimalBase goat = new AnimalBase(4, true, true, AnimalBase.SIZE_DOG, AnimalBase.COLOR_OTHER, AnimalBase.BAA, "goat", false, true, AnimalBase.MIDDLE);
	public AnimalBase platypus = new AnimalBase(4, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_BROWN, AnimalBase.OTHER, "platypus", false, false, AnimalBase.MIDDLE);
	public AnimalBase frog = new AnimalBase(4, false, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_GREEN, AnimalBase.CROAK, "frog", false, true, AnimalBase.MIDDLE);
	public AnimalBase giraffe = new AnimalBase(4, true, false, AnimalBase.SIZE_BIGGER, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "giraffe", false, true, AnimalBase.MIDDLE);
	public AnimalBase fly = new AnimalBase(6, false, false, AnimalBase.SIZE_INSECT, AnimalBase.COLOR_OTHER, AnimalBase.BUZZ, "fly", true, true, AnimalBase.MIDDLE);
	public AnimalBase beetle = new AnimalBase(6, false, false, AnimalBase.SIZE_INSECT, AnimalBase.COLOR_OTHER, AnimalBase.NONE, "beetle", true, true, AnimalBase.MIDDLE);
	public AnimalBase horse = new AnimalBase(4, true, false, AnimalBase.SIZE_CATTLE, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "horse", false, true, AnimalBase.MIDDLE);
	public AnimalBase bear = new AnimalBase(4, true, false, AnimalBase.SIZE_CATTLE, AnimalBase.COLOR_BROWN, AnimalBase.OTHER, "bear", false, false, AnimalBase.TOP);
	public AnimalBase rabbit = new AnimalBase(4, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "rabbit", false, true, AnimalBase.MIDDLE);
	public AnimalBase lion = new AnimalBase(4, true, false, AnimalBase.SIZE_CATTLE, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "lion", false, true, AnimalBase.TOP);
	public AnimalBase bird = new AnimalBase(2, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "bird", true, true, AnimalBase.MIDDLE);
	public AnimalBase flea = new AnimalBase(6, false, false, AnimalBase.SIZE_INSECT, AnimalBase.COLOR_BROWN, AnimalBase.OTHER, "flea", false, true, AnimalBase.MIDDLE);
	public AnimalBase penguin = new AnimalBase(2, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "penguin", false, true, AnimalBase.MIDDLE);
	public AnimalBase squirrel = new AnimalBase(4, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_BROWN, AnimalBase.OTHER, "squirrel", false, true, AnimalBase.MIDDLE);
	public AnimalBase meercat = new AnimalBase(4, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "meercat", false, true, AnimalBase.MIDDLE);
	public AnimalBase elephant = new AnimalBase(4, true, false, AnimalBase.SIZE_BIGGER, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "elephant", false, false, AnimalBase.TOP);
	public AnimalBase turtle = new AnimalBase(4, true, false, AnimalBase.SIZE_RODENT, AnimalBase.COLOR_GREEN, AnimalBase.OTHER, "turtle", false, true, AnimalBase.MIDDLE);
	public AnimalBase seal = new AnimalBase(2, true, false, AnimalBase.SIZE_BIGGER, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "seal", false, true, AnimalBase.MIDDLE);
	public AnimalBase zebra = new AnimalBase(4, true, false, AnimalBase.SIZE_CATTLE, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "zebra", false, true, AnimalBase.MIDDLE);
	public AnimalBase panda = new AnimalBase(4, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "panda", false, true, AnimalBase.MIDDLE);
	public AnimalBase flamingo = new AnimalBase(2, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_PINK, AnimalBase.OTHER, "flamingo", true, false, AnimalBase.MIDDLE);
	public AnimalBase cheeta = new AnimalBase(4, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "cheeta", false, true, AnimalBase.TOP);
	public AnimalBase monkey = new AnimalBase( 2, true, false, AnimalBase.SIZE_DOG, AnimalBase.COLOR_OTHER, AnimalBase.OTHER, "monkey", false, false, AnimalBase.TOP);

	public List<String>	animalNameList	= new ArrayList<String>();
	public List<AnimalBase>	animalList = new ArrayList<AnimalBase>();

	public Animals() {
		Field[] animals = this.getClass().getDeclaredFields();
		for (Field f:animals) {
			if(f.getType().equals(AnimalBase.class)){
				try {
					animalList.add((AnimalBase) f.get(this));
					animalNameList.add(((AnimalBase) f.get(this)).getName());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		String jtaTextTemp = "Remaining Animals in data base:\n\n";
		for (int i = 0; i < animalList.size(); i++) {
			try {
				jtaTextTemp += animalList.get(i).getName() + "\n";
				System.out.println(animalList.get(i).getName());
			} catch (Exception e) {
				System.err.println("<Intelligence> FAILED TO ADD '" + animalNameList.get(i) + "' TO LIST");
			}

		}
		MainClass.jta.setText(jtaTextTemp);
	}

}
