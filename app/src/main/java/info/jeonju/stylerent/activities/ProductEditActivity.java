package info.jeonju.stylerent.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ProductModels.ProductDataResponse;
import info.jeonju.stylerent.auth.ProductModels.ProductImageBases;
import info.jeonju.stylerent.auth.ProductModels.ProductImageResponse;
import info.jeonju.stylerent.auth.ProductModels.ProductInfoRequest;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.auth.UserNameUpdateResponse;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductEditActivity extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    List<ProductImageBases> productImageViewList = new ArrayList<>(); // DI - Dependency Inversion Principle;
    SharedPreferences sharedPreferences;
    LinearLayout productLayout;
    Integer productId;


    ImageButton backbtn, addProductBtn;

    Button savebtn;

    EditText name;
    EditText category;

    EditText price;

    EditText description;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        productId = getIntent().getIntExtra("productId", -1);

        backbtn = findViewById(R.id.back_btn);

        savebtn = findViewById(R.id.save_btn);

        addProductBtn = findViewById(R.id.addProduct_btn);

        name = findViewById(R.id.name_etv);

        price = findViewById(R.id.price_etv);

        description = findViewById(R.id.description_etv);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        if(productId != -1){
            getProductInfo(false);
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveBtnClick();
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProductEditActivity.this)
                        .crop()  //Crop image(Optional), Check Customization for more option
                        .compress(254) //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(700, 700)//Final image resolution will be less than 1080 x 1080(Optional)
                        .cropSquare()
                        .start();
            }
        });


