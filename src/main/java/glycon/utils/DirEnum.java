package glycon.utils;

import java.io.File;

public enum DirEnum {

	FIRM_DISCLOSURE_PATH("firmDisclosures" + File.separator), FIRM_MANAGERS_PATH("firmManagers" + File.separator),
	MANAGER_DISCLOSURE_PATH("managerDisclosures" + File.separator),FINAL_PATH("final" + File.separator);

	private final String value;

	DirEnum(String string) {

		this.value = string;
	}

	@Override
	public String toString() {
		return value;
	}

}
