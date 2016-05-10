package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cmu.tecnico.wifiDirect.CyclistsList;
import cmu.tecnico.R;

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

        Intent prevIntent = getIntent();
        cyclistName = prevIntent.getStringExtra(CyclistsList.CYCLER_NAME);
        Toast toast=Toast.makeText(getApplicationContext(), cyclistName, Toast.LENGTH_SHORT);
        toast.show();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(CyclistInteractionMenu.this, SendPoints.class);
                myIntent.putExtra(CyclistsList.CYCLER_NAME, cyclistName);
                CyclistInteractionMenu.this.startActivity(myIntent);
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(CyclistInteractionMenu.this, SendMessage.class);
                myIntent.putExtra(CyclistsList.CYCLER_NAME, cyclistName);
                CyclistInteractionMenu.this.startActivity(myIntent);
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
