package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cmu.tecnico.R;

public class BookBycicle extends AppCompatActivity {

    String stationName;
    int availableBikes;
    TextView textViewAvailableBikes;
    TextView textViewNoBikesAvailable;
    Button btnBookBike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_bycicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent prevIntent = getIntent();
        stationName = prevIntent.getStringExtra(StationsList.STATION_NAME);

        ((TextView) findViewById(R.id.textView_stationName)).append(" " + stationName);

        textViewAvailableBikes = (TextView) findViewById(R.id.textView_availableBikes);
        textViewNoBikesAvailable = (TextView) findViewById(R.id.textView_noBikeAvailable);
        btnBookBike = (Button) findViewById(R.id.btn_bookBike);

        GetAvailableBikes(stationName);

    }

    private void GetAvailableBikes(String stationName){
        //Comunicate with server
        availableBikes = 4;

        if(availableBikes == 0)
        {
            textViewNoBikesAvailable.setVisibility(View.VISIBLE);
            btnBookBike.setEnabled(false);
        }
        else {
            textViewNoBikesAvailable.setVisibility(View.INVISIBLE);
            btnBookBike.setEnabled(true);
        }

        textViewAvailableBikes.setText("Number of Available Bikes: " + availableBikes);
    }
}
