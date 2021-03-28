package glycon.object;

public class CurrentEmployment extends Employment {

	public String getDateRange() {

		return getRegistrationBeginDate() + " - Current";

	}

}
