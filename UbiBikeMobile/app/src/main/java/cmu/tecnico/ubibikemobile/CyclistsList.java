package cmu.tecnico.ubibikemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CyclistsList extends AppCompatActivity {
    ArrayList<String> cyclistsNames;
    ArrayAdapter adapter;
    ListView listView;
    static String CYCLER_NAME = "cyclerName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclists_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.cyclists);
        cyclistsNames = GetNearbyCyclistsList();
        adapter = new ArrayAdapter<String>(this, R.layout.single_list_item, R.id.list_item, cyclistsNames);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(CyclistsList.this, CyclistInteractionMenu.class);
                String selected = ((TextView) view.findViewById(R.id.list_item)).getText().toString();
                intent.putExtra(CYCLER_NAME, selected);
                startActivity(intent);
            }
        });
        
        listView.setAdapter(adapter);
    }

    private ArrayList<String> GetNearbyCyclistsList() {
        ArrayList<String> stationNames = new ArrayList<String>();
        stationNames.add("Johny");
        stationNames.add("Peter");
        stationNames.add("Tony");
        return stationNames;
    }


}
