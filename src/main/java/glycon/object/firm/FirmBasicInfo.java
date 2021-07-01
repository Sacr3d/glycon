package glycon.object.firm;

public class FirmBasicInfo {

	private String firmId;

	private String firmName;

	public String getFirmId() {
		return firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String[] getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String[] otherNames) {
		this.otherNames = otherNames;
	}

	private String[] otherNames;
}
