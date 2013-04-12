package poker.entities;

import java.sql.Connection;

public abstract class Entity {
	private int myId;
	
	public abstract void Commit(Connection connection);
	
	public abstract String toString();
}
