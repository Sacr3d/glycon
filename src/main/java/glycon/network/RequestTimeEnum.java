package glycon.network;

public enum RequestTimeEnum {

	CONNECTION_TIMEOUT(10000), DEV_TIME_PER_REQUEST(1000), NASTY_TIME_PER_REQUEST(250),
	NORMAL_TIME_PER_REQUEST(2000), POLITE_TIME_PER_REQUEST(4000);

	private final int value;

	RequestTimeEnum(int i) {

		this.value = i;

	}

	public int getValue() {
		return value;
	}

}
