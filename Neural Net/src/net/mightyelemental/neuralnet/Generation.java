package net.mightyelemental.neuralnet;

import java.util.Arrays;
import java.util.Comparator;

public class Generation {

	public Instance[] instances = new Instance[45];

	public Generation() {
		for ( int i = 0; i < instances.length; i++ ) {
			instances[i] = new Instance().mutate();
		}
		instances[0].getPongGame().slow = true;
	}

	BetterRandom rand = new BetterRandom();

	public Generation(Instance[] seeds) {
		int count = (int) Math.ceil(instances.length / 10.0);
		for ( int i = 0; i < instances.length; i++ ) {
			instances[i] = new Instance(seeds[i % count].getNodes()).mutate();
		}
		instances[0].getPongGame().slow = true;
	}

	public boolean isComplete() {
		for ( Instance i : instances ) {
			if ( !i.getPongGame().endGame ) { return false; }
		}
		return true;
	}

	public Instance[] getSeeds() {
		Arrays.sort(instances, Comparator.comparing((Instance a) -> a.getFitness()));
		int count = (int) Math.ceil(instances.length / 10.0);
		Instance[] result = new Instance[count];
		for ( int i = 0; i < count; i++ ) {
			int val = rand.nextIntTendToward(0, (int) (instances.length / 2.5f));
			result[i] = instances[val];
		}
		return result;
	}

}
