package glycon.utils.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import glycon.object.Firm;
import glycon.object.FirmManager;
import glycon.object.FirmManagerIn;
import glycon.object.FriendlyFirm;
import glycon.utils.DirEnum;
import glycon.utils.FileEnum;
import glycon.utils.FileUtil;

public class CSVUtil {

	private static final String ID = "ID";
	private static final String FIRM_NAME = "FIRM_NAME";
	private static final String SEARCH_TERM = "SEARCH_TERM";
	private static final String SECOND_NAME = "SECOND_NAME";
	private static final String OTHER_NAMES = "OTHER_NAMES";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String FIRM_ID = "FIRM_ID";

	private static final String[] FRIENDLY_HEADERS = { FIRM_ID, FIRM_NAME, SEARCH_TERM };
	private static final String[] MANAGER_HEADERS = { ID, FIRST_NAME, OTHER_NAMES, SECOND_NAME };
	static final String[] FINAL_MANAGER_HEADERS = { FIRM_ID, ID, FIRST_NAME, OTHER_NAMES, SECOND_NAME,
			"FINRA_DISCLOSURE_DATE", "FINRA_DISCLOSURE_TYPE", "FINRA_DISCLOSURE_RESULT", "FINRA_DISCLOSURE_INFO",
			"SEC_DISCLOSURE_DATE", "SEC_DISCLOSURE_TYPE", "SEC_DISCLOSURE_RESULT", "SEC_DISCLOSURE_INFO",
			"FINRA_EMPLOYMENT_DATES", "FINRA_EMPLOYMENT", "SEC_EMPLOYMENT_DATES", "SEC_EMPLOYMENT" };

	public static void createFriendlyCSVFile(List<FriendlyFirm> resultList) {

		BufferedWriter out = null;

		if (FileUtil.hasFriendlyFirmList()) {
			try {

				out = Files.newBufferedWriter(Paths.get(FileEnum.FRIENDLY_WORKBOOK.toString()),
						StandardOpenOption.APPEND, StandardOpenOption.CREATE);

				friendlyFirmAppend(resultList, out);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {

			try {

				out = Files.newBufferedWriter(Paths.get(FileEnum.FRIENDLY_WORKBOOK.toString()),
						StandardOpenOption.CREATE);

				friendlyFirmNoAppend(resultList, out);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	public static void createCSVFirmFile(List<FirmManager> resultList, Firm firm) {

		BufferedWriter out = null;

		try {

			out = Files.newBufferedWriter(Paths.get(DirEnum.FIRM_PATH.toString() + firm.getFirmId() + ".csv"),
					StandardOpenOption.CREATE);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(MANAGER_HEADERS))) {
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

	public static void createFinalList(List<File> finalManagerList) {

		String headers = Arrays.toString(FINAL_MANAGER_HEADERS);

		Iterator<File> iterFiles = finalManagerList.iterator();

		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(new SimpleDateFormat("yyyy'-'MM'-'dd'-'HH'-'mm'.csv'").format(new Date()), true))) {
			writer.write(headers.substring(1, headers.length() - 1));
			writer.newLine();

			while (iterFiles.hasNext()) {

				File nextFile = iterFiles.next();

				try (BufferedReader reader = new BufferedReader(new FileReader(nextFile))) {

					String line = null;

					while ((line = reader.readLine()) != null) {
						writer.write(line);
						writer.newLine();
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void friendlyFirmAppend(List<FriendlyFirm> resultList, BufferedWriter out) {
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withAllowDuplicateHeaderNames(false))) {
			resultList.forEach(friendlyfirm -> {
				try {
					printer.printRecord(friendlyfirm.getFirmId(), friendlyfirm.getFirmName(),
							":" + friendlyfirm.getFirmSec());
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

	private static void friendlyFirmNoAppend(List<FriendlyFirm> resultList, BufferedWriter out) {
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(FRIENDLY_HEADERS))) {
			resultList.forEach(friendlyfirm -> {
				try {
					printer.printRecord(friendlyfirm.getFirmId(), friendlyfirm.getFirmName(),
							":" + friendlyfirm.getFirmSec());
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

	public static List<Firm> generateFirmInformation() {

		List<Firm> firmObjectList = new ArrayList<>();

		try (Reader in = new FileReader(FileEnum.FRIENDLY_WORKBOOK.toString())) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(FRIENDLY_HEADERS).withFirstRecordAsHeader()
					.parse(in);
			for (CSVRecord record : records) {

				String firmId = record.get(FIRM_ID);
				String secId = record.get(SEARCH_TERM);
				String firmName = record.get(FIRM_NAME);

				firmObjectList.add(new Firm(firmId, secId.substring(1, secId.length()), firmName));

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return firmObjectList;
	}

	public static List<String> generateFriendlyFirmIdList() {

		List<String> existingFirmIdList = new ArrayList<>();

		try (Reader in = new FileReader(FileEnum.FRIENDLY_WORKBOOK.toString())) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(FRIENDLY_HEADERS).withFirstRecordAsHeader()
					.parse(in);
			for (CSVRecord record : records) {
				String firmId = record.get(FIRM_ID);

				existingFirmIdList.add(firmId);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return existingFirmIdList;
	}

	public static List<FirmManagerIn> generateManagerInformation(File firmFile) {

		List<FirmManagerIn> firmMangerObjectList = new ArrayList<>();

		try (Reader in = new FileReader(firmFile)) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(FRIENDLY_HEADERS).withFirstRecordAsHeader()
					.parse(in);
			for (CSVRecord record : records) {

				String managerId = record.get(ID);
				String firstName = record.get(FIRST_NAME);
				String otherNames = record.get(OTHER_NAMES);
				String lastName = record.get(SECOND_NAME);

				firmMangerObjectList
						.add(new FirmManagerIn(managerId, firstName, splitOtherNames(otherNames), lastName));

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return firmMangerObjectList;
	}

	private static String[] splitOtherNames(String otherNames) {

		String tempName = otherNames;

		tempName = tempName.replace("[", "").replace("]", "");

		return tempName.split(", ");
	}

	private CSVUtil() {
		throw new IllegalStateException("Utility class");
	}

}
