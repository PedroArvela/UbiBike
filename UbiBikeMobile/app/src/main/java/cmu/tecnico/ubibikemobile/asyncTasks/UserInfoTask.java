package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.helpers.URLHelper;
import cmu.tecnico.ubibikemobile.models.User;

public class UserInfoTask extends AsyncTask<String, Boolean, User> {
    App app;
    Handler handler;
    Resources resources;

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
            result.displayName = url.fetchUrl("user/" + username + "/name").second.get(0);
            result.points = Integer.parseInt(url.fetchUrl("user/" + username + "/points").second.get(0));
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
        handler.dispatchMessage(msg);
    }
}
