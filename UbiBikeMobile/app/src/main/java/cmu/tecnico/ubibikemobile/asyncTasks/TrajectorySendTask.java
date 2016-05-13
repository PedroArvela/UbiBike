package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.Trajectory;

public class TrajectorySendTask extends AsyncTask<String, Boolean, Boolean> {
    App app;
    Resources resources;
    String username;

    int responseCode = 0;

    public TrajectorySendTask(App app, Resources resources, String username) {
        this.app = app;
        this.resources = resources;
        this.username = username;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        URLHelper url = new URLHelper(resources);

        Boolean result = true;

        //trajectories are a JSON'ed array of "lat,long" positions
        String query = "newTrajectory?user=" + username + "&trajectories=" + params[0];
        try {
            Pair<Integer, List<String>> response = url.fetchUrl(query);

            responseCode = response.first;
            if(response.first != 200) {
                result = false;
            }

        } catch (MalformedURLException e) {
            Log.e("TrajectoryTask", "URL was malformed:\t" + resources.getString(R.string.base) +
                    "newTrajectory?user=" + username + "&trajectories=" + params[0]);
        } finally {
            return result;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        int type = (result) ? App.MESSAGE_CONFIRM : App.MESSAGE_DENY;
        Message msg = Message.obtain(null, type);

    }
}
