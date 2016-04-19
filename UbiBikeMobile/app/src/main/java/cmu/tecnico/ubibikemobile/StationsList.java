package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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

        listView = (ListView) findViewById(R.id.stations);
        stationNames = GetStationList();
        adapter = new ArrayAdapter<String>(this,R.layout.single_list_item, R.id.list_item, stationNames);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(StationsList.this, BookBycicle.class);
                String selected = ((TextView) view.findViewById(R.id.list_item)).getText().toString();
                intent.putExtra(STATION_NAME, selected);
                startActivity(intent);
            }
        });



        listView.setAdapter(adapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    private ArrayList<String> GetStationList (){
        ArrayList<String> stationNames = new ArrayList<String>();
        stationNames.add("Cascais");
        stationNames.add("Estoril");
        stationNames.add("Oeiras");
        return stationNames;
    }

}
