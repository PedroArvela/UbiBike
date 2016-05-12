package cmu.tecnico.ubibikeserver.domain;

public class Station {
	public String name;
	public String coordinates;
	public int freeBikes;
	public int reservedBikes;

	public Station(String name, String coordinates) {
		this.name = name;
		this.coordinates = coordinates;
		this.freeBikes = 10;
		this.reservedBikes = 0;
	}
}
