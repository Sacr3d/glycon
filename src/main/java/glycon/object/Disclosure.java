package glycon.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Disclosure implements Comparable<Disclosure> {

	private String eventDate;

	private String disclosureType;

	private String disclosureResolution;

	private String disclosureDetailString;

	private Date eventDateObject;

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getDisclosureType() {
		return disclosureType;
	}

	public void setDisclosureType(String disclosureType) {
		this.disclosureType = disclosureType;
	}

	public String getDisclosureResolution() {
		return disclosureResolution;
	}

	public void setDisclosureResolution(String disclosureResolution) {
		this.disclosureResolution = disclosureResolution;
	}

	public String getDisclosureDetailString() {
		return disclosureDetailString;
	}

	public void setDisclosureDetailString(String disclosureDetailString) {
		this.disclosureDetailString = disclosureDetailString;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Disclosure) {
			return ((Disclosure) obj).disclosureDetailString.contentEquals(disclosureDetailString);
		}

		return false;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		return prime + (this.disclosureDetailString == null ? 0 : this.disclosureDetailString.hashCode());
	}

	@Override
	public int compareTo(Disclosure o) {
		if (getEventDateObject() != null) {
			return getEventDateObject().compareTo(o.getEventDateObject());
		}

		return 0;
	}

	public Date getEventDateObject() {
		return eventDateObject;
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
