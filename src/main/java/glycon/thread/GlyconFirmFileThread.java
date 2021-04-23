package glycon.thread;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import glycon.object.CurrentEmployment;
import glycon.object.Disclosure;
import glycon.object.Examination;
import glycon.object.FirmManager;
import glycon.object.FirmManagerIn;
import glycon.object.PreviousEmployment;
import glycon.utils.DirEnum;
import glycon.utils.FileUtil;
import glycon.utils.csv.CSVUtilManager;

public class GlyconFirmFileThread implements Runnable {

	private List<File> primeFirmList;
	private AtomicInteger atomicInt;

	public GlyconFirmFileThread(List<File> firmList, AtomicInteger atomicInt) {

		this.primeFirmList = firmList;
		this.atomicInt = atomicInt;

	}

	@Override
	public void run() {

		primeFirmList.forEach(firmFile -> {

			List<FirmManagerIn> firmManagerList = CSVUtilManager.generateManagerInformation(firmFile);

			for (FirmManagerIn firmManager : firmManagerList) {

				if (!FileUtil.fileExists(DirEnum.MANAGER_PATH.toString() + firmManager.getInd_source_id() + ".csv")) {

					firmManager.generateInfo();

					sortObjectData(firmManager);

					CSVUtilManager.createManagerFile(firmManager);

				}
			}

			atomicInt.addAndGet(1);

		});

	}

	private void sortCurrentEmploymentInfo(FirmManager firmManager) {
		List<CurrentEmployment> uniqueCurrentEmployments = firmManager.getCurrentMangerEmployments().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueCurrentEmployments, Collections.reverseOrder());

		firmManager.setCurrentMangerEmployments(uniqueCurrentEmployments);
	}

	private void sortDisclosureInfo(FirmManager firmManager) {
		List<Disclosure> uniqueDisclosures = firmManager.getDiscolsures().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueDisclosures, Collections.reverseOrder());

		firmManager.setDiscolsures(uniqueDisclosures);
	}

	private void sortExaminationInfo(FirmManager firmManager) {
		List<Examination> uniqueExaminations = firmManager.getExaminations().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueExaminations, Collections.reverseOrder());

		firmManager.setExaminations(uniqueExaminations);
	}

	private void sortObjectData(FirmManager firmManager) {

		sortDisclosureInfo(firmManager);

		sortCurrentEmploymentInfo(firmManager);

		sortPreviousEmployment(firmManager);

		sortExaminationInfo(firmManager);

	}

	private void sortPreviousEmployment(FirmManager firmManager) {
		List<PreviousEmployment> uniquePreviousEmployments = firmManager.getPreviousMangerEmployments().stream()
				.distinct().collect(Collectors.toList());

		Collections.sort(uniquePreviousEmployments, Collections.reverseOrder());

		firmManager.setPreviousMangerEmployments(uniquePreviousEmployments);
	}

}
