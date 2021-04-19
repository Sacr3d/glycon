package glycon.parser.json;

import glycon.object.CurrentEmployment;
import glycon.object.Disclosure;
import glycon.object.FirmManager;

public class JSONParserError {

	static void generateAPIError(FirmManager firmManager, String errorDetail, String errorDate) {

		generateDisclosureError(firmManager, errorDetail, errorDate);

		generateEmoplymentError(firmManager, errorDetail, errorDate);

	}

	private static void generateDisclosureError(FirmManager firmManager, String errorDetail, String errorDate) {
		Disclosure errorDisclosure = new Disclosure();

		errorDisclosure.setDisclosureDetailString(errorDetail);

		errorDisclosure.setEventDate(errorDate);

		errorDisclosure.setEventDateObject();

		firmManager.getDiscolsures().add(errorDisclosure);
	}

	private static void generateEmoplymentError(FirmManager firmManager, String errorDetail, String errorDate) {
		CurrentEmployment errorEmployment = new CurrentEmployment();

		errorEmployment.setFirmName(errorDetail);

		errorEmployment.setFirmId(errorDetail);

		errorEmployment.setRegistrationBeginDate(errorDate);

		errorEmployment.setRegistrationBeginDateObject();

		firmManager.getCurrentMangerEmployments().add(errorEmployment);
	}

}
