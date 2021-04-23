package glycon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import glycon.utils.ASCIIArtUtil;
import glycon.utils.DirEnum;
import glycon.utils.FileUtil;
import glycon.utils.LoggingUtil;
import glycon.utils.csv.CSVUtil;
import glycon.utils.csv.CSVUtilFriendlyFirm;
import glycon.utils.csv.CSVUtilManager;

public class GlyconSystem {

	private GlyconConfig glyconConfig;

	public GlyconSystem(GlyconConfig glyconConfig) {
		this.setGlyconConfig(glyconConfig);
	}

	private void acceptTerms() throws IOException {

		LoggingUtil.msg("You are about to SCRAPE THE WHOLE FINRA AND SEC DATABASE");
		LoggingUtil.msg("Results may be long and unpridictable");
		LoggingUtil.msg("ARE YOU SURE YOU WOULD LIKE TO DO THIS?");
		LoggingUtil.msg("YES (Y)");

		if ((System.in.read() != 'Y'))
			Glycon.exitError();

		LoggingUtil.msg("YOU MUST NOT USE THIS PROGRAM FOR COMMERCIAL USE");
		LoggingUtil.msg("PLEASE PROVIDE AN EMAIL TO ATTACH TO REQUEST HEADERS");

	}

	void createDirectories() {
		try {

			FileUtil.createRequiredDirectories();

		} catch (IOException e) {

			LoggingUtil.warn("Could not create required directories, need access");

			Glycon.exitError();

		}
	}

	public GlyconConfig getGlyconConfig() {
		return glyconConfig;
	}

	void packFinalList(List<String> rawFrimList) {

		List<File> finalFirmFileList = FileUtil.generateFileInformation(rawFrimList);

		List<File> finalManagerFileList = new ArrayList<>();

		for (File firmFile : finalFirmFileList) {

			CSVUtilManager.generateManagerInformation(firmFile).forEach(firm ->

			finalManagerFileList.add(new File(DirEnum.MANAGER_PATH.toString() + firm.getInd_source_id() + ".csv")));

		}

		List<File> uniqueManagerFiles = finalManagerFileList.stream().distinct().collect(Collectors.toList());

		Collections.sort(uniqueManagerFiles);

		CSVUtil.createFinalList(uniqueManagerFiles);

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

	public void setGlyconConfig(GlyconConfig glyconConfig) {
		this.glyconConfig = glyconConfig;
	}

	public void startSystem() {

		printWelcome();

		createDirectories();

		List<String> rawFrimList = null;

		switch (glyconConfig.getMode()) {
		case 'f':
			try {

				rawFrimList = FileUtil.parseFirmsInTextFile(glyconConfig.getFileName());

				runCoreComponents(rawFrimList);

				packFinalList(rawFrimList);

			} catch (IOException e) {

				LoggingUtil.warn(glyconConfig.getFileName() + " could not be accesed or does not exist");

				Glycon.exitError();
			}

			break;
		case 'a':
			try {
				acceptTerms();

			} catch (IOException e) {

				LoggingUtil.warn("Input error exiting");

				Glycon.exitError();
			}

			rawFrimList = IntStream.range(1, 9999999).boxed().map(n -> n.toString()).collect(Collectors.toList());

			runCoreComponents(rawFrimList);

			packFinalList(FileUtil.getAllManagerFiles());

			break;
		case 's':

			rawFrimList = CSVUtilFriendlyFirm.generateFriendlyFirmIdList();

			runCoreComponents(rawFrimList);

			packFinalList(rawFrimList);

			break;

		default:

			LoggingUtil.warn("Could not find mode: " + glyconConfig.getMode());

			Glycon.exitError();

			break;
		}

		try {

			LoggingUtil.msg("Done press any key to escape");

			System.in.read();

		} catch (IOException e) {
			LoggingUtil.warn("Input error exiting");

			Glycon.exitError();
		}

	}

	private void runCoreComponents(List<String> rawFrimList) {

		GlyconSystemYielder.createFriendlyList(rawFrimList, glyconConfig.getThreads());

		GlyconSystemYielder.createBrokersWithDisclosuresList(rawFrimList, glyconConfig.getThreads());

		GlyconSystemYielder.workOnFirmBrokerList(rawFrimList, glyconConfig.getThreads());
	}

}
