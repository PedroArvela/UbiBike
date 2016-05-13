package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.asyncTasks.StationListTask;
import cmu.tecnico.ubibikemobile.models.Station;

public class StationsList extends AppCompatActivity {
    ArrayList<String> stationNames;
    ArrayAdapter adapter;
    ListView listView;
    static String STATION_NAME = "stationName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.stations);
        stationNames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stationNames);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(StationsList.this, ReserveBikeActivity.class);
                String selected = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                intent.putExtra(STATION_NAME, selected);
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == App.MESSAGE_STATIONS && msg.arg1 == 200) {
                    for (Station station : (List<Station>) msg.obj) {
                        adapter.add(station.getName());
                    }
                }
            }
        };

        new StationListTask((App) getApplication(), handler, getResources()).execute();
    }

    private ArrayList<String> GetStationList() {
        ArrayList<String> stationNames = new ArrayList<String>();
        stationNames.add("Cascais");
        stationNames.add("Estoril");
        stationNames.add("Oeiras");
        return stationNames;
    }

}
