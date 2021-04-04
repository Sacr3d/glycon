package glycon.utils;

import java.io.File;

public enum DirEnum {

	FIRM_PATH("firms" + File.separator), FIRM_FINAL_PATH("final" + File.separator),
	MANAGER_PATH("managers" + File.separator);

	private final String value;

	DirEnum(String string) {

		this.value = string;
	}

	@Override
	public String toString() {
		return value;
	}

}
