package glycon.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Examination implements Comparable<Examination> {

	private String category;

	private String examCategory;

	private String examName;

	private String examTakenDate;

	private String examScope;

	protected Date examDateTakenDateObject;

	public Date getExamDateTakenDateObject() {
		return examDateTakenDateObject;
	}

	public String getCategory() {
		return category;
	}

	public String getExamCategory() {
		return examCategory;
	}

	public String getExamName() {
		return examName;
	}

	public String getExamScope() {
		return examScope;
	}

	public String getExamTakenDate() {
		return examTakenDate;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setExamCategory(String examCategory) {
		this.examCategory = examCategory;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public void setExamScope(String examScope) {
		this.examScope = examScope;
	}

	public void setExamTakenDate(String examTakenDate) {
		this.examTakenDate = examTakenDate;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		return prime + (this.examTakenDate == null ? 0 : this.examTakenDate.hashCode() + this.examName.hashCode());
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Examination) {
			return ((((Examination) obj).examName.contentEquals(examName))
					&& (((Examination) obj).examTakenDate.contentEquals(examTakenDate)));
		}

		return false;
	}

	public void setExamDateTakenDateObject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

		try {

			this.examDateTakenDateObject = formatter.parse(this.examTakenDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(Examination o) {
		return o != null ? getExamDateTakenDateObject().compareTo(o.getExamDateTakenDateObject()) : 0;

	}
}
