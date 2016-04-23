package pt.tecnico.cmu.domain;

public class User {
	private String id;
	private int points;

	protected User(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public int getPoint() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
