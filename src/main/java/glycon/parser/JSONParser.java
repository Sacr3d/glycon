package glycon.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.FirmManager;
import glycon.object.FirmManagerOut;
import glycon.object.FriendlyFirm;
import glycon.utils.LoggingUtil;

public class JSONParser {

	private static String findAlternativeTag(JsonNode locatedNode) {

		return hasTagInNode(locatedNode, "firm_ia_full_sec_number")
				? getFeildAsString(locatedNode, "firm_ia_full_sec_number")
				: getFeildAsString(locatedNode, "firm_name").replace(" ", "+");
	}

	private static String getFeildAsString(JsonNode locatedNode, String fieldName) {

		return locatedNode.get(fieldName).asText();

	}

	private static boolean hasTagInNode(JsonNode locatedNode, String tag) {

		return locatedNode.has(tag);
	}

	static void parseFinraJSON(FirmManager firmManager, ObjectMapper objectMapper) {
		try {

			JsonNode masterJsonNode = objectMapper
					.readTree(snantizeManagerJson(sanitizeFinraJSON(firmManager.getFirmFinraJSON())));

			parseReleventData(firmManager, objectMapper, masterJsonNode, 'F');

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggingUtil.warn("Finra firm manager : " + firmManager.getInd_source_id() + " could not be parsed");

		}
	}

	public static int parseFirmHits(String firmJSON) {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(sanitizeFinraJSON(firmJSON));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (jsonNode != null)
			return Integer.parseInt(getFeildAsString(jsonNode.findPath("hits"), "total"));

		return 0;
	}

	public static FriendlyFirm parseFirmJSON(String firmJSON) {

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			JsonNode jsonNode = objectMapper.readTree(sanitizeFinraJSON(firmJSON));
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

	public static List<FirmManager> parseFirmManagerJSON(String firmManagersJSON) {

		List<FirmManager> firmManagers = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {

			JsonNode masterJsonNode = objectMapper.readTree(sanitizeFinraJSON(firmManagersJSON));
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

	private static void parseReleventData(FirmManager firmManager, ObjectMapper objectMapper, JsonNode masterJsonNode,
			char databaseType) throws IOException {

		JsonNode locatedNode = masterJsonNode.findPath("legacyReportStatusDescription");

		String nodeString = locatedNode != null ? locatedNode.textValue() : null;

		if (nodeString != null && nodeString.contentEquals("Generated")) {

			JSONManager.parseManagerPreviousEmployments(firmManager, objectMapper, masterJsonNode, databaseType);

			JSONManager.parseManagerCurrentEmployments(firmManager, objectMapper, masterJsonNode, databaseType);

			PDFParser.parsePDFInfoForManager(firmManager);

		}

		else {

			JSONManager.parseManagerPreviousEmployments(firmManager, objectMapper, masterJsonNode, databaseType);

			JSONManager.parseManagerCurrentEmployments(firmManager, objectMapper, masterJsonNode, databaseType);

			JSONManager.parseManagerDisclosure(firmManager, objectMapper, masterJsonNode, databaseType);

		}
	}

	static void parseSecJSON(FirmManager firmManager, ObjectMapper objectMapper) {
		try {

			JsonNode masterJsonNode = objectMapper.readTree(snantizeManagerJson(firmManager.getFirmSecJSON()));

			parseReleventData(firmManager, objectMapper, masterJsonNode, 'S');

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggingUtil.warn("SEC firm manager : " + firmManager.getInd_source_id() + " could not be parsed");

		}
	}

	private static String sanitizeFinraJSON(String firmJSON) {

		String sanitizedString = firmJSON.replace("/**/angular.callbacks.", "");

		sanitizedString = sanitizedString.substring(sanitizedString.indexOf("(") + 1, sanitizedString.length());

		return sanitizedString.substring(0, sanitizedString.length() - 1);

	}

	private static String snantizeManagerJson(String firmJSON) {

		return firmJSON.replace("\"{", "{").replace("}\"", "}").replace("\\\"", "\"").replace("\\\\\"", "\\\"");
	}

	public static List<JsonNode> split(final String string) throws IOException {
		final JsonNode jsonNode = new ObjectMapper().readTree(string);
		return StreamSupport.stream(jsonNode.spliterator(), false) // Stream
				.collect(Collectors.toList()); // and collect as a List
	}

<<<<<<< HEAD
	private JSONParser() {
		throw new IllegalStateException("Utility class");
=======
	private static String snantizeManagerJson(String firmJSON) {

		return firmJSON.replace("\"{", "{").replace("}\"", "}").replace("\\\"", "\"").replace("\\\\\"", "\\\"");
>>>>>>> parent of 1cd784f (Final List Implementation Finished)
	}

}
