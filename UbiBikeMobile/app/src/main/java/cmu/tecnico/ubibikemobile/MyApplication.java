package cmu.tecnico.ubibikemobile;

import android.app.Application;

import cmu.tecnico.wifiDirect.WifiHandler;

/**
 * Created by Toninho on 10/05/2016.
 */
public class MyApplication extends Application{
    WifiHandler wifiHandler;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

}
