package pt.tecnico.cmu;

import java.util.ArrayList;
import java.util.List;

public class UbiManager {
	static private UbiManager instance = null;

	private List<User> users;
	private List<Bike> bikes;
	private List<Station> stations;

	private UbiManager() {
		users = new ArrayList<User>();
		bikes = new ArrayList<Bike>();
		stations = new ArrayList<Station>();
	}

	static public UbiManager getInstance() {
		if (instance == null)
			instance = new UbiManager();
		return instance;
	}
}
