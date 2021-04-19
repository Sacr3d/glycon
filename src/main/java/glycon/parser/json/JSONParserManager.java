package glycon.parser.json;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.CurrentEmployment;
import glycon.object.Disclosure;
import glycon.object.FirmManager;
import glycon.object.PreviousEmployment;
import glycon.parser.PDFParser;
import glycon.utils.LoggingUtil;

public class JSONParserManager {

	public static void parseManagerJSON(FirmManager firmManager) {

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (firmManager.getFirmFinraJSON().contentEquals("NULL")) {
			JSONParserError.generateAPIError(firmManager, "FINRA API ERROR", "00/00/0000");

		} else {
			parseFinraJSON(firmManager, objectMapper);

		}

		if (firmManager.getFirmSecJSON().contentEquals("NULL")) {
			JSONParserError.generateAPIError(firmManager, "SEC API ERROR", "00/00/0000");

		} else {
			parseSecJSON(firmManager, objectMapper);
		}
	}

	private static void parseSecJSON(FirmManager firmManager, ObjectMapper objectMapper) {
		try {

			JsonNode masterJsonNode = objectMapper.readTree(snantizeManagerJson(firmManager.getFirmSecJSON()));

			parseReleventData(firmManager, objectMapper, masterJsonNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggingUtil.warn("SEC firm manager : " + firmManager.getInd_source_id() + " could not be parsed");

		}
	}

	private static void parseReleventData(FirmManager firmManager, ObjectMapper objectMapper, JsonNode masterJsonNode)
			throws IOException {

		JsonNode locatedNode = masterJsonNode.findPath("legacyReportStatusDescription");

		String nodeString = locatedNode != null ? locatedNode.textValue() : null;

		JSONParserManagerInfo.parseManagerPreviousEmployments(firmManager, objectMapper, masterJsonNode);

		JSONParserManagerInfo.parseManagerCurrentEmployments(firmManager, objectMapper, masterJsonNode);

		if (nodeString != null && nodeString.contentEquals("Generated")) {

			PDFParser.parsePDFInfoForManager(firmManager);

		}

		else {

			JSONParserManagerInfo.parseManagerDisclosure(firmManager, objectMapper, masterJsonNode);

		}

		JSONParserManagerInfo.parseManagerExaminations(firmManager, objectMapper, masterJsonNode);

	}

	private static void parseFinraJSON(FirmManager firmManager, ObjectMapper objectMapper) {
		try {

			JsonNode masterJsonNode = objectMapper
					.readTree(snantizeManagerJson(JSONParser.sanitizeFinraJSON(firmManager.getFirmFinraJSON())));

			parseReleventData(firmManager, objectMapper, masterJsonNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggingUtil.warn("Finra firm manager : " + firmManager.getInd_source_id() + " could not be parsed");

		}
	}

	private static String snantizeManagerJson(String firmJSON) {

		return firmJSON.replace("\"{", "{").replace("}\"", "}").replace("\\\"", "\"").replace("\\\\\"", "\\\"");
	}

}
