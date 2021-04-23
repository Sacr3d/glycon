package glycon.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import glycon.network.RequestURL;
import glycon.object.Disclosure;
import glycon.object.FirmManager;

public class PDFParser {

	private static final String ABOUT_THIS_BROKER_CHECK_REPORT = "About this BrokerCheck Report";

	private static List<Integer> findStartIndexs(String textString, String word) {

		List<Integer> indexes = new ArrayList<>();
		String lowerCaseTextString = textString.toLowerCase();
		String lowerCaseWord = word.toLowerCase();
		int wordLength = 0;

		int index = 0;
		while (index != -1) {
			index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength); // Slight improvement
			if (index != -1) {
				indexes.add(index);
			}
			wordLength = word.length();
		}
		return indexes;

	}

	private static List<String> generateDisclosureList(String disclosureTextString) {

		List<String> disclosureStringList = new ArrayList<>();

		List<Integer> discorsureStartIndexs = findStartIndexs(disclosureTextString, "Disclosure ");

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

	private static byte[] getPDF(FirmManager firmManager) {
		byte[] managerPDF = new byte[0];

		int failCount = 0;

		while (managerPDF.length < 1 && failCount < 5) {

			managerPDF = new RequestURL().getPDF(firmManager.getInd_source_id());

			failCount++;

		}

		return managerPDF;
	}

	private static void parseDisclosures(FirmManager firmManager, String pdfTextString) {

		String disclosureTextString = sanatizePDF(pdfTextString);

		List<String> disclosureStringList = generateDisclosureList(disclosureTextString);

		for (String disclosureString : disclosureStringList) {

			int splitIndex = disclosureString.indexOf("\r\n");

			Disclosure legacyDisclosure = new Disclosure();

			legacyDisclosure.setDisclosureType("Legacy PDF");

			legacyDisclosure.setDisclosureResolution(disclosureString.substring(0, splitIndex));

			legacyDisclosure.setDisclosureDetailString(prettyDetail(disclosureString, splitIndex));

			legacyDisclosure.setEventDate("00/00/0000");

			legacyDisclosure.setEventDateObject();

			firmManager.getDiscolsures().add(legacyDisclosure);

		}

	}

	public static void parsePDFInfoForManager(FirmManager firmManager) {

		byte[] pdfBytes = getPDF(firmManager);

		if (pdfBytes.length > 1) {

			InputStream input = new ByteArrayInputStream(pdfBytes);

			try (PDDocument doc = PDDocument.load(input)) {

				PDFTextStripper stripper = new PDFTextStripper();

				stripper.setSortByPosition(true);

				String pdfTextString = stripper.getText(doc);

				parseDisclosures(firmManager, pdfTextString);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static String prettyDetail(String disclosureString, int splitIndex) {

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

	private static String sanatizePDF(String pdfTextString) {

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