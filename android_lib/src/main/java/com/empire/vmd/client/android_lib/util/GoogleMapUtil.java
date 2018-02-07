package com.empire.vmd.client.android_lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.empire.vmd.client.android_lib.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


/**
 * Created by lidondon on 2015/10/28.
 */
public class GoogleMapUtil {
    private Context context;

    public GoogleMapUtil(Context ctx) {
        context = ctx;
    }

    public GoogleMap getGoogleMap(Activity activity, MapView mapView, Bundle bundle) {
        GoogleMap googleMap;

        mapView.onCreate(bundle);
        mapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage().toString());
        }
        googleMap = mapView.getMap();

        return googleMap;
    }

    public LatLng getLatLngByAddress(String strAddress) {
        LatLng result = null;

        try {
            List<Address> addressList = new Geocoder(context, Locale.TAIWAN).getFromLocationName(strAddress, 1);

            if (addressList != null) {
                result = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage().toString());
        }

        return result;
    }

    public void addMarker(GoogleMap googleMap, LatLng latLng, String title, String snippet, int landmarkResource) {
        BitmapDescriptor icon = getResizeBitmapDescriptor(landmarkResource);
        MarkerOptions markerOptions = new MarkerOptions();

        try {
            //markerOptions.position(latLng).title(title).snippet(snippet).icon(icon);
            markerOptions.position(latLng).title(title).snippet(snippet);
            googleMap.addMarker(markerOptions);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage().toString());
        }
    }

    public BitmapDescriptor getResizeBitmapDescriptor(int drawableResource) {
        BitmapDescriptor result = null;
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(drawableResource).getCurrent();
        Bitmap bitmap = bd.getBitmap();
        int widthHeight = (int) ConvertUtil.convertDp2Pixel(context,  25);

        bitmap = Bitmap.createScaledBitmap(bitmap, widthHeight, widthHeight, false);
        result = BitmapDescriptorFactory.fromBitmap(bitmap);

        return result;
    }

    // 移動地圖到參數指定的位置
    public void moveMap(GoogleMap googleMap, LatLng latLng) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder().target(latLng).zoom(14).build();

        // 使用動畫的效果移動地圖
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public LatLng getCurrentLatLng(LocationManager locationManager) {
        LatLng result = null;
        Location location = null;

        if (locationManager != null) {
            String bestProvider = locationManager.getBestProvider(new Criteria(), true);

            try {
                if (bestProvider == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else {
                    location = locationManager.getLastKnownLocation(bestProvider);
                }

                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage().toString());
            }
            result = new LatLng(location.getLatitude(), location.getLongitude());
        }

        return result;
    }
}
