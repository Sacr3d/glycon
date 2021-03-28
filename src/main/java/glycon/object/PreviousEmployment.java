package glycon.object;

public class PreviousEmployment extends Employment {

	private String registrationEndDate;

	public String getRegistrationEndDate() {
		return registrationEndDate;
	}

	public void setRegistrationEndDate(String registrationEndDate) {
		this.registrationEndDate = registrationEndDate;
	}

	public String getDateRange() {

		return getRegistrationBeginDate() + " - " + registrationEndDate;
	}


}
