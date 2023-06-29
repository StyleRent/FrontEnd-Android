package info.jeonju.stylerent.fragments;

import static android.content.ContentValues.TAG;

import de.hdodenhof.circleimageview.CircleImageView;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import info.jeonju.stylerent.activities.MapActivity;
import info.jeonju.stylerent.activities.NewProductActivity;
import info.jeonju.stylerent.activities.ProductDetailActivity;
import info.jeonju.stylerent.R;
import info.jeonju.stylerent.activities.Relative_user_profile;
import info.jeonju.stylerent.activities.SearchedListActivity;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.FavoriteModels.FavResponse;
import info.jeonju.stylerent.auth.ImagePathRequest;
import info.jeonju.stylerent.auth.LocationData;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.userdata.CoordinateResponse;
import info.jeonju.stylerent.userdata.GetNearByData;
import info.jeonju.stylerent.userdata.ProductDataResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.naver.maps.map.NaverMap;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private Double latitude;
    private Double longitude; // 클릭된 위치의 경도
    private String locationName;
    private String fullAddress = "asdasd";
    private Boolean favStatus = false;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private NaverMap naverMap;

    private Toolbar toolbar;
    LinearLayout mapbtn;
    ImageButton mapImage;

    TextView mapText;

    MenuItem addBtn;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //toolbar
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mapbtn = view.findViewById(R.id.map_btn);
        mapImage = view.findViewById(R.id.map_image);
        mapText = view.findViewById(R.id.map_text);


        getUserCurrentLocation();



        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });

        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });


        setHasOptionsMenu(true);

        return view;
    }

    //User Location
    private void getUserCurrentLocation() {
        //make Get request to get user's current location
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<CoordinateResponse> userProfileCall = apiInterface.getCurrentLocation("Bearer" + TOKEN);
        userProfileCall.enqueue(new Callback<CoordinateResponse>() {
            @Override
            public void onResponse(Call<CoordinateResponse> call, Response<CoordinateResponse> response) {
                if (response.isSuccessful()) {
                    CoordinateResponse coordinateResponse = response.body();
                    if (coordinateResponse.getError() != null) {
                        System.out.println("error response ---------------->>>>>>"+coordinateResponse.getError());
                        requestLocationPermission();
                    }
                    else{
                        //getnearby api req
                        System.out.println("네임 로케이션"+response.body().getNamelocation());
                        System.out.println("lat"+response.body().getLatitude());
                        String[] shortVer = response.body().getNamelocation().split(" ");
                        mapText.setText(shortVer[shortVer.length-2]);
                        getNearbyUserData();
                    }

                }
            }

            @Override
            public void onFailure(Call<CoordinateResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to get user current location: " + t.getMessage());

            }
        });
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user why the permission is needed
            // You can display a dialog or a Snackbar explaining the need for location permission

            // Example Snackbar:
            Snackbar.make(requireView(), "Location permission is required to show your current location.",
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> {
                        // Request the permission
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    })
                    .show();
        } else {
            // Request location permission if the last known location is not available
            // Init location
            Locale.setDefault(Locale.KOREAN);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(100);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null && latitude == null && longitude == null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        Geocoder geocoder = new Geocoder(getContext());
                        List<Address> addresses = null;

                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (!addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                fullAddress = address.getLocality(); // 전체 주소
                                String locality = address.getLocality(); // 시/도
                                String subLocality = address.getSubLocality(); // 구/군
                                String thoroughfare = address.getThoroughfare(); // 도로명
                                System.out.println("fulll adddreesss ------------------------:>>>"  + fullAddress);
                                System.out.println("locality ---->" + locality);
                                System.out.println("subLocality ---->" + subLocality);
                                System.out.println("thoroughfare ---->" + thoroughfare);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String TOKEN = sharedPreferences.getString("TOKEN", null);
                        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
                        LocationData locationData = new LocationData(fullAddress, Double.toString(longitude), Double.toString(latitude));

                        Call<CoordinateResponse> userProfileCall = apiInterface.setCurrentLocation("Bearer" + TOKEN, locationData);

                        userProfileCall.enqueue(new Callback<CoordinateResponse>() {
                            @Override
                            public void onResponse(Call<CoordinateResponse> call, Response<CoordinateResponse> response) {
                                if(response.isSuccessful()){

                                    CoordinateResponse currentLocation = response.body();
                                    System.out.println("네임 로케이션 "+ currentLocation.getNamelocation());
                                    Log.d("Location Response", "Location name: " + currentLocation.getLatitude());
                                    mapText.setText(currentLocation.getNamelocation());

                                }
                            }
                            @Override
                            public void onFailure(Call<CoordinateResponse> call, Throwable t) {

                            }
                        });

                        Log.d("Location", "Latitude:?? " + latitude + ", Longitude: " + longitude);
                    }
                }
            };


            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
            }
        }

    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // Update interval in milliseconds

        return locationRequest;
    }

    private void getNearbyUserData(){

        String TOKEN = sharedPreferences.getString("TOKEN", null);

        Integer distance = sharedPreferences.getInt("circleOverlay", 500);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<List<GetNearByData>> getNearbyUser = apiInterface.getNearByUsers("Bearer" + TOKEN, distance);
        getNearbyUser.enqueue(new Callback<List<GetNearByData>>() {
            @Override
            public void onResponse(Call<List<GetNearByData>> call, Response<List<GetNearByData>> response) {
                for(GetNearByData res : response.body()){
                    System.out.println(res.getUserName());
                }
                List<GetNearByData> getNearByData = response.body();
                setUserItems(getNearByData);
            }

            @Override
            public void onFailure(Call<List<GetNearByData>> call, Throwable t) {
                System.out.println("ERrrorrr ------->>>>>>>> is done");
            }
        });

    }

    private void setUserItems(List<GetNearByData> getNearByData){

        for(GetNearByData nearbyData : getNearByData){
            LinearLayout userDataLayout = view.findViewById(R.id.userDataLayout);
            // Create the parent LinearLayout
            LinearLayout parentLayout = new LinearLayout(getContext());
            parentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parentLayout.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            parentLayout.setOrientation(LinearLayout.VERTICAL);

// Create the first TextView


            LinearLayout userProfileAndNameLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams userProfileAndNameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.8f);
            userProfileAndNameLayoutParams.setMargins(0, dpToPx(4), 0, 0); // 위쪽 여백 설정
            userProfileAndNameLayout.setLayoutParams(userProfileAndNameLayoutParams);
            userProfileAndNameLayout.setGravity(Gravity.CENTER_VERTICAL); // 수직으로 중앙 정렬을 설정합니다.
            userProfileAndNameLayout.setOrientation(LinearLayout.HORIZONTAL);


            // Create CircleImageView instance
            CircleImageView userImageView = new CircleImageView(getContext());

            int imageSizeInPixels = dpToPx(50); // 이미지 뷰의 크기를 설정합니다. 여기서는 50dp를 픽셀(px)로 변환하여 사용했습니다.
            LinearLayout.LayoutParams userImageLayoutParams = new LinearLayout.LayoutParams(imageSizeInPixels, imageSizeInPixels);
            userImageLayoutParams.setMargins(dpToPx(6), dpToPx(4), dpToPx(8), dpToPx(1)); // 여백을 설정합니다.
            userImageView.setLayoutParams(userImageLayoutParams);
            //user Image
            if (nearbyData.getProfileImage() != null) {
                byte[] image = Base64.decode(nearbyData.getProfileImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                userImageView.setImageBitmap(bitmap);
            }

            userImageView.setBackgroundResource(R.drawable.circle_background);

            TextView textView1 = new TextView(getContext());
            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView1.setText(nearbyData.getUserName());
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);


            TextView distance = new TextView(getContext());
            LinearLayout.LayoutParams distanceLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            distanceLayoutParams.setMargins(dpToPx(30), 0, dpToPx(0), 0); // 왼쪽 여백 설정
            distance.setLayoutParams(distanceLayoutParams);
            distance.setTextSize(16);
            distance.setGravity(Gravity.CENTER_VERTICAL);
            distance.setTextColor(Color.parseColor("#E06469"));
            double distanceValue = Double.parseDouble(nearbyData.getDistance());
            int distanceInt = (int) distanceValue;
            String distanceText = String.format("%dm", distanceInt);
            distance.setText(distanceText);



            userProfileAndNameLayout.addView(userImageView);
            userProfileAndNameLayout.addView(textView1);
            userProfileAndNameLayout.addView(distance);

            parentLayout.addView(userProfileAndNameLayout);
            userProfileAndNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Relative_user_profile.class);
                    intent.putExtra("userId", nearbyData.getUserId());
                    startActivity(intent);
                }
            });

