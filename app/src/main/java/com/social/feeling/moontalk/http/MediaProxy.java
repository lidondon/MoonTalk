package com.social.feeling.moontalk.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.InputStream;

/**
 * Created by vincentchan on 2016/12/1.
 */

public class MediaProxy implements IMediaProxy {
    private static MediaProxy mInstance = null;

    private Context mActivity;
    // Set your Image URL into a string
    private String URL1 = "http://iamfeeling.ddns.net/test/Koala.jpg";
    private String URL2 = "http://iamfeeling.ddns.net/test/Tulips.jpg";

    private ImageView image;
    private VideoView video;
    private Button button;
    private ProgressDialog mProgressDialog;

    public static MediaProxy getProxy(Context app) {
        if (mInstance == null)
            mInstance = new MediaProxy(app);
        return mInstance;
    }

    public MediaProxy(Context app) {
        mActivity = app;
    }

    public void loadImage(ImageView view, String url, LruCache<String, Bitmap> bitmapLruCache) {
        new DownloadImage(mActivity, view, url, bitmapLruCache).execute(url);
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        Context mActivity;
        ImageView mImage;
        String mUrl;
        LruCache<String, Bitmap> bitmapLruCache;

        public DownloadImage(Context app, ImageView view, String url, LruCache<String, Bitmap> blc) {
            mActivity = app;
            mImage = view;
            mUrl = url;
            bitmapLruCache = blc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Create a progressdialog
            //mProgressDialog = new ProgressDialog(mActivity);
            // Set progressdialog title
            //mProgressDialog.setTitle("Download Image");
            // Set progressdialog message
            //mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                Log.e(getClass().toString(), e.toString());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Set the bitmap into ImageView
                mImage.setImageBitmap(result);
                // Close progressdialog
                //mProgressDialog.dismiss();
                if (bitmapLruCache != null) {
                    bitmapLruCache.put(mUrl, result);
                }
            }
        }
    }
}
