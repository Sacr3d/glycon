package glycon.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.network.RequestURL;
import glycon.object.Firm;
import glycon.object.FirmManager;
import glycon.parser.json.JSONParserFirm;
import glycon.utils.DirEnum;
import glycon.utils.FileUtil;
import glycon.utils.ListUtil;
import glycon.utils.csv.CSVUtilFirm;

public class GlyconFirmThread implements Runnable {

	private List<Firm> primeFirmList;
	private AtomicInteger atomicInt;

	public GlyconFirmThread(List<Firm> firmList, AtomicInteger atomicInt) {

		this.primeFirmList = firmList;
		this.atomicInt = atomicInt;

	}

	private List<FirmManager> basicCrawl(Firm firm, int managersToGet) {

		List<FirmManager> managerList = new ArrayList<>();

		for (int i = 0; i < managersToGet; i += 100) {

			String firmJSON = "NULL";

			int failCount = 0;

			while (firmJSON.contentEquals("NULL") && failCount < 5) {

				firmJSON = new RequestURL().getFirmManagersByRangeJSON(firm.getSecId(), 100, i);
				failCount++;

			}

			managerList.addAll(JSONParserFirm.parseFirmManagerJSON(firmJSON));

		}

		return managerList;

	}

	private List<FirmManager> collectManagersWithDisclosures(Firm firm) {

		String firmJSON = "NULL";

		int failCount = 0;

		while (firmJSON.contentEquals("NULL") && failCount < 5) {

			firmJSON = new RequestURL().probeFirmHits(firm.getSecId());
			failCount++;

		}

		int managersToGet = JSONParserFirm.parseFirmHits(firmJSON);

		if (managersToGet > 0 && managersToGet < 9000) {

			return basicCrawl(firm, managersToGet);

		} else if (managersToGet > 9000) {

			return optimisticCrawl(firm);

		}

		return Collections.emptyList();
	}

	private List<FirmManager> experienceCrawl(Firm firm) {

		List<FirmManager> managerList = new ArrayList<>();

		int startBoundry = 0;
		int endBoundry = 365;

		while (startBoundry < 18250) {

			String firmJSON = "NULL";

			int failCount = 0;

			while (firmJSON.contentEquals("NULL") && failCount < 5) {

				firmJSON = getFirmExpirenceFirms(firm, startBoundry, endBoundry);

				failCount++;
			}

			int managersToGet = JSONParserFirm.parseFirmHits(firmJSON);

			for (int i = 0; i < managersToGet; i += 100) {

				managerList.addAll(
						JSONParserFirm.parseFirmManagerJSON(getFirmExpirenceFirms(firm, startBoundry, endBoundry)));

			}

			startBoundry = endBoundry;
			endBoundry += 365;

		}

		return managerList;
	}

	private String getFirmExpirenceFirms(Firm firm, int startBoundry, int endBoundry) {
		return new RequestURL().getFirmManagersByRangeAndExpirienceJSON(firm.getSecId(), 100, 1, startBoundry,
				startBoundry != 18250 ? Integer.toString(endBoundry - 1) : "*");
	}

	private List<FirmManager> optimisticCrawl(Firm firm) {

		List<FirmManager> firmManagerList = new ArrayList<>();

		firmManagerList.addAll(ListUtil.removeDuplicates(firmManagerList, new ArrayList<>(experienceCrawl(firm))));

		return firmManagerList;

	}

	@Override
	public void run() {
		List<FirmManager> managersDisclosureList = new ArrayList<>();

		primeFirmList.forEach(firm -> {

			if (!FileUtil.fileExists(DirEnum.FIRM_PATH.toString() + File.separatorChar + firm.getFirmId() + ".csv")) {

				List<FirmManager> managersWithDisclosuresList = collectManagersWithDisclosures(firm);

				managersDisclosureList.addAll(managersWithDisclosuresList);

				if (!managersDisclosureList.isEmpty())
					CSVUtilFirm.createCSVFirmFile(managersDisclosureList, firm);

				atomicInt.addAndGet(1);
			}
		});

	}

}
