package glycon.parser.pdf;

public enum FirmInfoTagEnum {

	A("¬Allegations: "), CS("¬Current Status: "), DCN("¬Docket/Case Number: "), DI("¬Date Initiated: "),
	R("¬Resolution: "), RD("¬Resolution Date: "), RS("¬Reporting Source: "), SD("¬Sanction Details: "),
	SARA("¬Sum of All Relief Awarded: "), D("¬Disposition: "), DD("¬Disposition Date: "), CI("¬Case Initiated: "),
	PD("¬Payout Details: "), CN("¬Case Number: ");

	private final String word;

	FirmInfoTagEnum(String word) {

		this.word = word;

	}

	public String getWord() {
		return word;
	}

}