// Create the HorizontalScrollView
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
            horizontalScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            parentLayout.addView(horizontalScrollView);

// Create the LinearLayout inside HorizontalScrollView
            LinearLayout innerLinearLayout = new LinearLayout(getContext());
            innerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            innerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            innerLinearLayout.setWeightSum(2);
            horizontalScrollView.addView(innerLinearLayout);

            for(ProductDataResponse p: nearbyData.getProducts()){

                // Create the LinearLayout for the first product
                LinearLayout productLayout = new LinearLayout(getContext());
                productLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                productLayout.setOrientation(LinearLayout.VERTICAL);
                productLayout.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
                innerLinearLayout.addView(productLayout);

// Create the ConstraintLayout inside the first product LinearLayout
                ConstraintLayout constraintLayout = new ConstraintLayout(getContext());
                constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                productLayout.addView(constraintLayout);

// Create the ImageView inside ConstraintLayout
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new ConstraintLayout.LayoutParams(dpToPx(170), dpToPx(200)));


                //check bit map
                getImageByPath(p.getImagePath().get(0).getPath(), new BitmapCallback() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            // Handle the case where the image path list is null or empty
                        }

//                            imageView.setImageBitmap(bitmap); // Set the loaded bitmap on the ImageView


                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Handle the failure here
                    }
                });
                constraintLayout.addView(imageView);

