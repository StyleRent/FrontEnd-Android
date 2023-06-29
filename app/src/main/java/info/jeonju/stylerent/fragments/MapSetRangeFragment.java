package info.jeonju.stylerent.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import info.jeonju.stylerent.activities.HomeActivity;
import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.LocationData;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.userdata.CoordinateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapSetRangeFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    String TOKEN;

    private String namelocation;
    private double latitude;
    private double longitude;
    private int circleOverlayValue;
    private TextView addressTextView;

    private Button button500m;
    private Button button1000m;
    private Button button1500m;
    private Button button2000m;


    private OnRangeSelectedListener rangeSelectedListener;

    public MapSetRangeFragment(String namelocation, double latitude, double longitude, int circleOverlayValue, OnRangeSelectedListener rangeSelectedListener) {
        this.namelocation = namelocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.circleOverlayValue = circleOverlayValue;
        this.rangeSelectedListener = rangeSelectedListener;
    }

    public interface OnRangeSelectedListener {
        void onRangeSelected(int circleOverlayValue);
    }

    public void setAddressText(String text) {
        if (addressTextView != null) {
            addressTextView.setText(text);
        }
    }

    public void setCurrentLocation(String nameLocation, double latitude, double longitude) {
        this.namelocation = nameLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void initButtons() {
        button500m.setOnClickListener(v -> onRangeButtonClicked(500));
        button1000m.setOnClickListener(v -> onRangeButtonClicked(1000));
        button1500m.setOnClickListener(v -> onRangeButtonClicked(1500));
        button2000m.setOnClickListener(v -> onRangeButtonClicked(2000));
    }

    public void onRangeButtonClicked(int rangeValue) {
        circleOverlayValue = rangeValue;
        if (rangeSelectedListener != null) {
            rangeSelectedListener.onRangeSelected(circleOverlayValue);
        }
        setCurrentLocation(namelocation, latitude, longitude); // nameLocation 값을 설정
    }

    private void saveCircleOverlayValue(int circleOverlayValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("circleOverlay", circleOverlayValue);
        editor.apply();
    }


    private void updateLocation(String TOKEN,String namelocation,double longitude,double latitude){

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(requireContext()).create(ApiInterface.class);
        LocationData locationData = new LocationData(namelocation, Double.toString(longitude), Double.toString(latitude));

        Call<CoordinateResponse> updatedLocationRequest = apiInterface.updatecurrentlocation("Bearer " + TOKEN, locationData);
        updatedLocationRequest.enqueue(new Callback<CoordinateResponse>() {
            @Override
            public void onResponse(Call<CoordinateResponse> call, Response<CoordinateResponse> response) {
                if (response.isSuccessful()) {
                    CoordinateResponse currentLocation = response.body();
                    System.out.println("업데이트 성공");
                    System.out.println("업데이트 위도"+currentLocation.getLatitude());
                    System.out.println("업데이트 경도"+currentLocation.getLongitude());
                    System.out.println("업데이트 위치이름" + currentLocation.getNamelocation());
                }
            }

            @Override
            public void onFailure(Call<CoordinateResponse> call, Throwable t) {
                System.out.println("업데이트 실패");
                Log.e("MapSetRangeFragment", "onFailure: " + t.getMessage());
                Toast.makeText(requireContext(), "Incorrect Email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_set_range, container, false);
        addressTextView = view.findViewById(R.id.addressTextView);
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        TOKEN = sharedPreferences.getString("TOKEN", null);

        button500m = view.findViewById(R.id.button_500m);
        button1000m = view.findViewById(R.id.button_1000m);
        button1500m = view.findViewById(R.id.button_1500m);
        button2000m = view.findViewById(R.id.button_2000m);

        // Set 도로명 주소
        addressTextView.setText(namelocation);

        button500m.setOnClickListener(v -> onRangeButtonClicked(500));
        button1000m.setOnClickListener(v -> onRangeButtonClicked(1000));
        button1500m.setOnClickListener(v -> onRangeButtonClicked(1500));
        button2000m.setOnClickListener(v -> onRangeButtonClicked(2000));




        Button requestButton = view.findViewById(R.id.requestBtn);
        requestButton.setOnClickListener(v -> {
            // shared에 원 범위 저장
            saveCircleOverlayValue(circleOverlayValue);
            // db 업데이트
            String TOKEN = sharedPreferences.getString("TOKEN", null);
            updateLocation(TOKEN,namelocation,longitude,latitude);
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        });




        initButtons();

        return view;
    }
}