package info.jeonju.stylerent.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ImagePathRequest;
import info.jeonju.stylerent.auth.ProductModels.ProductImage;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.fragments.BitmapCallback;
import info.jeonju.stylerent.userdata.OtherUserDataResponse;
import info.jeonju.stylerent.userdata.ProductDataResponse;
import info.jeonju.stylerent.userdata.RelProductAdapter;
import info.jeonju.stylerent.userdata.RelProductAdapterModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Relative_user_profile extends AppCompatActivity implements RelProductAdapter.RelProductEventListener {
    SharedPreferences sharedPreferences;

    List<RelProductAdapterModel> relProductAdapterModels = new ArrayList<>();

    List<ProductDataResponse> productDataResponses = new ArrayList<>();
    private OtherUserDataResponse otherUserDataResponse;

    Integer productId;
    Integer userId;

    CircleImageView userProfile;

    TextView userName;

    RatingBar userRank;
    LinearLayout ratingLinearLayout;

    ListView productListView;

    private RelProductAdapter relProductAdapter;

    ImageButton backBtn;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_relative_user_profile);

        productListView = findViewById(R.id.relUserItemListView);


        backBtn = findViewById(R.id.relative_back_btn);
        userName = findViewById(R.id.userName); // userName TextView 초기화
        userProfile = findViewById(R.id.userImage); // userProfile CircleImageView 초기화
        userRank = findViewById(R.id.ratingBar); // userRating RatingBar 초기화
        ratingLinearLayout = findViewById(R.id.ratingLinearLayout);
        productId = getIntent().getIntExtra("productId", -1);
        userId = getIntent().getIntExtra("userId", -1);

        ratingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked-------");
                Intent intent = new Intent(getApplicationContext(), OtherReviewActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });




        getProductData(userId);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void getProductData(Integer userId) {
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        System.out.println("user Id -->>> " + userId);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(Relative_user_profile.this).create(ApiInterface.class);
        Call<OtherUserDataResponse> getOtherUserDataCall = apiInterface.getOtherUserData("Bearer" + TOKEN, userId);
        getOtherUserDataCall.enqueue(new Callback<OtherUserDataResponse>() {
            @Override
            public void onResponse(Call<OtherUserDataResponse> call, Response<OtherUserDataResponse> response) {
                System.out.println("성공");
                otherUserDataResponse = response.body();
                updateData(response.body());
            }

            @Override
            public void onFailure(Call<OtherUserDataResponse> call, Throwable t) {
                System.out.println("실패");
            }
        });


    }


    private void updateData(OtherUserDataResponse userData) {
        String userNameValue = userData.getUserName();
        String profileImageValue = userData.getprofileImage();
        Double rankAverageValue = userData.getRankAverage();
        List<ProductDataResponse> productsValue = userData.getProducts();

        userName.setText(userNameValue);

        System.out.println("기존 평점"+userData.getRankAverage());

        float initialRating = (rankAverageValue != null) ? rankAverageValue.floatValue() : 0.0f;
        userRank.setRating(initialRating);
        userRank.setIsIndicator(true);

        productDataResponses = productsValue;


        if (profileImageValue != null) {
            byte[] image = Base64.decode(profileImageValue, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            userProfile.setImageBitmap(bitmap);
        } else {
            userProfile.setBackgroundResource(R.drawable.ic_user);
        }

        relProductAdapterModels.clear();
        for (ProductDataResponse p : productsValue) {
            relProductAdapterModels.add(new RelProductAdapterModel(p.getProductId(), p.getProductName(), p.getProductPrice(), p.getRentStatus()));
        }

        relProductAdapter = new RelProductAdapter(this, relProductAdapterModels);
        productListView.setAdapter(relProductAdapter);
        relProductAdapter.setRelProductEventListener(this);
        relProductAdapter.notifyDataSetChanged();

        getImage();
    }

    private void getImage() {
        List<List<Bitmap>> bitmapLists = new ArrayList<>();

        for (ProductDataResponse p : productDataResponses) {
            System.out.println(p.getImagePath());

            List<ProductImage> productImages = p.getImagePath();
            List<Bitmap> bitmapList = new ArrayList<>();

            for (ProductImage image : productImages) {
                getImageByPath(image.getPath(), new BitmapCallback() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        bitmapList.add(bitmap);

                        if (bitmapList.size() == productImages.size()) {
                            // All bitmaps are loaded, add them to the MyProductAdapterModel object
                            relProductAdapterModels.get(productDataResponses.indexOf(p)).setProductImages(productImages);
                            relProductAdapterModels.get(productDataResponses.indexOf(p)).setImageBitmaps(bitmapList);

                            // Update the adapter's data and notify the adapter of the changes
                            relProductAdapter.setMyProducts(relProductAdapterModels);
                            relProductAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Handle the failure here
                    }
                });
            }

            bitmapLists.add(bitmapList);
        }
    }

    private void getImageByPath(String imagePath, BitmapCallback callback) {
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(Relative_user_profile.this).create(ApiInterface.class);
        ImagePathRequest imagePathRequest = new ImagePathRequest(imagePath);
        Call<ResponseBody> userProfileCall = apiInterface.getImageByPath("Bearer " + TOKEN, imagePathRequest);

        userProfileCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("d이미지 가져오기 성공");
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        callback.onBitmapLoaded(bitmap);
                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new Exception("Failed to load image"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    @Override
    public void onEventListener(int position) {
        openCurrentProduct(position);
    }

    private void openCurrentProduct(Integer position){
        System.out.println(position);
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productId", relProductAdapterModels.get(position).getProductId());
        intent.putExtra("userId", otherUserDataResponse.getUserId());
        intent.putExtra("userName", otherUserDataResponse.getUserName());
        intent.putExtra("productName", relProductAdapterModels.get(position).getProductName());
        intent.putExtra("productPrice", relProductAdapterModels.get(position).getProductPrice());
        intent.putExtra("productInfo", relProductAdapterModels.get(position).getProductInfo());
        intent.putExtra("productId", relProductAdapterModels.get(position).getProductId());
        startActivity(intent);
    }
}