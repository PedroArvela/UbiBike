package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.Trajectory;

public class TrajectoryTask extends AsyncTask<String, Boolean, Trajectory> {
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
    protected Trajectory doInBackground(String... params) {
        URLHelper url = new URLHelper(resources);

        int i = 0;
        Trajectory trajectory = null;

        try {
            Pair<Integer, List<String>> response = url.fetchUrl("user/" + username + "/trajectories/" + params[i]);

            responseCode = response.first;
            if (response.first == 200) {
                //Assumindo que a resposta é: 1a linha para data e [2..N] linha é um par de latitude,longitude (e.g. 38.737524,-9.136616)
                ArrayList<LatLng> coordinates = new ArrayList<>();
                String date = response.second.get(0);
                for(int j = 1; j < response.second.size(); j++){
                    String[] latLng = response.second.get(j).split(",");
                    double lat = Double.parseDouble(latLng[0]);
                    double lng = Double.parseDouble(latLng[1]);

                    coordinates.add(new LatLng(lat, lng));
                }
                trajectory = new Trajectory(date, coordinates, new Geocoder(app));
            }

        } catch (MalformedURLException e) {
            Log.e("TrajectoryTask", "URL was malformed:\t" + resources.getString(R.string.base) +
                    "user/" + username + "/trajectories/" + params[i]);
        } finally {
            return trajectory;
        }
    }

    @Override
    protected void onPostExecute(Trajectory result) {
        Message msg = Message.obtain(null, App.MESSAGE_TRAJECTORY, result);
        msg.arg1 = responseCode;

        handler.dispatchMessage(msg);
    }
}
