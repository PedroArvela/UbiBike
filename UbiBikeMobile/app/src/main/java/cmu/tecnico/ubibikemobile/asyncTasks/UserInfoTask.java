package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import java.io.IOException;
import java.util.List;
import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.User;

public class UserInfoTask extends AsyncTask<String, Boolean, User> {
    App app;
    Handler handler;
    Resources resources;

    int responseCode = 0;

    public UserInfoTask(App app, Handler handler, Resources resources) {
        this.app = app;
        this.handler = handler;
        this.resources = resources;

    }

    @Override
    protected User doInBackground(String... usernames) {
        String username = usernames[0];
        User result = new User(username, "");

        URLHelper url = new URLHelper(resources);
        try {
            Pair<Integer, List<String>> nameResult = url.fetchUrl("user/" + username + "/name");
            Pair<Integer, List<String>> pointsResult = url.fetchUrl("user/" + username + "/points");

            if (nameResult.first == 200 && pointsResult.first == 200) {
                responseCode = 200;

                result.displayName = nameResult.second.get(0);
                result.points = Integer.parseInt(pointsResult.second.get(0));
            } else {
                responseCode = (nameResult.first == 200) ? nameResult.first : pointsResult.first;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    protected void onPostExecute(User result) {
        app.setUser(result);
        Message msg = Message.obtain(null, App.MESSAGE_USER, result);
        msg.arg1 = responseCode;
        handler.dispatchMessage(msg);
    }
}
