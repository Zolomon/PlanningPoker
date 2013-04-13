package poker.entities;

public class User {
	private int id;
	private String name;

	/***
	 * Constructor for new User
	 * @param name
	 */
	public User(String name) {
		this.setName(name);
	}
	
	/***
	 * Constructor to use when fetching from SQLite
	 * @param id
	 * @param name
	 */
	public User(int id, String name) {
		this.setId(id);
		this.setName(name);
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

}
