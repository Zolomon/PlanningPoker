package poker.entities;

public class StoryEstimate {
	private Story story;
	private float estimate;
	
	public StoryEstimate(Story story, float estimate) {
		this.setStory(story);
		this.setEstimate(estimate);
	}

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
	}

	public float getEstimate() {
		return estimate;
	}

	public void setEstimate(float estimate) {
		this.estimate = estimate;
	}
}
