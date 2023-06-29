package info.jeonju.stylerent.activities;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ChattingModels.MessageInit;
import info.jeonju.stylerent.auth.ChattingModels.MessageInitResponse;
import info.jeonju.stylerent.auth.ImagePathRequest;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.fragments.BitmapCallback;
import info.jeonju.stylerent.userdata.ItemProductImageResponse;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    Integer productId;
    Integer userId;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;


    Toolbar toolBar;
    ImageButton backBtn;
    ImageButton chat_btn;
    RatingBar ratingBar;

    TextView reviewtextView;

    TextView userName;
    TextView productName;
    TextView productPrice;
    TextView productInfo;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolBar = findViewById(R.id.detail_navbar);
        backBtn = findViewById(R.id.back_btn);
        chat_btn = findViewById(R.id.chat_btn);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userName = findViewById(R.id.userName);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productInfo = findViewById(R.id.productInfo);
        ratingBar = findViewById(R.id.ratingBar);
        reviewtextView = findViewById(R.id.reviewtextView);



        productId = getIntent().getIntExtra("productId", -1);
        userId = getIntent().getIntExtra("userId", -1);

        float ratingReview = (float) getIntent().getDoubleExtra("rankAverage", -1);
        if(ratingReview == -1){
            ratingBar.setVisibility(View.INVISIBLE);
        }
        ratingBar.setRating(ratingReview);

        Integer numsReviews = getIntent().getIntExtra("numsReview", -1);

        if(numsReviews == -1){
            reviewtextView.setText("Review 0");
        }else{
            reviewtextView.setText("Review " + numsReviews);
        }

        String userName = getIntent().getStringExtra("userName");
        String productName = getIntent().getStringExtra("productName");
        String productPrice = getIntent().getStringExtra("productPrice");
        String productInfo = getIntent().getStringExtra("productInfo");

        updateData(userName, productName, productPrice, productInfo);
        getProductImage(productId);

        // chatting button
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 채팅 정의
                initChatRequest();
            }
        });

        toolBar.setTitle("");
        setSupportActionBar(toolBar);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reviewtextView = findViewById(R.id.reviewtextView);
        reviewtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, ReviewListActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }



    private void getProductImage(Integer productId) {
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        System.out.println("product Id -->>> " + productId);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(ProductDetailActivity.this).create(ApiInterface.class);
        Call<List<ItemProductImageResponse>> userProfileCall = apiInterface.findProductImage("Bearer" + TOKEN, productId);
        userProfileCall.enqueue(new Callback<List<ItemProductImageResponse>>() {
            @Override
            public void onResponse(Call<List<ItemProductImageResponse>> call, Response<List<ItemProductImageResponse>> response) {
                if (response.isSuccessful()) {
//                    List<ItemProductImageResponse> imageResponses = response.body();
                    showImages(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<ItemProductImageResponse>> call, Throwable t) {

            }
        });
        // path1 - image1.jpg , path2 -  image2.jpg

        // request result body -> success -> call function

        //showImages(response.body()) // List<ItemProductImageResponse>


        // download by path


    }

    private ImageView mainImage; // 메인 이미지를 저장할 변수 추가

    private void showImages(List<ItemProductImageResponse> imageList) {
        int imageCount = imageList.size();
        int maxImages = 4; // 최대 이미지 개수
        int displayCount = Math.min(imageCount, maxImages); // 표시할 이미지 개수

        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        LinearLayout parentLayout2 = findViewById(R.id.parentLayout2);

        // 첫 번째 이미지는 product_main과 product_first에 표시됩니다.
        if (imageCount > 0) {
            mainImage = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mainImage.setLayoutParams(params);
            mainImage.setScaleType(ImageView.ScaleType.FIT_XY); // 이미지를 가득 채우기 위해 추가
            parentLayout.addView(mainImage);


            getImageByPath(imageList.get(0).getPath(), new BitmapCallback() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    mainImage.setImageBitmap(bitmap);
                }

                @Override
                public void onFailure(Throwable t) {
                    // 이미지 로딩 실패 시 처리할 내용을 작성합니다.
                }
            });

            ImageView firstImage = new ImageView(this);
            LinearLayout.LayoutParams firstParams = new LinearLayout.LayoutParams(dpToPx(100), ViewGroup.LayoutParams.WRAP_CONTENT);
            firstParams.weight = 1; // 이미지 틀이 같은 크기로 배치되도록 weight를 설정합니다.
            firstParams.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            firstImage.setLayoutParams(firstParams);
            firstImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // 이미지를 가득 채우기 위해 추가
            parentLayout2.addView(firstImage);
            getImageByPath(imageList.get(0).getPath(), new BitmapCallback() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    firstImage.setImageBitmap(bitmap);
                }

                @Override
                public void onFailure(Throwable t) {
                    // 이미지 로딩 실패 시 처리할 내용을 작성합니다.
                }
            });

            // 첫 번째 이미지 클릭 시 main 이미지 변경
            firstImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainImage.setImageBitmap(((BitmapDrawable) firstImage.getDrawable()).getBitmap());
                }
            });
        }

        // 나머지 이미지는 product_second, product_third, product_four에 순서대로 표시됩니다.
        for (int i = 1; i < displayCount; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(100), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            layoutParams.weight = 1; // 이미지 틀이 같은 크기로 배치되도록 weight를 설정합니다.
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // 이미지를 가득 채우기 위해 추가

            parentLayout2.addView(imageView);
            int index = i; // 클로저를 위해 인덱스 값을 final 변수로 저장
            getImageByPath(imageList.get(i).getPath(), new BitmapCallback() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                    // 이미지 클릭 시 main 이미지 변경
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mainImage.setImageBitmap(bitmap);
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    // 이미지 로딩 실패 시 처리할 내용을 작성합니다.
                }
            });
        }

        // 이미지 개수가 표시할 수 있는 이미지 개수보다 적을 경우 나머지 이미지 틀에 기본 이미지를 추가합니다.
        for (int i = displayCount; i < maxImages; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(100), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1; // 이미지 틀이 같은 크기로 배치되도록 weight를 설정합니다.
            layoutParams.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // 이미지를 가득 채우기 위해 추가
            imageView.setImageResource(R.drawable.logo); // 기본 이미지로 설정
            int backgroundColor = Color.parseColor("#F9F5F6"); // 배경색 설정
            imageView.setBackground(getRoundedCornerDrawable(dpToPx(10), backgroundColor)); // 모서리 둥글게 설정
            parentLayout2.addView(imageView);

            // 이미지 클릭 시 main 이미지 변경
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mainImage.setImageBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap());
//                }
//            });
        }
    }

    private Drawable getRoundedCornerDrawable(int radius, int backgroundColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(backgroundColor); // 회색 배경 설정
        drawable.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius}); // 각 모서리의 반지름 설정
        return drawable;
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





    private void updateData(String userName,String productName,String productPrice,String productInfo){
        this.userName.setText(userName);
        this.productName.setText(productName);
        this.productPrice.setText(formatPrice(productPrice));
        this.productInfo.setText(productInfo);
    }

    private String formatPrice(String price) {
        double amount = Double.parseDouble(price);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }

    private void initChatRequest(){
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        MessageInit messageInit = new MessageInit(userId, productId);

//        System.out.println("user Id -->>> " + userId);
//        System.out.println("product Id -->>> " + productId);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(ProductDetailActivity.this).create(ApiInterface.class);
        Call<MessageInitResponse> chatResponseApi = apiInterface.messageInit("Bearer" + TOKEN, messageInit);
        chatResponseApi.enqueue(new Callback<MessageInitResponse>() {
            @Override
            public void onResponse(Call<MessageInitResponse> call, Response<MessageInitResponse> response) {
                if (response.isSuccessful()) {
                    MessageInitResponse messageInitResponse = response.body();
                    if(messageInitResponse.getError() == null){
                        Intent intent = new Intent(getApplication(), MessageActivity.class);
                        intent.putExtra("messageId", messageInitResponse.getMessageId());
                        intent.putExtra("receiverId", messageInitResponse.getReceiverId());
                        intent.putExtra("productId", messageInitResponse.getProductId());
                        intent.putExtra("rentStatus", messageInitResponse.getRentStatus());
                        intent.putExtra("isRentedToMe", messageInitResponse.getRentedToMe());
                        intent.putExtra("myChat", messageInitResponse.getMyChat());
                        intent.putExtra("productImage", messageInitResponse.getProductImage());
                        intent.putExtra("productName", messageInitResponse.getProductName());
                        intent.putExtra("productPrice", messageInitResponse.getProductPrice());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageInitResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to set product info: " + t.getMessage());
                System.out.println("coundt make req");

            }
        });
    }
}