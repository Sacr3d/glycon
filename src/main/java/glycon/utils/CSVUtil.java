package glycon.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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

import glycon.object.Firm;
import glycon.object.FirmManager;
import glycon.object.FriendlyFirm;

public class CSVUtil {

	private static final String[] FRIENDLY_HEADERS = { "FIRMID", "FIRMNAME", "SEARCHTERM" };
	private static final String[] MANAGER_HEADERS = { "ID", "FIRSTNAME", "OTHERNAMES", "SECONDNAME" };

	public static void createCSVFile(List<FriendlyFirm> resultList) {

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

	public static void createCSVFirmFile(List<FirmManager> resultList, Firm firm) {

		BufferedWriter out = null;


	

			try {

				out = Files.newBufferedWriter(Paths.get(FileEnum.FIRM_PATH.toString() + firm.getFirmId()+".csv"), StandardOpenOption.CREATE);

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

	public static List<String> generateFriendlyFirmIdList() {

		List<String> existingFirmIdList = new ArrayList<>();

		try (Reader in = new FileReader(FileEnum.FRIENDLY_WORKBOOK.toString())) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(FRIENDLY_HEADERS).withFirstRecordAsHeader()
					.parse(in);
			for (CSVRecord record : records) {
				String firmId = record.get("FIRMID");

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

	public static List<Firm> generateFirmInformation() {

		List<Firm> firmObjectList = new ArrayList<>();

		try (Reader in = new FileReader(FileEnum.FRIENDLY_WORKBOOK.toString())) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(FRIENDLY_HEADERS).withFirstRecordAsHeader()
					.parse(in);
			for (CSVRecord record : records) {

				String firmId = record.get("FIRMID");
				String secId = record.get("SEARCHTERM");
				String firmName = record.get("FIRMNAME");

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
	
}
