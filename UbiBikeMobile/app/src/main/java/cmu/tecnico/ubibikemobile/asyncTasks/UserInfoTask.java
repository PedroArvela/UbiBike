package cmu.tecnico.ubibikemobile.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import cmu.tecnico.ubibikemobile.App;
import cmu.tecnico.ubibikemobile.models.User;

public class UserInfoTask extends AsyncTask<String, Boolean, User> {
    App app;
    Handler handler;
    Resources res;

    public UserInfoTask(App app, Handler handler, Resources res) {
        this.app = app;
        this.handler = handler;
        this.res = res;
    }

    @Override
    protected User doInBackground(String... usernames) {
        String username = usernames[0];

        User result = new User(username, "José Miguel Maria Fernandes");
        result.points = 264;

        return result;
    }

    @Override
    protected void onPostExecute(User result) {
        app.setUser(result);

        Message msg = Message.obtain(null, App.MESSAGE_USERNAME, result);
        handler.dispatchMessage(msg);
    }
}
