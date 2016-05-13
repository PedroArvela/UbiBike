package cmu.tecnico.wifiDirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cmu.tecnico.ubibikemobile.helpers.GPSHandler;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {

    private WifiHandler handler;

    public void setGpshandler(GPSHandler gpshandler) {
        this.gpshandler = gpshandler;
    }

    public void setHandler(WifiHandler handler) {
        this.handler = handler;
    }

    private GPSHandler gpshandler;

    public SimWifiP2pBroadcastReceiver(WifiHandler handler) {
        super();
        this.handler = handler;
    }

    public SimWifiP2pBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // This action is triggered when the Termite service changes state:
            // - creating the service generates the WIFI_P2P_STATE_ENABLED event
            // - destroying the service generates the WIFI_P2P_STATE_DISABLED event

            int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1);
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
                handler.wifiEnabled(true);
            } else {
                handler.wifiEnabled(false);
            }

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            handler.peersChanged();
            gpshandler.updateNearbyBeaconList();

        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {

            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(
                    SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            ginfo.print();
            handler.membChanged();

        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {

            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(
                    SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            ginfo.print();
            handler.ownerChanged();
        }
    }
}
