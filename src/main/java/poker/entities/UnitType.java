package poker.entities;

public enum UnitType {
	PERSON_HOURS(1), PERSON_DAYS(2), PERSON_WEEK(3), PERSON_MONTHS(4), PERSON_YEARS(5);

	private int code;

	private UnitType(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
