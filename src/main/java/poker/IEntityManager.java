package poker;

import java.sql.SQLException;
import java.util.List;

import poker.entities.*;

public interface IEntityManager {
	Task getTask(int id) throws SQLException;
	void setTask(Task task) throws SQLException;
	
	Story getStory(int id) throws SQLException;
	void setStory(Story story) throws SQLException;
	
	User getUser(int id) throws SQLException;
	void setUser(User user) throws SQLException;
	
	Estimate getEstimate(int id) throws SQLException;
	void setEstimate(Estimate estimate) throws SQLException;
	
	List<Story> getStoriesFromTask(Task task) throws SQLException;
	List<User> getUsersFromTask(Task task) throws SQLException;
	List<Estimate> getEstimatesFromStory(Story story) throws SQLException;
	List<Estimate> getEstimatesFromUser(User user) throws SQLException;
		
	void addUserToTask(Task task, User user) throws SQLException;
	void addStoryToTask(Task task, Story story) throws SQLException;
	void addEstimateToStory(Story story, Estimate estimate) throws SQLException;
}
