package cmu.tecnico.ubibikemobile.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.asyncTasks.RegisterTask;
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
    final static String BEACON_NAME = "beacon";
    Context appContext;
    ArrayList<Location> trajectory;
    ArrayList<Location> bikeStations;

    Boolean currHasBike;
    Boolean prevHasBike;

    public GPSHandler(Context appContext){
        this.appContext = appContext;
        trajectory = new ArrayList<Location>();
        currHasBike = false;
        prevHasBike = false;

        LocationManager lManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
        }catch(SecurityException e){
            Log.e("SECURITY EXCEPTION", e.getMessage());
        }
    }

    public String getTrajectoryJson(){
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < trajectory.size(); i++) {
            jsonArray.put(trajectory.get(i).getLatitude()+","+trajectory.get(i).getLongitude());
        }
        return jsonArray.toString();
    }

    @Override
    public void onLocationChanged(Location location) {//nova posicao
        Log.d("GPS", "Location Changed " + location.toString());

        Boolean isNearStation = false;
        for(int i = 0; i < bikeStations.size(); i++){ //detectar se estiver perto de uma das estações
            float[] results = new float[1];
            if(location.distanceTo(bikeStations.get(i)) < DISTANCE_TO_LEAVE_STATION){ //change
                isNearStation = true;
            }
        }

        if(isNearStation) { //se estiver perto de uma das estações ver se tem beacons por perto e actualizar variaveis
            ((App)appContext).getWifiHandler().requestPeers(this); //requestPeers pede um PeerListListener e o listener dessa classe recebe o resultado
        }else{
            //apanhou bicicleta
            if(currHasBike && !prevHasBike){
                //avisar servidor
                Log.d("GPS","grabbed a bike");
                prevHasBike = true;
            }
            //largou bicicleta
            if(!currHasBike && prevHasBike) {
                //avisar servidor
                Log.d("GPS","grabbed a bike");
            }

            //está a andar de bicicleta
            if(currHasBike && prevHasBike){
                trajectory.add(location); //guardar trajectoria
                Log.d("GPS","biking");
            }
        }

    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) { //listener de requestPeers
        prevHasBike = currHasBike;
        currHasBike = false;
        for (SimWifiP2pDevice device : simWifiP2pDeviceList.getDeviceList()) {
            if(device.deviceName.contains(BEACON_NAME)) { //está perto de um beacon
                currHasBike = true;
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
