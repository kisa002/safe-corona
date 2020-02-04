package com.haeyum.safecorona;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.naver.maps.geometry.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class GPSService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        Log.i(LOGSERVICE, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGSERVICE, "onStartCommand");

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        return START_STICKY;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOGSERVICE, "onConnected" + bundle);

        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {
            Log.i(LOGSERVICE, "lat " + l.getLatitude());
            Log.i(LOGSERVICE, "lng " + l.getLongitude());

        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOGSERVICE, "onConnectionSuspended " + i);

    }

    View view;
    WindowManager mWindowManager;

    @Override
    public void onLocationChanged(Location location) {
//        Log.i(LOGSERVICE, "lat " + location.getLatitude());
//        Log.i(LOGSERVICE, "lng " + location.getLongitude());
        LatLng mLocation = (new LatLng(location.getLatitude(), location.getLongitude()));

        double currentLat = location.getLatitude();
        double currentLng = location.getLongitude();

        int infectedCount = PreferenceManager.getInt(this, "infectedCount");
        int closeCount = PreferenceManager.getInt(this, "logWaringCloseCount");

        boolean isShow = false;

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        String currentYear = new SimpleDateFormat("yyyy").format(date);
        String currentMonth = new SimpleDateFormat("MM").format(date);
        String currentDay = new SimpleDateFormat("dd").format(date);
        String currentHour = new SimpleDateFormat("HH").format(date);
        String currentMin = new SimpleDateFormat("mm").format(date);

        //                    String lastDate = PreferenceManager.getString(this, "logWaringClose" + closeCount + "Date");
        String lastYear = PreferenceManager.getString(this, "logWaringClose" + closeCount + "Year");
        String lastMonth = PreferenceManager.getString(this, "logWaringClose" + closeCount + "Month");
        String lastDay = PreferenceManager.getString(this, "logWaringClose" + closeCount + "Day");
        String lastHour = PreferenceManager.getString(this, "logWaringClose" + closeCount + "Hour");
        String lastMin = PreferenceManager.getString(this, "logWaringClose" + closeCount + "Min");

        if(lastYear == "" || lastHour == "")
            isShow = true;
        else {
            int lastYMD = Integer.parseInt(lastYear + lastMonth + lastDay);
            int currentYMD = Integer.parseInt(currentYear + currentMonth + currentDay);

            if (currentYMD > lastYMD) {
                isShow = true;
            } else {
                if (Integer.parseInt(currentHour) - Integer.parseInt(lastHour) >= 6)
                    isShow = true;
            }
        }

        if(closeCount == -1)
            isShow = true;

        if(!isShow)
            return;

        for(int i=0; i<infectedCount; i++) {
            int locationCount = PreferenceManager.getInt(getApplicationContext(), "infected" + i + "LocationCount");

            for(int j=0; j<locationCount; j++) {
                float infectedLat = PreferenceManager.getFloat(getApplicationContext(), "infected" + i + "Lat" + j);
                float infectedLng = PreferenceManager.getFloat(getApplicationContext(), "infected" + i + "Lng" + j);

                double distance = Util.distance(currentLat, currentLng, infectedLat, infectedLng, "kilometer");
        //        Log.d(LOGSERVICE, "distance: " + distance);

                if(distance <= 3) {
//                    if(isShow) {
    //                    PreferenceManager.setInt("logWaringClose" + closeCount + "Date");

//                    String date = PreferenceManager.getString(getApplicationContext(), "infected" + i + "Date" + j);
                    String address = PreferenceManager.getString(getApplicationContext(), "infected" + i + "Address" + j);

                    PreferenceManager.setInt(this, "logWaringCloseCount", ++closeCount);

                    PreferenceManager.setInt(this, "logWaringClose" + closeCount + "Infected", i);
                    PreferenceManager.setInt(this, "logWaringClose" + closeCount + "Route", i);
                    PreferenceManager.setString(this, "logWaringClose" + closeCount + "Year", currentYear);
                    PreferenceManager.setString(this, "logWaringClose" + closeCount + "Month", currentMonth);
                    PreferenceManager.setString(this, "logWaringClose" + closeCount + "Day", currentDay);
                    PreferenceManager.setString(this, "logWaringClose" + closeCount + "Hour", currentHour);
                    PreferenceManager.setString(this, "logWaringClose" + closeCount + "Min", currentMin);

                    Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
                    intent.putExtra("title", "현재 " + (i + 1) + "번째 확진자가 활동한 장소에 근접해있습니다!");
                    intent.putExtra("context", " - " + address);
                    intent.putExtra("id", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    break;
//                    }
                }
            }
        }

//        view = LayoutInflater.from(this).inflate(R.layout.activity_notice, null);
//        // 윈도우에 뷰 추가
//        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//
//        // 챗 헤드 위치 지정
//        int gravity = Gravity.CENTER | Gravity.CENTER;
//        int x = 0;
//        int y = 0;
//
//        params.gravity = gravity;
//        params.x = x;
//        params.y = y;
//
//        // 윈도우에 뷰 추가
//        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        mWindowManager.addView(view, params);
    }

    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_alert_close:
//                mWindowManager.removeView(view);
//                Toast.makeText(this, "hello...?", Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOGSERVICE, "onDestroy - Estou sendo destruido ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOGSERVICE, "onConnectionFailed ");

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }
}