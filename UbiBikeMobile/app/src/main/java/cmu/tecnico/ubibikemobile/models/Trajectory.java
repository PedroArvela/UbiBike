package cmu.tecnico.ubibikemobile.models;

import java.util.Date;

public class Trajectory {
    private String date;
    private String start;

    public Trajectory(String date, String start) {
        this.date = date;
        this.start = start;
    }

    public String getDate() { return date; }
    public String getStart() { return start; }

    public String toString() {
        return date + " - " + start;
    }
}
