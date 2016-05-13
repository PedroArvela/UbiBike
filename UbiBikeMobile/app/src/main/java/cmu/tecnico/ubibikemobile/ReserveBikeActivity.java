package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cmu.tecnico.ubibikemobile.asyncTasks.StationTask;
import cmu.tecnico.ubibikemobile.models.Station;

public class ReserveBikeActivity extends AppCompatActivity {

    String stationName;
    Station station;
    TextView textViewAvailableBikes;
    TextView textViewNoBikesAvailable;
    Button btnBookBike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_bicycle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent prevIntent = getIntent();
        stationName = prevIntent.getStringExtra(StationsList.STATION_NAME);

        ((TextView) findViewById(R.id.lbl_stationName)).append(" " + stationName);

        textViewAvailableBikes = (TextView) findViewById(R.id.lbl_freeBikes);
        textViewNoBikesAvailable = (TextView) findViewById(R.id.lbl_noBikeAvailable);
        btnBookBike = (Button) findViewById(R.id.btn_reserveBike);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 != 200) {
                    Toast.makeText(getBaseContext(), "Failed to fetch user info", Toast.LENGTH_SHORT);
                } else {
                    station = ((Station) msg.obj);
                    getFreeBikes();
                }
            }
        };
        new StationTask((App) getApplication(), handler, getResources()).execute(stationName);

    }

    private void getFreeBikes() {
        int freeBikes = station.getFreeBikes();

        if (freeBikes == 0) {
            textViewNoBikesAvailable.setVisibility(View.VISIBLE);
            btnBookBike.setEnabled(false);
        } else {
            textViewNoBikesAvailable.setVisibility(View.INVISIBLE);
            btnBookBike.setEnabled(true);
        }

        textViewAvailableBikes.setText("Number of Available Bikes: " + freeBikes);
    }
}
