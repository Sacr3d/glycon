package glycon.parser;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONParser {

	public static String getFeildAsString(JsonNode locatedNode, String fieldName) {

		return locatedNode.get(fieldName).asText();

	}

	public static boolean hasTagInNode(JsonNode locatedNode, String tag) {

		return locatedNode.has(tag);
	}

	public static String sanitizeFinraJSON(String firmJSON) {

		String sanitizedString = firmJSON.replace("/**/angular.callbacks.", "");

		sanitizedString = sanitizedString.substring(sanitizedString.indexOf("(") + 1, sanitizedString.length());

		return sanitizedString.substring(0, sanitizedString.length() - 1);

	}

	private JSONParser() {
		throw new IllegalStateException("Utility class");
	}
	
	public static String snantizeSourceJSON(String firmJSON) {

		return firmJSON.replace("\"{", "{").replace("}\"", "}").replace("\\\"", "\"").replace("\\\\\"", "\\\"");
	}



}
