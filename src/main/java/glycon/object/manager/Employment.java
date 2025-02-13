package glycon.object.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class Employment implements Comparable<Employment> {

	protected String firmId;

	protected String firmName;

	protected String registrationBeginDate;

	protected Date registrationBeginDateObject;

	@Override
	public int compareTo(Employment o) {
		return o != null ? getRegistrationBeginDateObject().compareTo(o.getRegistrationBeginDateObject()) : 0;

	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Employment) {
			return ((((Employment) obj).firmId.contentEquals(firmId))
					&& (((Employment) obj).registrationBeginDate.contentEquals(registrationBeginDate)));
		}

		return false;
	}

	public String getDetailedFirmName() {

		return firmName + "(CRD: " + firmId + ")";
	}

	public String getFirmId() {
		return firmId;
	}

	public String getFirmName() {
		return firmName;
	}

	public String getRegistrationBeginDate() {
		return registrationBeginDate;
	}

	public Date getRegistrationBeginDateObject() {
		return registrationBeginDateObject;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		return prime + (this.registrationBeginDate == null ? 0
				: this.registrationBeginDate.hashCode() + this.firmId.hashCode());
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public void setRegistrationBeginDate(String registrationBeginDate) {
		this.registrationBeginDate = registrationBeginDate;
	}

	public void setRegistrationBeginDateObject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

		try {

			this.registrationBeginDateObject = formatter.parse(this.registrationBeginDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
