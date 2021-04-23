package glycon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.object.Firm;
import glycon.thread.GlyconFirmFileThread;
import glycon.thread.GlyconFirmThread;
import glycon.thread.GlyconFriendlyFirmThread;
import glycon.utils.FileUtil;
import glycon.utils.ListUtil;
import glycon.utils.ProgressUtil;
import glycon.utils.csv.CSVUtilFirm;
import glycon.utils.csv.CSVUtilFriendlyFirm;

public class GlyconSystemYielder {

	static void createBrokersWithDisclosuresList(List<String> rawFrimList, int threads) {

		List<Firm> primeFirmList = CSVUtilFirm.generateFirmInformation();

		List<Firm> workingFirmList = ListUtil.createWorkingFirmList(rawFrimList, primeFirmList);

		List<List<Firm>> threadFirmList = ListUtil.splitList(threads, workingFirmList);

		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executorService = Executors.newFixedThreadPool(threads);

		for (List<Firm> firmList : threadFirmList) {

			executorService.submit(new GlyconFirmThread(firmList, atomicInt));

		}

		try {

			executorService.shutdown();

			executorService.awaitTermination(1, TimeUnit.NANOSECONDS);

			ProgressUtil.displayProgressBar(atomicInt, workingFirmList, executorService,
					"Gathering managers from firms");

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}

	static void createFriendlyList(List<String> rawFrimList, int threads) {

		List<String> friendlyFirmPrevious = FileUtil.hasFriendlyFirmList()
				? CSVUtilFriendlyFirm.generateFriendlyFirmIdList()
				: null;

		List<String> workingFirmList = null;

		if (friendlyFirmPrevious != null) {

			workingFirmList = ListUtil.removeDuplicates(rawFrimList, friendlyFirmPrevious);

		} else {

			workingFirmList = new ArrayList<>(rawFrimList);

		}

		List<List<String>> threadRawFirmList = friendlyFirmPrevious == null ? ListUtil.splitList(threads, rawFrimList)
				: ListUtil.splitList(threads, workingFirmList);

		runThreadedFriendlyFirmService(workingFirmList, threadRawFirmList, threads);

	}

	static void runThreadedFriendlyFirmService(List<String> workingFirmList, List<List<String>> threadRawFirmList,
			int threads) {

		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executorService = Executors.newFixedThreadPool(threads);

		for (List<String> rawFirmList : threadRawFirmList) {

			executorService.submit(new GlyconFriendlyFirmThread(rawFirmList, atomicInt));

		}

		try {

			executorService.shutdown();

			executorService.awaitTermination(1, TimeUnit.NANOSECONDS);

			ProgressUtil.displayProgressBar(atomicInt, workingFirmList, executorService,
					"Creating friendly names for firms");

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	static void workOnFirmBrokerList(List<String> rawFrimList, int threads) {

		List<File> primeFirmFileList = FileUtil.generateFileInformation(rawFrimList);

		if (!primeFirmFileList.isEmpty()) {

			List<List<File>> threadFirmFileList = ListUtil.splitList(threads, primeFirmFileList);

			AtomicInteger atomicInt = new AtomicInteger(0);

			ExecutorService executorService = Executors.newFixedThreadPool(threads);

			for (List<File> firmList : threadFirmFileList) {

				executorService.submit(new GlyconFirmFileThread(firmList, atomicInt));

			}

			try {

				executorService.shutdown();

				executorService.awaitTermination(1, TimeUnit.NANOSECONDS);

				ProgressUtil.displayProgressBar(atomicInt, primeFirmFileList, executorService,
						"Gathering information on managers from firms");

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}

	}

	private GlyconSystemYielder() {
		throw new IllegalStateException("Utility class");
	}

}
