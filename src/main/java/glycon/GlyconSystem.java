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
import java.util.stream.Collectors;

import glycon.object.Firm;
import glycon.object.FriendlyFirm;
import glycon.thread.GlyconFirmFileThread;
import glycon.thread.GlyconFirmThread;
import glycon.thread.GlyconFriendlyFirmThread;
import glycon.utils.ASCIIArtUtil;
import glycon.utils.DirEnum;
import glycon.utils.FileUtil;
import glycon.utils.ListUtil;
import glycon.utils.LoggingUtil;
import glycon.utils.ProgressUtil;
import glycon.utils.csv.CSVUtil;

public class GlyconSystem {

	private GlyconConfig glyconConfig;

	public GlyconSystem(GlyconConfig glyconConfig) {
		this.setGlyconConfig(glyconConfig);
	}

	void printWelcome() {

		ASCIIArtUtil.drawString("Glycon");

		LoggingUtil.msg("Finra Brokercheck Webscraper");
		LoggingUtil.msg("Mattia Sassone University of Southampton (ms2u19@soton.ac.uk)");

		if (glyconConfig.getMode() == 'f')
			LoggingUtil.msg("Gathering firms from list: " + glyconConfig.getFileName());
		else if (glyconConfig.getMode() == 'a')
			LoggingUtil.msg("Trying to gather all firms incrementally");

		LoggingUtil.msg("With threads " + glyconConfig.getThreads());

	}

	void packFinalList(List<String> rawFrimList) {

		List<File> finalFirmFileList = FileUtil.generateFileInformation(rawFrimList);

		List<File> finalManagerFileList = new ArrayList<>();

		for (File firmFile : finalFirmFileList) {

			CSVUtil.generateManagerInformation(firmFile).forEach(firm ->

			finalManagerFileList.add(new File(DirEnum.MANAGER_PATH.toString() + firm.getInd_source_id() + ".csv")));

		}

		List<File> uniqueManagerFiles = finalManagerFileList.stream().distinct().collect(Collectors.toList());

		Collections.sort(uniqueManagerFiles);

		CSVUtil.createFinalList(uniqueManagerFiles);

	}

	void createDirectories() {
		try {

			FileUtil.createRequiredDirectories();

		} catch (IOException e) {

			LoggingUtil.warn("Could not create required directories, need access");

			Glycon.exitError();

		}
	}

	void workOnFirmBrokerList(List<String> rawFrimList) {

		List<File> primeFirmFileList = FileUtil.generateFileInformation(rawFrimList);

		if (!primeFirmFileList.isEmpty()) {

			List<List<File>> threadFirmFileList = ListUtil.splitList(glyconConfig.getThreads(), primeFirmFileList);

			AtomicInteger atomicInt = new AtomicInteger(0);

			ExecutorService executorService = Executors.newFixedThreadPool(glyconConfig.getThreads());

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

	void runThreadedFriendlyFirmService(List<String> workingFirmList, List<Future<List<FriendlyFirm>>> resultList,
			List<List<String>> threadRawFirmList) {

		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executorService = Executors.newFixedThreadPool(glyconConfig.getThreads());

		for (List<String> rawFirmList : threadRawFirmList) {

			resultList.add(executorService.submit(new GlyconFriendlyFirmThread(rawFirmList, atomicInt)));

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

	void createBrokersWithDisclosuresList(List<String> rawFrimList) {

		List<Firm> primeFirmList = CSVUtil.generateFirmInformation();

		List<Firm> workingFirmList = ListUtil.createWorkingFirmList(rawFrimList, primeFirmList);

		List<List<Firm>> threadFirmList = ListUtil.splitList(glyconConfig.getThreads(), workingFirmList);

		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executorService = Executors.newFixedThreadPool(glyconConfig.getThreads());

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

	void createFriendlyList(List<String> rawFrimList) {

		List<String> friendlyFirmPrevious = FileUtil.hasFriendlyFirmList() ? CSVUtil.generateFriendlyFirmIdList()
				: null;

		List<Future<List<FriendlyFirm>>> resultList = Collections.synchronizedList(new ArrayList<>());

		List<String> workingFirmList = null;

		if (friendlyFirmPrevious != null) {

			workingFirmList = ListUtil.removeDuplicates(rawFrimList, friendlyFirmPrevious);

		} else {

			workingFirmList = new ArrayList<>(rawFrimList);

		}

		List<List<String>> threadRawFirmList = friendlyFirmPrevious == null
				? ListUtil.splitList(glyconConfig.getThreads(), rawFrimList)
				: ListUtil.splitList(glyconConfig.getThreads(), workingFirmList);

		runThreadedFriendlyFirmService(workingFirmList, resultList, threadRawFirmList);

		CSVUtil.createCSVFile(ListUtil.sanatizeFriendlyList(resultList));

	}

	public GlyconConfig getGlyconConfig() {
		return glyconConfig;
	}

	public void setGlyconConfig(GlyconConfig glyconConfig) {
		this.glyconConfig = glyconConfig;
	}

	public void startSystem() {

		printWelcome();

		createDirectories();

		if (glyconConfig.getMode() == 'f') {

			try {

				List<String> rawFrimList = FileUtil.parseFirmsInTextFile(glyconConfig.getFileName());

				createFriendlyList(rawFrimList);

				createBrokersWithDisclosuresList(rawFrimList);

				workOnFirmBrokerList(rawFrimList);

				packFinalList(rawFrimList);

			} catch (IOException e) {

				LoggingUtil.warn(glyconConfig.getFileName() + " could not be accesed or does not exist");

				Glycon.exitError();
			}

		} else if (glyconConfig.getMode() == 'a') {

		} else {

			LoggingUtil.warn("Could not find mode: " + glyconConfig.getMode());

			Glycon.exitError();

		}

		try {

			LoggingUtil.msg("Done press any key to escape");

			System.in.read();

		} catch (IOException e) {
			LoggingUtil.warn("Input error exiting");

			Glycon.exitError();
		}

	}

}
