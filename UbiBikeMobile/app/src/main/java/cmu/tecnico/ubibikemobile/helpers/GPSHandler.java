package cmu.tecnico.ubibikemobile.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.asyncTasks.DropoffBikeTask;
import cmu.tecnico.ubibikemobile.asyncTasks.PickupBikeTask;
import cmu.tecnico.ubibikemobile.asyncTasks.RegisterTask;
import cmu.tecnico.ubibikemobile.asyncTasks.SendPointsTask;
import cmu.tecnico.ubibikemobile.asyncTasks.StationListTask;
import cmu.tecnico.ubibikemobile.asyncTasks.TrajectorySendTask;
import cmu.tecnico.ubibikemobile.models.Station;
import cmu.tecnico.wifiDirect.SimWifiP2pBroadcastReceiver;
import cmu.tecnico.wifiDirect.WifiHandler;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;

/**
 * Created by Toninho on 12/05/2016.
 */
public class GPSHandler implements LocationListener, SimWifiP2pManager.PeerListListener {
//PeerListListener

    final static int DISTANCE_TO_LEAVE_STATION = 10; //in meters
    final static String BEACON_NAME = "Beacon";
    Context appContext;
    ArrayList<Location> trajectory;
    ArrayList<Station> bikeStations;
    ArrayList<Location> bikeStationLocations;

    public ArrayList<Station> getBikeStations() {
        return bikeStations;
    }

    public void setBikeStations(ArrayList<Station> bikeStations) {
        this.bikeStations = bikeStations;
    }

    private Boolean hasBeaconNearby;
    private Boolean isInStation;
    private Boolean wasInStation;
    private Station station;
    private Boolean pickedBycicle;

    public GPSHandler(Context appContext, SimWifiP2pBroadcastReceiver mReceiver){
        this.appContext = appContext;
        trajectory = new ArrayList<Location>();
        bikeStations = new ArrayList<Station>();
        bikeStationLocations = new ArrayList<Location>();

        hasBeaconNearby = false;
        isInStation = false;
        wasInStation = false;
        pickedBycicle = false;

        LocationManager lManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
        }catch(SecurityException e){
            Log.e("SECURITY EXCEPTION", e.getMessage());
        }

        mReceiver.setGpshandler(this);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == App.MESSAGE_STATIONS && msg.arg1 == 200) {
                    bikeStations = (ArrayList<Station>) msg.obj;
                    for (Station station : bikeStations) {
                        Location ll = new Location("provider");
                        ll.setLatitude(station.getCoordinates().latitude);
                        ll.setLongitude(station.getCoordinates().longitude);
                        bikeStationLocations.add(ll);
                    }
                }
            }
        };

        new StationListTask((App) appContext, handler, appContext.getResources()).execute();
    }

    public String getTrajectoryJson(){
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < trajectory.size(); i++) {
            jsonArray.put(trajectory.get(i).getLatitude()+","+trajectory.get(i).getLongitude());
        }
        return jsonArray.toString();
    }

    public void updateNearbyBeaconList(){
        Log.d("GPS", "Update nearby beacon status");
        ((App)appContext).getWifiHandler().requestPeers(this);
    }

    @Override
    public void onLocationChanged(Location location) {//nova posicao
        Log.d("GPS", "Location Changed " + location.toString());

        wasInStation = isInStation;
        isInStation = false;
        for(int i = 0; i < bikeStations.size(); i++){ //detectar se estiver perto de uma das estações
            float[] results = new float[1];
            if(location.distanceTo(bikeStationLocations.get(i)) < DISTANCE_TO_LEAVE_STATION){
                isInStation = true;
                station = bikeStations.get(i);
            }
        }
        if(!isInStation) { //se nao estiver na estação
            if(wasInStation){ //mas acabou de sair da estação

                //apanhou bicicleta
                if(!pickedBycicle && hasBeaconNearby){
                    //avisar servidor
                    Toast.makeText(appContext, "grabbed a bike",
                            Toast.LENGTH_SHORT).show();
                    Log.d("GPS","grabbed a bike");
                    new PickupBikeTask((App) appContext, appContext.getResources()).execute(station);
                    pickedBycicle = true;
                    trajectory = new ArrayList<Location>();
                }

                //largou bicicleta
                if(pickedBycicle && !hasBeaconNearby) {
                    //avisar servidor
                    Log.d("GPS","grabbed a bike");
                    Toast.makeText(appContext, "dropped a bike",
                            Toast.LENGTH_SHORT).show();
                    new DropoffBikeTask((App) appContext, appContext.getResources()).execute(station);
                    new TrajectorySendTask((App) appContext, appContext.getResources(), ((App) appContext).getUser().username).execute(getTrajectoryJson());
                    pickedBycicle = false;
                }
            }

            //está a andar de bicicleta
            if(pickedBycicle && hasBeaconNearby){
                trajectory.add(location); //guardar trajectoria
                Toast.makeText(appContext, "biking",
                        Toast.LENGTH_SHORT).show();
            }

            if(pickedBycicle && !hasBeaconNearby){
                Log.d("GPS","walking not on bike");
            }

        }
        Log.d("GPS", "Its near a station: "+isInStation);
        Log.d("GPS", "Was near a station: "+wasInStation);
        Log.d("GPS", "Picked bike: "+pickedBycicle);
        Log.d("GPS", "Has beacon Nearby: "+hasBeaconNearby);

    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) { //listener de requestPeers
        hasBeaconNearby = false;
        for (SimWifiP2pDevice device : simWifiP2pDeviceList.getDeviceList()) {
            if(device.deviceName.contains(BEACON_NAME)) { //está perto de um beacon
                Log.d("GPS","Beacon close by: "+device.deviceName);
                hasBeaconNearby = true;
            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
