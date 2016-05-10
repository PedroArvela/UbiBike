package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Intent myIntent;
    Button button;
    Button button2;
    Button btnUserInfo;
    ListView listView;
    ArrayList<String> lastTrajectories;
    ArrayAdapter adapter;
    static String TRAJECTORY_STARTING_POINT = "TRAJECTORY_STARTING_POINT";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = ((App) this.getApplication()).getUsername();

        TextView usernameLbl = (TextView) findViewById(R.id.lbl_username);
        usernameLbl.setText(username);

        button = (Button) findViewById(R.id.btn_BookStations);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(MainActivity.this, StationsList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        button2 = (Button) findViewById(R.id.btn_CyclistsNearby);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(MainActivity.this, CyclistsList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        listView = (ListView) findViewById(R.id.listview_trajectories);
        lastTrajectories = getLastTrajectories();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lastTrajectories);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TrajectoryActivity.class);
                String selected = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                String startingPoint = selected.split("-")[1];
                intent.putExtra(TRAJECTORY_STARTING_POINT, startingPoint);
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> getLastTrajectories() {
        //Buscar info da data + local de Partida das ultimas trajectorias ao server

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("22/03/2016 13:37 - Avenida Rovisco Pais 1");
        arrayList.add("23/03/2016 13:37 - Avenida Rovisco Pais 1");
        arrayList.add("24/03/2016 13:37 - Avenida Rovisco Pais 1");
        arrayList.add("25/03/2016 13:37 - Avenida Rovisco Pais 1");

        return arrayList;
    }
}
