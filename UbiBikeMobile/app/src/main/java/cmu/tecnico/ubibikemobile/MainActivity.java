package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.List;

import cmu.tecnico.ubibikemobile.asyncTasks.TrajectoryListTask;
import cmu.tecnico.ubibikemobile.asyncTasks.TrajectoryTask;
import cmu.tecnico.ubibikemobile.models.Trajectory;
import cmu.tecnico.ubibikemobile.models.User;
import cmu.tecnico.wifiDirect.WifiHandler;

public class MainActivity extends AppCompatActivity {

    Intent myIntent;
    Button button;
    Button button2;
    Button btnUserInfo;
    ListView listView;
    User user;
    ArrayList<String> lastTrajectories;
    ArrayAdapter<String> adapter;
    static String TRAJECTORY_OBJECT = "TRAJECTORY_OBJECT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = ((App) getApplication()).getUser();

        TextView usernameLbl = (TextView) findViewById(R.id.lbl_username);
        TextView pointsLbl = (TextView) findViewById(R.id.userPoints);

        App app = (App) getApplicationContext();
        if(app.getWifiHandler()==null)
            app.setWifiHandler(new WifiHandler(getApplicationContext()));
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

    public void btn_BookStations_onClick(View v) {
        myIntent = new Intent(MainActivity.this, StationsList.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void btn_CyclistsNearby_onClick(View v) {
        myIntent = new Intent(MainActivity.this, CyclistsList.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void list_LastTrajectories_onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String trajectory = ((String) parent.getAdapter().getItem(position));

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(MainActivity.this, TrajectoryActivity.class);

                try {
                    Trajectory trajectory = (Trajectory) msg.obj;
                    int response = (int) msg.arg1;

                    if (response == 200) {
                        intent.putExtra(TRAJECTORY_OBJECT, trajectory);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getBaseContext(), "Failed to get location", Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", "Error handling TrajectoryTask\n"+e.getMessage());
                }
            }
        };
        new TrajectoryTask((App) getApplication(), handler, getResources(), user.username).execute(trajectory);

    }
}
