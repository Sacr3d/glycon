package glycon.thread;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import glycon.object.Firm;
import glycon.object.manager.ManagerIn;
import glycon.utils.DirEnum;
import glycon.utils.FileUtil;
import glycon.utils.csv.CSVUtilFirm;
import glycon.utils.csv.CSVUtilManager;

public class GlyconFirmDisclosureThread implements Runnable {

	private List<Firm> primeFirmList;
	private AtomicInteger atomicInt;

	public GlyconFirmDisclosureThread(List<Firm> firmList, AtomicInteger atomicInt) {

		this.setPrimeFirmList(firmList);
		this.setAtomicInt(atomicInt);

	}

	@Override
	public void run() {

		for (Firm firm : primeFirmList) {

			if (!FileUtil.fileExists(DirEnum.MANAGER_DISCLOSURE_PATH.toString() + firm.getFirmId() + ".csv")) {

				firm.generateInfo();

				CSVUtilFirm.createFirmFile(firm);

			}
		}

		atomicInt.addAndGet(1);

	}

	public List<Firm> getPrimeFirmList() {
		return primeFirmList;
	}

	public void setPrimeFirmList(List<Firm> primeFirmList) {
		this.primeFirmList = primeFirmList;
	}

	public AtomicInteger getAtomicInt() {
		return atomicInt;
	}

	public void setAtomicInt(AtomicInteger atomicInt) {
		this.atomicInt = atomicInt;
	}

}
