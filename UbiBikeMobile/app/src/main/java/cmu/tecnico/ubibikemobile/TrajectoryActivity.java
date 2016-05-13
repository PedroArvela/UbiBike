package cmu.tecnico.ubibikemobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import cmu.tecnico.ubibikemobile.asyncTasks.TrajectoryTask;
import cmu.tecnico.ubibikemobile.helpers.GPSHandler;
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

        String trajectoryString = getIntent().getStringExtra(MainActivity.TRAJECTORY_OBJECT);
        String username = getIntent().getStringExtra(MainActivity.USERNAME_EXTRA);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    int response = (int) msg.arg1;

                    if (response == 200) {
                        trajectory = (Trajectory) msg.obj;
                        drawOnMap();
                    } else {
                        Toast.makeText(getBaseContext(), "Failed to get trajectory", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", "Error handling TrajectoryTask\n" + e.getMessage());
                    Toast.makeText(getBaseContext(), "Failed to get trajectory", Toast.LENGTH_LONG).show();
                }
            }
        };
        new TrajectoryTask((App) getApplication(), handler, getResources(), username).execute(trajectoryString);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }
        mMap.setMyLocationEnabled(true);
    }

    private void drawOnMap() {
        if (mMap != null) {
            mMap.addPolyline(new PolylineOptions()
                    .addAll(trajectory.getTrajectory())
                    .width(4)
                    .color(Color.BLUE).geodesic(true));

            if (trajectory.getTrajectory().size() > 0) {
                mMap.addMarker(new MarkerOptions()
                        .position(trajectory.getTrajectory().get(0))
                        .title(startingPoint));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trajectory.getTrajectory().get(0), 19));
            }

            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 750, null);
        }
    }
}
