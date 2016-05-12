package cmu.tecnico.ubibikeserver.domain;

import java.util.Map;
import java.util.TreeMap;

import cmu.tecnico.ubibikeserver.domain.exceptions.NotFoundException;

public class Manager {
	private static Manager instance;

	public Map<String, User> users;
	public Map<String, Station> stations;

	public User getUser(String username) throws NotFoundException {
		if (!users.containsKey(username)) {
			throw new NotFoundException(username);
		}

		return users.get(username);
	}

	public Station getStations(String station) throws NotFoundException {
		if (!stations.containsKey(station)) {
			throw new NotFoundException(station);
		}

		return stations.get(station);
	}

	private Manager() {
		this.users = new TreeMap<String, User>();
		this.stations = new TreeMap<String, Station>();

		users.put("toni", new User("toni", "pinto", "António Pinto"));
		users.put("pedro", new User("pedro", "arvela", "Pedro Arvela"));
		users.put("joao", new User("joao", "moura", "João Moura"));
		users.get("toni").points = 42;
		users.get("pedro").points = 666;
		users.get("joao").points = 321;
	}

	public static Manager getInstance() {
		if (instance == null) {
			instance = new Manager();
		}

		return instance;
	}
}
