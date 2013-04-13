package poker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import poker.entities.Estimate;
import poker.entities.Story;
import poker.entities.Task;
import poker.entities.User;

public class DatabaseManager implements IEntityManager {
	Connection connection = null;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		statement.execute("create table tasks ( "
				+ "id integer primary key autoincrement, "
				// task's name
				+ "name text, "
				// task's description
				+ "description text, "
				// When it was created
				+ "created_at datetime DEFAULT (datetime('now')), "
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
				+ "created_at datetime DEFAULT (strftime('%s', 'now')) " + ")");

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

	@Override
	public Task getTask(int id) throws SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:poker.db");

		PreparedStatement ps = connection
				.prepareStatement("SELECT (id, name, description, datetime(created_at), datetime(published_at)) FROM tasks where id=? LIMIT 1");
		ps.setInt(1, id);

		Task task = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			task = new Task(res.getInt("id"), res.getString("name"),
					res.getString("description"), res.getDate("created_at"),
					res.getDate("published_at"));
		}

		connection.close();

		return task;
	}

	@Override
	public void setTask(Task task) throws SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:poker.db");

		PreparedStatement ps = connection
				.prepareStatement("UPDATE tasks set name=?, created_at=?, published_at=? where id=?");
		ps.setString(1, task.getName());
		ps.setDate(2, task.getCreatedAt());
		ps.setDate(3, task.getPublishedAt());
		ps.setInt(4, task.getId());

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public Story getStory(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStory(Story story) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUser(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUser(User user) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Estimate getEstimate(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEstimate(Estimate estimate) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Story> getStoriesFromTask(Task task) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsersFromTask(Task task) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Estimate> getEstimatesFromStory(Story story)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Estimate> getEstimatesFromUser(User user) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUserToTask(Task task, User user) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addStoryToTask(Task task, Story story) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEstimateToStory(Story story, Estimate estimate)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void insertTask(Task task) throws SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:poker.db");
		PreparedStatement ps = connection
				.prepareStatement("INSERT into tasks (name, description) values (?,?)");
		ps.setString(1, task.getName());
		ps.setString(2, task.getDescription());
		// ps.setNull(3, sqlType)

		ps.executeUpdate();

		connection.close();
	}
}
