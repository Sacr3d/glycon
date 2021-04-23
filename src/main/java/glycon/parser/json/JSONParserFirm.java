package glycon.parser.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.FirmManager;
import glycon.object.FirmManagerOut;
import glycon.object.FriendlyFirm;

public class JSONParserFirm {

	static String findAlternativeTag(JsonNode locatedNode) {

		return JSONParser.hasTagInNode(locatedNode, "firm_ia_full_sec_number")
				? JSONParser.getFeildAsString(locatedNode, "firm_ia_full_sec_number")
				: JSONParser.getFeildAsString(locatedNode, "firm_name").replace(" ", "+");
	}

	public static int parseFirmHits(String firmJSON) {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(JSONParser.sanitizeFinraJSON(firmJSON));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (jsonNode != null)
			return Integer.parseInt(JSONParser.getFeildAsString(jsonNode.findPath("hits"), "total"));

		return 0;
	}

	public static List<FirmManager> parseFirmManagerJSON(String firmManagersJSON) {

		List<FirmManager> firmManagers = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {

			JsonNode masterJsonNode = objectMapper.readTree(JSONParser.sanitizeFinraJSON(firmManagersJSON));
			List<JsonNode> locatedNodes = masterJsonNode.findValues("_source");

			for (JsonNode jsonNode : locatedNodes) {

				FirmManagerOut firmManager = objectMapper.treeToValue(jsonNode, FirmManagerOut.class);

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

	public static FriendlyFirm parseFriendlyFirmJSON(String firmJSON) {

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			JsonNode jsonNode = objectMapper.readTree(JSONParser.sanitizeFinraJSON(firmJSON));
			JsonNode locatedNode = jsonNode.findPath("_source");

			if (!locatedNode.isMissingNode()) {

				String firmName = JSONParser.getFeildAsString(locatedNode, "firm_name");
				String firmId = JSONParser.getFeildAsString(locatedNode, "firm_source_id");

				String firmSec = JSONParser.hasTagInNode(locatedNode, "firm_bd_full_sec_number")
						? JSONParser.getFeildAsString(locatedNode, "firm_bd_full_sec_number")
						: findAlternativeTag(locatedNode);

				return new FriendlyFirm(firmName, firmId, firmSec);

			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	private JSONParserFirm() {
		throw new IllegalStateException("Utility class");
	}

}
