package glycon;

public enum argsEnum {

	AF("-af"), F("-f"), T("-t"), AS("-aS");

	private final String arg;

	argsEnum(String arg) {

		this.arg = arg;

	}

	public String getArgs() {
		return arg;
	}

}
