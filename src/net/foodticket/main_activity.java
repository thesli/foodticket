package net.foodticket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.foodticket.QR_Generator.QR_generator;
import net.foodticket.QR_Camera.CameraPreview;
import net.foodticket.QR_Camera.QRCam;
import net.foodticket.services.Socket_Services;
import net.sourceforge.zbar.ImageScanner;

public class main_activity extends Activity {
    Camera mCam;
    ImageScanner scanner;
    TextView scanText;
    Button startSocketBtn;
    Button sendMsg;
    Intent servicesIntent;
    Bitmap mBitmap;
    ImageView mImageView;
    Messenger msger;
    Handler msgerHandler;
    private FrameLayout QR_frame;
    private boolean previewing;
    CameraPreview cameraPreview;
    private CameraPreview preview;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        l("onCreated");
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.main_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        setClickListeners();
    }

    static {
        System.loadLibrary("iconv");
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void init() {
        setViews();
        final QR_generator qr_generator = new QR_generator(mBitmap, mImageView);
        QRCam qrCam = new QRCam(mCam, scanner, scanText);
        cameraPreview = new CameraPreview(this,mCam,qrCam.previewCb,null);
        msgerHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                l(String.valueOf(message.obj.toString()));
                qr_generator.generateQR(message.obj.toString());
                return true;
            }
        });
        msger = new Messenger(msgerHandler);
        qr_generator.generateQR("hello fucking World");
        preview = new CameraPreview(getApplicationContext(),mCam, qrCam.previewCb,null);
    }

    private void setViews() {
        startSocketBtn = (Button) findViewById(R.id.startSocketBtn);
        sendMsg = (Button) findViewById(R.id.sendMsgBtn);
        mImageView = (ImageView) findViewById(R.id.qrImgView);
        QR_frame = (FrameLayout) findViewById(R.id.qrCamFrame);
    }

    private Socket_Services s;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            s = ((Socket_Services.myBinder) iBinder).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void setClickListeners() {
        startSocketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicesIntent = new Intent(getApplicationContext(), Socket_Services.class);
                servicesIntent.putExtra("msger", msger);
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
//        bindService(new Intent(getApplicationContext(), Socket_Services.class), mConnection, Context.BIND_AUTO_CREATE);
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
