package cmu.tecnico.ubibikemobile;

import android.app.Application;

import cmu.tecnico.ubibikemobile.models.User;

public class App extends Application {
    public final static int MESSAGE_USERNAME = 0;

    private String username;
    private User user;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
