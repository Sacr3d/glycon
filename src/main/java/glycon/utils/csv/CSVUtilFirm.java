package glycon.utils.csv;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import glycon.object.Firm;
import glycon.object.Manager;
import glycon.utils.DirEnum;
import glycon.utils.FileEnum;

public class CSVUtilFirm {

	public static void createCSVFirmFile(List<Manager> resultList, Firm firm) {

		BufferedWriter out = null;

		try {

			out = Files.newBufferedWriter(Paths.get(DirEnum.FIRM_MANAGERS_PATH.toString() + firm.getFirmId() + ".csv"),
					StandardOpenOption.CREATE);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(CSVTermsEnum.getManagerHeaders()))) {
			resultList.forEach(firmManager -> {
				try {
					printer.printRecord(firmManager.getInd_source_id(), firmManager.getInd_firstname(),
							Arrays.toString(firmManager.getInd_other_names()), firmManager.getInd_lastname());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createCSVFirmFileTest(Firm firm) {

		BufferedWriter out = null;

		try {

			out = Files.newBufferedWriter(
					Paths.get(DirEnum.FIRM_DISCLOSURE_PATH.toString() + firm.getFirmId() + ".csv"),
					StandardOpenOption.CREATE);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(CSVTermsEnum.getFirmFinalInfo()))) {

			int longestEntry = CSVUtil.getLongestFirmEntry(firm);

			for (int i = 0; i < longestEntry; i++) {

				String firmCRD = decideFirmCRD(firm, i);

				String firmNames = decideFirmNames(firm, i);

				String firmSEC = decideFirmSEC(firm, i);

				String city = decideCity(firm, i);

				String state = decideState(firm, i);

				String zipcode = decideZipcode(firm, i);

				String otherName = decideOtherName(firm, i);

				String disclosureType = decideDisclosureType(firm, i);

				String disclosureNo = decideDisclosureNo(firm, i);

				String reportingSource = decideReportingSource(firm, i);

				String currentStatus = decideCurrentStatus(firm, i);

				String docketCaseNumber = decideDocketCaseNumber(firm, i);

				String allegations = decideAllegations(firm, i);

				String dateInitiated = decideDateInitiated(firm, i);

				String resolutionDate = decideResolutionDate(firm, i);

				String resolution = decideResolution(firm, i);

				String sanctionDetails = decideSanctionDetails(firm, i);

				printer.printRecord(firmCRD, firmNames, firmSEC, city, state, zipcode, otherName, disclosureType,
						disclosureNo, reportingSource, currentStatus, docketCaseNumber, allegations, dateInitiated,
						resolutionDate, resolution, sanctionDetails);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String decideSanctionDetails(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getSanctionDetails() : "";
	}

	private static String decideResolution(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getResolution() : "";
	}

	private static String decideResolutionDate(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getResolutionDate() : "";
	}

	private static String decideDateInitiated(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getEventDate() : "";
	}

	private static String decideAllegations(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getAllegations() : "";
	}

	private static String decideDocketCaseNumber(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getDocketCaseNumber() : "";
	}

	private static String decideCurrentStatus(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getCurrentStatus() : "";
	}

	private static String decideReportingSource(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getReportingSource() : "";
	}

	private static String decideDisclosureNo(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getDisclosureNo() : "";
	}

	private static String decideDisclosureType(Firm firm, int i) {
		return i < firm.getDiscolsures().size() ? firm.getDiscolsures().get(i).getDisclosureType() : "";
	}

	private static String decideOtherName(Firm firm, int i) {
		return i < firm.getBasicInfo().getOtherNames().length ? firm.getBasicInfo().getOtherNames()[i] : "";
	}

	private static String decideZipcode(Firm firm, int i) {
		return i == 0 ? firm.getFirmLocation().getPostalCode() : "";
	}

	private static String decideState(Firm firm, int i) {
		return i == 0 ? firm.getFirmLocation().getState() : "";
	}

	private static String decideCity(Firm firm, int i) {
		return i == 0 ? firm.getFirmLocation().getCity() : "";
	}

	private static String decideFirmSEC(Firm firm, int i) {
		return i == 0 ? firm.getSecId() : "";
	}

	private static String decideFirmNames(Firm firm, int i) {
		return i == 0 ? firm.getBasicInfo().getFirmName() : "";
	}

	private static String decideFirmCRD(Firm firm, int i) {
		return i == 0 ? firm.getBasicInfo().getFirmId() : "";
	}

	public static List<Firm> generateFirmInformation() {

		List<Firm> firmObjectList = new ArrayList<>();

		try (Reader in = new FileReader(FileEnum.FRIENDLY_WORKBOOK.toString())) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(CSVTermsEnum.getFriendlyHeaders())
					.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) {

				String firmId = record.get(CSVTermsEnum.FIRM_ID.toString());
				String secId = record.get(CSVTermsEnum.SEARCH_TERM.toString());
				String firmName = record.get(CSVTermsEnum.FIRM_NAME.toString());

				firmObjectList.add(new Firm(firmId, secId.substring(1, secId.length()), firmName));

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return firmObjectList;
	}

	private CSVUtilFirm() {
		throw new IllegalStateException("Utility class");
	}

	public static void createFirmFile(Firm firm) {
		// TODO Auto-generated method stub

	}
}
