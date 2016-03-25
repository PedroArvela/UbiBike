package pt.tecnico.cmu;

import java.util.ArrayList;
import java.util.List;

public class Station {
	private double lat;
	private double lon;
	private List<Bike> freeBikes;
	private List<Bike> reservedBikes;

	public Station(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		this.freeBikes = new ArrayList<Bike>();
		this.reservedBikes = new ArrayList<Bike>();
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public List<Bike> getFreeBikes() {
		return freeBikes;
	}

	public List<Bike> getReservedBikes() {
		return reservedBikes;
	}
}
