package glycon;

import java.io.IOException;

import glycon.utils.LoggingUtil;

public class Glycon {

	public static void main(String[] args) {

		GlyconConfig glyconConfig = null;

		if (args.length > 0) {

			glyconConfig = new GlyconConfig(args);

		} else {

			LoggingUtil.warn("No argument was provided, please use 'java -jar Glycon.jar -file data.txt'");
			exitError();

		}

		GlyconSystem glyconSystem = new GlyconSystem(glyconConfig);

		glyconSystem.startSystem();

	}

	static void exitError() {

		LoggingUtil.warn("Press any key to exit");

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);
	}
}
