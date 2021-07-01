package glycon.object;

import java.util.ArrayList;
import java.util.List;

import glycon.network.RequestURL;
import glycon.object.firm.FirmBasicInfo;
import glycon.object.firm.FirmLocation;
import glycon.parser.json.JSONParserFirm;
import glycon.parser.json.JSONParserManager;

public class Firm {

	private List<DisclosureFirm> discolsures = new ArrayList<>();
	private String firmFinraJSON;

	private String firmId;
	private String firmName;
	private String secId;

	private FirmBasicInfo basicInfo;
	
	private FirmLocation firmLocation;

	public Firm(String firmId, String secId, String firmName) {
		this.firmName = firmName;
		this.firmId = firmId;
		this.secId = secId;
	}

	public void generateInfo() {

		String managerFinraJSON = getJSON();

		this.firmFinraJSON = managerFinraJSON;

		JSONParserFirm.parseFirmJSON(this);
	}

	public List<DisclosureFirm> getDiscolsures() {
		return discolsures;
	}

	public String getFirmFinraJSON() {
		return firmFinraJSON;
	}

	public String getFirmId() {
		return firmId;
	}

	public String getFirmName() {
		return firmName;
	}

	private String getJSON() {
		String managerJSON = "NULL";

		int failCount = 0;

		while (managerJSON.contentEquals("NULL") && failCount < 5) {

			managerJSON = new RequestURL().getFirmJSON(firmId);

			failCount++;

		}
		return managerJSON;
	}

	public String getSecId() {
		return secId;
	}

	public void setDiscolsures(List<DisclosureFirm> discolsures) {
		this.discolsures = discolsures;
	}

	public void setFirmFinraJSON(String firmFinraJSON) {
		this.firmFinraJSON = firmFinraJSON;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public void setSecId(String secId) {
		this.secId = secId;
	}

	public FirmBasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(FirmBasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}

	public FirmLocation getFirmLocation() {
		return firmLocation;
	}

	public void setFirmLocation(FirmLocation firmLocation) {
		this.firmLocation = firmLocation;
	}

}
