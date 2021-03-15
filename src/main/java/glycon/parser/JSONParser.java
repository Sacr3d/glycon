package glycon.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.FirmManager;
import glycon.object.FriendlyFirm;

public class JSONParser {

	private static String findAlternativeTag(JsonNode locatedNode) {

		return hasTagInNode(locatedNode, "firm_ia_full_sec_number")
				? getFeildAsString(locatedNode, "firm_ia_full_sec_number")
				: getFeildAsString(locatedNode, "firm_name").replace(" ", "+");
	}

	private static String getFeildAsString(JsonNode locatedNode, String fieldName) {

		return locatedNode.get(fieldName).asText();

	}

	private static boolean hasDisclosure(JsonNode locatedNode) {

		if (locatedNode.has("firm_disclosure_fl")) {
			return isDisclosure(locatedNode, "firm_disclosure_fl");

		}

		if (locatedNode.has("firm_ia_disclosure_fl")) {
			return isDisclosure(locatedNode, "firm_ia_disclosure_fl");

		}

		return false;
	}

	private static boolean hasTagInNode(JsonNode locatedNode, String tag) {

		return locatedNode.has(tag);
	}

	private static boolean isDisclosure(JsonNode locatedNode, String fieldValue) {

		return getFeildAsString(locatedNode, fieldValue).contentEquals("Y");
	}

	public static FriendlyFirm parseFirmJSON(String firmJSON) {

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			JsonNode jsonNode = objectMapper.readTree(sanitizeFirmJSON(firmJSON));
			JsonNode locatedNode = jsonNode.findPath("_source");

			if (!locatedNode.isMissingNode()) {

				String firmName = getFeildAsString(locatedNode, "firm_name");
				String firmId = getFeildAsString(locatedNode, "firm_source_id");

				String firmSec = hasTagInNode(locatedNode, "firm_bd_full_sec_number")
						? getFeildAsString(locatedNode, "firm_bd_full_sec_number")
						: findAlternativeTag(locatedNode);

				return new FriendlyFirm(firmName, firmId, firmSec);

			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static String sanitizeFirmJSON(String firmJSON) {

		String sanitizedString = firmJSON.replace("/**/angular.callbacks.", "");

		sanitizedString = sanitizedString.substring(sanitizedString.indexOf("(") + 1, sanitizedString.length());

		return sanitizedString.substring(0, sanitizedString.length() - 1);

	}

	public static int parseFirmHits(String firmJSON) {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(sanitizeFirmJSON(firmJSON));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (jsonNode != null)
			return Integer.parseInt(getFeildAsString(jsonNode.findPath("hits"), "total"));

		return 0;
	}

	public static List<FirmManager> parseFirmManagerJSON(String firmManagersJSON) {

		List<FirmManager> firmManagers = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {

			JsonNode masterJsonNode = objectMapper.readTree(sanitizeFirmJSON(firmManagersJSON));
			List<JsonNode> locatedNodes = masterJsonNode.findValues("_source");

			for (JsonNode jsonNode : locatedNodes) {

				FirmManager firmManager = objectMapper.treeToValue(jsonNode, FirmManager.class);

				if (firmManager.getInd_source_id() != null
						&& firmManager.getInd_bc_disclosure_fl().contentEquals("Y")) {

					firmManagers.add(firmManager);

				}
			}

			return firmManagers;

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Collections.emptyList();

	}

}
