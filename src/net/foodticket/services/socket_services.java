package net.foodticket.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.http.socketio.*;

import net.foodticket.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by Administrator on 9/11/13.
 */
public class Socket_Services extends Service {
    public final IBinder mBinder = new myBinder();
    Messenger msger;
    Message msg;
    Handler h;

    @Override
    public IBinder onBind(Intent intent) {
        l("onBinded");

//        Toast.makeText(getApplicationContext(),"onBind",Toast.LENGTH_LONG).show();
        try {
            init(intent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        init();
        return START_STICKY;
    }

    public SocketIOClient clt;

    private void init(Intent intent) throws RemoteException {
        h = new Handler(getApplicationContext().getMainLooper());
        msger = intent.getParcelableExtra("msger");

//        String t = msg.obj.toString();
        SocketIOClient.connect("http://192.168.1.111:3060",new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception e, SocketIOClient client) {
                clt = client;
                if(e!=null){
                    e.printStackTrace();
                    return;
                }
                DisconnectCallback disCb = new DisconnectCallback() {
                    @Override
                    public void onDisconnect(Exception e) {
//                        toastText("disconnected");
                        l("disconnected");
                    }
                };
                StringCallback strCb = new StringCallback() {
                    @Override
                    public void onString(String s, Acknowledge acknowledge) {
//                        toastText("onString");
                        l(s.toString(),acknowledge.toString());
                    }
                };
                ReconnectCallback recCb = new ReconnectCallback() {
                    @Override
                    public void onReconnect() {
//                        toastText("reconnected");
                        l("reconnected");
                    }
                };
                JSONCallback jsonCb = new JSONCallback() {
                    @Override
                    public void onJSON(JSONObject jsonObject, Acknowledge acknowledge) {
//                        toastText("onJson");
                        l(jsonObject.toString(),acknowledge.toString());
                    }
                };
                ErrorCallback errCb = new ErrorCallback() {
                    @Override
                    public void onError(String s) {
//                        toastText("Error happened");
                        l(s.toString());
                    }
                };

                /*Initiatie the CallBacks*/
                client.setDisconnectCallback(disCb);
                client.setStringCallback(strCb);
                client.setReconnectCallback(recCb);
                client.setErrorCallback(errCb);
                client.setJSONCallback(jsonCb);
                /*Initiatie the CallBacks*/

                /*Listeners of the Event*/
                Socket_Listeners socket_listeners = new Socket_Listeners(client,h,msger,getApplicationContext());

            }
        },new Handler());
    }

    /*DEBUG Function*/
    String DEBUGTAG = "socket_servicesDEBUGTAG";

    public void l(String s) {
        Log.d(DEBUGTAG, s);
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public void l(String s, String t) {
        if (t.equals("d")) {
            Log.d(DEBUGTAG, s);
        } else if (t.equals("e")) {
            Log.d(DEBUGTAG, s);
        } else if (t.equals("v")) {
            Log.v(DEBUGTAG, s);
        } else if (t.equals("i")) {
            Log.i(DEBUGTAG, s);
        } else if (t.equals("wtf")) {
            Log.wtf(DEBUGTAG, s);
        } else if (t.equals("w")) {
            Log.w(DEBUGTAG, s);
        } else {
            l(s);
        }
    }

    public class myBinder extends Binder {
        public Socket_Services getService(){
            return Socket_Services.this;
        }
    }

    public void sendMsg() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fuck",12);
            jsonArray.put(jsonObject);
            l(jsonArray.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        clt.emit("return", jsonArray);
    }
}
