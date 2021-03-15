package glycon.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.network.RequestURL;
import glycon.object.Firm;
import glycon.object.FirmManager;
import glycon.parser.JSONParser;
import glycon.utils.CSVUtil;
import glycon.utils.FileEnum;
import glycon.utils.FileUtil;
import glycon.utils.ListUtil;

public class GlyconFirmThread implements Runnable {

	private List<Firm> primeFirmList;
	private AtomicInteger atomicInt;

	public GlyconFirmThread(List<Firm> firmList, AtomicInteger atomicInt) {
		this.primeFirmList = firmList;
		this.atomicInt = atomicInt;
	}

	private List<FirmManager> collectManagersWithDisclosures(Firm firm) {

		int managersToGet = JSONParser.parseFirmHits(RequestURL.probeFirmHits(firm.getSecId()));

		if (managersToGet > 0 && managersToGet < 9000) {

			return basicCrawl(firm, managersToGet);

		} else if (managersToGet > 9000) {

			return optimisticCrawl(firm, managersToGet);

		}

		return Collections.emptyList();
	}

	private List<FirmManager> optimisticCrawl(Firm firm, int managersToGet) {

		List<FirmManager> firmManagerList = new ArrayList<>();

//		firmManagerList.addAll(basicCrawl(firm, managersToGet));
//
//		firmManagerList.addAll(
//				ListUtil.removeDuplicates(firmManagerList, new ArrayList<>(alphabetCrawl(firm, managersToGet))));

		firmManagerList.addAll(ListUtil.removeDuplicates(firmManagerList, new ArrayList<>(experienceCrawl(firm))));

		return firmManagerList;

	}

	private List<FirmManager> experienceCrawl(Firm firm) {

		List<FirmManager> managerList = new ArrayList<>();

		int startBoundry = 0;
		int endBoundry = 365;

		while (startBoundry < 18250) {

			int managersToGet = JSONParser.parseFirmHits(getFirmExpirenceFirms(firm, startBoundry, endBoundry));

			for (int i = 0; i < managersToGet; i += 100) {

				managerList
						.addAll(JSONParser.parseFirmManagerJSON(getFirmExpirenceFirms(firm, startBoundry, endBoundry)));

			}

			startBoundry = endBoundry;
			endBoundry += 365;

		}

		return managerList;
	}

	private String getFirmExpirenceFirms(Firm firm, int startBoundry, int endBoundry) {
		return RequestURL.getFirmManagersByRangeAndExpirienceJSON(firm.getSecId(), 100, 1, startBoundry,
				startBoundry != 18250 ? Integer.toString(endBoundry - 1) : "*");
	}

	private List<FirmManager> alphabetCrawl(Firm firm, int managersToGet) {

		List<FirmManager> managerList = new ArrayList<>();

		for (int i = 0; i < managersToGet; i += 100) {

			managerList.addAll(JSONParser.parseFirmManagerJSON(
					RequestURL.getFirmManagersByRangeAndAlphabeticalJSON(firm.getSecId(), 100, i)));

		}

		return managerList;

	}

	private List<FirmManager> basicCrawl(Firm firm, int managersToGet) {

		List<FirmManager> managerList = new ArrayList<>();

		for (int i = 0; i < managersToGet; i += 100) {

			managerList.addAll(
					JSONParser.parseFirmManagerJSON(RequestURL.getFirmManagersByRangeJSON(firm.getSecId(), 100, i)));

		}

		return managerList;

	}

	@Override
	public void run() {
		List<FirmManager> test = new ArrayList<>();

		primeFirmList.forEach(firm -> {

			if (!FileUtil
					.fileExists(FileEnum.FIRM_PATH.toString() + File.separatorChar + firm.getFirmId() + ".csv")) {

				List<FirmManager> managersWithDisclosuresList = collectManagersWithDisclosures(firm);

				test.addAll(managersWithDisclosuresList);

				CSVUtil.createCSVFirmFile(test, firm);

				atomicInt.addAndGet(1);
			}
		});

	}

}
