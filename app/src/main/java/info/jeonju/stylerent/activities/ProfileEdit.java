package info.jeonju.stylerent.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.auth.UserNameRequest;
import info.jeonju.stylerent.auth.UserNameUpdateResponse;
import info.jeonju.stylerent.userdata.ImageResponse;
import info.jeonju.stylerent.userdata.UserProfileResponse;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEdit extends AppCompatActivity {

    private ImageView imageView;
    private FloatingActionButton Imagebutton;
    EditText usernameText;
//    private List<ProductImageView> imagesList; // image1, image2, image3
    Button savebutton;
    Uri uri;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    File imagePath;

    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);



        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Objects.requireNonNull(getSupportActionBar()).setSplitBackgroundDrawable(new ColorDrawable(getColor(R.color.black)));
        }

        imageView = findViewById(R.id.userImage);
        Imagebutton = findViewById(R.id.floatingActionButton);
        savebutton = findViewById(R.id.savebutton);
        usernameText = findViewById(R.id.userNameET);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        getUserData();


        Imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileEdit.this)
                        .crop()  //Crop image(Optional), Check Customization for more option
                        .compress(512) //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(140, 140)//Final image resolution will be less than 1080 x 1080(Optional)
                        .cropSquare()
                        .start();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //i want here make api request to server to upload image that I picked
                if(uri != null && imagePath != null){

                    String TOKEN = sharedPreferences.getString("TOKEN", null);

                    System.out.println("---- Image request Body " + imagePath);
                    // Create a RequestBody from the image File object
                    RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), imagePath);
                    // Call the API using Retrofit
                    ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(ProfileEdit.this).create(ApiInterface.class);

                    // Create a MultipartBody.Part from the RequestBody
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imagePath.getName(), imageBody);

                    Call<ImageResponse> call = apiInterface.uploadImage("Bearer" + TOKEN, imagePart);

                    call.enqueue(new Callback<ImageResponse>() {
                        @Override
                        public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                            // Handle successful response
                            Toast.makeText(ProfileEdit.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            updateUsername(usernameText.getText().toString());

//                            switch with UserFragment
//                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                                transaction.replace(R.id.fragments, userFragment);
//                                transaction.addToBackStack(null);
//                                transaction.commit();


                        }

                        @Override
                        public void onFailure(Call<ImageResponse> call, Throwable t) {
                            // Handle failure
                            Toast.makeText(ProfileEdit.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(ProfileEdit.this, "Profile image has not been changed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateUsername(String usernameEt){
        UserNameRequest userNameRequest = new UserNameRequest(usernameEt);
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);
        Call<UserNameUpdateResponse> userProfileCall = apiInterface.updateUserName("Bearer" + TOKEN, userNameRequest);
        userProfileCall.enqueue(new Callback<UserNameUpdateResponse>() {
            @Override
            public void onResponse(Call<UserNameUpdateResponse> call, Response<UserNameUpdateResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getError() == null){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserNameUpdateResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to get user data: " + t.getMessage());

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
//            imagesList.add(new ProductImageView(data.getData(), new File(data.getData().getPath())));
            uri = data.getData(); // Image data
            imagePath = new File(data.getData().getPath());
//            System.out.println(data.getData().getPath());
            imageView.setImageURI(uri);
            // start rebuild view image function
//            imageViewLayout();
        }
    }

    private void getUserData() {

        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);
        Call<UserProfileResponse> userProfileCall = apiInterface.getUserProfile("Bearer" + TOKEN);
        userProfileCall.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    UserProfileResponse userProfileResponse = response.body();
                    //user Image
                    if (userProfileResponse.getImageResponse() != null && userProfileResponse.getImageResponse().getImageByte() != null) {
                        byte[] image = Base64.decode(userProfileResponse.getImageResponse().getImageByte(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                        imageView.setImageBitmap(bitmap);
                    }
                    usernameText.setText(userProfileResponse.getUsername());

                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to get user data: " + t.getMessage());

            }
        });
    }



    public void backButtonOnClick(View view) {
        finish();
    }
}