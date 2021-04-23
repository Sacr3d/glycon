package glycon;

import java.util.Arrays;
import java.util.List;

import glycon.utils.LoggingUtil;

public class GlyconConfig {

	public static boolean isParsable(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (final NumberFormatException e) {
			return false;
		}
	}

	private char mode;

	private boolean skipFirendly = false;

	private String fileName;

	private int threads = 8;

	public GlyconConfig(String[] args) {

		List<String> argsList = Arrays.asList(args);

		if (validateArguments(argsList)) {

			try {

				parseArgs(argsList);

			} catch (ArrayIndexOutOfBoundsException e) {

				LoggingUtil.warn("Argument error");

				Glycon.exitError();

			}

		} else {

			Glycon.exitError();

		}

	}

	public String getFileName() {
		return fileName;
	}

	public char getMode() {
		return mode;
	}

	public int getThreads() {
		return threads;
	}

	private boolean isValidArgument(String arg) {

		if (arg.charAt(0) == '-') {

			for (argsEnum argument : argsEnum.values()) {

				if (argument.getArgs().equalsIgnoreCase(arg))

					return true;

			}

			return false;
		}

		return true;
	}

	private void parseArgs(List<String> argsList) throws ArrayIndexOutOfBoundsException {

		boolean fileOverride = false;

		for (int i = 0; i < argsList.size(); i++) {

			switch (argsList.get(i).toLowerCase()) {
			case "-af":

				fileOverride = true;

				break;

			case "-f":

				fileName = argsList.get(i + 1);

				break;

			case "-as":

				fileOverride = true;

				skipFirendly = true;

				break;

			case "-t":

				if (isParsable(argsList.get(i + 1))) {

					threads = Integer.parseInt(argsList.get(i + 1));

				}

				break;

			default:
				break;
			}

		}

		if (fileOverride) {
			if (skipFirendly)
				mode = 's';
			else
				mode = 'a';

			threads = 8;

		} else
			mode = 'f';

	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setMode(char mode) {
		this.mode = mode;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	private boolean validateArguments(List<String> argsList) {
		for (String arg : argsList) {

			if (!isValidArgument(arg)) {

				LoggingUtil.warn("Invalid argument provided: " + arg);

				return false;
			}

		}
		return true;
	}

	public boolean isSkipFirendly() {
		return skipFirendly;
	}

	public void setSkipFirendly(boolean skipFirendly) {
		this.skipFirendly = skipFirendly;
	}

}
