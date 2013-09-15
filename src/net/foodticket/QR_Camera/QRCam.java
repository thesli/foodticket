package net.foodticket.QR_Camera;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.ImageScanner;


/**
 * Created by Administrator on 9/15/13.
 */
public class QRCam {
    private Camera mCam;
    private ImageScanner scanner;
    private TextView scanText;
    private boolean previewing;
    private boolean barcodeScanned;
    public QRCam(Camera mCam, ImageScanner scanner, TextView scanText) {
        this.mCam = mCam;
        this.scanner = scanner;
        this.scanText = scanText;
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY,3);
        scanner.setConfig(0,Config.Y_DENSITY,3);
        mCam = getCameraInstance();
    }
    public Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters para = camera.getParameters();
            Camera.Size size = para.getPreviewSize();
            Image barcode = new Image(size.width,size.height,"Y800");
            barcode.setData(data);
            int result = scanner.scanImage(barcode);

            if (result!=0){
                previewing = false;
                mCam.setPreviewCallback(null);
                mCam.stopPreview();
                SymbolSet symbolSet = scanner.getResults();

                for (Symbol symbol : symbolSet){
                    scanText.setText("barcode result: " + symbol.getData());
                    barcodeScanned = false;
                }
            }
        }
    };

    public void releaseCamera(){
        if(mCam != null){
            previewing = false;
            mCam.setPreviewCallback(null);
            mCam.release();
            mCam = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public Camera getCameraInstance() {
        Camera cam= null;
        try{
            int foundId = -1;
            int numCams = Camera.getNumberOfCameras();
            for(int camId = 0;camId<numCams;camId++){
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(camId,info);
                if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    cam = Camera.open(camId);
                    return cam;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
