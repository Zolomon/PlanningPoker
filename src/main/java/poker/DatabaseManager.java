package poker;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poker.entities.Estimate;
import poker.entities.Story;
import poker.entities.Task;
import poker.entities.UnitType;
import poker.entities.User;

public class DatabaseManager implements IEntityManager {
	private static final String	JDBC_SQLITE_POKER_DB	= "jdbc:sqlite:poker.db";
	private Connection			connection				= null;
	private SimpleDateFormat	dateFormat				= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private OutputStream		debug;
	private boolean				debugging				= true;

	private void debug(String msg) {
		if (debug == null) {
			System.out.println(msg);
			return;
		}

		try {
			debug.write(msg.getBytes(Charset.forName("UTF-8")));
			debug.write('\n');
			debug.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DatabaseManager(OutputStream stream) {
		init();
		this.debug = stream;
	}

	public void init() {

		try {
			connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

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
		statement.execute("create table tasks ( " + "id integer primary key autoincrement, "
		// task's name
				+ "name text, "
				// task's description
				+ "description text, "
				// When it was created
				+ "created_at datetime DEFAULT (datetime('now', 'localtime')), "
				// When it was published
				+ "published_at datetime " + ")");

		// This table will store the individual estimations created by each
		// task
		statement.execute("drop table if exists estimations");
		statement.execute("create table estimations (" + "id integer primary key autoincrement, "
		// task.id
				+ "task_id integer, "
				// the complexity symbol (can be special, like coffee mug) [what
				// will be shown on the card]
				+ "complexity_symbol text, "
				// the unit, we should create an enum for this that uses the
				// same integer values
				+ "unit integer DEFAULT 1, "
				// the value of this complexity in its unit
				+ "unit_value integer " + ")");

		// This table will store the individual users
		statement.execute("drop table if exists users");
		statement.execute("create table users ( " + "id integer primary key autoincrement, "
		// user's name
				+ "name text)");

		// This table will store the team of users for each task
		statement.execute("drop table if exists task_team");
		statement.execute("create table task_team ( " + "id integer primary key autoincrement, "
		// users.id
				+ "user_id integer, "
				// task.id
				+ "task_id integer " + ")");

		// This table will store the individual stories
		statement.execute("drop table if exists stories");
		statement.execute("create table stories ( " + "id integer primary key autoincrement, "
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
				+ "iteration integer DEFAULT 0)");

		// This table will store the estimations for each user
		statement.execute("drop table if exists story_user_estimations");
		statement.execute("create table story_user_estimations ( " + "id integer primary key autoincrement, "
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

		if (debugging) {
			insertUser(new User("Bengt"));
			insertUser(new User("Soheil"));
			insertUser(new User("Alexander"));
			insertUser(new User("Anders"));
			insertUser(new User("Daniel"));

			insertTask(new Task("Planning Poker", "Implement Planning Poker"));
			insertTask(new Task("Write manual", "Write the manual for our implementation of PLanning Poker"));

			createFibonacciEstimations(1);
			createFibonacciEstimations(2);

			insertStory(new Story(1, "Database Operations", "Implement the database operations"));
			insertStory(new Story(1, "Write templates", "Implement the templates for the different pages of the game"));
			insertStory(new Story(1, "Set up the routes", "Create the rotues for the different pages of the game"));

			insertStory(new Story(2, "Create LaTeX document", "Create the LaTeX document"));

			addUserToTask(1, 1);
			addUserToTask(1, 2);
			addUserToTask(1, 3);

			addUserToTask(2, 4);
			addUserToTask(2, 5);
		}
	}

	private void createFibonacciEstimations(int task_id) throws SQLException {
		insertEstimate(new Estimate(task_id, "0", UnitType.PERSON_DAYS, 0));
		insertEstimate(new Estimate(task_id, "½", UnitType.PERSON_DAYS, 0.5f));
		insertEstimate(new Estimate(task_id, "1", UnitType.PERSON_DAYS, 1f));
		insertEstimate(new Estimate(task_id, "2", UnitType.PERSON_DAYS, 2f));
		insertEstimate(new Estimate(task_id, "3", UnitType.PERSON_DAYS, 3f));
		insertEstimate(new Estimate(task_id, "5", UnitType.PERSON_DAYS, 5f));
		insertEstimate(new Estimate(task_id, "8", UnitType.PERSON_DAYS, 8f));
		insertEstimate(new Estimate(task_id, "13", UnitType.PERSON_DAYS, 13f));
		insertEstimate(new Estimate(task_id, "20", UnitType.PERSON_DAYS, 20f));
		insertEstimate(new Estimate(task_id, "40", UnitType.PERSON_DAYS, 40f));
		insertEstimate(new Estimate(task_id, "100", UnitType.PERSON_DAYS, 100f));
		insertEstimate(new Estimate(task_id, "?", UnitType.PERSON_DAYS, -1));
		insertEstimate(new Estimate(task_id, "coffee", UnitType.PERSON_DAYS, -1));
	}

	@Override
	public Task getTask(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT id, name, description, datetime(created_at), datetime(published_at) FROM tasks where id=? LIMIT 1");
		ps.setInt(1, id);

		Task task = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			try {
				task = new Task(res.getInt("id"), res.getString("name"), res.getString("description"),
						new java.sql.Date(dateFormat.parse(res.getString("datetime(created_at)")).getTime()),
						(res.getString("datetime(published_at)")) == null ? null : new java.sql.Date(dateFormat.parse(
								res.getString("datetime(published_at)")).getTime()));
				debug("Fetching task: " + task.toString());
			} catch (ParseException e) {
				System.err.println("Error parsing tasks.created_at using task id: " + res.getInt("id"));
				e.printStackTrace();
			}
		}

		connection.close();

		return task;
	}

	@Override
	public void setTask(Task task) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("UPDATE tasks set name=?, created_at=?, published_at=? where id=?");
		ps.setString(1, task.getName());
		ps.setDate(2, task.getCreatedAt());
		ps.setDate(3, task.getPublishedAt());
		ps.setInt(4, task.getId());

		debug("Setting task: " + task.toString());

		ps.executeUpdate();

		connection.close();
	}

	public void insertTask(Task task) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
		PreparedStatement ps = connection.prepareStatement("INSERT into tasks (name, description) values (?,?)");
		ps.setString(1, task.getName());
		ps.setString(2, task.getDescription());

		debug("Inserting task: " + task.toString());

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void deleteTask(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		// Clean stories
		List<Story> stories = getStoriesFromTask(id);		
		for(Story story : stories) {
			deleteStory(story.getId());
		}
		
		// Clean users
		List<User> users = getUsersFromTask(id);
		for(User user : users) {
			deleteUserFromTask(id, user.getId());
		}
		
		PreparedStatement ps = connection.prepareStatement("DELETE FROM tasks where id=?");
		ps.setInt(1, id);	
		
		debug("Deleting task id: " + id);

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public Story getStory(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT id, task_id, name, description, consensus, iteration FROM stories where id=? LIMIT 1");
		ps.setInt(1, id);

		Story story = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			story = new Story(res.getInt("id"), res.getInt("task_id"), res.getString("name"),
					res.getString("description"), res.getInt("consensus"), res.getInt("iteration"));

			debug("Fetching story: " + story.toString());
		}

		connection.close();

		return story;
	}

	@Override
	public void setStory(Story story) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("UPDATE stories SET name=?, description=?, consensus=?, iteration=? where id=?");
		ps.setString(1, story.getName());
		ps.setString(2, story.getDescription());
		ps.setInt(3, story.getConsensus());
		ps.setInt(4, story.getIteration());
		ps.setInt(5, story.getId());

		debug("Setting story: " + story.toString());

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void insertStory(Story story) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
		PreparedStatement ps = connection
				.prepareStatement("INSERT into stories (task_id, name, description) values (?,?,?)");
		ps.setInt(1, story.getTaskId());
		ps.setString(2, story.getName());
		ps.setString(3, story.getDescription());

		debug("Insering story: " + story.toString());

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void deleteStory(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		HashMap<User,List<Estimate>> storyEstimates = getEstimatesFromStory(id);
		
		// Delete all story estimates
		for (List<Estimate> estimates : storyEstimates.values())
		{
			for (Estimate estimate : estimates) {
				deleteEstimateFromStory(id, estimate.getId());
			}
		}
		
		PreparedStatement ps = connection.prepareStatement("DELETE FROM stories where id=?");
		ps.setInt(1, id);

		debug("Deleting story with id: " + id);

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public User getUser(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection.prepareStatement("SELECT id, name FROM users where id=? LIMIT 1");
		ps.setInt(1, id);

		User user = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			user = new User(res.getInt("id"), res.getString("name"));
			debug("Fetching user: " + user.toString());
		}

		connection.close();

		return user;
	}

	@Override
	public void insertUser(User user) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
		PreparedStatement ps = connection.prepareStatement("INSERT into users (name) values (?)");
		ps.setString(1, user.getName());

		debug("Inserting user: " + user.toString());

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void deleteUser(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
		
		// Should delete from task_team
		PreparedStatement ps = connection.prepareStatement("DELETE FROM task_team where user_id=?");
		ps.setInt(1, id);

		debug("Deleting user from all tasks: " + id);

		ps.executeUpdate();
		
		// Should delete from story_user_estimations
		ps = connection.prepareStatement("DELETE FROM story_user_estimations where user_id=?");
		ps.setInt(1, id);

		debug("Deleting all estimations for user: " + id);

		ps.executeUpdate();
		
		// Delete user at last
		ps = connection.prepareStatement("DELETE FROM users where id=?");
		ps.setInt(1, id);

		debug("Deleting user with id: " + id);

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void setUser(User user) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection.prepareStatement("UPDATE users SET name=? where id=?");
		ps.setString(1, user.getName());
		ps.setInt(2, user.getId());

		debug("Setting user: " + user.toString());

		ps.executeUpdate();

		connection.close();

	}

	@Override
	public Estimate getEstimate(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT id, task_id, complexity_symbol, unit, unit_value FROM estimations where id=? LIMIT 1");
		ps.setInt(1, id);

		Estimate estimate = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			estimate = new Estimate(res.getInt("id"), res.getInt("task_id"), res.getString("complexity_symbol"),
					UnitType.values()[res.getInt("unit")], res.getInt("unit_value"));
			debug("Fetching estimate: " + estimate.toString());
		}

		connection.close();

		return estimate;
	}

	@Override
	public void setEstimate(Estimate estimate) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("UPDATE estimations SET complexity_symbol=?, unit=?, unit_value=? where id=?");
		ps.setString(1, estimate.getComplexitySymbol());
		ps.setInt(2, estimate.getUnit().getCode());
		ps.setFloat(3, estimate.getUnitValue());
		ps.setInt(4, estimate.getId());

		debug("Setting estimate: " + estimate.toString());

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void insertEstimate(Estimate estimate) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
		PreparedStatement ps = connection
				.prepareStatement("INSERT into estimations (task_id, complexity_symbol, unit, unit_value) values (?,?,?,?)");
		ps.setInt(2, estimate.getTaskId());
		ps.setString(1, estimate.getComplexitySymbol());
		ps.setInt(2, estimate.getUnit().getCode());
		ps.setFloat(3, estimate.getUnitValue());

		debug("Inserting estimate: " + estimate.toString());

		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void deleteEstimate(int id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection.prepareStatement("DELETE FROM estimations where id=?");
		ps.setInt(1, id);

		debug("Deleting estimate with id: " + id);
		ps.executeUpdate();

		connection.close();
	}

	@Override
	public List<Story> getStoriesFromTask(int task_id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT id, task_id, name, description, consensus, iteration FROM stories where task_id=?");
		ps.setInt(1, task_id);

		List<Story> stories = new ArrayList<Story>();
		Story story = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			story = new Story(res.getInt("id"), res.getInt("task_id"), res.getString("name"),
					res.getString("description"), res.getInt("consensus"), res.getInt("iteration"));
			stories.add(story);
			debug("Fetching story: " + story.toString());
		}

		connection.close();

		return stories;
	}

