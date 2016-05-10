package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.StationsList;
import cmu.tecnico.ubibikemobile.UserInfo;

public class TrajectoryActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String startingPoint;
    Geocoder geoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent prevIntent = getIntent();
        startingPoint = prevIntent.getStringExtra(UserInfo.TRAJECTORY_STARTING_POINT);
        geoCoder = new Geocoder(this, Locale.getDefault());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try
        {
            Address address = geoCoder.getFromLocationName(startingPoint, 1).get(0);
            if (address != null)
            {
                Double lat = (double) (address.getLatitude());
                Double lon = (double) (address.getLongitude());

                final LatLng user = new LatLng(lat, lon);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(user)
                        .title(startingPoint));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 19));

                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 750, null);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
