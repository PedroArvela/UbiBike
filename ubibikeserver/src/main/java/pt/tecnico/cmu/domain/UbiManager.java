package pt.tecnico.cmu.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UbiManager {
	static private UbiManager instance = null;

	private Map<String, User> users;
	private List<Bike> bikes;
	private List<Station> stations;

	private UbiManager() {
		users = new HashMap<String, User>();
		bikes = new ArrayList<Bike>();
		stations = new ArrayList<Station>();
	}

	static public UbiManager getInstance() {
		if (instance == null)
			instance = new UbiManager();
		return instance;
	}
	
	public boolean userExists(String username) {
		return users.containsKey(username);
	}
	
	public User getUser(String username) {
		return users.get(username);
	}
	
	public void createUser(String username) {
		users.put(username, new User(username));
	}
}
