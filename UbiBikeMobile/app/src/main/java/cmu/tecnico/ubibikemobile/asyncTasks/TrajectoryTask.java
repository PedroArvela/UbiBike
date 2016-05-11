package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.Trajectory;

public class TrajectoryTask extends AsyncTask<String, Boolean, List<Trajectory>> {
    App app;
    Handler handler;
    Resources resources;
    String username;

    int responseCode = 0;

    public TrajectoryTask(App app, Handler handler, Resources resources, String username) {
        this.app = app;
        this.handler = handler;
        this.resources = resources;
        this.username = username;
    }

    @Override
    protected List<Trajectory> doInBackground(String... params) {
        URLHelper url = new URLHelper(resources);

        int count = params.length;
        int i = 0;
        List<Trajectory> trajectories = new ArrayList<Trajectory>();

        try {
            for (i = 0; i < count; i++) {
                Pair<Integer, List<String>> response = url.fetchUrl("user/" + username + "/trajectories/" + params[i]);

                responseCode = response.first;

                if (response.first == 200) {
                    Trajectory t = new Trajectory(params[i], response.second.get(0));
                    trajectories.add(t);
                }
            }

        } catch (MalformedURLException e) {
            Log.e("TrajectoryTask", "URL was malformed:\t" + resources.getString(R.string.base) +
                    "user/" + username + "/trajectories/" + params[i]);
        } finally {
            return trajectories;
        }
    }

    @Override
    protected void onPostExecute(List<Trajectory> result) {
        Message msg = Message.obtain(null, App.MESSAGE_TRAJECTORY, result);
        msg.arg1 = responseCode;

        handler.dispatchMessage(msg);
    }
}
