package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.Station;

public class StationTask extends AsyncTask<String, Boolean, Station> {
    App app;
    Handler handler;
    Resources resources;

    int responseCode = 0;

    public StationTask(App app, Handler handler, Resources resources) {
        this.app = app;
        this.handler = handler;
        this.resources = resources;
    }

    @Override
    protected Station doInBackground(String... params) {
        Station station = null;

        if(params.length == 0)
            return station;

        String stationName = params[0];
        try {
            URLHelper url = new URLHelper(resources);

            //TODO Fetch URL de estações por stationName
            Pair<Integer, List<String>> response = url.fetchUrl("station/" + stationName);

            responseCode = response.first;

            if (response.first == 200) {
                //TODO Assume-se que o servidor retorna duas linhas. 1a com as coordenadas da estação num par lat+long (eg. "38.123,-9.123")
                // E a 2a linha contém um inteiro com o número de bicicletas livres na estação
                if(response.second.size() != 2)
                    return station;

                String[] latLng = response.second.get(0).split(",");
                double lat = Double.parseDouble(latLng[0]);
                double lng = Double.parseDouble(latLng[1]);
                LatLng coordinates =  new LatLng(lat,lng);

                int freeBikes = Integer.parseInt(response.second.get(1));

                station = new Station(stationName, freeBikes, coordinates);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            return station;
        }
    }

    @Override
    protected void onPostExecute(Station result) {
        Message msg = Message.obtain(null, App.MESSAGE_STATIONS, result);
        msg.arg1 = responseCode;

        handler.dispatchMessage(msg);
    }
}
