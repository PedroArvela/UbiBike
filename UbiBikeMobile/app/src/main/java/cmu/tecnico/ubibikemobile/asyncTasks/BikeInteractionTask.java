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
import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;

/**
 * Created by Toninho on 13/05/2016.
 */
public class BikeInteractionTask extends AsyncTask<String, Boolean, Boolean> {
    App app;
    Handler handler;
    Resources resources;
    String username;

    int responseCode = 0;

    public BikeInteractionTask(App app, Handler handler, Resources resources, String username) {
        this.app = app;
        this.handler = handler;
        this.resources = resources;
        this.username = username;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        URLHelper url = new URLHelper(resources);
        Boolean result = true;
        //Action either "PICKUP" or "DROPOFF"
        //station ... some kind of id depending on what the server provices to the app to identify the stations
        String query = "bikeInteraction?user=" + username + "&action=" + params[0] + "&station="+ params[1];
        try {
            Pair<Integer, List<String>> response = url.fetchUrl(query);

            responseCode = response.first;
            if(response.first != 200) {
                result = false;
            }

        } catch (MalformedURLException e) {
            Log.e("TrajectoryTask", "URL was malformed:\t" + resources.getString(R.string.base) +
                    "bikeInteraction?user=" + username + "&action=" + params[0] + "&station="+ params[1]);
        } finally {
            return result;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        int type = (result) ? App.MESSAGE_CONFIRM : App.MESSAGE_DENY;
        Message msg = Message.obtain(null, type);

        handler.dispatchMessage(msg);
    }
}
