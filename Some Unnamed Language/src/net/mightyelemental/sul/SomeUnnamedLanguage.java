package net.mightyelemental.sul;

import static net.mightyelemental.sul.SULCommands.*;

import java.util.ArrayList;
import java.util.List;

public class SomeUnnamedLanguage {

	// private List<List<String>> linesAndArgs = new ArrayList<List<String>>();

	private SomeUnnamedLanguage() {
		interpretLine(splitLineIntoArgs("set :cool to \"a fabulous language\""));
		//interpretLine(splitLineIntoArgs("display :name"));
		interpretLine(splitLineIntoArgs("set :name to \"Jack\""));
		interpretLine(splitLineIntoArgs("display \"Hello \" :name \". What do you think about this language? I think it is \" :cool"));
	}

	public static void main(String[] args) {
		new SomeUnnamedLanguage();
	}

	private List<String> splitLineIntoArgs(String line) {
		List<String> argList = new ArrayList<String>();
		boolean stringFlag = false;
		char[] chars = line.toCharArray();
		StringBuilder arg = new StringBuilder();
		for ( char c : chars ) {
			if ( c == '"' ) {
				stringFlag = !stringFlag;
			}
			if ( (c != ' ' && !stringFlag) || stringFlag ) {
				arg.append(c);
			}
			if ( !stringFlag && c == ' ' ) {
				argList.add(arg.toString());
				arg.setLength(0);
			}
		}
		if ( arg.length() > 0 ) {
			argList.add(arg.toString());
		}
		// System.err.println(argList);
		return argList;
	}

	private void interpretLine(List<String> line) {
		// System.out.println(line);
		switch (line.get(0)) {
		case "set":
			set(line);
			break;
		case "display":
			display(line);
			break;
		default:
			System.err.println("Command not recognised");
		}
	}

}