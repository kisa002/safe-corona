package com.haeyum.safecorona;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.naver.maps.map.util.FusedLocationSource;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CheckService extends Service {
    private IBinder mIBinder = new MyBinder();

    private FusedLocationSource locationSource;

    public int var = 777; //서비스바인딩의 예시로 출력할 값

    class MyBinder extends Binder {
        CheckService getService(){
            return CheckService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("CheckService", "onBind()");
        return mIBinder;
    }

    @Override
    public void onCreate() {
        Log.e("CheckService", "onCreate()");
        super.onCreate();

//        locationSource = new FusedLocationSource(this, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("CheckService", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("LOG", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("CheckService", "onUnbind()");
        return super.onUnbind(intent);
    }
}