//        category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                PopupMenu categoryMenu = new PopupMenu(NewProductActivity.this, category); //두 번째 파라미터가 팝업메뉴가 붙을 뷰
//                categoryMenu.getMenuInflater().inflate(R.menu.product_category_menu, categoryMenu.getMenu());
//
//                categoryMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        int clickedId = menuItem.getItemId();
//                        CharSequence clickedTitle = menuItem.getTitle();
//                        handleMenuClick(clickedId, clickedTitle);
//                        return false;
//                    }
//                });
//                categoryMenu.show();
//
//
//            }
//        });


    }


    void handleSaveBtnClick() {

        if(productId != -1) {
//            //사진

            if(name.getText().toString().isEmpty()){
                Toast.makeText(ProductEditActivity.this, "please enter product name! ", Toast.LENGTH_SHORT).show();
            }else if(price.getText().toString().isEmpty()){
                Toast.makeText(ProductEditActivity.this, "please enter product Price! ", Toast.LENGTH_SHORT).show();
            }else if(description.getText().toString().isEmpty()){
                Toast.makeText(ProductEditActivity.this, "please enter product Description! ", Toast.LENGTH_SHORT).show();
            } else if (productImageViewList.size() == 0) {
                Toast.makeText(ProductEditActivity.this, "Please add product Image! ", Toast.LENGTH_SHORT).show();
            } else{
                String productName = name.getText().toString();
                String productPrice = price.getText().toString();
                System.out.println("product price ->" + productPrice);
                String productDescription = description.getText().toString();
                ProductInfoRequest newProduct = new ProductInfoRequest(productId, productName, productPrice, productDescription);
                // 옷장 정보 추가 post 요청
                updateProductInfo(newProduct);
            }
        }
    }


    private void updateProductInfo(ProductInfoRequest productInfoRequest){
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(ProductEditActivity.this).create(ApiInterface.class);
        Call<UserNameUpdateResponse> addInfoApi = apiInterface.updateProductInfo("Bearer" + TOKEN, productInfoRequest);
        addInfoApi.enqueue(new Callback<UserNameUpdateResponse>() {
            @Override
            public void onResponse(Call<UserNameUpdateResponse> call, Response<UserNameUpdateResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getError() == null){
                        UserNameUpdateResponse productInfoResponse = response.body();
                        System.out.println("Product info successfully added");
                        Toast.makeText(ProductEditActivity.this, "옷장 정보 추가되었습니다 ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserNameUpdateResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to set product info: " + t.getMessage());

            }
        });
    }


    private void getProductInfo(Boolean updateImage){
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(ProductEditActivity.this).create(ApiInterface.class);
        Call<ProductDataResponse> addInfoApi = apiInterface.getProductInfo("Bearer" + TOKEN, productId);
        System.out.println("get product info -->" + productId);
        addInfoApi.enqueue(new Callback<ProductDataResponse>() {
            @Override
            public void onResponse(Call<ProductDataResponse> call, Response<ProductDataResponse> response) {
                if (response.isSuccessful()) {
                    if(updateImage){
                        ProductDataResponse productDataResponse1 = response.body();
                        productImageViewList.clear();
                        productImageViewList = productDataResponse1.getImagePath();
                        showProductImage();
                    }else{
                        ProductDataResponse productDataResponse1 = response.body();
                        System.out.println("get product name -->" + productDataResponse1.getProductName());
                        productImageViewList.clear();
                        productImageViewList = productDataResponse1.getImagePath();


                        name.setText(productDataResponse1.getProductName());
                        price.setText(productDataResponse1.getProductPrice());
                        description.setText(productDataResponse1.getProductInfo());
                        showProductImage();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductDataResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to set product info: " + t.getMessage());

            }
        });
    }

//    void handleMenuClick(int categoryId, CharSequence categoryTitle) {
//        switch (categoryId) {
//            case R.id.menu_top:
//                Toast.makeText(NewProductActivity.this, "top", Toast.LENGTH_SHORT).show();
//                category.setText(categoryTitle);
//                break;
//
//            case R.id.menu_bottom:
//                Toast.makeText(NewProductActivity.this, "bottom", Toast.LENGTH_SHORT).show();
//                category.setText(categoryTitle);
//                break;
//
//            case R.id.menu_shoes:
//                Toast.makeText(NewProductActivity.this, "shoes", Toast.LENGTH_SHORT).show();
//                category.setText(categoryTitle);
//                break;
//
//            case R.id.menu_bag:
//                Toast.makeText(NewProductActivity.this, "bag", Toast.LENGTH_SHORT).show();
//                category.setText(categoryTitle);
//                break;
//            case R.id.menu_etc:
//                Toast.makeText(NewProductActivity.this, "etc...", Toast.LENGTH_SHORT).show();
//                category.setText(categoryTitle);
//                break;
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (productImageViewList.size() < 4) {
                // 이미지 추가 post 요청
                Uri uri = data.getData(); // Image data
                File imagePath = new File(data.getData().getPath());
                productImagePostRequest(uri, imagePath);
            } else {
                // Button disable
                System.out.println("too much!");
            }
        }
    }

    private void productImagePostRequest(Uri uri, File imagePath){
        //i want here make api request to server to upload image that I picked
        if(uri != null && imagePath != null){

            String TOKEN = sharedPreferences.getString("TOKEN", null);

            System.out.println("---- Image request Body " + imagePath);
            // Create a RequestBody from the image File object
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), imagePath);
            // Call the API using Retrofit
            ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(ProductEditActivity.this).create(ApiInterface.class);

            // Create a MultipartBody.Part from the RequestBody
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", imagePath.getName(), imageBody);

            Call<ProductImageResponse> call = apiInterface.uploadProductImage("Bearer" + TOKEN, productId, imagePart);

            call.enqueue(new Callback<ProductImageResponse>() {
                @Override
                public void onResponse(Call<ProductImageResponse> call, Response<ProductImageResponse> response) {
                    // Handle successful response
                    getProductInfo(true);
                    Toast.makeText(ProductEditActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ProductImageResponse> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(ProductEditActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(ProductEditActivity.this, "Profile image has not been changed", Toast.LENGTH_SHORT).show();
        }
    }


    private void showProductImage() {
        ImageButton productImage;
        productLayout = findViewById(R.id.productImageLayout);
        productLayout.removeAllViews();

        int numberOfImages = 0;
        if(productImageViewList != null){
            numberOfImages = productImageViewList.size();
        }

        String imageCountText = numberOfImages + "/4";

        TextView numberOfPicTextView = findViewById(R.id.number_of_pic);
        numberOfPicTextView.setText(imageCountText);

        final FrameLayout[] firstImageContainer = {null};

        for (int i = 0; i < numberOfImages; i++) {
            final int position = i;

            FrameLayout imageContainer = new FrameLayout(ProductEditActivity.this); // Make imageContainer final
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageContainer.setLayoutParams(frameParams);

            productImage = new ImageButton(ProductEditActivity.this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx(140), dpToPx(140));
            productImage.setLayoutParams(imageParams);
            String image64 = productImageViewList.get(i).getPath();
            // productimage
            byte[] image = Base64.decode(image64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            int desiredWidth = dpToPx(60);
            int desiredHeight = dpToPx(60);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, false);

            BitmapDrawable drawable = new BitmapDrawable(getResources(), resizedBitmap);
            productImage.setImageDrawable(drawable);


            imageParams.height = dpToPx(60);
            imageParams.width = dpToPx(60);
            imageParams.setMargins(dpToPx(8), 0, dpToPx(0), 0);


            ImageView removeIcon = new ImageView(ProductEditActivity.this);
            FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(50, 50);
            iconParams.gravity = Gravity.END | Gravity.TOP;
            iconParams.setMargins(-dpToPx(10), -dpToPx(10), 0, 0);
            removeIcon.setLayoutParams(iconParams);
            removeIcon.setImageResource(R.drawable.ic_close_black); // Set the close icon image

            imageContainer.addView(productImage);
            imageContainer.addView(removeIcon);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.TOP | Gravity.END;
            params.setMargins(0, -6, -9, 0); // Adjust the margin to control the positioning of the icon
            removeIcon.setLayoutParams(params);


            int finalI = i;
            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImage(productImageViewList.get(finalI).getImageId());
//                    productImageViewList.remove(position);
//                    productLayout.removeView(imageContainer);
//
//                    if (firstImageContainer[0] != null) {
//                        TextView firstText = (TextView) firstImageContainer[0].getChildAt(1);
//                        if (firstText != null) {
//                            firstImageContainer[0].removeView(firstText);
//                            if (productLayout.getChildCount() > 0) {
//                                View firstChild = productLayout.getChildAt(0);
//                                if (firstChild instanceof FrameLayout) {
//                                    FrameLayout newFirstImageContainer = (FrameLayout) firstChild;
//                                    newFirstImageContainer.addView(firstText);
//                                }
//                            }
//                        }
//                    }
//
//                    showProductImage(); // Call showProductImage() after deleting the photo
                }
            });


            productLayout.addView(imageContainer);

//            if (i == 0) {
//                TextView representativeText = new TextView(NewProductActivity.this);
//                FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                textParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
//                textParams.setMargins(0, 0, 0, 16); // Add bottom margin to separate text from the image
//                representativeText.setLayoutParams(textParams);
//                representativeText.setText("대표 사진"); // Set the text for the representative image
//                representativeText.setTextColor(Color.WHITE); // Set the text color to white
//                imageContainer.addView(representativeText);
//                firstImageContainer[0] = imageContainer;
//            }
        }
    }

    private void removeImage(Integer imageId){
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(ProductEditActivity.this).create(ApiInterface.class);
        Call<UserNameUpdateResponse> addInfoApi = apiInterface.deleteProductImage("Bearer" + TOKEN, imageId);
        addInfoApi.enqueue(new Callback<UserNameUpdateResponse>() {
            @Override
            public void onResponse(Call<UserNameUpdateResponse> call, Response<UserNameUpdateResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getError() == null){
                        getProductInfo(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserNameUpdateResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to set product info: " + t.getMessage());

            }
        });
    }

    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}