package glycon.parser;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.CurrentEmployment;
import glycon.object.Disclosure;
import glycon.object.FirmManager;
import glycon.object.PreviousEmployment;

public class JSONManager {

	public static void parseManagerPreviousEmployments(FirmManager firmManager, ObjectMapper objectMapper,
			JsonNode masterJsonNode, char databaseType) throws IOException {

		JsonNode locatedNode = masterJsonNode.findPath("previousEmployments");

		List<JsonNode> locatedNodes = JSONParser.split(locatedNode.toString());

		locatedNode = masterJsonNode.findPath("previousIAEmployments");

		locatedNodes.addAll(JSONParser.split(locatedNode.toString()));

		for (JsonNode jsonNode : locatedNodes) {

			PreviousEmployment previousEmployments = objectMapper.treeToValue(jsonNode, PreviousEmployment.class);

			previousEmployments.setRegistrationBeginDateObject();

			if (databaseType == 'F') {

				firmManager.getPreviousMangerEmployments().add(previousEmployments);

			} else {

				firmManager.getPreviousMangerIAEmployments().add(previousEmployments);

			}

		}
	}

	public static void parseManagerDisclosure(FirmManager firmManager, ObjectMapper objectMapper,
			JsonNode masterJsonNode, char databaseType) throws IOException {
		JsonNode locatedNode = masterJsonNode.findPath("disclosures");

		List<JsonNode> locatedNodes = JSONParser.split(locatedNode.toString());

		for (JsonNode jsonNode : locatedNodes) {

			Disclosure disclosure = objectMapper.treeToValue(jsonNode, Disclosure.class);

			JsonNode detailLocatedNode = jsonNode.findPath("disclosureDetail");

			disclosure.setDisclosureDetailString(detailLocatedNode.toPrettyString().replace("\\\\r\\\\n", " "));

			disclosure.setEventDateObject();

			if (databaseType == 'F') {

				firmManager.getDiscolsuresFinra().add(disclosure);

			} else {

				firmManager.getDiscolsuresSEC().add(disclosure);
			}
		}

	}

	public static void parseManagerCurrentEmployments(FirmManager firmManager, ObjectMapper objectMapper,
			JsonNode masterJsonNode, char databaseType) throws IOException {

		JsonNode locatedNode = masterJsonNode.findPath("currentEmployments");

		List<JsonNode> locatedNodes = JSONParser.split(locatedNode.toString());

		locatedNode = masterJsonNode.findPath("currentIAEmployments");

		locatedNodes.addAll(JSONParser.split(locatedNode.toString()));

		for (JsonNode jsonNode : locatedNodes) {

			CurrentEmployment currentEmployments = objectMapper.treeToValue(jsonNode, CurrentEmployment.class);

			currentEmployments.setRegistrationBeginDateObject();

			if (databaseType == 'F') {

				firmManager.getCurrentMangerEmployments().add(currentEmployments);

			} else {

				firmManager.getCurrentMangerIAEmployments().add(currentEmployments);

			}
		}
	}

	public static void parseManagerJSON(FirmManager firmManager) {

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (firmManager.getFirmFinraJSON().contentEquals("NULL")) {

			JSONError.generateAPIError(firmManager, "FINRA API ERROR", "00/00/0000");

		} else {

			JSONParser.parseFinraJSON(firmManager, objectMapper);

		}

		if (firmManager.getFirmSecJSON().contentEquals("NULL")) {

			JSONError.generateAPIError(firmManager, "SEC API ERROR", "00/00/0000");

		} else {

			JSONParser.parseSecJSON(firmManager, objectMapper);

		}
	}

}
