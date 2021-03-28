package glycon.network;

public enum RequestTimeEnum {

	POLITE_TIME_PER_REQUEST(4000), NORMAL_TIME_PER_REQUEST(2000), NASTY_TIME_PER_REQUEST(250),
	DEV_TIME_PER_REQUEST(1000), CONNECTION_TIMEOUT(10000);

	private final int value;

	RequestTimeEnum(int i) {

		this.value = i;

	}

	public int getValue() {
		return value;
	}

}
