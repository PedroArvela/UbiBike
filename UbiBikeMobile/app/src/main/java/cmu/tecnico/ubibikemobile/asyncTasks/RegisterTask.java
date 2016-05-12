package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.R;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;

public class RegisterTask extends AsyncTask<String, Boolean, Boolean> {
    App app;
    Handler handler;
    Resources resources;

    public RegisterTask(App app, Handler handler, Resources resources) {
        this.app = app;
        this.handler = handler;
        this.resources = resources;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = true;
        URLHelper url = new URLHelper(resources);

        if (params.length != 3) {
            return false;
        } else {
            String resource = "register?username=" + params[0] +
                    "&password=" + params[1] + "&name=" + params[2];

            try {
                Pair<Integer, List<String>> response = url.fetchUrl(resource);
                if(response.first != 200) {
                    result = false;
                }
            } catch (MalformedURLException e) {
                Log.e("TrajectoryTask", "URL was malformed:\t" + resources.getString(R.string.base) +
                        resource);
            } finally {
                return result;
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        int type = (result) ? App.MESSAGE_CONFIRM : App.MESSAGE_DENY;
        Message msg = Message.obtain(null, type);

        handler.dispatchMessage(msg);
    }
}
