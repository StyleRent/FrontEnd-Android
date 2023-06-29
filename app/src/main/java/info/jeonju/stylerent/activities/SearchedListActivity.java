package info.jeonju.stylerent.activities;

import static info.jeonju.stylerent.fragments.HomeFragment.MyPREFERENCES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ImagePathRequest;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.fragments.BitmapCallback;
import info.jeonju.stylerent.userdata.GetNearByData;
import info.jeonju.stylerent.userdata.ProductDataResponse;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchedListActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String query;
    private Toolbar toolbar;
    private SearchView searchView;
    private ImageView backBtn;

    private Intent intent;

    private ProgressBar loadingSpinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_list);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        loadingSpinner = findViewById(R.id.loading_spinner);
        backBtn = findViewById(R.id.back_button);
        toolbar = findViewById(R.id.toolbar);
        searchView = toolbar.findViewById(R.id.search_view);
        Locale.setDefault(Locale.KOREAN);


        query = getIntent().getStringExtra("query");

        searchQuery(query);
        finishSearch();

//        InputMethodManager imm = (InputMethodManager) SearchedListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
//        searchView.clearFocus();



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSearch();
                Intent intent = new Intent(SearchedListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색어 제출 시 동작 구현
                searchQuery(query);
                finishSearch();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색어 변경 시 동작 구현
                return true;
            }


        });

    }



    private void finishSearch() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            currentFocus.clearFocus();
        }
    }


    private void searchQuery(String query) {
        loadingSpinner.setVisibility(View.VISIBLE);

        String TOKEN = sharedPreferences.getString("TOKEN", null);
        Integer distance = sharedPreferences.getInt("circleOverlay", 500);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);
        Call<List<GetNearByData>> userProfileCall = apiInterface.getNearByUsers("Bearer" + TOKEN, distance);

        userProfileCall.enqueue(new Callback<List<GetNearByData>>() {
            @Override
            public void onResponse(Call<List<GetNearByData>> call, Response<List<GetNearByData>> response) {
                List<GetNearByData> getNearByData = response.body();

                List<GetNearByData> filteredList = getFilteredUser(getNearByData, query);

                showProductList(filteredList, query);
                loadingSpinner.setVisibility(View.GONE);

                finishSearch(); // 키보드 숨기기
            }

            @Override
            public void onFailure(Call<List<GetNearByData>> call, Throwable t) {
                loadingSpinner.setVisibility(View.GONE);
            }
        });
    }

    private List<GetNearByData> getFilteredUser(List<GetNearByData> getNearByData, String product) {
        List<GetNearByData> filteredList = new ArrayList<>();

        if (getNearByData == null) {
            return filteredList;
        }

        for (GetNearByData nearbyData : getNearByData) {
            boolean productFound = false;

            for (ProductDataResponse p : nearbyData.getProducts()) {
                if (isItemMatching(p.getProductName(), product)) {
                    productFound = true;
                    break;
                }
            }

            if (productFound) {
                filteredList.add(nearbyData);
            }
        }

        return filteredList;
    }

    private void showProductList(List<GetNearByData> filteredList, String query) {
        searchView.setQuery(query, false);
        LinearLayout userDataLayout = findViewById(R.id.userDataLayout);
        userDataLayout.removeAllViews(); // 기존 요소들을 모두 제거

        for (GetNearByData productData : filteredList) {
            for (ProductDataResponse p : productData.getProducts()) {
                // Create the parent LinearLayout
                LinearLayout parentLayout = new LinearLayout(this);
                parentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                parentLayout.setOrientation(LinearLayout.HORIZONTAL);
                parentLayout.setPadding(dpToPx(14), 25, dpToPx(14), 25);

                LinearLayout imageLayout = new LinearLayout(this);
                imageLayout.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(120), dpToPx(120)));
                ImageView productImage = new ImageView(this);
                productImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                getImageByPath(p.getImagePath().get(0).getPath(), new BitmapCallback() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        productImage.setImageBitmap(bitmap); // Set the loaded bitmap on the ImageView
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Handle the failure here
                    }
                });

                imageLayout.addView(productImage);

                LinearLayout infoLayout = new LinearLayout(this);
                LinearLayout.LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                infoLayoutParams.setMargins(dpToPx(20), 0, 0, 0); // 왼쪽 여백 설정
                infoLayout.setLayoutParams(infoLayoutParams);
                infoLayout.setOrientation(LinearLayout.VERTICAL);

                TextView productName = new TextView(this);
                LinearLayout.LayoutParams productNameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.8f);
                productName.setLayoutParams(productNameLayoutParams);
                productName.setTextSize(22);
                productName.setText(p.getProductName());
                productName.setTextColor(Color.parseColor("#212529"));

                LinearLayout addressDistanceLayout = new LinearLayout(this);
                LinearLayout.LayoutParams addressDistanceLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                addressDistanceLayoutParams.setMargins(0, dpToPx(4), 0, 0); // 위쪽 여백 설정
                addressDistanceLayout.setLayoutParams(addressDistanceLayoutParams);
                addressDistanceLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView productLocation = new TextView(this);
                LinearLayout.LayoutParams productLocationLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                productLocation.setLayoutParams(productLocationLayoutParams);
                productLocation.setTextSize(18);
                productLocation.setGravity(Gravity.CENTER_VERTICAL);
                String fullAddress = getAddress(Double.parseDouble(productData.getLatitude()), Double.parseDouble(productData.getLongtitude()));
                productLocation.setText(fullAddress);

                TextView distance = new TextView(this);
                LinearLayout.LayoutParams distanceLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                distanceLayoutParams.setMargins(dpToPx(8), 0, 0, 0); // 왼쪽 여백 설정
                distance.setLayoutParams(distanceLayoutParams);
                distance.setTextSize(16);
                distance.setGravity(Gravity.CENTER_VERTICAL);
                distance.setTextColor(Color.parseColor("#E06469"));
                double distanceValue = Double.parseDouble(productData.getDistance());
                int distanceInt = (int) distanceValue;
                String distanceText = String.format("%dm", distanceInt);
                distance.setText(distanceText);

                addressDistanceLayout.addView(productLocation);
                addressDistanceLayout.addView(distance);


                TextView productPrice = new TextView(this);
                LinearLayout.LayoutParams productPriceLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.8f);
                productPrice.setLayoutParams(productPriceLayoutParams);
                productPrice.setTextSize(22);
                productPrice.setGravity(Gravity.CENTER_VERTICAL);
                double productPriceValue = Double.parseDouble(p.getProductPrice());
                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                String formattedPrice = decimalFormat.format(productPriceValue);
                productPrice.setTypeface(productPrice.getTypeface(), Typeface.BOLD);
                productPrice.setTextColor(Color.parseColor("#212529"));
                productPrice.setText(formattedPrice + "원");

                infoLayout.addView(productName);
                infoLayout.addView(addressDistanceLayout);
                infoLayout.addView(productPrice);

                if (isItemMatching(p.getProductName(), query)) {
                    userDataLayout.addView(parentLayout);
                    View lineView = new View(this);
                    lineView.setBackgroundColor(Color.BLACK);
                    LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dpToPx(2)
                    );
                    lineLayoutParams.setMargins(dpToPx(14), dpToPx(10), dpToPx(14), dpToPx(10));
                    lineView.setLayoutParams(lineLayoutParams);
                    lineView.setBackgroundColor(Color.parseColor("#E6E6E6"));
                    userDataLayout.addView(lineView);



                    parentLayout.addView(imageLayout);
                    parentLayout.addView(infoLayout);

                    parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SearchedListActivity.this, ProductDetailActivity.class);
                            intent.putExtra("productId", p.getProductId());
                            intent.putExtra("userId", productData.getUserId());
                            intent.putExtra("productId", p.getProductId());
                            intent.putExtra("productName", p.getProductName());
                            intent.putExtra("productPrice", p.getProductPrice());
                            intent.putExtra("productInfo", p.getProductInfo());
                            intent.putExtra("productId", p.getProductId());
                            intent.putExtra("rankAverage", p.getRankAverage());
                            intent.putExtra("numsReview", p.getNumsReview());
                            startActivity(intent);
                        }
                    });
                }



            }
        }
    }



    private boolean isItemMatching(String itemName, String queryItem) {
        if (itemName.length() < 3 || queryItem.length() < 3) {
            return false;
        }

        itemName = itemName.toLowerCase();
        queryItem = queryItem.toLowerCase();

        for (int i = 0; i <= itemName.length() - 3; i++) {
            for (int j = 0; j <= queryItem.length() - 3; j++) {
                if (itemName.charAt(i) == queryItem.charAt(j) && itemName.charAt(i + 1) == queryItem.charAt(j + 1)
                        && itemName.charAt(i + 2) == queryItem.charAt(j + 2)) {
                    return true;
                }
            }
        }

        return false;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    private void getImageByPath(String imagePath, BitmapCallback callback) {
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);
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

    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String fulladdres = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                fulladdres = address.getSubLocality()+" "+address.getThoroughfare();



                // 상세 주소 및 다른 정보 활용
                // 예: textView.setText(addressLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fulladdres;
    }

}