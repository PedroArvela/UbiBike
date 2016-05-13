package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import cmu.tecnico.ubibikemobile.helpers.ConcreteWifiHandler;

public class SendPoints extends AppCompatActivity {

    Intent myIntent;
    Button button;
    NumberPicker pointsField;
    ConcreteWifiHandler wifiHandler;
    String cyclistName;
    //static String CYCLER_NAME = "cyclerName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_points);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent prevIntent = getIntent();

        cyclistName = prevIntent.getStringExtra(CyclistsList.CYCLER_NAME);
        Toast toast=Toast.makeText(getApplicationContext(), cyclistName, Toast.LENGTH_SHORT);
        toast.show();

        final App app = (App) getApplicationContext();
        app.getWifiHandler().currActivity = this;
        this.wifiHandler = app.getWifiHandler();

        button = (Button) findViewById(R.id.btn_ReserveStations);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(SendPoints.this, MainActivity.class);
                SendPoints.this.startActivity(myIntent);
                wifiHandler.sendPoints(cyclistName, pointsField.getValue());
            }
        });
        button.setEnabled(false);

        pointsField = (NumberPicker) findViewById(R.id.points_value);
        pointsField.setWrapSelectorWheel(false);
        pointsField.setMaxValue(10);
        pointsField.setMinValue(0);

        pointsField.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0)
                    button.setEnabled(false);
                else if (oldVal == 0)
                    button.setEnabled(true);
            }
        });
    }

}