// Create the ImageButton inside ConstraintLayout
                ImageButton imageButton = new ImageButton(getContext());
                imageButton.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                imageButton.setImageResource(R.drawable.ic_favorite);
                imageButton.setBackgroundColor(Color.TRANSPARENT);
                if(p.getLiked()){
                    imageButton.setImageResource(R.drawable.ic_favorite_border);
                    imageButton.setBackgroundColor(Color.TRANSPARENT);
                }
                ConstraintLayout.LayoutParams imageButtonLayoutParams = (ConstraintLayout.LayoutParams) imageButton.getLayoutParams();
                imageButtonLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // 부모의 오른쪽에 맞추기
                imageButtonLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID; // 부모의 상단에 맞추기
                imageButtonLayoutParams.horizontalBias = 1.0f; // 가로 위치를 오른쪽으로 이동
                imageButtonLayoutParams.verticalBias = 0.0f; // 세로 위치를 상단에서 아래로 조정
                imageButtonLayoutParams.topMargin = dpToPx(4);

                favStatus = p.getLiked();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //api 요청
                        if(favStatus){
                            imageButton.setImageResource(R.drawable.ic_favorite);
                            imageButton.setBackgroundColor(Color.TRANSPARENT);
                            // 좋아요 제거 요청

                            deleteLike(p.getProductId());

                        }else{
                            imageButton.setImageResource(R.drawable.ic_favorite_border);
                            imageButton.setBackgroundColor(Color.TRANSPARENT);
                            // 좋아요 추가 요청

                            addLike(p.getProductId());
                        }
                    }
                });
                constraintLayout.addView(imageButton);
                constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("userId", nearbyData.getUserId());
                        intent.putExtra("productId", p.getProductId());
                        intent.putExtra("userName", nearbyData.getUserName());
                        intent.putExtra("productName", p.getProductName());
                        intent.putExtra("productPrice", p.getProductPrice());
                        intent.putExtra("productInfo", p.getProductInfo());
                        intent.putExtra("productId", p.getProductId());
                        intent.putExtra("rankAverage", p.getRankAverage());
                        intent.putExtra("numsReview", p.getNumsReview());
                        startActivity(intent);
                    }
                });

// Create the LinearLayout inside the first product LinearLayout
                LinearLayout innerProductLayout = new LinearLayout(getContext());
                innerProductLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                productLayout.addView(innerProductLayout);

