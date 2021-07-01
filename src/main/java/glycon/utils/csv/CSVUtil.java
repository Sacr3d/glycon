package glycon.utils.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import glycon.object.Firm;
import glycon.object.Manager;
import glycon.utils.DirEnum;


public class CSVUtil {

	public static void createFinalList(List<File> finalManagerList) {

		String headers = createHeaders();

		Iterator<File> iterFiles = finalManagerList.iterator();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(DirEnum.FINAL_PATH.toString()
				+ new SimpleDateFormat("yyyy'-'MM'-'dd'-'HH'-'mm'.csv'").format(new Date()), true))) {
			writer.write(headers);
			writer.newLine();

			while (iterFiles.hasNext()) {

				File nextFile = iterFiles.next();

				try (BufferedReader reader = new BufferedReader(new FileReader(nextFile))) {

					String line = null;

					while ((line = reader.readLine()) != null) {
						if (!headers.equals(line)) {
							writer.write(line);
							writer.newLine();
						}
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String createHeaders() {

		String headers = Arrays.toString(CSVTermsEnum.getFinalManagerHeaders());

		return headers.substring(1, headers.length() - 1).replace(" ", "");

	}

	static int getLongestMangerEntry(Manager firmManager) {

		int longestEntry = firmManager.getDiscolsures().size();

		if (firmManager.getCurrentMangerEmployments().size()
				+ firmManager.getPreviousMangerEmployments().size() > longestEntry)
			longestEntry = firmManager.getCurrentMangerEmployments().size()
					+ firmManager.getPreviousMangerEmployments().size();

		if (firmManager.getExaminations().size() > longestEntry)
			longestEntry = firmManager.getExaminations().size();

		return longestEntry;
	}

	static String[] splitOtherNames(String otherNames) {

		String tempName = otherNames;

		tempName = tempName.replace("[", "").replace("]", "");

		return tempName.split(", ");
	}

	private CSVUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static int getLongestFirmEntry(Firm firm) {

		int longestEntry = firm.getDiscolsures().size();

		if (firm.getBasicInfo().getOtherNames().length > longestEntry)
			longestEntry = firm.getBasicInfo().getOtherNames().length;

		return longestEntry;
	}

}
