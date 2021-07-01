package glycon.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static void createRequiredDirectories() throws IOException {

		for (DirEnum filePath : DirEnum.values()) {

			try {

				Files.createDirectories(Paths.get(filePath.toString()));

			} catch (IOException e) {

				throw new IOException();

			}

		}

	}

	public static boolean fileExists(String string) {

		return new File(string).exists();

	}

	public static <E> List<File> generateFileInformation(List<E> rawFrimList) {

		List<File> requiredFilesList = new ArrayList<>();

		for (E fileName : rawFrimList) {

			File f = new File(DirEnum.FIRM_MANAGERS_PATH.toString() + fileName + ".csv");

			if (f.exists() && !f.isDirectory()) {

				requiredFilesList.add(f);

			}
		}

		return requiredFilesList;
	}

	public static List<File> getAllFirmFiles() {

		List<File> requiredFilesList = new ArrayList<>();

		// Creates an array in which we will store the names of files and directories
		String[] pathnames;

		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(DirEnum.FIRM_MANAGERS_PATH.toString());

		// Populates the array with names of files and directories
		pathnames = f.list();

		// For each pathname in the pathnames array
		for (String pathname : pathnames) {
			// Print the names of files and directories
			requiredFilesList.add(new File(DirEnum.FIRM_MANAGERS_PATH.toString() + pathname));
		}

		return requiredFilesList;
	}

	public static List<String> getAllManagerFiles() {

		List<String> requiredFilesList = new ArrayList<>();

		// Creates an array in which we will store the names of files and directories
		String[] pathnames;

		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(DirEnum.MANAGER_DISCLOSURE_PATH.toString());

		// Populates the array with names of files and directories
		pathnames = f.list();

		// For each pathname in the pathnames array
		for (String pathname : pathnames) {
			// Print the names of files and directories
			requiredFilesList.add(new File(DirEnum.MANAGER_DISCLOSURE_PATH.toString() + pathname).getAbsolutePath());
		}

		return requiredFilesList;
	}

	public static boolean hasFriendlyFirmList() {

		return new File(FileEnum.FRIENDLY_WORKBOOK.toString()).exists();

	}

	public static List<String> parseFirmsInTextFile(String fileName) throws IOException {

		return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

	}

	private FileUtil() {
		throw new IllegalStateException("Utility class");
	}

}
