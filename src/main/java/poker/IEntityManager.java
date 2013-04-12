package poker;

import java.util.List;

import poker.entities.*;

public interface IEntityManager {
	Task getTask(int id);
	void setTask(Task task);
	
	Story getStory(int id);
	void setStory(Story story);
	
	User getUser(int id);
	void setUser(User user);
	
	Estimate getEstimate(int id);
	void setEstimate(Estimate estimate);
	
	List<Story> getStoriesFromTask(Task task);
	List<User> getUsersFromTask(Task task);
	List<Estimate> getEstimatesFromStory(Story story);
	List<Estimate> getEstimatesFromUser(User user);
		
	void addUserToTask(Task task, User user);
	void addStoryToTask(Task task, Story story);
	void addEstimateToStory(Story story, Estimate estimate);
}
