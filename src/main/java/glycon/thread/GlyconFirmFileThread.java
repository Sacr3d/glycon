package glycon.thread;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import glycon.object.FirmManager;
import glycon.object.FirmManagerIn;
import glycon.object.PreviousEmployment;
import glycon.object.CurrentEmployment;
import glycon.object.Disclosure;
import glycon.utils.CSVUtil;
import glycon.utils.DirEnum;
import glycon.utils.FileUtil;

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

			List<FirmManagerIn> firmManagerList = CSVUtil.generateManagerInformation(firmFile);

			for (FirmManagerIn firmManager : firmManagerList) {

				if (!FileUtil.fileExists(DirEnum.MANAGER_PATH.toString() + firmManager.getInd_source_id() + ".csv")) {

					firmManager.generateInfo();

					sortObjectData(firmManager);

					CSVUtil.createManagerFile(firmManager);

				}
			}

			atomicInt.addAndGet(1);

		});

	}

	private void sortObjectData(FirmManager firmManager) {

		List<Disclosure> uniqueDisclosures = firmManager.getDiscolsures().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueDisclosures, Collections.reverseOrder());

		firmManager.setDiscolsures(uniqueDisclosures);

		List<CurrentEmployment> uniqueCurrentEmployments = firmManager.getCurrentMangerEmployments().stream().distinct()
				.collect(Collectors.toList());

		Collections.sort(uniqueCurrentEmployments, Collections.reverseOrder());

		firmManager.setCurrentMangerEmployments(uniqueCurrentEmployments);

		List<PreviousEmployment> uniquePreviousEmployments = firmManager.getPreviousMangerEmployments().stream()
				.distinct().collect(Collectors.toList());

		Collections.sort(uniquePreviousEmployments, Collections.reverseOrder());

		firmManager.setPreviousMangerEmployments(uniquePreviousEmployments);

	}

}
