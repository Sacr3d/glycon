package glycon.utils.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import glycon.object.FirmManager;
import glycon.object.FirmManagerIn;
import glycon.utils.DirEnum;

public class CSVUtilManager {

	public static void createManagerFile(FirmManager firmManager) {

		BufferedWriter out = null;

		try {

			out = Files.newBufferedWriter(
					Paths.get(DirEnum.MANAGER_PATH.toString() + firmManager.getInd_source_id() + ".csv"),
					StandardOpenOption.CREATE);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		writeManagerInfo(firmManager, out);

	}

	private static String decideFinraEmploymentDate(FirmManager firmManager, int i) {

		if (i < firmManager.getCurrentMangerEmployments().size()) {

			return (i < firmManager.getCurrentMangerEmployments().size())
					&& (!firmManager.getCurrentMangerEmployments().isEmpty())
							? firmManager.getCurrentMangerEmployments().get(i).getDateRange()
							: "";

		}

		return (i < firmManager.getPreviousMangerEmployments().size()
				+ firmManager.getCurrentMangerEmployments().size())
				&& (!firmManager.getPreviousMangerEmployments().isEmpty())
						? firmManager.getPreviousMangerEmployments()
								.get(i - firmManager.getCurrentMangerEmployments().size()).getDateRange()
						: "";

	}

	private static String decideFinraEmploymentFirm(FirmManager firmManager, int i) {

		if (i < firmManager.getCurrentMangerEmployments().size()) {

			return (i < firmManager.getCurrentMangerEmployments().size())
					&& (!firmManager.getCurrentMangerEmployments().isEmpty())
							? firmManager.getCurrentMangerEmployments().get(i).getDetailedFirmName()
							: "";

		}

		return (i < firmManager.getPreviousMangerEmployments().size()
				+ firmManager.getCurrentMangerEmployments().size())
				&& (!firmManager.getPreviousMangerEmployments().isEmpty())
						? firmManager.getPreviousMangerEmployments()
								.get(i - firmManager.getCurrentMangerEmployments().size()).getDetailedFirmName()
						: "";

	}

	public static List<FirmManagerIn> generateManagerInformation(File firmFile) {

		List<FirmManagerIn> firmMangerObjectList = new ArrayList<>();

		try (Reader in = new FileReader(firmFile)) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(CSVTermsEnum.getFriendlyHeaders())
					.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) {

				String managerId = record.get(CSVTermsEnum.ID);
				String firstName = record.get(CSVTermsEnum.FIRST_NAME);
				String otherNames = record.get(CSVTermsEnum.OTHER_NAMES);
				String lastName = record.get(CSVTermsEnum.SECOND_NAME);

				firmMangerObjectList
						.add(new FirmManagerIn(managerId, firstName, CSVUtil.splitOtherNames(otherNames), lastName));

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return firmMangerObjectList;
	}

	private static String getCategory(FirmManager firmManager, int i) {
		return i < firmManager.getExaminations().size() ? firmManager.getExaminations().get(i).getCategory() : "";
	}

	private static String getDisclosureDate(FirmManager firmManager, int i) {
		return i < firmManager.getDiscolsures().size() ? firmManager.getDiscolsures().get(i).getEventDate() : "";
	}

	private static String getDisclosureInfo(FirmManager firmManager, int i) {
		return i < firmManager.getDiscolsures().size() ? firmManager.getDiscolsures().get(i).getDisclosureDetailString()
				: "";
	}

	private static String getDisclosureResult(FirmManager firmManager, int i) {
		return i < firmManager.getDiscolsures().size() ? firmManager.getDiscolsures().get(i).getDisclosureResolution()
				: "";
	}

	private static String getDisclosureType(FirmManager firmManager, int i) {
		return i < firmManager.getDiscolsures().size() ? firmManager.getDiscolsures().get(i).getDisclosureType() : "";
	}

	private static String getExamCategory(FirmManager firmManager, int i) {
		return i < firmManager.getExaminations().size() ? firmManager.getExaminations().get(i).getExamCategory() : "";
	}

	private static String getExamDateTaken(FirmManager firmManager, int i) {
		return i < firmManager.getExaminations().size() ? firmManager.getExaminations().get(i).getExamTakenDate() : "";
	}

	private static String getExamName(FirmManager firmManager, int i) {
		return i < firmManager.getExaminations().size() ? firmManager.getExaminations().get(i).getExamName() : "";
	}

	private static String getExamScope(FirmManager firmManager, int i) {
		return i < firmManager.getExaminations().size() ? firmManager.getExaminations().get(i).getExamScope() : "";
	}

	private static void writeManagerInfo(FirmManager firmManager, BufferedWriter out) {

		try (CSVPrinter printer = new CSVPrinter(out,
				CSVFormat.EXCEL.withHeader(CSVTermsEnum.getFinalManagerHeaders()))) {

			int longestEntry = CSVUtil.getLongestEntry(firmManager);

			for (int i = 0; i < longestEntry; i++) {

				String disclosureDate = getDisclosureDate(firmManager, i);

				String disclosureType = getDisclosureType(firmManager, i);

				String disclosureResult = getDisclosureResult(firmManager, i);

				String disclosureInfo = getDisclosureInfo(firmManager, i);

				String finraEmploymentDate = "";

				finraEmploymentDate = decideFinraEmploymentDate(firmManager, i);

				String finraFirm = "";

				finraFirm = decideFinraEmploymentFirm(firmManager, i);

				String category = getCategory(firmManager, i);

				String examCategory = getExamCategory(firmManager, i);

				String examName = getExamName(firmManager, i);

				String examTakenDate = getExamDateTaken(firmManager, i);

				String examScope = getExamScope(firmManager, i);

				printer.printRecord(firmManager.getMostRecentFirmId(), firmManager.getInd_source_id(),
						firmManager.getInd_firstname(), Arrays.toString(firmManager.getInd_other_names()),
						firmManager.getInd_lastname(), disclosureDate, disclosureType, disclosureResult, disclosureInfo,
						finraEmploymentDate, finraFirm, category, examCategory, examName, examTakenDate, examScope);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private CSVUtilManager() {
		throw new IllegalStateException("Utility class");
	}

}
