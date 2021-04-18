package glycon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import glycon.utils.FileUtil;
import glycon.utils.ListUtil;

class GlyconTest {

	@Test
	void test() {

		final String ID = "ID";
		final String SECOND_NAME = "SECOND_NAME";
		final String OTHER_NAMES = "OTHER_NAMES";
		final String FIRST_NAME = "FIRST_NAME";
		final String FIRM_ID = "FIRM_ID";

		final String[] MANAGER_HEADERS = { ID, FIRST_NAME, OTHER_NAMES, SECOND_NAME };

		final String[] FINAL_MANAGER_HEADERS = { FIRM_ID, ID, FIRST_NAME, OTHER_NAMES, SECOND_NAME, "DISCLOSURE_DATE",
				"DISCLOSURE_TYPE", "DISCLOSURE_RESULT", "DISCLOSURE_INFO", "FINRA_EMPLOYMENT_DATES",
				"FINRA_EMPLOYMENT" };

		String headers = Arrays.toString(FINAL_MANAGER_HEADERS);

		List<File> listOfFilesToBeMerged = FileUtil.getAllManagerFiles();

		Iterator<File> iterFiles = listOfFilesToBeMerged.iterator();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("final-List.csv", true))) {
			writer.write(headers.substring(1, headers.length() - 1));
			writer.newLine();

			while (iterFiles.hasNext()) {
				File nextFile = iterFiles.next();
				BufferedReader reader = new BufferedReader(new FileReader(nextFile));

				String line = null;
				String[] firstLine = null;
				if ((line = reader.readLine()) != null)
					firstLine = line.split(",");

				while ((line = reader.readLine()) != null) {
					writer.write(line);
					writer.newLine();
				}

				reader.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	@Test
//	void test() {
//
//		List<String> testList = new ArrayList<>();
//
//		testList.add("759");
//
//		Glycon.workOnFirmBrokerList(testList);
//
//	}

}
