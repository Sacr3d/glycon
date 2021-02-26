package glycon.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.network.RequestURL;
import glycon.object.Firm;
import glycon.parser.JSONParser;

public class GlyconFirmThread implements Callable<List<Firm>> {

	private List<Firm> primeFirmList;
	private AtomicInteger atomicInt;

	public GlyconFirmThread(List<Firm> firmList, AtomicInteger atomicInt) {
		this.primeFirmList = firmList;
		this.atomicInt = atomicInt;
	}

	public List<Firm> call() throws Exception {

		List<Firm> test = new ArrayList<>();

		primeFirmList.forEach(firm -> {

			Collection<? extends Firm> managersWithDisclosuresList = collectManagersWithDisclosures(firm);

			test.addAll(managersWithDisclosuresList);

			atomicInt.addAndGet(1);

		});

		return test;
	}

	private Collection<? extends Firm> collectManagersWithDisclosures(Firm firm) {

		int managersToGet = JSONParser.parseFirmHits(RequestURL.probeFirmHits(firm.getSecId()));

		if (managersToGet > 0 && managersToGet < 9000) {

			return basicCrawl(firm, managersToGet);

		} else if (managersToGet > 9000) {

			return optimisticCrawl(firm, managersToGet);

		}

		return Collections.emptyList();
	}

	private Collection<? extends Firm> optimisticCrawl(Firm firm, int managersToGet) {
		return primeFirmList;

	}

	private Collection<? extends Firm> basicCrawl(Firm firm, int managersToGet) {
		return primeFirmList;

	}

}
