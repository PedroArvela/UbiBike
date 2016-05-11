package cmu.tecnico.ubibikemobile.models;

import java.util.Date;
import java.util.Map;

public class User {
    public String username;
    public String displayName;
    public int points;
    public Map<String, Trajectory> recentTrajectories;

    public User(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }
}
