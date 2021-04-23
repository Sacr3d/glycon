package glycon.parser.json;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.CurrentEmployment;
import glycon.object.Disclosure;
import glycon.object.Examination;
import glycon.object.FirmManager;
import glycon.object.PreviousEmployment;

public class JSONParserManagerInfo {

	static void parseManagerCurrentEmployments(FirmManager firmManager, ObjectMapper objectMapper,
			JsonNode masterJsonNode) throws IOException {

		JsonNode locatedNode = masterJsonNode.findPath("currentEmployments");

		List<JsonNode> locatedNodes = split(locatedNode.toString());

		locatedNode = masterJsonNode.findPath("currentIAEmployments");

		locatedNodes.addAll(split(locatedNode.toString()));

		for (JsonNode jsonNode : locatedNodes) {

			CurrentEmployment currentEmployments = objectMapper.treeToValue(jsonNode, CurrentEmployment.class);

			currentEmployments.setRegistrationBeginDateObject();

			firmManager.getCurrentMangerEmployments().add(currentEmployments);

		}
	}

	static void parseManagerDisclosure(FirmManager firmManager, ObjectMapper objectMapper, JsonNode masterJsonNode)
			throws IOException {
		JsonNode locatedNode = masterJsonNode.findPath("disclosures");

		List<JsonNode> locatedNodes = split(locatedNode.toString());

		for (JsonNode jsonNode : locatedNodes) {

			Disclosure disclosure = objectMapper.treeToValue(jsonNode, Disclosure.class);

			JsonNode detailLocatedNode = jsonNode.findPath("disclosureDetail");

			disclosure.setDisclosureDetailString(detailLocatedNode.toPrettyString().replace("\\\\r\\\\n", " "));

			disclosure.setEventDateObject();

			firmManager.getDiscolsures().add(disclosure);

		}
	}

	public static void parseManagerExaminations(FirmManager firmManager, ObjectMapper objectMapper,
			JsonNode masterJsonNode) throws IOException {

		String[] examinationGroups = { "stateExamCategory", "principalExamCategory", "productExamCategory" };

		for (String jsonTag : examinationGroups) {

			JsonNode locatedNode = masterJsonNode.findPath(jsonTag);

			List<JsonNode> locatedNodes = split(locatedNode.toString());

			for (JsonNode jsonNode : locatedNodes) {

				Examination examination = objectMapper.treeToValue(jsonNode, Examination.class);

				examination.setCategory(jsonTag);

				examination.setExamDateTakenDateObject();

				firmManager.getExaminations().add(examination);

			}
		}
	}

	static void parseManagerPreviousEmployments(FirmManager firmManager, ObjectMapper objectMapper,
			JsonNode masterJsonNode) throws IOException {

		JsonNode locatedNode = masterJsonNode.findPath("previousEmployments");

		List<JsonNode> locatedNodes = split(locatedNode.toString());

		locatedNode = masterJsonNode.findPath("previousIAEmployments");

		locatedNodes.addAll(split(locatedNode.toString()));

		for (JsonNode jsonNode : locatedNodes) {

			PreviousEmployment previousEmployments = objectMapper.treeToValue(jsonNode, PreviousEmployment.class);

			previousEmployments.setRegistrationBeginDateObject();

			firmManager.getPreviousMangerEmployments().add(previousEmployments);

		}
	}

	public static List<JsonNode> split(final String string) throws IOException {
		final JsonNode jsonNode = new ObjectMapper().readTree(string);
		return StreamSupport.stream(jsonNode.spliterator(), false) // Stream
				.collect(Collectors.toList()); // and collect as a List
	}

	private JSONParserManagerInfo() {
		throw new IllegalStateException("Utility class");
	}

}
