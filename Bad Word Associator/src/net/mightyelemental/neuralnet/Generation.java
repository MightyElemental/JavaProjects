package net.mightyelemental.neuralnet;

import java.util.Arrays;
import java.util.Comparator;

public class Generation {

	public Instance[] instances = new Instance[64];

	public String word;

	public Generation() {

		word = (String) WordManager.words.keySet().toArray()[Main.genNumber % WordManager.words.size()];
		for (int i = 0; i < instances.length; i++) {
			instances[i] = new Instance(word).mutate();
			// instances[i].word = rand.randKeyFromMap(WordManager.words);
		}
	}

	BetterRandom rand = new BetterRandom();

	public Generation(Instance[] seeds) {
		word = (String) WordManager.words.keySet().toArray()[Main.genNumber % WordManager.words.size()];
		System.out.println(WordManager.getType(word));
		int count = (int) Math.ceil(instances.length / 20.0);
		for (int i = 0; i < instances.length; i++) {
			instances[i] = new Instance(seeds[i % count].getNodes(), word).mutate();
			// instances[i].word = rand.randKeyFromMap(WordManager.words);
		}
	}

	public Instance[] getSeeds() {
		Arrays.sort(instances, Comparator.comparing((Instance a) -> a.getFitness()).reversed());
		int count = (int) Math.ceil(instances.length / 20.0);
		Instance[] result = new Instance[count];
		for (int i = 0; i < count; i++) {
			int val = rand.nextIntTendToward(0, (int) (instances.length / 6f));
			result[i] = instances[val];
		}
		return result;
	}

}
