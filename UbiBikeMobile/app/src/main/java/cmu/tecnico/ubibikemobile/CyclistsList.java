package cmu.tecnico.ubibikemobile;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cmu.tecnico.wifiDirect.WifiHandler;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import cmu.tecnico.R;

public class CyclistsList extends AppCompatActivity {
    ArrayList<String> cyclistsNames;
    ArrayAdapter adapter;
    ListView listView;
    public static String CYCLER_NAME = "cyclerName";
    private WifiHandler wifiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclists_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final MyApplication app = (MyApplication) getApplicationContext();
        wifiHandler = app.wifiHandler;
        app.wifiHandler.currActivity = this;
        wifiHandler.requestPeers();
        Log.d("TEST","TEST");

        cyclistsNames = new ArrayList<String>();
        cyclistsNames = wifiHandler.nearbyAvailable;
        adapter = new ArrayAdapter<String>(this,R.layout.single_list_item, R.id.list_item, cyclistsNames);

        listView = (ListView) findViewById(R.id.cyclists);

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
        //adapter.notify();
    }

}
