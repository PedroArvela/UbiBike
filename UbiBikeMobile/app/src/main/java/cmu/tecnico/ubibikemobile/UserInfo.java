package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserInfo extends AppCompatActivity {

    int points;
    String username;
    TextView textViewPoints;
    ListView listView;
    ArrayList<String> lastTrajectories;
    ArrayAdapter adapter;
    static String TRAJECTORY_STARTING_POINT = "TRAJECTORY_STARTING_POINT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent prevIntent = getIntent();
        username = prevIntent.getStringExtra("username");

        textViewPoints = (TextView) findViewById(R.id.userPoints);
        GetPoints();

        listView = (ListView) findViewById(R.id.listview_trajectories);
        lastTrajectories = getLastTrajectories();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lastTrajectories);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//                Intent intent = new Intent(UserInfo.this, BLABLABLA);
//                String selected = ((TextView) view.findViewById(R.id.list_item)).getText().toString();
//                String startingPoint = selected.split("-")[1];
//                intent.putExtra(TRAJECTORY_STARTING_POINT, startingPoint);
//                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);
    }

    private void GetPoints() {
        // Buscar info ao servidor para o user logged in

        points = 100;
        textViewPoints.setText("Points: " + points);
    }

    private ArrayList<String> getLastTrajectories() {
        //Buscar info da data + local de Partida das ultimas trajectorias ao server

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("22/03/2016 13:37 - Avenida Rovisco Pais Nº1");
        arrayList.add("23/03/2016 13:37 - Avenida Rovisco Pais Nº1");
        arrayList.add("24/03/2016 13:37 - Avenida Rovisco Pais Nº1");
        arrayList.add("25/03/2016 13:37 - Avenida Rovisco Pais Nº1");

        return arrayList;
    }
}
