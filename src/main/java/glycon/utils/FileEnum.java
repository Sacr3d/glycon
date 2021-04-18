package glycon.utils;

import java.io.File;

public enum FileEnum {

	FIRM_PATH("firms" + File.separator), FIRM_FINAL_PATH("final" + File.separator),
	MANAGER_PATH("managers" + File.separator), MANAGER_PDF_PATH(MANAGER_PATH.toString() + "legacy" + File.separator),
	FRIENDLY_WORKBOOK("friendly-workbook.csv"), FIRM_WORKBOOK("firm.csv");

	private final String value;

	FileEnum(String string) {

		this.value = string;
	}

	@Override
	public String toString() {
		return value;
	}

}
