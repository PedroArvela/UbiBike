package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import java.net.MalformedURLException;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.Station;

public class PickupBikeTask extends AsyncTask<Station, Boolean, Boolean> {
    App app;
    Handler handler;
    Resources resources;

    int responseCode = 0;

    public PickupBikeTask(App app, Handler handler, Resources resources) {
        this.app = app;
        this.handler = handler;
        this.resources = resources;
    }

    @Override
    protected Boolean doInBackground(Station... params) {
        boolean result = false;
        if(params.length == 0)
            return result;

        Station station = params[0];
        try {
            URLHelper url = new URLHelper(resources);

            Pair<Integer, List<String>> response = url.fetchUrl("station/" + station.getName() + "/pickup");

            responseCode = response.first;

            if (response.first == 200) {
                if(response.second.size() == 0)
                    return result;

                String line = response.second.get(0);

                if(line.equals("true")) {
                    result = true;
                    station.cancelBikeReservation();
                }
            }
        } catch (MalformedURLException e) {
            Log.e("PickupBikeTask", e.getMessage());
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Message msg = Message.obtain(null, App.MESSAGE_STATIONS, result);
        msg.arg1 = responseCode;

        handler.dispatchMessage(msg);
    }
}
