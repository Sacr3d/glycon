package glycon.object.manager;

public class CurrentEmployment extends Employment {

	public String getDateRange() {

		return getRegistrationBeginDate() + " - Current";

	}

}
