package cmu.tecnico.ubibikemobile;

import android.app.Application;

import cmu.tecnico.ubibikemobile.models.User;
import cmu.tecnico.ubibikemobile.helpers.ConcreteWifiHandler;

public class App extends Application {
    public final static int MESSAGE_USER = 0;
    public final static int MESSAGE_TRAJECTORY = 1;
    public final static int MESSAGE_CONFIRM = 2;
    public final static int MESSAGE_DENY = 3;

    private String username;
    private User user;
    private ConcreteWifiHandler wifiHandler;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ConcreteWifiHandler getWifiHandler() {
        return wifiHandler;
    }
    public void setWifiHandler(ConcreteWifiHandler wifiHandler) {
        this.wifiHandler = wifiHandler;
    }
}
