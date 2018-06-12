package net.mightyelemental.neuralnet;

import java.util.Map;
import java.util.Random;

public class BetterRandom extends Random {

	private static final long serialVersionUID = -8780148215314110260L;

	public <T> T randFromArray(T[] arr) {
		return arr[this.nextInt(arr.length)];
	}

	public <T, K> T randKeyFromMap(Map<T, K> map) {
		return (T) randFromArray(map.keySet().toArray());
	}

	public int nextInt(int min, int max) {
		// System.out.println(min + "|" + max + "|" + (nextDouble() * (max - min) +
		// min));
		return (int) Math.round(nextDouble() * (max - min) + min);
	}

	public double nextBoolInt(double base, double diff) {
		return (nextBoolean() ? 1 : -1) * diff + base;
	}
}
