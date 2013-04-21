package poker.entities;

public class StoryEstimate {
	private Story	story;
	private String	estimate;
	private String	complexityString;

	public StoryEstimate(Story story, String estimate, String complexityString) {
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

	public String getEstimate() {
		return estimate;
	}

	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	public String getComplexityString() {
		return complexityString;
	}

	public void setComplexityString(String complexityString) {
		this.complexityString = complexityString;
	}
}
