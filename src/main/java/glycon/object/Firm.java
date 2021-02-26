package glycon.object;

public class Firm {

	private String firmName;
	private String secId;

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getSecId() {
		return secId;
	}

	public void setSecId(String secId) {
		this.secId = secId;
	}

	public String getFirmId() {
		return firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	private String firmId;

	public Firm(String firmId, String secId, String firmName) {
		this.firmName = firmName;
		this.firmId = firmId;
		this.secId = secId;
	}
}
