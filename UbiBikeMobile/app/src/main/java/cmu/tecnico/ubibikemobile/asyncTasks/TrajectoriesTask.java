package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.models.Trajectory;

public class TrajectoriesTask extends AsyncTask<Boolean, Boolean, List<Trajectory>> {
    Resources resources;
    ArrayAdapter<Trajectory> adapter;
    ProgressBar progress;
    String username;

    public TrajectoriesTask(Resources res, ArrayAdapter<Trajectory> adapter, ProgressBar progressInfo, String username) {
        this.resources = res;
        this.adapter = adapter;
        this.progress = progressInfo;
        this.username = username;
    }

    @Override
    protected List<Trajectory> doInBackground(Boolean... params) {
        List<Trajectory> trajectories = new ArrayList<Trajectory>();

        try {
            URL url = new URL(
                    resources.getString(R.string.protocol), resources.getString(R.string.domain),
                    resources.getInteger(R.integer.port), resources.getString(R.string.base) +
                    "user/" + username + "/trajectories");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            int response = conn.getResponseCode();

            if (response == 200) {
                String line;

                while ((line = rd.readLine()) != null) {
                    URL turl = new URL(resources.getString(R.string.protocol), resources.getString(R.string.domain),
                             resources.getInteger(R.integer.port), resources.getString(R.string.base) +
                    "user/" + username + "/trajectories/" + line);

                    HttpURLConnection tconn = (HttpURLConnection) turl.openConnection();
                    BufferedReader trd = new BufferedReader(new InputStreamReader(
                            new BufferedInputStream(tconn.getInputStream()), StandardCharsets.UTF_8));

                    if (tconn.getResponseCode() == 200) {

                        Trajectory t = new Trajectory(line, trd.readLine());
                        trajectories.add(t);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            return trajectories;
        }
    }

    @Override
    protected void onPostExecute(List<Trajectory> trajectories) {
        progress.setVisibility(View.GONE);
        Log.d("TrajectoriesTaskPost", "Size: " + trajectories.size());
        for (Trajectory trajectory : trajectories) {
            Log.d("TrajectoriesTaskPost", trajectory.toString());
            adapter.add(trajectory);
        }
    }
}