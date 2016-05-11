package cmu.tecnico.ubibikemobile.models;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Trajectory implements Serializable {
    private String date;
    private String start;
    private ArrayList<LatLng> trajectory;

    public Trajectory(String date, ArrayList<LatLng> trajectory, Geocoder geocoder) {
        this.date = date;
        this.trajectory = trajectory;

        try {
            if(trajectory.size() > 1) {
                Address address = geocoder.getFromLocation(trajectory.get(0).latitude, trajectory.get(0).longitude, 1).get(0);
                this.start = "";

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    if (this.start.isEmpty())
                        this.start = address.getAddressLine(i);
                    else
                        this.start += ", " + address.getAddressLine(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Trajectory","Error transforming GPS coordinates into address");
        }
    }

    public String getDate() { return date; }
    public String getStart() { return start; }
    public ArrayList<LatLng> getTrajectory() { return trajectory; }

    public String toString() {
        return date + " - " + start;
    }
}
