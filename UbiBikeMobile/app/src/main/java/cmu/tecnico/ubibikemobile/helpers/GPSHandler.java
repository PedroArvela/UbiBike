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

    final static String BEACON_NAME = "beacon";
    Activity currActivity;
    Context appContext;
    ArrayList<LatLng> trajectory;
    ArrayList<LatLng> bikeStations;

    Boolean currHasBike;
    Boolean prevHasBike;

    public GPSHandler(Activity currActivity, Context appContext){
        this.currActivity = currActivity;
        this.appContext = appContext;
        trajectory = new ArrayList<LatLng>();
        currHasBike = false;
        prevHasBike = false;

        if ( ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
/*
            ActivityCompat.requestPermissions(currActivity, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
                    */
            ActivityCompat.requestPermissions(currActivity, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    2 );
        }

        LocationManager lManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
    }

    public String getTrajectoryJson(){
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < trajectory.size(); i++) {
            jsonArray.put(trajectory.get(i).latitude+","+trajectory.get(i).longitude);
        }
        return jsonArray.toString();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude()); //nova posicao
        Log.d("GPS", "Location Changed " + location.toString());

        Boolean isNearStation = false;
        for(int i = 0; i < bikeStations.size(); i++){ //detectar se estiver perto de uma das estações
            if(bikeStations.get(i) == ll){ //change
                isNearStation = true;
            }
        }

        if(isNearStation) { //se estiver perto de uma das estações ver se tem beacons por perto e actualizar variaveis
            ((App)appContext).getWifiHandler().requestPeers(this); //requestPeers pede um PeerListListener e o listener dessa classe recebe o resultado
        }else{
            //apanhou bicicleta
            if(currHasBike && !prevHasBike){
                //avisar servidor
                prevHasBike = true;
            }
            //largou bicicleta
            if(!currHasBike && prevHasBike) {
                //avisar servidor
            }

            //está a andar de bicicleta
            if(currHasBike && prevHasBike){
                trajectory.add(ll); //guardar trajectoria
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
