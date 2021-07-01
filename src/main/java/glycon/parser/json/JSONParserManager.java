package glycon.parser.json;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.Manager;
import glycon.parser.JSONParser;
import glycon.parser.pdf.PDFParserManager;
import glycon.utils.LoggingUtil;

public class JSONParserManager {

	private static void parseFinraJSON(Manager firmManager, ObjectMapper objectMapper) {
		try {

			JsonNode masterJsonNode = objectMapper.readTree(
					JSONParser.snantizeSourceJSON(JSONParser.sanitizeFinraJSON(firmManager.getManagerFinraJSON())));

			parseReleventData(firmManager, objectMapper, masterJsonNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggingUtil.warn("Finra firm manager : " + firmManager.getInd_source_id() + " could not be parsed");

		}
	}

	public static void parseManagerJSON(Manager firmManager) {

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (firmManager.getManagerFinraJSON().contentEquals("NULL")) {
			JSONParserError.generateAPIErrorManager(firmManager, "FINRA API ERROR", "00/00/0000");

		} else {
			parseFinraJSON(firmManager, objectMapper);

		}

		if (firmManager.getFirmSecJSON().contentEquals("NULL")) {
			JSONParserError.generateAPIErrorManager(firmManager, "SEC API ERROR", "00/00/0000");

		} else {
			parseSecJSON(firmManager, objectMapper);
		}
	}

	private static void parseReleventData(Manager firmManager, ObjectMapper objectMapper, JsonNode masterJsonNode)
			throws IOException {

		JsonNode locatedNode = masterJsonNode.findPath("legacyReportStatusDescription");

		String nodeString = locatedNode != null ? locatedNode.textValue() : null;

		JSONParserManagerInfo.parseManagerPreviousEmployments(firmManager, objectMapper, masterJsonNode);

		JSONParserManagerInfo.parseManagerCurrentEmployments(firmManager, objectMapper, masterJsonNode);

		if (nodeString != null && nodeString.contentEquals("Generated")) {

			PDFParserManager.parsePDFInfoForManager(firmManager);

		}

		else {

			JSONParserManagerInfo.parseManagerDisclosure(firmManager, objectMapper, masterJsonNode);

		}

		JSONParserManagerInfo.parseManagerExaminations(firmManager, objectMapper, masterJsonNode);

	}

	private static void parseSecJSON(Manager firmManager, ObjectMapper objectMapper) {
		try {

			JsonNode masterJsonNode = objectMapper
					.readTree(JSONParser.snantizeSourceJSON(firmManager.getFirmSecJSON()));

			parseReleventData(firmManager, objectMapper, masterJsonNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggingUtil.warn("SEC firm manager : " + firmManager.getInd_source_id() + " could not be parsed");

		}
	}

	private JSONParserManager() {
		throw new IllegalStateException("Utility class");
	}

}
