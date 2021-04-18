package glycon.utils.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import glycon.object.FirmManager;
import glycon.utils.DirEnum;

public class CSVUtilManagers {

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

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(CSVUtil.FINAL_MANAGER_HEADERS))) {

			int longestEntry = getLongestEntry(firmManager);

			for (int i = 0; i < longestEntry; i++) {

				String finraDisclosureDate = i < firmManager.getDiscolsuresFinra().size()
						? firmManager.getDiscolsuresFinra().get(i).getEventDate()
						: "";

				String finraDisclosureType = i < firmManager.getDiscolsuresFinra().size()
						? firmManager.getDiscolsuresFinra().get(i).getDisclosureType()
						: "";

				String finraDisclosureResult = i < firmManager.getDiscolsuresFinra().size()
						? firmManager.getDiscolsuresFinra().get(i).getDisclosureResolution()
						: "";

				String finraDisclosureInfo = i < firmManager.getDiscolsuresFinra().size()
						? firmManager.getDiscolsuresFinra().get(i).getDisclosureDetailString()
						: "";

				String secDisclosureDate = i < firmManager.getDiscolsuresSEC().size()
						? firmManager.getDiscolsuresSEC().get(i).getEventDate()
						: "";

				String secDisclosureType = i < firmManager.getDiscolsuresSEC().size()
						? firmManager.getDiscolsuresSEC().get(i).getDisclosureType()
						: "";

				String secDisclosureResult = i < firmManager.getDiscolsuresSEC().size()
						? firmManager.getDiscolsuresSEC().get(i).getDisclosureResolution()
						: "";

				String secDisclosureInfo = i < firmManager.getDiscolsuresSEC().size()
						? firmManager.getDiscolsuresSEC().get(i).getDisclosureDetailString()
						: "";

				String finraEmploymentDate = "";

				finraEmploymentDate = decideFinraEmploymentDate(firmManager, i);

				String finraFirm = "";

				finraFirm = decideFinraEmploymentFirm(firmManager, i);

				String secEmploymentDate = "";

				secEmploymentDate = decideSECEmploymentDate(firmManager, i);

				String secFirm = "";

				secFirm = decideSECEmploymentFirm(firmManager, i);

				printer.printRecord(firmManager.getMostRecentFirmId(), firmManager.getInd_source_id(),
						firmManager.getInd_firstname(), Arrays.toString(firmManager.getInd_other_names()),
						firmManager.getInd_lastname(), finraDisclosureDate, finraDisclosureType, finraDisclosureResult,
						finraDisclosureInfo, secDisclosureDate, secDisclosureType, secDisclosureResult,
						secDisclosureInfo, finraEmploymentDate, finraFirm, secEmploymentDate, secFirm);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String decideSECEmploymentFirm(FirmManager firmManager, int i) {
		if (i < firmManager.getCurrentMangerIAEmployments().size()) {

			return (i < firmManager.getCurrentMangerIAEmployments().size())
					&& (!firmManager.getCurrentMangerIAEmployments().isEmpty())
							? firmManager.getCurrentMangerIAEmployments().get(i).getDetailedFirmName()
							: "";

		}

		return (i < firmManager.getPreviousMangerIAEmployments().size()
				+ firmManager.getCurrentMangerIAEmployments().size())
				&& (!firmManager.getPreviousMangerIAEmployments().isEmpty())
						? firmManager.getPreviousMangerIAEmployments()
								.get(i - firmManager.getCurrentMangerIAEmployments().size()).getDetailedFirmName()
						: "";
	}

	private static String decideSECEmploymentDate(FirmManager firmManager, int i) {
		if (i < firmManager.getCurrentMangerIAEmployments().size()) {

			return (i < firmManager.getCurrentMangerIAEmployments().size())
					&& (!firmManager.getCurrentMangerIAEmployments().isEmpty())
							? firmManager.getCurrentMangerIAEmployments().get(i).getDateRange()
							: "";

		}

		return (i < firmManager.getPreviousMangerIAEmployments().size()
				+ firmManager.getCurrentMangerIAEmployments().size())
				&& (!firmManager.getPreviousMangerIAEmployments().isEmpty())
						? firmManager.getPreviousMangerIAEmployments()
								.get(i - firmManager.getCurrentMangerIAEmployments().size()).getDateRange()
						: "";
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

	private static int getLongestEntry(FirmManager firmManager) {

		int longestEntry = firmManager.getDiscolsuresFinra().size();

		if (firmManager.getCurrentMangerEmployments().size()
				+ firmManager.getPreviousMangerEmployments().size() > longestEntry)
			longestEntry = firmManager.getCurrentMangerEmployments().size()
					+ firmManager.getPreviousMangerEmployments().size();

		if (firmManager.getCurrentMangerIAEmployments().size()
				+ firmManager.getPreviousMangerIAEmployments().size() > longestEntry)
			longestEntry = firmManager.getCurrentMangerIAEmployments().size()
					+ firmManager.getPreviousMangerIAEmployments().size();

		return longestEntry;
	}

}
