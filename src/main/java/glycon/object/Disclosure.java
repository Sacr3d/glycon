package glycon.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class Disclosure implements Comparable<Disclosure> {

	private String disclosureDetailString;

	private String disclosureResolution;

	private String disclosureType;

	private String eventDate;

	private Date eventDateObject;

	@Override
	public int compareTo(Disclosure o) {
		if (getEventDateObject() != null) {
			return getEventDateObject().compareTo(o.getEventDateObject());
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Disclosure) {
			return ((Disclosure) obj).disclosureDetailString.contentEquals(disclosureDetailString);
		}

		return false;
	}

	public String getDisclosureDetailString() {
		return disclosureDetailString;
	}

	public String getDisclosureResolution() {
		return disclosureResolution;
	}

	public String getDisclosureType() {
		return disclosureType;
	}

	public String getEventDate() {
		return eventDate;
	}

	public Date getEventDateObject() {
		return eventDateObject;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		return prime + (this.disclosureDetailString == null ? 0 : this.disclosureDetailString.hashCode());
	}

	public void setDisclosureDetailString(String disclosureDetailString) {
		this.disclosureDetailString = disclosureDetailString;
	}

	public void setDisclosureResolution(String disclosureResolution) {
		this.disclosureResolution = disclosureResolution;
	}

	public void setDisclosureType(String disclosureType) {
		this.disclosureType = disclosureType;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventDateObject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

		try {
			if (this.eventDate != null) {
				this.eventDateObject = formatter.parse(this.eventDate);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
