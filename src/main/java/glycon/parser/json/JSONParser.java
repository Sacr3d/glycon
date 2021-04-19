package glycon.parser.json;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONParser {

	static String getFeildAsString(JsonNode locatedNode, String fieldName) {

		return locatedNode.get(fieldName).asText();

	}

	static boolean hasTagInNode(JsonNode locatedNode, String tag) {

		return locatedNode.has(tag);
	}

	static String sanitizeFinraJSON(String firmJSON) {

		String sanitizedString = firmJSON.replace("/**/angular.callbacks.", "");

		sanitizedString = sanitizedString.substring(sanitizedString.indexOf("(") + 1, sanitizedString.length());

		return sanitizedString.substring(0, sanitizedString.length() - 1);

	}

	private JSONParser() {
		throw new IllegalStateException("Utility class");
	}

}
