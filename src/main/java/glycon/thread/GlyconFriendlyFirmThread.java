package glycon.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.network.RequestURL;
import glycon.object.FriendlyFirm;
import glycon.parser.json.JSONParserFirm;
import glycon.utils.ListUtil;
import glycon.utils.csv.CSVUtilFriendlyFirm;

public class GlyconFriendlyFirmThread implements Runnable {

	private AtomicInteger atomicInt;
	private List<String> rawFirmList;

	public GlyconFriendlyFirmThread(List<String> rawFirmList, AtomicInteger atomicInt) {
		this.rawFirmList = rawFirmList;
		this.atomicInt = atomicInt;
	}

	@Override
	public void run() {

		List<FriendlyFirm> finalFriendlyFirmList = new ArrayList<>();

		rawFirmList.forEach(firmId -> {

			String firmJSON = "NULL";

			int failCount = 0;

			while (firmJSON.contentEquals("NULL") && failCount < 5) {

				firmJSON = new RequestURL().getAllFirmJSON(firmId);
				failCount++;

			}

			FriendlyFirm friendlyFirm = JSONParserFirm.parseFriendlyFirmJSON(firmJSON);

			if (friendlyFirm != null && friendlyFirm.getFirmId().equals(firmId))
				finalFriendlyFirmList.add(friendlyFirm);

			atomicInt.addAndGet(1);

		});

		CSVUtilFriendlyFirm.createCSVFile(ListUtil.sanatizeFriendlyList(finalFriendlyFirmList));

	}

}
