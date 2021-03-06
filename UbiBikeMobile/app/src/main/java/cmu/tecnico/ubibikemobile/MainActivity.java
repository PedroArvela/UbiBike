package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cmu.tecnico.ubibikemobile.asyncTasks.TrajectoryListTask;
import cmu.tecnico.ubibikemobile.asyncTasks.TrajectoryTask;
import cmu.tecnico.ubibikemobile.helpers.GPSHandler;
import cmu.tecnico.ubibikemobile.models.Trajectory;
import cmu.tecnico.ubibikemobile.models.User;
import cmu.tecnico.ubibikemobile.helpers.ConcreteWifiHandler;
import cmu.tecnico.wifiDirect.SimWifiP2pBroadcastReceiver;

public class MainActivity extends AppCompatActivity {

    Intent myIntent;
    ListView listView;
    public TextView pointsLbl;
    User user;
    ArrayList<String> lastTrajectories;
    ArrayAdapter<String> adapter;
    static String TRAJECTORY_OBJECT = "TRAJECTORY_OBJECT";
    static String USERNAME_EXTRA = "USERNAME_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = ((App) getApplication()).getUser();

        TextView usernameLbl = (TextView) findViewById(R.id.lbl_username);
        pointsLbl = (TextView) findViewById(R.id.userPoints);

        App app = (App) getApplicationContext();

        if(app.getRcv()==null)
            app.setRcv(new SimWifiP2pBroadcastReceiver());

        if(app.getWifiHandler()==null)
            app.setWifiHandler(new ConcreteWifiHandler(getApplicationContext(), app.getRcv()));

        if(app.getGpsHandler()== null) {
            if ( ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                        123 ); //random number
                app.setGpsHandler(new GPSHandler(getApplicationContext(),app.getRcv()));
            }else
                app.setGpsHandler(new GPSHandler(getApplicationContext(),app.getRcv()));
        }
        app.getWifiHandler().currActivity = this;
        app.getWifiHandler().wifiOn();

        usernameLbl.setText(user.displayName);
        pointsLbl.setText(Integer.toString(user.points));

        listView = (ListView) findViewById(R.id.listview_trajectories);

        lastTrajectories = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lastTrajectories);

        new TrajectoryListTask(getResources(), adapter, (ProgressBar) findViewById(R.id.loading_trajectories), user.username).execute(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_LastTrajectories_onItemClick(parent, view, position, id);
            }
        });

        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        pointsLbl.setText(Integer.toString(user.points));
    }

    public void btn_ReserveStations_onClick(View v) {
        myIntent = new Intent(MainActivity.this, StationsList.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void btn_CyclistsNearby_onClick(View v) {
        myIntent = new Intent(MainActivity.this, CyclistsList.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void list_LastTrajectories_onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String trajectory = ((String) parent.getAdapter().getItem(position));

        Intent intent = new Intent(MainActivity.this, TrajectoryActivity.class);
        intent.putExtra(TRAJECTORY_OBJECT, trajectory);
        intent.putExtra(USERNAME_EXTRA, user.username);
        startActivity(intent);
    }
}
