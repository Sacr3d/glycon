package glycon.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static boolean fileExists(String string) {

		return new File(string).exists();

	}

	public static boolean hasFriendlyFirmList() {

		return new File(FileEnum.FRIENDLY_WORKBOOK.toString()).exists();

	}

	public static List<String> parseFirmsInTextFile(String fileName) throws IOException {

		return Files.readAllLines(Paths.get(fileName));

	}

	public static List<File> getAllManagerFiles() {

		List<File> requiredFilesList = new ArrayList<>();

		// Creates an array in which we will store the names of files and directories
		String[] pathnames;

		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(FileEnum.MANAGER_PATH.toString());

		// Populates the array with names of files and directories
		pathnames = f.list();

		// For each pathname in the pathnames array
		for (String pathname : pathnames) {
			// Print the names of files and directories
			requiredFilesList.add(new File(FileEnum.MANAGER_PATH.toString() + pathname));
		}

		return requiredFilesList;
	}
	
	public static List<File> getAllFirmFiles() {

		List<File> requiredFilesList = new ArrayList<>();

		// Creates an array in which we will store the names of files and directories
		String[] pathnames;

		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(FileEnum.FIRM_PATH.toString());

		// Populates the array with names of files and directories
		pathnames = f.list();

		// For each pathname in the pathnames array
		for (String pathname : pathnames) {
			// Print the names of files and directories
			requiredFilesList.add(new File(FileEnum.FIRM_PATH.toString() + pathname));
		}

		return requiredFilesList;
	}
	

}
