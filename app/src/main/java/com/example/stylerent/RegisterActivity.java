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
import com.example.stylerent.auth.RegisterRequest;
import com.example.stylerent.auth.RegisterResponse;
import com.example.stylerent.auth.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Button btnregister;
    private TextView usernameText, emailText, passwordText, loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginText = findViewById(R.id.loginTextview);

        btnregister = findViewById(R.id.registerBtn1);

        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        passwordText = findViewById(R.id.password_text);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnregisterClicked();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void btnregisterClicked() {

        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Username!!", Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Email!!", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Password!!", Toast.LENGTH_SHORT).show();
        }else {

            // Registration request
            ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
            Call<RegisterResponse> call = apiInterface.getRegisterInformation(new RegisterRequest(username, email, password));
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body().getError() != null) {
                        Toast.makeText(RegisterActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registartion successful: Your Token->" + response.body().getToken(), Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG, "onResponse: " + response.code());
                    Log.e(TAG, "onResponse: " + response.body());
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }
}