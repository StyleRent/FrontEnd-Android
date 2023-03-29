package com.example.stylerent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylerent.auth.ApiInterface;
import com.example.stylerent.auth.LoginRequest;
import com.example.stylerent.auth.LoginResponse;
import com.example.stylerent.auth.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private Button btnlogin;
    private TextView registT, emailText, passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registT = findViewById(R.id.registerTextview);
        btnlogin = findViewById((R.id.login_button));

        emailText = findViewById(R.id.email_edit_text);
        passwordText = findViewById(R.id.password_edit_text);


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
            }
        });
    }

    private void btnloginClicked() {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Email!!", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Password!!", Toast.LENGTH_SHORT).show();
        }else{

            // Login Request
            Call<LoginResponse> call = apiInterface.getLoginInformation(new LoginRequest(email, password));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.body() == null){
                        Toast.makeText(MainActivity.this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Log in successful!", Toast.LENGTH_SHORT).show();

                        // Save current Token and Open HomeActivity
                    }
                    Log.e(TAG, "onResponse: "+response.code() );
                    Log.e(TAG, "onResponse: "+ response.body());
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: "+ t.getMessage() );
                    Toast.makeText(MainActivity.this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
