package glycon.utils.csv;

public enum CSVTermsEnum {

	ALLEGATIONS("ALLEGATIONS"), CURRENT_STATUS("CURRENT_STATUS"), DISCLOSURE_DATE("DISCLOSURE_DATE"),
	DISCLOSURE_INFO("DISCLOSURE_INFO"), DISCLOSURE_NO("DISCLOSURE_NO"), DISCLOSURE_RESULT("DISCLOSURE_RESULT"),
	DISCLOSURE_TYPE("DISCLOSURE_TYPE"), DOCKET_CASE_NO("DOCKET_CASE_NO"), EXAM_CATEGORY("EXAM_CATEGORY"),
	EXAM_DATE_TAKEN("EXAM_DATE_TAKEN"), EXAM_NAME("EXAM_NAME"), EXAM_SCOPE("EXAM_SCOPE"), EXAM_TYPE("EXAM_TYPE"),
	FINRA_EMPLOYMENT("FINRA_EMPLOYMENT"), FINRA_EMPLOYMENT_DATES("FINRA_EMPLOYMENT_DATES"), FIRM_ID("FIRM_ID"),
	FIRM_NAME("FIRM_NAME"), FIRST_NAME("FIRST_NAME"), ID("ID"), OTHER_NAMES("OTHER_NAMES"),
	REPORTING_SOURCE("REPORTING_SOURCE"), RESOLUTION("RESOLUTION"), RESOLUTION_DATE("RESOLUTION_DATE"),
	SANCTION_DETIALS("SANCTION_DETAILS"), SEARCH_TERM("SEARCH_TERM"), SECOND_NAME("SECOND_NAME"), FIRM_CRD("FIRM_CRD"),
	FIRM_NAMES("FIRM_NAMES"), FIRM_SEC("FIRM_SEC"), FIRM_CITY("FIRM_CITY"), FIRM_STATE("FIRM_STATE"),
	FIRM_ZIP("FIRM_ZIP");

	public static String[] getFinalManagerHeaders() {

		return new String[] { FIRM_ID.toString(), ID.toString(), FIRST_NAME.toString(), OTHER_NAMES.toString(),
				SECOND_NAME.toString(), DISCLOSURE_DATE.toString(), DISCLOSURE_TYPE.toString(),
				DISCLOSURE_RESULT.toString(), DISCLOSURE_INFO.toString(), FINRA_EMPLOYMENT_DATES.toString(),
				FINRA_EMPLOYMENT.toString(), EXAM_TYPE.toString(), EXAM_CATEGORY.toString(), EXAM_NAME.toString(),
				EXAM_DATE_TAKEN.toString(), EXAM_SCOPE.toString() };

	}

	static String[] getFirmFinalInfo() {
		return new String[] { FIRM_CRD.toString(), FIRM_NAME.toString(), FIRM_SEC.toString(), FIRM_CITY.toString(),
				FIRM_STATE.toString(), FIRM_ZIP.toString(), FIRM_NAMES.toString(), DISCLOSURE_TYPE.toString(),
				DISCLOSURE_NO.toString(), REPORTING_SOURCE.toString(), CURRENT_STATUS.toString(),
				DOCKET_CASE_NO.toString(), ALLEGATIONS.toString(), DISCLOSURE_DATE.toString(),
				RESOLUTION_DATE.toString(), RESOLUTION.toString(), SANCTION_DETIALS.toString() };
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
