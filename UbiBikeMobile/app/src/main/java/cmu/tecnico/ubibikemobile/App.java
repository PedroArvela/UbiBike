package cmu.tecnico.ubibikemobile;

import android.app.Application;

import cmu.tecnico.ubibikemobile.helpers.GPSHandler;
import cmu.tecnico.ubibikemobile.models.User;
import cmu.tecnico.ubibikemobile.helpers.ConcreteWifiHandler;
import cmu.tecnico.wifiDirect.SimWifiP2pBroadcastReceiver;

public class App extends Application {
    public final static int MESSAGE_USER = 0;
    public final static int MESSAGE_TRAJECTORY = 1;
    public final static int MESSAGE_CONFIRM = 2;
    public final static int MESSAGE_DENY = 3;
    public final static int MESSAGE_STATIONS = 4;

    private String username;
    private User user;
    private ConcreteWifiHandler wifiHandler;
    private GPSHandler gpsHandler;
    private SimWifiP2pBroadcastReceiver rcv;

    public SimWifiP2pBroadcastReceiver getRcv() {
        return rcv;
    }

    public void setRcv(SimWifiP2pBroadcastReceiver rcv) {
        this.rcv = rcv;
    }
    public GPSHandler getGpsHandler() {
        return gpsHandler;
    }

    public void setGpsHandler(GPSHandler gpsHandler) {
        this.gpsHandler = gpsHandler;
    }

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
