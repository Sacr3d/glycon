package glycon.object.manager;

public class PreviousEmployment extends Employment {

	private String registrationEndDate;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Employment) {
			return ((((Employment) obj).firmId.contentEquals(firmId))
					&& (((Employment) obj).registrationBeginDate.contentEquals(registrationBeginDate)));
		}

		return false;
	}

	public String getDateRange() {

		return getRegistrationBeginDate() + " - " + registrationEndDate;
	}

	public String getRegistrationEndDate() {
		return registrationEndDate;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		return prime + (this.registrationBeginDate == null ? 0
				: this.registrationBeginDate.hashCode() + this.firmId.hashCode());
	}

	public void setRegistrationEndDate(String registrationEndDate) {
		this.registrationEndDate = registrationEndDate;
	}

}
