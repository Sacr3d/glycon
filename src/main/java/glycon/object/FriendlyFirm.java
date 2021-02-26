package glycon.object;

public class FriendlyFirm {

	private String firmId;

	private String firmName;
	private String firmSec;
	public FriendlyFirm(String firmName, String firmId, String firmSec) {

		this.firmId = firmId;
		this.firmName = firmName;
		this.firmSec = firmSec;

	}

	public String getFirmId() {
		return firmId;
	}

	public String getFirmName() {
		return firmName;
	}

	public String getFirmSec() {
		return firmSec;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public void setFirmSec(String firmSec) {
		this.firmSec = firmSec;
	}

}
