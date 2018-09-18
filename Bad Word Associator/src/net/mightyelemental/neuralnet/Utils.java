package net.mightyelemental.neuralnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {

	public static <T> T deepCopy(T orig) {
		T obj = null;
		try {
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(orig);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			obj = (T) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

	public static <T> T[] deepCopyArray(T[] array) {
		T[] result = array.clone();
		System.arraycopy(array, 0, result, 0, array.length);
		for ( int i = 0; i < array.length; i++ ) {
			result[i] = deepCopy(array[i]);
		}
		return result;
	}

}
