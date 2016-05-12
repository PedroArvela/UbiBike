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

		for (User user : users.values()) {
			Trajectory trajectory = new Trajectory("2016-05-10T172100Z");

			trajectory.coordinates.add("38.737524,-9.136616");
			trajectory.coordinates.add("38.735951,-9.136702");
			trajectory.coordinates.add("38.735286,-9.138622");
			trajectory.coordinates.add("38.735309,-9.140392");
			trajectory.coordinates.add("38.736673,-9.140306");
			trajectory.coordinates.add("38.737837,-9.140918");
			trajectory.coordinates.add("38.738029,-9.139963");
			trajectory.coordinates.add("38.738339,-9.138804");
			trajectory.coordinates.add("38.739382,-9.137373");
			trajectory.coordinates.add("38.737892,-9.136697");

			user.trajectories.put(trajectory.date, trajectory);
		}

		stations.put("Cascais", new Station("Cascais", "38.7225743,-9.4675251"));
		stations.put("Técnico", new Station("Técnico", "38.7368234,-9.140899"));
		stations.put("Terreiro do Paço", new Station("Terreiro do Paço", "38.707899,-9.1387368"));
	}

	public static Manager getInstance() {
		if (instance == null) {
			instance = new Manager();
		}

		return instance;
	}
}