// Create the TextView for Product Name1
                TextView productNameTextView = new TextView(getContext());
                LinearLayout.LayoutParams productNameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                productNameLayoutParams.setMargins(dpToPx(6), dpToPx(4), 0, 0);
                productNameTextView.setLayoutParams(productNameLayoutParams);
                productNameTextView.setText(p.getProductName());
                innerProductLayout.addView(productNameTextView);

// Create the TextView for User Name1
                TextView userNameTextView = new TextView(getContext());
                LinearLayout.LayoutParams userNameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                userNameLayoutParams.setMargins(dpToPx(6), dpToPx(4), 0, 0);
                userNameTextView.setLayoutParams(userNameLayoutParams);
                userNameTextView.setText(formatPrice(p.getProductPrice()));


                innerProductLayout.addView(userNameTextView);
                innerProductLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                        intent.putExtra("productId", p.getProductId());
                        intent.putExtra("userId", nearbyData.getUserId());
                        startActivity(intent);
                    }
                });

            }



// Add the parent LinearLayout to your desired container (e.g., a parent ViewGroup)
            userDataLayout.addView(parentLayout);
            // Add a separator line
            View lineView = new View(getContext());
            lineView.setBackgroundColor(Color.BLACK);
            LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(2)
            );
            lineLayoutParams.setMargins(dpToPx(14), dpToPx(10), dpToPx(14), dpToPx(10)); // 구분선 위아래 여백 설정
            lineView.setLayoutParams(lineLayoutParams);
            parentLayout.addView(lineView);

            lineView.setBackgroundColor(Color.parseColor("#E6E6E6"));
        }
    }

    private void deleteLike(Integer productId) {
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<FavResponse> userProfileCall = apiInterface.deleteFav("Bearer" + TOKEN, productId);
        userProfileCall.enqueue(new Callback<FavResponse>() {
            @Override
            public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                FavResponse favResponse = response.body();
                System.out.println("Deleted ");
                favStatus = false;


            }

            @Override
            public void onFailure(Call<FavResponse> call, Throwable t) {

            }
        });
    }

    private void addLike(Integer productId){

        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<FavResponse> userProfileCall = apiInterface.addFav("Bearer" + TOKEN, productId);
        userProfileCall.enqueue(new Callback<FavResponse>() {
            @Override
            public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                FavResponse favResponse = response.body();
                System.out.println("Added ");
                favStatus = true;

            }

            @Override
            public void onFailure(Call<FavResponse> call, Throwable t) {

            }
        });



    }

    private void getImageByPath(String imagePath, BitmapCallback callback){

        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        // Create a new ImagePathRequest with current image path
        ImagePathRequest imagePathRequest = new ImagePathRequest(imagePath);
        Call<ResponseBody> userProfileCall = apiInterface.getImageByPath("Bearer" + TOKEN, imagePathRequest);

        userProfileCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    callback.onBitmapLoaded(bitmap); // Pass the bitmap to the callback
                } else {
                    callback.onFailure(new Exception("Failed to load image")); // Pass failure to the callback
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }



    private SearchView makeSearchView(Menu menu){
        MenuItem searchItem = menu.findItem(R.id.search_btn);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search...");
        return searchView;
    }


    //menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.topnavbar_home, menu);


        addBtn = menu.findItem(R.id.add_btn);

        addBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // add_btn이 클릭되었을 때의 동작을 여기에 작성합니다.
                Intent intent = new Intent(getActivity(), NewProductActivity.class);
                startActivity(intent);
                return true;
            }
        });

        SearchView searchView = makeSearchView(menu);





        // SearchView의 검색 이벤트 리스너 설정
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchProduct(query);

//                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색어가 변경될 때마다 호출되는 메서드
                // 필요한 경우 검색어의 변경에 따라 검색 결과를 업데이트할 수 있음
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private String formatPrice(String price) {
        double amount = Double.parseDouble(price);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }


    private void searchProduct(String query){
        Intent intent = new Intent(getActivity(), SearchedListActivity.class);
        intent.putExtra("query",query );
        startActivity(intent);
    }



}