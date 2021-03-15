package glycon.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtil {

	public static List<String> parseFirmsInTextFile(String fileName) throws IOException {

		return Files.readAllLines(Paths.get(fileName));

	}

	public static boolean hasFriendlyFirmList() {

		return new File(FileEnum.FRIENDLY_WORKBOOK.toString()).exists();

	}
	
	public static boolean fileExists(String string) {

		return new File(string).exists();

	}

}
