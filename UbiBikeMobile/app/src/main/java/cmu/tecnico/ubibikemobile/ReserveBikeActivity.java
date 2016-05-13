package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cmu.tecnico.ubibikemobile.asyncTasks.CancelBikeReservationTask;
import cmu.tecnico.ubibikemobile.asyncTasks.ReserveBikeTask;
import cmu.tecnico.ubibikemobile.asyncTasks.StationTask;
import cmu.tecnico.ubibikemobile.models.Station;

public class ReserveBikeActivity extends AppCompatActivity {

    String stationName;
    Station station;
    TextView textViewAvailableBikes;
    TextView textViewNoBikesAvailable;
    TextView textViewBikeReserved;
    Button btnResrveBike;
    Button btnCancelReservation;
    boolean bikeReserved;

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
        textViewBikeReserved = (TextView) findViewById(R.id.lbl_bike_already_reserved);
        btnResrveBike = (Button) findViewById(R.id.btn_reserveBike);
        btnCancelReservation = (Button) findViewById(R.id.btn_cancelReservation);

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

        if(bikeReserved)
        {
            textViewAvailableBikes.setVisibility(View.VISIBLE);
            textViewNoBikesAvailable.setVisibility(View.INVISIBLE);
            textViewBikeReserved.setVisibility(View.VISIBLE);
            btnResrveBike.setEnabled(false);
            btnCancelReservation.setEnabled(true);
        }
        else {
            textViewBikeReserved.setVisibility(View.INVISIBLE);
            btnResrveBike.setEnabled(true);
            btnCancelReservation.setEnabled(false);

            if (freeBikes == 0) {
                textViewNoBikesAvailable.setVisibility(View.VISIBLE);
                textViewAvailableBikes.setVisibility(View.INVISIBLE);
                btnResrveBike.setEnabled(false);
            } else {
                textViewNoBikesAvailable.setVisibility(View.INVISIBLE);
                textViewAvailableBikes.setVisibility(View.VISIBLE);
                btnResrveBike.setEnabled(true);
            }
        }

        textViewAvailableBikes.setText("Number of Available Bikes: " + freeBikes);
    }

    public void btn_reserveBike_onClick(View v) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int code = msg.arg1;
                boolean result = (boolean)msg.obj;
                bikeReserved = result;

                if (code == 200 && result) {
                    Toast.makeText(getBaseContext(), "Bike Reserved!", Toast.LENGTH_SHORT);
                    getFreeBikes();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to Reserve Bike", Toast.LENGTH_SHORT);
                }
            }
        };

        new ReserveBikeTask((App) getApplication(), handler, getResources()).execute(station);
    }

    public void btn_cancelBikeReservation_onClick(View v) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int code = msg.arg1;
                boolean result = (boolean)msg.obj;
                bikeReserved = !result;

                if (code == 200 && result) {
                    Toast.makeText(getBaseContext(), "Bike Reservation Canceled!", Toast.LENGTH_SHORT);
                    getFreeBikes();
                } else {
                    Toast.makeText(getBaseContext(), "Failed to Cancel Bike Reservation", Toast.LENGTH_SHORT);
                }
            }
        };

        new CancelBikeReservationTask((App) getApplication(), handler, getResources()).execute(station);
    }
}
