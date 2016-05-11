package cmu.tecnico.ubibikemobile.asyncTasks;

import android.os.AsyncTask;
import android.util.Pair;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.RegisterActivity;

public class LoginTask extends AsyncTask<Boolean, Boolean, Pair<String, Boolean>> {
    RegisterActivity activity;
    String username;
    String password;

    public LoginTask(RegisterActivity activity, String username, String password) {
        this.activity = activity;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Pair<String, Boolean> doInBackground(Boolean... params) {
        Pair<String, Boolean> result = new Pair<String, Boolean>(username, false);

        try {
            URL url = new URL(activity.getResources().getString(R.string.protocol),
                    activity.getResources().getString(R.string.domain),
                    activity.getResources().getInteger(R.integer.port),
                    activity.getResources().getString(R.string.base) + "user/" + username + "/password");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            int response = conn.getResponseCode();

            if (response == 200) {
                result = new Pair<String, Boolean>(username, password.equals(rd.readLine()));
            }
        } catch (MalformedURLException e) {
            // nothing to do
        } finally {
            return result;
        }
    }

    @Override
    protected void onPostExecute(Pair<String, Boolean> result) {
        if (result.second) {
            activity.login(result.first);
        } else {
            ((EditText) activity.findViewById(R.id.txt_username)).setError("Username or password wrong");
        }
    }
}
