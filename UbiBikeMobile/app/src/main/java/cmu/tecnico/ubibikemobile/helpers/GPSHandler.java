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

/**
 * Created by Toninho on 12/05/2016.
 */
public class GPSHandler implements LocationListener {
//PeerListListener


    Activity currActivity;
    Context appContext;
    ArrayList<LatLng> trajectory;

    public GPSHandler(Activity currActivity, Context appContext){
        this.currActivity = currActivity;
        this.appContext = appContext;
        trajectory = new ArrayList<LatLng>();

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
            jsonArray.put(trajectory.get(i).toString());
        }
        return jsonArray.toString();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        trajectory.add(ll);
        Log.d("GPS", "Location Changed " + location.toString());
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
