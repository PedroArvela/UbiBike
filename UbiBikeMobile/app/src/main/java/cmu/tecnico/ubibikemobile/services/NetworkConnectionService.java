package cmu.tecnico.ubibikemobile.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class NetworkConnectionService extends IntentService {
    public final static String PARAM_USERNAME = "username";
    public final static int MESSAGE_USERNAME = 0;

    public NetworkConnectionService() {
        super("NetworkConnectionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String username = intent.getStringExtra(PARAM_USERNAME);

        final Messenger messenger = (Messenger) intent.getParcelableExtra("messenger");
        final Message message = Message.obtain(null, MESSAGE_USERNAME, username);

        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
