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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.Trajectory;

public class TrajectoryListTask extends AsyncTask<Boolean, Boolean, List<String>> {
    Resources resources;
    ArrayAdapter<String> adapter;
    ProgressBar progress;
    String username;

    int responseCode = 0;

    public TrajectoryListTask(Resources res, ArrayAdapter<String> adapter, ProgressBar progressInfo, String username) {
        this.resources = res;
        this.adapter = adapter;
        this.progress = progressInfo;
        this.username = username;
    }

    @Override
    protected List<String> doInBackground(Boolean... params) {
        List<String> trajectories = new ArrayList<String>();

        try {
            URLHelper url = new URLHelper(resources);
            Pair<Integer, List<String>> response = url.fetchUrl("user/" + username + "/trajectories");

            responseCode = response.first;

            if (response.first == 200) {
                String line;

                trajectories = response.second;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return trajectories;
        }
    }

    @Override
    protected void onPostExecute(List<String> trajectories) {
        progress.setVisibility(View.GONE);

        for (String trajectory : trajectories) {
            adapter.add(trajectory);
        }
    }
}
