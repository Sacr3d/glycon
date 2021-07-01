package glycon.parser.json;

import glycon.object.Disclosure;
import glycon.object.DisclosureFirm;
import glycon.object.Firm;
import glycon.object.Manager;
import glycon.object.manager.CurrentEmployment;
import glycon.object.manager.DisclosureManager;

public class JSONParserError {

	static void generateAPIErrorManager(Manager firmManager, String errorDetail, String errorDate) {

		generateDisclosureErrorManager(firmManager, errorDetail, errorDate);

		generateEmoplymentError(firmManager, errorDetail, errorDate);

	}

	private static void generateDisclosureErrorManager(Manager firmManager, String errorDetail, String errorDate) {
		Disclosure errorDisclosure = new DisclosureManager();

		createDisclosureError(errorDetail, errorDate, errorDisclosure);

		firmManager.getDiscolsures().add(errorDisclosure);
	}

	private static void createDisclosureError(String errorDetail, String errorDate, Disclosure errorDisclosure) {
		errorDisclosure.setDisclosureDetailString(errorDetail);

		errorDisclosure.setEventDate(errorDate);

		errorDisclosure.setEventDateObject();
	}

	private static void generateEmoplymentError(Manager firmManager, String errorDetail, String errorDate) {
		CurrentEmployment errorEmployment = new CurrentEmployment();

		errorEmployment.setFirmName(errorDetail);

		errorEmployment.setFirmId(errorDetail);

		errorEmployment.setRegistrationBeginDate(errorDate);

		errorEmployment.setRegistrationBeginDateObject();

		firmManager.getCurrentMangerEmployments().add(errorEmployment);
	}

	private JSONParserError() {
		throw new IllegalStateException("Utility class");
	}

	public static void generateAPIErrorFirm(Firm firm, String errorDetail, String errorDate) {

		generateDisclosureErrorFirm(firm, errorDetail, errorDate);

	}

	private static void generateDisclosureErrorFirm(Firm firm, String errorDetail, String errorDate) {

		Disclosure errorDisclosure = new DisclosureFirm();

		createDisclosureError(errorDetail, errorDate, errorDisclosure);

		firm.getDiscolsures().add((DisclosureFirm) errorDisclosure);
	}

}
