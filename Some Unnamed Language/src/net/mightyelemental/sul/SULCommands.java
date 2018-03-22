package net.mightyelemental.sul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.mightyelemental.sul.SULExceptions.*;

public class SULCommands {

	private SULCommands() {
	}

	public static Map<String, Object> variables = new HashMap<String, Object>();

	public static void set(List<String> line) {
		if ( !line.get(1).startsWith(":") ) {
			System.err.println("Variable names must start with a ':'");
			return;
		}
		StringBuilder buffer = new StringBuilder();
		line.remove(0);
		boolean isString = false;
		for ( int i = 2; i < line.size(); i++ ) {
			String arg = line.get(i);
			if ( arg.startsWith(":") ) {
				buffer.append(getVarVal(arg).toString().replaceAll("\"", ""));
			} else if ( arg.startsWith("\"") ) {
				buffer.append(arg.replaceAll("\"", ""));
				isString = true;
			} else if ( isNumber(arg) ) {
				buffer.append(arg);
			} else {
				invalidSyntaxException(
						"-Strings must be contained within quotes.\n-Variables must start with a colon.\n-Numbers can only contain the numbers 0-9, '-', and '.'",
						arg);
			}
		}
		if ( isString ) {
			variables.put(line.get(0), "\"" + buffer.toString() + "\"");
		} else {
			variables.put(line.get(0), buffer.toString());
		}
		// System.out.println(variables);
	}

	private static boolean isNumber(String val) {
		try {
			Float.parseFloat(val);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String display(List<String> line) {
		line.remove(0);
		StringBuilder buffer = new StringBuilder();
		for ( String arg : line ) {
			if ( arg.startsWith(":") ) {
				Object val = getVarVal(arg);
				if ( val == null ) {
					varNotSetException(arg);
					return null;
				}
				buffer.append(getVarVal(arg).toString().replaceAll("\"", ""));
			} else if ( arg.startsWith("\"") ) {
				buffer.append(arg.replaceAll("\"", ""));
			} else {
				invalidSyntaxException("Strings must be contained within quotes and variables must start with a colon.", arg);
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}

	public static Object getVarVal(String var) {
		return variables.get(var);
	}

}