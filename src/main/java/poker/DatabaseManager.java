package poker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	Connection connection = null;

	public DatabaseManager() {
		init();
	}

	public void init() {

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:poker.db");

			createTables(connection);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	private void createTables(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30); // timeout after 30 seconds

		// this table will store the individual tasks
		statement.execute("drop table if exists tasks");
		statement
				.execute("create table tasks ( "
						+ "id integer primary key autoincrement, "
						// task's name
						+ "name text, "
						// When it was created
						+ "created_at datetime DEFAULT (datetime('now', 'localtime')), "
						// When it was published
						+ "published_at datetime " + ")");

		// This table will store the individual estimations created by each
		// task
		statement.execute("drop table if exists estimations");
		statement.execute("create table estimations ("
				+ "id integer primary key autoincrement, "
				// task.id
				+ "task_id integer, "
				// the complexity value [what will be shown on the card]
				+ "complexity integer, "
				// the unit, we should create an enum for this that uses the
				// same integer values
				+ "unit integer, "
				// the value of this complexity in its unit
				+ "unit_value integer " + ")");

		// This table will store the individual users
		statement.execute("drop table if exists users");
		statement.execute("create table users ( "
				+ "id integer primary key autoincrement, "
				// user's name
				+ "name text, "
				// when it was created
				+ "created_at datetime DEFAULT (datetime('now', 'localtime')) "
				+ ")");

		// This table will store the team of users for each task
		statement.execute("drop table if exists task_team");
		statement.execute("create table task_team ( "
				+ "id integer primary key autoincrement, "
				// users.id
				+ "user_id integer, "
				// task.id
				+ "task_id integer " + ")");

		// This table will store the individual stories
		statement.execute("drop table if exists stories");
		statement.execute("create table stories ( "
				+ "id integer primary key autoincrement, "
				// tasks.id
				+ "task_id integer, "
				// story's name
				+ "name text, "
				// story's description
				+ "description text, "
				// will be set to the final value after consensus has been
				// reached
				+ "consensus integer DEFAULT -1,"
				// current iteration, so we can figure out consensus and how
				// long it took etc. etc.
				+ "iteration integer DEFAULT 0" + ")");

		// This table will store the estimations for each user
		statement.execute("drop table if exists story_user_estimations");
		statement.execute("create table story_user_estimations ( "
				+ "id integer primary key autoincrement, "
				// story.id
				+ "story_id integer, "
				// users.id
				+ "user_id integer, "
				// estimations.id
				+ "estimation_id integer, "
				// the value of the current iteration of the story id when
				// insert (so we can keep track of during which iteration the
				// estimate was made)
				+ "story_iteration integer" + ")");
	}
}
