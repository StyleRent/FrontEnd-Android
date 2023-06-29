package info.jeonju.stylerent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.Rank.MyReviewListAdapter;
import info.jeonju.stylerent.auth.Rank.ReviewListModel;
import info.jeonju.stylerent.auth.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherReviewActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    List<ReviewListModel> reviewListModelList = new ArrayList<>();
    MyReviewListAdapter myReviewListAdapter;
    private ListView reviewList;
    ImageButton imageButtonBack;
    Integer userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_review);
        reviewList = findViewById(R.id.myReviewListView);
        imageButtonBack = findViewById(R.id.imageButtonBack);
        userId = getIntent().getIntExtra("userId", -1);


        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        getMyReview();
    }

    private void getMyReview(){
        if(userId != -1){
            String TOKEN = sharedPreferences.getString("TOKEN", null);
            ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);
            Call<List<ReviewListModel>> getNearByDataRequest = apiInterface.getReview("Bearer " + TOKEN, userId);
            getNearByDataRequest.enqueue(new Callback<List<ReviewListModel>>() {
                @Override
                public void onResponse(Call<List<ReviewListModel>> call, Response<List<ReviewListModel>> response) {
                    System.out.println("REsults ------>>>>>>");

                    myReviewListAdapter = new MyReviewListAdapter(response.body(), getApplication());
                    reviewList.setAdapter(myReviewListAdapter);
                }

                @Override
                public void onFailure(Call<List<ReviewListModel>> call, Throwable t) {

                }
            });
        }else {
            System.out.println("Cannot get user id");
        }
    }
}