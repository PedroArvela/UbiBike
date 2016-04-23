package pt.tecnico.cmu.domain;

import java.util.Random;

public class Bike {
	private int id;
	private double lat;
	private double lon;

	public Bike() {
		id = new Random().nextInt();
	}

	public int getId() {
		return id;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
}
