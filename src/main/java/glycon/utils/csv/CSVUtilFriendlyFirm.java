package glycon.utils.csv;

import java.io.BufferedWriter;
import java.io.FileReader;
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

import glycon.object.FriendlyFirm;
import glycon.utils.FileEnum;
import glycon.utils.FileUtil;

public class CSVUtilFriendlyFirm {

	public static synchronized void createCSVFile(List<FriendlyFirm> resultList) {

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
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(CSVTermsEnum.getFriendlyHeaders()))) {
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
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(CSVTermsEnum.getFriendlyHeaders())
					.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) {
				String firmId = record.get(CSVTermsEnum.FIRM_ID.toString());

				existingFirmIdList.add(firmId);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return existingFirmIdList;
	}
	
	private CSVUtilFriendlyFirm() {
		throw new IllegalStateException("Utility class");
	}

}
