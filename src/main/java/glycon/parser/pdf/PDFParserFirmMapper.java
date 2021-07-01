package glycon.parser.pdf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import glycon.object.DisclosureFirm;

public class PDFParserFirmMapper {

	private static String indexString = "";

	private static boolean lastIndexFound = false;

	private static void findAndPutTag(String disclosureReportingSourceString, Map<String, String> disclosureInfoMap,
			String word) {

		int startIndex = disclosureReportingSourceString.indexOf(word) + word.length();

		if (startIndex != -1 && disclosureReportingSourceString.charAt(startIndex - 1) == ' ') {
			int endIndex = disclosureReportingSourceString.indexOf("¬", startIndex);

			if (endIndex == -1)
				endIndex = disclosureReportingSourceString.length();

			String foundWord = disclosureReportingSourceString.substring(startIndex, endIndex);

			disclosureInfoMap.put(word, foundWord);
		} else {

			disclosureInfoMap.put(word, "");

		}

	}

	static Map<String, String> generateDisclosureInfoMap(String disclosureReportingSourceString) {

		Map<String, String> disclosureInfoMap = new HashMap<>();

		for (FirmInfoTagEnum firmInfoTag : FirmInfoTagEnum.values()) {

			String word = firmInfoTag.getWord();

			findAndPutTag(disclosureReportingSourceString, disclosureInfoMap, word);
		}

		return disclosureInfoMap;
	}

	public static Map<String, String> generateDisclosureTypeMap(String pdfTextString) {

		Map<String, String> disclosureTypeMap = new HashMap<>();

		for (FirmDisclosureTypeEnum firmDisclosureType : FirmDisclosureTypeEnum.values()) {

			parseDisclosureType(pdfTextString, disclosureTypeMap, firmDisclosureType.getWord());

		}

		return disclosureTypeMap;
	}

	public static void mapEntryToFirmObject(Entry<String, String> entry, DisclosureFirm firmDisclosure) {

		if (entry.getValue().length() > 1) {
			switch (entry.getKey()) {
			case "¬Allegations: ":
				firmDisclosure.setAllegations(prettyDisclosure(entry.getValue()));
				break;

			case "¬Reporting Source: ":
				firmDisclosure.setReportingSource(prettyDisclosure(entry.getValue()));
				break;

			case "¬Current Status: ":
				firmDisclosure.setCurrentStatus(prettyDisclosure(entry.getValue()));
				break;

			case "¬Case Number: ":
			case "¬Docket/Case Number: ":
				firmDisclosure.setDocketCaseNumber(prettyNumber(entry.getValue()));
				break;

			case "¬Case Initiated: ":
			case "¬Date Initiated: ":
				firmDisclosure.setEventDate(prettyNumber(entry.getValue()));
				break;

			case "¬Disposition Date: ":
			case "¬Resolution Date: ":
				firmDisclosure.setResolutionDate(prettyNumber(entry.getValue()));
				break;

			case "¬Disposition: ":
			case "¬Resolution: ":
				firmDisclosure.setResolution(prettyDisclosure(entry.getValue()));
				break;

			case "¬Sum of All Relief Awarded: ":
				firmDisclosure.setSanctionDetails(prettyNumber(entry.getValue()));
				break;

			case "¬Payout Details: ":
			case "¬Sanction Details: ":
				firmDisclosure.setSanctionDetails(prettyDisclosure(entry.getValue()));
				break;

			default:
				break;
			}
		}
	}

	private static void parseDisclosureType(String pdfTextString, Map<String, String> disclosureTypeMap, String word) {

		indexString = "";

		int startIndex = pdfTextString.indexOf(word);

		if (startIndex != -1) {

			Arrays.asList(FirmDisclosureTypeEnum.values()).forEach(firmDisclosureType -> {

				if (!lastIndexFound) {

					int endIndex = pdfTextString.indexOf(firmDisclosureType.getWord(), startIndex);

					if ((endIndex != -1 && startIndex != endIndex) && !lastIndexFound) {

						indexString = pdfTextString.substring(startIndex, endIndex);

						lastIndexFound = true;
					}

				}
			});

			if (!lastIndexFound)
				indexString = pdfTextString.substring(startIndex, pdfTextString.length());

			disclosureTypeMap.put(word, indexString);

			lastIndexFound = false;

		}
	}

	private static String prettyDisclosure(String value) {

		return value.replace("\r\n", " ");
	}

	private static String prettyNumber(String value) {

		int endIndex = value.indexOf("\r\n");

		if (endIndex != -1)
			return value.substring(0, endIndex);

		return value;
	}
}
