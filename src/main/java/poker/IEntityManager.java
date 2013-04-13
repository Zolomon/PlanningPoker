package poker;

import java.sql.SQLException;
import java.util.List;

import poker.entities.*;

public interface IEntityManager {
	Task getTask(int id) throws SQLException;
	void setTask(Task task) throws SQLException;
	void insertTask(Task task) throws SQLException;
	void deleteTask(int id) throws SQLException;
	
	Story getStory(int id) throws SQLException;
	void setStory(Story story) throws SQLException;
	void insertStory(Story story) throws SQLException;
	void deleteStory(int id) throws SQLException;
	
	User getUser(int id) throws SQLException;
	void setUser(User user) throws SQLException;
	void insertUser(User user) throws SQLException;
	void deleteUser(int id) throws SQLException;
	
	Estimate getEstimate(int id) throws SQLException;
	void setEstimate(Estimate estimate) throws SQLException;
	void insertEstimate(Estimate estimate) throws SQLException;
	void deleteEstimate(int id) throws SQLException;
	
	List<Story> getStoriesFromTask(int task_id) throws SQLException;
	List<User> getUsersFromTask(int task_id) throws SQLException;
	List<Estimate> getEstimatesFromStory(int story_id) throws SQLException;
	List<Estimate> getEstimatesFromUser(int user_id) throws SQLException;
		
	void addUserToTask(int task_id, User user) throws SQLException;
	void addStoryToTask(int task_id, Story story) throws SQLException;
	void addEstimateToStory(int story_id, Estimate estimate) throws SQLException;
	
	void deleteUserFromTask(int task_id, int user_id) throws SQLException;
	void deleteStoryFromTask(int task_id, int story_id) throws SQLException;
	void deleteEstimateFromStory(int story_id, int estimate_id) throws SQLException;
	
	//void deleteAllStoriesForTask(int task_id) throws SQLException;
	//void deleteAllEstimationsForUser(int user_id) throws SQLException;
	//void deleteAllEstimationsForStory(int story_id) throws SQLException;
}
