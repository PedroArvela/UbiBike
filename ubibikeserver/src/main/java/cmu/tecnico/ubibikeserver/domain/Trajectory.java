package cmu.tecnico.ubibikeserver.domain;

import java.util.ArrayList;
import java.util.List;

public class Trajectory {
	public String date;
	public List<String> coordinates;
	
	public Trajectory(String date) {
		this.date = date;
		this.coordinates = new ArrayList<String>();
	}
}
