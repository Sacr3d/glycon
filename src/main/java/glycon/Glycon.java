package glycon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.object.Firm;
import glycon.object.FriendlyFirm;
import glycon.thread.GlyconFirmFileThread;
import glycon.thread.GlyconFirmThread;
import glycon.thread.GlyconFriendlyFirmThread;
import glycon.utils.ASCIIArtUtil;
import glycon.utils.CSVUtil;
import glycon.utils.FileUtil;
import glycon.utils.ListUtil;
import glycon.utils.LoggingUtil;
import glycon.utils.ProgressUtil;

public class Glycon {

	private static final int THREADS = 8;

	public static void createBrokersWithDisclosuresList(List<String> rawFrimList) {

		List<Firm> primeFirmList = CSVUtil.generateFirmInformation();

		List<Firm> workingFirmList = ListUtil.createWorkingFirmList(rawFrimList, primeFirmList);

		List<List<Firm>> threadFirmList = ListUtil.splitList(THREADS, workingFirmList);

		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

		for (List<Firm> firmList : threadFirmList) {

			executorService.submit(new GlyconFirmThread(firmList, atomicInt));

		}

		try {

			executorService.shutdown();

			executorService.awaitTermination(1, TimeUnit.NANOSECONDS);

			ProgressUtil.displayProgressBar(atomicInt, workingFirmList, executorService,
					"Gathering managers from firms");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private static void createFriendlyList(List<String> rawFrimList) {

		List<String> friendlyFirmPrevious = FileUtil.hasFriendlyFirmList() ? CSVUtil.generateFriendlyFirmIdList()
				: null;

		List<Future<List<FriendlyFirm>>> resultList = Collections.synchronizedList(new ArrayList<>());

		List<String> workingFirmList = null;

		if (friendlyFirmPrevious != null) {

			workingFirmList = ListUtil.removeDuplicates(rawFrimList, friendlyFirmPrevious);

		} else {

			workingFirmList = new ArrayList<>(rawFrimList);

		}

		List<List<String>> threadRawFirmList = friendlyFirmPrevious == null ? ListUtil.splitList(THREADS, rawFrimList)
				: ListUtil.splitList(THREADS, workingFirmList);

		runThreadedFriendlyFirmService(workingFirmList, resultList, threadRawFirmList);

		CSVUtil.createCSVFile(ListUtil.sanatizeFriendlyList(resultList));

	}

	private static void exitError() {
		System.exit(0);
	}

	public static void main(String[] args) {

		if (args.length == 2) {

			try {

				List<String> rawFrimList = FileUtil.parseFirmsInTextFile(args[1]);

				printWelcome(args[1]);

				createFriendlyList(rawFrimList);

				createBrokersWithDisclosuresList(rawFrimList);
				
				workOnFirmBrokerList(rawFrimList);

			} catch (IOException e) {
				LoggingUtil.warn(args[1] + " could not be accesed or does not exist");
				exitError();
			}

		} else {

			LoggingUtil.warn("No argument was provided, please use 'java -jar Glycon.jar -file data.txt'");
			exitError();
		}

	}

	public static void workOnFirmBrokerList(List<String> rawFrimList) {

		List<File> primeFirmFileList = ListUtil.generateFileInformation(rawFrimList);
		
		if (!primeFirmFileList.isEmpty()) {
			
			
			List<List<File>> threadFirmFileList = ListUtil.splitList(THREADS, primeFirmFileList);

			AtomicInteger atomicInt = new AtomicInteger(0);

			ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

			for (List<File> firmList : threadFirmFileList) {

				executorService.submit(new GlyconFirmFileThread(firmList, atomicInt));

			}
			
			try {

				executorService.shutdown();

				executorService.awaitTermination(1, TimeUnit.NANOSECONDS);

				ProgressUtil.displayProgressBar(atomicInt, primeFirmFileList, executorService,
						"Gathering information on managers from firms");

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
				
	}

	private static void printWelcome(String fileName) {
		ASCIIArtUtil.drawString("Glycon");

		LoggingUtil.msg("Finra Brokercheck Webscraper");
		LoggingUtil.msg("Mattia Sassone University of Southampton (ms2u19@soton.ac.uk)");

		LoggingUtil.msg("Gathering firms from list: " + fileName);
		LoggingUtil.msg("With Threads: " + THREADS);
	}

	private static void runThreadedFriendlyFirmService(List<String> workingFirmList,
			List<Future<List<FriendlyFirm>>> resultList, List<List<String>> threadRawFirmList) {

		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

		for (List<String> rawFirmList : threadRawFirmList) {

			resultList.add(executorService.submit(new GlyconFriendlyFirmThread(rawFirmList, atomicInt)));

		}

		try {

			executorService.shutdown();

			executorService.awaitTermination(1, TimeUnit.NANOSECONDS);

			ProgressUtil.displayProgressBar(atomicInt, workingFirmList, executorService,
					"Creating friendly names for firms");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
