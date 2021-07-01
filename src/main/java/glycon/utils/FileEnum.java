package glycon.utils;

public enum FileEnum {

	FIRM_WORKBOOK("firm.csv"), FRIENDLY_WORKBOOK("friendly-workbook.csv");

	private final String value;

	FileEnum(String string) {

		this.value = string;
	}

	@Override
	public String toString() {
		return value;
	}
}
