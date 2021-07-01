package glycon.object;

public class DisclosureFirm extends Disclosure {

	private String allegations;

	private String currentStatus;

	private String disclosureNo;

	private String docketCaseNumber;

	private String reportingSource;

	private String resolution;

	private String resolutionDate;

	private String sanctionDetails;

	public String getAllegations() {
		return allegations;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public String getDisclosureNo() {
		return disclosureNo;
	}

	public String getDocketCaseNumber() {
		return docketCaseNumber;
	}

	public String getReportingSource() {
		return reportingSource;
	}

	public String getResolution() {
		return resolution;
	}

	public String getResolutionDate() {
		return resolutionDate;
	}

	public String getSanctionDetails() {
		return sanctionDetails;
	}

	public void setAllegations(String allegations) {
		this.allegations = allegations;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public void setDisclosureNo(String disclosureNo) {
		this.disclosureNo = disclosureNo;
	}

	public void setDocketCaseNumber(String docketCaseNumber) {
		this.docketCaseNumber = docketCaseNumber;
	}

	public void setReportingSource(String reportingSource) {
		this.reportingSource = reportingSource;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setResolutionDate(String resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	public void setSanctionDetails(String sanctionDetails) {
		this.sanctionDetails = sanctionDetails;
	}

}
