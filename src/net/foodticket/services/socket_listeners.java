package net.foodticket.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import net.foodticket.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 9/15/13.
 */
public class Socket_Listeners {
    Messenger msger;
    Message msg;
    SocketIOClient c;
    Handler h;
    public Socket_Listeners(final SocketIOClient c, final Handler h, final Messenger msger,Context ctx) {
        this.c = c;
        this.h = h;
        this.ctx = ctx;
        this.msger = msger;
        init();

        for(int i=0;i<event_name.size();i++){
            c.addListener(event_name.get(i),event_callback.get(i));
        }
/*
        c.addListener("hello",new EventCallback() {
            @Override
            public void onEvent(String s, JSONArray jsonArray, Acknowledge acknowledge) {

            }
        });
        c.addListener("helloWorld", new EventCallback() {
            @Override
            public void onEvent(String s, JSONArray jsonArray, Acknowledge acknowledge) {
//                        Toast.makeText(getApplicationContext(),"helloWorld",Toast.LENGTH_LONG).show();
            }
        });

        c.addListener("fiveSecond", new EventCallback() {
            @Override
            public void onEvent(String s, JSONArray jsonArray, Acknowledge acknowledge) {
                        toastText("five Second Passed,Go working");
            }
        });

        final JSONArray ja = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fuck",12);
            ja.put(jsonObject);
            l(ja.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        c.addListener("hello", new EventCallback() {
            @Override
            public void onEvent(String s, JSONArray jsonArray, Acknowledge acknowledge) {
                l("hello triggered");
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            msg = Message.obtain(null, R.string.abc,"abcde");
                            msger.send(msg);
                            c.emit("return", ja);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });

                */
/*Listeners of the Event*//*


*/

    }


    public ArrayList<EventCallback> event_callback;
    public ArrayList<String> event_name;

    public void init(){
        event_name.add("hello_server");
        event_callback.add(new EventCallback() {
            @Override
            public void onEvent(String s, JSONArray jsonArray, Acknowledge acknowledge) {
                l("string : " + s);
                l("jsonArray : " + jsonArray.toString());
            }
        });
        event_name.add("client_success");
        event_callback.add(new EventCallback() {
            @Override
            public void onEvent(String s, JSONArray jsonArray, Acknowledge acknowledge) {
                l("string : " + s);
                l("jsonArray : " + jsonArray.toString());
            }
        });

    }

    /*DEBUG Function*/
    String DEBUGTAG = "Socket_ListenersDEBUGTAG";
    private Context ctx;

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
    private void toastText(String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
    }
}

