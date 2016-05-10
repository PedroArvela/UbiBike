package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CyclistInteractionMenu extends AppCompatActivity {

    Intent myIntent;
    Button button;
    Button button2;
    String cyclistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclist_interaction_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent prevIntent = getIntent();
        cyclistName = prevIntent.getStringExtra(CyclistsList.CYCLER_NAME);
        Toast toast = Toast.makeText(getApplicationContext(), cyclistName, Toast.LENGTH_SHORT);
        toast.show();

        button = (Button) findViewById(R.id.btn_BookStations);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(CyclistInteractionMenu.this, SendPoints.class);
                myIntent.putExtra(CyclistsList.CYCLER_NAME, cyclistName);
                CyclistInteractionMenu.this.startActivity(myIntent);
            }
        });

        button2 = (Button) findViewById(R.id.btn_CyclistsNearby);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(CyclistInteractionMenu.this, SendMessage.class);
                myIntent.putExtra(CyclistsList.CYCLER_NAME, cyclistName);
                CyclistInteractionMenu.this.startActivity(myIntent);
            }
        });
    }

}
