package glycon.parser.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import glycon.object.Disclosure;
import glycon.object.DisclosureFirm;
import glycon.object.Firm;
import glycon.parser.PDFParser;
import glycon.utils.csv.CSVUtilFirm;

public class PDFParserFirm {

	private static String formatStartTags(String disclosureTextString) {

		String[] disclosureStringArray = disclosureTextString.split("\r\n");

		StringBuilder resultString = new StringBuilder();

		String disclosureLinePrevious = "";

		for (int i = 0; i < disclosureStringArray.length; i++) {

			String disclosureLine = disclosureStringArray[i];

			if (disclosureLine.contains(":")) {
				if (!disclosureLinePrevious.contains("Allegations:")
						&& !disclosureLinePrevious.contains("Sanction Details:")) {
					disclosureStringArray[i] = insertStartSymbol(disclosureLine);
				}
				disclosureLinePrevious = disclosureStringArray[i];
			}
		}
		for (String string : disclosureStringArray) {

			resultString.append(string + "\r\n");

		}
		return resultString.toString();
	}

	private static String getDisclosureNumber(String disclosureTextString) {

		String word = "Disclosure ";

		int startIndex = disclosureTextString.indexOf(word) + word.length();

		int endIndex = disclosureTextString.indexOf(" of", startIndex);

		return disclosureTextString.substring(startIndex, endIndex);
	}

	private static String insertStartSymbol(String disclosureLine) {
		StringBuilder sb = new StringBuilder(disclosureLine);
		sb.insert(0, "¬");
		return sb.toString();
	}

	private static void parseDisclosures(Firm firm, String pdfTextString) {

		Map<String, String> disclosureTypeMap = PDFParserFirmMapper.generateDisclosureTypeMap(pdfTextString);

		for (Map.Entry<String, String> entry : disclosureTypeMap.entrySet()) {

			String disclosureTextString = PDFParser.sanatizePDF(entry.getValue());

			List<String> disclosureStringList = PDFParser.generateDisclosureList(disclosureTextString, "Disclosure ");

			for (String disclosureString : disclosureStringList) {

				parseReleventInfo(disclosureString, firm, entry.getKey());

			}

		}

		CSVUtilFirm.createCSVFirmFileTest(firm);
	}

	public static void parsePDFInfoForFirm(Firm firm) {

		byte[] pdfBytes = PDFParser.getPDF(firm.getFirmId(), 'F');

		if (pdfBytes.length > 1) {

			InputStream input = new ByteArrayInputStream(pdfBytes);

			try (PDDocument doc = PDDocument.load(input)) {

				PDFTextStripper stripper = new PDFTextStripper();

				stripper.setSortByPosition(true);

				String pdfTextString = stripper.getText(doc);

				parseDisclosures(firm, pdfTextString);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void parseReleventInfo(String disclosureTextString, Firm firm, String disclosureType) {

		String disclosureNumber = getDisclosureNumber(disclosureTextString);

		disclosureTextString = removeDisclosureNumbers(disclosureTextString);

		List<String> disclosureStringList = PDFParser.generateDisclosureList(disclosureTextString,
				"Reporting Source: ");

		for (String disclosureReportingSourceString : disclosureStringList) {

			DisclosureFirm firmDisclosure = new DisclosureFirm();

			parseSubDisclosures(disclosureNumber, disclosureReportingSourceString, disclosureType, firmDisclosure);

			firm.getDiscolsures().add(firmDisclosure);

		}

	}

	private static void parseSubDisclosures(String disclosureNumber, String disclosureReportingSourceString,
			String disclosureType, DisclosureFirm firmDisclosure) {

		disclosureReportingSourceString = formatStartTags(disclosureReportingSourceString);

		Map<String, String> disclosureInfoMap = PDFParserFirmMapper
				.generateDisclosureInfoMap(disclosureReportingSourceString);

		for (Map.Entry<String, String> entry : disclosureInfoMap.entrySet()) {

			PDFParserFirmMapper.mapEntryToFirmObject(entry, firmDisclosure);
		}

		firmDisclosure.setDisclosureNo(disclosureNumber);
		firmDisclosure.setDisclosureType(disclosureType);

	}

	private static String removeDisclosureNumbers(String disclosureTextString) {

		int endIndex = disclosureTextString.indexOf("\r\n", 1) + 2;

		String removeTargetString = disclosureTextString.substring(0, endIndex);

		return disclosureTextString.replace(removeTargetString, "");
	}
}
