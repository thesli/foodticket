package net.foodticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import net.foodticket.services.socket_services;

public class main_activity extends Activity
{
    Button startSocketBtn;
    Intent servicesIntent;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        l("onCreated");
        setContentView(R.layout.main_layout);
        startSocketBtn = (Button)findViewById(R.id.startSocketBtn);
        setClickListeners();
    }

    private void setClickListeners() {
        startSocketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicesIntent = new Intent(getApplicationContext(),socket_services.class);
                startService(servicesIntent);
                l("SocketServices started");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(getApplicationContext(),"the activity is resumed",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"the activity is destroyed",Toast.LENGTH_LONG).show();
    }

    /*DEBUG Function*/
    String DEBUGTAG = "main_activityDEBUGTAG";

    public void l(String s) {
        Log.d(DEBUGTAG, s);
    }

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
}
