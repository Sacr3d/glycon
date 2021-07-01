package glycon.parser.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import glycon.object.Disclosure;
import glycon.object.Manager;
import glycon.object.manager.DisclosureManager;
import glycon.parser.PDFParser;

public class PDFParserManager {

	private static void parseDisclosures(Manager firmManager, String pdfTextString) {

		String disclosureTextString = PDFParser.sanatizePDF(pdfTextString);

		List<String> disclosureStringList = PDFParser.generateDisclosureList(disclosureTextString, "Disclosure ");

		for (String disclosureString : disclosureStringList) {

			int splitIndex = disclosureString.indexOf("\r\n");

			DisclosureManager legacyDisclosure = new DisclosureManager();

			legacyDisclosure.setDisclosureType("Legacy PDF");

			legacyDisclosure.setDisclosureResolution(disclosureString.substring(0, splitIndex));

			legacyDisclosure.setDisclosureDetailString(PDFParser.prettyDetail(disclosureString, splitIndex));

			legacyDisclosure.setEventDate("00/00/0000");

			legacyDisclosure.setEventDateObject();

			firmManager.getDiscolsures().add(legacyDisclosure);

		}

	}

	public static void parsePDFInfoForManager(Manager firmManager) {

		byte[] pdfBytes = PDFParser.getPDF(firmManager.getInd_source_id(), 'M');

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
}
