package glycon.parser.pdf;

public enum FirmDisclosureTypeEnum {

	REGULATORY_FINAL("Regulatory - Final"), REGULATORY_PENDING("Regulatory - Pending"),
	REGULATORY_ON_APPEAL("Regulatory - On Appeal"), CIVIL_FINAL("Civil - Final"), CIVIL_PENDING("Civil - Pending"),
	CIVIL_ON_APPEAL("Civil - On Appeal"), ARBITRATION("Arbitration Award - Award / Judgment"), BOND("Civil Bond");

	private final String word;

	FirmDisclosureTypeEnum(String word) {

		this.word = word;

	}

	public String getWord() {
		return word;
	}

}
