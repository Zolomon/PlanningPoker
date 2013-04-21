package poker.entities;

public class StoryEstimate {
	private Story	story;
	private float	estimate;
	private String	complexityString;

	public StoryEstimate(Story story, float estimate, String complexityString) {
		this.setStory(story);
		this.setEstimate(estimate);
		this.setComplexityString(complexityString);
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

	public String getComplexityString() {
		return complexityString;
	}

	public void setComplexityString(String complexityString) {
		this.complexityString = complexityString;
	}
}
