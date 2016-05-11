package cmu.tecnico.wifiDirect;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cmu.tecnico.ubibikemobile.*;


import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;

/**
 * Created by Toninho on 09/05/2016.
 */


public class WifiHandler implements SimWifiP2pManager.PeerListListener,SimWifiP2pManager.GroupInfoListener {

    public Activity currActivity;
    private Context appContext;
    private MessageHistory msgHistory;
    private boolean mBound = false;
    public SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private SimWifiP2pBroadcastReceiver mReceiver;
    public ArrayList<String> nearbyAvailable;
    public HashMap<String, String> connected;


    public WifiHandler(Context appContext){
        this.appContext = appContext;
        nearbyAvailable = new ArrayList<String>();
        connected = new HashMap<String, String>();
        msgHistory = new MessageHistory(appContext);

        SimWifiP2pSocketManager.Init(appContext);

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new SimWifiP2pBroadcastReceiver(this);
        getContext().registerReceiver(mReceiver, filter);
    }

    public void requestPeers(){
        if (mBound) {
            mManager.requestPeers(mChannel, this);
        } else {
            Log.d("WIFI_MANAGER", "Service not bound.");
        }
    }

    public void requestGroupInfo(){
        if (mBound) {
            mManager.requestGroupInfo(mChannel, WifiHandler.this);
        } else {
            Log.d("WIFI_MANAGER", "Service not bound.");
        }

    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList, SimWifiP2pInfo simWifiP2pInfo) {
        StringBuilder peersStr = new StringBuilder();
        for (String deviceName : simWifiP2pInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = simWifiP2pDeviceList.getByName(deviceName);
            String devstr = "" + deviceName + " (" +
                    ((device == null)?"??":device.getVirtIp()) + ")\n";
            peersStr.append(devstr);
            connected.put(deviceName, device.getVirtIp());
        }
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) {
        nearbyAvailable.clear();
        for(SimWifiP2pDevice device : simWifiP2pDeviceList.getDeviceList()){
            nearbyAvailable.add(device.getVirtIp());
        }
    }

    public class OutgoingCommTask extends AsyncTask<String, Void, String> {
        SimWifiP2pSocket mCliSocket;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.d("OUTGOING", "CLISOCKET");
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getContext().getString(R.string.port)));
            } catch (UnknownHostException e) {
                return "Unknown Host:" + e.getMessage();
            } catch (IOException e) {
                return "IO error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //error
            } else {

            }
        }
    }

    public void sendMessage(String user, String message){
        if (mBound) {
            new SendCommTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user, message);
        }
    }

    public class SendCommTask extends AsyncTask<String, String, Void> {
        SimWifiP2pSocket mCliSocket;

        @Override
        protected Void doInBackground(String... msg) {
            try {
                Log.d("TEST", "Sending message " + msg[0] + msg[1] );
                mCliSocket = new SimWifiP2pSocket(msg[0], 10001);
                if(mCliSocket == null){
                    Log.d("TAG", "no such user");
                }else {
                    mCliSocket.getOutputStream().write((msg[1] + "\n").getBytes());
                    BufferedReader sockIn = new BufferedReader(
                            new InputStreamReader(mCliSocket.getInputStream()));
                    sockIn.readLine();
                    mCliSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCliSocket = null;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //clean ui
        }
    }

    public class IncommingCommTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(mSrvSocket == null)
                    mSrvSocket = new SimWifiP2pSocketServer(10001);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SimWifiP2pSocket sock = mSrvSocket.accept();
                    try {
                        BufferedReader sockIn = new BufferedReader(
                                new InputStreamReader(sock.getInputStream()));
                        String st = sockIn.readLine();
                        publishProgress(st);
                        sock.getOutputStream().write(("\n").getBytes());
                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    } finally {
                        sock.close();
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            try {
                JSONObject jsonObject = new JSONObject(values[0]);
                if(jsonObject.getString("type").equals(Message.TYPE_POINTS)){
                    addPoints(Integer.parseInt(jsonObject.getString("content")));
                }else if (jsonObject.getString("type").equals(Message.TYPE_MSG)){
                    messageReceived("",jsonObject.getString("content"));
                }
            }catch(JSONException e){
                Log.d("Error jsonMessage: ", e.getMessage());
            }
        }
    }

    private void addPoints(int pointsToadd){
        ((App)appContext).getUser().points += pointsToadd;
    }

    private void messageReceived(String sender, String message){
        //se estiver na actividade das mensagens, adicionar ao ecra
        if(currActivity instanceof SendMessage) {
            String currhistory = ((SendMessage)currActivity).history.getText().toString() + '\n' + "Me: " + message ;
            ((SendMessage)currActivity).history.setText(currhistory);
        }
        saveFile(sender, message);
    }

    public void saveFile(String sender, String message){
        msgHistory.writeFile(sender, message);
    }

    public String readFile(String sender){
        return msgHistory.readFile(sender);
    }

    public void sendPoints(String user,int points){
        Message pointsMsg = new Message(points);
        new SendCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, user, pointsMsg.toJSON()
                );
    }

    public void wifiEnabled(boolean state){
        String statePrint;
        if(state)
            statePrint = "WiFi Direct enabled";
        else
            statePrint = "WiFi Direct disabled";

        Toast.makeText(currActivity, statePrint,
                Toast.LENGTH_SHORT).show();
    }

    public void peersChanged(){
        Toast.makeText(currActivity, "Peer list changed",
                Toast.LENGTH_SHORT).show();
    }

    public void membChanged(){
        Toast.makeText(currActivity, "Network membership changed",
                Toast.LENGTH_SHORT).show();
    }

    public void ownerChanged(){
        Toast.makeText(currActivity, "Group ownership changed",
                Toast.LENGTH_SHORT).show();
    }

    private Context getContext(){
        return appContext;
    }

    public void wifiOn(){
        if(mBound)
            return;
        Intent intent = new Intent(getContext(), SimWifiP2pService.class);
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

        new IncommingCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void wifiOff(){
       if(mBound){
           getContext().unbindService(mConnection);
           mBound = false;
       }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(currActivity.getApplication(), getContext().getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };
}
