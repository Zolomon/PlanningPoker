package poker.entities;

public enum UnitType {
	PERSON_HOURS(1), PERSON_DAYS(2), PERSON_MONTHS(3), PERSON_YEARS(4);

	private int code;

	private UnitType(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
