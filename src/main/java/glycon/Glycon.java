package glycon;

import java.io.IOException;

import glycon.utils.LoggingUtil;

public class Glycon {

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

	public static void main(String[] args) {

		GlyconConfig glyconConfig = null;

		if (args.length > 0) {

			glyconConfig = new GlyconConfig(args);

		} else {

			LoggingUtil.warn("No argument was provided, please use 'java -jar Glycon.jar -f {firmNameFile}.txt'");
			exitError();

		}

		GlyconSystem glyconSystem = new GlyconSystem(glyconConfig);

		glyconSystem.startSystem();

	}
}
