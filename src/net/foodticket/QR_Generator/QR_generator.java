package net.foodticket.QR_Generator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by Administrator on 9/15/13.
 */
public class QR_generator {
    private Bitmap mBitmap;
    private ImageView mImageView;
    public QR_generator(Bitmap mBitmap, ImageView mImageView) {
        this.mBitmap = mBitmap;
        this.mImageView = mImageView;
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
}
