package cmu.tecnico.ubibikemobile.asyncTasks;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.RegisterActivity;

public class LoginTask extends AsyncTask<Pair<RegisterActivity, URL>, Boolean, List<String>> {
    RegisterActivity activity;

    @Override
    protected List<String> doInBackground(Pair<RegisterActivity, URL>... params) {
        List<String> result = new ArrayList<String>();
        activity = params[0].first;

        try {
            HttpURLConnection conn = (HttpURLConnection) params[0].second.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            return null;
        } finally {
            return result;
        }
    }

    @Override
    protected void onPostExecute(List<String> result) {
        if (result.size() >= 1) {
            activity.login(result.get(0));
        }
    }
}
