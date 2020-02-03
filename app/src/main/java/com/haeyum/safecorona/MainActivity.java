package com.haeyum.safecorona;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.haeyum.safecorona.models.InfectedRoute;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnCameraChangeListener {

    // Views
    private ConstraintLayout clMain;
    private ConstraintLayout clDetail;

    private ConstraintLayout clDetailInfo;
    private ConstraintLayout clDetailRoute;

    private ConstraintLayout clLoading;

    private LinearLayout llMore;

    // Detail
    private TextView tvDetailTitle, tvDetailContext;

    private TextView tvInfoInfected, tvInfoSymptom, tvInfoRelease, tvInfoQuarantine, tvInfoRecovery, tvInfoDeath, tvInfoDate;

    // Data
    private ArrayList<ArrayList<InfectedRoute>> listInfectedRoute = new ArrayList<>();

    private int selectedCount = -1;
    private int selectedIndex = -1;

    private int prevCount = -1;
    private int prevIndex = -1;

    private boolean isServerConnected;

    // Info
    private int infected = -1;
    private int symptom = -1;
    private int release = -1;
    private int quarantine = -1;
    private int recovery = -1;
    private int death = -1;
    private String date = "";

    // Location
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    // Maps
    private NaverMap naverMap;

    // Mps Overlay
    private ArrayList<CircleOverlay> listCircle = new ArrayList<>();
    private ArrayList<Marker> listMarker = new ArrayList<>();
    private ArrayList<PathOverlay> listPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
        intent.putExtra("title", "공지사항");
        intent.putExtra("context", "공지사항을 넣어봅시다!");
        intent.putExtra("id", 1);
        startActivity(intent);
//        Timer timer = new Timer();
//        timer.schedule(timerServerCheck, 0, 5000);
//        initDetailInfectedRoute();
    }

    private void initUI()
    {
        clMain = findViewById(R.id.cl_main);
        clDetail = findViewById(R.id.cl_detail);

        clDetailInfo = findViewById(R.id.cl_main_detail_info);
        clDetailRoute = findViewById(R.id.cl_main_detail_route);
        clLoading = findViewById(R.id.cl_loading);

        llMore = findViewById(R.id.ll_more);

        tvDetailTitle = findViewById(R.id.tv_main_detail_title);
        tvDetailContext = findViewById(R.id.tv_main_detail_context);

        tvInfoInfected = findViewById(R.id.tv_main_detail_info_infected_count);
        tvInfoSymptom = findViewById(R.id.tv_main_detail_info_symptom_count);
        tvInfoRelease = findViewById(R.id.tv_main_detail_info_release_count);
        tvInfoQuarantine = findViewById(R.id.tv_main_detail_info_quarantine_count);
        tvInfoRecovery = findViewById(R.id.tv_main_detail_info_recovery_count);
        tvInfoDeath = findViewById(R.id.tv_main_detail_info_death_count);
        tvInfoDate = findViewById(R.id.tv_main_detail_date);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    private void initDetailInfectedRoute(int count, int index) {
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
//        ArrayList<String> list = new ArrayList<>();
        ArrayList<InfectedRoute> list = new ArrayList<>();
//
//        for (int i=0; i<100; i++) {
////            list.add(String.format("1월 %d일", i)) ;
//            InfectedRoute infectedRoute = new InfectedRoute();
//            infectedRoute.setDate("1월 " + i + "일");
//            infectedRoute.setRoute("화장실 " + i + "번째 칸");
//            list.add(infectedRoute);
//        }

//        if(listInfectedRoute.size() > 2)
        for(int i=0; i<listInfectedRoute.get(count).size(); i++) {
            list.add(listInfectedRoute.get(count).get(i));
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        final RecyclerView recyclerView = findViewById(R.id.rv_main_detail_location);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        final DetailInfectedRouteAdapter adapter = new DetailInfectedRouteAdapter(list);
        recyclerView.setAdapter(adapter);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },3000);

//        Log.d("initDetailInfectedRoute", "리사이클러뷰 업데이트 완료");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_detail_more:
                llMore.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ll_more:
                llMore.setVisibility(View.GONE);
                break;

            case R.id.cl_more_laboratory:
                startActivity(new Intent(getApplicationContext(), LaboratoryActivity.class));
                break;

            case R.id.cl_more_prevention_method:
                startActivity(new Intent(getApplicationContext(), PreventionActivity.class));
                break;

            case R.id.cl_more_close:
                llMore.setVisibility(View.GONE);
                break;
        }
    }

    private void showDetailInfo() {
        clDetailInfo.setVisibility(View.VISIBLE);
        clDetailRoute.setVisibility(View.GONE);
    }

    private void showDetailRoute() {
        clDetailInfo.setVisibility(View.GONE);
        clDetailRoute.setVisibility(View.VISIBLE);
    }

    final Handler handlerInitDetailInfectedRoute = new Handler(){
        public void handleMessage(Message msg){
            initDetailInfectedRoute(selectedCount, selectedIndex);
        }
    };

    final Handler handlerUpdateMap = new Handler(){
        public void handleMessage(Message msg){
            updateMap();
        }
    };

    final Handler handlerUpdateInfo = new Handler() {
        public void handleMessage(Message msg) {
            tvInfoInfected.setText(infected + "명");
            tvInfoSymptom.setText(symptom + "명");
            tvInfoRelease.setText(release + "명");
            tvInfoQuarantine.setText(quarantine + "명");
            tvInfoRecovery.setText(recovery + "명");
            tvInfoDeath.setText(death + "명");
            tvInfoDate.setText(date);
        }
    };

    private void loadServerDetail() {
        new Thread() {
            @Override
            public void run() {
                if(isServerConnected)
                    return;

                try {
                    URL url = new URL("https://haeyum.ml/safe-corona/api/infected.json");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    InputStream is = con.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String result;
                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }

                    result = sb.toString();
//                    Log.d("HTTP Success", result);
                    JSONArray jsonArray = new JSONArray(result);
//                    Log.d("JSON", "LENGTH: " + jsonArray.length());

                    if(isServerConnected)
                        return;

                    isServerConnected = true;

                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");

                        JSONArray arrayLocation = new JSONArray(jsonObject.getString("location"));

                        Log.d("TITLE", title);
//                        Log.d("location", objLocation.getString("lat") + "/ " + objLocation.getString("lng"));

                        ArrayList<InfectedRoute> infectedRoutes = new ArrayList<>();

                        int color = Color.parseColor("#80" + jsonObject.getString("color"));

                        PathOverlay path = new PathOverlay();
                        ArrayList<LatLng> pathCoords = new ArrayList<>();

                        path.setColor(color);

                        for(int j=0; j<arrayLocation.length(); j++) {
                            JSONObject objLocation = arrayLocation.getJSONObject(j);
                            final LatLng latLng = new LatLng(objLocation.getDouble("lat"), objLocation.getDouble("lng"));

                            CircleOverlay circle = new CircleOverlay();
                            circle.setCenter(latLng);
                            circle.setRadius(getZoomRadius()); //(21 - naverMap.getCameraPosition().zoom) * 10
                            circle.setColor(color); //0x80ff5050
                            circle.setGlobalZIndex(100000);

                            Marker marker = new Marker();
                            marker.setPosition(latLng);
                            marker.setGlobalZIndex(150000);
//                            marker.setIcon(null);
                            marker.setWidth(10);
                            marker.setHeight(10);
                            marker.setIcon(MarkerIcons.BLACK);
                            marker.setIconTintColor(color); //0x80ff5050
                            marker.setCaptionText((i + 1) + "번");

                            if(latLng.latitude != -1 || latLng.longitude != -1)
                                pathCoords.add(latLng);
//                            circle.setTag(i + "번째 확진자");

                            final int count = i;
                            final int index = j;

                            circle.setOnClickListener(new Overlay.OnClickListener() {
                                @Override
                                public boolean onClick(@NonNull Overlay overlay) {
                                    selectedCount = count;
                                    selectedIndex = index;

                                    showDetailRoute();

                                    Message msg = handlerInitDetailInfectedRoute.obtainMessage();
                                    handlerInitDetailInfectedRoute.sendMessage(msg);
//                                    initDetailInfectedRoute(count, index);

                                    tvDetailTitle.setText((count + 1) + "번째 확진자");
                                    tvDetailContext.setText("코로나바이러스 확진자의 이동 경로입니다");

                                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Linear); //new LatLng(37.5666102, 126.9783881)
                                    naverMap.moveCamera(cameraUpdate);

                                    Log.d("CircleClick", count + "번째 확진자: " + index);
                                    return false;
                                }
                            });

                            listCircle.add(circle);
                            listMarker.add(marker);
//                            Log.d("Circle Add", i + ": " + latLng);

                            InfectedRoute tempInfetedRoute = new InfectedRoute();
                            tempInfetedRoute.setDate(objLocation.getString("date"));
                            tempInfetedRoute.setRoute(objLocation.getString("address"));

                            infectedRoutes.add(tempInfetedRoute);
                        }

                        if(pathCoords.size() < 2)
                            path = null;
                        else
                            path.setCoords(pathCoords);

                        listPath.add(path);
                        listInfectedRoute.add(infectedRoutes);
                    }
//                    initDetailInfectedRoute();
                    Message msg = handlerUpdateMap.obtainMessage();
                    handlerUpdateMap.sendMessage(msg);

                    isServerConnected = true;
                } catch (MalformedURLException e) {
                    Log.d("HTTP Failed", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("HTTP Failed", e.toString());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d("JSON Failed", e.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void loadServerInfo() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://haeyum.ml/safe-corona/api/info.json");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    InputStream is = con.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String result;
                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }

                    result = sb.toString();
                    JSONObject jsonObject = new JSONObject(result);

                    infected = jsonObject.getInt("infected");
                    symptom = jsonObject.getInt("symptom");
                    release = jsonObject.getInt("release");
                    quarantine = jsonObject.getInt("quarantine");
                    recovery = jsonObject.getInt("recovery");
                    death = jsonObject.getInt("death");
                    date = jsonObject.getString("date");

                    Message msg = handlerUpdateInfo.obtainMessage();
                    handlerUpdateInfo.sendMessage(msg);
                } catch (MalformedURLException e) {
                    Log.d("HTTP Failed", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("HTTP Failed", e.toString());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d("JSON Failed", e.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private double getZoomRadius() {
        double origin = 100;
        double zoom = 0;
//        Log.d("A",(int)naverMap.getCameraPosition().zoom + " AAA+");
        switch ((int)naverMap.getCameraPosition().zoom) {
            case 6:
                zoom = 12000;
                break;

            case 7:
                zoom = 8000;
                break;

            case 8:
                zoom = 4500;
                break;

            case 9:
                zoom = 2600;
                break;

            case 10:
                zoom = 1500;
                break;

            case 11:
                zoom = 1200;
                break;

            case 12:
                zoom = 600;
                break;

            case 13:
                zoom = 400;
                break;

            case 14:
                zoom = 280;
                break;

            case 15:
                zoom = 160;
                break;

            case 16:
                zoom = 80;
                break;

            case 17:
                zoom = 40;
                break;

            case 18:
                zoom = 20;
                break;

            case 19:
                zoom = 10;
                break;

            case 20:
                zoom = 5;
                break;

            case 21:
                zoom = 2;
                break;
        }

        return zoom;
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.addOnCameraChangeListener(this);
        naverMap.setMaxZoom(21);
        naverMap.setMinZoom(6);

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
//                if(prevCount == -1 && prevIndex == -1) {
//                    prevCount = selectedCount;
//                    prevIndex = selectedIndex;
//                }

                if(prevCount == selectedCount && prevIndex == selectedIndex) {
                    tvDetailTitle.setText("세이프 코로나 Safe Corona");
                    tvDetailContext.setText("확진자 위치 파악, 실시간 위치정보 알림 서비스");

                    showDetailInfo();
                }

                Log.d("MapClick", selectedCount + ":" + prevCount + " / " + selectedIndex + ":" + prevIndex);
                prevCount = selectedCount;
                prevIndex = selectedIndex;
            }
        });

//        LatLng latLng = new LatLng(37.564018, 127.029536);
        LatLng latLng = new LatLng(37.485829, 126.781014);
//
//        CircleOverlay circle = new CircleOverlay();
//        circle.setCenter(latLng);
//        circle.setRadius(getZoomRadius()); //(21 - naverMap.getCameraPosition().zoom) * 10
//        circle.setColor(0x80FF0000);
//        circle.setZIndex(400000);
//        circle.setMap(naverMap);
//
//        listCircle.add(circle);

//        Toast.makeText(this, "Zoom: " + naverMap.getCameraPosition().zoom, Toast.LENGTH_SHORT).show();

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng); //new LatLng(37.5666102, 126.9783881)
        naverMap.moveCamera(cameraUpdate);

        final Timer timer = new Timer();
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                if(isServerConnected) {
                    Message msg = handlerUpdateMap.obtainMessage();
                    handlerUpdateMap.sendMessage(msg);

                    timer.cancel();
                } else {
                    loadServerDetail();
                    loadServerInfo();
                }
            }
        };
        timer.schedule(TT, 0, 1000); //Timer 실행
    }

    private void updateMap() {
        if(!isServerConnected)
            return;

        for (CircleOverlay circle: listCircle) {
            if(circle == null)
                return;

//            if(circle.getCenter().latitude == -1 || circle.getCenter().longitude == -1)
//                break;

            circle.setRadius(getZoomRadius());
            circle.setMap(naverMap);
        }

        for (Marker marker: listMarker) {
//            if(naverMap.getCameraPosition().zoom < 10)
//                marker.setMap(null);
//            else

//            if(marker.getPosition().latitude == -1 || marker.getPosition().longitude == -1)
//                break;
            marker.setMap(naverMap);
        }

        for (PathOverlay path: listPath) {
            if(path == null)
                break;

//            if(naverMap.getCameraPosition().zoom > 10)
//                path.setMap(null);
//            else
            path.setMap(naverMap);
        }

        clLoading.setVisibility(View.GONE);
    }

    @Override
    public void onCameraChange(int i, boolean b) {
        if(naverMap == null)
            return;

        try {
            updateMap();
    //        Log.d("Circle Count", listCircle.size() + " CNT");

            Log.d("onCameraChange", "Zoom: " + naverMap.getCameraPosition().zoom);
    //        Log.d("onCameraChange", "Calc: " + getZoomRadius());

            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
}
