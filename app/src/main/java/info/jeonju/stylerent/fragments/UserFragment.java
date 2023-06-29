package info.jeonju.stylerent.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import info.jeonju.stylerent.activities.MainActivity;
import info.jeonju.stylerent.activities.NewProductActivity;
import info.jeonju.stylerent.activities.ProfileEdit;
import info.jeonju.stylerent.R;
import info.jeonju.stylerent.activities.ReviewListActivity;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ChattingModels.RentResponse;
import info.jeonju.stylerent.auth.ProductModels.MyProductAdapterModel;
import info.jeonju.stylerent.auth.ProductModels.ProductInitResponse;
import info.jeonju.stylerent.auth.RetrofitClient;
import info.jeonju.stylerent.userdata.ProductAdapter;
import info.jeonju.stylerent.userdata.ProductDataResponse;
import info.jeonju.stylerent.userdata.UserProfileResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment implements ProductAdapter.RemoveProductListener {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    TextView userName;
    List<ProductDataResponse> productDataResponses = new ArrayList<>();
    List<MyProductAdapterModel> myProductAdapterModels = new ArrayList<>();
    ImageView userImage;
    ImageButton plusBtn;
    Button logoutbtn, editButton, reviewBtn;
    private ProductAdapter productAdapter;
    private ListView productListView;
    LinearLayout ratingLayout;
    RatingBar ratingBar;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_user, container, false);
        userName = view.findViewById(R.id.userName);
        userImage = view.findViewById(R.id.userImage);
        editButton = view.findViewById(R.id.editButton);
        plusBtn = view.findViewById(R.id.plusBtn);
        ratingLayout = view.findViewById(R.id.ratingLayout);
        ratingBar = view.findViewById(R.id.ratingBar);

        //run adapter
        productListView = view.findViewById(R.id.userItemListView);


        logoutbtn = view.findViewById(R.id.logout_btn);

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewListActivity.class);
                startActivity(intent);
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("TOKEN");
                editor.commit();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        getUserData(false);
//
//        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteProductRequest();
//            }
//        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEdit.class);
                startActivity(intent);
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Post request -> 옷장 추가 데이터 정의
                productInitRequest();
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        // Perform any necessary operations when the fragment resumes
        // For example, you can update the UI or reload data

        // Call the method to refresh the user data
        getUserData(true);
    }




    private void getUserData(Boolean update) {

        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<UserProfileResponse> userProfileCall = apiInterface.getUserProfile("Bearer" + TOKEN);
        userProfileCall.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    UserProfileResponse userProfileResponse = response.body();
                    if(update){

                        // get products list
                        myProductAdapterModels.clear();
                        productDataResponses.clear();
                        productDataResponses = userProfileResponse.getProducts();

                        for(ProductDataResponse p : productDataResponses){
                            myProductAdapterModels.add(new MyProductAdapterModel(p.getProductId(), p.getProductName(), p.getProductPrice(), p.getRentStatus(), p.getProductImage(), p.getRenterId(), TOKEN));
                        }

                        //create the custom adapter
                        //create the custom adapter
                        productAdapter = new ProductAdapter(getContext(), myProductAdapterModels);
                        productAdapter.setRemoveProductListener(UserFragment.this);

                        productListView.setAdapter(productAdapter);
                        if(productAdapter != null){
                            productAdapter.notifyDataSetChanged();
                            // generate adapter model
                        }
                    }else{
                        //user name
                        userName.setText(userProfileResponse.getUsername());
                        float rankFloat = (float) userProfileResponse.getAverageRank();
                        ratingBar.setRating(rankFloat);


                        //user Image
                        if (userProfileResponse.getImageResponse() != null && userProfileResponse.getImageResponse().getImageByte() != null) {
                            byte[] image = Base64.decode(userProfileResponse.getImageResponse().getImageByte(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                            userImage.setImageBitmap(bitmap);
                        }


                        // get products list
                        myProductAdapterModels.clear();
                        productDataResponses.clear();
                        productDataResponses = userProfileResponse.getProducts();

                        for(ProductDataResponse p : productDataResponses){
                            myProductAdapterModels.add(new MyProductAdapterModel(p.getProductId(), p.getProductName(), p.getProductPrice(), p.getRentStatus(), p.getProductImage(), p.getRenterId(), TOKEN));
                        }

                        // generate adapter model
                    }

                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to get user data: " + t.getMessage());

            }
        });
    }





    private void productInitRequest(){
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<ProductInitResponse> userProfileCall = apiInterface.productInitRequest("Bearer" + TOKEN);
        userProfileCall.enqueue(new Callback<ProductInitResponse>() {
            @Override
            public void onResponse(Call<ProductInitResponse> call, Response<ProductInitResponse> response) {
                if (response.isSuccessful()) {
                    ProductInitResponse productInitResponse = response.body();
                    // Handle successful user profile response
                    if(productInitResponse != null){
                        System.out.println("Product Init --->" + productInitResponse.getCurrentProductId());
                        Intent intent = new Intent(getActivity(), NewProductActivity.class);
                        intent.putExtra("productid", productInitResponse.getCurrentProductId());
                        startActivity(intent);
                    }



                }
            }

            @Override
            public void onFailure(Call<ProductInitResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to get user data: " + t.getMessage());
            }
        });
    }


    private void deleteProductRequest(Integer position){
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<RentResponse> userProfileCall = apiInterface.removeProduct("Bearer" + TOKEN, position);
        System.out.println("Call delete request!!!-----....");
        userProfileCall.enqueue(new Callback<RentResponse>() {
            @Override
            public void onResponse(Call<RentResponse> call, Response<RentResponse> response) {
                if(response.body().getError() == null){
                    System.out.println("successs delete!!!-----....");
                    Toast.makeText(getContext(), "Product successfully Deleted.", Toast.LENGTH_SHORT).show();
                    getUserData(true);
                }
            }

            @Override
            public void onFailure(Call<RentResponse> call, Throwable t) {
                // Handle network failure
                System.out.println("Error delete!!!-----....");
                Log.e(TAG, "Failed to delete product data: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to delete product data.", Toast.LENGTH_SHORT).show();

            }
        });
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.topnavbaruser);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public void onRemoveProduct(int position) {
        System.out.println("on remove product started ----!!!-----....");
        deleteProductRequest(productDataResponses.get(position).getProductId());
    }
}