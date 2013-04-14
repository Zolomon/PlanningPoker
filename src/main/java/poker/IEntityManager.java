package poker;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.javatuples.Pair;

import poker.entities.*;

public interface IEntityManager {
	Task getTask(int id);
	void setTask(Task task);
	void insertTask(Task task);
	void deleteTask(int id);
	
	Story getStory(int id);
	void setStory(Story story);
	void insertStory(Story story);
	void deleteStory(int id);
	
	User getUser(int id);
	void setUser(User user);
	void insertUser(User user);
	void deleteUser(int id);
	
	Estimate getEstimate(int id);
	void setEstimate(Estimate estimate);
	void insertEstimate(Estimate estimate);
	void deleteEstimate(int id);
	
	List<Story> getStoriesFromTask(int task_id);
	List<User> getUsersFromTask(int task_id);
	List<Estimate> getEstimationsForTask(int task_id);
	HashMap<User, List<Estimate>> getEstimatesFromStory(int story_id);
	List<Estimate> getEstimatesFromUser(int user_id);
		
	void addUserToTask(int task_id, int user_id);
	void addEstimateToStory(int story_id, int user_id, int estimate_id);
	
	void deleteUserFromTask(int task_id, int user_id);
	void deleteEstimateFromStory(int story_id, int estimate_id);
	
	//void deleteAllStoriesForTask(int task_id);
	//void deleteAllEstimationsForUser(int user_id);
	//void deleteAllEstimationsForStory(int story_id);
}
