package glycon.parser;

import java.util.ArrayList;
import java.util.List;

import glycon.network.RequestURL;

public class PDFParser {

	private static final String ABOUT_THIS_BROKER_CHECK_REPORT = "About this BrokerCheck Report";

	private static List<Integer> findStartIndexs(String textString, String word) {

		List<Integer> indexes = new ArrayList<>();
		int wordLength = 0;

		int index = 0;
		while (index != -1) {
			index = textString.indexOf(word, index + wordLength); // Slight improvement
			if (index != -1) {
				indexes.add(index);
			}
			wordLength = word.length();
		}
		return indexes;

	}

	public static List<String> generateDisclosureList(String disclosureTextString, String word) {

		List<String> disclosureStringList = new ArrayList<>();

		List<Integer> discorsureStartIndexs = findStartIndexs(disclosureTextString, word);

		int endIndex = -1;

		for (int i = 0; i < discorsureStartIndexs.size(); i++) {

			if (i == 0) {

				endIndex = discorsureStartIndexs.size() > 1 ? discorsureStartIndexs.get(i + 1)
						: disclosureTextString.length();

				disclosureStringList.add(disclosureTextString.substring(0, endIndex));

			} else {

				int startIndex = discorsureStartIndexs.get(i);

				endIndex = discorsureStartIndexs.size() > i + 1 ? discorsureStartIndexs.get(i + 1)
						: disclosureTextString.length();

				disclosureStringList.add(disclosureTextString.substring(startIndex, endIndex));

			}

		}

		return disclosureStringList;

	}

	public static byte[] getPDF(String universalCRD, char objectType) {

		byte[] managerPDF = new byte[0];

		int failCount = 0;

		while (managerPDF.length < 1 && failCount < 5) {

			if (objectType == 'M')
				managerPDF = new RequestURL().getManagerPDF(universalCRD);
			else if (objectType == 'F')
				managerPDF = new RequestURL().getFirmPDF(universalCRD);

			failCount++;

		}

		return managerPDF;
	}

	public static String prettyDetail(String disclosureString, int splitIndex) {

		String baseString = disclosureString.substring(splitIndex, disclosureString.length());
		return baseString.replaceFirst("\r\n", "");
	}

	private static String removeCopyright(String disclosureString) {

		String[] disclosureStringArray = disclosureString.split("\r\n");

		List<String> disclosureAcceptedStringList = new ArrayList<>();

		for (int i = 0; i < disclosureStringArray.length; i++) {

			String disclousreLine = disclosureStringArray[i];

			if (!disclousreLine.contains("©")) {

				disclosureAcceptedStringList.add(disclousreLine);

			}
		}

		StringBuilder resultString = new StringBuilder();

		for (String string : disclosureAcceptedStringList) {

			resultString.append(string + "\r\n");

		}

		return resultString.toString();
	}

	private static String removeCopyrightAndWebsite(String disclosureString) {

		disclosureString = disclosureString.replace("www.finra.org/brokercheck\r\n", "");

		disclosureString = disclosureString.replace("www.finra.org/brokercheck User Guidance\r\n", "");

		disclosureString = removeCopyright(disclosureString);

		return disclosureString;
	}

	private static String removeTailingText(String disclosureText) {
		if (disclosureText.contains(ABOUT_THIS_BROKER_CHECK_REPORT)) {

			int endIndex = disclosureText.indexOf(ABOUT_THIS_BROKER_CHECK_REPORT);

			disclosureText = disclosureText.substring(0, endIndex);

		}
		return disclosureText;
	}

	public static String sanatizePDF(String pdfTextString) {

		int primeIndex = pdfTextString.indexOf("Disclosure 1 of ");

		String disclosureText = pdfTextString.substring(primeIndex);

		disclosureText = removeTailingText(disclosureText);

		disclosureText = removeCopyrightAndWebsite(disclosureText);

		return disclosureText;
	}

	private PDFParser() {
		throw new IllegalStateException("Utility class");
	}

}