package glycon.utils.csv;

public enum CSVTermsEnum {

	ID("ID"), FIRM_NAME("FIRM_NAME"), SEARCH_TERM("SEARCH_TERM"), SECOND_NAME("SECOND_NAME"),
	OTHER_NAMES("OTHER_NAMES"), FIRST_NAME("FIRST_NAME"), FIRM_ID("FIRM_ID"), DISCLOSURE_DATE("DISCLOSURE_DATE"),
	DISCLOSURE_TYPE("DISCLOSURE_TYPE"), DISCLOSURE_RESULT("DISCLOSURE_RESULT"), DISCLOSURE_INFO("DISCLOSURE_INFO"),
	FINRA_EMPLOYMENT_DATES("FINRA_EMPLOYMENT_DATES"), FINRA_EMPLOYMENT("FINRA_EMPLOYMENT"), EXAM_TYPE("EXAM_TYPE"),
	EXAM_CATEGORY("EXAM_CATEGORY"), EXAM_NAME("EXAM_NAME"), EXAM_DATE_TAKEN("EXAM_DATE_TAKEN"),
	EXAM_SCOPE("EXAM_SCOPE");

	public static String[] getFinalManagerHeaders() {

		return new String[] { FIRM_ID.toString(), ID.toString(), FIRST_NAME.toString(), OTHER_NAMES.toString(),
				SECOND_NAME.toString(), DISCLOSURE_DATE.toString(), DISCLOSURE_TYPE.toString(),
				DISCLOSURE_RESULT.toString(), DISCLOSURE_INFO.toString(), FINRA_EMPLOYMENT_DATES.toString(),
				FINRA_EMPLOYMENT.toString(), EXAM_TYPE.toString(), EXAM_CATEGORY.toString(), EXAM_NAME.toString(),
				EXAM_DATE_TAKEN.toString(), EXAM_SCOPE.toString() };

	}

	public static String[] getFriendlyHeaders() {

		return new String[] { FIRM_ID.toString(), FIRM_NAME.toString(), SEARCH_TERM.toString() };
	}

	public static String[] getManagerHeaders() {

		return new String[] { ID.toString(), FIRST_NAME.toString(), OTHER_NAMES.toString(), SECOND_NAME.toString() };

	}

	private final String value;

	CSVTermsEnum(String string) {

		this.value = string;
	}

	@Override
	public String toString() {
		return value;
	}

}
