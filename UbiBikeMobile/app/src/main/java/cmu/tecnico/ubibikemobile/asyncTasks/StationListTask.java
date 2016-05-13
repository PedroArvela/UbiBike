package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.Station;

public class StationListTask extends AsyncTask<Boolean, Boolean, List<Station>> {
    App app;
    Handler handler;
    Resources resources;

    int responseCode = 0;

    public StationListTask(App app, Handler handler, Resources resources) {
        this.app = app;
        this.handler = handler;
        this.resources = resources;
    }

    @Override
    protected List<Station> doInBackground(Boolean... params) {
        List<Station> stations = new ArrayList<Station>();

        try {
            URLHelper url = new URLHelper(resources);
            Pair<Integer, List<String>> response = url.fetchUrl("station");

            responseCode = response.first;

            if (response.first == 200) {
                String line;
                for(int i = 0; i < response.second.size(); i++) {
                    line = response.second.get(i);
                    String[] args = line.split(";");
                    String stationName = args[0];

                    String[] latLng = args[1].split(",");
                    double lat = Double.parseDouble(latLng[0]);
                    double lng = Double.parseDouble(latLng[1]);
                    LatLng coordinates =  new LatLng(lat,lng);

                    int freeBikes = Integer.parseInt(args[2]);
                    int reservedBikes = Integer.parseInt(args[3]);

                    stations.add(new Station(stationName, freeBikes, coordinates, reservedBikes));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            return stations;
        }
    }

    @Override
    protected void onPostExecute(List<Station> result) {
        Message msg = Message.obtain(null, App.MESSAGE_STATIONS, result);
        msg.arg1 = responseCode;

        handler.dispatchMessage(msg);
    }
}
