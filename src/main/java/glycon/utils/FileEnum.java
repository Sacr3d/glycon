package glycon.utils;

public enum FileEnum {

	FRIENDLY_WORKBOOK("friendly-workbook.csv");

	private final String value;

	FileEnum(String string) {

		this.value = string;
	}

	@Override
	public String toString() {
		return value;
	}

}
