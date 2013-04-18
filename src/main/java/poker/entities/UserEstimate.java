package poker.entities;

public class UserEstimate {
	private User user;
	private Estimate estimate;

	public UserEstimate(User user, Estimate estimate) {
		this.setUser(user);
		this.setEstimate(estimate);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Estimate getEstimate() {
		return estimate;
	}

	public void setEstimate(Estimate estimate) {
		this.estimate = estimate;
	}
	
}
