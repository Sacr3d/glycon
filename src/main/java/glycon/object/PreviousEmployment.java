package glycon.object;

public class PreviousEmployment extends Employment {

	private String registrationEndDate;

<<<<<<< HEAD
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

=======
>>>>>>> parent of 1cd784f (Final List Implementation Finished)
	public String getRegistrationEndDate() {
		return registrationEndDate;
	}

<<<<<<< HEAD
	@Override
	public int hashCode() {
		int prime = 31;
		return prime + (this.registrationBeginDate == null ? 0
				: this.registrationBeginDate.hashCode() + this.firmId.hashCode());
	}

=======
>>>>>>> parent of 1cd784f (Final List Implementation Finished)
	public void setRegistrationEndDate(String registrationEndDate) {
		this.registrationEndDate = registrationEndDate;
	}

<<<<<<< HEAD
=======
	public String getDateRange() {

		return getRegistrationBeginDate() + " - " + registrationEndDate;
	}


>>>>>>> parent of 1cd784f (Final List Implementation Finished)
}
