package com.empire.vmd.client.android_lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by lidondon on 2015/10/15.
 */
public class QrCodeUtil {
    public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Context context;

    public QrCodeUtil(Context ctx) {
        context = ctx;
    }

    public ImageView getQrCode(String content) {
        ImageView imgView = null;
        MultiFormatWriter writer = new MultiFormatWriter();
        //QRCode內容的編碼
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        //QRCode的寬度
        int QRCodeWidth = (int) ConvertUtil.convertDp2Pixel(context, 200);
        //QRCode的高度
        int QRCodeHeight = (int) ConvertUtil.convertDp2Pixel(context, 200);

        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            //建立QRCode的資料矩陣
            BitMatrix result= writer.encode(content, BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight, hints);
            //建立點陣圖
            Bitmap bitmap = Bitmap.createBitmap(QRCodeWidth, QRCodeHeight, Bitmap.Config.ARGB_8888);

            //將QRCode資料矩陣繪製到點陣圖上
            for (int y = 0; y < QRCodeHeight; y++) {
                for (int x = 0; x < QRCodeWidth; x++) {
                    bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            //建立新ImageView
            imgView = new ImageView(context);
            //設定為QRCode影像
            imgView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return imgView;
    }

}
