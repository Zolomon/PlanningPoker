package poker.entities;

public class Estimate {
	private int id;
	private int task_id;
	private String complexity_symbol;
	private UnitType unit;
	private float unit_value;

	/***
	 * Constructor for when creating a new Estimate
	 * @param task_id
	 * @param complexity_symbol
	 * @param unit
	 * @param unit_value
	 */
	public Estimate(int task_id, String complexity_symbol, UnitType unit,
			float unit_value) {
		this.setTaskId(task_id);
		this.setComplexitySymbol(complexity_symbol);
		this.setUnit(unit);
		this.setUnitValue(unit_value);
	}

	/***
	 * Constructor to use when fetching from SQLite
	 * @param id
	 * @param task_id
	 * @param complexity_symbol
	 * @param unit
	 * @param unit_value
	 */
	public Estimate(int id, int task_id, String complexity_symbol,
			UnitType unit, float unit_value) {
		this.setId(id);
		this.setTaskId(task_id);
		this.setComplexitySymbol(complexity_symbol);
		this.setUnit(unit);
		this.setUnitValue(unit_value);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskId() {
		return task_id;
	}

	public void setTaskId(int task_id) {
		this.task_id = task_id;
	}

	public String getComplexitySymbol() {
		return complexity_symbol;
	}

	public void setComplexitySymbol(String complexity_symbol) {
		this.complexity_symbol = complexity_symbol;
	}

	public UnitType getUnit() {
		return unit;
	}

	public void setUnit(UnitType unit) {
		this.unit = unit;
	}

	public float getUnitValue() {
		return unit_value;
	}

	public void setUnitValue(float unit_value) {
		this.unit_value = unit_value;
	}
	
	@Override
	public String toString() {
		return String.format("[%d, %d, %s, %s, %f]", getId(), getTaskId(), getComplexitySymbol(), getUnit().name(), getUnitValue()); 
	}
}
