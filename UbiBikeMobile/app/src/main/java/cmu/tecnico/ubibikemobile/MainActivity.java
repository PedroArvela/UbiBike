package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

import cmu.tecnico.ubibikemobile.asyncTasks.TrajectoriesTask;
import cmu.tecnico.ubibikemobile.models.Trajectory;
import cmu.tecnico.ubibikemobile.models.User;
import cmu.tecnico.wifiDirect.WifiHandler;

public class MainActivity extends AppCompatActivity {

    Intent myIntent;
    Button button;
    Button button2;
    Button btnUserInfo;
    ListView listView;
    ArrayList<Trajectory> lastTrajectories;
    ArrayAdapter adapter;
    static String TRAJECTORY_STARTING_POINT = "TRAJECTORY_STARTING_POINT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        App app = (App) getApplicationContext();
        if(app.getWifiHandler()==null)
            app.setWifiHandler(new WifiHandler(getApplicationContext()));
        app.getWifiHandler().currActivity = this;
        app.getWifiHandler().wifiOn();

        User user = ((App) getApplication()).getUser();

        TextView usernameLbl = (TextView) findViewById(R.id.lbl_username);
        TextView pointsLbl = (TextView) findViewById(R.id.userPoints);

        usernameLbl.setText(user.displayName);
        pointsLbl.setText(Integer.toString(user.points));

        listView = (ListView) findViewById(R.id.listview_trajectories);
        lastTrajectories = new ArrayList<Trajectory>();

        adapter = new ArrayAdapter<Trajectory>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lastTrajectories);

        new TrajectoriesTask(getResources(), adapter, (ProgressBar) findViewById(R.id.loading_trajectories), user.username).execute(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_LastTrajectories_onItemClick(parent, view, position, id);
            }
        });

        listView.setAdapter(adapter);
    }

    public void btn_BookStations_onClick(View v) {
        myIntent = new Intent(MainActivity.this, StationsList.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void btn_CyclistsNearby_onClick(View v) {
        myIntent = new Intent(MainActivity.this, CyclistsList.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void list_LastTrajectories_onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, TrajectoryActivity.class);
        String startingPoint = ((Trajectory) parent.getAdapter().getItem(position)).getStart();
        intent.putExtra(TRAJECTORY_STARTING_POINT, startingPoint);
        startActivity(intent);
    }
}
