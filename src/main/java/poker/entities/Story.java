package poker.entities;

public class Story {
	private int id;
	private int task_id;
	private String name;
	private String description;
	private int consensus;
	private int iteration;

	/***
	 * Constructor for new Story
	 * @param task_id
	 * @param name
	 * @param description
	 */
	public Story(int task_id, String name, String description) {
		this.setTaskId(task_id);
		this.setName(name);
		this.setDescription(description);
	}
	
	/***
	 * Constructor to use when fetching from SQLite
	 * @param id
	 * @param task_id
	 * @param name
	 * @param description
	 * @param consensus
	 * @param iteration
	 */
	public Story(int id, int task_id, String name, String description,
			int consensus, int iteration) {
		this.setId(id);
		this.setTaskId(task_id);
		this.setName(name);
		this.setDescription(description);
		this.setConsensus(consensus);
		this.setIteration(iteration);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getConsensus() {
		return consensus;
	}

	public void setConsensus(int consensus) {
		this.consensus = consensus;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
}
