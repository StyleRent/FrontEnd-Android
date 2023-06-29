package info.jeonju.stylerent.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentManager;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.fragments.MapSetRangeFragment;
import info.jeonju.stylerent.userdata.CoordinateResponse;
import info.jeonju.stylerent.userdata.GetNearByData;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapSetRangeFragment.OnRangeSelectedListener {
    private static final String TAG = "MapActivity";

    private SharedPreferences sharedPreferences;

    ApiInterface apiInterface;
    private String token;

    private ImageButton mapBackButton;
    private double latitude;
    private double longitude;

    private String namelocation;
    private Marker marker;
    private CircleOverlay circleOverlay;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapSetRangeFragment bottomFragment;
    private FragmentManager fragmentManager;
    private int circleOverlayValue;
    private int updatedCircleOverlayValue;


    // 마커를 관리하기 위한 리스트
    List<Marker> userMarkers = new ArrayList<>();

    ImageButton currentLocationButton;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        token = sharedPreferences.getString("TOKEN", null);

        circleOverlayValue = sharedPreferences.getInt("circleOverlay", 500);


        fragmentManager = getSupportFragmentManager();

        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        mapBackButton = findViewById(R.id.map_back);
        mapBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(MapActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        currentLocationButton = findViewById(R.id.my_location_button);
        currentLocationButton.setOnClickListener(v -> moveTocurrentLocation());


    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);

        setInitialMap(naverMap);
        showNearbyUser(circleOverlayValue);
        setupMapClickListener();
    }

    private void setUserProfile(List<GetNearByData> getNearByData) {
        // 이전에 추가된 마커 삭제
        for (Marker marker : userMarkers) {
            marker.setMap(null);
        }
        userMarkers.clear();

        if (getNearByData != null) {
            for (GetNearByData gD : getNearByData) {
                Marker withinRangeUserMarker = new Marker();
                LatLng location = new LatLng(Double.parseDouble(gD.getLatitude()), Double.parseDouble(gD.getLongtitude()));
                withinRangeUserMarker.setPosition(location);
                withinRangeUserMarker.setMap(naverMap);
                withinRangeUserMarker.setIconTintColor(Color.parseColor("#70A8FA"));
                userMarkers.add(withinRangeUserMarker);
                System.out.println("주변유저이름:" + gD.getUserName());
            }
        } else {
            System.out.println("주변정보 못받음");
        }
    }



    private int makeCircleColor(){
        int color = getResources().getColor(R.color.green);
        int alpha = 120;
        int newColor = ColorUtils.setAlphaComponent(color, alpha);
        return newColor;
    }

    private void moveCamera(LatLng location){
        System.out.println("카메라 이동");
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(location).animate(CameraAnimation.Fly, 1500);
        naverMap.moveCamera(cameraUpdate);
    }

    public void setMarkerAndCircleOverlay(LatLng location) {

        int newColor = makeCircleColor();

        onRangeSelected(updatedCircleOverlayValue);

        System.out.println("업데이트 범위"+updatedCircleOverlayValue);

        if (marker != null && circleOverlay != null) {
            marker.setMap(null);
            circleOverlay.setMap(null);
        }

        marker = new Marker();
        marker.setPosition(location);
        marker.setMap(naverMap);

        circleOverlay = new CircleOverlay();
        circleOverlay.setCenter(location);
        circleOverlay.setColor(newColor);
        circleOverlay.setRadius(updatedCircleOverlayValue);
        circleOverlay.setMap(naverMap);

        moveCamera(location);
        showNearbyUser(updatedCircleOverlayValue);
    }

    private void setMapFragmentHeight(){
        int fragmentHeight = getFragmentHeight();
        ViewGroup.LayoutParams fragmentLayoutParams = findViewById(R.id.container_map).getLayoutParams();
        fragmentLayoutParams.height = fragmentHeight;
        findViewById(R.id.container_map).setLayoutParams(fragmentLayoutParams);
    }
    private void makeMapFragment(String namelocation){
        if (bottomFragment == null) {
            bottomFragment = new MapSetRangeFragment(namelocation, latitude, longitude, circleOverlayValue, MapActivity.this);
            fragmentManager.beginTransaction().replace(R.id.container_map, bottomFragment).commit();
        } else {
            bottomFragment.setAddressText(namelocation);
            bottomFragment.getView().setVisibility(View.VISIBLE);
        }

        if (bottomFragment.getView() != null) {
            ViewGroup.LayoutParams layoutParams = bottomFragment.getView().getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            bottomFragment.getView().setLayoutParams(layoutParams);
        }
    }

    private void setupMapClickListener() {
        naverMap.setOnMapClickListener((point, coord) -> {
            // 클릭된 위도 경도 구함
            latitude = coord.latitude;
            longitude = coord.longitude;
            LatLng location = new LatLng(latitude, longitude);



            setMarkerAndCircleOverlay(location);


            try {
                Geocoder geocoder = new Geocoder(getApplicationContext());
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    namelocation = address.getAddressLine(0);

                    setMapFragmentHeight();
                    makeMapFragment(namelocation);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setInitialSetting(int circleRangeValue, LatLng location) {
        updatedCircleOverlayValue = circleRangeValue;
        int newColor = makeCircleColor();
        if (marker != null) {
            marker.setMap(null);
        }
        marker = new Marker();
        marker.setPosition(location);
        marker.setMap(naverMap);

        circleOverlay = new CircleOverlay();

        if (updatedCircleOverlayValue == 0) {
            updatedCircleOverlayValue = 500;
        }

        circleOverlay.setRadius(updatedCircleOverlayValue);
        circleOverlay.setColor(newColor);
        circleOverlay.setCenter(location);
        circleOverlay.setMap(naverMap);
    }

    @Override
    public void onRangeSelected(int circleOverlayValue) {
        updatedCircleOverlayValue = circleOverlayValue;
        if (circleOverlay != null) {
            circleOverlay.setRadius(updatedCircleOverlayValue);
            circleOverlay.setMap(naverMap);
        }
        showNearbyUser(updatedCircleOverlayValue);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                onBackPressed();
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void moveTocurrentLocation() {
        // 현재 위치로 지도 이동
        if (naverMap != null && locationSource != null && locationSource.getLastLocation() != null) {
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        }
    }

    private int getFragmentHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        return (int) (screenHeight * 0.3);
    }

    private void showNearbyUser(Integer circleOverlay){
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);
        Call<List<GetNearByData>> getNearByDataRequest = apiInterface.getNearByUsers("Bearer " + TOKEN, circleOverlay);
        getNearByDataRequest.enqueue(new Callback<List<GetNearByData>>() {
            @Override
            public void onResponse(Call<List<GetNearByData>> call, Response<List<GetNearByData>> response) {
                List<GetNearByData> getNearByData = response.body();
                setUserProfile(getNearByData);
            }

            @Override
            public void onFailure(Call<List<GetNearByData>> call, Throwable t) {

            }

        });
    }

    private void setInitialMap(NaverMap naverMap){
        Locale.setDefault(Locale.KOREAN);
        this.naverMap = naverMap;

        naverMap.setLocationSource(locationSource);
        // 초기 위치 설정
        setInitialCurrentLocation(apiInterface);

    }

    private void setInitialCurrentLocation(ApiInterface apiInterface){
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        Call<CoordinateResponse> getCurrentLocationRequest = apiInterface.getCurrentLocation("Bearer " + TOKEN);
        getCurrentLocationRequest.enqueue(new Callback<CoordinateResponse>() {
            @Override
            public void onResponse(Call<CoordinateResponse> call, Response<CoordinateResponse> response) {
                if (response.isSuccessful()) {
                    CoordinateResponse currentLocation = response.body();
                    if (currentLocation != null) {
                        String currentNameLocation = currentLocation.getNamelocation();
                        double currentLatitude = Double.parseDouble(currentLocation.getLatitude());
                        double currentLongitude = Double.parseDouble(currentLocation.getLongitude());
                        LatLng location = new LatLng(currentLatitude, currentLongitude);

                        moveCamera(location);

                        System.out.println("초기위치"+currentLocation.getNamelocation());
                        System.out.println("초기위도 경도"+currentLocation.getLongitude());
                        System.out.println("초기원 범위"+circleOverlayValue);

                        if (bottomFragment != null) {
                            bottomFragment.setCurrentLocation(currentNameLocation, currentLatitude, currentLongitude);
                        }

                        setInitialSetting(circleOverlayValue, location);
                    }
                }
            }

            @Override
            public void onFailure(Call<CoordinateResponse> call, Throwable t) {
                System.out.println("초기값 불러오기 실패");
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}