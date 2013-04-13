package poker.entities;

import java.sql.Date;
import java.util.List;

public class Task {
	private int id;
	private String name;
	private String description;
	private Date created_at;
	private Date published_at;

	/***
	 * Constructor for new Task
	 * @param name
	 * @param description
	 */
	public Task(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}

	/***
	 * Constructor to use when fetched from SQLite
	 * @param id
	 * @param name
	 * @param description
	 * @param created_at
	 * @param published_at
	 */
	public Task(int id, String name, String description, Date created_at,
			Date published_at) {
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setCreatedAt(created_at);
		this.setPublishedAt(published_at);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(Date created_at) {
		this.created_at = created_at;
	}

	public Date getPublishedAt() {
		return published_at;
	}

	public void setPublishedAt(Date published_at) {
		this.published_at = published_at;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {

		return "["
				+ getId()
				+ ","
				+ getName()
				+ ", "
				+ (getDescription().length() > 10 ? getDescription().substring(
						0, 9) : getDescription()) + ", "
				+ (getCreatedAt() != null ? getCreatedAt().toString() : "null") + ", " + (getPublishedAt() != null ? getPublishedAt().toString() : "null") + "]";
	}

}
