package glycon;

public enum argsEnum {

	AF("-af"), AS("-aS"), F("-f"), T("-t");

	private final String arg;

	argsEnum(String arg) {

		this.arg = arg;

	}

	public String getArgs() {
		return arg;
	}

}
