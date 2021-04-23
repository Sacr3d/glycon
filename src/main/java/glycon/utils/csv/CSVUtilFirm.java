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
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import glycon.object.Firm;
import glycon.object.FirmManager;
import glycon.utils.DirEnum;
import glycon.utils.FileEnum;

public class CSVUtilFirm {

	public static void createCSVFirmFile(List<FirmManager> resultList, Firm firm) {

		BufferedWriter out = null;

		try {

			out = Files.newBufferedWriter(Paths.get(DirEnum.FIRM_PATH.toString() + firm.getFirmId() + ".csv"),
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

}
