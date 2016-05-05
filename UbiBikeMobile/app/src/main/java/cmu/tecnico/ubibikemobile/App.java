package cmu.tecnico.ubibikemobile;

import android.app.Application;

public class App extends Application {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
