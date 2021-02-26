package glycon.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.network.RequestURL;
import glycon.object.FriendlyFirm;
import glycon.parser.JSONParser;

public class GlyconFriendlyFirmThread implements Callable<List<FriendlyFirm>> {

	private List<String> rawFirmList;
	private AtomicInteger atomicInt;

	public GlyconFriendlyFirmThread(List<String> rawFirmList, AtomicInteger atomicInt) {
		this.rawFirmList = rawFirmList;
		this.atomicInt = atomicInt;
	}

	public List<FriendlyFirm> call() throws Exception {

		List<FriendlyFirm> finalFriendlyFirmList = new ArrayList<>();

		rawFirmList.forEach(firmId -> {

			FriendlyFirm friendlyFirm = JSONParser.parseFirmJSON(RequestURL.getFirmJSON(firmId));

			finalFriendlyFirmList.add(friendlyFirm);

			atomicInt.addAndGet(1);

		});

		return finalFriendlyFirmList;
	}

}
