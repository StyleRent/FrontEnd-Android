package info.jeonju.stylerent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.LoginRequest;
import info.jeonju.stylerent.auth.LoginResponse;
import info.jeonju.stylerent.auth.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button btnlogin;
    private TextView registT;
    private EditText emailText, passwordText;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registT = findViewById(R.id.registerTextview);
        btnlogin = findViewById((R.id.login_button));
        emailText = findViewById(R.id.email_edit_text);
        passwordText = findViewById(R.id.password_edit_text);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


//        token 확인
        String TOKEN = sharedPreferences.getString("TOKEN", null);
         if(TOKEN != null){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
      }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnloginClicked();
            }
        });

        registT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

//    private void checkToken(String token){
//        // check token is valid
//        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(MainActivity.this).create(ApiInterface.class);
//        Call<UserProfileResponse> call = apiInterface.getUserProfile(token);
//        call.enqueue(new Callback<UserProfileResponse>() {
//            @Override
//            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
//                if(response.code() == 403){
//                    Toast.makeText(MainActivity.this, "Token is not valid", Toast.LENGTH_SHORT).show();
//                }else if(response.isSuccessful()){
//                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: "+ t.getMessage());
//                Toast.makeText(MainActivity.this, "Incorrect Email or password", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//    }

    private void btnloginClicked() {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else{


            //Login Request
            //You create an instance of the interface you defined in step 1 using the retrofit.create() method. This instance represents the API service you will use to make network requests.
            //You handle the response using a callback or by returning a Call object. Retrofit uses the Call object to asynchronously make the network request and return the response. You can use the enqueue() method to handle the response asynchronously, or you can use the execute() method to make the request synchronously.
            ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(MainActivity.this).create(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.getLoginInformation(new LoginRequest(email, password));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if(response.body() == null){
                        Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(MainActivity.this, "Logged in successul", Toast.LENGTH_SHORT).show();

                        //when login is success -> get user TOKEN (email,password data) and Save on shared preferences with key TOKEN

                        String TOKEN = response.body().getToken();
                        Log.e(TAG, "onResponse: token->" + TOKEN);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("TOKEN", TOKEN);
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    Log.e(TAG, "onResponse: "+ response.code());
                    Log.e(TAG, "onResponse: "+ response.body());
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: "+ t.getMessage());
                    Toast.makeText(MainActivity.this, "Incorrect Email or password", Toast.LENGTH_SHORT).show();
                }

                });
            }
        }


}