	@Override
	public List<User> getUsersFromTask(int task_id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT id, name from users join task_team on users.id=task_team.user_id where task_team.task_id=?");
		ps.setInt(1, task_id);

		List<User> users = new ArrayList<User>();
		User user = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			user = new User(res.getInt("User.id"), res.getString("User.name"));
			users.add(user);
			debug("Fetching user: " + user.toString());
		}

		connection.close();

		return users;
	}

	@Override
	public HashMap<User, List<Estimate>> getEstimatesFromStory(int story_id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT estimations.id, task_id, complexity_symbol, unit, unit_value FROM estimations where id=? LIMIT 1");
		ps.setInt(1, story_id);

		List<User> users = getUsersFromTask(getStory(story_id).getTaskId());
		HashMap<User, List<Estimate>> storyEstimations = new HashMap<User, List<Estimate>>();

		for (User user : users) {
			storyEstimations.put(user, getEstimatesFromUser(user.getId()));
		}

		connection.close();

		return storyEstimations;
	}

	@Override
	public List<Estimate> getEstimatesFromUser(int user_id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT estimations.id, estimations.task_id, estimations.complexity_symbol, estimations.unit, estimations.unit_value FROM estimations join story_user_estimations on story_user_estimations.estimation_id=estimations_id where user_id=?");
		ps.setInt(1, user_id);

		List<Estimate> estimations = new ArrayList<Estimate>();
		Estimate estimate = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			estimate = new Estimate(res.getInt("id"), res.getInt("task_id"), res.getString("complexity_symbol"),
					UnitType.values()[res.getInt("unit")], res.getInt("unit_value"));
			debug("Fetching estimate: " + estimate.toString());
			estimations.add(estimate);
		}

		connection.close();

		return estimations;
	}

	@Override
	public void deleteEstimateFromStory(int story_id, int estimate_id) throws SQLException {
		 connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
		
		 PreparedStatement ps =
		 connection.prepareStatement("DELETE FROM story_user_estimations where story_id = ? and estimate_id=?");
		 ps.setInt(1, story_id);
		 ps.setInt(2, estimate_id);
		
		 debug(String.format("Deleting estimate [%d] from story [%d]", estimate_id, story_id));
		 ps.executeUpdate();
		
		 connection.close();
	}
	@Override
	public void deleteUserFromTask(int task_id, int user_id) throws SQLException {
		 connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
			
		 PreparedStatement ps =
		 connection.prepareStatement("DELETE FROM task_team where task_id = ? and user_id=?");
		 ps.setInt(1, task_id);
		 ps.setInt(2, user_id);
		
		 debug(String.format("Deleting user [%d] from task [%d]", user_id, task_id));
		 ps.executeUpdate();
		
		 connection.close();
	}
	
	@Override
	public void addUserToTask(int task_id, int user_id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection.prepareStatement("INSERT INTO task_team (user_id, task_id) VALUES (?,?)");
		ps.setInt(1, task_id);
		ps.setInt(2, user_id);

		debug("Adding user [" + user_id + "] to task [" + task_id + "]");
		ps.executeUpdate();

		connection.close();
	}

	@Override
	public void addEstimateToStory(int story_id, int user_id, int estimate_id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("INSERT INTO story_user_estimations (story_id, user_id, estimation_id) VALUES (?,?,?)");
		ps.setInt(1, story_id);
		ps.setInt(2, user_id);
		ps.setInt(3, estimate_id);

		debug("Adding estimate [" + estimate_id + "] to story [" + story_id + "] for user [" + user_id + "]");
		ps.executeUpdate();

		connection.close();
	}

	@Override
	public List<Estimate> getEstimationsForTask(int task_id) throws SQLException {
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);

		PreparedStatement ps = connection
				.prepareStatement("SELECT id, task_id, complexity_symbol, unit, unit_value from estimations where task_id = ? ");
		ps.setInt(1, task_id);

		debug("Fetching estimate with id: " + task_id);

		List<Estimate> estimations = new ArrayList<Estimate>();

		Estimate estimate = null;

		ResultSet res = ps.executeQuery();

		while (res.next()) {
			estimate = new Estimate(res.getInt("id"), res.getInt("task_id"), res.getString("complexity_symbol"),
					UnitType.values()[res.getInt("unit")], res.getInt("unit_value"));
			debug("Fetching estimate: " + estimate.toString());
			estimations.add(estimate);
		}

		connection.close();
		return estimations;
	}
	
	public List<Task> getTasks() throws SQLException{
		connection = DriverManager.getConnection(JDBC_SQLITE_POKER_DB);
		PreparedStatement ps = connection
				.prepareStatement("SELECT id from tasks");
		List<Task> tasks = new ArrayList<Task>();

		debug("Getting all tasks");
		
		ResultSet res = ps.executeQuery();
		while (res.next()) {
			tasks.add(getTask(res.getInt("id")));
		}
		
		connection.close();
		
		return tasks;
		
	}

}
