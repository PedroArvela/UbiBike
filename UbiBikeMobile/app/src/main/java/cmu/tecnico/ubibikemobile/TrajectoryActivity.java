package cmu.tecnico.ubibikemobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

import cmu.tecnico.ubibikemobile.models.Trajectory;

public class TrajectoryActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String startingPoint;
    Trajectory trajectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        trajectory = (Trajectory)getIntent().getSerializableExtra(MainActivity.TRAJECTORY_OBJECT);

        //TODO Apagar quando o server estiver implementado e com o modelo correcto das trajectorias
        if(trajectory.getTrajectory().size() == 0)
            trajectory = new Trajectory(trajectory.getDate(), getTrajectory(), new Geocoder(this));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addPolyline(new PolylineOptions()
                .addAll(trajectory.getTrajectory())
                .width(4)
                .color(Color.BLUE).geodesic(true));

        if (trajectory.getTrajectory().size() > 1) {
            mMap.addMarker(new MarkerOptions()
                    .position(trajectory.getTrajectory().get(0))
                    .title(startingPoint));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trajectory.getTrajectory().get(0), 19));
        }

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 750, null);
    }

    private ArrayList<LatLng> getTrajectory() {
        //TODO Apagar quando o server estiver implementado e com o modelo correcto das trajectorias
        //Valores de teste
        
        ArrayList<LatLng> trajectory = new ArrayList<LatLng>();
        trajectory.add(new LatLng(38.737524, -9.136616));
        trajectory.add(new LatLng(38.735951, -9.136702));
        trajectory.add(new LatLng(38.735286, -9.138622));
        trajectory.add(new LatLng(38.735309, -9.140392));
        trajectory.add(new LatLng(38.736673, -9.140306));
        trajectory.add(new LatLng(38.737837, -9.140918));
        trajectory.add(new LatLng(38.738029, -9.139963));
        trajectory.add(new LatLng(38.738339, -9.138804));
        trajectory.add(new LatLng(38.739382, -9.137373));
        trajectory.add(new LatLng(38.737892, -9.136697));

        return trajectory;
    }
}
