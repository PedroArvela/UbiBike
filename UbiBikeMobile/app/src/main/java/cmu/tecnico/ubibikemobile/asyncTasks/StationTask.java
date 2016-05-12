package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;

public class StationTask extends AsyncTask<String, Boolean, List<String>> {
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
    protected List<String> doInBackground(String... params) {
        List<String> trajectories = new ArrayList<String>();

        try {
            URLHelper url = new URLHelper(resources);
            Pair<Integer, List<String>> response = url.fetchUrl("station");

            responseCode = response.first;

            if (response.first == 200) {
                String line;

                trajectories = response.second;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            return trajectories;
        }
    }

    @Override
    protected void onPostExecute(List<String> result) {
        Message msg = Message.obtain(null, App.MESSAGE_STATIONS, result);
        msg.arg1 = responseCode;

        handler.dispatchMessage(msg);
    }
}
