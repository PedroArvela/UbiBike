package cmu.tecnico.ubibikeserver.domain;

import java.util.Map;
import java.util.TreeMap;

public class User {
	public String username;
	public String password;
	public String displayName;
	public int points;
	public Map<String, Trajectory> trajectories;

	public User(String username, String password, String displayName) {
		this.username = username;
		this.password = password;
		this.displayName = displayName;
		this.points = 0;

		this.trajectories = new TreeMap<String, Trajectory>();
	}
}
