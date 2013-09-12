package net.foodticket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import net.foodticket.services.socket_services;

public class main_activity extends Activity {
    Button startSocketBtn;
    Button sendMsg;
    Intent servicesIntent;
    Bitmap mBitmap;
    ImageView mImageView;
    Messenger msger;
    Handler msgerHandler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        l("onCreated");
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.main_layout);
        init();
        setClickListeners();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {
        startSocketBtn = (Button) findViewById(R.id.startSocketBtn);
        sendMsg = (Button) findViewById(R.id.sendMsgBtn);
        mImageView = (ImageView) findViewById(R.id.qrImgView);
        msgerHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                l(String.valueOf(message.what));
                Toast.makeText(getApplicationContext(),message.what,Toast.LENGTH_LONG).show();
                return true;
            }
        });
        msger = new Messenger(msgerHandler);
        generateQR("Hello");
    }

    private socket_services s;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            s = ((socket_services.myBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void setClickListeners() {
        startSocketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicesIntent = new Intent(getApplicationContext(), socket_services.class);
                servicesIntent.putExtra("msger",msger);
                bindService(servicesIntent, mConnection, Context.BIND_AUTO_CREATE);
                l("SocketServices started");
            }
        });
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l("sendMsg pressed");
                s.sendMsg();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(getApplicationContext(), socket_services.class), mConnection, Context.BIND_AUTO_CREATE);
//        Toast.makeText(getApplicationContext(),"the activity is resumed",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "the activity is destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    public void generateQR(String data){
        Writer writer = new QRCodeWriter();
        String finaldata = Uri.encode(data, "ISO-8559-1");
        try {
            int width = 150;
            BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE, width, width);
            mBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    mBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if(mBitmap != null){
            mImageView.setImageBitmap(mBitmap);
        }
    }

    /*DEBUG Function*/
    String DEBUGTAG = "main_activityDEBUGTAG";

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
}
