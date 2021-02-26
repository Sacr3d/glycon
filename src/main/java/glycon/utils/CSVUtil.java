package glycon.utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import glycon.object.Firm;
import glycon.object.FriendlyFirm;

public class CSVUtil {

	private static final String[] HEADERS = { "FIRMID", "FIRMNAME", "SEARCHTERM" };

	public static void createCSVFile(List<FriendlyFirm> resultList) {

		BufferedWriter out = null;

		if (FileUtil.hasFriendlyFirmList()) {
			try {

				out = Files.newBufferedWriter(Paths.get(FileEnum.FRIENDLY_WORKBOOK.toString()),
						StandardOpenOption.APPEND, StandardOpenOption.CREATE);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {

			try {

				out = Files.newBufferedWriter(Paths.get(FileEnum.FRIENDLY_WORKBOOK.toString()),
						StandardOpenOption.CREATE);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(HEADERS))) {
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

	public static List<String> generateFriendlyFirmIdList() {

		List<String> existingFirmIdList = new ArrayList<>();

		try (Reader in = new FileReader(FileEnum.FRIENDLY_WORKBOOK.toString())) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader().parse(in);
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
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader().parse(in);
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
