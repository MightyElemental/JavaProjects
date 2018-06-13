package net.mightyelemental.neuralnet;

public class Generation {

	public Instance[] instances = new Instance[45];

	public Generation() {
		for ( int i = 0; i < instances.length; i++ ) {
			instances[i] = new Instance().mutate();
		}
		instances[0].getPongGame().slow = true;
	}

	public Generation(Instance base) {
		for ( int i = 0; i < instances.length; i++ ) {
			instances[i] = new Instance(base.getNodes()).mutate();
		}
		instances[0].getPongGame().slow = true;
	}

	public boolean isComplete() {
		for ( Instance i : instances ) {
			if ( !i.getPongGame().endGame ) { return false; }
		}
		return true;
	}

	public Instance getBestOfGen() {
		Instance result = null;
		double bestFit = Integer.MIN_VALUE;
		for ( Instance i : instances ) {
			if ( i.getPongGame().getFitness() > bestFit ) {
				result = i;
				bestFit = i.getPongGame().getFitness();
			}
		}
		return result;
	}

}
