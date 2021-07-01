package glycon.thread;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import glycon.object.Disclosure;
import glycon.object.Manager;
import glycon.object.manager.CurrentEmployment;
import glycon.object.manager.Examination;
import glycon.object.manager.ManagerIn;
import glycon.object.manager.PreviousEmployment;
import glycon.utils.DirEnum;
import glycon.utils.FileUtil;
import glycon.utils.csv.CSVUtilManager;

public class GlyconFirmFileThread implements Runnable {

	private AtomicInteger atomicInt;
	private List<File> primeFirmList;

	public GlyconFirmFileThread(List<File> firmList, AtomicInteger atomicInt) {

		this.primeFirmList = firmList;
		this.atomicInt = atomicInt;

	}

	@Override
	public void run() {

		primeFirmList.forEach(firmFile -> {

			List<ManagerIn> firmManagerList = CSVUtilManager.generateManagerInformation(firmFile);

			for (ManagerIn firmManager : firmManagerList) {

				if (!FileUtil.fileExists(DirEnum.MANAGER_DISCLOSURE_PATH.toString() + firmManager.getInd_source_id() + ".csv")) {

					firmManager.generateInfo();

					sortObjectData(firmManager);

					CSVUtilManager.createManagerFile(firmManager);

				}
			}

			atomicInt.addAndGet(1);

		});

	}

	private void sortCurrentEmploymentInfo(Manager firmManager) {
		List<CurrentEmployment> uniqueCurrentEmployments = firmManager.getCurrentMangerEmployments().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueCurrentEmployments, Collections.reverseOrder());

		firmManager.setCurrentMangerEmployments(uniqueCurrentEmployments);
	}

	private void sortDisclosureInfo(Manager firmManager) {
		List<Disclosure> uniqueDisclosures = firmManager.getDiscolsures().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueDisclosures, Collections.reverseOrder());

		firmManager.setDiscolsures(uniqueDisclosures);
	}

	private void sortExaminationInfo(Manager firmManager) {
		List<Examination> uniqueExaminations = firmManager.getExaminations().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueExaminations, Collections.reverseOrder());

		firmManager.setExaminations(uniqueExaminations);
	}

	private void sortObjectData(Manager firmManager) {

		sortDisclosureInfo(firmManager);

		sortCurrentEmploymentInfo(firmManager);

		sortPreviousEmployment(firmManager);

		sortExaminationInfo(firmManager);

	}

	private void sortPreviousEmployment(Manager firmManager) {
		List<PreviousEmployment> uniquePreviousEmployments = firmManager.getPreviousMangerEmployments().stream()
				.distinct().collect(Collectors.toList());

		Collections.sort(uniquePreviousEmployments, Collections.reverseOrder());

		firmManager.setPreviousMangerEmployments(uniquePreviousEmployments);
	}

}
