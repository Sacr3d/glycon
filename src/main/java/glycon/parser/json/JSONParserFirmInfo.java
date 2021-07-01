package glycon.parser.json;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import glycon.object.Firm;
import glycon.object.firm.FirmBasicInfo;
import glycon.object.firm.FirmLocation;

public class JSONParserFirmInfo {

	public static void parseFirmBasicInfo(Firm firm, ObjectMapper objectMapper, JsonNode masterNode)
			throws IOException {

		if (!masterNode.isMissingNode()) {

			JsonNode locatedNode = masterNode.findPath("basicInformation");

			FirmBasicInfo firmBasicInfo = objectMapper.treeToValue(locatedNode, FirmBasicInfo.class);

			firm.setBasicInfo(firmBasicInfo);

		}
	}

	public static void parseFirmLocation(Firm firm, ObjectMapper objectMapper, JsonNode masterNode) throws IOException {

		if (!masterNode.isMissingNode()) {

			JsonNode locatedNode = masterNode.findPath("officeAddress");

			FirmLocation firmLocation = objectMapper.treeToValue(locatedNode, FirmLocation.class);

			firm.setFirmLocation(firmLocation);

		}
	}

}
